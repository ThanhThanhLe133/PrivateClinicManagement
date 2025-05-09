/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import Enum.AppointmentStatus;

/**
 *
 * @author WINDOWS 10
 */
public class AppointmentData {

	private String id;
	private Timestamp time;
	private String status;
	private String cancelReason;
	private String doctorId;
	private String patientId;
	private String serviceId;
	private Timestamp createDate;
	private Timestamp updateDate;

	public AppointmentData() {
		this.id = UUID.randomUUID().toString();
		this.time = new Timestamp(System.currentTimeMillis());
		this.status = AppointmentStatus.Coming.name();
		this.cancelReason = "";
		this.doctorId = "";
		this.patientId = "";
		this.serviceId = "";
	}

	public AppointmentData(String id, Timestamp time, String status, String cancelReason, String doctorId,
			String patientId, String serviceId) {
		this.id = id;
		this.time = time;
		this.status = status;
		this.cancelReason = cancelReason;
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.serviceId = serviceId;
	}

	// Getter và Setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
