import java.util.Scanner;
import java.sql.Connection;

public class Driver {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your MySQL password: ");
        String password = input.nextLine();

        Connection conn = ConnectDB.getConnection(password);
        if (conn == null) {
            System.out.println("Failed to connect to database. Exiting...");
            return;
        }

        DepartmentsDAO deptDao = new DepartmentsDAO(conn);
        PersonnelDAO dao = new PersonnelDAO(conn);

        int choice = -1;

        do {
            ClearScreen.clearScreen(); // clear at start of main menu
            System.out.println("=== TRAINING AND CERTIFICATION MANAGEMENT SYSTEM ===");
            System.out.println("1. Manage Personnel");
            System.out.println("2. Manage Training Programs");
            System.out.println("3. Manage Certifications");
            System.out.println("4. Assign Trainings to Personnel");
            System.out.println("5. Assign Certifications to Personnel");
            System.out.println("6. Manage Departments");
            System.out.println("7. Exit Program");
            System.out.print("Choose an option: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    int manageChoice = -1;

                    do {
                        ClearScreen.clearScreen(); // clear each personnel submenu
                        System.out.println("--- MANAGE PERSONNEL ---");
                        System.out.println("1. Add new personnel");
                        System.out.println("2. View Personnel List");
                        System.out.println("3. Update Personnel Info");
                        System.out.println("4. Delete Personnel");
                        System.out.println("5. View Specific Columns");
                        System.out.println("6. Back to Main Menu");
                        System.out.print("Choose an option: ");

                        manageChoice = input.nextInt();
                        input.nextLine(); // consume newline

                        switch (manageChoice) {
                            case 1:
                                System.out.println("\n=== ADD PERSONNEL ===");
                                System.out.print("Enter name: ");
                                String name = input.nextLine();

                                deptDao.showDepartments();
                                System.out.print("Enter department ID: ");
                                int departmentID = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter role: ");
                                String role = input.nextLine();

                                dao.addPersonnel(new Personnel(name, departmentID, role));
                                System.out.println("\n=== UPDATED PERSONNEL LIST ===");
                                dao.showPersonnel();

                                ClearScreen.pause(1500); // pause 1.5 seconds
                                break;

                            case 2:
                                System.out.println("\n=== PERSONNEL LIST ===");
                                dao.showPersonnel();
                                ClearScreen.pause(1500);
                                break;

                            case 3:
                                System.out.println("\n=== UPDATE PERSONNEL ===");
                                dao.showPersonnel();
                                System.out.print("Enter personnel ID to update: ");
                                int updateId = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new name: ");
                                name = input.nextLine();

                                deptDao.showDepartments();
                                System.out.print("Enter new department ID: ");
                                departmentID = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new role: ");
                                role = input.nextLine();

                                dao.updatePersonnel(updateId, new Personnel(name, departmentID, role));
                                System.out.println("\n=== UPDATED PERSONNEL LIST ===");
                                dao.showPersonnel();

                                ClearScreen.pause(1500);
                                break;

                            case 4:
                                System.out.println("\n=== DELETE PERSONNEL ===");
                                dao.showPersonnel();
                                System.out.print("Enter personnel ID to delete: ");
                                int deleteId = input.nextInt();
                                input.nextLine();

                                dao.deletePersonnel(deleteId);
                                ClearScreen.pause(1500);
                                break;

                            case 5:
                                int viewId;
                                do {
                                    ClearScreen.clearScreen();
                                    System.out.println("=== VIEW SPECIFIC COLUMNS ===");
                                    System.out.println("1. Personnel ID");
                                    System.out.println("2. Name");
                                    System.out.println("3. Department");
                                    System.out.println("4. Role");
                                    System.out.println("5. Go back to Personnel Menu");
                                    System.out.print("Enter column number to view: ");

                                    viewId = input.nextInt();
                                    input.nextLine();

                                    switch (viewId) {
                                        case 1 -> dao.showPersonnelIDs();
                                        case 2 -> dao.showPersonnelNames();
                                        case 3 -> deptDao.showDepartments();
                                        case 4 -> dao.showRoles();
                                        case 5 -> System.out.println("Returning to Personnel Menu...");
                                        default -> System.out.println("Invalid option. Try again.");
                                    }
                                    ClearScreen.pause(1000);
                                } while (viewId != 5);
                                break;

                            case 6:
                                System.out.println("Returning to main menu...");
                                ClearScreen.pause(1000);
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                                ClearScreen.pause(1000);
                        }
                    } while (manageChoice != 6);
                    break;

                case 6:
                    int deptChoice;

                    do {
                        ClearScreen.clearScreen();
                        System.out.println("--- MANAGE DEPARTMENTS ---");
                        System.out.println("1. Add Department");
                        System.out.println("2. View Departments");
                        System.out.println("3. Update Department");
                        System.out.println("4. Delete Department");
                        System.out.println("5. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        deptChoice = input.nextInt();
                        input.nextLine();

                        switch(deptChoice){
                            case 1:
                                System.out.println("\n=== ADD NEW DEPARTMENT ===");
                                System.out.print("Enter department name: ");
                                String deptName = input.nextLine();
                                deptDao.addDepartment(deptName);
                                ClearScreen.pause(1500);
                                break;
                            case 2:
                                System.out.println("\n=== DEPARTMENTS LIST ===");
                                deptDao.showDepartments();
                                ClearScreen.pause(1500);
                                break;
                            case 3:
                                System.out.println("\n=== UPDATE DEPARTMENT ===");
                                deptDao.showDepartments();
                                System.out.print("Enter department ID to update: ");
                                int updateDeptId = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new department name: ");
                                String newName = input.nextLine();

                                deptDao.updateDepartment(updateDeptId, newName);
                                ClearScreen.pause(1500);
                                break;
                            case 4:
                                System.out.println("\n=== DELETE DEPARTMENT ===");
                                deptDao.showDepartments();
                                System.out.print("Enter department ID to delete: ");
                                int deleteDeptId = input.nextInt();
                                input.nextLine();

                                deptDao.deleteDepartment(deleteDeptId);
                                ClearScreen.pause(1500);
                                break;
                            case 5:
                                System.out.println("Returning to main menu...");
                                ClearScreen.pause(1000);
                                break;
                            default:
                                System.out.println("Invalid option. Try again.");
                                ClearScreen.pause(1000);
                        }
                    } while(deptChoice != 5);
                    break;

                case 7:
                    System.out.println("Exiting program...");
                    ClearScreen.pause(1000);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    ClearScreen.pause(1000);
            }

        } while (choice != 7);
    }
}
