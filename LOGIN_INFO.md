# DistHub - Login Information

## Application Accounts

### Admin Account
- **Username**: `admin`
- **Password**: `admin123456`
- **Role**: `ADMIN`
- **Status**: Active
- **Note**: Created automatically when auth-service starts for the first time

### Customer Accounts
- **Username**: `customer01`
- **Password**: `password123`
- **Role**: `CUSTOMER`
- **Status**: Active
- **AccountId**: 4
- **Created**: 2025-08-14 04:20:06

- **Username**: `customer02`
- **Password**: `password123`
- **Role**: `CUSTOMER`
- **Status**: Active
- **AccountId**: 5
- **Created**: 2025-08-14 04:20:27

## PostgreSQL Database
- **Host**: `dishub_postgres` (internal) / `localhost` (external)
- **Port**: `5432`
- **Username**: `voduc`
- **Password**: `voduc123`
- **Databases**: 
  - `auth_db`
  - `user_db`
  - `language_db`
  - `content_management_db`
  - `notification_db`

## Redis
- **Host**: `localhost`
- **Port**: `6379`
- **Password**: `voduc123`

## pgAdmin (Database Management)
- **URL**: http://localhost:8087
- **Email**: `admin@dishub.com`
- **Password**: `admin123`

## Redis Commander
- **URL**: http://localhost:8090
- **Authentication**: None required

## Kafka Cluster (3 brokers)
- **Broker 1**: `localhost:9092`
- **Broker 2**: `localhost:9093`
- **Broker 3**: `localhost:9094`
- **Bootstrap Servers**: `localhost:9092,localhost:9093,localhost:9094`

## ZooKeeper Cluster (3 nodes)
- **ZK 1**: `localhost:2181`
- **ZK 2**: `localhost:2182`
- **ZK 3**: `localhost:2183`

## Kafka UI
- **URL**: http://localhost:8091
- **Authentication**: None required