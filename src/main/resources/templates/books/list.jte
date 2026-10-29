@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param String search
@param String flash
@param com.example.springjte.model.Page<com.example.springjte.dto.BookResponse> pageData
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Books", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="app-page-title mb-0">Books</h1>
                <div class="d-flex gap-2 flex-wrap">
                    <a class="app-btn app-btn-outline" href="${contextPath}/books/export">Export CSV</a>
                    <a class="app-btn app-btn-primary" href="${contextPath}/books/new">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><path d="M12 5v14M5 12h14"/></svg>
                        Add Book
                    </a>
                </div>
            </div>

            @if (flash != null)
            <div class="app-alert app-alert-success d-flex justify-content-between align-items-center">
                <span>${flash}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            <div class="app-card mb-4">
                <div class="card-body" style="padding: 1rem 1.25rem;">
                    <form class="d-flex gap-2 flex-wrap align-items-center" method="get" action="${contextPath}/books">
                        <input type="text" name="search" value="${search}" class="app-input" style="max-width: 300px;" placeholder="Search title or author">
                        <button type="submit" class="app-btn app-btn-outline app-btn-sm">Search</button>
                        <a class="app-btn app-btn-ghost app-btn-sm" href="${contextPath}/books">Reset</a>
                        <span class="app-text-muted ms-auto" style="font-size: 0.8125rem;">${pageData.getTotal()} books</span>
                    </form>
                </div>
            </div>

            <div class="app-card">
                <div style="overflow-x: auto;">
                    <table class="app-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th style="text-align: right;">Price</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        @if (pageData.getItems().isEmpty())
                        <tr>
                            <td colspan="5" style="text-align: center;" class="app-text-muted">No books found</td>
                        </tr>
                        @endif
                        @for (var book : pageData.getItems())
                        <tr>
                            <td>${book.getId()}</td>
                            <td><strong>${book.getTitle()}</strong></td>
                            <td>${book.getAuthor()}</td>
                            <td style="text-align: right;">${String.format("%,d", book.getPrice())}</td>
                            <td style="text-align: right; white-space: nowrap;">
                                <a class="app-btn app-btn-outline app-btn-sm" href="${contextPath}/books/${book.getId()}">Details</a>
                                <a class="app-btn app-btn-outline app-btn-sm" href="${contextPath}/books/${book.getId()}/edit">Edit</a>
                                <form class="d-inline" method="post" action="${contextPath}/books/${book.getId()}/delete" onsubmit="return confirm('Delete this book? This cannot be undone.');">
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
                    <a class="page-link" href="${contextPath}/books?page=${pageData.getPage() - 1}&search=${search}">Previous</a>
                </li>
                <li class="page-item disabled">
                    <span class="page-link">Page ${pageData.getPage()} of ${pageData.getTotalPages()}</span>
                </li>
                <li class="page-item ${pageData.getHasNext() ? "" : "disabled"}">
                    <a class="page-link" href="${contextPath}/books?page=${pageData.getPage() + 1}&search=${search}">Next</a>
                </li>
            </ul>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
