package edu.cs.birzeit.groupass1.Models;

public class student {
    private String ID;
    private String Name;
    private String Email;
    private String Phone;
    private String Gender;
    private String University;
    private String courseID;

    public student() {
    }

    public student(String ID, String name, String email, String phone, String gender, String university, String courseID) {
        this.ID = ID;
        Name = name;
        Email = email;
        Phone = phone;
        Gender = gender;
        University = university;
        this.courseID = courseID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUniversity() {
        return University;
    }

    public void setUniversity(String university) {
        University = university;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "student{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Gender='" + Gender + '\'' +
                ", University='" + University + '\'' +
                ", courseID='" + courseID + '\'' +
                '}';
    }
}
