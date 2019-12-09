package pers.me.monday.model.table;


import org.springframework.stereotype.Repository;

@Repository
public class Student {
    private int id;
    private String name;
    private String deptNo;
    private String deptName;


    @Override
    public String toString(){
        return "["+Integer.toString(id)+","+name+","+deptNo+","+deptName+"]";
    }








    //********************************
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }
    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getDeptName() {
        return deptName;
    }

}
