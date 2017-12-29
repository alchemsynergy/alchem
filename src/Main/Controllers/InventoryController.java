package Main.Controllers;

import Main.Helpers.Medicine;
import Main.JdbcConnection.JDBC;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Predicate;


public class InventoryController {
    private static double drawableWidth;
    public static ObservableList<Medicine> medicines;
    FilteredList<Medicine> filteredData;
    Boolean advancedSearchedToggle = false;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Medicine> medicineTableView;
    @FXML
    private TableColumn<Medicine, Integer> codeColumn;
    @FXML
    private TableColumn<Medicine, String> nameColumn;
    @FXML
    private TableColumn<Medicine, String> saltColumn;
    @FXML
    private TableColumn<Medicine, String> companyColumn;
    @FXML
    private TableColumn<Medicine, String> typeColumn;
    @FXML
    private TableColumn<Medicine, String> hsnColumn;
    @FXML
    private TableColumn<Medicine, String> batchColumn;
    @FXML
    private TableColumn<Medicine, String> expiryColumn;
    @FXML
    private TableColumn<Medicine, Integer> quantityColumn;
    @FXML
    private TableColumn<Medicine, Float> mrpColumn;
    @FXML
    private TableColumn<Medicine, Float> costColumn;
    @FXML
    private TableColumn<Medicine, Integer> sgstColumn;
    @FXML
    private TableColumn<Medicine, Integer> cgstColumn;
    @FXML
    private TableColumn<Medicine, Integer> igstColumn;
    @FXML
    private AnchorPane inventory_parent_pane;
    @FXML
    private Button advancedSearch;
    @FXML
    private RadioButton medicineButton;
    @FXML
    private RadioButton codeButton;
    @FXML
    private RadioButton saltButton;
    @FXML
    private RadioButton companyButton;
    @FXML
    private RadioButton typeButton;
    @FXML
    private RadioButton batchButton;
    @FXML
    private RadioButton hsnButton;
    @FXML
    private Label searchBy;
    @FXML
    private ToggleGroup searchGroup;
    @FXML
    private VBox searchVBox;

    public static void setDrawableWidth(double width) {
        drawableWidth = width;
    }

    public void initialize() {
        inventory_parent_pane.setPrefWidth(drawableWidth);
        initializeTable();
        assignToggleValues();
        onChangingSearchOption();
        searchTable();
        disableVisibilityAdvancedSearch();
    }

