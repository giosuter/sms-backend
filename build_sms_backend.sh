#!/bin/bash

# Navigate to the project directory
cd /Users/giovannisuter/dev/projects/sms/back-end/workspace/sms-backend || { echo "Directory not found! Exiting..."; exit 1; }

# Clean and install the project
echo "Cleaning and installing the project..."
./mvnw clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "Build failed! Exiting..."
    exit 1
fi

echo "Build successful!"