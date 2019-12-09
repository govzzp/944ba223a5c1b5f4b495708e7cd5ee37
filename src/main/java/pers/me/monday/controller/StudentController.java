package pers.me.monday.controller;


import pers.me.monday.controller.responseEntity.DataWrapper;
import pers.me.monday.controller.responseEntity.StudentCourse;
import pers.me.monday.mapper.RecordMapper;
import pers.me.monday.mapper.StudentMapper;
import pers.me.monday.model.table.Record;
import pers.me.monday.service.StudentCourseService;
import pers.me.monday.utils.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@RestController
public class StudentController {

    @Autowired
    private StudentMapper studentmapper;
    //返回学生信息
    @RequestMapping(value="/v1/student/info",method= RequestMethod.GET)
    ResponseEntity<HashMap<String,String>> personnelInfo(
            @RequestHeader("UID") String authorizedId
    ){
        MyLogger.log("/v1/student/info","UID",authorizedId);
        var si = new HashMap<String,String>();
        var s = studentmapper.getStudentById(Integer.parseInt(authorizedId));
        si.put("dept_name",s.getDeptName());
        si.put("dept_no",s.getDeptNo());
        si.put("id",String.valueOf(s.getId()));
        si.put("name" , s.getName());
        return new ResponseEntity<>(si, HttpStatus.OK);
    }

    @Autowired
    private StudentCourseService studentCourseService;
    //返回学生课表(无状态信息)
    @RequestMapping(value="/v1/student/course/week",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<StudentCourse>>> getStudentCourseList(
            @RequestParam("no") int weekNo,
            @RequestHeader("UID") int uid){
            ArrayList<StudentCourse> array = studentCourseService.getStudentCourseByWeek(weekNo,uid);
            return new ResponseEntity<>(new DataWrapper<>(array), HttpStatus.OK);
    }


    //学生查看某们课程历史签到信息
    @Autowired
    RecordMapper recordMapper;
    @RequestMapping(value="/v1/student/course/history",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<LinkedList<Record>>> courseDetail(
            @RequestHeader("UID") String uid,
            @RequestParam("course_id") int courseId
    ){
        var list = recordMapper.getRecordsBy2Ids(Integer.parseInt(uid),courseId);
        return new ResponseEntity<>(new DataWrapper<>(list), HttpStatus.OK);
    }
}


