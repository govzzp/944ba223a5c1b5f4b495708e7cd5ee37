package pers.me.monday.model.table;

public class Teacher {
    private int id;
    private String deptNo;
    private String deptName;
    private String name;
    private int privilege;




    @Override
    public String toString(){
        return Integer.toString(id)+"|"+
                deptNo+"|"+
                deptName+"|"+
                name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public int getId() {
        return id;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public String getName() {
        return name;
    }

    public void setPrivilege(int privilege){
        this.privilege =privilege;
    }
    public int getPrivilege(){
        return privilege;
    }
}
