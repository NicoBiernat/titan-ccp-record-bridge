package titan.ccp.kiekerbridge.raritan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import kieker.common.record.IMonitoringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import titan.ccp.models.records.ActivePowerRecord;

public class RaritanJsonTransformerTest {

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
    final String json = this.getFileAsString("ibak_raritan_outlet_single_sensor.json");
    final List<IMonitoringRecord> monitoringRecords = this.raritanJsonTransformer.apply(json);
    assertEquals(2, monitoringRecords.size());

    this.testRecord(monitoringRecords.get(0), "13", 1521817336_000l, 87.21);
    this.testRecord(monitoringRecords.get(1), "13", 1521817380_000l, 88.84);
  }

  @Test
  public void testIbakRaritanOutletTwoSensorsJson() {
    final String json = this.getFileAsString("ibak_raritan_outlet_two_sensors.json");
    final List<IMonitoringRecord> monitoringRecords = this.raritanJsonTransformer.apply(json);
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

  private String getFileAsString(final String file) {
    final URL url = Resources.getResource(file);
    try {
      return Resources.toString(url, Charsets.UTF_8);
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
