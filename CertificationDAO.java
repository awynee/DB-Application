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

        System.out.print("Enter validity period (in years): ");
        int validity = 0;
        try {
            validity = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Setting validity period to 0.");
        }

        String sql = "INSERT INTO Certification (name, issuing_body, validity_period) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, issuingBody);
            stmt.setInt(3, validity);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Certification added successfully!\n");
            }
        } catch (SQLException e) {
            System.out.println("Error adding certification: " + e.getMessage());
        }
    }

    public void showCertifications() {
        String sql = "SELECT cert_id, name, issuing_body, validity_period FROM Certification";

        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo certifications found.\n");
                return;
            }

            System.out.println("\nID | Name | Issuing Body | Validity (Years)");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("cert_id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("issuing_body") + " | " +
                                rs.getInt("validity_period")
                );
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error displaying certifications: " + e.getMessage());
        }
    }

    public void updateCertification() {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter Certification ID to update: ");
        int certId = Integer.parseInt(sc.nextLine());

        String checkSql = "SELECT * FROM Certification WHERE cert_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, certId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Certification with ID " + certId + " not found.\n");
                return;
            }

            String currentName = rs.getString("name");
            String currentBody = rs.getString("issuing_body");
            int currentValidity = rs.getInt("validity_period");

            System.out.println("\n--- Current Certification Details ---");
            System.out.println("Name: " + currentName);
            System.out.println("Issuing Body: " + currentBody);
            System.out.println("Validity Period: " + currentValidity);
            System.out.println("------------------------------------");

            System.out.print("\nEnter new Name (leave blank to keep): ");
            String newName = sc.nextLine();
            if (newName.trim().isEmpty()) newName = currentName;

            System.out.print("Enter new Issuing Body (leave blank to keep): ");
            String newBody = sc.nextLine();
            if (newBody.trim().isEmpty()) newBody = currentBody;

            System.out.print("Enter new Validity Period (leave blank to keep): ");
            String validityInput = sc.nextLine();
            int newValidity = validityInput.trim().isEmpty()
                    ? currentValidity
                    : Integer.parseInt(validityInput);

            String updateSql = "UPDATE Certification SET name = ?, issuing_body = ?, validity_period = ? WHERE cert_id = ?";

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, newName);
                updateStmt.setString(2, newBody);
                updateStmt.setInt(3, newValidity);
                updateStmt.setInt(4, certId);

                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("\nCertification updated successfully!\n");
                } else {
                    System.out.println("\nUpdate failed.\n");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error updating certification: " + e.getMessage());
        }
    }

    public void deleteCertification() {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter Certification ID to delete: ");
        int certId;

        try {
            certId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.\n");
            return;
        }

        String checkSql = "SELECT * FROM Certification WHERE cert_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, certId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Certification with ID " + certId + " not found.\n");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error checking certification: " + e.getMessage());
            return;
        }

        String deleteSql = "DELETE FROM Certification WHERE cert_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, certId);

            int rows = deleteStmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Certification deleted successfully!\n");
            } else {
                System.out.println("Failed to delete certification.\n");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getErrorCode() == 1451) {
                System.out.println("Cannot delete: Certification is still referenced by another record.\n");
            } else {
                System.out.println("Error deleting certification: " + e.getMessage());
            }
        }
    }


}

