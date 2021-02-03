@echo off
set base=%~d0%~p0

docker-compose -f %base%scndb\docker-compose.yml up -d
