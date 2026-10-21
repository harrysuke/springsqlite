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
    @template.fragments.head(title = "Users")
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="d-flex">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="flex-grow-1 p-4">
            <h1>Users</h1>

            @if (flash != null)
            <div class="alert alert-success alert-dismissible fade show">
                ${flash}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            <form class="row g-2 mb-3" method="get" action="${contextPath}/users">
                <div class="col-auto">
                    <input type="text" name="search" value="${search}" class="form-control" placeholder="Search name or email">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-outline-primary">Search</button>
                </div>
                <div class="col-auto">
                    <a class="btn btn-outline-secondary" href="${contextPath}/users">Reset</a>
                </div>
                <div class="col-auto d-flex align-items-center">
                    <span class="text-muted">${pageData.getTotal()} users</span>
                </div>
            </form>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                @if (pageData.getItems().isEmpty())
                <tr>
                    <td colspan="5" class="text-center text-muted">No users found</td>
                </tr>
                @endif
                @for (var user : pageData.getItems())
                <tr>
                    <td>${user.getId()}</td>
                    <td>${user.getName()}</td>
                    <td>${user.getEmail()}</td>
                    <td>
                        @if (user.getRole() != null && user.getRole().contains("ADMIN"))
                        <span class="badge bg-danger">ADMIN</span>
                        @else
                        <span class="badge bg-secondary">USER</span>
                        @endif
                    </td>
                    <td>
                        <a class="btn btn-sm btn-primary" href="${contextPath}/users/${user.getId()}/edit">Edit</a>
                        <form class="d-inline" method="post" action="${contextPath}/users/${user.getId()}/delete" onsubmit="return confirm('Delete ${user.getName()}?');">
                            <input type="hidden" name="${csrfParam}" value="${csrf}">
                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                        </form>
                    </td>
                </tr>
                @endfor
                </tbody>
            </table>

            <nav>
                <ul class="pagination">
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
            </nav>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</body>
</html>