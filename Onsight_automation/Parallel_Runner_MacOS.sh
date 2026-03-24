#!/bin/bash

# Validate argument
if [ -z "$1" ]; then
  echo "Please provide an environment: DEV, PROD, STAGE, or UAT"
  exit 1
fi

# Convert argument to uppercase
ENV=$(echo "$1" | tr '[:lower:]' '[:upper:]')

# Check valid environments
case "$ENV" in
  DEV|PROD|STAGE|UAT)
    echo "Running tests for environment: $ENV"
    ;;
  *)
    echo "Invalid environment: $ENV"
    echo "Valid options: DEV, PROD, STAGE, UAT"
    exit 1
    ;;
esac

CURRENT_DIR=$(pwd)
# Start Android tests in background terminal window
osascript <<EOF
tell application "Terminal"
    do script "cd \"$CURRENT_DIR\"; rm Appium_Logs_${ENV}_Android.txt; mvn clean test -P${ENV}_android>>Appium_Logs_${ENV}_Android.txt; exit; exit"
end tell
EOF

# Start iOS tests in another background terminal window (with parallel flag)
osascript <<EOF
tell application "Terminal"
    do script "cd \"$CURRENT_DIR\"; rm Appium_Logs_${ENV}_iOS.txt; mvn clean test -P${ENV}_ios>>Appium_Logs_${ENV}_iOS.txt -Dparallel=true; exit; exit"
end tell
EOF
