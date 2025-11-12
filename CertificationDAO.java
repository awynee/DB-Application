import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Date;

public class CertificationDAO {
    private Connection conn;

    public CertificationDAO(Connection conn) {
        this.conn = conn;
    }

    public void addCertification() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter certification name: ");
        String name = input.nextLine();

        System.out.print("Enter issuing body: ");
        String issuingBody = input.nextLine();

        System.out.print("Enter date earned (YYYY-MM-DD): ");
        String dateStr = input.nextLine();
        Date dateEarned = null;
        try {
            dateEarned = Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Setting date to NULL.");
        }

        System.out.print("Enter validity period (in years): ");
        int validity = 0;
        try {
            validity = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Setting validity period to 0.");
        }

        System.out.print("Enter renewal status: ");
        String renewalStatus = input.nextLine();

        String sql = "INSERT INTO Certification (name, issuing_body, date_earned, validity_period, renewal_status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, issuingBody);
            if (dateEarned != null)
                stmt.setDate(3, dateEarned);
            else
                stmt.setNull(3, java.sql.Types.DATE);
            stmt.setInt(4, validity);
            stmt.setString(5, renewalStatus);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Certification added successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error adding certification: " + e.getMessage());
        }
    }

    public void showCertifications() {
        String sql = "SELECT cert_id, name, issuing_body, date_earned, validity_period, renewal_status FROM Certification";

        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo certifications found.\n");
                return;
            }

            System.out.println("\nID | Name | Issuing Body | Date Earned | Validity (Years) | Renewal Status");

            while (rs.next()) {
                System.out.println(rs.getInt("cert_id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("issuing_body") + " | " +
                        (rs.getDate("date_earned") != null ? rs.getDate("date_earned") : "N/A") + " | " + "YEAR " +
                        rs.getInt("validity_period") + " | " +
                        rs.getString("renewal_status"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error displaying certifications: " + e.getMessage());
        }
    }
}

