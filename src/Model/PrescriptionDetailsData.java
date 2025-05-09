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
public class PrescriptionDetailsData {

	private String prescriptionId;
	private String drugId;
	private int quantity;
	private String instructions;
	private Timestamp createDate;
	private Timestamp updateDate;

	public PrescriptionDetailsData() {
		this.prescriptionId = "";
		this.drugId = "";
		this.quantity = 0;
		this.instructions = "";
	}

	public PrescriptionDetailsData(String prescriptionId, String drugId, int quantity, String instructions) {
		this.prescriptionId = prescriptionId;
		this.drugId = drugId;
		this.quantity = quantity;
		this.instructions = instructions;
	}

	public String getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
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
