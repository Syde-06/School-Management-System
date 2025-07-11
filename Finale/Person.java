import java.util.*;

public abstract class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String profilePicture;

    public Person(String id, String firstName, String lastName) {
        this.id = id != null ? id : "";
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id != null ? id : ""; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName != null ? firstName : ""; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName != null ? lastName : ""; }

    public String getFullName() { 
        String first = firstName != null ? firstName : "";
        String last = lastName != null ? lastName : "";
        return (first + " " + last).trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public abstract String display();
}

class Student extends Person {
    private String middleName;
    private String course;
    private int yearLevel;
    private String address;
    private ArrayList<Subject> enrolledSubjects;

    public Student(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
        this.enrolledSubjects = new ArrayList<>();
        this.yearLevel = 1; // Default year level
    }

    // Getters and setters
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getCourse() { return course != null ? course : ""; }
    public void setCourse(String course) { this.course = course; }

    public int getYearLevel() { return yearLevel; }
    public void setYearLevel(int yearLevel) { 
        this.yearLevel = yearLevel > 0 && yearLevel <= 4 ? yearLevel : 1; 
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public ArrayList<Subject> getEnrolledSubjects() { 
        return new ArrayList<>(enrolledSubjects); // Return a copy
    }
    
    public void enrollSubject(Subject subject) { 
        if (subject != null && !enrolledSubjects.contains(subject)) {
            this.enrolledSubjects.add(subject); 
        }
    }

    @Override
    public String display() {
        return "👨‍🎓 Student | ID: " + getId() + " | Name: " + getFullName();
    }
}

class Teacher extends Person {
    private String employeeNumber;
    private String department;
    private String specialization;
    private ArrayList<Subject> assignedSubjects;

    public Teacher(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
        this.assignedSubjects = new ArrayList<>();
    }

    // Getters and setters
    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getDepartment() { return department != null ? department : ""; }
    public void setDepartment(String department) { this.department = department; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public ArrayList<Subject> getAssignedSubjects() { 
        return new ArrayList<>(assignedSubjects); // Return a copy
    }
    
    public void assignSubject(Subject subject) { 
        if (subject != null && !assignedSubjects.contains(subject)) {
            this.assignedSubjects.add(subject); 
        }
    }

    @Override
    public String display() {
        return "👨‍🏫 Teacher | ID: " + getId() + " | Name: " + getFullName();
    }
}

class Staff extends Person {
    private String employeeNumber;
    private String position;
    private String office;

    public Staff(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    // Getters and setters
    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getPosition() { return position != null ? position : ""; }
    public void setPosition(String position) { this.position = position; }

    public String getOffice() { return office; }
    public void setOffice(String office) { this.office = office; }

    @Override
    public String display() {
        return "👥 " + getPosition() + " | ID: " + getId() + " | Name: " + getFullName();
    }
}
