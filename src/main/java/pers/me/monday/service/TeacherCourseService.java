package pers.me.monday.service;


import pers.me.monday.controller.responseEntity.TeacherCourse;
import pers.me.monday.mapper.CourseSchemaMapper;
import pers.me.monday.mapper.StudentSchemaMapper;
import pers.me.monday.mapper.TeacherSchemaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TeacherCourseService {

    @Autowired
    TeacherSchemaMapper teacherSchemaMapper;

    @Autowired
    StudentSchemaMapper studentSchemaMapper;

    @Autowired
    CourseSchemaMapper courseSchemaMapper;

    //返回教师某周的课表
    public ArrayList<TeacherCourse> getCourses(int weekNo,int teacherId) {
        var schemaId = teacherSchemaMapper.getSchemaIdByTeacherId(teacherId);
        var courseSchemas = courseSchemaMapper.getDetailRecordsBySchemaId(schemaId, weekNo);

        var returnTypeArray = new ArrayList<TeacherCourse>();

        for (var i : courseSchemas) {
            var odd_or_even = (String) i.get("odd_or_even");
            if (odd_or_even != null) {
                if (odd_or_even.equals("单周") && (weekNo % 2 == 0)) {
                    continue;
                } else if (odd_or_even.equals("双周") && (weekNo % 2 == 1)) {
                    continue;
                }
            }
            var e = new TeacherCourse();
            e.class_room = (String) i.get("class_room");
            e.section_start = (int) i.get("start_section");
            e.section_end = (int) i.get("end_section");
            e.name = (String) i.get("name");
            e.week_day = (int) i.get("week_day");
            returnTypeArray.add(e);
        }
        return returnTypeArray;
    }
}