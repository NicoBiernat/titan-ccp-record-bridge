FROM openjdk:10-slim

ADD build/distributions/raritan-simulator.tar /

EXPOSE 80

CMD export RARITAN_SIMULATOR_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=$LOG_LEVEL &&
  /raritan-simulator/bin/raritan-simulator