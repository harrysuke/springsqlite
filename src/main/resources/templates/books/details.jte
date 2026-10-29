@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param com.example.springjte.dto.BookResponse book
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Book Details", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">Book Details</h1>

            <div class="app-card" style="max-width: 640px;">
                <div class="card-body" style="padding: 1.5rem;">
                    <dl class="row mb-0">
                        <dt class="col-sm-3 app-text-muted">ID</dt>
                        <dd class="col-sm-9">${book.getId()}</dd>

                        <dt class="col-sm-3 app-text-muted">Title</dt>
                        <dd class="col-sm-9"><strong>${book.getTitle()}</strong></dd>

                        <dt class="col-sm-3 app-text-muted">Author</dt>
                        <dd class="col-sm-9">${book.getAuthor()}</dd>

                        <dt class="col-sm-3 app-text-muted">Price</dt>
                        <dd class="col-sm-9">${String.format("%,d", book.getPrice())}</dd>
                    </dl>
                </div>
                <div class="card-footer d-flex gap-2" style="background: transparent;">
                    <a class="app-btn app-btn-primary" href="${contextPath}/books/${book.getId()}/edit">Edit</a>
                    <a class="app-btn app-btn-outline" href="${contextPath}/books">Back to list</a>
                    <form class="d-inline ms-auto" method="post" action="${contextPath}/books/${book.getId()}/delete" onsubmit="return confirm('Delete this book? This cannot be undone.');">
                        <input type="hidden" name="${csrfParam}" value="${csrf}">
                        <button type="submit" class="app-btn app-btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
