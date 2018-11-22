package titan.ccp.kiekerbridge.raritan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PushMessageTest {

  @Test
  public void testWithIdentfier() {
    final String id = "Identifier";
    final String message = "Message";
    final PushMessage pushMessage = new PushMessage(id, message);
    assertTrue(pushMessage.getId().isPresent());
    assertEquals(id, pushMessage.getId().get());
    assertEquals(message, pushMessage.getMessage());
  }

  @Test
  public void testWithoutIdentfier() {
    final String message = "Message";
    final PushMessage pushMessage = new PushMessage(message);
    assertEquals(message, pushMessage.getMessage());
    assertFalse(pushMessage.getId().isPresent());
  }

}
