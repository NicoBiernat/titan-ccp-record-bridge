FROM openjdk:11-slim

ADD build/distributions/load-tester-bridge.tar /

CMD export JAVA_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=$LOG_LEVEL \
    && /load-tester-bridge/bin/load-tester-bridge