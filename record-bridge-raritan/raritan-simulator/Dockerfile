FROM openjdk:11-slim

ADD build/distributions/raritan-simulator.tar /

EXPOSE 80

CMD export JAVA_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=$LOG_LEVEL \
    && /raritan-simulator/bin/raritan-simulator
