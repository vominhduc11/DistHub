-- Create databases for different services
CREATE DATABASE auth_db;
CREATE DATABASE user_db;
CREATE DATABASE language_db;
CREATE DATABASE content_management_db;
CREATE DATABASE notification_db;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE auth_db TO voduc;
GRANT ALL PRIVILEGES ON DATABASE user_db TO voduc;
GRANT ALL PRIVILEGES ON DATABASE language_db TO voduc;
GRANT ALL PRIVILEGES ON DATABASE content_management_db TO voduc;
GRANT ALL PRIVILEGES ON DATABASE notification_db TO voduc;

-- Note: Default admin account will be created by application startup
-- Username: admin
-- Password: admin123456
-- This account will have ADMIN role and will be created automatically
-- when the auth-service starts up for the first time.