@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "User CRUD")
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="d-flex">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="flex-grow-1 p-4">
            <h1>User Management</h1>
            <form id="userForm" class="mb-3">
                <div class="row g-2">
                    <div class="col">
                        <input type="text" id="name" placeholder="Name" class="form-control" required>
                    </div>
                    <div class="col">
                        <input type="email" id="email" placeholder="Email" class="form-control" required>
                    </div>
                    <div class="col">
                        <input type="password" id="password" placeholder="Password" class="form-control" required minlength="6">
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Add User</button>
                    </div>
                </div>
            </form>
            <div id="formMessage" class="alert d-none"></div>
        </main>
    </div>
    <script>window.CONTEXT_PATH = "${contextPath}";</script>
    <script>window.CSRF_TOKEN = "${csrf}";</script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
    <script src="${contextPath}/app.js"></script>
</body>
</html>