package se.yrgo.schedule.servlet;

import se.yrgo.schedule.data.AssignmentsFactory;
import se.yrgo.schedule.domain.Assignment;
import se.yrgo.schedule.domain.Assignments;
import se.yrgo.schedule.format.Formatter;
import se.yrgo.schedule.format.FormatterFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ScheduleServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Read the request as UTF-8
    request.setCharacterEncoding(UTF_8.name());

    // Parse the arguments - see ParamParser class
    ParamParser parser = new ParamParser(request);
    // Set the content type (using the parser)

    PrintWriter out = response.getWriter();

    // For handling invalid requests, using the isValidRequest method from
    // ParamParser
    if (!parser.isValidRequest()) {
      response.setStatus(parser.getHttpStatus());
      response.setCharacterEncoding(UTF_8.name());

      // Checking if format parameter is either json or xml, in order to be able to
      // proceed
      if (parser.format() == null ||
          !(parser.format().equals("json") || parser.format().equals("xml"))) {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter errorOut = response.getWriter()) {
          out.println(parser.getErrorStatus());
        }
      } else {
        response.setContentType(parser.contentType());
        try (PrintWriter errorOut = response.getWriter()) {
          List<Assignment> emptyList = new ArrayList<>();
          Formatter formatter = FormatterFactory.getFormatter(parser.format());
          out.println(formatter.format(emptyList));
        }
      }
      return;
    }

    response.setContentType(parser.contentType());
    // To write the response, we're using a PrintWriter
    response.setCharacterEncoding(UTF_8.name());

    // Get access to the database, using a factory
    // Assignments is an interface - see Assignments interface
    Assignments db = AssignmentsFactory.getAssignments();

    // Start with an empty list (makes code easier)
    List<Assignment> assignments = new ArrayList<>();

    // Call the correct method, depending on the parser's type value
    try {
      switch (parser.type()) {
        case ALL:
          assignments = db.all();
          break;

        case TEACHER_ID_AND_DAY:
          assignments = db.forTeacherAt(parser.teacherId(), parser.day());
          break;

        case DAY:
          assignments = db.at(parser.day());
          break;

        case TEACHER_ID:
          assignments = db.forTeacher(parser.teacherId());
          break;
      }
    } catch (AccessException e) {
      out.println("Error fetching se.yrgo.schedule.data: " + e.getMessage());
      System.err.println("Error: " + e);
      e.printStackTrace();
    }

    // Formatting and sending answers. If parameter for format is missing or not
    // supported, it will send a message informing the user of that
    try {
      Formatter formatter = FormatterFactory.getFormatter(parser.format());
      String result = formatter.format(assignments);
      out.println(result);
    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.println("Format missing or not supported (use json or xml)");
    }

    out.close();
  }
}