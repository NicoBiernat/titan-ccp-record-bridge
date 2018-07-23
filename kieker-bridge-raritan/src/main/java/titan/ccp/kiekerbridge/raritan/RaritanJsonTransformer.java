package titan.ccp.kiekerbridge.raritan;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.models.records.ActivePowerRecord;

public class RaritanJsonTransformer implements Function<String, List<IMonitoringRecord>> {

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
	public List<IMonitoringRecord> apply(final String json) {
		final JsonObject rootObject = this.jsonParser.parse(json).getAsJsonObject();
		final JsonArray sensors = rootObject.get(SENSORS_KEY).getAsJsonArray();
		final int[] relevantSensorIndices = this.getReleventSensorIndices(sensors);
		final String[] sensorLabels = this.getSensorLabels(sensors, relevantSensorIndices);
		final JsonArray rows = rootObject.get(ROWS_KEY).getAsJsonArray();

		final List<IMonitoringRecord> monitoringRecords = new ArrayList<>(rows.size());
		for (final JsonElement rowJsonElement : rows) {
			final JsonObject row = rowJsonElement.getAsJsonObject();
			final long timestampReceived = row.get(TIMESTAMP_KEY).getAsLong();
			final long timestampInMs = this.inputTimestampsInMs ? timestampReceived : timestampReceived * 1_000;
			final JsonArray records = row.get(RECORDS_KEY).getAsJsonArray();

			for (int i = 0; i < relevantSensorIndices.length; i++) {
				final int sensorIndex = relevantSensorIndices[i];
				final String sensorLabel = sensorLabels[i];

				final JsonObject relevantRecord = records.get(sensorIndex).getAsJsonObject();
				final double value = relevantRecord.get(AVG_VALUE_KEY).getAsDouble();

				monitoringRecords.add(new ActivePowerRecord(sensorLabel, timestampInMs, value));
			}

		}

		return monitoringRecords;
	}

	private int[] getReleventSensorIndices(final JsonArray sensors) {
		return IntStream.range(0, sensors.size())
				.filter(i -> sensors.get(i).getAsJsonObject().get(ID_KEY).getAsString().equals(RELEVANT_SENSOR_NAME))
				.toArray();
	}

	private String[] getSensorLabels(final JsonArray sensors, final int[] releventSensorIndices) {
		return IntStream.of(releventSensorIndices).mapToObj(
				i -> sensors.get(i).getAsJsonObject().get(DEVICE_KEY).getAsJsonObject().get(LABEL_KEY).getAsString())
				.toArray(l -> new String[l]);
	}

}
