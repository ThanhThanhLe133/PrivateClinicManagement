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

	private String doctorId; // CHAR(36) PRIMARY KEY
	private String phone; // VARCHAR(50)
	private String specialized; // TEXT
	private String address; // TEXT
	public DoctorData(String doctorId, String phone, String specialized, String address) {
		super();
		this.doctorId = doctorId;
		this.phone = phone;
		this.specialized = specialized;
		this.address = address;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public String getPhone() {
		return phone;
	}
	public String getSpecialized() {
		return specialized;
	}
	public String getAddress() {
		return address;
	}

	

//	public DoctorData(Integer id, String doctorID, String fullName, String specialized, String email, String image) {
//		this.id = id;
//		this.doctorID = doctorID;
//		this.fullName = fullName;
//		this.specialized = specialized;
//		this.email = email;
//		this.image = image;
//	}



}
