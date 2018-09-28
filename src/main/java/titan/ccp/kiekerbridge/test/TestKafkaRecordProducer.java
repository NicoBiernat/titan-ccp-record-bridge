package titan.ccp.kiekerbridge.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import titan.ccp.models.records.ActivePowerRecord;

public class TestKafkaRecordProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestKafkaRecordProducer.class);

  public static void main(final String[] args) {

    LOGGER.info("Start running...");

    final KafkaRecordSender<ActivePowerRecord> sender =
        new KafkaRecordSender<>("cc01:32400", "test");

    final ActivePowerRecord record = new ActivePowerRecord("identifier", 12345, 10.3);

    sender.write(record);
  }

}
