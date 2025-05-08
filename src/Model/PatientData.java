/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.sql.Date;

/**
 *
 * @author WINDOWS 10
 */
public class PatientData {
    

    private String patientId;                // CHAR(36) PRIMARY KEY
    private String name;             // VARCHAR(50) NOT NULL
    private String email;            // VARCHAR(50) NOT NULL
    private String gender;           // VARCHAR(10) NOT NULL
    private String phone;            // VARCHAR(50) NOT NULL
    private String address;          // TEXT
    private String diagnosis;        // TEXT
    private BigDecimal height;       // DECIMAL
    private BigDecimal weight;       // DECIMAL
    private Timestamp createDate;    // DATETIME
    private Timestamp updateDate;    // DATETIME
    
    public String getPatientId() {
    	return patientId;
    }

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}
	
	public PatientData()
    {
    	this.patientId = UUID.randomUUID().toString();
    	this.createDate = Timestamp.valueOf(LocalDateTime.now());
    	this.updateDate = Timestamp.valueOf(LocalDateTime.now());
    }
    
    public PatientData(String name, String email, String gender, String phone, String address,
			String diagnosis, BigDecimal height, BigDecimal weight)
    {
    	this.patientId = UUID.randomUUID().toString();
    	this.name = name;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.diagnosis = diagnosis;
		this.height = height;
		this.weight = weight;
    	this.createDate = Timestamp.valueOf(LocalDateTime.now());
    	this.updateDate = Timestamp.valueOf(LocalDateTime.now());
    }

	public PatientData(String patientId, String name, String email, String gender, String phone, String address,
			String diagnosis, BigDecimal height, BigDecimal weight, Timestamp createDate, Timestamp updateDate) {
		this.patientId = patientId;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.diagnosis = diagnosis;
		this.height = height;
		this.weight = weight;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}   
}
