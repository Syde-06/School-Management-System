import java.util.*;

public abstract class Person {
    // Common attributes for all person types
    private int id;
    private String name;
    
    // Constructor with basic information
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Abstract method for displaying person details, to be implemented by subclasses
    public abstract String display();
}

class Student extends Person {
 private ArrayList<Subject> enrolledSubjects;
 
 public Student(int id, String name) {
     super(id, name); // Call the parent constructor
     this.enrolledSubjects = new ArrayList<>();
 }
 
 public ArrayList<Subject> getEnrolledSubjects() {
     return enrolledSubjects;
 }
 
 public void enrollSubject(Subject subject) {
     this.enrolledSubjects.add(subject);
 }
 
 // Implementation of the abstract method from Person
 @Override
 public String display() {
     return "👤 Student | ID: " + getId() + " | Name: " + getName();
 }
}

class Teacher extends Person {
    private String department;
    private ArrayList<Subject> assignedSubjects;
    
    public Teacher(int id, String name) {
        super(id, name); // Call the parent constructor
        this.assignedSubjects = new ArrayList<>();
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public ArrayList<Subject> getAssignedSubjects() {
        return assignedSubjects;
    }
    
    public void assignSubject(Subject subject) {
        this.assignedSubjects.add(subject);
    }
    
    // Implementation of the abstract method from Person
    @Override
    public String display() {
        return "👤 Teacher | ID: " + getId() + " | Name: " + getName();
    }
}

class Staff extends Person {
    private String position;
    public Staff(int id, String name) {
        super(id, name); // Call the parent constructor
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    // Implementation of the abstract method from Person
    @Override
    public String display() {
        return "👤 " + position + " | ID: " + getId() + " | Name: " + getName();
    }
}