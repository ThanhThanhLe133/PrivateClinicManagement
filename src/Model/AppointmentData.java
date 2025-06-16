/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
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
	private String doctorName;
	private String patientId;
	private String patientName;
	private String serviceId;
	private String serviceName;
	private String contactNumber;
	private String prescriptionStatus;
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
	
	public AppointmentData(String id, Timestamp time, String status, String doctorId,
			String patientId) {
		this.id = id;
		this.time = time;
		this.status = status;
		this.doctorId = doctorId;
		this.patientId = patientId;
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

	public AppointmentData(String id, Timestamp time, String status, String cancelReason, String doctorId,
			String patientId, String serviceId, String serviceName, String prescriptionStatus, Timestamp createdDate, Timestamp updateDate,
			String patientName, String doctorName,String contactNumber) {
		this.id = id;
		this.time = time;
		this.status = status;
		this.cancelReason = cancelReason;
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.serviceId = serviceId;
		this.prescriptionStatus = prescriptionStatus;
		this.createDate = createdDate;
		this.updateDate = updateDate;
		this.setServiceName(serviceName);
		this.setPatientName(patientName);
		this.setDoctorName(doctorName);
		this.setContactNumber(contactNumber);
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

	public String getPrescriptionStatus() {
		return prescriptionStatus;
	}

	public void setPrescriptionStatus(String prescriptionStatus) {
		this.prescriptionStatus = prescriptionStatus;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public LocalDate getLocalDate() {
	    return time != null ? time.toLocalDateTime().toLocalDate() : null;
	}

	public LocalTime getLocalTime() {
	    return time != null ? time.toLocalDateTime().toLocalTime() : null;
	}

}
