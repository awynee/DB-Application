import java.sql.*;
import java.util.*;

public class ReportsDAO {

    private Connection conn;

    public ReportsDAO(Connection conn) {
        this.conn = conn;
    }


    // REPORT 1: Training Participation Report
    public void showTrainingParticipationReport() {
        String sql =
                "SELECT tp.training_id, tp.title, " +
                        "SUM(CASE WHEN tr.status = 'Enrolled' THEN 1 ELSE 0 END) AS enrolled, " +
                        "SUM(CASE WHEN tr.status = 'In-Progress' THEN 1 ELSE 0 END) AS inprogress, " +
                        "SUM(CASE WHEN tr.status = 'Completed' THEN 1 ELSE 0 END) AS completed, " +
                        "SUM(CASE WHEN tr.status = 'Failed' THEN 1 ELSE 0 END) AS failed " +
                        "FROM TrainingProgram tp " +
                        "LEFT JOIN TrainingRecord tr ON tp.training_id = tr.training_id " +
                        "GROUP BY tp.training_id, tp.title " +
                        "ORDER BY tp.training_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== TRAINING PARTICIPATION REPORT ===\n");

            System.out.printf("%-6s | %-35s | %-9s | %-12s | %-10s | %-7s%n",
                    "ID", "Training Title", "Enrolled", "In-Progress", "Completed", "Failed");
            System.out.println("---------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-6d | %-35s | %-9d | %-12d | %-10d | %-7d%n",
                        rs.getInt("training_id"),
                        rs.getString("title"),
                        rs.getInt("enrolled"),
                        rs.getInt("inprogress"),
                        rs.getInt("completed"),
                        rs.getInt("failed"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error generating participation report: " + e.getMessage());
        }
    }


    // REPORT 2: Training Completion Report
    public void showTrainingCompletionReport() {
        String sql =
                "SELECT tp.title, " +
                        "SUM(CASE WHEN tr.status = 'Completed' THEN 1 ELSE 0 END) AS passed, " +
                        "SUM(CASE WHEN tr.status = 'Failed' THEN 1 ELSE 0 END) AS failed " +
                        "FROM TrainingProgram tp " +
                        "LEFT JOIN TrainingRecord tr ON tp.training_id = tr.training_id " +
                        "GROUP BY tp.title " +
                        "ORDER BY tp.title";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== TRAINING COMPLETION REPORT ===\n");

            System.out.printf("%-40s | %-7s | %-7s | %-10s%n",
                    "Training Title", "Passed", "Failed", "Pass Rate");
            System.out.println("----------------------------------------------------------------------------");

            while (rs.next()) {
                int passed = rs.getInt("passed");
                int failed = rs.getInt("failed");
                int total = passed + failed;
                double rate = (total == 0 ? 0.0 : (passed * 100.0 / total));

                System.out.printf("%-40s | %-7d | %-7d | %-9.2f%%%n",
                        rs.getString("title"),
                        passed,
                        failed,
                        rate);
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error generating completion report: " + e.getMessage());
        }
    }


    // REPORT 3: Certification Status Report
    public void showCertificationStatusReport() {
        String sql =
                "SELECT p.name, " +
                        "SUM(CASE WHEN pc.expiry_date >= CURDATE() THEN 1 ELSE 0 END) AS active, " +
                        "SUM(CASE WHEN pc.expiry_date < CURDATE() THEN 1 ELSE 0 END) AS expired " +
                        "FROM Personnel p " +
                        "LEFT JOIN PersonnelCertification pc ON p.personnel_id = pc.personnel_id " +
                        "GROUP BY p.name " +
                        "ORDER BY p.name";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== CERTIFICATION STATUS REPORT ===\n");

            System.out.printf("%-30s | %-12s | %-12s%n",
                    "Personnel", "Active Certs", "Expired Certs");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-30s | %-12d | %-12d%n",
                        rs.getString("name"),
                        rs.getInt("active"),
                        rs.getInt("expired"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error generating certification status report: " + e.getMessage());
        }
    }
}
