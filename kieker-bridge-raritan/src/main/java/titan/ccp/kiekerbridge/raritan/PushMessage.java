package titan.ccp.kiekerbridge.raritan;

import java.util.Optional;

/**
 * A sensor push message with an additional identifier identifying the PDU.
 */
public final class PushMessage {

  private final String id;
  private final String message;

  public PushMessage(final String message) {
    this(null, message);
  }

  public PushMessage(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  public Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }

  public String getMessage() {
    return this.message;
  }

}
