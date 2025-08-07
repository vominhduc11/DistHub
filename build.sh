#!/bin/bash

# Dừng script nếu có lỗi xảy ra
set -e

# Màu hiển thị (tuỳ chọn cho đẹp)
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Bắt đầu build dự án Spring Boot...${NC}"

# Build config-server
echo -e "${BLUE}📦 Building config-server...${NC}"
cd config-server
./mvnw clean install -DskipTests
cp target/config-server-0.0.1-SNAPSHOT.jar ../config-server.jar
cd ..

# Build api-gateway
echo -e "${BLUE}📦 Building api-gateway...${NC}"
cd api-gateway
./mvnw clean install -DskipTests
cp target/api-gateway-0.0.1-SNAPSHOT.jar ../api-gateway.jar
cd ..

# Build discovery-service
echo -e "${BLUE}📦 Building discovery-service...${NC}"
cd discovery-service
./mvnw clean install -DskipTests
cp target/discovery-service-0.0.1-SNAPSHOT.jar ../discovery-service.jar
cd ..

# Build auth-service
echo -e "${BLUE}📦 Building auth-service...${NC}"
cd auth-service
./mvnw clean install -DskipTests
cp target/auth-service-0.0.1-SNAPSHOT.jar ../auth-service.jar
cd ..

# Build user-service
echo -e "${BLUE}📦 Building user-service...${NC}"
cd user-service
./mvnw clean install -DskipTests
cp target/user-service-0.0.1-SNAPSHOT.jar ../user-service.jar
cd ..

# Build notification-service
echo -e "${BLUE}📦 Building notification-service...${NC}"
cd notification-service
./mvnw clean install -DskipTests
cp target/notification-service-0.0.1-SNAPSHOT.jar ../notification-service.jar
cd ..

echo -e "${GREEN}✅ Build thành công! Tất cả file JAR đã được copy ra thư mục root.${NC}"
