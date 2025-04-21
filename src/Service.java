import java.util.*;

class StudentService {
    private ArrayList<Student> students;
    
    public StudentService() {
        this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public Student findById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
    
    public ArrayList<Student> getAllStudents() {
        return students;
    }
    
    public void enrollInSubject(Student student, Subject subject) {
        student.enrollSubject(subject);
    }
}

// Teacher Service class for handling teacher-related operations
class TeacherService {
    private ArrayList<Teacher> teachers;
    
    public TeacherService() {
        this.teachers = new ArrayList<>();
    }
    
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    
    public Teacher findById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                return teacher;
            }
        }
        return null;
    }
    
    public ArrayList<Teacher> getAllTeachers() {
        return teachers;
    }
    
    public void assignSubject(Teacher teacher, Subject subject) {
        teacher.assignSubject(subject);
    }
}

// Staff Service class for handling staff-related operations
class StaffService {
    private ArrayList<Staff> staffMembers;
    
    public StaffService() {
        this.staffMembers = new ArrayList<>();
    }
    
    public void addStaff(Staff staff) {
        staffMembers.add(staff);
    }
    
    public Staff findById(int id) {
        for (Staff staff : staffMembers) {
            if (staff.getId() == id) {
                return staff;
            }
        }
        return null;
    }
    
    public ArrayList<Staff> getAllStaff() {
        return staffMembers;
    }
}

// Subject Service class for handling subject-related operations
class SubjectService {
    private ArrayList<Subject> subjects;
    
    public SubjectService() {
        this.subjects = new ArrayList<>();
    }
    
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }
    
    public Subject findByCode(String code) {
        for (Subject subject : subjects) {
            if (subject.getCode().equals(code)) {
                return subject;
            }
        }
        return null;
    }
    
    public ArrayList<Subject> getAllSubjects() {
        return subjects;
    }
}