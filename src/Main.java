import java.util.*;

public class Main {
    // Service classes for handling business logic
    private static StudentService studentService;
    private static TeacherService teacherService;
    private static StaffService staffService;
    private static SubjectService subjectService;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        initialize();
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addPerson();
                    break;
                case 2:
                    enrollStudentToSubject();
                    break;
                case 3:
                    assignSubjectToTeacher();
                    break;
                case 4:
                    displayAllPeople();
                    break;
                case 5:
                    displayStudentSubjects();
                    break;
                case 6:
                    displayTeacherSubjects();
                    break;
                case 7:
                    System.out.println("Thank you for using the SY University Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }
    
    public static void initialize() {
        // Initialize scanner
        scanner = new Scanner(System.in);
        
        // Initialize services
        studentService = new StudentService();
        teacherService = new TeacherService();
        staffService = new StaffService();
        subjectService = new SubjectService();
        
        // Add predefined subjects
        subjectService.addSubject(new Subject("MATH101", "Basic Mathematics", 3));
        subjectService.addSubject(new Subject("ENG101", "English Composition", 3));
        subjectService.addSubject(new Subject("COMP101", "Introduction to Computing", 4));
        subjectService.addSubject(new Subject("SCI101", "General Science", 4));
        subjectService.addSubject(new Subject("HIST101", "World History", 3));
    }
    
    public static void displayMenu() {
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
        System.out.println("\n=== ADD A PERSON ===");
        System.out.println("1. Add a Student");
        System.out.println("2. Add a Teacher");
        System.out.println("3. Add a Staff");
        
        int choice = getIntInput("Enter your choice (1-3): ");
        while (choice < 1 || choice > 3) {
            System.out.println("❌ Invalid choice! Please enter a number between 1 and 3.");
            choice = getIntInput("Enter your choice (1-3): ");
        }
        
        int id = getIntInput("Enter ID: ");
        
        // Check if ID is already in use by any person type
        if (studentService.findById(id) != null || 
            teacherService.findById(id) != null || 
            staffService.findById(id) != null) {
            System.out.println("❌ Person with ID " + id + " already exists!");
            return;
        }
        
        String name = getStringInput("Enter Name: ");
        char first;
        switch (choice) {
            case 1: // Student
            	first = name.charAt(0);
                name = Character.toUpperCase(first) + name.substring(1);
                Student student = new Student(id, name);
                studentService.addStudent(student);
                System.out.println("✅ Student added successfully!");
                break;
                
            case 2: // Teacher
            	first = name.charAt(0);
                name = Character.toUpperCase(first) + name.substring(1);
                Teacher teacher = new Teacher(id, name);
                teacherService.addTeacher(teacher);
                System.out.println("✅ Teacher added successfully!");
                break;
                
            case 3: // Staff
                String position = getStringInput("Enter Position: ");
                first = position.charAt(0);
                char first2 = name.charAt(0);
                String pos = Character.toUpperCase(first) + position.substring(1);
                name = Character.toUpperCase(first2) + name.substring(1);
                Staff staff = new Staff(id, name);
                staff.setPosition(pos);
                staffService.addStaff(staff);
                System.out.println("✅ Staff added successfully!");
                break;
        }
    }
    
    public static void enrollStudentToSubject() {
        System.out.println("\n=== ENROLL STUDENT TO SUBJECT ===");
        displayStudents();
        int studentId = getIntInput("Enter Student ID: ");
        
        Student student = studentService.findById(studentId);
        
        if (student == null) {
            System.out.println("❌ No student found with ID " + studentId);
            return;
        }
        
        System.out.println("\nAvailable Subjects:");
        ArrayList<Subject> availableSubjects = subjectService.getAllSubjects();
        
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
        studentService.enrollInSubject(student, selectedSubject);
        System.out.println("✅ " + student.getName() + " enrolled in " + selectedSubject.getName() + " successfully!");
    }
    
    public static void assignSubjectToTeacher() {
        System.out.println("\n=== ASSIGN SUBJECT TO TEACHER ===");
        displayTeachers();
        int teacherId = getIntInput("Enter Teacher ID: ");
        
        Teacher teacher = teacherService.findById(teacherId);
        
        if (teacher == null) {
            System.out.println("❌ No teacher found with ID " + teacherId);
            return;
        }
        
        System.out.println("\nAvailable Subjects:");
        ArrayList<Subject> availableSubjects = subjectService.getAllSubjects();
        
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
        teacherService.assignSubject(teacher, selectedSubject);
        System.out.println("✅ " + selectedSubject.getName() + " assigned to " + teacher.getName() + " successfully!");
    }
    
    public static void displayTeachers() {
        System.out.println("\n📊 ALL REGISTERED TEACHERS");
        System.out.println("==================================");
        
        ArrayList<Teacher> teachers = teacherService.getAllTeachers();
        
        if (teachers.isEmpty()) {
            System.out.println("❌ No people registered yet.");
        } else {
            for (Teacher teacher : teachers) {
                System.out.println(teacher.display());
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayStudents() {
        System.out.println("\n📊 ALL REGISTERED STUDENTS");
        System.out.println("==================================");
        
        ArrayList<Student> students = studentService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("❌ No people registered yet.");
        } else {
            for (Student student : students) {
                System.out.println(student.display());
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayAllPeople() {
        System.out.println("\n📊 ALL REGISTERED PEOPLE");
        System.out.println("==================================");
        
        ArrayList<Student> students = studentService.getAllStudents();
        ArrayList<Teacher> teachers = teacherService.getAllTeachers();
        ArrayList<Staff> staffMembers = staffService.getAllStaff();
        
        if (students.isEmpty() && teachers.isEmpty() && staffMembers.isEmpty()) {
            System.out.println("❌ No people registered yet.");
        } else {
            // Display each type of person using polymorphism (display() method)
            for (Student student : students) {
                System.out.println(student.display());
            }
            
            for (Teacher teacher : teachers) {
                System.out.println(teacher.display());
            }
            
            for (Staff staff : staffMembers) {
                System.out.println(staff.display());
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayStudentSubjects() {
        System.out.println("\n📊 STUDENT ENROLLED SUBJECTS");
        System.out.println("==================================");
        
        ArrayList<Student> students = studentService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("❌ No students registered yet.");
        } else {
            for (Student student : students) {
                System.out.println("👤 " + student.getName() + " (ID: " + student.getId() + ")");
                
                ArrayList<Subject> enrolledSubjects = student.getEnrolledSubjects();
                if (enrolledSubjects.isEmpty()) {
                    System.out.println("   ❌ Not enrolled in any subjects");
                } else {
                    for (Subject subject : enrolledSubjects) {
                        System.out.println("   " + subject);
                    }
                }
                System.out.println();
            }
        }
        
        System.out.println("==================================");
    }
    
    public static void displayTeacherSubjects() {
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
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Please enter a valid number!");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer
        return input;
    }
    
    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}