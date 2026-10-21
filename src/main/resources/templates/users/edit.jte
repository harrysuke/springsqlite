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
    @template.fragments.head(title = "Edit User")
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="d-flex">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="flex-grow-1 p-4">
            <h1>Edit User</h1>

            @if (flash != null)
            <div class="alert alert-success alert-dismissible fade show">
                ${flash}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            @if (error != null)
            <div class="alert alert-danger alert-dismissible fade show">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            <form method="post" action="${contextPath}/users/${user.getId()}/update" class="col-lg-6">
                <input type="hidden" name="${csrfParam}" value="${csrf}">
                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" id="name" name="name" class="form-control" value="${user.getName()}" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" id="email" name="email" class="form-control @if (error != null)is-invalid @endif" value="${user.getEmail()}" required>
                    @if (error != null)
                    <div class="invalid-feedback">${error}</div>
                    @endif
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password (leave blank to keep current)</label>
                    <input type="password" id="password" name="password" class="form-control">
                </div>
                <div class="mb-3">
                    <span class="badge ${user.getRole() != null && user.getRole().contains("ADMIN") ? "bg-danger" : "bg-secondary"}">
                        ${user.getRole() != null && user.getRole().contains("ADMIN") ? "ADMIN" : "USER"}
                    </span>
                </div>
                <button type="submit" class="btn btn-primary">Save</button>
                <a class="btn btn-outline-secondary" href="${contextPath}/users">Cancel</a>
            </form>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</body>
</html>