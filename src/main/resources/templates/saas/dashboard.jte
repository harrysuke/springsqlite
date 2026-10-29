@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param com.example.springjte.dto.DashboardSummary summary
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Dashboard", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">Centralized Dashboard</h1>

            <!-- Metric Cards -->
            <div class="row g-3 mb-4">
                <div class="col-md-3">
                    <div class="app-metric">
                        <div class="metric-label">Total Users</div>
                        <div class="metric-value">${summary.getTotalUsers()}</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="app-metric">
                        <div class="metric-label">API Keys</div>
                        <div class="metric-value">${summary.getTotalApiKeys()}</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="app-metric">
                        <div class="metric-label">Active Webhooks</div>
                        <div class="metric-value">${summary.getActiveWebhooks()}</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="app-metric">
                        <div class="metric-label">Audit Entries</div>
                        <div class="metric-value">${summary.getTotalAuditLogs()}</div>
                    </div>
                </div>
            </div>

            <!-- Usage Summary -->
            <div class="row g-3 mb-4">
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">API Usage Summary</h5></div>
                        <div class="card-body">
                            <p style="font-size: 1.125rem; margin-bottom: 1rem;">Total API Requests: <strong>${summary.getUsageSummary().getTotalRequests()}</strong></p>
                            <div class="sidebar-heading" style="padding-left: 0;">Requests by Method</div>
                            <ul class="list-group list-group-flush mb-3">
                                @for (var entry : summary.getUsageSummary().getRequestsByMethod().entrySet())
                                <li class="app-list-group-item d-flex justify-content-between">
                                    <span>${entry.getKey()}</span>
                                    <strong>${entry.getValue()}</strong>
                                </li>
                                @endfor
                            </ul>
                            <div class="sidebar-heading" style="padding-left: 0;">Requests by Status Code</div>
                            <ul class="list-group list-group-flush">
                                @for (var entry : summary.getUsageSummary().getRequestsByStatus().entrySet())
                                <li class="app-list-group-item d-flex justify-content-between">
                                    <span>${entry.getKey()}</span>
                                    <strong>${entry.getValue()}</strong>
                                </li>
                                @endfor
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Requests by Endpoint</h5></div>
                        <div class="card-body">
                            @if (summary.getUsageSummary().getRequestsByEndpoint().isEmpty())
                            <p class="app-text-muted">No API usage recorded yet.</p>
                            @else
                            <ul class="list-group list-group-flush">
                                @for (var entry : summary.getUsageSummary().getRequestsByEndpoint().entrySet())
                                <li class="app-list-group-item d-flex justify-content-between">
                                    <span class="app-code">${entry.getKey()}</span>
                                    <strong>${entry.getValue()}</strong>
                                </li>
                                @endfor
                            </ul>
                            @endif
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Audit Logs & Webhook Deliveries -->
            <div class="row g-3 mb-4">
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Recent Audit Logs</h5>
                            <a href="${contextPath}/saas/audit-logs" class="app-btn app-btn-outline app-btn-sm">View All</a>
                        </div>
                        <div class="card-body">
                            @if (summary.getRecentAuditLogs().isEmpty())
                            <p class="app-text-muted">No audit entries yet.</p>
                            @else
                            <div style="overflow-x: auto;">
                                <table class="app-table">
                                    <thead>
                                    <tr><th>Action</th><th>Resource</th><th>Actor</th><th>Time</th></tr>
                                    </thead>
                                    <tbody>
                                    @for (var log : summary.getRecentAuditLogs())
                                    <tr>
                                        <td><span class="app-badge app-badge-secondary">${log.getAction()}</span></td>
                                        <td>${log.getResource()}</td>
                                        <td>${log.getActor()}</td>
                                        <td><small class="app-text-muted">${log.getCreatedAt()}</small></td>
                                    </tr>
                                    @endfor
                                    </tbody>
                                </table>
                            </div>
                            @endif
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Recent Webhook Deliveries</h5>
                            <a href="${contextPath}/saas/webhooks" class="app-btn app-btn-outline app-btn-sm">Manage</a>
                        </div>
                        <div class="card-body">
                            @if (summary.getRecentWebhookDeliveries().isEmpty())
                            <p class="app-text-muted">No webhook deliveries yet.</p>
                            @else
                            <div style="overflow-x: auto;">
                                <table class="app-table">
                                    <thead>
                                    <tr><th>Event</th><th>Status</th><th>Attempts</th><th>Time</th></tr>
                                    </thead>
                                    <tbody>
                                    @for (var d : summary.getRecentWebhookDeliveries())
                                    <tr>
                                        <td>${d.getEventType()}</td>
                                        <td>
                                            @if (d.getSuccess())
                                            <span class="app-badge app-badge-success">OK</span>
                                            @else
                                            <span class="app-badge app-badge-danger">FAIL</span>
                                            @endif
                                        </td>
                                        <td>${d.getAttemptCount()}</td>
                                        <td><small class="app-text-muted">${d.getCreatedAt()}</small></td>
                                    </tr>
                                    @endfor
                                    </tbody>
                                </table>
                            </div>
                            @endif
                        </div>
                    </div>
                </div>
            </div>

            <!-- Daily Usage Chart Data -->
            @if (!summary.getUsageSummary().getDailyCounts().isEmpty())
            <div class="app-card mb-4">
                <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Daily API Request Volume (Last 30 Days)</h5></div>
                <div class="card-body">
                    <table class="app-table">
                        <thead><tr><th>Date</th><th>Requests</th></tr></thead>
                        <tbody>
                        @for (var dc : summary.getUsageSummary().getDailyCounts())
                        <tr>
                            <td>${dc.getDate()}</td>
                            <td>
                                <div class="app-progress" style="height: 20px;">
                                    <div class="progress-bar" role="progressbar"
                                         style="width: ${(dc.getCount() * 100 / (summary.getUsageSummary().getDailyCounts().stream().mapToLong(e -> e.getCount()).max().orElse(1)))}%">
                                        ${dc.getCount()}
                                    </div>
                                </div>
                            </td>
                        </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            </div>
            @endif
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
