import java.util.*;  // Import Java utility classes (e.g., ArrayList)

// Abstract base class that defines shared properties of all people
public abstract class Person {
    // Unique identifier for the person
    private int id;
    // Name of the person
    private String name;
    
    // Constructor to set up the ID and name when creating a person
    public Person(int id, String name) {
        this.id = id;       // Save the given ID
        this.name = name;   // Save the given name
    }

    // "Getter" method to read the ID
    public int getId() {
        return id;
    }
    
    // "Setter" method to change the ID later if needed
    public void setId(int id) {
        this.id = id;
    }
    
    // Getter for the person's name
    public String getName() {
        return name;
    }
    
    // Setter to update the person's name
    public void setName(String name) {
        this.name = name;
    }
    
    // Abstract method: each subclass must provide its own display text
    public abstract String display();
}

// Student class extends Person and adds a list of enrolled subjects
class Student extends Person {
    // List of subjects the student is enrolled in
    private ArrayList<Subject> enrolledSubjects;
    
    // Constructor calls the Person constructor and sets up the list
    public Student(int id, String name) {
        super(id, name);                    // Initialize id and name in Person
        this.enrolledSubjects = new ArrayList<>();  // Start with no subjects
    }
    
    // Provide access to the list of subjects
    public ArrayList<Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }
    
    // Method to add a subject to the student's list
    public void enrollSubject(Subject subject) {
        this.enrolledSubjects.add(subject);
    }
    
    // Implementation of display(): show student's role, ID, and name
    @Override
    public String display() {
        return "👤 Student | ID: " + getId() + " | Name: " + getName();
    }
}

// Teacher class extends Person and tracks assigned subjects
class Teacher extends Person {
    // Department the teacher belongs to (optional)
    private String department;
    // List of subjects this teacher teaches
    private ArrayList<Subject> assignedSubjects;
    
    // Constructor initializes Person fields and subject list
    public Teacher(int id, String name) {
        super(id, name);
        this.assignedSubjects = new ArrayList<>();
    }
    
    // Getter and setter for department
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    // Accessor for the list of assigned subjects
    public ArrayList<Subject> getAssignedSubjects() {
        return assignedSubjects;
    }
    
    // Add a subject to the teacher's list
    public void assignSubject(Subject subject) {
        this.assignedSubjects.add(subject);
    }
    
    // display() shows teacher role, ID, and name
    @Override
    public String display() {
        return "👤 Teacher | ID: " + getId() + " | Name: " + getName();
    }
}

// Staff class extends Person and adds a position attribute
class Staff extends Person {
    // Job title or role of the staff member
    private String position;
    
    // Constructor initializes Person fields
    public Staff(int id, String name) {
        super(id, name);
    }
    
    // Getter and setter for position
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    // display() shows position with ID and name
    @Override
    public String display() {
        return "👤 " + position + " | ID: " + getId() + " | Name: " + getName();
    }
}
