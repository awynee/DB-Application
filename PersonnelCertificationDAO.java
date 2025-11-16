import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Scanner;

public class PersonnelCertificationDAO {
    private Connection conn;

    public PersonnelCertificationDAO(Connection conn) {
        this.conn = conn;
    }

    // Used by auto-issue (Transaction 2) and manual assign (Transaction 3 menu)
    public void assignCertification(int personnelId, int certId) {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        // Get validity_period from Certification
        String vsql = "SELECT validity_period FROM Certification WHERE cert_id = ?";
        int validity = 0;

        try (PreparedStatement vstmt = conn.prepareStatement(vsql)) {
            vstmt.setInt(1, certId);
            try (ResultSet rs = vstmt.executeQuery()) {
                if (rs.next()) {
                    validity = rs.getInt("validity_period");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading validity period: " + e.getMessage());
        }

        // compute expiry date: today + validity years
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.YEAR, validity);
        java.sql.Date expiry = new java.sql.Date(cal.getTimeInMillis());

        String sql = "INSERT INTO PersonnelCertification (personnel_id, cert_id, issue_date, expiry_date) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personnelId);
            stmt.setInt(2, certId);
            stmt.setDate(3, today);
            stmt.setDate(4, expiry);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Certification assigned successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error assigning certification: " + e.getMessage());
        }
    }

    // Transaction 3: Certification Renewal
    public void renewCertification() {
        Scanner input = new Scanner(System.in);

        showAllPersonnelCertifications();

        System.out.print("Enter PersonnelCertification ID to renew: ");
        int id = input.nextInt();
        input.nextLine();

        // Need cert_id and validity_period
        int certId = -1;
        int validity = 0;

        String vsql = "SELECT pc.cert_id, c.validity_period " +
                "FROM PersonnelCertification pc " +
                "JOIN Certification c ON pc.cert_id = c.cert_id " +
                "WHERE pc.id = ?";

        try (PreparedStatement vstmt = conn.prepareStatement(vsql)) {
            vstmt.setInt(1, id);
            try (ResultSet rs = vstmt.executeQuery()) {
                if (rs.next()) {
                    certId = rs.getInt("cert_id");
                    validity = rs.getInt("validity_period");
                } else {
                    System.out.println("PersonnelCertification record not found.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching certification validity: " + e.getMessage());
            return;
        }

        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.YEAR, validity);
        java.sql.Date newExpiry = new java.sql.Date(cal.getTimeInMillis());

        String sql = "UPDATE PersonnelCertification SET issue_date = ?, expiry_date = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, today);
            stmt.setDate(2, newExpiry);
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Certification renewed successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error renewing certification: " + e.getMessage());
        }
    }

    public void showCertificationHistory(int personnelId) {
        String sql = "SELECT pc.id, c.name, pc.issue_date, pc.expiry_date " +
                "FROM PersonnelCertification pc " +
                "JOIN Certification c ON pc.cert_id = c.cert_id " +
                "WHERE pc.personnel_id = ?";

        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personnelId);
            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("\nNo certifications found for this personnel.\n");
                    return;
                }

                System.out.println("\nID | Certification | Issue Date | Expiry Date | Status");
                while (rs.next()) {
                    java.sql.Date expiry = rs.getDate("expiry_date");
                    String status = (expiry != null && !expiry.before(today)) ? "Active" : "Expired";

                    System.out.printf("%d | %s | %s | %s | %s%n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDate("issue_date"),
                            expiry,
                            status);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error displaying certification history: " + e.getMessage());
        }
    }

    public void showAllPersonnelCertifications() {
        String sql = "SELECT pc.id, p.name AS personnel_name, c.name AS cert_name, " +
                "pc.issue_date, pc.expiry_date " +
                "FROM PersonnelCertification pc " +
                "JOIN Personnel p ON pc.personnel_id = p.personnel_id " +
                "JOIN Certification c ON pc.cert_id = c.cert_id";

        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo personnel certifications found.\n");
                return;
            }

            System.out.println("\nID | Personnel | Certification | Issue Date | Expiry Date | Status");
            while (rs.next()) {
                java.sql.Date expiry = rs.getDate("expiry_date");
                String status = (expiry != null && !expiry.before(today)) ? "Active" : "Expired";

                System.out.printf("%d | %s | %s | %s | %s | %s%n",
                        rs.getInt("id"),
                        rs.getString("personnel_name"),
                        rs.getString("cert_name"),
                        rs.getDate("issue_date"),
                        expiry,
                        status);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error displaying personnel certifications: " + e.getMessage());
        }
    }
}
