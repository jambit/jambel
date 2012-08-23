jambel
======

three color signal light for monitoring software build status

Usage
-----
Download the binaries of the [jambel control](https://github.com/downloads/jambit/jambel/jambel-1.0-SNAPSHOT.zip).

The file `/etc/jambel.json` is used for configuring the Jambel. All jobs that are mapped onto this Jambel are listed in `jobs`. This includes:
- the URL of the Jenkins job
- the update mode of the job, or how he obtains status updates (either the Jenkins instance POSTs updates or the job URL is polled)
- if the job is polled, the polling interval can be configured
The signal light configuration includes the following items:
- the position of the green light (`top` vs. `bottom`)
- the host and port of the signal light
- the timeout that is used when waiting for a reply and upon establishing a TCP connection
- the interval after which the current light status is resent in order to keep the signal light alive. Thus, the signal light can be switched off and on again and receive a new status update without a new build needing to happen
Furthermore, the port of the web interface can be configured.

The app must be started within the jambel folder with the command `bin/jambel`.

The web interface of the Jambel can be found at `http://<HOSTNAME>:<HTTP-PORT>/web/`. It can be used to see the last statuses of the jobs, the last sent configuration of the signal light and to reset the signal light.


Contributing
------------
The project is built with [Gradle](http://www.gradle.org/). Use the commited Gradle wrapper script to obtain Gradle. Type `./gradlew tasks` to get an overview of executable Gradle tasks. The task `distZip` is used to build a ZIP file that can be distributed.


Potential extensions
--------------------
Here is a list of ideas what to improve:
- control multiple signal lights and offer a flexible configuration from jobs to signal lights
- WebSocket for web UI to monitor the build status in the browser in real time
- set up application task of Gradle to generate start scripts for a daemon process (maybe using http://gradle.org/docs/current/dsl/org.gradle.api.tasks.application.CreateStartScripts.html)
