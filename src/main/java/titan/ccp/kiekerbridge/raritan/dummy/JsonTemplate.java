package titan.ccp.kiekerbridge.raritan.dummy;

//TODO move to .emulator
public final class JsonTemplate {

	public static final String TEMPLATE = "'{'\n" + "  \"sensors\": [\n" + "    '{'\n" + "      \"device\": '{'\n"
			+ "        \"type\": 4,\n" + "        \"label\": \"13\",\n" + "        \"line\": 0\n" + "      '}',\n"
			+ "      \"id\": \"voltage\",\n" + "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n"
			+ "        \"type\": '{'\n" + "          \"readingtype\": 0,\n" + "          \"type\": 1,\n"
			+ "          \"unit\": 1\n" + "        '}',\n" + "        \"decdigits\": 0,\n"
			+ "        \"accuracy\": 0,\n" + "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n"
			+ "        \"noiseThreshold\": 0,\n" + "        \"range\": '{'\n" + "          \"lower\": 0,\n"
			+ "          \"upper\": 500\n" + "        '}',\n" + "        \"thresholdCaps\": '{'\n"
			+ "          \"hasUpperCritical\": true,\n" + "          \"hasUpperWarning\": true,\n"
			+ "          \"hasLowerWarning\": true,\n" + "          \"hasLowerCritical\": true\n" + "        '}'\n"
			+ "      '}'\n" + "    '}',\n" + "    '{'\n" + "      \"device\": '{'\n" + "        \"type\": 4,\n"
			+ "        \"label\": \"13\",\n" + "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"current\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 2,\n" + "          \"unit\": 2\n"
			+ "        '}',\n" + "        \"decdigits\": 3,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0.001,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0.04,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 70\n" + "        '}',\n"
			+ "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        '}'\n" + "      '}'\n" + "    '}',\n" + "    '{'\n"
			+ "      \"device\": '{'\n" + "        \"type\": 4,\n" + "        \"label\": \"{0}\",\n"
			+ "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"activePower\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 4,\n" + "          \"unit\": 3\n"
			+ "        '}',\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 21000\n"
			+ "        '}',\n" + "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        '}'\n" + "      '}'\n" + "    '}',\n" + "    '{'\n"
			+ "      \"device\": '{'\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"apparentPower\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 4,\n" + "          \"unit\": 4\n"
			+ "        '}',\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 21000\n"
			+ "        '}',\n" + "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        '}'\n" + "      '}'\n" + "    '}',\n" + "    '{'\n"
			+ "      \"device\": '{'\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"powerFactor\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 5,\n" + "          \"unit\": 0\n"
			+ "        '}',\n" + "        \"decdigits\": 2,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0.01,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 1\n" + "        '}',\n"
			+ "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        '}'\n" + "      '}'\n" + "    '}',\n" + "    '{'\n"
			+ "      \"device\": '{'\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"activeEnergy\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 6,\n" + "          \"unit\": 5\n"
			+ "        '}',\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 1099511627776\n"
			+ "        '}',\n" + "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        '}'\n" + "      '}'\n" + "    '}',\n" + "    '{'\n"
			+ "      \"device\": '{'\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"lineFrequency\",\n"
			+ "      \"readingtype\": 0,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 7,\n" + "          \"unit\": 8\n"
			+ "        '}',\n" + "        \"decdigits\": 1,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0.1,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 6500\n"
			+ "        '}',\n" + "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": true,\n"
			+ "          \"hasUpperWarning\": true,\n" + "          \"hasLowerWarning\": true,\n"
			+ "          \"hasLowerCritical\": true\n" + "        '}'\n" + "      '}'\n" + "    '}',\n" + "    '{'\n"
			+ "      \"device\": '{'\n" + "        \"type\": 4,\n" + "        \"label\": \"13\",\n"
			+ "        \"line\": 0\n" + "      '}',\n" + "      \"id\": \"outletState\",\n"
			+ "      \"readingtype\": 1,\n" + "      \"metadata\": '{'\n" + "        \"type\": '{'\n"
			+ "          \"readingtype\": 0,\n" + "          \"type\": 0,\n" + "          \"unit\": 0\n"
			+ "        '}',\n" + "        \"decdigits\": 0,\n" + "        \"accuracy\": 0,\n"
			+ "        \"resolution\": 0,\n" + "        \"tolerance\": 0,\n" + "        \"noiseThreshold\": 0,\n"
			+ "        \"range\": '{'\n" + "          \"lower\": 0,\n" + "          \"upper\": 0\n" + "        '}',\n"
			+ "        \"thresholdCaps\": '{'\n" + "          \"hasUpperCritical\": false,\n"
			+ "          \"hasUpperWarning\": false,\n" + "          \"hasLowerWarning\": false,\n"
			+ "          \"hasLowerCritical\": false\n" + "        '}'\n" + "      '}'\n" + "    '}'\n" + "  ],\n"
			+ "  \"rows\": [\n" + "    '{'\n" + "      \"timestamp\": {1},\n" + "      \"records\": [\n"
			+ "        '{'\n" + "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n"
			+ "          \"state\": 5,\n" + "          \"minValue\": 227.569995,\n"
			+ "          \"avgValue\": 227.569995,\n" + "          \"maxValue\": 227.569995\n" + "        '}',\n"
			+ "        '{'\n" + "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n"
			+ "          \"state\": 5,\n" + "          \"minValue\": 0.388,\n" + "          \"avgValue\": 0.388,\n"
			+ "          \"maxValue\": 0.388\n" + "        '}',\n" + "        '{'\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 0.0,\n" + "          \"avgValue\": {2},\n" + "          \"maxValue\": 99999.9\n"
			+ "        '}',\n" + "        '{'\n" + "          \"available\": true,\n"
			+ "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 88.536004,\n" + "          \"avgValue\": 88.536004,\n"
			+ "          \"maxValue\": 88.536004\n" + "        '}',\n" + "        '{'\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 0.985091,\n" + "          \"avgValue\": 0.985091,\n"
			+ "          \"maxValue\": 0.985091\n" + "        '}',\n" + "        '{'\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 325047.687001,\n" + "          \"avgValue\": 325047.687001,\n"
			+ "          \"maxValue\": 325047.687001\n" + "        '}',\n" + "        '{'\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 5,\n"
			+ "          \"minValue\": 49.999999,\n" + "          \"avgValue\": 49.999999,\n"
			+ "          \"maxValue\": 49.999999\n" + "        '}',\n" + "        '{'\n"
			+ "          \"available\": true,\n" + "          \"takenValidSamples\": 1,\n" + "          \"state\": 8,\n"
			+ "          \"minValue\": 0,\n" + "          \"avgValue\": 0,\n" + "          \"maxValue\": 0\n"
			+ "        '}'\n" + "      ]\n" + "    '}'\n" + "  ]\n" + "'}'\n" + "";

}
