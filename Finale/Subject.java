public class Subject {
    private String code;
    private String name;
    private int units;
    
    public Subject(String code, String name, int units) {
        this.code = code != null ? code : "";
        this.name = name != null ? name : "";
        this.units = units > 0 ? units : 1;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code != null ? code : "";
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name != null ? name : "";
    }
    
    public int getUnits() {
        return units;
    }
    
    public void setUnits(int units) {
        this.units = units > 0 ? units : 1;
    }
    
    @Override
    public String toString() {
        return "📖 " + code + " - " + name + " (" + units + " units)";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return code.equals(subject.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
