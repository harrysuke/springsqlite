package com.example.springjte.web

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.csrf.CsrfToken

object WebUtils {
    fun csrfToken(request: HttpServletRequest): CsrfToken? =
        request.getAttribute(CsrfToken::class.java.name) as? CsrfToken
}