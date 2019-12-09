package pers.me.monday.controller;


import pers.me.monday.controller.responseEntity.DataWrapper;
import pers.me.monday.controller.responseEntity.TeacherCourse;
import pers.me.monday.mapper.RecordMapper;
import pers.me.monday.mapper.StudentSchemaMapper;
import pers.me.monday.service.TeacherCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class TeacherController {


    //返回某周的课程

    @Autowired
    TeacherCourseService teacherCourseService;

    @RequestMapping(value="/v1/teacher/course/week",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<TeacherCourse>>> getCourseInWeek(
            @RequestHeader("UID") String uid,
            @RequestParam("day") int weekDay
    ){
        var list = teacherCourseService.getCourses(weekDay,Integer.parseInt(uid));
        return new ResponseEntity<>(new DataWrapper<>(list), HttpStatus.OK);
    }




    //返回某门课程人员名单

    @Autowired
    StudentSchemaMapper studentSchemaMapper;

    @RequestMapping(value="/v1/teacher/course-member",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<HashMap<String,Object>>>> getCourseMember(
            @RequestHeader("UID") String uid,
            @RequestParam("course_id") int course_id
    ) {
        return new ResponseEntity<>(
                new DataWrapper<>(
                        studentSchemaMapper.getAllMemberInACourse(course_id)
                ),
                HttpStatus.OK
        );
    }


    @Autowired
    RecordMapper recordMapper;

    //教师查看某一天的某门课程的历史签到信息
    @RequestMapping(value="/v1/teacher/course/history",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<HashMap<String,Object>>>> getCourseHistory(
            @RequestHeader("UID") String uid,
            @RequestParam("course_id") int courseId,
            @RequestParam("week_no") int weekNo,
            @RequestParam("week_day") int weekDay
    ){
        return new ResponseEntity<>(
                new DataWrapper<>(
                        recordMapper.getHistory(
                                weekDay,weekNo,courseId,Integer.parseInt(uid)
                        )
                ), HttpStatus.OK
        );
    }
}
