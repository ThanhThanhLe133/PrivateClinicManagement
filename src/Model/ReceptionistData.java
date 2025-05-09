// ReceptionistFullData.java
package Model;

public class ReceptionistFullData {
    private String receptionistId, username, password, name, email, gender, phone, address;
    private boolean isConfirmed;

    public ReceptionistFullData(String receptionistId, String username, String password, String name,
                                 String email, String gender, String phone, String address, boolean isConfirmed) {
        this.receptionistId = receptionistId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.isConfirmed = isConfirmed;
    }

    public String getReceptionistId() { return receptionistId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isConfirmed() { return isConfirmed; }
}