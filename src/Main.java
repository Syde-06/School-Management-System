import java.util.*;  // Import Java utility classes (Scanner, ArrayList, etc.)

public class Main {
    // Service classes hold the logic for managing students, teachers, staff, and subjects
    private static StudentService studentService;
    private static TeacherService teacherService;
    private static StaffService staffService;
    private static SubjectService subjectService;
    private static Scanner scanner;  // Scanner reads user input from the keyboard
    
    public static void main(String[] args) {
        // Entry point: set up everything and start the menu loop
        initialize();  
        boolean running = true;  // Flag to keep the program running until user chooses to exit
        while (running) {  // Loop until running is set to false
            displayMenu();  // Show options to the user
            int choice = getIntInput("Enter your choice: ");  // Read the user's menu choice
            
            switch (choice) {  // Handle the user's choice
                case 1:
                    addPerson();  // Option 1: add a new person
                    break;
                case 2:
                    enrollStudentToSubject();  // Option 2: enroll a student
                    break;
                case 3:
                    assignSubjectToTeacher();  // Option 3: assign a teacher
                    break;
                case 4:
                    displayAllPeople();  // Option 4: show all people
                    break;
                case 5:
                    displayStudentSubjects();  // Option 5: show student enrollments
                    break;
                case 6:
                    displayTeacherSubjects();  // Option 6: show teacher assignments
                    break;
                case 7:
                    System.out.println("Thank you for using the SY University Management System!");  // Goodbye message
                    running = false;  // Exit the loop
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");  // Any other number is invalid
            }
        }
        scanner.close();  // Close the Scanner to free resources
    }
    
    public static void initialize() {
        // Prepare the services and initial data
        scanner = new Scanner(System.in);  // Create Scanner for reading input
        
        // Create new service objects for each type of person and subjects
        studentService = new StudentService();
        teacherService = new TeacherService();
        staffService = new StaffService();
        subjectService = new SubjectService();
        
        // Preload some subjects so we can enroll and assign people
        subjectService.addSubject(new Subject("MATH101", "Basic Mathematics", 3));
        subjectService.addSubject(new Subject("ENG101", "English Composition", 3));
        subjectService.addSubject(new Subject("COMP101", "Introduction to Computing", 4));
        subjectService.addSubject(new Subject("SCI101", "General Science", 4));
        subjectService.addSubject(new Subject("HIST101", "World History", 3));
    }
    
    public static void displayMenu() {
        // Print the main menu options
        System.out.println("\n=== SY UNIVERSITY MANAGEMENT SYSTEM ===");
        System.out.println("1. Add a Person (Student, Teacher, or Staff)");
        System.out.println("2. Enroll a Student in a Subject");
        System.out.println("3. Assign a Subject to a Teacher");
        System.out.println("4. Display All Registered People");
        System.out.println("5. Display Student Enrolled Subjects");
        System.out.println("6. Display Teacher Assigned Subjects");
        System.out.println("7. Exit");
    }
    
