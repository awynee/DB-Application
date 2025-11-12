import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DepartmentsDAO {
    private Connection conn;

    public DepartmentsDAO(Connection conn) {
        this.conn = conn;
    }

    public void addDepartment(String departmentName) {
        String sql = "INSERT INTO Departments (department_name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Department added successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error adding department: " + e.getMessage());
        }
    }

    public void showDepartments() {
        String sql = "SELECT department_id, department_name FROM Departments ORDER BY department_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("No departments found.\n");
                return;
            }

            System.out.println("\nID | Department Name");
            while (rs.next()) {
                System.out.printf("%d | %s%n",
                        rs.getInt("department_id"),
                        rs.getString("department_name"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying departments: " + e.getMessage());
        }
    }


    public void updateDepartment(int departmentId, String newName) {
        String sql = "UPDATE Departments SET department_name = ? WHERE department_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, departmentId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Department updated successfully!\n");
            } else {
                System.out.println("Department ID not found.\n");
            }
        } catch (SQLException e) {
            System.out.println("Error updating department: " + e.getMessage());
        }
    }

    // Delete a department
    public void deleteDepartment(int departmentId) {
        String sql = "DELETE FROM Departments WHERE department_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Department deleted successfully!\n");
            } else {
                System.out.println("Department ID not found.\n");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting department: " + e.getMessage());
        }
    }
}
