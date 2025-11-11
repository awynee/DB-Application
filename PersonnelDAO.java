import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class PersonnelDAO {
    private Connection conn;

    public PersonnelDAO(Connection conn) {
        this.conn = conn;
    }

    public void addPersonnel(Personnel p) {
        String sql = "INSERT INTO Personnel (name, department, role) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDepartment());
            stmt.setString(3, p.getRole());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New personnel added successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error adding personnel: " + e.getMessage() + "\n");
        }
    }

    public void showPersonnel(){
        String sql = "SELECT * FROM Personnel";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n\nID | Name | Department | Role");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("personnel_id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                String role = rs.getString("role");

                System.out.println(id + " | " + name + " | " + department + " | " + role);
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error displaying personnel: " + e.getMessage() + "\n");
        }
    }

    public void updatePersonnel(int id, Personnel p) {
        String sql = "UPDATE Personnel SET name = ?, department = ?, role = ? WHERE personnel_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDepartment());
            stmt.setString(3, p.getRole());
            stmt.setInt(4, id);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Personnel updated successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error adding personnel: " + e.getMessage() + "\n");
        }
    }
}
