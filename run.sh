#!/bin/sh

echo "Starting Application..."

# Go to the correct directory.
cd $(dirname $0)

# This is the class that will be launched.
CLASS="com.arcblaze.archire.Server"

# Set Java configuration options.
JAVA_OPTS=
JAVA_OPTS="$JAVA_OPTS -ea"
JAVA_OPTS="$JAVA_OPTS -Xmx256m"
JAVA_OPTS="$JAVA_OPTS -Darchire.configurationFile=conf/archire-config.properties"
JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=conf/archire-logging.xml"

# Build the classpath.
CLASSPATH="archire-dist/target/lib/*"

# Start the app.
java $JAVA_OPTS -classpath "$CLASSPATH" $CLASS

