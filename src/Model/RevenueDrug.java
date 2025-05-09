/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

import Enum.AppointmentStatus;

/**
 *
 * @author WINDOWS 10
 */
public class RevenueDrug {
	private String drugName;
	private int quantity;
	private BigDecimal price;
	private BigDecimal revenue;

	public RevenueDrug(String drugName, int quantity, BigDecimal price, BigDecimal revenue) {
		super();
		this.drugName = drugName;
		this.quantity = quantity;
		this.price = price;
		this.revenue = revenue;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public String getFormattedPrice() {
		return formatCurrencyVND(price);
	}

	public String getFormattedTotalRevenue() {
		return formatCurrencyVND(revenue);
	}

	public static String formatCurrencyVND(BigDecimal amount) {
		if (amount == null)
			return "0 VNĐ";
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		return formatter.format(amount) + " VNĐ";
	}
}
