@param Integer status
@param String error
@param String message
@param String path
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Error")
</head>
<body>
    <button class="theme-toggle app-auth-theme-toggle" onclick="toggleTheme()" title="Toggle theme" aria-label="Toggle theme">
        <svg class="icon-moon" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
        <svg class="icon-sun" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
    </button>
    <div class="app-auth-wrapper">
        <div class="app-auth-card" style="text-align: center;">
            <h1 class="auth-title" style="font-size: 2.5rem; font-weight: 800; margin-bottom: 0.5rem;">
                @if (status != null)
                ${status}
                @else
                Error
                @endif
            </h1>
            <p class="auth-subtitle" style="font-size: 1rem; margin-bottom: 1rem;">
                @if (error != null && !error.isEmpty())
                ${error}
                @else
                Something went wrong
                @endif
            </p>
            @if (message != null && !message.isEmpty() && message != error)
            <p class="app-text-muted" style="font-size: 0.8125rem; margin-bottom: 1rem;">${message}</p>
            @endif
            @if (path != null)
            <p class="app-text-muted" style="font-size: 0.75rem;">${path}</p>
            @endif
        </div>
    </div>
    @template.fragments.scripts(contextPath = "", withApp = false)
</body>
</html>
