# Setup

Please review:

- https://cyberbotics.com/doc/guide/running-extern-robot-controllers?tab-os=linux&tab-language=java
- https://www.cyberbotics.com/doc/guide/using-java
- https://www.cyberbotics.com/doc/guide/using-your-ide?tab-language=java

I'm (Tomek) using Java SDK 1.8, language version 11, JetBrains InteliJ on Ubuntu 20.04 with Webots installed trough Snap.

Currently, the `experiment.wbt` is

To get Webots to work with external controllers I had to:
- Add to my `~./profile`
```
export WEBOTS_HOME=/snap/webots/current/usr/share/webots
export LD_LIBRARY_PATH=$WEBOTS_HOME/lib/controller
```
- Add global library in project structure in Intelij
```
/snap/webots/current/usr/share/webots/lib/controller/java/Controller.jar
```
- Add following VM option to my run configuration in Intelij
```
-Djava.library.path=/snap/webots/current/usr/share/webots/lib/controller/java
```