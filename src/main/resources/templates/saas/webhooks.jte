@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param java.util.List<com.example.springjte.dto.WebhookResponse> webhooks
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Webhooks", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">Webhooks</h1>

            <div id="formMessage" class="app-alert" style="display: none;"></div>

            <div class="app-card mb-4">
                <div class="card-body" style="padding: 1rem 1.25rem;">
                    <form id="webhookForm" class="d-flex gap-2 flex-wrap align-items-center">
                        <input type="url" id="whUrl" name="url" class="app-input" style="max-width: 280px;" placeholder="https://example.com/webhook" required>
                        <input type="text" id="whDesc" name="description" class="app-input" style="max-width: 200px;" placeholder="Description (optional)">
                        <input type="text" id="whEventTypes" name="eventTypes" class="app-input" style="max-width: 140px;" placeholder="event types" value="*">
                        <button type="submit" class="app-btn app-btn-primary app-btn-sm">Add Webhook</button>
                    </form>
                </div>
            </div>

            <div class="app-card">
                <div style="overflow-x: auto;">
                    <table class="app-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>URL</th>
                            <th>Description</th>
                            <th>Event Types</th>
                            <th>Status</th>
                            <th>Created</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        @if (webhooks.isEmpty())
                        <tr><td colspan="7" style="text-align: center;" class="app-text-muted">No webhooks registered yet.</td></tr>
                        @endif
                        @for (var wh : webhooks)
                        <tr>
                            <td>${wh.getId()}</td>
                            <td><span class="app-code">${wh.getUrl()}</span></td>
                            <td>${wh.getDescription() != null ? wh.getDescription() : ""}</td>
                            <td>${wh.getEventTypes()}</td>
                            <td>
                                @if (wh.getActive())
                                <span class="app-badge app-badge-success">Active</span>
                                @else
                                <span class="app-badge app-badge-secondary">Disabled</span>
                                @endif
                            </td>
                            <td><small class="app-text-muted">${wh.getCreatedAt()}</small></td>
                            <td style="text-align: right; white-space: nowrap;">
                                <button class="app-btn app-btn-outline app-btn-sm" onclick="toggleWh(${wh.getId()})">
                                    ${wh.getActive() ? "Disable" : "Enable"}
                                </button>
                                <button class="app-btn app-btn-danger app-btn-sm" onclick="deleteWh(${wh.getId()})">Delete</button>
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
        window.WEBHOOKS_URL = "${contextPath}/api/webhooks";
        window.WEBHOOKS_TOGGLE_URL = function(id) { return "${contextPath}/api/webhooks/" + id + "/toggle"; };
        window.WEBHOOKS_DELETE_URL = function(id) { return "${contextPath}/api/webhooks/" + id; };
    </script>
    <script src="${contextPath}/saas-webhooks.js"></script>
</body>
</html>
