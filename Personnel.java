public class Personnel {
    private int personnelId;
    private String name;
    int departmentId;
    private String role;

    public Personnel(String name, int departmentId, String role) {
        this.name = name;
        this.departmentId = departmentId;
        this.role = role;
    }

    public Personnel(int personnelId, String name, int departmentId, String role) {
        this.personnelId = personnelId;
        this.name = name;
        this.departmentId = departmentId;
        this.role = role;
    }


    public int getPersonnelId() {
        return personnelId;
    }

    public String getName() {
        return name;
    }

    public int getDepartmentId() { return departmentId; };

    public String getRole() {
        return role;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
