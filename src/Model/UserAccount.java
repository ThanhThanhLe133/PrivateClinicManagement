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
public class UserAccount {

	private String id; // CHAR(36)
	private String username; // VARCHAR(50)
	private String password; // VARCHAR(50)
	private String name; // VARCHAR(50)
	private String avatar; // TEXT
	private String email; // VARCHAR(50)
	private String gender; // VARCHAR(10)
	private Boolean isActive; // BOOLEAN
	private Timestamp createDate; // TIMESTAMP
	private Timestamp updateDate; // TIMESTAMP

	public UserAccount(String id, String username, String password, String name, String avatar, String email,
			String gender, Boolean isActive, Timestamp createDate, Timestamp updateDate) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.avatar = avatar;
		this.email = email;
		this.gender = gender;
		this.isActive = isActive;
		this.createDate = createDate;
		this.updateDate = updateDate;
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

	public String getAvatar() {
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
