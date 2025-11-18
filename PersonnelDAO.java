import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class PersonnelDAO {
    private Connection conn;

    public PersonnelDAO(Connection conn) {
        this.conn = conn;
    }

    public void addPersonnel(Personnel p) {
        String sql = "INSERT INTO Personnel (name, department_id, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getDepartmentId()); // store ID
            stmt.setString(3, p.getRole());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New personnel added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding personnel: " + e.getMessage());
        }
    }

    public void showPersonnel() {
        String sql = "SELECT p.personnel_id, p.name, d.department_name, p.role " +
                "FROM Personnel p " +
                "LEFT JOIN Departments d ON p.department_id = d.department_id"  ;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("No personnel found.\n");
                return;
            }

            System.out.println("\nID   | Name                 | Department             | Role");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-4d | %-20s | %-20s  | %-20s%n",
                        rs.getInt("personnel_id"),
                        rs.getString("name"),
                        rs.getString("department_name") != null ? rs.getString("department_name") : "None",
                        rs.getString("role"));
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying personnel: " + e.getMessage());
        }
    }


    public void updatePersonnel(int id, Personnel p) {
        String sql = "UPDATE Personnel SET name = ?, department_id = ?, role = ? WHERE personnel_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getDepartmentId()); // new department ID
            stmt.setString(3, p.getRole());
            stmt.setInt(4, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Personnel updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating personnel: " + e.getMessage());
        }
    }

    public void deletePersonnel(int id) {
        String sql = "DELETE FROM Personnel WHERE personnel_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Personnel deleted successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting personnel: " + e.getMessage() + "\n");
        }
    }

    public void showPersonnelIDs() {
        String sql = "SELECT personnel_id FROM Personnel";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- PERSONNEL IDs ---");
            while (rs.next()) {
                System.out.println(rs.getInt("personnel_id"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying IDs: " + e.getMessage());
        }
    }

    public void showPersonnelNames() {
        String sql = "SELECT name FROM Personnel";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- PERSONNEL NAMES ---");
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying names: " + e.getMessage());
        }
    }

    public void showDepartments() {
        String sql = "SELECT department_id, department_name FROM Departments ORDER BY department_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- DEPARTMENTS ---");
            System.out.println("ID | Department Name");

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


    public void showRoles() {
        String sql = "SELECT DISTINCT role FROM Personnel";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- ROLES ---");
            while (rs.next()) {
                System.out.println(rs.getString("role"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying roles: " + e.getMessage());
        }
    }

}
