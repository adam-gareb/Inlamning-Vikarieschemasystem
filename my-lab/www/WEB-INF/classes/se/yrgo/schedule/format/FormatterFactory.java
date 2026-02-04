package se.yrgo.schedule.format;

/**
 * A factory to get a formatter (json and XML is supported)
 */
public class FormatterFactory {

  private static Formatter XML_FORMATTER = new XMLFormatter();
  private static Formatter JSON_FORMATTER = new JsonFormatter();

  /**
   * Returns a formatter for the given contentType
   * @param The content type you want to se.yrgo.schedule.format to
   * @return A Formatter of the correct type, depending on the provided
   * contentType. Defaults to HTML. Cannot handle null.
   */
  public static Formatter getFormatter(String contentType) {
    if (contentType.equalsIgnoreCase("xml")) {
      return XML_FORMATTER;
    } else if (contentType.equalsIgnoreCase("json")) {
      return JSON_FORMATTER;
    } else {
      throw new IllegalArgumentException("Format not supported");
    }
  }
}