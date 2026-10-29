@param String contextPath
@param String csrf
@param String csrfParam
@param boolean hasError
@param boolean hasLogout
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Sign In", contextPath = contextPath)
</head>
<body>
    <button class="theme-toggle app-auth-theme-toggle" onclick="toggleTheme()" title="Toggle theme" aria-label="Toggle theme">
        <svg class="icon-moon" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
        <svg class="icon-sun" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
    </button>
    <div class="app-auth-wrapper">
        <div class="app-auth-card">
            <h1 class="auth-title">SaaS Admin</h1>
            <p class="auth-subtitle">Sign in to your account</p>

            @if (hasError)
            <div class="app-alert app-alert-danger">Invalid email or password.</div>
            @endif

            @if (hasLogout)
            <div class="app-alert app-alert-success">You have been logged out.</div>
            @endif

            <form method="post" action="${contextPath}/login">
                <input type="hidden" name="${csrfParam}" value="${csrf}">
                <div class="mb-3">
                    <label for="username" class="app-form-label">Email</label>
                    <input type="email" id="username" name="username" class="app-input" autofocus required>
                </div>
                <div class="mb-4">
                    <label for="password" class="app-form-label">Password</label>
                    <input type="password" id="password" name="password" class="app-input" required>
                </div>
                <button type="submit" class="app-btn app-btn-primary" style="width: 100%;">Sign In</button>
            </form>
        </div>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
