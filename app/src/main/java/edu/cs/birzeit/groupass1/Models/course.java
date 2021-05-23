package edu.cs.birzeit.groupass1.Models;

public class course {
    private String courseID;
    private String courseName;
    private String studentID;

    public course() {
    }

    public course(String courseID, String courseName, String studentID) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.studentID = studentID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    @Override
    public String toString() {
        return "course{" +
                "courseID='" + courseID + '\'' +
                ", courseName='" + courseName + '\'' +
                ", studentID=" + studentID +
                '}';
    }
}
