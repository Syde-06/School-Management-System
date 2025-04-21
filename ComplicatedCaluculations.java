// Calculations
// This imports the Java utilities package which contains useful tools like ArrayLists and Scanner
import java.util.*;

// This defines a class (blueprint) called ComplicatedCaluculations
public class ComplicatedCaluculations {
	
    // These are class variables accessible throughout the class
    // ArrayList to store all people (students, teachers, staff)
    private static ArrayList<Person> people;
    // ArrayList to store all available subjects
    private static ArrayList<Subject> subjects;
    // Scanner object to accept user input from keyboard
    private static Scanner scanner;
    
    // This method sets up initial data for the program
    public void initialize() {
        // Create new empty collections for people and subjects
        people = new ArrayList<>();
        subjects = new ArrayList<>();
        // Set up the scanner to read input from the keyboard
        scanner = new Scanner(System.in);
        
        // Add 5 predefined subjects to the subjects list, each with code, name, and credit units
        subjects.add(new Subject("MATH101", "Basic Mathematics", 3));
        subjects.add(new Subject("ENG101", "English Composition", 3));
        subjects.add(new Subject("COMP101", "Introduction to Computing", 4));
        subjects.add(new Subject("SCI101", "General Science", 4));
        subjects.add(new Subject("HIST101", "World History", 3));
    }
    
    // This method displays the main menu options to the user
    public void displayMenu() {
        System.out.println("\n=== SY UNIVERSITY MANAGEMENT SYSTEM ===");
        System.out.println("1. Add a Person (Student, Teacher, or Staff)");
        System.out.println("2. Enroll a Student in a Subject");
        System.out.println("3. Assign a Subject to a Teacher");
        System.out.println("4. Display All Registered People");
        System.out.println("5. Display Student Enrolled Subjects");
        System.out.println("6. Display Teacher Assigned Subjects");
        System.out.println("7. Exit");
    }
    
    // Method to add a new person (student, teacher, or staff)
    public void addPerson() {
        // Show submenu for person types
        System.out.println("\n=== ADD A PERSON ===");
        System.out.println("1. Add a Student");
        System.out.println("2. Add a Teacher");
        System.out.println("3. Add a Staff");
        int choice;
        // This loop ensures the user enters a valid choice (1, 2, or 3)
        do {
            System.out.println("Enter your choice: ");
            // Check if the input is not a number
            while (!scanner.hasNextInt()) {
                System.out.println("❌ Please enter a valid number!");
                System.out.println("Enter your choice: ");
                scanner.next(); // Skip the invalid input
            }
            choice = scanner.nextInt(); // Read the user's choice
            scanner.nextLine(); // Clear the input buffer
            
            // Check if the choice is out of range
            if (choice > 3 || choice < 1) {
                System.out.println("❌ Invalid choice! Please enter a number between 1 and 3.");
            }
        } while (choice > 3 || choice < 1); // Keep asking until a valid choice is entered
        	
        // Get the ID for the new person
        int id = getIntInput("Enter ID: ");
        
        // Check if a person with this ID already exists
        if (findPersonById(id) != null) {
            System.out.println("❌ Person with ID " + id + " already exists!");
            return; // Exit the method if ID already exists
        }
        
        // Get the name for the new person
        String name = getStringInput("Enter Name: ");
        
        // Handle different person types based on the user's choice
        switch (choice) {
            case 1: // Student
                // Create a new student with ID and name
                people.add(new Person(id, name));
                System.out.println("✅ Student added successfully!");
                break;
            case 2: // Teacher
                // Create a new teacher with ID, name, and isTeacher=true
                people.add(new Person(id, name, true));
                System.out.println("✅ Teacher added successfully!");
                break;
            case 3: // Staff
                // Get additional position information for staff
                String position = getStringInput("Enter Role: ");
                // Create a new staff with ID, name, position, and type="Staff"
                people.add(new Person(id, name, position, "Staff"));
                System.out.println("✅ Staff added successfully!");
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }
    }
    
    // Method to enroll a student in a subject
    public void enrollStudentToSubject() {
        System.out.println("\n=== ENROLL STUDENT TO SUBJECT ===");
        // Get the student's ID
        int studentId = getIntInput("Enter Student ID: ");
        
        // Find the student with the provided ID
        Person student = findPersonById(studentId);
        
        // Check if a person with that ID exists
        if (student == null) {
            System.out.println("❌ No person found with ID " + studentId);
            return; // Exit if no person found
        }
        
        // Check if the person is actually a student
        if (!student.getType().equals("Student")) {
            System.out.println("❌ Person with ID " + studentId + " is not a student!");
            return; // Exit if not a student
        }
        
        // Display all available subjects
        System.out.println("\nAvailable Subjects:");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }
        
        // Get the user's subject choice
        int subjectChoice = getIntInput("Enter Subject Number: ");
        // Validate the subject choice is in range
        if (subjectChoice < 1 || subjectChoice > subjects.size()) {
            System.out.println("❌ Invalid subject choice!");
            return; // Exit if invalid choice
        }
        
