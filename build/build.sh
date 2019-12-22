# Build the backend
cd ../backend
nexe app.js -n "../dist/bugzilla-live-backend"

# Build the frontend
cd ../frontend
ant build_frontend