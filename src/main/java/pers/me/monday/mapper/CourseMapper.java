package pers.me.monday.mapper;

import pers.me.monday.model.table.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper

public interface CourseMapper {
    public Course getCourseById(int id);
}
