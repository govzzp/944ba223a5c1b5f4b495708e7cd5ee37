package pers.me.monday.mapper;


import pers.me.monday.model.table.CourseSchema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Mapper

public interface CourseSchemaMapper {


    CourseSchema getOneRecordBy2Ids(@Param("schemaId") int schemaId, @Param("courseId") int courseId);

    ArrayList<HashMap<String,Object>> getDetailRecordsBySchemaId(int schemaId, int weekNo);

    //得到某个学生在某周某天的全部课程
    ArrayList<Integer> getCourseIdsBySchemaIdAndTime(int schemaId, int weekNo, int weekDay);
}
