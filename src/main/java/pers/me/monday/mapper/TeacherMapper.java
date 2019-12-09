package pers.me.monday.mapper;

import pers.me.monday.model.table.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TeacherMapper {
    public Teacher getInfoById(int id);
}
