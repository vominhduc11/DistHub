-- Create databases for different services
CREATE DATABASE auth_db;
CREATE DATABASE user_db;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE auth_db TO voduc;
GRANT ALL PRIVILEGES ON DATABASE user_db TO voduc;