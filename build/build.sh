# Build the backend
cd ../backend
nexe app.js -n "../dist/bugzilla-live-backend"

# Build the frontend
cd ../frontend
ant build_frontend

# Now zip everything up for the release
cd ..
zip -r dist/bugzilla-live.zip dist