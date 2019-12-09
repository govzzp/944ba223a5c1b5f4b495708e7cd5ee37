package pers.me.monday.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Mapper

public interface LeaveMapper {
    //教师得到某天某门课需要签到假的StudentInfo
    ArrayList<HashMap<String,Object>> getUnSucceedStudent(String date, int courseId);
    //教师得到某天某门课程请假成功的StudentInfo
    ArrayList<HashMap<String,Object>> getSucceedStudent(String date, int courseId);

    //教师得到所有未处理的请假请求
    ArrayList<HashMap<String,String>> teacherGetTheLeaveStudentQueue(int schema_id);
    //教师通过或拒绝一个学生的请假请求
    int teacherSetTheStudentLeaveAccepted(int course_id, int student_id, String start_date, int accepted);

    Integer checkTeacherSetTheStudentLeaveRepeated(int course_id, int student_id, String start_date, int accepted);

    //学生在请假主表里面添加一条记录
    Boolean studentLeaveAddOne(
            int student_id, String start_date,
            String end_date, int len,
            int current_pass, int target_pass,
            String reason);
    //学生往教师请假表中添加若干条待处理记录
    void teacherLeaveAcceptedInsert(ArrayList<Integer> courseIds, int studentId, String startDate, String endDate);

    //在插入前检查主键是否重复
    Integer checkPrimaryKeyExists(int student_id, String start_date);

    //得到主体请假信息
    ArrayList<HashMap<String,Object>> getLeaveList(int studentId);

    //得到每节课程的请假通过信息
    ArrayList<HashMap<String,Object>> getLeaveTeacherList(int studentId, String startDate);

    //辅导员,院长得到请假人员名单(分学院)
    ArrayList<HashMap<String,Object>> getAdvancedLeaveStudentInfo(int privilege, String deptNo);

    //校长得到请假人员名单(不分学院)
    ArrayList<HashMap<String,Object>> getMostAdvancedLeaveStudentInfo();

    //权限人员通过或拒绝学生请假请求
    int updateTheStudentLeave(int student_id, String start_date, int current_pass);

}
