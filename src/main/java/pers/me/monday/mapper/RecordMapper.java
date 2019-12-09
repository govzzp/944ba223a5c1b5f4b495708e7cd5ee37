package pers.me.monday.mapper;

import pers.me.monday.controller.responseEntity.CheckInRecordOfStudent;
import pers.me.monday.model.table.Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Repository
@Mapper
public interface RecordMapper {
    LinkedList<Record> getRecordsBy2Ids(int studentId, int courseId);

    //得到某天某们课程的历史签到信息
    ArrayList<HashMap<String,Object>> getHistory(int weekDay, int weekNo, int courseId, int teacherId);

    //记录签到信息
    void setRecord(
            String startTime, String endTime,
            int teacherId, String teacherName,
            int courseId, String courseName,
            int weekNo, int weekDay,
            String semester, ArrayList<CheckInRecordOfStudent> studentRecords);

}
