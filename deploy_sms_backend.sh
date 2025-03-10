#!/bin/bash

# Define the port and process ID file
PORT=8080
PID_FILE="sms_backend.pid"
LOG_FILE="sms_backend.log"

# Function to check if the port is in use
is_port_in_use() {
    lsof -i :$PORT > /dev/null
}

# Function to stop the running application
stop_running_app() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if kill -0 "$PID" 2>/dev/null; then
            echo "Stopping running application (PID: $PID)..."
            kill "$PID"
            sleep 2
            if ps -p "$PID" > /dev/null; then
                echo "Force stopping process..."
                kill -9 "$PID"
            fi
            rm -f "$PID_FILE"
            echo "Application stopped."
        fi
    elif is_port_in_use; then
        PID=$(lsof -t -i :$PORT)
        echo "Stopping application using port $PORT (PID: $PID)..."
        kill -9 "$PID"
    fi
}

# Get the Spring profile from the environment variable or default to 'dev'
PROFILE=${SPRING_PROFILES_ACTIVE:-dev}

# Navigate to the project directory
cd /Users/giovannisuter/dev/projects/sms/back-end/workspace/sms-backend || { echo "Directory not found! Exiting..."; exit 1; }

# Clean and install the project
echo "Cleaning and installing the project..."
./mvnw clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "Build failed! Exiting..."
    exit 1
fi

# Stop the running application if it's already running
stop_running_app

# Start the Spring Boot application in the background with nohup
echo "Starting Spring Boot application with profile $PROFILE..."
nohup ./mvnw spring-boot:run -Dspring-boot.run.profiles=$PROFILE > "$LOG_FILE" 2>&1 &

# Save the process ID
echo $! > "$PID_FILE"

# Allow a brief moment for the server to start
sleep 5

# Check if the application is running on port 8080
if is_port_in_use; then
    echo "Spring Boot application started successfully on port 8080."
else
    echo "Failed to start the Spring Boot application. Please check logs in $LOG_FILE."
    exit 1
fi

# Open the application URL in Firefox
echo "Opening application in Firefox..."
open -a "Firefox" http://localhost:8080/sms/students

echo "Application is running on port 8080. You can access it at http://localhost:8080/sms/students"