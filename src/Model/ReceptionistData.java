/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.security.Timestamp;

/**
 *
 * @author WINDOWS 10
 */
public class ReceptionistData {

	private String receptionistId; // CHAR(36)
	private String phone; // VARCHAR(50)
	private String address; // TEXT
	
	public ReceptionistData(String receptionistId, String phone, String address) {
		super();
		this.receptionistId = receptionistId;
		this.phone = phone;
		this.address = address;
	}
	public String getReceptionistId() {
		return receptionistId;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddress() {
		return address;
	}

	

}
