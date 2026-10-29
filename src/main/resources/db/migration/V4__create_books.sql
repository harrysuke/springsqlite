-- ---------------------------------------------------------------------------
-- Book catalog — the SQLite equivalent of the ASP.NET Core `books` table.
--
-- Ported from ASP.NET-Core-MVC-SQLite (mvc/SQL/books.sql). The original exposed
-- the catalog through EF Core plus the T-SQL stored procedures BookViewAll,
-- BookViewById, BookAdd, BookEdit, BookAddOrEdit, BookDeleteById and
-- BookSearchByTitle. SQLite has no stored procedures, so those operations are
-- plain statements in mappers/BookMapper.xml instead.
--
-- `price` is a whole number, exactly like the original `int Price` column.
-- ---------------------------------------------------------------------------
CREATE TABLE books (
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    title  TEXT    NOT NULL,
    author TEXT    NOT NULL,
    price  INTEGER NOT NULL DEFAULT 0
);

-- Kept for exact/prefix lookups and for ordering by author or title. The
-- `%term%` search below is a scan regardless — a leading wildcard rules out an
-- index seek — which is fine for a catalog this size.
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);

-- Same demo catalog the ASP.NET Core app seeds through DatabaseInitializer.
INSERT INTO books (title, author, price) VALUES
('The Pragmatic Programmer', 'Andrew Hunt', 42),
('Clean Code', 'Robert C. Martin', 38),
('Design Patterns', 'Erich Gamma', 55),
('Refactoring', 'Martin Fowler', 47),
('Domain-Driven Design', 'Eric Evans', 52),
('The Mythical Man-Month', 'Frederick Brooks', 29);
