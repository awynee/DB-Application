import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;


public class TrainingProgramDAO {
    private Connection conn;

    public TrainingProgramDAO(Connection conn) {
        this.conn = conn;
    }


    public void addTrainingProgram() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter training title: ");
        String title = input.nextLine();

        System.out.print("Enter provider: ");
        String provider = input.nextLine();

        System.out.print("Enter training date (YYYY-MM-DD): ");
        java.sql.Date date = java.sql.Date.valueOf(input.nextLine());

        System.out.print("Enter duration (hours): ");
        int duration = input.nextInt();
        input.nextLine();

        System.out.print("Enter cost: ");
        double cost = input.nextDouble();
        input.nextLine();

        System.out.println("\n--- AVAILABLE CERTIFICATIONS ---");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT cert_id, name FROM Certification")) {

            while (rs.next()) {
                System.out.printf("%d: %s%n", rs.getInt("cert_id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching certifications: " + e.getMessage());
        }

        System.out.print("\nEnter the cert_id this training will grant (or 0 for none): ");
        int certId = input.nextInt();
        input.nextLine();
        Integer certIdObj = (certId == 0) ? null : certId;

        String sql = "INSERT INTO TrainingProgram (title, provider, training_date, duration, cost, cert_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, provider);
            stmt.setDate(3, date);
            stmt.setInt(4, duration);
            stmt.setDouble(5, cost);

            if (certIdObj != null) {
                stmt.setInt(6, certIdObj);
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New training program added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding training program: " + e.getMessage());
        }
    }

    public void showTrainingPrograms() {
        String sql = "SELECT tp.training_id, tp.title, tp.provider, tp.training_date, tp.duration, tp.cost, " +
                "c.name AS certification_name " +
                "FROM TrainingProgram tp " +
                "LEFT JOIN Certification c ON tp.cert_id = c.cert_id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("No training programs found.\n");
                return;
            }

            System.out.println("\n" +
                    String.format("%-6s | %-35s | %-30s | %-12s | %-15s | %-10s | %-30s",
                            "ID", "Title", "Provider", "Date", "Duration (hrs)", "Cost", "Certification"));
            System.out.println("---------------------------------------------------------------------------------------------------------------" +
                    "------------------------------------------------");

            while (rs.next()) {
                String certName = rs.getString("certification_name");
                if (certName == null) certName = "None";

                System.out.printf("%-6d | %-35s | %-30s | %-12s | %-15d | %-10.2f | %-30s%n",
                        rs.getInt("training_id"),
                        rs.getString("title"),
                        rs.getString("provider"),
                        String.valueOf(rs.getDate("training_date")),
                        rs.getInt("duration"),
                        rs.getDouble("cost"),
                        certName);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying training programs: " + e.getMessage());
        }
    }



    public void updateTrainingProgram(int id) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter new training title: ");
        String title = input.nextLine();

        System.out.print("Enter new provider: ");
        String provider = input.nextLine();

        System.out.print("Enter new training date (YYYY-MM-DD): ");
        java.sql.Date date = java.sql.Date.valueOf(input.nextLine());

        System.out.print("Enter new duration (hours): ");
        int duration = input.nextInt();
        input.nextLine();

        System.out.print("Enter new cost: ");
        double cost = input.nextDouble();
        input.nextLine();

        System.out.println("\n--- AVAILABLE CERTIFICATIONS ---");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT cert_id, name FROM Certification")) {

            while (rs.next()) {
                System.out.printf("%d: %s%n", rs.getInt("cert_id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching certifications: " + e.getMessage());
        }

        System.out.print("Enter the cert_id this training will grant (or 0 for none): ");
        int certId = input.nextInt();
        input.nextLine();
        Integer certIdObj = (certId == 0) ? null : certId;

        String sql = "UPDATE TrainingProgram " +
                "SET title = ?, provider = ?, training_date = ?, duration = ?, cost = ?, cert_id = ? " +
                "WHERE training_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, provider);
            stmt.setDate(3, date);
            stmt.setInt(4, duration);
            stmt.setDouble(5, cost);

            if (certIdObj != null) {
                stmt.setInt(6, certIdObj);
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            stmt.setInt(7, id);

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
