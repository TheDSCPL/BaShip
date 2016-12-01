CREATE TABLE users (
    uid BIGSERIAL PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT 
);

CREATE TABLE games (
    gid BIGSERIAL PRIMARY KEY,
    startdate TIMESTAMP,
    enddate TIMESTAMP,
    player1 INTEGER NOT NULL REFERENCES users,
    player2 INTEGER NOT NULL REFERENCES users,
    winner INTEGER REFERENCES users CHECK (winner IN (player1, player2))
);

CREATE TABLE moves (
    moveid BIGSERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player in (1,2)),
    index INTEGER NOT NULL,
    UNIQUE(gmid, index)
);

CREATE TABLE ships (
    sid BIGSERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player IN (1,2)),
    size INTEGER CHECK (type >= 1 AND type <= 4),
    posx INTEGER CHECK (posx >= 0 AND posx < 10),
    posy INTEGER CHECK (posy >= 0 AND posy < 10),
    vertical BOOLEAN
);

CREATE TABLE gamechat (
    mssgid BIGSERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player IN (1,2)),
    timestamp TIME NOT NULL,
    txt TEXT NOT NULL,
    UNIQUE(gmid, player, timestamp)
);

CREATE TABLE globalchat (
    mssgid BIGSERIAL PRIMARY KEY,
    uid INTEGER NOT NULL REFERENCES users,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    txt TEXT NOT NULL,
    UNIQUE(uid, timestamp)
);
