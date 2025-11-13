import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class TrainingProgramDAO {
    private Connection conn;

    public TrainingProgramDAO(Connection conn) {
        this.conn = conn;
    }

    public void addTrainingProgram(TrainingProgram t) {
        String sql = "INSERT INTO TrainingProgram (title, provider, training_date, duration, cost) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getTitle());
            stmt.setString(2, t.getProvider());
            stmt.setDate(3, t.getTrainingDate());
            stmt.setInt(4, t.getDuration());
            stmt.setDouble(5, t.getCost());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New training program added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding training program: " + e.getMessage());
        }
    }

    public void showTrainingPrograms() {
        String sql = "SELECT training_id, title, provider, training_date, duration, cost " +
                "FROM TrainingProgram";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("No training programs found.\n");
                return;
            }

            System.out.println("\nID | Title | Provider | Date | Duration (hrs) | Cost");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %d | %.2f%n",
                        rs.getInt("training_id"),
                        rs.getString("title"),
                        rs.getString("provider"),
                        rs.getDate("training_date"),
                        rs.getInt("duration"),
                        rs.getDouble("cost"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying training programs: " + e.getMessage());
        }
    }

    public void updateTrainingProgram(int id, TrainingProgram t) {
        String sql = "UPDATE TrainingProgram " +
                "SET title = ?, provider = ?, training_date = ?, duration = ?, cost = ? " +
                "WHERE training_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getTitle());
            stmt.setString(2, t.getProvider());
            stmt.setDate(3, t.getTrainingDate());
            stmt.setInt(4, t.getDuration());
            stmt.setDouble(5, t.getCost());
            stmt.setInt(6, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Training program updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating training program: " + e.getMessage());
        }
    }

    public void deleteTrainingProgram(int id) {
        String sql = "DELETE FROM TrainingProgram WHERE training_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Training program deleted successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting training program: " + e.getMessage() + "\n");
        }
    }

    public void showTrainingProgramIDs() {
        String sql = "SELECT training_id FROM TrainingProgram";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- TRAINING PROGRAM IDs ---");
            while (rs.next()) {
                System.out.println(rs.getInt("training_id"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error displaying training program IDs: " + e.getMessage());
        }
    }

    public void showTrainingTitles() {
        String sql = "SELECT title FROM TrainingProgram";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- TRAINING TITLES ---");
            while (rs.next()) {
                System.out.println(rs.getString("title"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error displaying training titles: " + e.getMessage());
        }
    }

    // View list of employees enrolled in a specific training program, with status
    public void showTrainingEnrollees(int trainingId) {
        String sql = "SELECT tr.record_id, p.personnel_id, p.name, tr.status, tr.completion_date " +
                "FROM TrainingRecord tr " +
                "JOIN Personnel p ON tr.personnel_id = p.personnel_id " +
                "WHERE tr.training_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trainingId);
            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("No enrollees found for this training program.\n");
                    return;
                }

                System.out.println("\nRecordID | PersonnelID | Name | Status | Completion Date");
                while (rs.next()) {
                    System.out.printf("%d | %d | %s | %s | %s%n",
                            rs.getInt("record_id"),
                            rs.getInt("personnel_id"),
                            rs.getString("name"),
                            rs.getString("status"),
                            rs.getDate("completion_date"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error displaying training enrollees: " + e.getMessage());
        }
    }
}
