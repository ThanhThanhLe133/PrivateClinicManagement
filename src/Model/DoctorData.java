/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.security.Timestamp;
import java.sql.Date;

/**
 *
 * @author WINDOWS 10
 */
public class DoctorData {
    private String doctorId, username, name, gender, phone, email, specialized, address, password;

    public DoctorData(String doctorId, String username, String name, String gender, String phone, String email,
                      String specialized, String address, String password) {
        this.doctorId = doctorId;
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.specialized = specialized;
        this.address = address;
        this.password = password;
    }

    public String getDoctorId() { return doctorId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getSpecialized() { return specialized; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
}
