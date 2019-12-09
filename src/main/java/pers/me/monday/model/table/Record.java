package pers.me.monday.model.table;

public class Record {
//    private int studentId;
//    private int courseId;
    private String startTime;
    private String endTime;
    private String status;
    private String courseName;
    private String teacherName;
    private int teacherId;


    public String toString(){
        return startTime+"|"+
                endTime+"|"+
                status+"|"+
                courseName+"|"+
                teacherName+"|"+
                Integer.toString(teacherId)+"|";

    }



    //*************



    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndTime() {
        return endTime;
    }


    public String getTeacherName() {
        return teacherName;
    }

    public String getStatus() {
        return status;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStartTime() {
        return startTime;
    }
}
