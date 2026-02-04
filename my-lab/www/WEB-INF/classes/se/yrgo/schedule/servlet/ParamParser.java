package se.yrgo.schedule.servlet;

import javax.servlet.http.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ParamParser {
  enum QueryType {
    ALL,
    TEACHER_ID,
    DAY,
    TEACHER_ID_AND_DAY
  }

  private HttpServletRequest request;
  private QueryType type;
  private String teacherId;
  private String day;
  private String contentType;
  private String format;
  private boolean valid = true;
  private int httpStatus = HttpServletResponse.SC_OK;
  private String errorStatus;

  /**
   * Constructs a new ParamParser from the Servlet's request object
   * @param request The Servlet's request, whose GET params will be parsed
   */
  public ParamParser(HttpServletRequest request) {
    this.request = request;
    parseValues();
    parseType();
    parseContentType();
    validate();
  }

 /**
  * Displays the value of the format parameter in HEAD in Content-Type
  */
  private void parseContentType() {
    if (format != null && format.equalsIgnoreCase("json")) {
      contentType = "application/json;charset=" + UTF_8.name();
    } else if (format != null && format.equalsIgnoreCase("xml")) {
      contentType = "application/xml;charset=" + UTF_8.name();
    }
    else {
      contentType = "text/html;charset=" + UTF_8.name();
    }
  }

  /**
   * Returns the content type of the request
   * @return The content-type as a String, or "html" (default) if none is given
   */
  public String contentType() {
    return contentType;
  }

  private void parseType() {
    if (teacherId == null && day == null) {
      type = QueryType.ALL;
    } else if (day != null && teacherId != null) {
      type = QueryType.TEACHER_ID_AND_DAY;
    } else if (day != null && teacherId == null) {
      type = QueryType.DAY;
    } else {
      type = QueryType.TEACHER_ID;
    }
  }

  private void parseValues() {
    this.format = request.getParameter("format");
    if (format != null) {
      format = format.toLowerCase();
    } else {
      format = "html";
    }
    this.day = request.getParameter("day");
    this.teacherId = request.getParameter("substitute_id");
  }

  /**
   * For trying to detect invalid parameters, format, substitute teachers and days.
   * If something is invalid, it will send a 400 Bad Request error
   */
  public void validate() {
    // Makes sure there is a parameter value for substitute_id, and that it is a numeric value
    // TILL NAHID:
    // Jag har problemet att den skickar statuskod 200 även när den inte hittar
    // substitute_id eller day i databasen. Det är nog i denna if-satsen som något
    // fel händer. Jag lyckades tyvärr inte lösa problemet.
    if (teacherId != null && !teacherId.matches("\\d+")) {
      valid = false;
      httpStatus = HttpServletResponse.SC_NOT_FOUND;
      return;
    }

    // Makes sure that there is a parameter value for day, and that it matches the format YYYY-mm-dd
    if (day != null && !day.matches("\\d{4}-\\d{2}-\\d{2}")) {
      valid = false;
      httpStatus = HttpServletResponse.SC_NOT_FOUND;
    }
  }

  /**
   * @return Returns if it is a valid (true) request or not (false)
   */
  public boolean isValidRequest() {
    return valid;
  }

  /**
   * @return Returns the current HTTP status
   */
  public int getHttpStatus() {
    return httpStatus;
  }

  /**
   * @return Returns the current error status message
   */
  public String getErrorStatus() {
    return errorStatus;
  }

  /**
   * Returns the se.yrgo.schedule.format from the request param
   * se.yrgo.schedule.format, as a String
   * 
   * @return The se.yrgo.schedule.format request parameter, as a String, or null
   *         if none is given
   */
  public String format() {
    return format;
  }

  /**
   * Returns the day paramteter of the request
   * 
   * @return The day parameter of the request, as a String, or null if none is
   *         given
   */
  public String day() {
    return day;
  }

  /**
   * Returns the teacherId (from the substitute_id parameter), as a String
   * 
   * @return The teacherId, as a String, or null if none is given
   */
  public String teacherId() {
    return teacherId;
  }

  /**
   * Returns the QueryType of the request, one of ALL, TEACHER_ID, DAY, and,
   * TEACHER_ID_AND_DAY (an enum of this class)
   * 
   * @return the QueryType found in this query. See the QueryType enum.
   */
  public QueryType type() {
    return type;
  }

  /**
   * Returns this parser as a String representation. Mostly for debugging.
   * 
   * @return This ParamParser as a String representation.
   */
  @Override
  public String toString() {
    return String.format("Type: %s teacherId: %s day: %s Content-Type: %s Format: %s\n",
        type.toString(), teacherId, day, contentType, format);
  }
}
