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
                        <input type="text" id="name" name="name" placeholder="Name" class="form-control" required>
                    </div>
                    <div class="col">
                        <input type="email" id="email" name="email" placeholder="Email" class="form-control" required>
                    </div>
                    <div class="col">
                        <input type="password" id="password" name="password" placeholder="Password" class="form-control" required minlength="6">
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Add User</button>
                    </div>
                </div>
            </form>
            <div id="formMessage" class="alert d-none"></div>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = true)
</body>
</html>