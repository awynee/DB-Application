import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TrainingRecordDAO {
    private Connection conn;

    public TrainingRecordDAO(Connection conn) {
        this.conn = conn;
    }

    // Transaction 1: Enrolling in a Training Program
    public void enrollPersonnelInTraining() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Personnel ID: ");
        int personnelId = input.nextInt();
        input.nextLine();

        System.out.print("Enter Training Program ID: ");
        int trainingId = input.nextInt();
        input.nextLine();

        String sql = "INSERT INTO TrainingRecord (personnel_id, training_id, status, completion_date) " +
                "VALUES (?, ?, 'Enrolled', NULL)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personnelId);
            stmt.setInt(2, trainingId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Enrollment recorded successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error enrolling personnel: " + e.getMessage());
        }
    }

    // Helper to show all training records (for choosing which to complete/update)
    public void showAllTrainingRecords() {
        String sql = "SELECT tr.record_id, p.name, tp.title, tr.status, tr.completion_date " +
                "FROM TrainingRecord tr " +
                "JOIN Personnel p ON tr.personnel_id = p.personnel_id " +
                "JOIN TrainingProgram tp ON tr.training_id = tp.training_id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo training records found.\n");
                return;
            }

            System.out.println("\nRecordID | Personnel | Training | Status | Completion Date");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %s%n",
                        rs.getInt("record_id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getDate("completion_date"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying training records: " + e.getMessage());
        }
    }

    // Transaction 2: Training Completion Update + Auto-Cert
    public void completeTrainingAndIssueCertification() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Training Record ID to update: ");
        int recordId = input.nextInt();
        input.nextLine();

        int personnelId = -1;
        int trainingId = -1;
        String findSql = "SELECT personnel_id, training_id FROM TrainingRecord WHERE record_id = ?";

        try (PreparedStatement findStmt = conn.prepareStatement(findSql)) {
            findStmt.setInt(1, recordId);
            try (ResultSet rs = findStmt.executeQuery()) {
                if (rs.next()) {
                    personnelId = rs.getInt("personnel_id");
                    trainingId = rs.getInt("training_id");
                } else {
                    System.out.println("Training record not found.\n");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding training record: " + e.getMessage());
            return;
        }

        System.out.print("Enter result (Pass/Fail): ");
        String result = input.nextLine().trim();
        String status;

        if (result.equalsIgnoreCase("Pass")) {
            status = "Completed";
        } else if (result.equalsIgnoreCase("Fail")) {
            status = "Failed";
        } else {
            System.out.println("Invalid result. Use Pass or Fail.");
            return;
        }

        System.out.println("\nChoose completion date:");
        System.out.println("1. Use today's date");
        System.out.println("2. Enter custom completion date (YYYY-MM-DD)");
        System.out.print("Enter choice: ");
        int dateChoice = input.nextInt();
        input.nextLine();

        java.sql.Date completionDate;
        if (dateChoice == 1) {
            completionDate = new java.sql.Date(System.currentTimeMillis());
        } else if (dateChoice == 2) {
            System.out.print("Enter completion date (YYYY-MM-DD): ");
            try {
                completionDate = java.sql.Date.valueOf(input.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date. Using today's date instead.");
                completionDate = new java.sql.Date(System.currentTimeMillis());
            }
        } else {
            System.out.println("Invalid option. Using today's date.");
            completionDate = new java.sql.Date(System.currentTimeMillis());
        }

        String updateSql = "UPDATE TrainingRecord SET status = ?, completion_date = ? WHERE record_id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, status);
            updateStmt.setDate(2, completionDate);
            updateStmt.setInt(3, recordId);

            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Training completion updated successfully!");
            } else {
                System.out.println("No rows updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating training completion: " + e.getMessage());
            return;
        }

        if (status.equals("Completed")) {
            String certSql = "SELECT tp.cert_id, c.validity_period " +
                    "FROM TrainingProgram tp " +
                    "JOIN Certification c ON tp.cert_id = c.cert_id " +
                    "WHERE tp.training_id = ?";

            try (PreparedStatement certStmt = conn.prepareStatement(certSql)) {
                certStmt.setInt(1, trainingId);
                try (ResultSet rs = certStmt.executeQuery()) {
                    if (rs.next()) {
                        int certId = rs.getInt("cert_id");
                        int validity = rs.getInt("validity_period");

                        // Check if already issued
                        String checkSql = "SELECT * FROM PersonnelCertification WHERE personnel_id = ? AND cert_id = ?";
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                            checkStmt.setInt(1, personnelId);
                            checkStmt.setInt(2, certId);
                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                if (checkRs.next()) {
                                    System.out.println("Certification already issued. Skipping.");
                                    return;
                                }
                            }
                        }

                        // Issue certification
                        String insertSql = "INSERT INTO PersonnelCertification (personnel_id, cert_id, issue_date, expiry_date) " +
                                "VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            java.sql.Date issueDate = completionDate;
                            java.sql.Date expiryDate = new java.sql.Date(issueDate.getTime());
                            if (validity > 0) {
                                expiryDate = new java.sql.Date(issueDate.getTime() + ((long) validity) * 30 * 24 * 60 * 60 * 1000);
                            }

                            insertStmt.setInt(1, personnelId);
                            insertStmt.setInt(2, certId);
                            insertStmt.setDate(3, issueDate);
                            insertStmt.setDate(4, expiryDate);

                            insertStmt.executeUpdate();
                            System.out.println("Certification issued successfully!");
                        }

                    } else {
                        System.out.println("No certification linked to this training. Skipping auto-issue.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error issuing certification: " + e.getMessage());
            }
        }
    }

    // Used for Transaction 1 "view training history" (for a specific person)
    public void showTrainingHistoryForPersonnel(int personnelId) {
        String sql = "SELECT tr.record_id, tp.title, tr.status, tr.completion_date " +
                "FROM TrainingRecord tr " +
                "JOIN TrainingProgram tp ON tr.training_id = tp.training_id " +
                "WHERE tr.personnel_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personnelId);
            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("\nNo training history for this personnel.\n");
                    return;
                }

                System.out.println("\nRecordID | Training Title | Status | Completion Date");
                while (rs.next()) {
                    System.out.printf("%d | %s | %s | %s%n",
                            rs.getInt("record_id"),
                            rs.getString("title"),
                            rs.getString("status"),
                            rs.getDate("completion_date"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error displaying training history: " + e.getMessage());
        }
    }
}