    public void initializeTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("name"));
        saltColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("salt"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("company"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("type"));
        hsnColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("hsn"));
        batchColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("batch"));
        expiryColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("expiry"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("quantity"));
        mrpColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Float>("mrp"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Float>("cost"));
        sgstColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("sgst"));
        cgstColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("cgst"));
        igstColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("igst"));

        medicineTableView.setItems(getMedicine());

    }

    public void assignToggleValues() {
        medicineButton.setUserData("Medicine");
        codeButton.setUserData("Code");
        saltButton.setUserData("Salt");
        companyButton.setUserData("Company");
        batchButton.setUserData("Batch Number");
        typeButton.setUserData("Type");
        hsnButton.setUserData("HSN Number");
    }

    public void onChangingSearchOption() {
        searchGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (searchGroup.getSelectedToggle() != null) {
                    searchField.setText("");
                }
            }
        });

    }

    public boolean linearCompare(String a,String b){

        int p = 1;
        int r = 0;
        for(int i = 0;i < a.length();i++){
            if(p == 1) {
                r = 0;
                for (int j = 0; j < b.length(); j++) {
                    if (b.charAt(j) != a.charAt(j + i)) {
                        r = 1;
                        break;
                    }
                }
                if (r == 0)
                    break;
                else
                    p = 0;
            }
            if(a.charAt(i)==' ')
                p = 1;
        }
        if(r == 0)
            return true;
        else
            return false;
    }

    public void searchTable() {

        filteredData = new FilteredList<>(medicines, e -> true);

        searchField.setOnKeyPressed(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Medicine>) medicine -> {


                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    String codeChk = Integer.toString(medicine.getCode());


                    if (linearCompare(codeChk, newValue) && searchGroup.getSelectedToggle().getUserData().toString().equals("Code")) {
                        return true;
                    } else if (linearCompare(medicine.getName().toLowerCase(), lowerCaseFilter) && searchGroup.getSelectedToggle().getUserData().toString().equals("Medicine")) {
                        return true;
                    } else if (linearCompare(medicine.getSalt().toLowerCase(), lowerCaseFilter) && searchGroup.getSelectedToggle().getUserData().toString().equals("Salt")) {
                        return true;
                    } else if (linearCompare(medicine.getCompany().toLowerCase(), lowerCaseFilter) && searchGroup.getSelectedToggle().getUserData().toString().equals("Company")) {
                        return true;
                    } else if (linearCompare(medicine.getBatch().toLowerCase(), lowerCaseFilter) && searchGroup.getSelectedToggle().getUserData().toString().equals("Batch Number")) {
                        return true;
                    } else if (linearCompare(medicine.getType().toLowerCase(), lowerCaseFilter) && searchGroup.getSelectedToggle().getUserData().toString().equals("Type")) {
                        return true;
                    } else if (linearCompare(medicine.getHsn().toLowerCase(), lowerCaseFilter) && searchGroup.getSelectedToggle().getUserData().toString().equals("HSN Number")) {
                        return true;
                    }
                    return false;



                });
            });
            SortedList<Medicine> searchedData = new SortedList<Medicine>(filteredData);
            searchedData.comparatorProperty().bind(medicineTableView.comparatorProperty());
            medicineTableView.setItems(searchedData);
        });

    }

    public void disableVisibilityAdvancedSearch() {
        searchBy.setVisible(false);
        searchVBox.setVisible(false);
    }

    public ObservableList<Medicine> getMedicine() {
        int code = 0, quantity = 0, sgst = 0, cgst = 0, igst = 0;
        String name, salt, company, type, batch, hsn, expiry;
        float mrp, cost;
        medicines = FXCollections.observableArrayList();

        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
            ResultSet medicineResultSet = sqlStatement.executeQuery("SELECT medicine.medicine_id,medicine.name,medicine.salt,medicine.company,medicine.type,medicine.hsn_number,medicine_info.batch_number,medicine_info.expiry_date,medicine_info.mrp,medicine_info.cost_price,quantity.piece,gst.sgst,gst.cgst,gst.igst FROM medicine JOIN  medicine_info ON medicine.medicine_id=medicine_info.medicine_id JOIN quantity ON medicine_info.medicine_info_id=quantity.medicine_info_id JOIN gst ON medicine.medicine_id=gst.medicine_id");
            while (medicineResultSet.next()) {
                code = medicineResultSet.getInt("medicine_id");
                name = medicineResultSet.getString("name");
                salt = medicineResultSet.getString("salt");
                company = medicineResultSet.getString("company");
                type = medicineResultSet.getString("type");
                hsn = medicineResultSet.getString("hsn_number");
                sgst = medicineResultSet.getInt("sgst");
                cgst = medicineResultSet.getInt("cgst");
                igst = medicineResultSet.getInt("igst");
                batch = medicineResultSet.getString("batch_number");
                expiry = medicineResultSet.getString("expiry_date");
                mrp = medicineResultSet.getFloat("mrp");
                cost = medicineResultSet.getFloat("cost_price");
                quantity = medicineResultSet.getInt("piece");
                medicines.add(new Medicine(code, name, salt, company, type, hsn, batch, expiry, quantity, mrp, cost, sgst, cgst, igst));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public void advancedSearchMethod() {
        if (!advancedSearchedToggle) {
            advancedSearch.setText("Advanced Search v");
            searchBy.setVisible(true);
            searchVBox.setVisible(true);
            advancedSearchedToggle = true;
        } else {
            advancedSearch.setText("Advanced Search >");
            searchBy.setVisible(false);
            searchVBox.setVisible(false);
            advancedSearchedToggle = false;
        }
    }


}
