/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author WINDOWS 10
 */
public abstract class UserAccount {

	protected String id; // CHAR(36)
	protected String username; // VARCHAR(50)
	protected String password; // VARCHAR(50)
	protected String name; // VARCHAR(50)
	protected byte[] avatar;
	protected String email; // VARCHAR(50)
	protected String gender; // VARCHAR(10)
	protected Boolean isActive; // BOOLEAN
	protected Timestamp createDate; // TIMESTAMP
	protected Timestamp updateDate; // TIMESTAMP

	public UserAccount(String id, String username, String password, String name, String email,
			String gender, Boolean isActive) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.isActive = isActive;
	}

	public UserAccount() {
		this.id = UUID.randomUUID().toString();
		this.username = "";
		this.password = "";
		this.name = "";
		this.email = "";
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

}
