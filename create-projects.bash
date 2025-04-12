#!/usr/bin/env bash

# Script for creating ProductOrder microservices project skeleton
# Compatible with Linux, macOS, and Windows (with Git Bash or WSL)

echo "Creating ProductOrder project structure..."

# Create main project directory if not exists
mkdir -p ProductOrder
cd ProductOrder

# Create main project using Spring CLI
spring init \
  --boot-version=3.4.3 \
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
  --boot-version=3.4.3 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=orderService \
  --package-name=com.ProductOrder.microservices.order \
  --groupId=com.ProductOrder.microservices \
  --dependencies=web,data-mongodb,validation,actuator \
  --version=1.0.0-SNAPSHOT \
  orderService

# Create Product Service
spring init \
  --boot-version=3.4.3 \
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
  --boot-version=3.4.3 \
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

cd ..

# Create Utils module
mkdir -p Utils
cd Utils

spring init \
  --boot-version=3.4.3 \
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

# Create empty docker-compose.yml
touch docker-compose.yml

echo "ProductOrder project skeleton structure created successfully!"
echo "Navigate to the ProductOrder directory to begin developing your microservices."