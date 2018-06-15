package titan.ccp.kiekerbridge.raritan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.models.records.ActivePowerRecord;

public class RaritanJsonTransformerTest {

	// TODO read from file
	private static final String IBAK_RARITAN_OUTLET_SINGLE_SENSOR_JSON = "{\n" + "  \"sensors\": [\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"voltage\",\n" + "      \"readingtype\": 0,\n"
			+ "      \"metadata\": {\n" + "        \"type\": {\n" + "          \"readingtype\": 0,\n"
			+ "          \"type\": 1,\n" + "          \"unit\": 1\n" + "        },\n" + "        \"decdigits\": 0,\n"
			+ "        \"accuracy\": 0,\n" + "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n"
			+ "        \"noiseThreshold\": 0,\n" + "        \"range\": {\n" + "          \"lower\": 0,\n"
			+ "          \"upper\": 500\n" + "        },\n" + "        \"thresholdCaps\": {\n"
			+ "          \"hasUpperCritical\": true,\n" + "          \"hasUpperWarning\": true,\n"
			+ "          \"hasLowerWarning\": true,\n" + "          \"hasLowerCritical\": true\n" + "        }\n"
			+ "      }\n" + "    },\n" + "    {\n" + "      \"device\": {\n" + "        \"type\": 4,\n"
			+ "        \"label\": \"13\",\n" + "        \"line\": 0\n" + "      },\n" + "      \"id\": \"current\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 2,\n" + "          \"unit\": 2\n"
			+ "        },\n" + "        \"decdigits\": 3,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0.001,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0.04,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 70\n" + "        },\n"
			+ "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        }\n" + "      }\n" + "    },\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"activePower\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 4,\n" + "          \"unit\": 3\n"
			+ "        },\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 21000\n" + "        },\n"
			+ "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        }\n" + "      }\n" + "    },\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"apparentPower\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 4,\n" + "          \"unit\": 4\n"
			+ "        },\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 21000\n" + "        },\n"
			+ "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        }\n" + "      }\n" + "    },\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"powerFactor\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 5,\n" + "          \"unit\": 0\n"
			+ "        },\n" + "        \"decdigits\": 2,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0.01,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 1\n" + "        },\n"
			+ "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        }\n" + "      }\n" + "    },\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"activeEnergy\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 6,\n" + "          \"unit\": 5\n"
			+ "        },\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 1099511627776\n"
			+ "        },\n" + "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        }\n" + "      }\n" + "    },\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"lineFrequency\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 7,\n" + "          \"unit\": 8\n"
			+ "        },\n" + "        \"decdigits\": 1,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0.1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 6500\n" + "        },\n"
			+ "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        }\n" + "      }\n" + "    },\n" + "    {\n"
			+ "      \"device\": {\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      },\n" + "      \"id\": \"outletState\",\n"
			+ "      \"readingtype\": 1,\n" + "      \"metadata\": {\n" + "        \"type\": {\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 0,\n" + "          \"unit\": 0\n"
			+ "        },\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": {\n" + "          \"lower\": 0,\n" + "          \"upper\": 0\n" + "        },\n"
			+ "        \"thresholdCaps\": {\n" + "          \"hasUpperCritical\": false,\n"
			+ "          \"hasUpperWarning\": false,\n" + "          \"hasLowerWarning\": false,\n"
			+ "          \"hasLowerCritical\": false\n" + "        }\n" + "      }\n" + "    }\n" + "  ],\n"
			+ "  \"rows\": [\n" + "    {\n" + "      \"timestamp\": 1521817336,\n" + "      \"records\": [\n"
			+ "        {\n" + "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n"
			+ "          \"state\": 5,\n" + "          \"minValue\": 227.569995,\n"
			+ "          \"avgValue\": 227.569995,\n" + "          \"maxValue\": 227.569995\n" + "        },\n"
			+ "        {\n" + "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n"
			+ "          \"state\": 5,\n" + "          \"minValue\": 0.388,\n" + "          \"avgValue\": 0.388,\n"
			+ "          \"maxValue\": 0.388\n" + "        },\n" + "        {\n" + "          \"available\": true,\n"
			+ "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 87.216004,\n" + "          \"avgValue\": 87.216004,\n"
			+ "          \"maxValue\": 87.216004\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 88.536004,\n" + "          \"avgValue\": 88.536004,\n"
			+ "          \"maxValue\": 88.536004\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 0.985091,\n" + "          \"avgValue\": 0.985091,\n"
			+ "          \"maxValue\": 0.985091\n" + "        },\n" + "        {\n" + "          \"available\": true,\n"
			+ "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 325047.687001,\n" + "          \"avgValue\": 325047.687001,\n"
			+ "          \"maxValue\": 325047.687001\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 49.999999,\n" + "          \"avgValue\": 49.999999,\n"
			+ "          \"maxValue\": 49.999999\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 8,\n"
			+ "          \"minValue\": 0,\n" + "          \"avgValue\": 0,\n" + "          \"maxValue\": 0\n"
			+ "        }\n" + "      ]\n" + "    },\n" + "    {\n" + "      \"timestamp\": 1521817380,\n"
			+ "      \"records\": [\n" + "        {\n" + "          \"available\": true,\n"
			+ "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 226.929995,\n" + "          \"avgValue\": 227.428177,\n"
			+ "          \"maxValue\": 227.759995\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 0.386,\n" + "          \"avgValue\": 0.395523,\n"
			+ "          \"maxValue\": 0.457\n" + "        },\n" + "        {\n" + "          \"available\": true,\n"
			+ "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 86.580004,\n" + "          \"avgValue\": 88.840254,\n"
			+ "          \"maxValue\": 102.899005\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 87.894004,\n" + "          \"avgValue\": 90.137982,\n"
			+ "          \"maxValue\": 103.916005\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 0.984755,\n" + "          \"avgValue\": 0.985565,\n"
			+ "          \"maxValue\": 0.990213\n" + "        },\n" + "        {\n" + "          \"available\": true,\n"
			+ "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 325047.711001,\n" + "          \"avgValue\": 325048.24507,\n"
			+ "          \"maxValue\": 325048.772001\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 49.999999,\n" + "          \"avgValue\": 49.999999,\n"
			+ "          \"maxValue\": 49.999999\n" + "        },\n" + "        {\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 8,\n"
			+ "          \"minValue\": 0,\n" + "          \"avgValue\": 0,\n" + "          \"maxValue\": 0\n"
			+ "        }\n" + "      ]\n" + "    }\n" + "  ]\n" + "}\n" + "";

	private static final String IBAK_RARITAN_OUTLET_TWO_SENSORS_JSON = "{\n" + "   \"sensors\":[\n" + "      {\n"
			+ "         \"device\":{\n" + "            \"type\":4,\n" + "            \"label\":\"47\",\n"
			+ "            \"line\":0\n" + "         },\n" + "         \"id\":\"activePower\",\n"
			+ "         \"readingtype\":0,\n" + "         \"metadata\":{\n" + "            \"type\":{\n"
			+ "               \"readingtype\":0,\n" + "               \"type\":4,\n" + "               \"unit\":3\n"
			+ "            },\n" + "            \"decdigits\":0,\n" + "            \"accuracy\":0.000000,\n"
			+ "            \"resolution\":1.000000,\n" + "            \"tolerance\":0.000000,\n"
			+ "            \"noiseThreshold\":0.000000,\n" + "            \"range\":{\n"
			+ "               \"lower\":0.000000,\n" + "               \"upper\":21000.000000\n" + "            },\n"
			+ "            \"thresholdCaps\":{\n" + "               \"hasUpperCritical\":true,\n"
			+ "               \"hasUpperWarning\":true,\n" + "               \"hasLowerWarning\":true,\n"
			+ "               \"hasLowerCritical\":true\n" + "            }\n" + "         }\n" + "      },\n"
			+ "      {\n" + "         \"device\":{\n" + "            \"type\":4,\n" + "            \"label\":\"46\",\n"
			+ "            \"line\":0\n" + "         },\n" + "         \"id\":\"activePower\",\n"
			+ "         \"readingtype\":0,\n" + "         \"metadata\":{\n" + "            \"type\":{\n"
			+ "               \"readingtype\":0,\n" + "               \"type\":4,\n" + "               \"unit\":3\n"
			+ "            },\n" + "            \"decdigits\":0,\n" + "            \"accuracy\":0.000000,\n"
			+ "            \"resolution\":1.000000,\n" + "            \"tolerance\":0.000000,\n"
			+ "            \"noiseThreshold\":0.000000,\n" + "            \"range\":{\n"
			+ "               \"lower\":0.000000,\n" + "               \"upper\":21000.000000\n" + "            },\n"
			+ "            \"thresholdCaps\":{\n" + "               \"hasUpperCritical\":true,\n"
			+ "               \"hasUpperWarning\":true,\n" + "               \"hasLowerWarning\":true,\n"
			+ "               \"hasLowerCritical\":true\n" + "            }\n" + "         }\n" + "      }\n"
			+ "   ],\n" + "   \"rows\":[\n" + "      {\n" + "         \"timestamp\":1529057441,\n"
			+ "         \"records\":[\n" + "            {\n" + "               \"available\":true,\n"
			+ "               \"takenValidSamples\":1,\n" + "               \"state\":5,\n"
			+ "               \"minValue\":0.000000,\n" + "               \"avgValue\":0.000000,\n"
			+ "               \"maxValue\":0.000000\n" + "            },\n" + "            {\n"
			+ "               \"available\":true,\n" + "               \"takenValidSamples\":1,\n"
			+ "               \"state\":5,\n" + "               \"minValue\":34.130002,\n"
			+ "               \"avgValue\":34.130002,\n" + "               \"maxValue\":34.130002\n" + "            }\n"
			+ "         ]\n" + "      },\n" + "      {\n" + "         \"timestamp\":1529057442,\n"
			+ "         \"records\":[\n" + "            {\n" + "               \"available\":true,\n"
			+ "               \"takenValidSamples\":1,\n" + "               \"state\":5,\n"
			+ "               \"minValue\":0.000000,\n" + "               \"avgValue\":0.000000,\n"
			+ "               \"maxValue\":0.000000\n" + "            },\n" + "            {\n"
			+ "               \"available\":true,\n" + "               \"takenValidSamples\":1,\n"
			+ "               \"state\":5,\n" + "               \"minValue\":34.126002,\n"
			+ "               \"avgValue\":34.126002,\n" + "               \"maxValue\":34.126002\n" + "            }\n"
			+ "         ]\n" + "      },\n" + "      {\n" + "         \"timestamp\":1529057443,\n"
			+ "         \"records\":[\n" + "            {\n" + "               \"available\":true,\n"
			+ "               \"takenValidSamples\":1,\n" + "               \"state\":5,\n"
			+ "               \"minValue\":0.000000,\n" + "               \"avgValue\":0.000000,\n"
			+ "               \"maxValue\":0.000000\n" + "            },\n" + "            {\n"
			+ "               \"available\":true,\n" + "               \"takenValidSamples\":1,\n"
			+ "               \"state\":5,\n" + "               \"minValue\":34.134002,\n"
			+ "               \"avgValue\":34.134002,\n" + "               \"maxValue\":34.134002\n" + "            }\n"
			+ "         ]\n" + "      }\n" + "   ]\n" + "}";

	private RaritanJsonTransformer raritanJsonTransformer;

	@Before
	public void setUp() throws Exception {
		this.raritanJsonTransformer = new RaritanJsonTransformer();
	}

	@After
	public void tearDown() throws Exception {
		this.raritanJsonTransformer = null;
	}

	@Test
	public void testIbakRaritanOutletSingleSensorJson() {
		final List<IMonitoringRecord> monitoringRecords = this.raritanJsonTransformer
				.apply(IBAK_RARITAN_OUTLET_SINGLE_SENSOR_JSON);
		assertEquals(2, monitoringRecords.size());

		this.testRecord(monitoringRecords.get(0), "13", 1521817336_000l, 87.21);
		this.testRecord(monitoringRecords.get(1), "13", 1521817380_000l, 88.84);
	}

	@Test
	public void testIbakRaritanOutletTwoSensorsJson() {
		final List<IMonitoringRecord> monitoringRecords = this.raritanJsonTransformer
				.apply(IBAK_RARITAN_OUTLET_TWO_SENSORS_JSON);
		assertEquals(6, monitoringRecords.size());

		this.testRecord(monitoringRecords.get(0), "47", 1529057441_000l, 0.0);
		this.testRecord(monitoringRecords.get(1), "46", 1529057441_000l, 34.13);
		this.testRecord(monitoringRecords.get(2), "47", 1529057442_000l, 0.0);
		this.testRecord(monitoringRecords.get(3), "46", 1529057442_000l, 34.12);
		this.testRecord(monitoringRecords.get(4), "47", 1529057443_000l, 0.0);
		this.testRecord(monitoringRecords.get(5), "46", 1529057443_000l, 34.13);
	}

	private void testRecord(final IMonitoringRecord record, final String expectedIdentifier,
			final long expectedTimestamp, final double expectedValueinW) {
		assertTrue(record instanceof ActivePowerRecord);
		final ActivePowerRecord castedRecord = (ActivePowerRecord) record;
		assertEquals(expectedIdentifier, castedRecord.getIdentifier());
		assertEquals(expectedTimestamp, castedRecord.getTimestamp());
		assertEquals(expectedValueinW, castedRecord.getValueInW(), 0.01);
	}

}
