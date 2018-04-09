package titan.ccp.kiekerbridge.raritan;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.model.PowerConsumptionRecord;

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

	@Override
	public List<IMonitoringRecord> apply(final String json) {
		final JsonObject rootObject = this.jsonParser.parse(json).getAsJsonObject();
		final JsonArray sensors = rootObject.get(SENSORS_KEY).getAsJsonArray();
		final int relevantSensorIndex = this.getReleventSensorIndex(sensors);
		final String sensorLabel = sensors.get(relevantSensorIndex).getAsJsonObject().get(DEVICE_KEY).getAsJsonObject()
				.get(LABEL_KEY).getAsString();
		final JsonArray rows = rootObject.get(ROWS_KEY).getAsJsonArray();

		final List<IMonitoringRecord> monitoringRecords = new ArrayList<>(rows.size());
		for (final JsonElement rowJsonElement : rows) {
			final JsonObject row = rowJsonElement.getAsJsonObject();
			final long timestamp = row.get(TIMESTAMP_KEY).getAsLong();
			final JsonArray records = row.get(RECORDS_KEY).getAsJsonArray();
			final JsonObject relevantRecord = records.get(relevantSensorIndex).getAsJsonObject();
			final int value = (int) relevantRecord.get(AVG_VALUE_KEY).getAsDouble(); // TODO temp casting to int

			monitoringRecords.add(new PowerConsumptionRecord(sensorLabel, timestamp, value));
		}
		return monitoringRecords;
	}

	private int getReleventSensorIndex(final JsonArray sensors) {
		for (int i = 0; i < sensors.size(); i++) {
			final String sensorName = sensors.get(i).getAsJsonObject().get(ID_KEY).getAsString();
			if (sensorName.equals(RELEVANT_SENSOR_NAME)) {
				return i;
			}
		}
		throw new JsonParseException("No sensor with id=" + RELEVANT_SENSOR_NAME + " found");
	}

}
