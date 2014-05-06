Code Organization
======

**Directory Structure**

  
```
  code
    -  lib
         +common
         +test-framework
    -  queuengin
    -  sparkngin
    -  scribengin
    -  release
```

1. The project lib/common project contains the common classess and util classes.
2. The project lib/test-framework contains the api and classes for the test, mock and embbeded environment.
3. The project queuengin contains the api and implementation for the kafka and kinesis queue.
4. The project sparkngin 
5. The project scribengin
6. The project release contains the code and script to build and release

Gradle
======


1. To build
  - cd NeverwinterDP/code
  - gradle clean build
2. Other 

Eclipse
====

1. To generate the eclipse configuration
  - cd NeverwinterDP/code
  - gradle eclipse
2. Open eclipse
  - Choose File > Import
  - Choose General > Existing Projects into Workspace
  - Check Select root directory and browse to path/NeverwinterDP/code
  - Select all the projects except the code project, then click Finish