        // Get the selected subject (subtract 1 because array indices start at 0)
        Subject selectedSubject = subjects.get(subjectChoice - 1);
        // Enroll the student in the selected subject
        student.enrollSubject(selectedSubject);
        // Confirm successful enrollment
        System.out.println("✅ " + student.getName() + " enrolled in " + selectedSubject.getName() + " successfully!");
    }
    
    // Method to assign a subject to a teacher
    public void assignSubjectToTeacher() {
        System.out.println("\n=== ASSIGN SUBJECT TO TEACHER ===");
        // Get the teacher's ID
        int teacherId = getIntInput("Enter Teacher ID: ");
        
        // Find the teacher with the provided ID
        Person teacher = findPersonById(teacherId);
        
        // Check if a person with that ID exists
        if (teacher == null) {
            System.out.println("❌ No person found with ID " + teacherId);
            return; // Exit if no person found
        }
        
        // Check if the person is actually a teacher
        if (!teacher.getType().equals("Teacher")) {
            System.out.println("❌ Person with ID " + teacherId + " is not a teacher!");
            return; // Exit if not a teacher
        }
        
        // Display all available subjects
        System.out.println("\nAvailable Subjects:");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }
        
        // Get the user's subject choice
        int subjectChoice = getIntInput("Enter Subject Number: ");
        // Validate the subject choice is in range
        if (subjectChoice < 1 || subjectChoice > subjects.size()) {
            System.out.println("❌ Invalid subject choice!");
            return; // Exit if invalid choice
        }
        
        // Get the selected subject (subtract 1 because array indices start at 0)
        Subject selectedSubject = subjects.get(subjectChoice - 1);
        // Assign the subject to the teacher
        teacher.assignSubject(selectedSubject);
        // Confirm successful assignment
        System.out.println("✅ " + selectedSubject.getName() + " assigned to " + teacher.getName() + " successfully!");
    }
    
    // Method to display all registered people
    public void displayAllPeople() {
        // Check if there are any people registered
        if (people.isEmpty()) {
            System.out.println("❌ No people registered yet.");
            return; // Exit if no people
        }
        
        // Display header for the list
        System.out.println("\n📊 ALL REGISTERED PEOPLE");
        System.out.println("==================================");
        // Loop through all people and display their details
        for (Person person : people) {
            System.out.println(person.getDetails());
        }
        System.out.println("==================================");
    }
    
    // Method to display all students and their enrolled subjects
    public void displayStudentSubjects() {
        // Flag to track if any students are found
        boolean hasStudents = false;
        
        // Display header for the list
        System.out.println("\n📊 STUDENT ENROLLED SUBJECTS");
        System.out.println("==================================");
        
        // Loop through all people
        for (Person person : people) {
            // Filter for only students
            if (person.getType().equals("Student")) {
                // Display student info
                System.out.println("👤 " + person.getName() + " (ID: " + person.getId() + ")");
                
                // Get the subjects the student is enrolled in
                ArrayList<Subject> subjects = person.getEnrolledSubjects();
                // Check if the student is enrolled in any subjects
                if (subjects == null || subjects.isEmpty()) {
                    System.out.println("   ❌ Not enrolled in any subjects");
                } else {
                    // Display each subject the student is enrolled in
                    for (Subject subject : subjects) {
                        System.out.println("   " + subject);
                    }
                }
                System.out.println(); // Add a blank line for readability
                hasStudents = true; // Mark that at least one student was found
            }
        }
        
        // Display message if no students are registered
        if (!hasStudents) {
            System.out.println("❌ No students registered yet.");
        }
        System.out.println("==================================");
    }
    
    // Method to display all teachers and their assigned subjects
    public void displayTeacherSubjects() {
        // Flag to track if any teachers are found
        boolean hasTeachers = false;
        
        // Display header for the list
        System.out.println("\n📊 TEACHER ASSIGNED SUBJECTS");
        System.out.println("==================================");
        
        // Loop through all people
        for (Person person : people) {
            // Filter for only teachers
            if (person.getType().equals("Teacher")) {
                // Display teacher info
                System.out.println("👤 " + person.getName() + " (ID: " + person.getId() + ")");
                
                // Get the subjects assigned to the teacher
                ArrayList<Subject> subjects = person.getAssignedSubjects();
                // Check if the teacher is assigned any subjects
                if (subjects == null || subjects.isEmpty()) {
                    System.out.println("   ❌ Not assigned to any subjects");
                } else {
                    // Display each subject the teacher is assigned to
                    for (Subject subject : subjects) {
                        System.out.println("   " + subject);
                    }
                }
                System.out.println(); // Add a blank line for readability
                hasTeachers = true; // Mark that at least one teacher was found
            }
        }
        
        // Display message if no teachers are registered
        if (!hasTeachers) {
            System.out.println("❌ No teachers registered yet.");
        }
        System.out.println("==================================");
    }
    
    // Helper method to find a person by their ID
    public Person findPersonById(int id) {
        // Loop through all people
        for (Person person : people) {
            // Check if this person has the ID we're looking for
            if (person.getId() == id) {
                return person; // Return the person if found
            }
        }
        return null; // Return null if no person with that ID is found
    }
    
    // Helper method to get an integer input from the user
    public int getIntInput(String prompt) {
        System.out.print(prompt); // Display the prompt message
        // Keep asking until a valid integer is entered
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Please enter a valid number!");
            System.out.print(prompt);
            scanner.next(); // Skip the invalid input
        }
        int input = scanner.nextInt(); // Read the integer input
        scanner.nextLine(); // Clear the input buffer (consume newline)
        return input; // Return the valid integer
    }
    
    // Helper method to get a string input from the user
    public String getStringInput(String prompt) {
        System.out.print(prompt); // Display the prompt message
        return scanner.nextLine(); // Read the string input
    }
}
