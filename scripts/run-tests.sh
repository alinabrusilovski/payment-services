#!/bin/bash

echo "Installing dependencies..."
gradle build -x test

echo "Running tests..."
gradle test
