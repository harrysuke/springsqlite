@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param java.util.List<com.example.springjte.dto.ApiKeyResponse> apiKeys
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "API Keys", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">API Keys</h1>

            <div id="formMessage" class="app-alert" style="display: none;"></div>

            <div class="app-card mb-4">
                <div class="card-body" style="padding: 1rem 1.25rem;">
                    <form id="apiKeyForm" class="d-flex gap-2 flex-wrap align-items-center">
                        <input type="text" id="keyName" name="name" class="app-input" style="max-width: 350px;" placeholder="Key name (e.g. Production API)" required minlength="2">
                        <button type="submit" class="app-btn app-btn-primary app-btn-sm">Generate Key</button>
                    </form>
                </div>
            </div>

            <div class="app-card">
                <div style="overflow-x: auto;">
                    <table class="app-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Key</th>
                            <th>Owner</th>
                            <th>Status</th>
                            <th>Last Used</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        @if (apiKeys.isEmpty())
                        <tr><td colspan="7" style="text-align: center;" class="app-text-muted">No API keys yet. Generate one above.</td></tr>
                        @endif
                        @for (var key : apiKeys)
                        <tr>
                            <td>${key.getId()}</td>
                            <td><strong>${key.getName()}</strong></td>
                            <td><span class="app-code">${key.getKeyValue()}</span></td>
                            <td>${key.getUserEmail()}</td>
                            <td>
                                @if (key.getActive())
                                <span class="app-badge app-badge-success">Active</span>
                                @else
                                <span class="app-badge app-badge-secondary">Disabled</span>
                                @endif
                            </td>
                            <td><small class="app-text-muted">${key.getLastUsedAt() != null ? key.getLastUsedAt() : "Never"}</small></td>
                            <td style="text-align: right; white-space: nowrap;">
                                <button class="app-btn app-btn-outline app-btn-sm" onclick="toggleKey(${key.getId()})">
                                    ${key.getActive() ? "Disable" : "Enable"}
                                </button>
                                <button class="app-btn app-btn-danger app-btn-sm" onclick="deleteKey(${key.getId()})">Delete</button>
                            </td>
                        </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
    <script>
        window.API_KEYS_URL = "${contextPath}/api/api-keys";
        window.API_KEYS_TOGGLE_URL = function(id) { return "${contextPath}/api/api-keys/" + id + "/toggle"; };
        window.API_KEYS_DELETE_URL = function(id) { return "${contextPath}/api/api-keys/" + id; };
    </script>
    <script src="${contextPath}/saas-api-keys.js"></script>
</body>
</html>
