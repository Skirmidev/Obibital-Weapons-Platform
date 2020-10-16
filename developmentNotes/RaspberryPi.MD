when building for raspberry pi, an additional package was needed. ARM caused platform complications resulting in a raw alpine image with additional java installed after the fact being the simplest option

to run succesfully on raspberry pi:
 - apt install openjdk-8-jre
 - apt install openjdk-8-jdk
 - set java home as appropriate
 - apt-get install libjffi-jni