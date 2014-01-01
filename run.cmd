@echo off

echo Starting Application...

rem Go to the correct directory.
cd "%~p0%"

rem This is the class that will be launched.
set CLASS=com.arcblaze.archire.Server

rem Set Java configuration options.
set JAVA_OPTS=
set JAVA_OPTS=%JAVA_OPTS% -ea
set JAVA_OPTS=%JAVA_OPTS% -Xmx256m
set JAVA_OPTS=%JAVA_OPTS% -Darchire.configurationFile=conf/archire-config.properties
set JAVA_OPTS=%JAVA_OPTS% -Dlogback.configurationFile=conf/archire-logging.xml

rem Build the classpath.
set CLASSPATH=archire-dist\target\lib\*

rem Start the app.
java %JAVA_OPTS% -classpath "%CLASSPATH%" %CLASS%

rem Pause so we can see the output.
pause

