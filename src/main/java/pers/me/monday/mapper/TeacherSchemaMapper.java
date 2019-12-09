package pers.me.monday.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper

public interface TeacherSchemaMapper {
    public Integer getSchemaIdByTeacherId(int courseId);
}
