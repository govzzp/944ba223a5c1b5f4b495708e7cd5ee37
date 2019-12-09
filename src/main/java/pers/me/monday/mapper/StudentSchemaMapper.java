package pers.me.monday.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Mapper
public interface StudentSchemaMapper {
    public Integer getSchemaIdByStudentId(@Param("id") int id);
    public ArrayList<Integer> getStudentIdsBySchemaId(int schemaId);
    public ArrayList<Integer> getSchemaIdsByCourseId(int courseId);

    public ArrayList<HashMap<String,Object>> getAllMemberInACourse(int courseId);
}
