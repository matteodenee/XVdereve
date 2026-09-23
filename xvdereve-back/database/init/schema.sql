CREATE TABLE team (
    id SERIAL PRIMARY KEY,

    year SMALLINT NOT NULL,
    country VARCHAR(100) NOT NULL,

    description TEXT,
    logo_url VARCHAR(500),

    CONSTRAINT uq_team_year_country
        UNIQUE (year, country),

    CONSTRAINT ck_team_year
        CHECK (year >= 1987)
);


CREATE TABLE player (
    id SERIAL PRIMARY KEY,

    team_id INTEGER NOT NULL,

    name VARCHAR(150) NOT NULL,
    position VARCHAR(50) NOT NULL,
    overall SMALLINT NOT NULL,
    can_kick BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_player_team
        FOREIGN KEY (team_id)
        REFERENCES team(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_player_team_name
        UNIQUE (team_id, name),

    CONSTRAINT ck_player_overall
        CHECK (overall BETWEEN 0 AND 100)
);


CREATE INDEX idx_player_team
    ON player(team_id);