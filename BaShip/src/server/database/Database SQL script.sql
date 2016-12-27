CREATE TABLE users (
    uid BIGSERIAL PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL
);

CREATE TABLE games (
    gmid BIGSERIAL PRIMARY KEY,
    startdate TIMESTAMP WITH TIME ZONE,
    enddate TIMESTAMP WITH TIME ZONE,
    player1 INTEGER NOT NULL REFERENCES users,
    player2 INTEGER NOT NULL REFERENCES users,
    winner INTEGER REFERENCES users CHECK (winner IN (player1, player2))
);

CREATE TABLE moves (
    moveid BIGSERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player in (1,2)),
    index INTEGER NOT NULL,
    posx INTEGER NOT NULL CHECK (posx >= 0 AND posx < 10),
    posy INTEGER NOT NULL CHECK (posy >= 0 AND posy < 10),
    UNIQUE(gmid, index)
);

CREATE TABLE ships (
    sid BIGSERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player IN (1,2)),
    size INTEGER NOT NULL CHECK (size >= 1 AND size <= 4),
    posx INTEGER NOT NULL CHECK (posx >= 0 AND posx < 10),
    posy INTEGER NOT NULL CHECK (posy >= 0 AND posy < 10),
    vertical BOOLEAN NOT NULL
);

CREATE TABLE gamechat (
    mssgid BIGSERIAL PRIMARY KEY,
    gmid INTEGER NOT NULL REFERENCES games,
    player INTEGER NOT NULL CHECK (player IN (1,2)),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    txt TEXT NOT NULL,
    UNIQUE(gmid, player, timestamp)
);

CREATE VIEW user_stats AS SELECT t1.uid,
    t2.ngames,
    t1.nwins,
    t3.nshots
   FROM ( SELECT users.uid,
            count(DISTINCT games.gmid) AS nwins
           FROM users
             LEFT JOIN games ON games.winner = users.uid
          GROUP BY users.uid) t1
     JOIN ( SELECT users.uid,
            count(DISTINCT games.gmid) AS ngames
           FROM users
             LEFT JOIN games ON users.uid = games.player1 OR users.uid = games.player2
          GROUP BY users.uid) t2 USING (uid)
     JOIN ( SELECT users.uid,
            count(DISTINCT tbl1.moveid) AS nshots
           FROM users
             LEFT JOIN ( SELECT users_1.uid,
                    moves.moveid
                   FROM users users_1
                     JOIN games ON users_1.uid = games.player1
                     LEFT JOIN moves USING (gmid)
                  WHERE moves.player = 1
                UNION
                 SELECT users_1.uid,
                    moves.moveid
                   FROM users users_1
                     JOIN games ON users_1.uid = games.player2
                     LEFT JOIN moves USING (gmid)
                  WHERE moves.player = 2) tbl1 USING (uid)
          GROUP BY users.uid) t3 USING (uid);

CREATE VIEW user_ranks AS SELECT tbl.uid,
    row_number() OVER (ORDER BY tbl.rank_value DESC)::integer AS rank
   FROM ( SELECT users.uid,
            user_stats.nwins::numeric / (user_stats.ngames - user_stats.nwins + 1)::numeric AS rank_value
           FROM users
             JOIN user_stats USING (uid)) tbl
  ORDER BY (row_number() OVER (ORDER BY tbl.rank_value DESC)::integer);
