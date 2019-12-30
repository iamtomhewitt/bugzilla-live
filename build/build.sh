# Build the backend
cd ../backend
echo 
echo Building backend
echo =================
nexe app.js -n "../dist/bugzilla-live-backend"

# Build the frontend
cd ../frontend
echo 
echo Building frontend
echo =================
ant build_frontend

# Now zip everything up for the release
echo 
echo Building zip
echo ================
zip -r dist/bugzilla-live.zip dist