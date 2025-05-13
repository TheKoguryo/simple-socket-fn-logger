# simple-socket-fn-logger - updates

## Install gradle 7 and JDK 19 for gradle 7

```
sudo su
wget https://services.gradle.org/distributions/gradle-7.6.4-bin.zip
mkdir /opt/gradle
unzip -d /opt/gradle gradle-7.6.4-bin.zip
ls /opt/gradle/gradle-7.6.4

yum list jdk*
yum install jdk-19-headless
yum install jdk-19-headful
```

## Setup for opc user

Edit `~/.bashrc`

```
export PATH=$PATH:/opt/gradle/gradle-7.6.4/bin
```

```
source .bashrc
```

Edit or Create `~/.gradle/gradle.properties`

```
org.gradle.java.home=/usr/lib/jvm/jdk-19-oracle-x64
```

Change current JDK version to JDK 19

```
sudo alternatives --config java
java -version
```

## Setup for opc user

```
cd simple-socket-fn-logger
```

### Migration - Already Done

```
./gradlew wrapper --gradle-version 7.6.4 --distribution-type all
```

### Build & Run 

```
./gradlew shadowJar
java -jar build/libs/simple-socket-fn-logger-1.0.0-all.jar
```