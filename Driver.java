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
        PersonnelDAO perDao = new PersonnelDAO(conn);
        CertificationDAO cerDao = new CertificationDAO(conn);

        TrainingProgramDAO tpDao = new TrainingProgramDAO(conn);
        TrainingRecordDAO trDao = new TrainingRecordDAO(conn);
        PersonnelCertificationDAO pcDao = new PersonnelCertificationDAO(conn);


        int choice = -1;

        do {
            ClearScreen.clearScreen();
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
                        ClearScreen.clearScreen();
                        System.out.println("--- MANAGE PERSONNEL ---");
                        System.out.println("1. Add new personnel");
                        System.out.println("2. View Personnel List");
                        System.out.println("3. Update Personnel Info");
                        System.out.println("4. Delete Personnel");
                        System.out.println("5. View Specific Columns");
                        System.out.println("6. Back to Main Menu");
                        System.out.print("Choose an option: ");

                        manageChoice = input.nextInt();
                        input.nextLine();

                        switch (manageChoice) {
                            case 1:
                                System.out.println("\n=== ADD PERSONNEL ===");
                                System.out.print("Enter personnel name: ");
                                String name = input.nextLine();

                                deptDao.showDepartments();
                                System.out.print("Enter department ID: ");
                                int departmentID = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter role: ");
                                String role = input.nextLine();

                                perDao.addPersonnel(new Personnel(name, departmentID, role));
                                System.out.println("\n=== UPDATED PERSONNEL LIST ===");
                                perDao.showPersonnel();

                                ClearScreen.pause(1500);
                                break;

                            case 2:
                                System.out.println("\n=== PERSONNEL LIST ===");
                                perDao.showPersonnel();
                                ClearScreen.pause(1500);
                                break;

                            case 3:
                                System.out.println("\n=== UPDATE PERSONNEL ===");
                                perDao.showPersonnel();
                                System.out.print("Enter personnel ID to update: ");
                                int updateId = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new peronnel name: ");
                                name = input.nextLine();

                                deptDao.showDepartments();
                                System.out.print("Enter new department ID: ");
                                departmentID = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new role: ");
                                role = input.nextLine();

                                perDao.updatePersonnel(updateId, new Personnel(name, departmentID, role));
                                System.out.println("\n=== UPDATED PERSONNEL LIST ===");
                                perDao.showPersonnel();

                                ClearScreen.pause(1500);
                                break;

                            case 4:
                                System.out.println("\n=== DELETE PERSONNEL ===");
                                perDao.showPersonnel();
                                System.out.print("Enter personnel ID to delete: ");
                                int deleteId = input.nextInt();
                                input.nextLine();

                                perDao.deletePersonnel(deleteId);
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
                                        case 1 -> perDao.showPersonnelIDs();
                                        case 2 -> perDao.showPersonnelNames();
                                        case 3 -> deptDao.showDepartments();
                                        case 4 -> perDao.showRoles();
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

                case 2:

                    manageChoice = -1;
                    do {
                        ClearScreen.clearScreen();
                        System.out.println("--- MANAGE TRAINING PROGRAMS ---");
                        System.out.println("1. Add New Training Program");
                        System.out.println("2. View All Training Programs");
                        System.out.println("3. Update Training Program Information");
                        System.out.println("4. Delete Training Program");
                        System.out.println("5. View Specific Columns");
                        System.out.println("6. View Enrolled Personnel for a Training");
                        System.out.println("7. Back to Main Menu");
                        System.out.print("Choose an option: ");

                        manageChoice = input.nextInt();
                        input.nextLine();

                        switch (manageChoice) {

                            case 1:
                                System.out.println("\n=== ADD TRAINING PROGRAM ===");
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

                                tpDao.addTrainingProgram(new TrainingProgram(0, title, provider, date, duration, cost));

                                System.out.println("\n=== UPDATED TRAINING PROGRAM LIST ===");
                                tpDao.showTrainingPrograms();
                                ClearScreen.pause(1500);
                                break;

                            case 2:
                                System.out.println("\n=== TRAINING PROGRAM LIST ===");
                                tpDao.showTrainingPrograms();
                                ClearScreen.pause(1500);
                                break;

                            case 3:
                                System.out.println("\n=== UPDATE TRAINING PROGRAM ===");
                                tpDao.showTrainingPrograms();

                                System.out.print("Enter training program ID to update: ");
                                int updateId = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new title: ");
                                title = input.nextLine();

                                System.out.print("Enter new provider: ");
                                provider = input.nextLine();

                                System.out.print("Enter new training date (YYYY-MM-DD): ");
                                date = java.sql.Date.valueOf(input.nextLine());

                                System.out.print("Enter new duration (hours): ");
                                duration = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter new cost: ");
                                cost = input.nextDouble();
                                input.nextLine();

                                tpDao.updateTrainingProgram(updateId,
                                        new TrainingProgram(updateId, title, provider, date, duration, cost));

                                System.out.println("\n=== UPDATED TRAINING PROGRAM LIST ===");
                                tpDao.showTrainingPrograms();
                                ClearScreen.pause(1500);
                                break;

                            case 4:
                                System.out.println("\n=== DELETE TRAINING PROGRAM ===");
                                tpDao.showTrainingPrograms();

                                System.out.print("Enter training program ID to delete: ");
                                int deleteId = input.nextInt();
                                input.nextLine();

                                tpDao.deleteTrainingProgram(deleteId);
                                ClearScreen.pause(1500);
                                break;

                            case 5:
                                int viewChoice = -1;

                                do {
                                    ClearScreen.clearScreen();
                                    System.out.println("=== VIEW SPECIFIC COLUMNS ===");
                                    System.out.println("1. Training Program IDs");
                                    System.out.println("2. Titles");
                                    System.out.println("3. Providers");
                                    System.out.println("4. Back to Training Program Menu");
                                    System.out.print("Choose an option: ");

                                    viewChoice = input.nextInt();
                                    input.nextLine();

                                    switch (viewChoice) {
                                        case 1 -> tpDao.showTrainingProgramIDs();
                                        case 2 -> tpDao.showTrainingTitles();
                                        case 3 -> System.out.println("No provider-only method yet. (Optional to add)");
                                        case 4 -> System.out.println("Returning...");
                                        default -> System.out.println("Invalid option. Try again.");
                                    }
                                    ClearScreen.pause(1000);
                                } while (viewChoice != 4);

                                break;

                            case 6:
                                System.out.println("\n=== VIEW ENROLLED PERSONNEL ===");
                                tpDao.showTrainingProgramIDs();
                                System.out.print("Enter training ID: ");
                                int tId = input.nextInt();
                                input.nextLine();

                                tpDao.showTrainingEnrollees(tId);
                                ClearScreen.pause(1500);
                                break;

                            case 7:
                                System.out.println("Returning to main menu...");
                                ClearScreen.pause(1000);
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                                ClearScreen.pause(1000);
                        }

                    } while (manageChoice != 7);

                    break;


                case 3:
                    manageChoice = -1;

                    do {
                        ClearScreen.clearScreen();
                        System.out.println("--- MANAGE CERTIFICATIONS ---");
                        System.out.println("1. Add New Certification");
                        System.out.println("2. View All Certifications");
                        System.out.println("3. Update Certification Information");
                        System.out.println("4. Delete Certification");
                        System.out.println("5. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        manageChoice = input.nextInt();
                        input.nextLine();

                        switch(manageChoice) {
                            case 1:
                                System.out.println("\n=== ADD CERTIFICATION  ===");
                                cerDao.addCertification();
                                ClearScreen.pause(1500);
                                break;
                            case 2:
                                System.out.println("\n=== CERTIFICATION LIST ===");
                                cerDao.showCertifications();
                                ClearScreen.pause(1500);
                                break;

                            case 5:
                                System.out.println("Returning to main menu...");
                                ClearScreen.pause(1000);
                                break;
                            default:
                                System.out.println("Invalid option. Try again.");
                                break;
                        }
                    }while(manageChoice != 5);
                    break;

                case 4:
                    manageChoice = -1;

                    do {
                        ClearScreen.clearScreen();
                        System.out.println("--- ASSIGN TRAININGS TO PERSONNEL ---");
                        System.out.println("1. Record New Training for Personnel (Enroll)");
                        System.out.println("2. Update Training Status (Pass/Fail + Auto-Cert)");
                        System.out.println("3. View Personnel Training History");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        manageChoice = input.nextInt();
                        input.nextLine();

                        switch (manageChoice) {
                            case 1:
                                System.out.println("\n=== ENROLL PERSONNEL IN TRAINING ===");
                                System.out.println("\n--- PERSONNEL LIST ---");
                                perDao.showPersonnel();
                                System.out.println("\n--- TRAINING PROGRAMS ---");
                                tpDao.showTrainingPrograms();

                                trDao.enrollPersonnelInTraining();
                                ClearScreen.pause(1500);
                                break;

                            case 2:
                                System.out.println("\n=== UPDATE TRAINING COMPLETION ===");
                                trDao.showAllTrainingRecords();
                                trDao.completeTrainingAndIssueCertification();
                                ClearScreen.pause(1500);
                                break;

                            case 3:
                                System.out.println("\n=== VIEW PERSONNEL TRAINING HISTORY ===");
                                perDao.showPersonnel();
                                System.out.print("Enter Personnel ID: ");
                                int perId = input.nextInt();
                                input.nextLine();

                                trDao.showTrainingHistoryForPersonnel(perId);
                                ClearScreen.pause(1500);
                                break;

                            case 4:
                                System.out.println("Returning to main menu...");
                                ClearScreen.pause(1000);
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                                ClearScreen.pause(1000);
                        }
                    } while (manageChoice != 4);
                    break;


                case 5:
                    manageChoice = -1;

                    do {
                        ClearScreen.clearScreen();
                        System.out.println("--- ASSIGN CERTIFICATIONS TO PERSONNEL ---");
                        System.out.println("1. Record New Certification for Personnel (Manual Assign)");
                        System.out.println("2. Renew Existing Certification");
                        System.out.println("3. View Personnel Certification History");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        manageChoice = input.nextInt();
                        input.nextLine();

                        switch (manageChoice) {
                            case 1:
                                System.out.println("\n=== ASSIGN CERTIFICATION TO PERSONNEL ===");
                                System.out.println("\n--- PERSONNEL LIST ---");
                                perDao.showPersonnel();

                                System.out.println("\n=== CERTIFICATION LIST ===");
                                cerDao.showCertifications();

                                System.out.print("Enter Personnel ID: ");
                                int perId = input.nextInt();
                                input.nextLine();

                                System.out.print("Enter Certification ID: ");
                                int certId = input.nextInt();
                                input.nextLine();

                                pcDao.assignCertification(perId, certId);
                                ClearScreen.pause(1500);
                                break;

                            case 2:
                                System.out.println("\n=== RENEW CERTIFICATION ===");
                                pcDao.renewCertification();
                                ClearScreen.pause(1500);
                                break;

                            case 3:
                                System.out.println("\n=== VIEW PERSONNEL CERTIFICATION HISTORY ===");
                                perDao.showPersonnel();
                                System.out.print("Enter Personnel ID: ");
                                int pId = input.nextInt();
                                input.nextLine();

                                pcDao.showCertificationHistory(pId);
                                ClearScreen.pause(1500);
                                break;

                            case 4:
                                System.out.println("Returning to main menu...");
                                ClearScreen.pause(1000);
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                                ClearScreen.pause(1000);
                        }

                    } while (manageChoice != 4);
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
