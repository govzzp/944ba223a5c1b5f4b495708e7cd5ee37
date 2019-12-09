package pers.me.monday.controller;

import pers.me.monday.controller.responseEntity.DataWrapper;
import pers.me.monday.controller.responseEntity.StudentLeaveHistory;
import pers.me.monday.mapper.LeaveMapper;
import pers.me.monday.mapper.TeacherMapper;
import pers.me.monday.mapper.TeacherSchemaMapper;
import pers.me.monday.service.LeaveService;
import pers.me.monday.service.StudentCourseService;
import pers.me.monday.utils.HMUtil;
import pers.me.monday.utils.MyDate;
import pers.me.monday.utils.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;


@RestController
public class LeaveController {

    @Autowired
    LeaveMapper leaveMapper;
    @Autowired
    TeacherSchemaMapper teacherSchemaMapper;

    //教师的全部待审核请假人员名单
    @RequestMapping(value="/v1/teacher/course/leave",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<HashMap<String,String>>>> teacherGetLeavePersonToAccept(
            @RequestHeader("UID") String uid
    ){
        MyLogger.log("/v1/teacher/course/leave","UID",uid);
        int schema_id = teacherSchemaMapper.getSchemaIdByTeacherId(Integer.parseInt(uid));
        MyLogger.log("debug","schema_id",String.valueOf(schema_id));
        ArrayList<HashMap<String,String>> infoList = leaveMapper.teacherGetTheLeaveStudentQueue(schema_id);
        if(infoList!=null){
            return new ResponseEntity<>(new DataWrapper<>(infoList), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



    //教师通过或拒绝一个学生的请假请求

    @RequestMapping(value="/v1/teacher/course/leave",method= RequestMethod.PUT)
    ResponseEntity<HashMap<String, String>> TeacherAddALeaveStudent(
            @RequestHeader("UID")String uid,
            @RequestParam("student_id") int student_id,
            @RequestParam("start_date") String start_date,
            @RequestParam("course_id") int course_id,
            @RequestParam("accept") int accept
    ) {
        var hashMap = new HashMap<String,String>();
        int affectedNumber = 0;
        if(accept!=1){
            if(accept==0){
                accept=-1;
            }else{
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        if(leaveMapper.checkTeacherSetTheStudentLeaveRepeated(course_id,student_id,start_date,accept)!=null){
            hashMap.put("error","操作重复");
            return new ResponseEntity<>(hashMap, HttpStatus.CONFLICT);
        }

        affectedNumber=leaveMapper.teacherSetTheStudentLeaveAccepted(course_id,student_id,start_date,accept);
        if(affectedNumber==1){
            hashMap.put("accepted","1");
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }else{
            MyLogger.log("/v1/teacher/course/leave","sql","no column affected");
            hashMap.put("error","不存在对应学生");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //学生发起一个请假请求
    @Autowired
    StudentCourseService studentCourseService;
    @RequestMapping(value="/v1/student/leave",method= RequestMethod.POST)
    ResponseEntity<HashMap<String,Object>> requestALeave(
            @RequestBody HashMap<String,String> body,
            @RequestHeader("UID") int uid
    ){
        var path = "/v1/student/leave";
        if(!HMUtil.NotNull(body,"start_date","len","reason")){
            MyLogger.log(path,"request body","incomplete");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        int len = Integer.parseInt(body.get("len"));
        if(len<=0){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        String start_date = body.get("start_date");
        String reason = body.get("reason");
        String end_date = MyDate.addDays(start_date,len-1);


        int current_pass;
        int target_pass;
        if(len==1){
            current_pass = 0;
            target_pass = 0;
        }else if(len <= 7){
            current_pass = 0;
            target_pass = 1;
        }else if(len <= 30){
            current_pass = 0;
            target_pass = 2;
        }else {
            current_pass = 0;
            target_pass = 3;
        }

        Integer exists = leaveMapper.checkPrimaryKeyExists(uid,start_date);
        Boolean tableLeaveAdded=false;
        if(exists==null){
            tableLeaveAdded = leaveMapper.studentLeaveAddOne(
                    uid,start_date,end_date,
                    len,current_pass,target_pass,
                    reason);
        }else{
            MyLogger.log(path,"select ... from leave","key exists");
            var hashMap = new HashMap<String,Object>();
            hashMap.put("error","exists");
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }


        if(tableLeaveAdded){
            if(len==1){
                var list = studentCourseService.getStudentCoursesByDate(start_date,uid);
                if(!list.isEmpty()) {
                    leaveMapper.teacherLeaveAcceptedInsert(list,uid,start_date,end_date);
                }else{
                    MyLogger.log(path,"insert into leave_teacher_accept","no course to insert");
                }
            }
        }else{
            MyLogger.log(path,"sql accepted","false");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        var responseBody = new HashMap<String,Object>();
        responseBody.put("accepted",1);
        responseBody.put("level",target_pass);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


    @Autowired
    LeaveService leaveService;
    //学生的待处理请假列表
    @RequestMapping(value="/v1/student/leave",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<StudentLeaveHistory>>> getLeaveList(
            @RequestHeader("UID") int uid
    ) {
        var list = leaveService.getStudentLeaveRequestHistory(uid);
        return new ResponseEntity<>(new DataWrapper<>(list), HttpStatus.OK);
    }

    //返回辅导员,院长,校长级别的待请假人员名单

    @Autowired
    TeacherMapper teacherMapper;
    @RequestMapping(value="/v1/teacher/advanced/leave",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<HashMap<String,Object>>>> getAdvancedLeaveInfo(
            @RequestHeader("UID") String uid
    ){
        var teacherInfo = teacherMapper.getInfoById(Integer.parseInt(uid));
        var deptNo = teacherInfo.getDeptNo();
        var targetPass = teacherInfo.getPrivilege();
        if(targetPass==0){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        var List = new ArrayList<HashMap<String,Object>>();
        if(targetPass<3){
            List = leaveMapper.getAdvancedLeaveStudentInfo(targetPass,deptNo);
        }else{
            List = leaveMapper.getMostAdvancedLeaveStudentInfo();
        }
        return new ResponseEntity<>(new DataWrapper<>(List), HttpStatus.OK);

    }


    //辅导员,院长,校长通过或者拒绝请假请求
    @RequestMapping(value="/v1/teacher/advanced/leave",method= RequestMethod.PUT)
    ResponseEntity<HashMap<String,Boolean>> putALeavePass(
            @RequestHeader("UID") String uid,
            @RequestParam("student_id") int studentId,
            @RequestParam("start_date") String startDate,
            @RequestParam("accepted") int accepted
    ){
        var info = teacherMapper.getInfoById(Integer.parseInt(uid));
        int affected;
        if(accepted==1){
            affected = leaveMapper.updateTheStudentLeave(studentId,startDate,info.getPrivilege());
        }else{
            affected = leaveMapper.updateTheStudentLeave(studentId,startDate,-1);
        }

        var responseBody = new HashMap<String,Boolean>();
        if(affected==1){
            responseBody.put("accepted",true);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

    }

}
