-- =========================
-- IMPORT DES EQUIPES
-- =========================

COPY team (
    country,
    year,
    description,
    logo_url
)
FROM '/csv/Team.csv'
WITH (
    FORMAT CSV,
    HEADER TRUE,
    ENCODING 'UTF8'
);


-- =========================
-- IMPORT TEMPORAIRE JOUEURS
-- =========================

CREATE TEMP TABLE player_csv (
    year SMALLINT,
    country VARCHAR(100),
    player VARCHAR(150),
    position VARCHAR(50),
    overall SMALLINT,
    can_kick BOOLEAN
);


COPY player_csv (
    year,
    country,
    player,
    position,
    overall,
    can_kick
)
FROM '/csv/Player.csv'
WITH (
    FORMAT CSV,
    HEADER TRUE,
    ENCODING 'UTF8'
);


-- =========================
-- CREATION DES JOUEURS
-- =========================

INSERT INTO player (
    team_id,
    name,
    position,
    overall,
    can_kick
)
SELECT
    t.id,
    TRIM(pc.player),
    TRIM(pc.position),
    pc.overall,
    pc.can_kick
FROM player_csv pc
JOIN team t
    ON t.year = pc.year
    AND t.country = TRIM(pc.country);