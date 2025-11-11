import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = -1;

        do{
            System.out.println("=== TRAINING AND CERTIFICATION MANAGEMENT SYSTEM ===");
            System.out.println("1. Manage Personnel");
            System.out.println("2. Manage Training Programs");
            System.out.println("3. Manage Certifications");
            System.out.println("4. Assign Trainings to Personnel");
            System.out.println("5. Assign Certifications to Personnel");
            System.out.println("6. Exit Program");
            System.out.print("Choose an option: ");

            choice = input.nextInt();

            switch(choice){
                case 1:
                    int manageChoice = -1;

                    System.out.println("\n--- MANAGE PERSONNEL ---");
                    System.out.println("1. Add new personnel");
                    System.out.println("2. View Personnel List");
                    System.out.println("3. Update Personnel Info");
                    System.out.println("4. Delete Personnel");
                    System.out.println("5. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    manageChoice = input.nextInt();
                    input.nextLine();

                    switch(manageChoice){
                        case 1:
                            Connection conn = ConnectDB.getConnection();
                            PersonnelDAO dao = new PersonnelDAO(conn);

                            System.out.print("Enter personnel name: ");
                            String name = input.nextLine();

                            System.out.print("Enter personnel department: ");
                            String department = input.nextLine();

                            System.out.print("Enter personnel role: ");
                            String role = input.nextLine();

                            Personnel p = new Personnel(name, department, role);

                            dao.addPersonnel(p);
                            break;

                        case 2:
                            conn = ConnectDB.getConnection();
                            dao = new PersonnelDAO(conn);

                            dao.showPersonnel();
                            break;

                        case 3:
                            conn = ConnectDB.getConnection();
                            dao = new PersonnelDAO(conn);

                            dao.showPersonnel();

                            System.out.print("Enter personnel ID to update: ");
                            int id = input.nextInt();
                            input.nextLine();

                            System.out.print("Enter new name: ");
                            name = input.nextLine();

                            System.out.print("Enter new department: ");
                            department = input.nextLine();

                            System.out.print("Enter new role: ");
                            role = input.nextLine();

                            p = new Personnel(name, department, role);

                            dao.updatePersonnel(id, p);

                            break;

                        case 4:
                            break;
                    }

                    break;

                case 2:
                    manageChoice = -1;

                    System.out.println("\n--- MANAGE TRAINING PROGRAMS ---");
                    System.out.println("1. Add New Training Program");
                    System.out.println("2. View All Training Programs");
                    System.out.println("3. Update Training Program Information");
                    System.out.println("4. Delete Training Program");
                    System.out.println("5. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    break;

                case 3:
                    manageChoice = -1;

                    System.out.println("\n--- MANAGE CERTIFICATIONS ---");
                    System.out.println("1. Add New Certification");
                    System.out.println("2. View All Certifications");
                    System.out.println("3. Update Certification Information");
                    System.out.println("4. Delete Certification");
                    System.out.println("5. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    break;

                case 4:
                    manageChoice = -1;

                    System.out.println("\n--- ASSIGN TRAININGS TO PERSONNEL ---");
                    System.out.println("1. Record New Training for Personnel");
                    System.out.println("2. Update Training Status (In-Progress / Completed)");
                    System.out.println("3. View Personnel Training History");
                    System.out.println("4. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    break;

                case 5:
                    manageChoice = -1;

                    System.out.println("\n--- ASSIGN CERTIFICATIONS TO PERSONNEL ---");
                    System.out.println("1. Record New Certification for Personnel");
                    System.out.println("2. Update Certification Expiry / Renewal Status");
                    System.out.println("3. View Personnel Certification History");
                    System.out.println("4. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    break;

                case 6:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("\nInvalid Option. Please try again.\n");
            }
        }while(choice != 6);
    }
}
