package Model;

public class DoctorFullData {
    // USER_ACCOUNT
    private String doctorId;
    private String username;
    private String name;
    private String email;
    private String gender;
    private String password;

    // DOCTOR
    private String phone;
    private String specialized;
    private String address;
    private boolean isConfirmed;

    public DoctorFullData() {}

    public DoctorFullData(String doctorId, String username, String name, String email, String gender, String password,
                          String phone, String specialized, String address, boolean isConfirmed) {
        this.doctorId = doctorId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.phone = phone;
        this.specialized = specialized;
        this.address = address;
        this.isConfirmed = isConfirmed;
    }

    // Getter & Setter cho USER_ACCOUNT
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Getter & Setter cho DOCTOR
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialized() { return specialized; }
    public void setSpecialized(String specialized) { this.specialized = specialized; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isConfirmed() { return isConfirmed; }
    public void setConfirmed(boolean confirmed) { isConfirmed = confirmed; }
}
