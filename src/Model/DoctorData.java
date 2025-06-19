package Model;

import java.sql.Timestamp;

public class DoctorData extends UserAccount {
	// DOCTOR
	private String phone;
	private String serviceName;
	private String address;
	private boolean isConfirmed;
	private boolean status;

	public DoctorData() {
		super();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public DoctorData(String id, String username, String password, String name, String email,
			String gender, Boolean isActive, String phone, String serviceName,
			String address, boolean isConfirmed) {
		super(id, username, password, name, email, gender, isActive);
		this.phone = phone;
		this.serviceName = serviceName;
		this.address = address;
		this.isConfirmed = isConfirmed;
	}

	// Getter & Setter cho DOCTOR
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAddress() {
		return address;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void setName(String name) {
		this.name = name;
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

	public boolean getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	public void setEmail(String trim) {
		this.email = trim;
	}

	public void setGender(String value) {
		this.gender = value;
	}
}
