// This defines a class (blueprint) for Subject objects
public class Subject {
    // These are instance variables (data fields) for each Subject object
    private String code;      // Stores the subject code (e.g., "MATH101")
    private String name;      // Stores the subject name (e.g., "Basic Mathematics")
    private int units;        // Stores the credit units for the subject (e.g., 3)
    
    // This is a constructor method that creates a new Subject object
    // with the specified code, name, and units
    public Subject(String code, String name, int units) {
        this.code = code;     // "this.code" refers to the instance variable, "code" is the parameter
        this.name = name;     // Assign the name parameter to the instance variable
        this.units = units;   // Assign the units parameter to the instance variable
    }
    
    // This is a "getter" method that allows other classes to access the subject code
    public String getCode() {
        return code;          // Return the value of the code instance variable
    }
    
    // This is a "setter" method that allows other classes to change the subject code
    public void setCode(String code) {
        this.code = code;     // Update the code instance variable with the new value
    }
    
    // Getter method for the subject name
    public String getName() {
        return name;          // Return the value of the name instance variable
    }
    
    // Setter method for the subject name
    public void setName(String name) {
        this.name = name;     // Update the name instance variable with the new value
    }
    
    // Getter method for the subject units
    public int getUnits() {
        return units;         // Return the value of the units instance variable
    }
    
    // Setter method for the subject units
    public void setUnits(int units) {
        this.units = units;   // Update the units instance variable with the new value
    }
    
    // This method overrides the default toString() method
    // It defines how a Subject object should be converted to a string
    @Override
    public String toString() {
        // Return a formatted string with emoji and all subject information
        return "📖 " + code + " - " + name + " (" + units + " units)";
    }
}
