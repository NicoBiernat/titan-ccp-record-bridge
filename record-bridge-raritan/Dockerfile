FROM openjdk:10-slim

ADD build/distributions/titanccp-kieker-bridge-raritan.tar /

EXPOSE 80

CMD export JAVA_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=$LOG_LEVEL \
    && /titanccp-record-bridge-raritan/bin/titanccp-record-bridge-raritan