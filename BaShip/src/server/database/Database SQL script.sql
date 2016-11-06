CREATE TABLE users (
    uid SERIAL PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL
);

CREATE TABLE games (
    gid SERIAL PRIMARY KEY,
    startdate DATE NOT NULL,
    enddate DATE,
    player1 INTEGER NOT NULL REFERENCES users,
    player2 INTEGER NOT NULL REFERENCES users,
    winner INTEGER REFERENCES users CHECK (winner IN (player1, player2))
);

CREATE TABLE moves (
    moveid SERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player in (1,2)),
    index INTEGER NOT NULL,
    UNIQUE(gmid, index)
);

CREATE TABLE ships (
    sid SERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player IN (1,2)),
    type INTEGER CHECK (type >= 0 AND type >= 999), -- TODO: max number of ships TBD
    posx INTEGER CHECK (posx >= 0 AND posx < 10),
    posy INTEGER CHECK (posy >= 0 AND posy < 10),
    horizontal BOOLEAN,
    UNIQUE(gmid, player, type)
);

CREATE TABLE gamechat (
    mssgid SERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player IN (1,2)),
    timestamp TIME NOT NULL,
    txt TEXT NOT NULL,
    UNIQUE(gmid, player, timestamp)
);

CREATE TABLE globalchat (
    mssgid SERIAL PRIMARY KEY,
    uid INTEGER NOT NULL REFERENCES users,
    timestamp TIME NOT NULL,
    txt TEXT NOT NULL,
    UNIQUE(uid, timestamp)
);
