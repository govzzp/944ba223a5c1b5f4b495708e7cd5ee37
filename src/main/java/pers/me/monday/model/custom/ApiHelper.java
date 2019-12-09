package pers.me.monday.model.custom;
import java.util.HashMap;

public class ApiHelper {
    private HashMap<String,String> Api;
    public ApiHelper(){
        this.Api = new HashMap<String,String>();

        Api.put("OPTIONS /v1","Pre-flight");

        Api.put("GET  / , /v1 , /v1/ "           ,"Api help page");
        Api.put("GET /v1/token "                 ,"Token");
        Api.put("GET /v1/info/semester"          ,"Current semester");
        Api.put("GET /v1/info/week"              ,"Current week");

        Api.put("GET /v1/student/course/week"    ,"The list of courses in specific week");
        Api.put("GET /v1/student/course/detail"  ,"The check-in history of specific course");
        Api.put("GET /v1/student/info"           ,"The personnel info of a student");
        Api.put("GET /v1/student/check-in"        ,"A signal indicating whether you are checking in");



        Api.put("GET /v1/teacher/course/week"    ,"The schema of specific teacher");
        Api.put("POST /v1/teacher/course/leave"  ,"Student's leaving information");
        Api.put("POST /v1/teacher/course/listen-whitelist","Student's listen-free information");
        Api.put("GET /v1/teacher/course-member"  ,"The personnel list of a course");
        Api.put("POST /v1/teacher/check-in"      ,"Invoke a check-in");
        Api.put("DELETE /v1/teacher/check-in"    ,"Revoke a check-in");
        Api.put("GET /v1/teacher/check-in/member","The list of people in your check-in queue");
    }
    public HashMap<String,String> getApi(){
        return this.Api;
    }
}
