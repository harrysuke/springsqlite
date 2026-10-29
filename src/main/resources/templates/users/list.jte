@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param String search
@param String flash
@param com.example.springjte.model.Page<com.example.springjte.dto.UserResponse> pageData
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Users", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="app-page-title mb-0">Users</h1>
                <button type="button" class="app-btn app-btn-primary" data-bs-toggle="modal" data-bs-target="#addUserModal">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><path d="M12 5v14M5 12h14"/></svg>
                    Add User
                </button>
            </div>

            @if (flash != null)
            <div class="app-alert app-alert-success d-flex justify-content-between align-items-center">
                <span>${flash}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            <div class="app-card mb-4">
                <div class="card-body" style="padding: 1rem 1.25rem;">
                    <form class="d-flex gap-2 flex-wrap align-items-center" method="get" action="${contextPath}/users">
                        <input type="text" name="search" value="${search}" class="app-input" style="max-width: 300px;" placeholder="Search name or email">
                        <button type="submit" class="app-btn app-btn-outline app-btn-sm">Search</button>
                        <a class="app-btn app-btn-ghost app-btn-sm" href="${contextPath}/users">Reset</a>
                        <span class="app-text-muted ms-auto" style="font-size: 0.8125rem;">${pageData.getTotal()} users</span>
                    </form>
                </div>
            </div>

            <div class="app-card">
                <div style="overflow-x: auto;">
                    <table class="app-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        @if (pageData.getItems().isEmpty())
                        <tr>
                            <td colspan="5" style="text-align: center;" class="app-text-muted">No users found</td>
                        </tr>
                        @endif
                        @for (var user : pageData.getItems())
                        <tr>
                            <td>${user.getId()}</td>
                            <td><strong>${user.getName()}</strong></td>
                            <td>${user.getEmail()}</td>
                            <td>
                                @if (user.getRole() != null && user.getRole().contains("ADMIN"))
                                <span class="app-badge app-badge-admin">Admin</span>
                                @else
                                <span class="app-badge app-badge-user">User</span>
                                @endif
                            </td>
                            <td style="text-align: right; white-space: nowrap;">
                                <a class="app-btn app-btn-outline app-btn-sm" href="${contextPath}/users/${user.getId()}/edit">Edit</a>
                                <form class="d-inline" method="post" action="${contextPath}/users/${user.getId()}/delete" onsubmit="return confirm('Delete ${user.getName()}?');">
                                    <input type="hidden" name="${csrfParam}" value="${csrf}">
                                    <button type="submit" class="app-btn app-btn-danger app-btn-sm">Delete</button>
                                </form>
                            </td>
                        </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            </div>

            <ul class="app-pagination">
                <li class="page-item ${pageData.getHasPrevious() ? "" : "disabled"}">
                    <a class="page-link" href="${contextPath}/users?page=${pageData.getPage() - 1}&search=${search}">Previous</a>
                </li>
                <li class="page-item disabled">
                    <span class="page-link">Page ${pageData.getPage()} of ${pageData.getTotalPages()}</span>
                </li>
                <li class="page-item ${pageData.getHasNext() ? "" : "disabled"}">
                    <a class="page-link" href="${contextPath}/users?page=${pageData.getPage() + 1}&search=${search}">Next</a>
                </li>
            </ul>
        </main>
    </div>

    <!-- Add User Modal -->
    <div class="modal fade app-modal" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addUserModalLabel">Add New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div id="formMessage" class="app-alert" style="display: none;"></div>
                    <form id="userForm">
                        <div class="mb-3">
                            <label for="name" class="app-form-label">Name</label>
                            <input type="text" id="name" name="name" class="app-input" placeholder="Enter full name" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="app-form-label">Email</label>
                            <input type="email" id="email" name="email" class="app-input" placeholder="user@example.com" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="app-form-label">Password</label>
                            <input type="password" id="password" name="password" class="app-input" placeholder="Min. 6 characters" required minlength="6">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="app-btn app-btn-ghost" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" form="userForm" class="app-btn app-btn-primary">Add User</button>
                </div>
            </div>
        </div>
    </div>

    @template.fragments.scripts(contextPath = contextPath, withApp = true)
</body>
</html>
