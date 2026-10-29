@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param java.util.List<com.example.springjte.dto.AuditLogResponse> auditLogs
@param Long total
@param Integer page
@param Integer size
@param String filterAction
@param String filterActor
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Audit Logs", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">Audit Logs</h1>

            <div class="app-card mb-4">
                <div class="card-body" style="padding: 1rem 1.25rem;">
                    <form class="d-flex gap-2 flex-wrap align-items-center" method="get" action="${contextPath}/saas/audit-logs">
                        <input type="text" name="action" value="${filterAction}" class="app-input" style="max-width: 250px;" placeholder="Filter by action">
                        <input type="text" name="actor" value="${filterActor}" class="app-input" style="max-width: 250px;" placeholder="Filter by actor email">
                        <button type="submit" class="app-btn app-btn-outline app-btn-sm">Filter</button>
                        <a class="app-btn app-btn-ghost app-btn-sm" href="${contextPath}/saas/audit-logs">Reset</a>
                        <span class="app-text-muted ms-auto" style="font-size: 0.8125rem;">${total} entries</span>
                    </form>
                </div>
            </div>

            <div class="app-card">
                <div style="overflow-x: auto;">
                    <table class="app-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Action</th>
                            <th>Resource</th>
                            <th>Resource ID</th>
                            <th>Actor</th>
                            <th>IP Address</th>
                            <th>Details</th>
                            <th>Timestamp</th>
                        </tr>
                        </thead>
                        <tbody>
                        @if (auditLogs.isEmpty())
                        <tr><td colspan="8" style="text-align: center;" class="app-text-muted">No audit log entries found.</td></tr>
                        @endif
                        @for (var log : auditLogs)
                        <tr>
                            <td>${log.getId()}</td>
                            <td>
                                @if (log.getAction() == "CREATE")
                                <span class="app-badge app-badge-success">${log.getAction()}</span>
                                @elseif (log.getAction() == "UPDATE")
                                <span class="app-badge app-badge-warning">${log.getAction()}</span>
                                @elseif (log.getAction() == "DELETE")
                                <span class="app-badge app-badge-danger">${log.getAction()}</span>
                                @else
                                <span class="app-badge app-badge-secondary">${log.getAction()}</span>
                                @endif
                            </td>
                            <td>${log.getResource()}</td>
                            <td>${log.getResourceId() != null ? log.getResourceId() : ""}</td>
                            <td>${log.getActor()}</td>
                            <td>${log.getIpAddress() != null ? log.getIpAddress() : ""}</td>
                            <td><small class="app-text-muted">${log.getDetails() != null ? log.getDetails() : ""}</small></td>
                            <td><small class="app-text-muted">${log.getCreatedAt()}</small></td>
                        </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            </div>

            <ul class="app-pagination">
                <li class="page-item ${page > 1 ? "" : "disabled"}">
                    <a class="page-link" href="${contextPath}/saas/audit-logs?page=${page - 1}&action=${filterAction}&actor=${filterActor}">Previous</a>
                </li>
                <li class="page-item disabled">
                    <span class="page-link">Page ${page}</span>
                </li>
                <li class="page-item ${(page * size < total) ? "" : "disabled"}">
                    <a class="page-link" href="${contextPath}/saas/audit-logs?page=${page + 1}&action=${filterAction}&actor=${filterActor}">Next</a>
                </li>
            </ul>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
