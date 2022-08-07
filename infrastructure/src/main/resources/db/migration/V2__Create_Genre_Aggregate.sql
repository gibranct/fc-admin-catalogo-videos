CREATE TABLE genres(
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL
);

CREATE TABLE genres_categories(
    genre_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    constraint idx_genre_category unique (genre_id, category_id),
    constraint fk_genre_id foreign key (genre_id) references genres (id) on delete cascade,
    constraint fk_category_id foreign key (category_id) references category (id) on delete cascade
)