    public static void addPerson() {
        // Handle adding a student, teacher, or staff member
        System.out.println("\n=== ADD A PERSON ===");
        System.out.println("1. Add a Student");
        System.out.println("2. Add a Teacher");
        System.out.println("3. Add a Staff");
        
        int choice = getIntInput("Enter your choice (1-3): ");  // Get user selection
        while (choice < 1 || choice > 3) {  // Validate choice
            System.out.println("❌ Invalid choice! Please enter a number between 1 and 3.");
            choice = getIntInput("Enter your choice (1-3): ");
        }
        
        int id = getIntInput("Enter ID: ");  // Ask for a unique ID
        
        // Make sure no one else has the same ID
        if (studentService.findById(id) != null || 
            teacherService.findById(id) != null || 
            staffService.findById(id) != null) {
            System.out.println("❌ Person with ID " + id + " already exists!");
            return;  // Stop if ID is taken
        }
        
        String name = getStringInput("Enter Name: ");  // Ask for the person's name
        char first;
        switch (choice) {
            case 1: // Student
                first = name.charAt(0);  // Get first letter of name
                name = Character.toUpperCase(first) + name.substring(1);  // Capitalize name
                Student student = new Student(id, name);  // Create new Student object
                studentService.addStudent(student);  // Save the student
                System.out.println("✅ Student added successfully!");
                break;
            case 2: // Teacher
                first = name.charAt(0);
                name = Character.toUpperCase(first) + name.substring(1);
                Teacher teacher = new Teacher(id, name);  // Create Teacher object
                teacherService.addTeacher(teacher);  // Save teacher
                System.out.println("✅ Teacher added successfully!");
                break;
            case 3: // Staff
                String position = getStringInput("Enter Position: ");  // Ask for job title
                first = position.charAt(0);
                char first2 = name.charAt(0);
                String pos = Character.toUpperCase(first) + position.substring(1);  // Capitalize position
                name = Character.toUpperCase(first2) + name.substring(1);  // Capitalize name
                Staff staff = new Staff(id, name);  // Create Staff object
                staff.setPosition(pos);  // Set the staff's position
                staffService.addStaff(staff);  // Save staff
                System.out.println("✅ Staff added successfully!");
                break;
        }
    }
    
    public static void enrollStudentToSubject() {
        // Enroll an existing student in one of the subjects
        System.out.println("\n=== ENROLL STUDENT TO SUBJECT ===");
        displayStudents();  // Show list of students
        int studentId = getIntInput("Enter Student ID: ");
        
        Student student = studentService.findById(studentId);  // Look up the student
        if (student == null) {
            System.out.println("❌ No student found with ID " + studentId);
            return;  // Stop if not found
        }
        
        System.out.println("\nAvailable Subjects:");
        ArrayList<Subject> availableSubjects = subjectService.getAllSubjects();  // Get subjects
        
        if (availableSubjects.isEmpty()) {
            System.out.println("❌ No subjects available!");
            return;
        }
        
        // List subjects with numbers for selection
        for (int i = 0; i < availableSubjects.size(); i++) {
            System.out.println((i + 1) + ". " + availableSubjects.get(i));
        }
        
        int subjectChoice = getIntInput("Enter Subject Number: ");  // Ask which subject
        if (subjectChoice < 1 || subjectChoice > availableSubjects.size()) {
            System.out.println("❌ Invalid subject choice!");
            return;
        }
        
        Subject selectedSubject = availableSubjects.get(subjectChoice - 1);
        studentService.enrollInSubject(student, selectedSubject);  // Enroll student
        System.out.println("✅ " + student.getName() + " enrolled in " + selectedSubject.getName() + " successfully!");
    }
    
    public static void assignSubjectToTeacher() {
        // Assign a subject to a teacher similar to enrolling students
        System.out.println("\n=== ASSIGN SUBJECT TO TEACHER ===");
        displayTeachers();  // Show list of teachers
        int teacherId = getIntInput("Enter Teacher ID: ");
        
        Teacher teacher = teacherService.findById(teacherId);  // Find teacher
        if (teacher == null) {
            System.out.println("❌ No teacher found with ID " + teacherId);
            return;
        }
        
        System.out.println("\nAvailable Subjects:");
        ArrayList<Subject> availableSubjects = subjectService.getAllSubjects();  // Get subjects
        
        if (availableSubjects.isEmpty()) {
            System.out.println("❌ No subjects available!");
            return;
        }
        
        for (int i = 0; i < availableSubjects.size(); i++) {
            System.out.println((i + 1) + ". " + availableSubjects.get(i));
        }
        
        int subjectChoice = getIntInput("Enter Subject Number: ");
        if (subjectChoice < 1 || subjectChoice > availableSubjects.size()) {
            System.out.println("❌ Invalid subject choice!");
            return;
        }
        
        Subject selectedSubject = availableSubjects.get(subjectChoice - 1);
        teacherService.assignSubject(teacher, selectedSubject);  // Assign subject
        System.out.println("✅ " + selectedSubject.getName() + " assigned to " + teacher.getName() + " successfully!");
    }
    
