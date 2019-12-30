<p align="center">
	<img src="https://travis-ci.org/iamtomhewitt/bugzilla-live.svg"/>
	<img src="https://img.shields.io/github/v/tag/iamtomhewitt/bugzilla-live?color=success&style=flat-square&label=latest%20version">
</p>

# 🐛 Bugzilla Live

An application to show bugs from Bugzilla using the provided API. Originally as a desktop tool to help avoid spreadsheets to manage bugs at work, it has now become a hobby project to try out new languages or programming techniques.

## 🔧 Getting Started
The app is split into a JavaFX frontend, and a Node Express backend. The frontend makes requests to the backend, which in turn makes requests to the Bugzilla API or makes config changes etc.

## 🏃‍♂️ Run Locally
* <b>Frontend</b>: Run `ui.login.Login.java` in your IDE.
* <b>Backend</b>:
	* `npm install`
	* `node app.js`

## 👷🏻‍♂️ Build
Run `build/build.sh` (or `.cmd` if on Windows) to build the frontend and the backend, and create a zip of both of them.

## 🏷 Release
Releases are handled by Travis. When a tag is pushed to the repo, Travis creates a release based on the `/dist` folder, and creates a release on Github.

## 🎉 Contributing
Visit the issues tab or create a new one.
Follow [this guide](CODESTYLE.md) for a guide on how code should look! 🙂