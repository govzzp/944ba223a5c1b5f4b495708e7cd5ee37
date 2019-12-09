package pers.me.monday.service;


import pers.me.monday.controller.responseEntity.StudentLeaveHistory;
import pers.me.monday.mapper.CourseMapper;
import pers.me.monday.mapper.LeaveMapper;
import pers.me.monday.model.table.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;


//==========response===========================//
//public class StudentLeaveHistory {
//    public String level;
//    public String reason;
//    public String teacherName;
//    public String courseName;
//    public String status;
//    public String startDate;
//    public String endDate;
//    public String dayLen;
//}
//==========================================//

@Service

public class LeaveService {
    @Autowired
    LeaveMapper leaveMapper;

    @Autowired
    DateService dateService;

    //返回教师某天某门课程上需要签到的studentInfos
    public ArrayList<HashMap<String,Object>> getLeaveUnSuccessfulStudentIds(int weekNo,int weekDay,int courseId){
        String now = dateService.getDate(weekNo,weekDay);
        return leaveMapper.getUnSucceedStudent(now,courseId);
    }

    //返回不需要签到的同学
    public ArrayList<HashMap<String,Object>> getSuccessfulStudentids(int weekNo,int weekDay,int courseId){

        String now = dateService.getDate(weekNo,weekDay);
        return leaveMapper.getSucceedStudent(now,courseId);
    }





    //学生待处理的请假列表
    @Autowired
    CourseMapper courseMapper;
    public ArrayList<StudentLeaveHistory> getStudentLeaveRequestHistory(int studentId){
        var list = leaveMapper.getLeaveList(studentId);

        var historyList = new ArrayList<StudentLeaveHistory>();

        for(var i:list){
            var current_pass = (int)i.get("current_pass");
            var target_pass  =(int)i.get("target_pass");
            var startDate = (Date)i.get("start_date");
            var endDate = (Date)i.get("end_date");
            var reason = (String)i.get("reason");
            var len = (int)i.get("len");
            if((current_pass==target_pass)&&current_pass==0){
                var listList = leaveMapper.getLeaveTeacherList(studentId,startDate.toString());
                for(var ii:listList){
                    int courseId = (int)ii.get("course_id");
                    int accepted = (int) ii.get("accepted");
                    Course course = courseMapper.getCourseById(courseId);
                    var e = new StudentLeaveHistory();
                    if(accepted==1){
                        e.status="已通过";
                    }else if(accepted==0){
                        e.status="未通过";
                    }else{
                        e.status="已拒绝";
                    }
                    e.reason=reason;
                    e.startDate=startDate.toString();
                    e.endDate=endDate.toString();
                    e.dayLen=len;
                    e.level="任课教师";
                    e.courseNameAndTeacherName=course.getName()+":"+course.getTeacherName();
                    historyList.add(e);
                }
            }else{
                var e = new StudentLeaveHistory();
                e.courseNameAndTeacherName="任课教师没有权限";
                e.dayLen=len;
                e.startDate=startDate.toString();
                e.endDate=endDate.toString();
                e.reason=reason;
                if(len<=7){
                    e.level="辅导员";
                }else if(len<=30){
                    e.level="院长";
                }else{
                    e.level="校长";
                }
                if(current_pass==-1){
                    e.status="已拒绝";
                }else if(current_pass<target_pass){
                    e.status="未通过";
                }else{
                    e.status="已通过";
                }
                historyList.add(e);
            }
        }
        return historyList;
    }
}
