public class Personnel {
    private int personnelId;
    private String name;
    private String department;
    private String role;

    public Personnel(String name, String department, String role) {
        this.name = name;
        this.department = department;
        this.role = role;
    }

    public int getPersonnelId() {
        return personnelId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
