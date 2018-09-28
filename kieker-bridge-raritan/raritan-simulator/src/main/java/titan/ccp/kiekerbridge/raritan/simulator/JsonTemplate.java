package titan.ccp.kiekerbridge.raritan.simulator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;

/**
 * Class that represents the JSON template for Raritan push messages. It is stateless and normally
 * has to be created only once per execution.
 */
public final class JsonTemplate {

  private static final String TEMPLATE_RESOURCE = "template.json";

  private final String template;

  /**
   * Create a new template.
   */
  public JsonTemplate() {
    final URL url = Resources.getResource(TEMPLATE_RESOURCE);
    try {
      // Replace all { and } by '{' and '}', except of something like {0}
      this.template = Resources.toString(url, Charsets.UTF_8).replaceAll("\\{(?![0-9])", "'{'")
          .replaceAll("(?<![0-9])\\}", "'}'");
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public String getTemplate() {
    return this.template;
  }

}
