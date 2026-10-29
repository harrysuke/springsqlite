@param String contextPath
@param String currentUser
@param String csrf
@param String csrfParam
@param com.example.springjte.dto.UsageSummary usageSummary
<!DOCTYPE html>
<html>
<head>
    @template.fragments.head(title = "Usage Analytics", contextPath = contextPath)
</head>
<body>
    @template.fragments.navbar(contextPath = contextPath, currentUser = currentUser, csrf = csrf, csrfParam = csrfParam)
    <div class="app-layout">
        @template.fragments.sidebar(contextPath = contextPath)
        <main class="app-content">
            <h1 class="app-page-title">Usage Analytics & Reporting</h1>

            <div class="row g-3 mb-4">
                <div class="col-md-12">
                    <div class="app-metric">
                        <div class="metric-label">Total API Requests</div>
                        <div class="metric-value" style="font-size: 2.25rem;">${usageSummary.getTotalRequests()}</div>
                    </div>
                </div>
            </div>

            <div class="row g-3 mb-4">
                <!-- By Endpoint -->
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Requests by Endpoint</h5></div>
                        <div class="card-body">
                            @if (usageSummary.getRequestsByEndpoint().isEmpty())
                            <p class="app-text-muted">No data yet.</p>
                            @else
                            <table class="app-table">
                                <thead><tr><th>Endpoint</th><th>Requests</th></tr></thead>
                                <tbody>
                                @for (var entry : usageSummary.getRequestsByEndpoint().entrySet())
                                <tr>
                                    <td><span class="app-code">${entry.getKey()}</span></td>
                                    <td><strong>${entry.getValue()}</strong></td>
                                </tr>
                                @endfor
                                </tbody>
                            </table>
                            @endif
                        </div>
                    </div>
                </div>

                <!-- By Method -->
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Requests by HTTP Method</h5></div>
                        <div class="card-body">
                            @if (usageSummary.getRequestsByMethod().isEmpty())
                            <p class="app-text-muted">No data yet.</p>
                            @else
                            <table class="app-table">
                                <thead><tr><th>Method</th><th>Requests</th></tr></thead>
                                <tbody>
                                @for (var entry : usageSummary.getRequestsByMethod().entrySet())
                                <tr>
                                    <td><span class="app-badge app-badge-info">${entry.getKey()}</span></td>
                                    <td><strong>${entry.getValue()}</strong></td>
                                </tr>
                                @endfor
                                </tbody>
                            </table>
                            @endif
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-3 mb-4">
                <!-- By Status -->
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Requests by Status Code</h5></div>
                        <div class="card-body">
                            @if (usageSummary.getRequestsByStatus().isEmpty())
                            <p class="app-text-muted">No data yet.</p>
                            @else
                            <table class="app-table">
                                <thead><tr><th>Status</th><th>Count</th></tr></thead>
                                <tbody>
                                @for (var entry : usageSummary.getRequestsByStatus().entrySet())
                                <tr>
                                    <td>
                                        @if (entry.getKey().startsWith("2"))
                                        <span class="app-badge app-badge-success">${entry.getKey()}</span>
                                        @elseif (entry.getKey().startsWith("4"))
                                        <span class="app-badge app-badge-warning">${entry.getKey()}</span>
                                        @elseif (entry.getKey().startsWith("5"))
                                        <span class="app-badge app-badge-danger">${entry.getKey()}</span>
                                        @else
                                        <span class="app-badge app-badge-secondary">${entry.getKey()}</span>
                                        @endif
                                    </td>
                                    <td><strong>${entry.getValue()}</strong></td>
                                </tr>
                                @endfor
                                </tbody>
                            </table>
                            @endif
                        </div>
                    </div>
                </div>

                <!-- Daily Counts -->
                <div class="col-md-6">
                    <div class="app-card">
                        <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Daily Request Counts (Last 30 Days)</h5></div>
                        <div class="card-body">
                            @if (usageSummary.getDailyCounts().isEmpty())
                            <p class="app-text-muted">No data yet.</p>
                            @else
                            <table class="app-table">
                                <thead><tr><th>Date</th><th>Requests</th></tr></thead>
                                <tbody>
                                @for (var dc : usageSummary.getDailyCounts())
                                <tr>
                                    <td>${dc.getDate()}</td>
                                    <td>
                                        <div class="app-progress" style="height: 18px;">
                                            <div class="progress-bar" role="progressbar"
                                                 style="width: ${(dc.getCount() * 100 / Math.max(1L, usageSummary.getDailyCounts().stream().mapToLong(e -> e.getCount()).max().orElse(1L)))}%">
                                                ${dc.getCount()}
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                @endfor
                                </tbody>
                            </table>
                            @endif
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Events -->
            <div class="app-card">
                <div class="card-header"><h5 class="mb-0" style="font-size: 0.9375rem; font-weight: 600;">Recent API Events</h5></div>
                <div class="card-body">
                    @if (usageSummary.getRecentEvents().isEmpty())
                    <p class="app-text-muted">No API events recorded yet.</p>
                    @else
                    <div style="overflow-x: auto;">
                        <table class="app-table">
                            <thead>
                            <tr><th>ID</th><th>Method</th><th>Endpoint</th><th>User</th><th>Status</th><th>Duration</th><th>Time</th></tr>
                            </thead>
                            <tbody>
                            @for (var evt : usageSummary.getRecentEvents())
                            <tr>
                                <td>${evt.getId()}</td>
                                <td>${evt.getMethod() != null ? evt.getMethod() : ""}</td>
                                <td><span class="app-code">${evt.getEndpoint() != null ? evt.getEndpoint() : ""}</span></td>
                                <td>${evt.getUserEmail() != null ? evt.getUserEmail() : "anonymous"}</td>
                                <td>${evt.getStatusCode() != null ? String.valueOf(evt.getStatusCode()) : ""}</td>
                                <td>${evt.getDurationMs() != null ? evt.getDurationMs() + "ms" : ""}</td>
                                <td><small class="app-text-muted">${evt.getCreatedAt()}</small></td>
                            </tr>
                            @endfor
                            </tbody>
                        </table>
                    </div>
                    @endif
                </div>
            </div>
        </main>
    </div>
    @template.fragments.scripts(contextPath = contextPath, withApp = false)
</body>
</html>
