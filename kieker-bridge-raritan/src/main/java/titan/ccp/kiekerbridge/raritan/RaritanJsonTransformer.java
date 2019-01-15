package titan.ccp.kiekerbridge.raritan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import kieker.common.record.IMonitoringRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import titan.ccp.models.records.ActivePowerRecord;

/**
 * {@link Function} that transforms Raritan JSON push messages into a list of
 * {@link IMonitoringRecord}s.
 */
public class RaritanJsonTransformer implements Function<PushMessage, List<IMonitoringRecord>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RaritanJsonTransformer.class);

  private static final String SENSORS_KEY = "sensors";
  private static final String DEVICE_KEY = "device";
  private static final String LABEL_KEY = "label";
  private static final String ROWS_KEY = "rows";
  private static final String TIMESTAMP_KEY = "timestamp";
  private static final String RECORDS_KEY = "records";
  private static final String AVG_VALUE_KEY = "avgValue";
  private static final String ID_KEY = "id";

  private static final String RELEVANT_SENSOR_NAME = "activePower";

  private final JsonParser jsonParser = new JsonParser();

  private final boolean inputTimestampsInMs;

  public RaritanJsonTransformer() {
    this(false);
  }

  public RaritanJsonTransformer(final boolean inputTimestampsInMs) {
    this.inputTimestampsInMs = inputTimestampsInMs;
  }

  @Override
  public List<IMonitoringRecord> apply(final PushMessage pushMessage) { // NOPMD
    try {
      // final Optional<String> pduId = pushMessage.getId();
      final String json = pushMessage.getMessage();
      final JsonObject rootObject = this.jsonParser.parse(json).getAsJsonObject();
      final JsonArray sensors = rootObject.get(SENSORS_KEY).getAsJsonArray();
      final int[] relevantSensorIndices = this.getReleventSensorIndices(sensors);
      final String[] sensorLabels = this.getSensorLabels(sensors, relevantSensorIndices);
      final JsonArray rows = rootObject.get(ROWS_KEY).getAsJsonArray();

      final List<IMonitoringRecord> monitoringRecords = new ArrayList<>(rows.size());
      for (final JsonElement rowJsonElement : rows) {
        final JsonObject row = rowJsonElement.getAsJsonObject();
        final long timestampReceived = row.get(TIMESTAMP_KEY).getAsLong();
        final long timestampInMs =
            this.inputTimestampsInMs ? timestampReceived : timestampReceived * 1_000; // NOCS
        final JsonArray records = row.get(RECORDS_KEY).getAsJsonArray();

        final Optional<String> pduId =
            this.determinePduId(relevantSensorIndices, sensorLabels, records);

        for (int i = 0; i < relevantSensorIndices.length; i++) {
          final int sensorIndex = relevantSensorIndices[i];
          final String sensorLabel = (pduId.isPresent() ? pduId.get() + '.' : "") + sensorLabels[i];

          final JsonObject relevantRecord = records.get(sensorIndex).getAsJsonObject();
          final double value = relevantRecord.get(AVG_VALUE_KEY).getAsDouble();

          monitoringRecords.add(new ActivePowerRecord(sensorLabel, timestampInMs, value));
        }

      }

      return monitoringRecords;

    } catch (JsonParseException e) {
      LOGGER.warn("Message could not be parsed. ", e);
      return List.of();
    }
  }

  private Optional<String> determinePduId(final int[] relevantSensorIndices, // NOCS NOPMD
      final String[] sensorLabels, final JsonArray records) {
    boolean sensor20used = false;
    boolean sensor25used = false;
    boolean sensor15used = false;
    for (int i = 0; i < relevantSensorIndices.length; i++) {
      final int sensorIndex = relevantSensorIndices[i];
      final JsonObject relevantRecord = records.get(sensorIndex).getAsJsonObject();
      if (sensorLabels[i].equals("20") && relevantRecord.get(AVG_VALUE_KEY).getAsDouble() != 0.0) {
        sensor20used = true;
      } else if (sensorLabels[i].equals("25")
          && relevantRecord.get(AVG_VALUE_KEY).getAsDouble() != 0.0) {
        sensor25used = true;
      } else if (sensorLabels[i].equals("15")
          && relevantRecord.get(AVG_VALUE_KEY).getAsDouble() != 0.0) {
        sensor15used = true;
      }
    }
    String determinedPduId = "undefined";
    if (sensor20used) {
      determinedPduId = "1";
      LOGGER.info("Determined PDU ID 1");
    } else if (sensor25used) {
      determinedPduId = "2";
      LOGGER.info("Determined PDU ID 2");
    } else if (sensor15used) {
      determinedPduId = "3";
      LOGGER.info("Determined PDU ID 3;");
    }
    return Optional.of(determinedPduId);
  }

  private int[] getReleventSensorIndices(final JsonArray sensors) {
    return IntStream.range(0, sensors.size()).filter(i -> sensors.get(i).getAsJsonObject()
        .get(ID_KEY).getAsString().equals(RELEVANT_SENSOR_NAME)).toArray();
  }

  private String[] getSensorLabels(final JsonArray sensors, final int... releventSensorIndices) {
    return IntStream.of(releventSensorIndices).mapToObj(i -> sensors.get(i).getAsJsonObject()
        .get(DEVICE_KEY).getAsJsonObject().get(LABEL_KEY).getAsString())
        .toArray(l -> new String[l]);
  }

}

