package se.yrgo.schedule.format;

import java.util.List;

import se.yrgo.schedule.domain.Assignment;
import org.json.*;

/**
 * A class implementing the Formatter interface. Formats a List of Assignment
 * to Json.
 *
 */
public class JsonFormatter implements Formatter {
  public String format(List<Assignment> assignments) {
    // Standard output if no assignments were found, or builds a Json array if it was found
    if (assignments.size() == 0) {
      return "[]";
    } else {
      JSONArray JSON = new JSONArray();
      for (Assignment assignment : assignments) {
       JSON.put(JSONAssignment(assignment));        
     }
      return JSON.toString(2);
    }
  }

  /* Creates a JSON object from assignment(s) */
  private JSONObject JSONAssignment(Assignment assignment) {
    JSONObject JSONAssignment = new JSONObject();
    JSONAssignment.put("date", assignment.date());
    JSONObject JSONSubstitute = new JSONObject();
    JSONSubstitute.put("name", assignment.teacher().name());
    JSONAssignment.put("substitute", JSONSubstitute);
    JSONObject JSONSchool = new JSONObject();
    JSONSchool.put("school_name", assignment.school().name());
    JSONSchool.put("address", assignment.school().address());
    JSONAssignment.put("school", JSONSchool);
    return JSONAssignment;
  }
}