package se.yrgo.schedule.data;

import se.yrgo.schedule.domain.Assignments;

public class AssignmentsFactory {
  private AssignmentsFactory() {}
  public static Assignments getAssignments() {
    return new DatabaseAssignments();
  }
  
}