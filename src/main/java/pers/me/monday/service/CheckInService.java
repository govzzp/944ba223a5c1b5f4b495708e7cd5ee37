package pers.me.monday.service;


import pers.me.monday.controller.responseEntity.CheckInRecordOfStudent;
import pers.me.monday.mapper.CourseMapper;
import pers.me.monday.mapper.RecordMapper;
import pers.me.monday.mapper.StudentSchemaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CheckInService {

    private final String CHECK_IN_MEM_STUDENT = "check-in-mem-student";
    private final String CHECK_IN_INFO = "check-in-info";
    private final String CHECK_IN_STATUS = "check-in-status";

    @Autowired
    TeacherCourseService teacherCourseService;

    @Autowired
    StudentSchemaMapper studentSchemaMapper;

    @Autowired
    LeaveService leaveService;

    @Autowired
    RedisTemplate<String,String> redisTemplate00;//check-info check-mem-student

    @Autowired
    RedisTemplate<String,String> redisTemplate01;//check-status

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    TaskService taskService;

    @Autowired
    RecordMapper recordMapper;


    //教师发起签到
    public ArrayList<HashMap<String,Object>> invokeCheckIn(
            int teacher_id,int week_no,int week_day,int course_id
    ){
        ArrayList<HashMap<String,Object>>
                SucceedList = leaveService.getSuccessfulStudentids(week_no,week_day,course_id);
        ArrayList<HashMap<String,Object>>
                UnSucceedList = leaveService.getLeaveUnSuccessfulStudentIds(week_no,week_day,course_id);
        for(var i:SucceedList){
            i.put("status","已请假");
        }
        for(var i:UnSucceedList){
            i.put("status","未签到");
        }

        var list = new ArrayList<HashMap<String,Object>>();
        list.addAll(SucceedList);
        list.addAll(UnSucceedList);

        //在redis中产生签到记录
        var course = courseMapper.getCourseById(course_id);
        String courseName = course.getName();
        String teacherName = course.getTeacherName();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String startTime = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.SECOND,20);
        String endTime = simpleDateFormat.format(calendar.getTime());
                //生成签到教师信息
        String ValueOfCheckInInfo = teacherName+":"+course_id+":"+courseName+":"+startTime+":"+endTime+":"+week_day+":"+week_no;
        redisTemplate00.opsForHash().put(CHECK_IN_INFO,String.valueOf(teacher_id),ValueOfCheckInInfo);
                //批量插入签到学生信息
        HashMap<String,String> check_in_mem_student = new HashMap<>();
        for(var i:list){
            check_in_mem_student.put(String.valueOf(i.get("id")),String.valueOf(teacher_id));
        }
        redisTemplate00.opsForHash().putAll(CHECK_IN_MEM_STUDENT,check_in_mem_student);

        HashMap<String,String> check_in_status = new HashMap<>();
        for(var i:list){
                check_in_status.put(String.valueOf(i.get("id")),(String)i.get("status")+":"+i.get("name")+":"+i.get("dept_name"));
        }
        redisTemplate01.opsForHash().putAll(CHECK_IN_STATUS+":"+teacher_id,check_in_status);


//        //学生姓名HashMap
//        HashMap<Integer,String> nameMap = new HashMap<>();
//        for(var i:list){
//            nameMap.put((int)i.get("id"),(String)i.get("name"));
//        }
//        System.out.println(nameMap.toString());


        //redis批量删除fields
        ArrayList<String> studentsList = new ArrayList<>();
        for(var i:list){
            studentsList.add(String.valueOf(i.get("id")));
        }
        int size = studentsList.size();
        String[] studentsArray = studentsList.toArray(new String[size]);

        //编写若干时间(20s)之后进行的持久化签到记录操作
        taskService.runATask(new TimerTask() {

            @Override
            public void run() {
                Map<Object, Object> check_in_status_result =
                        redisTemplate01.opsForHash().entries(CHECK_IN_STATUS+":"+teacher_id);
                System.out.println("status:"+check_in_status_result.toString());

                        System.out.println(redisTemplate01.delete(CHECK_IN_STATUS+":"+teacher_id));
                        System.out.println(redisTemplate00.opsForHash().delete(CHECK_IN_INFO,String.valueOf(teacher_id)));
                        System.out.println(redisTemplate00.opsForHash().delete(CHECK_IN_MEM_STUDENT, studentsArray));
                ArrayList<CheckInRecordOfStudent> records = new ArrayList<>();

                for (Map.Entry<Object,Object> entry : check_in_status_result.entrySet()) {
                    String[] valueSlice = ((String)entry.getValue()).split(":");
                    var e = new CheckInRecordOfStudent();
                    e.id = Integer.parseInt((String) entry.getKey());
                    e.status=valueSlice[0];
                    e.name=valueSlice[1];
                    e.dept_name=valueSlice[2];
                    System.out.println(e.id+e.name+e.status+e.dept_name);
                    records.add(e);
                }
                 recordMapper.setRecord(
                         startTime,endTime,
                         teacher_id,teacherName,
                         course_id,courseName,
                         week_no,week_day,"2019-1",
                         records
                 );
            }
        }, 20*1000);
        //返回用户的签到信息
        return list;
    }


//         "name": "阿坦",
//         "dept_name": "理学院",
//         "id": 5,
//         "status": "已请假"
    //教师显示当前自己正在签到人员的名单
    public ArrayList<HashMap<String,String>> checkInMemberNow(int uid){

        var list = new ArrayList<HashMap<String,String>>();
        Map<Object, Object> check_in_status_curr =
                redisTemplate01.opsForHash().entries(CHECK_IN_STATUS+":"+uid);
        for (Map.Entry<Object,Object> entry : check_in_status_curr.entrySet()) {
            HashMap<String,String> hashMap = new HashMap<>();
            String[] valueSlice = ((String)entry.getValue()).split(":");
            hashMap.put("id", (String) entry.getKey());
            hashMap.put("name",valueSlice[1]);
            hashMap.put("status",valueSlice[0]);
            hashMap.put("dept_name",valueSlice[2]);
            list.add(hashMap);
        }
        return list;
    }
}
