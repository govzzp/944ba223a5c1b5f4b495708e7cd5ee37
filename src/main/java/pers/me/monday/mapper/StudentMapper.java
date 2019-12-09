package pers.me.monday.mapper;

import pers.me.monday.model.table.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StudentMapper {
    Student getStudentById(int id);
}
