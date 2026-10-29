package com.example.springjte.security

import com.example.springjte.model.User
import com.example.springjte.repository.UserRepository
import com.example.springjte.service.ApiKeyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val repo: UserRepository,
    private val apiKeyService: ApiKeyService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService { email ->
            val user = repo.findByEmail(email)
                ?: throw UsernameNotFoundException("No user found for email: $email")
            toUserDetails(user)
        }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/login", "/css/**", "/js/**", "/favicon.ico", "/error").permitAll()
                    // SaaS management pages — admin only
                    .requestMatchers("/saas/**").hasRole("ADMIN")
                    // User management mutations — admin only
                    .requestMatchers("/users/*/edit", "/users/*/update", "/users/*/delete").hasRole("ADMIN")
                    // SaaS API endpoints — admin only for mutations
                    .requestMatchers(HttpMethod.POST, "/api/users", "/api/users/*").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/api-keys", "/api/api-keys/*").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/api-keys/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/api-keys/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/webhooks", "/api/webhooks/*").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/webhooks/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/webhooks/**").hasRole("ADMIN")
                    // Read-only SaaS API endpoints — any authenticated user
                    .requestMatchers(HttpMethod.GET, "/api/dashboard", "/api/usage/**", "/api/audit-logs/**").authenticated()
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/saas/dashboard", true)
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
            }
            .csrf { csrf ->
                csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
            }
        return http.build()
    }

    private fun toUserDetails(user: User): UserDetails =
        org.springframework.security.core.userdetails.User.builder()
            .username(user.email)
            .password(user.password)
            .roles(*rolesFor(user))
            .disabled(false)
            .build()

    private fun rolesFor(user: User): Array<String> =
        if (user.role?.contains("ADMIN") == true) arrayOf("ADMIN") else arrayOf("USER")
}
