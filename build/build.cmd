@echo off
REM Build the backend
cd ../backend
echo. 
echo Building backend
echo =================
nexe app.js -n -b "../dist/bugzilla-live-backend"

REM Build the frontend
cd ../frontend
echo. 
echo Building frontend
echo =================
ant build_frontend

REM Now zip everything up for the release
echo. 
echo Building zip
echo ================
7z a -tzip dist/bugzilla-live.zip dist