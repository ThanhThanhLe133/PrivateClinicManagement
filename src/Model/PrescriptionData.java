/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import Enum.AppointmentStatus;

/**
 *
 * @author WINDOWS 10
 */
public class PrescriptionData {

	private String id;
	private String doctorId;
	private String patientId;
	private String diagnose;
	private String advice;
	private BigDecimal totalAmount;
	private Timestamp createDate;
	private Timestamp updateDate;

	public PrescriptionData() {
		this.id = UUID.randomUUID().toString();
		this.doctorId = "";
		this.patientId = "";
		this.diagnose = "";
		this.advice = "";
		this.totalAmount = BigDecimal.ZERO;
	}


	public PrescriptionData(String id, String doctorId, String patientId, String diagnose, String advice,
			BigDecimal totalAmount, Timestamp createDate) {
		this.id = id;
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.diagnose = diagnose;
		this.advice = advice;
		this.totalAmount = totalAmount;
		this.createDate = createDate;
	}


	// Getter và Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	

}
