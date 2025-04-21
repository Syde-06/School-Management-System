import java.util.*;  // Import ArrayList and other utility classes

// Service class to manage Student objects and their actions
class StudentService {
    // List that holds all students
    private ArrayList<Student> students;
    
    // Constructor: create a new empty list when the service starts
    public StudentService() {
        this.students = new ArrayList<>();  // Initialize the list
    }
    
    // Add a new student to our list
    public void addStudent(Student student) {
        students.add(student);  // Put the student into the list
    }
    
    // Find a student by their ID number
    public Student findById(int id) {
        // Look through each student in the list
        for (Student student : students) {
            // If the ID matches, return this student
            if (student.getId() == id) {
                return student;
            }
        }
        // If we finish looping without finding them, return null
        return null;
    }
    
    // Return the full list of students
    public ArrayList<Student> getAllStudents() {
        return students;
    }
    
    // Enroll a student in a subject by updating the Student object
    public void enrollInSubject(Student student, Subject subject) {
        student.enrollSubject(subject);  // Call the student's method to add the subject
    }
}

// Service class to manage Teacher objects and their actions
class TeacherService {
    // List that holds all teachers
    private ArrayList<Teacher> teachers;
    
    // Constructor: create a new empty list when the service starts
    public TeacherService() {
        this.teachers = new ArrayList<>();
    }
    
    // Add a new teacher to our list
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    
    // Find a teacher by their ID number
    public Teacher findById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                return teacher;
            }
        }
        return null;
    }
    
    // Return the full list of teachers
    public ArrayList<Teacher> getAllTeachers() {
        return teachers;
    }
    
    // Assign a subject to a teacher by updating the Teacher object
    public void assignSubject(Teacher teacher, Subject subject) {
        teacher.assignSubject(subject);  // Call the teacher's method to add the subject
    }
}

// Service class to manage Staff objects and their actions
class StaffService {
    // List that holds all staff members
    private ArrayList<Staff> staffMembers;
    
    // Constructor: create a new empty list when the service starts
    public StaffService() {
        this.staffMembers = new ArrayList<>();
    }
    
    // Add a new staff member to our list
    public void addStaff(Staff staff) {
        staffMembers.add(staff);
    }
    
    // Find a staff member by their ID number
    public Staff findById(int id) {
        for (Staff staff : staffMembers) {
            if (staff.getId() == id) {
                return staff;
            }
        }
        return null;
    }
    
    // Return the full list of staff members
    public ArrayList<Staff> getAllStaff() {
        return staffMembers;
    }
}

// Service class to manage Subject objects and their actions
class SubjectService {
    // List that holds all subjects
    private ArrayList<Subject> subjects;
    
    // Constructor: create a new empty list when the service starts
    public SubjectService() {
        this.subjects = new ArrayList<>();
    }
    
    // Add a new subject to our list
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }
    
    // Find a subject by its unique code (like "MATH101")
    public Subject findByCode(String code) {
        for (Subject subject : subjects) {
            // If the code matches, return this subject
            if (subject.getCode().equals(code)) {
                return subject;
            }
        }
        // If nothing matches, return null
        return null;
    }
    
    // Return the full list of subjects
    public ArrayList<Subject> getAllSubjects() {
        return subjects;
    }
}
