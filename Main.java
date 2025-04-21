// Import the Java utilities package which contains Scanner for user input
import java.util.*;

// This is the main class that runs the program
public class Main {
    // Create an instance of ComplicatedCaluculations class to use its methods
    static ComplicatedCaluculations func = new ComplicatedCaluculations();
    // Declare a scanner variable (though this isn't actually used in Main)
    private static Scanner scanner;
    
    // The main method - the entry point for the Java program
    public static void main(String[] args) {
        // Initialize the system data (subjects list and scanner)
        func.initialize();
        // Variable to control the main program loop
        boolean running = true;
        // Main program loop - continues until the user chooses to exit
        while (running) {
            // Display the menu options
            func.displayMenu();
            // Get the user's choice
            int choice = func.getIntInput("Enter your choice: ");
            
            // Process the user's choice using a switch statement
            switch (choice) {
                case 1: // If user chose option 1
                    func.addPerson(); // Call method to add a person
                    break;
                case 2: // If user chose option 2
                    func.enrollStudentToSubject(); // Call method to enroll a student
                    break;
                case 3: // If user chose option 3
                    func.assignSubjectToTeacher(); // Call method to assign a subject to a teacher
                    break;
                case 4: // If user chose option 4
                    func.displayAllPeople(); // Call method to display all people
                    break;
                case 5: // If user chose option 5
                    func.displayStudentSubjects(); // Call method to display student subjects
                    break;
                case 6: // If user chose option 6
                    func.displayTeacherSubjects(); // Call method to display teacher subjects
                    break;
                case 7: // If user chose option 7
                    System.out.println("Thank you for using the School Management System!");
                    running = false; // Set running to false to exit the loop
                    break;
                default: // If user entered an invalid choice
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
        // Close the scanner to free resources (though this scanner was never initialized)
        scanner.close();
    }
}
