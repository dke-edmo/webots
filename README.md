# Setup

Please review:

- https://cyberbotics.com/doc/guide/running-extern-robot-controllers?tab-os=linux&tab-language=java
- https://www.cyberbotics.com/doc/guide/using-java
- https://www.cyberbotics.com/doc/guide/using-your-ide?tab-language=java

I'm (Tomek) using Java SDK 1.8, language version 11, JetBrains InteliJ on Ubuntu 20.04 with Webots installed trough Snap.

Currently, the `experiment.wbt` is set up to use `.jar` files from `controllers`.

In order to generate `.jar` files with InteliJ you will need to create following project structure:
1. In project settings setup Module at `src` directory. The module will have to have dependency on library `$WEBOTS_HOME/lib/controller/java/Controller.jar`.
2. In project settings setup Artifact for `src` module with output directory at `controllers/JoinController`
3. In project settings setup Artifact for `src` module with output directory at `controllers/SupervisorController`
4. Make sure that both artifacts are included in project build. It's a check box in `Project Settings - Artifacts`.

Now you can build project.

Previously, to get Webots to work with external controllers I had to:

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
