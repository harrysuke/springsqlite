@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="${contextPath}/">User Admin</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav ms-auto align-items-lg-center">
                <li class="nav-item"><a class="nav-link" href="${contextPath}/">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${contextPath}/users">Users</a></li>
                @if (currentUser != null && !currentUser.isEmpty())
                <li class="nav-item"><span class="navbar-text px-3">Welcome, ${currentUser}</span></li>
                <li class="nav-item">
                    <form class="d-inline" method="post" action="${contextPath}/logout">
                        <input type="hidden" name="${csrfParam}" value="${csrf}">
                        <button type="submit" class="btn btn-link nav-link">Logout</button>
                    </form>
                </li>
                @endif
            </ul>
        </div>
    </div>
</nav>