-- SaaS feature tables: API keys, webhooks, webhook deliveries, usage events, audit logs.
-- All tables use INTEGER PRIMARY KEY AUTOINCREMENT (SQLite convention).

-- ---------------------------------------------------------------------------
-- API Keys — tokens that external clients present to authenticate API calls.
-- ---------------------------------------------------------------------------
CREATE TABLE api_keys (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    key_value   TEXT    NOT NULL UNIQUE,
    name        TEXT    NOT NULL,
    user_email  TEXT    NOT NULL,
    active      INTEGER NOT NULL DEFAULT 1,
    created_at  TEXT    NOT NULL DEFAULT (datetime('now')),
    last_used_at TEXT
);
CREATE INDEX idx_api_keys_key_value ON api_keys(key_value);

-- ---------------------------------------------------------------------------
-- Webhooks — endpoint URLs that receive POST notifications on domain events.
-- ---------------------------------------------------------------------------
CREATE TABLE webhooks (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    url             TEXT    NOT NULL,
    description     TEXT,
    event_types     TEXT    NOT NULL DEFAULT '*',
    active          INTEGER NOT NULL DEFAULT 1,
    secret          TEXT,
    created_at      TEXT    NOT NULL DEFAULT (datetime('now'))
);

-- ---------------------------------------------------------------------------
-- Webhook Deliveries — log of each delivery attempt (for retry & debugging).
-- ---------------------------------------------------------------------------
CREATE TABLE webhook_deliveries (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    webhook_id      INTEGER NOT NULL,
    event_type      TEXT    NOT NULL,
    payload         TEXT    NOT NULL,
    response_status INTEGER,
    response_body   TEXT,
    success         INTEGER NOT NULL DEFAULT 0,
    attempt_count   INTEGER NOT NULL DEFAULT 1,
    created_at      TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (webhook_id) REFERENCES webhooks(id) ON DELETE CASCADE
);
CREATE INDEX idx_webhook_deliveries_webhook_id ON webhook_deliveries(webhook_id);

-- ---------------------------------------------------------------------------
-- Usage Events — append-only log of API calls and feature usage for analytics.
-- ---------------------------------------------------------------------------
CREATE TABLE usage_events (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    event_type      TEXT    NOT NULL,
    endpoint        TEXT,
    method          TEXT,
    user_email      TEXT,
    api_key_id      INTEGER,
    status_code     INTEGER,
    duration_ms     INTEGER,
    created_at      TEXT    NOT NULL DEFAULT (datetime('now'))
);
CREATE INDEX idx_usage_events_event_type ON usage_events(event_type);
CREATE INDEX idx_usage_events_created_at ON usage_events(created_at);
CREATE INDEX idx_usage_events_user_email ON usage_events(user_email);

-- ---------------------------------------------------------------------------
-- Audit Logs — who-did-what-when for compliance and security tracking.
-- ---------------------------------------------------------------------------
CREATE TABLE audit_logs (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    actor           TEXT    NOT NULL,
    action          TEXT    NOT NULL,
    resource        TEXT    NOT NULL,
    resource_id     TEXT,
    details         TEXT,
    ip_address      TEXT,
    created_at      TEXT    NOT NULL DEFAULT (datetime('now'))
);
CREATE INDEX idx_audit_logs_actor ON audit_logs(actor);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
