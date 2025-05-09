package Model;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class DrugData {
    private String id, name, manufacturer, unit;
    private BigDecimal price;
    private int stock;
    private LocalDate expiry_date;
    private Timestamp create_date, update_date;
    
    
    public DrugData()
    {
    	this.id = UUID.randomUUID().toString();
    	this.name = "";
    	this.manufacturer = "";
    	this.unit = "";
    	this.price = BigDecimal.ZERO;
    	this.stock = 0;
    	this.expiry_date = LocalDate.now();
    }
    
    public DrugData(String drugId, String name, String manufacturer, String unit,
    	    BigDecimal price, int stock, LocalDate expiry_date)
    {
    	this.id = drugId;
    	this.name = name;
    	this.manufacturer = manufacturer;
    	this.unit = unit;
    	this.price = price;
    	this.stock = stock;
    	this.expiry_date = expiry_date;
    }
    public String getFormattedPrice() {
		return formatCurrencyVND(price);
	}


	public static String formatCurrencyVND(BigDecimal amount) {
		if (amount == null)
			return "0 VNĐ";
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		return formatter.format(amount) + " VNĐ";
	}
    
    public String getDrugId() { return id; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public String getUnit() { return unit; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public LocalDate getExpiryDate() { return expiry_date; }
    public Timestamp getCreateDate() { return create_date; }
    public Timestamp getUpdateDate() { return update_date; }
}