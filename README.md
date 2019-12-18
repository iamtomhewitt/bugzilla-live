[![Build Status](https://travis-ci.org/iamtomhewitt/bugzilla-live.svg?branch=Issue%2311)](https://travis-ci.org/iamtomhewitt/bugzilla-live)

# 🐛 Bugzilla Live

An application to show bugs from Bugzilla using the provided API. Originally as a desktop tool to help avoid spreadsheets to manage bugs at work, it has now become a hobby project to try out new languages or programming techniques.

## 🔧 Getting Started
The app is split into a JavaFX frontend, and a Node Express backend. The front end makes requests to the backend, which in turn makes requests to the Bugzilla API or makes config changes etc.

## 🏃‍♂️ Run Locally

### Frontend
* Run `gui.login.service.LoginService` in your IDE.

### Backend
* `npm install`
* `node app.js`

## 👷🏻‍♂️ Build
### Frontend
* Use `mvn clean install package` to create the jar file.
* Run `java -jar target/bugzilla-version.jar` to run the built jar.

### Backend
* Use `nexe app.js -n "bugzilla-live-backend"` to create an executable of the backend.
* Run the produced executable.

## 🎉 Contributing
Visit the issues tab or create a new one.
Follow [this guide](CODESTYLE.md) for a guide on how code should look! 🙂