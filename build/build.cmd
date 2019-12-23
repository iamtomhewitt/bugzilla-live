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

# Copy the changelog for the release package
cd ..
echo 
echo Copying CHANGELOG
echo =================
cp CHANGELOG.md dist/CHANGELOG.md

# Now zip everything up for the release
echo 
echo Building zip
echo ================
zip -r dist/bugzilla-live.zip dist