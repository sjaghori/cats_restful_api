#!/usr/bin/env bash
docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=cats -e POSTGRES_USER=cats -e POSTGRES_DB=cats -d postgres:13.1-alpine