    public static void displayTeachers() {
        // Show all teachers in a formatted list
        System.out.println("\n📊 ALL REGISTERED TEACHERS");
        System.out.println("==================================");
        
        ArrayList<Teacher> teachers = teacherService.getAllTeachers();  // Get teachers
        
        if (teachers.isEmpty()) {
            System.out.println("❌ No people registered yet.");
        } else {
            for (Teacher teacher : teachers) {
                System.out.println(teacher.display());  // Use display method to show teacher details
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayStudents() {
        // Show all students
        System.out.println("\n📊 ALL REGISTERED STUDENTS");
        System.out.println("==================================");
        
        ArrayList<Student> students = studentService.getAllStudents();  // Get students
        
        if (students.isEmpty()) {
            System.out.println("❌ No people registered yet.");
        } else {
            for (Student student : students) {
                System.out.println(student.display());  // Show student details
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayAllPeople() {
        // Show students, teachers, and staff all together
        System.out.println("\n📊 ALL REGISTERED PEOPLE");
        System.out.println("==================================");
        
        ArrayList<Student> students = studentService.getAllStudents();
        ArrayList<Teacher> teachers = teacherService.getAllTeachers();
        ArrayList<Staff> staffMembers = staffService.getAllStaff();
        
        // If nobody exists yet, show a message
        if (students.isEmpty() && teachers.isEmpty() && staffMembers.isEmpty()) {
            System.out.println("❌ No people registered yet.");
        } else {
            // List each category
            for (Student student : students) System.out.println(student.display());
            for (Teacher teacher : teachers) System.out.println(teacher.display());
            for (Staff staff : staffMembers) System.out.println(staff.display());
        }
        
        System.out.println("==================================");
    }
    
    public static void displayStudentSubjects() {
        // For each student, list the subjects they are enrolled in
        System.out.println("\n📊 STUDENT ENROLLED SUBJECTS");
        System.out.println("==================================");
        
        ArrayList<Student> students = studentService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("❌ No students registered yet.");
        } else {
            for (Student student : students) {
                System.out.println("👤 " + student.getName() + " (ID: " + student.getId() + ")");  // Header line
                
                ArrayList<Subject> enrolledSubjects = student.getEnrolledSubjects();  // Get their subjects
                if (enrolledSubjects.isEmpty()) {
                    System.out.println("   ❌ Not enrolled in any subjects");
                } else {
                    for (Subject subject : enrolledSubjects) {
                        System.out.println("   " + subject);  // List each subject
                    }
                }
                System.out.println();
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayTeacherSubjects() {
        // For each teacher, list the subjects they teach
        System.out.println("\n📊 TEACHER ASSIGNED SUBJECTS");
        System.out.println("==================================");
        
        ArrayList<Teacher> teachers = teacherService.getAllTeachers();
        
        if (teachers.isEmpty()) {
            System.out.println("❌ No teachers registered yet.");
        } else {
            for (Teacher teacher : teachers) {
                System.out.println("👤 " + teacher.getName() + " (ID: " + teacher.getId() + ")");
                
                ArrayList<Subject> assignedSubjects = teacher.getAssignedSubjects();
                if (assignedSubjects.isEmpty()) {
                    System.out.println("   ❌ Not assigned to any subjects");
                } else {
                    for (Subject subject : assignedSubjects) {
                        System.out.println("   " + subject);
                    }
                }
                System.out.println();
            }
        }
        
        System.out.println("==================================");
    }
    
    public static int getIntInput(String prompt) {
        // Ask the user for a number and keep asking until they give a valid integer
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {  // If input is not an integer
            System.out.println("❌ Please enter a valid number!");
            System.out.print(prompt);
            scanner.next();  // Discard invalid input
        }
        int input = scanner.nextInt();  // Read the integer
        scanner.nextLine();  // Clear the rest of the line
        return input;  // Return the number to the caller
    }
    
    public static String getStringInput(String prompt) {
        // Ask the user for text and return their input
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
