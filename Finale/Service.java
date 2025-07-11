import java.sql.*;
import java.util.*;

class StudentService {
    private Connection connection;

    public StudentService() {
        this.connection = DatabaseConfig.getConnection();
    }

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO student_info (student_id, first_name, last_name, middle_name, course, year_level, email, contact_number, address, profile_picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getMiddleName());
            stmt.setString(5, student.getCourse());
            stmt.setInt(6, student.getYearLevel());
            stmt.setString(7, student.getEmail());
            stmt.setString(8, student.getContactNumber());
            stmt.setString(9, student.getAddress());
            stmt.setString(10, student.getProfilePicture());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE student_info SET first_name = ?, last_name = ?, middle_name = ?, course = ?, year_level = ?, email = ?, contact_number = ?, address = ?, profile_picture = ? WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getMiddleName());
            stmt.setString(4, student.getCourse());
            stmt.setInt(5, student.getYearLevel());
            stmt.setString(6, student.getEmail());
            stmt.setString(7, student.getContactNumber());
            stmt.setString(8, student.getAddress());
            stmt.setString(9, student.getProfilePicture());
            stmt.setString(10, student.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String studentId) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            String deleteEnrollmentsSql = "DELETE FROM student_class WHERE student_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteEnrollmentsSql)) {
                stmt.setString(1, studentId);
                stmt.executeUpdate();
            }

            String deleteStudentSql = "DELETE FROM student_info WHERE student_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteStudentSql)) {
                stmt.setString(1, studentId);
                int result = stmt.executeUpdate();
                
                if (result > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Student findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT * FROM student_info WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                student.setMiddleName(rs.getString("middle_name"));
                student.setCourse(rs.getString("course"));
                student.setYearLevel(rs.getInt("year_level"));
                student.setEmail(rs.getString("email"));
                student.setContactNumber(rs.getString("contact_number"));
                student.setAddress(rs.getString("address"));
                student.setProfilePicture(rs.getString("profile_picture"));

                loadStudentSubjects(student);
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student_info ORDER BY student_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                student.setMiddleName(rs.getString("middle_name"));
                student.setCourse(rs.getString("course"));
                student.setYearLevel(rs.getInt("year_level"));
                student.setEmail(rs.getString("email"));
                student.setContactNumber(rs.getString("contact_number"));
                student.setAddress(rs.getString("address"));
                student.setProfilePicture(rs.getString("profile_picture"));

                loadStudentSubjects(student);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private void loadStudentSubjects(Student student) {
        String sql = "SELECT s.* FROM student_class sc JOIN subject s ON sc.subject = s.code WHERE sc.student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Subject subject = new Subject(
                    rs.getString("code"), 
                    rs.getString("name"), 
                    rs.getInt("units")
                );
                student.enrollSubject(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean enrollInSubject(Student student, Subject subject) {
        if (student == null || subject == null) {
            return false;
        }
        
        String checkSql = "SELECT COUNT(*) FROM student_class WHERE student_id = ? AND subject = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, student.getId());
            checkStmt.setString(2, subject.getCode());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        String sql = "INSERT INTO student_class (student_id, subject) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getId());
            stmt.setString(2, subject.getCode());

            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                student.enrollSubject(subject);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

class TeacherService {
    private Connection connection;

    public TeacherService() {
        this.connection = DatabaseConfig.getConnection();
    }

    public boolean addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teacher (teacher_id, employee_number, first_name, last_name, department, email, contact_number, specialization) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getId());
            stmt.setString(2, teacher.getEmployeeNumber());
            stmt.setString(3, teacher.getFirstName());
            stmt.setString(4, teacher.getLastName());
            stmt.setString(5, teacher.getDepartment());
            stmt.setString(6, teacher.getEmail());
            stmt.setString(7, teacher.getContactNumber());
            stmt.setString(8, teacher.getSpecialization());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Teacher findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT * FROM teacher WHERE teacher_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Teacher teacher = new Teacher(
                    rs.getString("teacher_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                teacher.setEmployeeNumber(rs.getString("employee_number"));
                teacher.setDepartment(rs.getString("department"));
                teacher.setEmail(rs.getString("email"));
                teacher.setContactNumber(rs.getString("contact_number"));
                teacher.setSpecialization(rs.getString("specialization"));

                loadTeacherSubjects(teacher);
                return teacher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Teacher> getAllTeachers() {
        ArrayList<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teacher ORDER BY teacher_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Teacher teacher = new Teacher(
                    rs.getString("teacher_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                teacher.setEmployeeNumber(rs.getString("employee_number"));
                teacher.setDepartment(rs.getString("department"));
                teacher.setEmail(rs.getString("email"));
                teacher.setContactNumber(rs.getString("contact_number"));
                teacher.setSpecialization(rs.getString("specialization"));

                loadTeacherSubjects(teacher);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    private void loadTeacherSubjects(Teacher teacher) {
        String sql = "SELECT s.* FROM class c JOIN subject s ON c.subject = s.code WHERE c.teacher_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Subject subject = new Subject(
                    rs.getString("code"), 
                    rs.getString("name"), 
                    rs.getInt("units")
                );
                teacher.assignSubject(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean assignSubject(Teacher teacher, Subject subject) {
        if (teacher == null || subject == null) {
            return false;
        }
        
        String checkSql = "SELECT COUNT(*) FROM class WHERE teacher_id = ? AND subject = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, teacher.getId());
            checkStmt.setString(2, subject.getCode());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        String sql = "INSERT INTO class (teacher_id, subject) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getId());
            stmt.setString(2, subject.getCode());

            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                teacher.assignSubject(subject);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

class StaffService {
    private Connection connection;

    public StaffService() {
        this.connection = DatabaseConfig.getConnection();
    }

    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (staff_id, employee_number, first_name, last_name, position, office, email, contact_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, staff.getId());
            stmt.setString(2, staff.getEmployeeNumber());
            stmt.setString(3, staff.getFirstName());
            stmt.setString(4, staff.getLastName());
            stmt.setString(5, staff.getPosition());
            stmt.setString(6, staff.getOffice());
            stmt.setString(7, staff.getEmail());
            stmt.setString(8, staff.getContactNumber());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Staff findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT * FROM staff WHERE staff_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Staff staff = new Staff(
                    rs.getString("staff_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                staff.setEmployeeNumber(rs.getString("employee_number"));
                staff.setPosition(rs.getString("position"));
                staff.setOffice(rs.getString("office"));
                staff.setEmail(rs.getString("email"));
                staff.setContactNumber(rs.getString("contact_number"));

                return staff;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> staffMembers = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY staff_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Staff staff = new Staff(
                    rs.getString("staff_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                staff.setEmployeeNumber(rs.getString("employee_number"));
                staff.setPosition(rs.getString("position"));
                staff.setOffice(rs.getString("office"));
                staff.setEmail(rs.getString("email"));
                staff.setContactNumber(rs.getString("contact_number"));

                staffMembers.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffMembers;
    }
}

class SubjectService {
    private Connection connection;
    private ArrayList<Subject> subjects;

    public SubjectService() {
        this.connection = DatabaseConfig.getConnection();
        this.subjects = new ArrayList<>();
        loadPredefinedSubjects();
    }

    private void loadPredefinedSubjects() {
        // Create a temporary subject table in memory
        subjects.add(new Subject("MATH101", "Basic Mathematics", 3));
        subjects.add(new Subject("ENG101", "English Composition", 3));
        subjects.add(new Subject("COMP101", "Introduction to Computing", 4));
        subjects.add(new Subject("SCI101", "General Science", 4));
        subjects.add(new Subject("HIST101", "World History", 3));
        subjects.add(new Subject("PE101", "Physical Education", 2));
        subjects.add(new Subject("ART101", "Art Appreciation", 3));
        subjects.add(new Subject("MUS101", "Music Appreciation", 3));
    }

    public void addSubject(Subject subject) {
        if (subject != null && !subjects.contains(subject)) {
            subjects.add(subject);
        }
    }

    public Subject findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        
        for (Subject subject : subjects) {
            if (subject.getCode().equals(code)) {
                return subject;
            }
        }
        return null;
    }

    public ArrayList<Subject> getAllSubjects() {
        return new ArrayList<>(subjects); // Return a copy to prevent external modification
    }
}
