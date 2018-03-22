package titan.ccp.kiekerbridge.raritan;

import java.util.function.Function;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.model.PowerConsumptionRecord;

public class RaritanJsonTransformer implements Function<String, IMonitoringRecord> {

	@Override
	public IMonitoringRecord apply(String json) {
		// TODO transform
		final byte[] identifier = { 1, 2, 3, 4, 5, 6, 7, 8 };
		final long timestamp = System.nanoTime();
		final int consumption = 10;
		return new PowerConsumptionRecord(identifier, timestamp, consumption);
	}

}
