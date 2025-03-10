package com.college.registration ;

import java.io.Console;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    	try (Connection conn = DBConnection.getConnection()) {
    		
    		Scanner scanner = new Scanner(System.in);
            StudentRegistration studentReg = new StudentRegistration(conn);
            CourseManagement courseMgmt = new CourseManagement(conn);
            CourseEnrollment courseEnroll = new CourseEnrollment(conn);
            ViewCourses viewCourses = new ViewCourses(conn);
            
            Console console = System.console();  // Get the console object
            if (console == null) {
                System.out.println("No console available. Please run this application in a terminal/command line.");
                return;  // Exit if no console is available
            }
            
            System.out.println("\nWelcome to College Course Registration System");
            while (true) {
                System.out.println("1) Register a new student");
                System.out.println("2) Admin - Add a course");
                System.out.println("3) Student login (Register/Drop/View courses)");
                System.out.println("4) Exit");
                System.out.print("Select an option: ");
                
                int choice = -1;
                
                // Safely reading choice
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Clear invalid input
                    continue; // Restart loop
                }
                
                scanner.nextLine(); // Clear the newline character after int input

                switch (choice) {
                    case 1:
                        // Register a new student
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        studentReg.registerStudent(name, email, password);
                        break;

                    case 2:
                        // Admin adds a course
                        System.out.print("Enter admin password: ");
                        String adminPassword = scanner.nextLine();
                        if (studentReg.verifyAdminPassword(adminPassword)) {
                            System.out.print("Enter course name: ");
                            String courseName = scanner.nextLine();
                            System.out.print("Enter available seats: ");
                            int seats = scanner.nextInt();
                            System.out.print("Enter course slot (1-8): ");
                            int slot = scanner.nextInt();
                            courseMgmt.addCourse(courseName, seats, slot);
                        } else {
                            System.out.println("Invalid admin password.");
                        }
                        break;

                    case 3:
                        // Student login and register/drop/view courses
                        System.out.print("Enter Student Email: ");
                        String studentEmail = scanner.nextLine();
                         // for password protection
                        char[] Password = console.readPassword("Enter Password :") ;
                        String studentPassword = new String(Password) ;

                        int studentId = studentReg.getStudentIdByEmail(studentEmail);
                        if (studentId != -1 && studentReg.verifyStudentLogin(studentId, studentPassword)) {
                            
                            boolean flag = true ;
                            while(flag) {
                            	System.out.println("\n1) Register a course");
                                System.out.println("2) Drop a course");
                                System.out.println("3) View all registered courses");
                                System.out.println("4) Log Out ");
                                System.out.print("Select an option: ");
                            	int studentChoice = scanner.nextInt();
                            	
	                            switch (studentChoice) {
	                            	
	                                case 1:
	                                	//showing the available courses and their ids before registering
	                                	System.out.println("Available Courses:");
	                                	courseMgmt.viewAvailableCourses();
	                                	
	                                	
	                                    System.out.print("Enter Course ID to register: ");
	                                    int courseId = scanner.nextInt();
	                                    courseEnroll.enrollInCourse(studentId, courseId);
	                                    break ;
	
	                                case 2:
	                                    System.out.print("Enter Course ID to drop: ");
	                                    int dropCourseId = scanner.nextInt();
	                                    courseEnroll.dropCourse(studentId, dropCourseId);
	                                    break ; 
	                                    
	                                case 3:
	                                    viewCourses.viewEnrolledCourses(studentId);
	                                    break ;
	                                default:
	                                    System.out.println("Logging out");
	                                    flag = false ;
	                                    break ; 
	                            }
                            }
                        } else {
                            System.out.println("Invalid login credentials.");
                        }
                        break;

                    case 4:
                        // Exit option
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

