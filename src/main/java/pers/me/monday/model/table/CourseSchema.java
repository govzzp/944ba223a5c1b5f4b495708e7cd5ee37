package pers.me.monday.model.table;



public class CourseSchema {
    private int schemaId;
    private int courseId;
    private int startWeek;
    private int endWeek;
    private int startSection;
    private int endSection;
    private String classRoom;
    private int weekDay;
    private String oddOrEven;




    public String toString(){
        return Integer.toString(schemaId)+"|"+
                Integer.toString(courseId)+"|"+
                Integer.toString(startWeek)+"|"+
                Integer.toString(endWeek)+"|"+
                Integer.toString(startSection)+"|"+
                Integer.toString(endSection)+"|"+
                classRoom+"|"+
                Integer.toString(weekDay)+"|"+
                oddOrEven;
    }



    //******************
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public void setOddOrEven(String oddOrEven) {
        this.oddOrEven = oddOrEven;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getEndSection() {
        return endSection;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public int getStartSection() {
        return startSection;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getOddOrEven() {
        return oddOrEven;
    }
}
