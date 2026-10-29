@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param String heading
@param String actionUrl
@param String error
@param String titleValue
@param String authorValue
@param String priceValue
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = heading, contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">${heading}</h1>

            @if (error != null)
            <div class="app-alert app-alert-danger d-flex justify-content-between align-items-center" style="max-width: 540px;">
                <span>${error}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @endif

            <div class="app-card" style="max-width: 540px;">
                <div class="card-body" style="padding: 1.5rem;">
                    <form method="post" action="${actionUrl}" autocomplete="off">
                        <input type="hidden" name="${csrfParam}" value="${csrf}">
                        <div class="mb-3">
                            <label for="title" class="app-form-label">Title</label>
                            <input type="text" id="title" name="title" class="app-input" value="${titleValue}" maxlength="200" required autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="author" class="app-form-label">Author</label>
                            <input type="text" id="author" name="author" class="app-input" value="${authorValue}" maxlength="120" required>
                        </div>
                        <div class="mb-3">
                            <label for="price" class="app-form-label">Price</label>
                            <input type="number" id="price" name="price" class="app-input" value="${priceValue}" min="0" step="1" inputmode="numeric" spellcheck="false" required>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="app-btn app-btn-primary">Save</button>
                            <a class="app-btn app-btn-outline" href="${contextPath}/books">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
