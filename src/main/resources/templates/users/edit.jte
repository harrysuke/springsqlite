@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param String flash
@param String error
@param com.example.springjte.dto.UserResponse user
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Edit User", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">Edit User</h1>

            @if (flash != null)
            <div class="app-alert app-alert-success d-flex justify-content-between align-items-center">
                <span>${flash}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            @if (error != null)
            <div class="app-alert app-alert-danger d-flex justify-content-between align-items-center">
                <span>${error}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            <div class="app-card" style="max-width: 540px;">
                <div class="card-body" style="padding: 1.5rem;">
                    <form method="post" action="${contextPath}/users/${user.getId()}/update">
                        <input type="hidden" name="${csrfParam}" value="${csrf}">
                        <div class="mb-3">
                            <label for="name" class="app-form-label">Name</label>
                            <input type="text" id="name" name="name" class="app-input" value="${user.getName()}" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="app-form-label">Email</label>
                            <input type="email" id="email" name="email" class="app-input @if (error != null)is-invalid @endif" value="${user.getEmail()}" required>
                            @if (error != null)
                            <div class="app-alert app-alert-danger mt-2" style="padding: 0.5rem 0.75rem; font-size: 0.8125rem;">${error}</div>
                            @endif
                        </div>
                        <div class="mb-3">
                            <label for="password" class="app-form-label">Password</label>
                            <input type="password" id="password" name="password" class="app-input" placeholder="Leave blank to keep current">
                        </div>
                        <div class="mb-3">
                            <span class="app-badge ${user.getRole() != null && user.getRole().contains("ADMIN") ? "app-badge-admin" : "app-badge-user"}">
                                ${user.getRole() != null && user.getRole().contains("ADMIN") ? "Admin" : "User"}
                            </span>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="app-btn app-btn-primary">Save Changes</button>
                            <a class="app-btn app-btn-outline" href="${contextPath}/users">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
