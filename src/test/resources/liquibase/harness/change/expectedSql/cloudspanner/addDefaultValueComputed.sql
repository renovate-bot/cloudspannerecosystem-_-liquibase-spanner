ALTER TABLE posts ADD default_uuid STRING(36)
ALTER TABLE posts ALTER COLUMN default_uuid SET DEFAULT (GENERATE_UUID())