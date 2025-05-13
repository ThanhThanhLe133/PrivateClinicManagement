package Model;

import java.util.Date;
import java.util.UUID;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class DrugData {
    private String drugId, name, manufacturer, unit;
    private BigDecimal price;
    private int stock;
    private LocalDate expiry_date;
    private Timestamp create_date, update_date;
    
    public DrugData()
    {
    	this.drugId = UUID.randomUUID().toString();
    	this.create_date = Timestamp.valueOf(LocalDateTime.now());
    	this.update_date = Timestamp.valueOf(LocalDateTime.now());
    }
    
    public DrugData(String name, String manufacturer, String unit,
    	    BigDecimal price, int stock, LocalDate expiry_date)
    {
    	this.drugId = UUID.randomUUID().toString();
    	this.name = name;
    	this.manufacturer = manufacturer;
    	this.unit = unit;
    	this.price = price;
    	this.stock = stock;
    	this.expiry_date = expiry_date;
<<<<<<< HEAD
    	this.create_date = Timestamp.valueOf(LocalDateTime.now());
    	this.update_date = Timestamp.valueOf(LocalDateTime.now());
    }
=======
    	this.update_date = new Timestamp(System.currentTimeMillis());
		this.create_date = new Timestamp(System.currentTimeMillis());
    }
    
    public DrugData(String drugId, String name, String manufacturer, String unit,
		    BigDecimal price, int stock, LocalDate expiry_date, Timestamp create_date, Timestamp update_date)
	{
		this.id = drugId;
		this.name = name;
		this.manufacturer = manufacturer;
		this.unit = unit;
		this.price = price;
		this.stock = stock;
		this.expiry_date = expiry_date;
		this.create_date = create_date;
		this.update_date = update_date;
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
>>>>>>> ab7dbe2 (xong quản lý thuốc role recept)
    
    public DrugData(String drugId, String name, String manufacturer, String unit,
    	    BigDecimal price, int stock, LocalDate expiry_date,
    	    Timestamp create_date, Timestamp update_date)
    {
    	this.drugId = drugId;
    	this.name = name;
    	this.manufacturer = manufacturer;
    	this.unit = unit;
    	this.price = price;
    	this.stock = stock;
    	this.expiry_date = expiry_date;
    	this.create_date = create_date;
    	this.update_date = update_date;
    }
    
    public String getDrugId() { return drugId; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public String getUnit() { return unit; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public LocalDate getExpiryDate() { return expiry_date; }
    public Timestamp getCreateDate() { return create_date; }
    public Timestamp getUpdateDate() { return update_date; }
}