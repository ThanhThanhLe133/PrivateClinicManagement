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
    

    private String patientId;              
    private String name;            
    private String email;          
    private String gender;           
    private String phone;          
    private String address;        
    private String diagnosis;       
    private BigDecimal height;     
    private BigDecimal weight;      
    private Timestamp createDate;   
    private Timestamp updateDate;   
    
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
    	this.name = "";
		this.email = "";
		this.gender = "";
		this.phone = "";
		this.address = "";
		this.diagnosis = "";
		this.height = BigDecimal.ZERO;
		this.weight = BigDecimal.ZERO;
    }

	public PatientData(String patientId, String name, String email, String gender, String phone, String address,
			String diagnosis, BigDecimal height, BigDecimal weight) {
		this.patientId = patientId;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.diagnosis = diagnosis;
		this.height = height;
		this.weight = weight;
		this.createDate = new Timestamp(System.currentTimeMillis());
		this.updateDate = new Timestamp(System.currentTimeMillis());
	}   
	
	public PatientData(String patientId, String name, String email, String gender, String phone, String address,
			String diagnosis, BigDecimal height, BigDecimal weight, Timestamp createDate, Timestamp updateDate) 
	{
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

