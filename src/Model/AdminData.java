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
public class AdminData {

	private String adminId; // CHAR(36)

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public AdminData(String adminId) {
		super();
		this.adminId = adminId;
	}


}
