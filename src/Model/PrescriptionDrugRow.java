package Model;

import javafx.beans.property.*;

public class PrescriptionDrugRow {
    private final StringProperty drugId = new SimpleStringProperty();
    private final StringProperty drugName = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final StringProperty instructions = new SimpleStringProperty();

    public PrescriptionDrugRow(String drugId, String drugName, int quantity, String instructions) {
        this.drugId.set(drugId);
        this.drugName.set(drugName);
        this.quantity.set(quantity);
        this.instructions.set(instructions);
    }

    public String getDrugId() {
        return drugId.get();
    }
    
    public String getDrugName() {
		return drugName.get();
	}

    public int getQuantity() {
        return quantity.get();
    }

    public String getInstructions() {
        return instructions.get();
    }

    public StringProperty drugIdProperty() {
        return drugId;
    }
    
    public StringProperty drugNameProperty() {
		return drugName;
	}

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public StringProperty instructionsProperty() {
        return instructions;
    }
}
