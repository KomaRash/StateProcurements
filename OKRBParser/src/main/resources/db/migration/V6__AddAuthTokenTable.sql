CREATE TABLE BearerTokens
(
    ID          VARCHAR PRIMARY KEY,
    Identity    Integer   not null references users (userid) ON DELETE Cascade,
    EXPIRY      TIMESTAMP NOT NULL,
    LastTouched TIMESTAMP
)

