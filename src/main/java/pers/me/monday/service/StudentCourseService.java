package pers.me.monday.service;

import pers.me.monday.controller.responseEntity.StudentCourse;
import pers.me.monday.mapper.CourseSchemaMapper;
import pers.me.monday.mapper.StudentSchemaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class StudentCourseService {
    @Autowired
    StudentSchemaMapper studentSchemaMapper;
    @Autowired
    CourseSchemaMapper courseSchemaMapper;

//    public class StudentCourse {
//        String week_day = null;
//        String section_start = null;
//        String section_end = null;
//        String name = null;
//        String teacher = null;
//        String dept = null;
//    }
    //通过周数得到学生的课表
    public ArrayList<StudentCourse> getStudentCourseByWeek(int weekNo, int studentId){
        var schemaId = studentSchemaMapper.getSchemaIdByStudentId(studentId);
        var courseSchemas = courseSchemaMapper.getDetailRecordsBySchemaId(schemaId,weekNo);

        var returnTypeArray = new ArrayList<StudentCourse>();

        for(var i:courseSchemas){
            var odd_or_even = (String)i.get("odd_or_even");
            if(odd_or_even!=null){
                if(odd_or_even.equals("单周")&&(weekNo%2==0)){
                    continue;
                }else if(odd_or_even.equals("双周")&&(weekNo%2==1)){
                    continue;
                }
            }
            var e = new StudentCourse();
            e.class_room = (String)i.get("class_room");
            e.teacher = (String)i.get("teacher_name");
            e.section_start=(int)i.get("start_section");
            e.section_end=(int)i.get("end_section");
            e.name=(String)i.get("name");
            e.week_day=(int)i.get("week_day");
            e.dept=(String)i.get("class_room");
            e.course_id=(int)i.get("course_id");
            returnTypeArray.add(e);
        }
        return returnTypeArray;
    }

    @Autowired
    DateService dateService;

    //通过日期得到学生的课程IDs
    public ArrayList<Integer> getStudentCoursesByDate(String date,int student_id){


        var weekNo = dateService.getWeek(date);
        var weekDay = dateService.getWeekDay(date);
        var schema_id = studentSchemaMapper.getSchemaIdByStudentId(student_id);
        return courseSchemaMapper.getCourseIdsBySchemaIdAndTime(schema_id, (int) weekNo,(int)weekDay);
    }
}
