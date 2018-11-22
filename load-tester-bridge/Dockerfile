FROM openjdk:10-slim

ADD build/distributions/load-tester-bridge.tar /

CMD export RARITAN_SIMULATOR_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=$LOG_LEVEL
    && /load-tester-bridge/bin/load-tester-bridge