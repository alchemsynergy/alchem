package Main.Helpers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Billing {
    private SimpleStringProperty billItem;
    private SimpleStringProperty billBatch;
    private SimpleIntegerProperty billQuantity;
    private SimpleStringProperty billFree;
    private SimpleFloatProperty billRate;
    private SimpleFloatProperty billAmount;
    private Boolean check;

    public Billing(String billItem, String billBatch, int billQuantity, String billFree, float billRate, float billAmount) {
        this.billItem = new SimpleStringProperty(billItem);
        this.billBatch = new SimpleStringProperty(billBatch);
        this.billQuantity = new SimpleIntegerProperty(billQuantity);
        this.billFree = new SimpleStringProperty(billFree);
        this.billRate = new SimpleFloatProperty(billRate);
        this.billAmount = new SimpleFloatProperty(billAmount);
    }

    public Billing(String billItem,String billBatch,int billQuantity,Boolean check)
    {
        System.out.println("");
        this.billItem = new SimpleStringProperty(billItem);
        this.billBatch = new SimpleStringProperty(billBatch);
        this.billQuantity = new SimpleIntegerProperty(billQuantity);
         System.out.println("");
        this.check=check;
    }

    public String getBillItem() {
        return billItem.get();
    }

    public String getBillBatch() {
        return billBatch.get();
    }

    public Integer getBillQuantity() {
        return billQuantity.get();
    }

    public String getBillFree() {
        return billFree.get();
    }

    public Float getBillRate() {
        return billRate.get();
    }

    public Float getBillAmount() {
        return billAmount.get();
    }

    public void setBillQuantity(int billQuantity) {
        this.billQuantity.set(billQuantity);
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
