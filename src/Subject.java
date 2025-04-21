public class Subject {
    // Unique code for the subject
    private String code;
    // Descriptive name of the subject
    private String name;
    // Number of units/credits the subject is worth
    private int units;
    
    // Constructor to create a new Subject with its code, name, and unit count
    public Subject(String code, String name, int units) {
        this.code = code;   // Save the code
        this.name = name;   // Save the name
        this.units = units; // Save the number of units
    }
    
    // Getter for the subject code
    public String getCode() {
        return code;
    }
    
    // Setter to update the subject code later if needed
    public void setCode(String code) {
        this.code = code;
    }
    
    // Getter for the subject name
    public String getName() {
        return name;
    }
    
    // Setter to update the subject name
    public void setName(String name) {
        this.name = name;
    }
    
    // Getter for how many units the subject has
    public int getUnits() {
        return units;
    }
    
    // Setter to change the unit count
    public void setUnits(int units) {
        this.units = units;
    }
    
    // toString() defines how to display the Subject as text
    @Override
    public String toString() {
        // Show an icon, the code, name, and unit count in a friendly format
        return "📖 " + code + " - " + name + " (" + units + " units)";
    }
}
