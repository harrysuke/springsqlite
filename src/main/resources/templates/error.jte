@param Integer status
@param String error
@param String message
@param String path
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Error")
</head>
<body class="bg-light d-flex align-items-center justify-content-center" style="min-height: 100vh;">
    <div class="card shadow-sm text-center" style="width: 28rem;">
        <div class="card-body p-5">
            <h1 class="display-4 fw-bold mb-3">
                @if (status != null)
                ${status}
                @else
                Error
                @endif
            </h1>
            <p class="lead mb-3">
                @if (error != null && !error.isEmpty())
                ${error}
                @else
                Something went wrong
                @endif
            </p>
            @if (message != null && !message.isEmpty() && message != error)
            <p class="text-muted mb-4">${message}</p>
            @endif
            @if (path != null)
            <p class="text-muted small mb-4">${path}</p>
            @endif
        </div>
    </div>
</body>
</html>
