This is a simple Java program that implements a university management system. The system is structured using object-oriented programming principles with four main classes working together.
Overall architecture:
* The Main class serves as the entry point with the main method that runs the program.
* ComplicatedCaluculations (which has a typo in its name) contains most of the business logic and user interface code.
* Person is a flexible class that represents students, teachers, and staff using constructor overloading.
* Subject represents academic courses offered by the university.

The program works as a console application with a menu-driven interface that lets users perform several operations:

* Add people (students, teachers, staff) to the system
* Enroll students in subjects
* Assign subjects to teachers
* View lists of people and their associated subjects

The data model is pretty straightforward. We use ArrayLists to store collections of Person and Subject objects. For students, we track which subjects they're enrolled in, and for teachers, we track which subjects they're assigned to teach.
The application follows a common pattern in console applications where a menu is displayed, the user selects an option, the corresponding function is called, and then the menu is displayed again. This continues until the user chooses to exit.

System Features

✅ Add a Person - Implemented in the addPerson() method in Main.
✅ Enroll a Student - Implemented in the enrollStudentToSubject() method.
✅ Assign Subject to Teacher - Implemented in the assignSubjectToTeacher() method.
✅ Display All People - Implemented in the displayAllPeople() method.
✅ Display Student Subjects - Implemented in the displayStudentSubjects() method.
✅ Display Teacher Subjects - Implemented in the displayTeacherSubjects() method.
✅ Menu for User Interaction - Implemented in the displayMenu() method and main loop.

OOP Concepts

✅ Encapsulation - All data members are private and accessed through getters/setters.
✅ Inheritance - Created a base Person class with Student, Teacher, and Staff subclasses.
✅ Polymorphism - Implemented through the abstract display() method in the Person class, which is overridden differently in each subclass.
✅ Abstraction - Created separate service classes to handle specific business logic.

Additional Requirements

✅ Unique ID Numbers - The system checks if an ID already exists before adding a new person.
✅ Valid Student ID for Enrollment - The system verifies that a student exists before enrolling them in a subject.
✅ Valid Teacher ID for Assignment - The system verifies that a teacher exists before assigning a subject to them.

