# SWDesign 'Reactive' task.

## What is implemented:
* Backend, which uses Vert.X framework, paired with muTiny reactive framework bindings for Java.
* Frontend (not the most eye-appealing) using Vue.JS v3

## How to launch:
* Backend: issue `mvn compile exec:java` command, this would automatically download all dependencues and launch Vert.X
* Frontend: execute `npm run serve` to start development server. You have to start backend first, otherwise its port would be busy.

## How to check

After starting the backend, it would automatically download docker container for `testcontainers` and deploy PostgreSQL databse, which provides decent reactive bindings for Vert.X with muTiny.

After starting the frontend, node.js development server is configured to proxy all requests to the backend. It will show you the address and port (e.g. 8081) where frontend is available, but all API requests would be sent to port 8080 (where backend resides).

Simply navigate to `localhost:8081` in your browser and main component with UI would be shown.
When backend is stopped, all data is wiped from database.


## Why not RxJava?

Example from lecture used `rxjava` and `rxnetty`, but their versions were deprecated and newer ones are not compatible with MongoDB's reactive driver. I decided to search for the alternative frameworks and found Eclipse's Vert.X with muTiny.
