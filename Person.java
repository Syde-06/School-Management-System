// Import the ArrayList class from the Java utilities package
import java.util.ArrayList;

// This defines a class (blueprint) for Person objects which can be Students, Teachers, or Staff
public class Person {
    // Common attributes for all person types
    private int id;             // Unique identifier for each person
    private String name;        // Person's name
    private int age;            // Person's age
    private String contactNumber; // Person's contact number
    private String type;        // Type of person (Student, Teacher, or Staff)
    
    // Student-specific attributes
    private String course;      // Student's enrolled course
    private ArrayList<Subject> enrolledSubjects; // List of subjects the student is enrolled in
    
    // Teacher-specific attributes
    private String department;  // Teacher's department
    private ArrayList<Subject> assignedSubjects; // List of subjects the teacher is assigned to
    
    // Staff-specific attributes
    private String position;    // Staff member's position/role
    
    // Constructor for creating a Student object
    public Person(int id, String name) {
        this.id = id;           // Set the ID
        this.name = name;       // Set the name
        this.type = "Student";  // Set the type to Student
        this.enrolledSubjects = new ArrayList<>(); // Initialize empty list for enrolled subjects
    }
    
    // Constructor for creating a Teacher object
    public Person(int id, String name, boolean isTeacher) {
        this.id = id;           // Set the ID
        this.name = name;       // Set the name
        this.type = "Teacher";  // Set the type to Teacher
        this.assignedSubjects = new ArrayList<>(); // Initialize empty list for assigned subjects
    }
    
    // Constructor for creating a Staff object
    public Person(int id, String name, String position, String staffType) {
        this.id = id;           // Set the ID
        this.name = name;       // Set the name
        this.position = position; // Set the position
        this.type = "Staff";    // Set the type to Staff
    }
    
    // Getter method for ID
    public int getId() {
        return id;              // Return the ID
    }
    
    // Setter method for ID
    public void setId(int id) {
        this.id = id;           // Update the ID
    }
    
    // Getter method for name
    public String getName() {
        return name;            // Return the name
    }
    
    // Setter method for name
    public void setName(String name) {
        this.name = name;       // Update the name
    }
    
    // Getter method for age
    public int getAge() {
        return age;             // Return the age
    }
    
    // Setter method for age
    public void setAge(int age) {
        this.age = age;         // Update the age
    }
    
    // Getter method for contact number
    public String getContactNumber() {
        return contactNumber;   // Return the contact number
    }
    
    // Setter method for contact number
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber; // Update the contact number
    }
    
    // Getter method for person type
    public String getType() {
        return type;            // Return the type (Student, Teacher, or Staff)
    }
    
    // Getter method for student's course
    public String getCourse() {
        return course;          // Return the course
    }
    
    // Setter method for student's course
    public void setCourse(String course) {
        this.course = course;   // Update the course
    }
    
    // Getter method for teacher's department
    public String getDepartment() {
        return department;      // Return the department
    }
    
    // Setter method for teacher's department
    public void setDepartment(String department) {
        this.department = department; // Update the department
    }
    
    // Getter method for staff's position
    public String getPosition() {
        return position;        // Return the position
    }
    
    // Setter method for staff's position
    public void setPosition(String position) {
        this.position = position; // Update the position
    }
    
    // Getter method for student's enrolled subjects
    public ArrayList<Subject> getEnrolledSubjects() {
        return enrolledSubjects; // Return the list of enrolled subjects
    }
    
    // Method to enroll a student in a subject
    public void enrollSubject(Subject subject) {
        this.enrolledSubjects.add(subject); // Add the subject to the enrolled subjects list
    }
    
    // Getter method for teacher's assigned subjects
    public ArrayList<Subject> getAssignedSubjects() {
        return assignedSubjects; // Return the list of assigned subjects
    }
    
    // Method to assign a subject to a teacher
    public void assignSubject(Subject subject) {
        this.assignedSubjects.add(subject); // Add the subject to the assigned subjects list
    }
    
    // Method to get formatted details of the person
    public String getDetails() {
        // Create basic details string with emoji, type, ID, and name
        String details = "👤 " + type + " | ID: " + id + " | Name: " + name;
        // If the person is Staff, add spacing to align the output and return
        if(type == "Staff") {
            details = "👤 " + type + "   | ID: " + id + " | Name: " + name;
            return details;
        }
        return details; // Return the formatted details
    }
}
