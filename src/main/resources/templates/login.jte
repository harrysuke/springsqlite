@param String contextPath
@param String csrf
@param String csrfParam
@param boolean hasError
@param boolean hasLogout
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Sign In")
</head>
<body class="bg-light d-flex align-items-center justify-content-center" style="min-height: 100vh;">
    <div class="card shadow-sm" style="width: 24rem;">
        <div class="card-body p-4">
            <h1 class="h4 mb-4 text-center">User Admin</h1>

            @if (hasError)
            <div class="alert alert-danger" role="alert">Invalid email or password.</div>
            @endif

            @if (hasLogout)
            <div class="alert alert-success" role="alert">You have been logged out.</div>
            @endif

            <form method="post" action="${contextPath}/login">
                <input type="hidden" name="${csrfParam}" value="${csrf}">
                <div class="mb-3">
                    <label for="username" class="form-label">Email</label>
                    <input type="email" id="username" name="username" class="form-control" autofocus required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Sign In</button>
            </form>
        </div>
    </div>
</body>
</html>
