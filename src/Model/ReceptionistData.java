// ReceptionistFullData.java
package Model;

import java.sql.Timestamp;

public class ReceptionistData extends UserAccount {
	private String phone;
	private String address;
	private boolean isConfirmed;

	public ReceptionistData() {
		super();
	}

	public ReceptionistData(String id, String username, String password, String name, String email,
			String gender, Boolean isActive, String phone, 
			String address, boolean isConfirmed) {
		super(id, username, password, name,email, gender, isActive);
		this.phone = phone;
		this.address = address;
		this.isConfirmed = isConfirmed;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean confirmed) {
		isConfirmed = confirmed;
	}
}