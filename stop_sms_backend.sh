#!/bin/bash

PID_FILE="sms_backend.pid"
PORT=8080

# Function to check if a process is running by PID
is_process_running() {
    ps -p "$1" > /dev/null 2>&1
}

# Function to check if the port is still in use
is_port_in_use() {
    lsof -i :$PORT > /dev/null 2>&1
}

# Stop the application if a PID file exists
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if is_process_running "$PID"; then
        echo "Stopping Spring Boot application (PID: $PID)..."
        kill "$PID"
        sleep 3  # Wait for graceful shutdown

        if is_process_running "$PID"; then
            echo "Force stopping process (PID: $PID)..."
            kill -9 "$PID"
        fi

        rm -f "$PID_FILE"
        echo "Application stopped successfully."
    else
        echo "No running application found with PID $PID. Removing stale PID file."
        rm -f "$PID_FILE"
    fi
else
    echo "No PID file found. Checking for processes on port $PORT..."
    
    # If no PID file, check for processes using port 8080
    if is_port_in_use; then
        PIDS=$(lsof -t -i :$PORT)
        echo "Stopping process(es) using port $PORT: $PIDS"
        kill -9 $PIDS
        echo "Process(es) on port $PORT stopped."
    else
        echo "No running application found."
    fi
fi