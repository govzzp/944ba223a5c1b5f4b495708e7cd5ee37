package pers.me.monday.model.table;

public class Course {

    private int id;
    private String schoolYear;
    private String semester;
    private String name;
    private String dept;
    private String teacherName;
    private String teacherId;



    public String toString(){
        return Integer.toString(id)+schoolYear+semester+name+dept+teacherName+teacherId;
    }




    //*******************************************
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }
    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    public String getSemester() {
        return semester;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getDept() {
        return dept;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    public String getTeacherId() {
        return teacherId;
    }
}
