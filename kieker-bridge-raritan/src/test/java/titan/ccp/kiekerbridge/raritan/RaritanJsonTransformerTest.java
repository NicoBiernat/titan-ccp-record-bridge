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

  /*
   * @Test public void testIbakRaritanOutletSingleSensorJson() { final String json =
   * this.getFileAsString("ibak_raritan_outlet_single_sensor.json"); final PushMessage pushMessage =
   * new PushMessage(json); final List<IMonitoringRecord> monitoringRecords =
   * this.raritanJsonTransformer.apply(pushMessage); assertEquals(2, monitoringRecords.size());
   *
   * this.testRecord(monitoringRecords.get(0), "13", 1521817336_000L, 87.21);
   * this.testRecord(monitoringRecords.get(1), "13", 1521817380_000L, 88.84); }
   */

  /*
   * @Test public void testIbakRaritanOutletTwoSensorsJson() { final String json =
   * this.getFileAsString("ibak_raritan_outlet_two_sensors.json"); final PushMessage pushMessage =
   * new PushMessage(json); final List<IMonitoringRecord> monitoringRecords =
   * this.raritanJsonTransformer.apply(pushMessage); assertEquals(6, monitoringRecords.size());
   *
   * this.testRecord(monitoringRecords.get(0), "47", 1529057441_000L, 0.0);
   * this.testRecord(monitoringRecords.get(1), "46", 1529057441_000L, 34.13);
   * this.testRecord(monitoringRecords.get(2), "47", 1529057442_000L, 0.0);
   * this.testRecord(monitoringRecords.get(3), "46", 1529057442_000L, 34.12);
   * this.testRecord(monitoringRecords.get(4), "47", 1529057443_000L, 0.0);
   * this.testRecord(monitoringRecords.get(5), "46", 1529057443_000L, 34.13); }
   */

  @Test
  public void testIbakId1() {
    final String json = this.getFileAsString("ibak_raritan_id1_1.json");
    final PushMessage pushMessage = new PushMessage(json);
    final List<IMonitoringRecord> monitoringRecords =
        this.raritanJsonTransformer.apply(pushMessage);

    final IMonitoringRecord firstRecord = monitoringRecords.get(0);
    final int numberOfRecords = monitoringRecords.size();
    final IMonitoringRecord lastRecord = monitoringRecords.get(numberOfRecords - 1);
    assertTrue(firstRecord instanceof ActivePowerRecord);
    assertTrue(lastRecord instanceof ActivePowerRecord);
    final ActivePowerRecord castedFirstRecord = (ActivePowerRecord) firstRecord;
    final ActivePowerRecord castedLastRecord = (ActivePowerRecord) lastRecord;
    assertEquals("1", castedFirstRecord.getIdentifier().substring(0, 1));
    assertEquals("1", castedLastRecord.getIdentifier().substring(0, 1));
  }

  @Test
  public void testIbakId2() {
    final String json = this.getFileAsString("ibak_raritan_id2_1.json");
    final PushMessage pushMessage = new PushMessage(json);
    final List<IMonitoringRecord> monitoringRecords =
        this.raritanJsonTransformer.apply(pushMessage);

    final IMonitoringRecord firstRecord = monitoringRecords.get(0);
    final int numberOfRecords = monitoringRecords.size();
    final IMonitoringRecord lastRecord = monitoringRecords.get(numberOfRecords - 1);
    assertTrue(firstRecord instanceof ActivePowerRecord);
    assertTrue(lastRecord instanceof ActivePowerRecord);
    final ActivePowerRecord castedFirstRecord = (ActivePowerRecord) firstRecord;
    final ActivePowerRecord castedLastRecord = (ActivePowerRecord) lastRecord;
    assertEquals("2", castedFirstRecord.getIdentifier().substring(0, 1));
    assertEquals("2", castedLastRecord.getIdentifier().substring(0, 1));
  }

  @Test
  public void testIbakId3() {
    final String json = this.getFileAsString("ibak_raritan_id3_1.json");
    final PushMessage pushMessage = new PushMessage(json);
    final List<IMonitoringRecord> monitoringRecords =
        this.raritanJsonTransformer.apply(pushMessage);

    final IMonitoringRecord firstRecord = monitoringRecords.get(0);
    final int numberOfRecords = monitoringRecords.size();
    final IMonitoringRecord lastRecord = monitoringRecords.get(numberOfRecords - 1);
    assertTrue(firstRecord instanceof ActivePowerRecord);
    assertTrue(lastRecord instanceof ActivePowerRecord);
    final ActivePowerRecord castedFirstRecord = (ActivePowerRecord) firstRecord;
    final ActivePowerRecord castedLastRecord = (ActivePowerRecord) lastRecord;
    assertEquals("3", castedFirstRecord.getIdentifier().substring(0, 1));
    assertEquals("3", castedLastRecord.getIdentifier().substring(0, 1));
  }

  @Test
  public void testIbakId1Second() {
    final String json = this.getFileAsString("ibak_raritan_id1_2.json");
    final PushMessage pushMessage = new PushMessage(json);
    final List<IMonitoringRecord> monitoringRecords =
        this.raritanJsonTransformer.apply(pushMessage);

    final IMonitoringRecord firstRecord = monitoringRecords.get(0);
    final int numberOfRecords = monitoringRecords.size();
    final IMonitoringRecord lastRecord = monitoringRecords.get(numberOfRecords - 1);
    assertTrue(firstRecord instanceof ActivePowerRecord);
    assertTrue(lastRecord instanceof ActivePowerRecord);
    final ActivePowerRecord castedFirstRecord = (ActivePowerRecord) firstRecord;
    final ActivePowerRecord castedLastRecord = (ActivePowerRecord) lastRecord;
    assertEquals("1", castedFirstRecord.getIdentifier().substring(0, 1));
    assertEquals("1", castedLastRecord.getIdentifier().substring(0, 1));
  }

  @Test
  public void testIbakId2Second() {
    final String json = this.getFileAsString("ibak_raritan_id2_2.json");
    final PushMessage pushMessage = new PushMessage(json);
    final List<IMonitoringRecord> monitoringRecords =
        this.raritanJsonTransformer.apply(pushMessage);

    final IMonitoringRecord firstRecord = monitoringRecords.get(0);
    final int numberOfRecords = monitoringRecords.size();
    final IMonitoringRecord lastRecord = monitoringRecords.get(numberOfRecords - 1);
    assertTrue(firstRecord instanceof ActivePowerRecord);
    assertTrue(lastRecord instanceof ActivePowerRecord);
    final ActivePowerRecord castedFirstRecord = (ActivePowerRecord) firstRecord;
    final ActivePowerRecord castedLastRecord = (ActivePowerRecord) lastRecord;
    assertEquals("2", castedFirstRecord.getIdentifier().substring(0, 1));
    assertEquals("2", castedLastRecord.getIdentifier().substring(0, 1));
  }

  @Test
  public void testIbakId3Second() {
    final String json = this.getFileAsString("ibak_raritan_id3_2.json");
    final PushMessage pushMessage = new PushMessage(json);
    final List<IMonitoringRecord> monitoringRecords =
        this.raritanJsonTransformer.apply(pushMessage);

    final IMonitoringRecord firstRecord = monitoringRecords.get(0);
    final int numberOfRecords = monitoringRecords.size();
    final IMonitoringRecord lastRecord = monitoringRecords.get(numberOfRecords - 1);
    assertTrue(firstRecord instanceof ActivePowerRecord);
    assertTrue(lastRecord instanceof ActivePowerRecord);
    final ActivePowerRecord castedFirstRecord = (ActivePowerRecord) firstRecord;
    final ActivePowerRecord castedLastRecord = (ActivePowerRecord) lastRecord;
    assertEquals("3", castedFirstRecord.getIdentifier().substring(0, 1));
    assertEquals("3", castedLastRecord.getIdentifier().substring(0, 1));
  }

  /*
   * private void testRecord(final IMonitoringRecord record, final String expectedIdentifier, final
   * long expectedTimestamp, final double expectedValueinW) { assertTrue(record instanceof
   * ActivePowerRecord); final ActivePowerRecord castedRecord = (ActivePowerRecord) record;
   * assertEquals(expectedIdentifier, castedRecord.getIdentifier()); assertEquals(expectedTimestamp,
   * castedRecord.getTimestamp()); assertEquals(expectedValueinW, castedRecord.getValueInW(), 0.01);
   * }
   */

  private String getFileAsString(final String file) {
    final URL url = Resources.getResource(file);
    try {
      return Resources.toString(url, Charsets.UTF_8);
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
