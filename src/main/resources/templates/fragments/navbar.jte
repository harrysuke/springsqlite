@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
<nav class="app-navbar">
    <a class="navbar-brand" href="${contextPath}/saas/dashboard">SaaS Admin</a>
    <button class="navbar-toggler d-lg-none border-0 ms-2" type="button" onclick="document.querySelector('.app-sidebar').classList.toggle('show')">
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M3 12h18M3 6h18M3 18h18"/></svg>
    </button>
    <div class="d-flex align-items-center ms-auto">
        <a class="nav-link" href="${contextPath}/saas/dashboard">Dashboard</a>
        <a class="nav-link" href="${contextPath}/users">Users</a>
        @if (currentUser != null && !currentUser.isEmpty())
        <span class="navbar-text px-3 d-none d-sm-inline">${currentUser}</span>
        <form class="d-inline" method="post" action="${contextPath}/logout">
            <input type="hidden" name="${csrfParam}" value="${csrf}">
            <button type="submit" class="app-btn app-btn-ghost app-btn-sm">Logout</button>
        </form>
        @endif
        <button class="theme-toggle" onclick="toggleTheme()" title="Toggle theme" aria-label="Toggle theme">
            <svg class="icon-moon" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
            <svg class="icon-sun" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
        </button>
    </div>
</nav>
<script>
    function toggleTheme() {
        var current = document.documentElement.getAttribute('data-bs-theme');
        var next = current === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-bs-theme', next);
        localStorage.setItem('theme', next);
    }
</script>
