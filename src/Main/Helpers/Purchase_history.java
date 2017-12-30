package Main.Helpers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase_history {
    private final SimpleStringProperty searchWholesaler;
    private final SimpleLongProperty searchBillNo;
    private final SimpleStringProperty searchDate;
    private final SimpleFloatProperty searchAmount;

    public Purchase_history(String searchWholesaler, Long searchBillNo, String searchDate, Float searchAmount) {
        this.searchWholesaler = new SimpleStringProperty(searchWholesaler);
        this.searchBillNo = new SimpleLongProperty(searchBillNo);
        this.searchDate = new SimpleStringProperty(searchDate);
        this.searchAmount = new SimpleFloatProperty(searchAmount);
    }

    public String getSearchWholesaler() {
        return searchWholesaler.get();
    }

    public void setSearchWholesaler(String searchWholesaler) {
        this.searchWholesaler.set(searchWholesaler);
    }

    public long getSearchBillNo() {
        return searchBillNo.get();
    }

    public void setSearchBillNo(long searchBillNo) {
        this.searchBillNo.set(searchBillNo);
    }

    public String getSearchDate() {
        return searchDate.get();
    }

    public void setSearchDate(String searchDate) {
        this.searchDate.set(searchDate);
    }

    public float getSearchAmount() {
        return searchAmount.get();
    }

    public void setSearchAmount(float searchAmount) {
        this.searchAmount.set(searchAmount);
    }
}
