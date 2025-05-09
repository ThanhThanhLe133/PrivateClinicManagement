package Model;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import Enum.ServiceType;

import java.sql.Timestamp;
import java.text.NumberFormat;

public class ServiceData {
    private String serviceId;
    private String name;
    private String type;
    private BigDecimal price;

    public ServiceData() {
    	 this.serviceId = UUID.randomUUID().toString();
         this.name = "";
         this.price = BigDecimal.ZERO;
         this.type=ServiceType.Examination.name();
    }

    public ServiceData(String serviceId, String name, BigDecimal price,String type) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
        this.type=type;
    }

    // Getter và Setter
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
