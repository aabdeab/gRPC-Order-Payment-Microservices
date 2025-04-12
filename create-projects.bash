#!/usr/bin/env bash

# Script for creating ProductOrder microservices project structure
# Compatible with Linux, macOS, and Windows (with Git Bash or WSL)

echo "Creating ProductOrder project structure..."

# Create main project directory if not exists
mkdir -p ProductOrder
cd ProductOrder

# Create root directories
mkdir -p assets
mkdir -p src

# Create main project using Spring CLI
spring init \
  --boot-version=3.2.0 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=ProductOrder \
  --package-name=com.ProductOrder \
  --groupId=com.ProductOrder \
  --dependencies=web,actuator \
  --version=1.0.0-SNAPSHOT \
  .

# Create microservices module
mkdir -p microservices
cd microservices

# Create Order Service
spring init \
  --boot-version=3.2.0 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=orderService \
  --package-name=com.ProductOrder.microservices.order \
  --groupId=com.ProductOrder.microservices \
  --dependencies=web,data-jpa,validation,actuator \
  --version=1.0.0-SNAPSHOT \
  orderService

# Create Product Service
spring init \
  --boot-version=3.2.0 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=ProductService \
  --package-name=com.ProductOrder.microservices.product \
  --groupId=com.ProductOrder.microservices \
  --dependencies=web,data-jpa,validation,actuator \
  --version=1.0.0-SNAPSHOT \
  ProductService

cd ..

# Create proto-api module
mkdir -p proto-api
cd proto-api

spring init \
  --boot-version=3.2.0 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=proto-api \
  --package-name=com.ProductOrder.proto \
  --groupId=com.ProductOrder.proto \
  --dependencies=web \
  --version=1.0.0-SNAPSHOT \
  .

# Create proto directory structure
mkdir -p src/main/proto
echo "// Order service definition" > src/main/proto/order_service.proto
echo "// Product service definition" > src/main/proto/product_service.proto

cd ..

# Create Utils module
mkdir -p Utils
cd Utils

spring init \
  --boot-version=3.2.0 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=Utils \
  --package-name=com.ProductOrder.utils \
  --groupId=com.ProductOrder.utils \
  --dependencies=web \
  --version=1.0.0-SNAPSHOT \
  .

# Create utility directories
mkdir -p src/main/java/com/ProductOrder/DTOs
mkdir -p src/main/java/com/ProductOrder/Events
mkdir -p src/main/java/com/ProductOrder/Exceptions

cd ..

# Create docker-compose.yml
cat > docker-compose.yml << EOL
version: '3.8'
services:
  product-service:
    build: ./microservices/ProductService
    ports:
      - "8081:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  order-service:
    build: ./microservices/orderService
    ports:
      - "8082:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
EOL

# Update settings.gradle to include all modules
cat > settings.gradle << EOL
rootProject.name = 'ProductOrder'

include 'microservices:orderService'
include 'microservices:ProductService'
include 'proto-api'
include 'Utils'
EOL

# Update build.gradle with multi-module setup
cat > build.gradle << EOL
plugins {
    id 'org.springframework.boot' version '3.2.0' apply false
    id 'io.spring.dependency-management' version '1.1.4' apply false
    id 'java'
}

allprojects {
    group = 'com.ProductOrder'
    version = '1.0.0-SNAPSHOT'

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply plugin: 'java'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-web'
        implementation 'org.springframework.boot:spring-boot-starter-actuator'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
    }
}
EOL

# Create README.md with project structure
cat > README.md << EOL
# ProductOrder Microservices Project

## Project Structure
\`\`\`
ProductOrder/
├── .gradle/
├── .idea/
├── assets/
│   └── architecture.png
├── build/
├── gradle/
├── microservices/
│   ├── build/
│   ├── orderService/
│   ├── ProductService/
│   ├── src/
│   └── build.gradle
├── proto-api/
│   └── Proto files for gRPC definitions
├── src/
├── Utils/
│   ├── Custom exceptions
│   ├── Models and DTOs
│   ├── Events
│   └── Global error handling
├── build.gradle
├── settings.gradle
└── docker-compose.yml
\`\`\`
EOL

echo "ProductOrder project structure created successfully!"
echo "Navigate to the ProductOrder directory and run 'gradle build' to build the project."