package therapyapp.model;

public class Therapist {
    private String therapistId;
    private String name;
    private String specialty;
    private String pincode;

    public Therapist() {}

    public Therapist(String therapistId, String name, String specialty, String pincode) {
        this.therapistId = therapistId;
        this.name = name;
        this.specialty = specialty;
        this.pincode = pincode;
    }

    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}
