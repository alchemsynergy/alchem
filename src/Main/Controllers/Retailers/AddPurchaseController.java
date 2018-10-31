package Main.Controllers.Retailers;

import Main.Controllers.LoginController;
import Main.ErrorAndInfo.AlertBox;
import Main.Helpers.Add_purchase;
import Main.Helpers.Medicine;
import Main.Helpers.Purchase_history;
import Main.Helpers.Retailers.ProfitLoss;
import Main.Helpers.Retailers.Purchase;
import Main.Helpers.UserInfo;
import Main.JdbcConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class AddPurchaseController {

    ObservableList<String> Mode = FXCollections.observableArrayList("Cash", "Debit Card", "Credit Card");
    ObservableList<String> Type = FXCollections.observableArrayList("prescribed", "non prescribed");
    ObservableList<Add_purchase> table_data = FXCollections.observableArrayList();
    ObservableList<String> whole_medicine_list = FXCollections.observableArrayList();
    ObservableList<Purchase_history> search_bill_table_list = FXCollections.observableArrayList();

    @FXML
    private TextField wholesaler_name, bill_no, hsn_code,medicine_name,salt,company,batch_number,quantity,cost_price,mrp,total_amount,sgst,cgst,igst,ipunit,ppitem,search_bill;
    @FXML
    private DatePicker date,expiry_date,mfd;
    @FXML
    private TableView<Add_purchase> purchase_table;
    @FXML
    private TableColumn<Add_purchase, String> purchase_hsn;
    @FXML
    private TableColumn<Add_purchase, String> purchase_item;
    @FXML
    private TableColumn<Add_purchase, String> purchase_batch;
    @FXML
    private TableColumn<Add_purchase, Integer> purchase_quantity;
    @FXML
    private TableColumn<Add_purchase, Float> purchase_cost;
    @FXML
    private TableColumn<Add_purchase, Float> purchase_mrp;
    @FXML
    private TableColumn<Add_purchase, String> purchase_salt;
    @FXML
    private TableColumn<Add_purchase, String> purchase_company;
    @FXML
    private TableColumn<Add_purchase, String> purchase_type;
    @FXML
    private TableColumn<Add_purchase, String> purchase_expiry;
    @FXML
    private TableColumn<Add_purchase, String> purchase_mfd;
    @FXML
    private TableColumn<Add_purchase, Integer> purchase_sgst;
    @FXML
    private TableColumn<Add_purchase, Integer> purchase_cgst;
    @FXML
    private TableColumn<Add_purchase, Integer> purchase_igst;
    @FXML
    private TableColumn<Add_purchase, Integer> purchase_ipunit;
    @FXML
    private TableColumn<Add_purchase, Integer> purchase_ppitem;
    @FXML
    private TableView<Purchase_history> search_bill_table;
    @FXML
    private TableColumn<Purchase_history, String> search_wholesaler;
    @FXML
    private TableColumn<Purchase_history, Long> search_bill_no;
    @FXML
    private TableColumn<Purchase_history, String> search_date;
    @FXML
    private TableColumn<Purchase_history, Float> search_amount;
    @FXML
    private ComboBox mode,medicine_type;
    private MainFeaturesTabSceneController mainFeaturesTabSceneController;

    public void initialize() {

        purchase_table.setItems(table_data);
        mode.setItems(Mode);
        mode.getSelectionModel().selectFirst();
        mfd.setEditable(false);
        expiry_date.setEditable(false);
        date.setEditable(false);
        date.setValue(LocalDate.now());
        medicine_type.setItems(Type);
        medicine_type.getSelectionModel().selectFirst();

        setSearchBillDataList();
        search_bill_table.setItems(search_bill_table_list);
        searchTableClickEvent();
        initializePurchaseHistoryProperty();
        initializeMedicineList();
        medicineAutoCompleteBinding();
        medicineKeyReleaseEvent();
        initializePurchaseProperty();
        searchKeyReleaseEvent();
        tableKeyPressEvent();
        ppitemKeyPressEvent();
    }

    public void setSearchBillDataList() {
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
             System.out.println("");
            ResultSet searchTableResultSet = sqlStatement.executeQuery("SELECT wholesaler_name,bill_no,date,total_amount FROM retailer_purchase_bill where user_access_id='" + UserInfo.accessId + "'");
            while (searchTableResultSet.next()) {
                search_bill_table_list.add(new Purchase_history(searchTableResultSet.getString("wholesaler_name"), searchTableResultSet.getLong("bill_no"), searchTableResultSet.getString("date"), searchTableResultSet.getFloat("total_amount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializePurchaseProperty()
    {
        purchase_hsn.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseHsn"));
        purchase_item.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseItem"));
        purchase_batch.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseBatch"));
        purchase_quantity.setCellValueFactory(new PropertyValueFactory<Add_purchase, Integer>("purchaseQuantity"));
        purchase_cost.setCellValueFactory(new PropertyValueFactory<Add_purchase, Float>("purchaseCost"));
        purchase_mrp.setCellValueFactory(new PropertyValueFactory<Add_purchase, Float>("purchaseMrp"));
        purchase_salt.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseSalt"));
        purchase_company.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseCompany"));
        purchase_type.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseType"));
        purchase_expiry.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseExpiry"));
        purchase_mfd.setCellValueFactory(new PropertyValueFactory<Add_purchase, String>("purchaseMfd"));
        purchase_sgst.setCellValueFactory(new PropertyValueFactory<Add_purchase, Integer>("purchaseSgst"));
        purchase_cgst.setCellValueFactory(new PropertyValueFactory<Add_purchase, Integer>("purchaseCgst"));
        purchase_igst.setCellValueFactory(new PropertyValueFactory<Add_purchase, Integer>("purchaseIgst"));
        purchase_ipunit.setCellValueFactory(new PropertyValueFactory<Add_purchase, Integer>("purchaseIpunit"));
        purchase_ppitem.setCellValueFactory(new PropertyValueFactory<Add_purchase, Integer>("purchasePpitem"));
    }

    public void initializePurchaseHistoryProperty() {
        search_wholesaler.setCellValueFactory(new PropertyValueFactory<Purchase_history, String>("searchWholesaler"));
        search_bill_no.setCellValueFactory(new PropertyValueFactory<Purchase_history, Long>("searchBillNo"));
        search_date.setCellValueFactory(new PropertyValueFactory<Purchase_history, String>("searchDate"));
        search_amount.setCellValueFactory(new PropertyValueFactory<Purchase_history, Float>("searchAmount"));
    }

    public void tableKeyPressEvent() {
        purchase_table.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (purchase_table.getSelectionModel().getSelectedItem() != null) {
                if (keyEvent.getCode() == KeyCode.DELETE)
                    onDelete(null);
            }
        });
    }

    public void medicineKeyReleaseEvent()
    {
        medicine_name.setOnKeyReleased((KeyEvent keyEvent) -> {

            if(keyEvent.getCode() != KeyCode.ENTER)
            {
                hsn_code.setText("");
                salt.setText("");
                company.setText("");
                medicine_type.getSelectionModel().selectFirst();
            }

        });
    }

    public void ppitemKeyPressEvent()
    {
        ppitem.setOnKeyPressed((KeyEvent keyEvent) -> {
            Node source = (Node) keyEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Layouts/alert_stage.fxml"));

            if (keyEvent.getCode() == KeyCode.ENTER)
                onAddEntry(currentStage, fxmlLoader);
        });
    }

    public void initializeMedicineList() {
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT name FROM medicine");
            while (resultSet.next()) {
                whole_medicine_list.add(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void medicineAutoCompleteBinding() {
        TextFields.bindAutoCompletion(medicine_name, whole_medicine_list).setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> autoCompletionEvent) -> {
            String selectedItem = medicine_name.getText();
            try {
                Connection dbConnection = JDBC.databaseConnect();
                PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT  medicine.hsn_number, medicine.salt, medicine.company, medicine.type, gst.sgst, gst.cgst, gst.igst FROM medicine JOIN gst ON medicine.medicine_id=gst.medicine_id WHERE name=?");
                preparedStatement.setString(1, selectedItem);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    hsn_code.setText(resultSet.getString("hsn_number"));
                    salt.setText(resultSet.getString("salt"));
                    company.setText(resultSet.getString("company"));
                    medicine_type.getSelectionModel().select(resultSet.getString("type"));
                    sgst.setText(String.valueOf(resultSet.getInt("sgst")));
                    cgst.setText(String.valueOf(resultSet.getInt("cgst")));
                    igst.setText(String.valueOf(resultSet.getInt("igst")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void onAddPurchase(ActionEvent actionEvent)
    {
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Layouts/alert_stage.fxml"));

        onAddEntry(currentStage, fxmlLoader);
    }

    public void onAddEntry(Stage currentStage, FXMLLoader fxmlLoader)
    {

        String regexDecimal = "[0-9]*\\.?[0-9]+";
        String regexQuantity = "^[0-9]*$";

        String Hsn = hsn_code.getText();
        String Item = medicine_name.getText();
        String Batch = batch_number.getText();
        String Quantity = quantity.getText();
        String Cost = cost_price.getText();
        String Mrp = mrp.getText();
        String Salt = salt.getText();
        String Company = company.getText();
        String Type = medicine_type.getSelectionModel().getSelectedItem().toString();
        String Sgst = sgst.getText();
        String Cgst = cgst.getText();
        String Igst = igst.getText();
        String Ipunit = ipunit.getText();
        String Ppitem = ppitem.getText();

        if (Hsn.equals("") || Item.equals("") || Batch.equals("") || Quantity.equals("") || Cost.equals("") || Mrp.equals("") ||
                expiry_date.getValue() == null || mfd.getValue() == null || Sgst.equals("") || Cgst.equals("") || Igst.equals("") || Ipunit.equals("") || Ppitem.equals(""))
        {
            if (Hsn.equals(""))
                hsn_code.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Item.equals(""))
                medicine_name.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Batch.equals(""))
                batch_number.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Quantity.equals(""))
                quantity.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Cost.equals(""))
                cost_price.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Mrp.equals(""))
                mrp.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (expiry_date.getValue() == null)
                expiry_date.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (mfd.getValue() == null)
                mfd.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Sgst.equals(""))
                sgst.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Cgst.equals(""))
                cgst.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Igst.equals(""))
                igst.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Ipunit.equals(""))
                ipunit.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Ppitem.equals(""))
                ppitem.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else if (!Quantity.matches(regexQuantity)) {
            new AlertBox(currentStage, fxmlLoader, false, "Quantity must be a number !!");
            quantity.setStyle("-fx-text-inner-color: red;");
        }
        else if(!Cost.matches(regexDecimal)) {
            new AlertBox(currentStage, fxmlLoader, false, "Cost must be a number !!");
            cost_price.setStyle("-fx-text-inner-color: red;");
        }
        else if(!Mrp.matches(regexDecimal)) {
            new AlertBox(currentStage, fxmlLoader, false, "MRP must be a number !!");
            mrp.setStyle("-fx-text-inner-color: red;");
        }
        else
        {
            int intQuantity = Integer.parseInt(Quantity);
            float floatCost = Float.parseFloat(Cost);
            float floatMrp = Float.parseFloat(Mrp);
            String Expiry = expiry_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String Mfd = mfd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int SgstInt = Integer.parseInt(sgst.getText());
            int CgstInt = Integer.parseInt(cgst.getText());
            int IgstInt = Integer.parseInt(igst.getText());
            int IpunitInt = Integer.parseInt(ipunit.getText());
            int PpitemInt = Integer.parseInt(ppitem.getText());

            table_data.add(new Add_purchase(Hsn, Item, Batch, intQuantity, floatCost, floatMrp, Salt, Company, Type, Expiry, Mfd, SgstInt, CgstInt, IgstInt, IpunitInt, PpitemInt));

            hsn_code.setText("");
            medicine_name.setText("");
            batch_number.setText("");
            quantity.setText("");
            cost_price.setText("");
            mrp.setText("");
            salt.setText("");
            company.setText("");

            hsn_code.setStyle(null);
            medicine_name.setStyle(null);
            batch_number.setStyle(null);
            quantity.setStyle(null);
            cost_price.setStyle(null);
            mrp.setStyle(null);
            salt.setStyle(null);
            company.setStyle(null);
            expiry_date.setStyle(null);
            mfd.setStyle(null);
            ipunit.setStyle(null);
            ppitem.setStyle(null);
            sgst.setStyle(null);
            cgst.setStyle(null);
            igst.setStyle(null);
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        int selectedIndex = purchase_table.getSelectionModel().getSelectedIndex();
        purchase_table.getItems().remove(selectedIndex);

    }

    public void onSave(ActionEvent actionEvent)
    {
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Layouts/alert_stage.fxml"));

        String WholesalerName = wholesaler_name.getText();
        String Mode = mode.getSelectionModel().getSelectedItem().toString();
        String BillNo = bill_no.getText();
        String Amount = total_amount.getText();
        float floatAmount = 0;
        String regexFloat = "[0-9]*\\.?[0-9]+";
        String regexInt = "^[0-9]*$";

        if (Amount.equals("")) {
            floatAmount = 0;
        } else floatAmount = Float.parseFloat(total_amount.getText());

        if(WholesalerName.equals("") || date.getValue() == null || BillNo.equals(""))
        {
            new AlertBox(currentStage, fxmlLoader, true, "Error!! Blank Fields");

            if(WholesalerName.equals(""))
                wholesaler_name.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if(date.getValue()==null)
                date.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if(BillNo.equals(""))
                bill_no.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        else if(!BillNo.matches(regexInt))
        {
            new AlertBox(currentStage, fxmlLoader, false, "Bill No must be correct !!");
        }
        else if(!Amount.matches(regexFloat))
        {
            new AlertBox(currentStage, fxmlLoader, false, "Amount must be a number !!");
        }
        else
        {
            String Date = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Long BillNoLong = Long.parseLong(BillNo);
            Float totalAmount = Float.parseFloat(total_amount.getText());
            int med_flag = 0;
            int flag = 0;
            int medicine_id = 0;
            int medicine_info_id = 0;
            long rp_bill_id = 0;
            int piece = 0;
            int pp_item = 0;
            int items = 0;
            int ip_unit = 0;
            int unit = 0;

            try {
                Connection dbConnection = JDBC.databaseConnect();

                PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT rp_bill_id FROM retailer_purchase_bill WHERE user_access_id=? AND bill_no=? AND wholesaler_name=?");
                preparedStatement.setInt(1, LoginController.userAccessId);
                preparedStatement.setLong(2, BillNoLong);
                preparedStatement.setString(3, WholesalerName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    flag = 1;
                }
                else
                    flag = 0;
            }
            catch (Exception e) {e.printStackTrace();}

            // Condition to save a new purchase bill entry

            if(flag == 0)
            {
                try {
                    Connection dbConnection = JDBC.databaseConnect();
                    PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO retailer_purchase_bill VALUES (?,?,?,?,?,?,DEFAULT )");
                    preparedStatement.setInt(1, LoginController.userAccessId);
                    preparedStatement.setLong(2, BillNoLong);
                    preparedStatement.setString(3, WholesalerName);
                    preparedStatement.setString(4, Date);
                    preparedStatement.setString(5, Mode);
                    preparedStatement.setFloat(6, floatAmount);
                    preparedStatement.executeUpdate();

                    Statement statement = dbConnection.createStatement();
                    ResultSet resultSet1 = statement.executeQuery("SELECT MAX(rp_bill_id) FROM retailer_purchase_bill");
                    if (resultSet1.next()) {
                        rp_bill_id = resultSet1.getLong(1);
                    }
                }
                catch (Exception e) {e.printStackTrace();}

            }

            // Condition to update the purchase bill entry

            else
            {
                try {
                    Connection dbConnection = JDBC.databaseConnect();
                    PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE retailer_purchase_bill SET date=?, mode=?, total_amount=? WHERE user_access_id=? AND bill_no=? AND wholesaler_name=?");
                    preparedStatement.setString(1, Date);
                    preparedStatement.setString(2, Mode);
                    preparedStatement.setFloat(3, totalAmount);
                    preparedStatement.setInt(4, LoginController.userAccessId);
                    preparedStatement.setLong(5, BillNoLong);
                    preparedStatement.setString(6, WholesalerName);
                    preparedStatement.executeUpdate();

                    preparedStatement = dbConnection.prepareStatement("SELECT rp_bill_id FROM retailer_purchase_bill WHERE user_access_id=? AND bill_no=? AND wholesaler_name=?");
                    preparedStatement.setInt(1, LoginController.userAccessId);
                    preparedStatement.setLong(2, BillNoLong);
                    preparedStatement.setString(3, WholesalerName);
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    if (resultSet1.next()) {
                        rp_bill_id = resultSet1.getLong(1);
                    }

                    preparedStatement = dbConnection.prepareStatement("SELECT medicine_info_id FROM retailer_purchase_bill_info WHERE rp_bill_id=?");
                    preparedStatement.setLong(1, rp_bill_id);
                    ResultSet resultSet2 = preparedStatement.executeQuery();
                    while (resultSet2.next())
                    {
                        medicine_info_id = resultSet2.getInt(1);

                        PreparedStatement preparedStatement1 = dbConnection.prepareStatement("DELETE FROM quantity WHERE medicine_info_id=?");
                        preparedStatement1.setInt(1, medicine_info_id);
                        preparedStatement1.executeUpdate();

                        preparedStatement1 = dbConnection.prepareStatement("DELETE FROM medicine_info WHERE medicine_info_id=?");
                        preparedStatement1.setInt(1, medicine_info_id);
                        preparedStatement1.executeUpdate();

                    }

                    preparedStatement = dbConnection.prepareStatement("DELETE FROM retailer_purchase_bill_info WHERE rp_bill_id=?");
                    preparedStatement.setLong(1, rp_bill_id);
                    preparedStatement.executeUpdate();
                }
                catch (Exception e) {e.printStackTrace();}

            }

            Iterator<Add_purchase> it = table_data.iterator();
            while (it.hasNext())
            {
                Add_purchase temp = it.next();

                try {
                    Connection dbConnection = JDBC.databaseConnect();
                    Statement statement = dbConnection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT name FROM medicine");
                    while(resultSet.next())
                    {
                        if(temp.getPurchaseItem().equals(resultSet.getString("name")))
                        {
                            med_flag = 1;
                            break;
                        }
                        else med_flag = 0;
                    }
                }
                catch (Exception e) {e.printStackTrace();}

                // when medicine already exists

                if(med_flag == 1)
                {

                    try {
                        Connection dbConnection = JDBC.databaseConnect();
                        PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_id FROM medicine WHERE name=?");
                        preparedStatement.setString(1, temp.getPurchaseItem());
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if(resultSet.next())
                        {
                            medicine_id = resultSet.getInt("medicine_id");
                        }
                    }
                    catch (Exception e) {e.printStackTrace();}
                }

                else
                {
                    try {
                        Connection dbConnection = JDBC.databaseConnect();
                        PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO medicine VALUES (DEFAULT,?,?,?,?,?)");
                        preparedStatement.setString(1, temp.getPurchaseItem());
                        preparedStatement.setString(2, temp.getPurchaseSalt());
                        preparedStatement.setString(3, temp.getPurchaseCompany());
                        preparedStatement.setString(4, temp.getPurchaseHsn());
                        preparedStatement.setString(5, temp.getPurchaseType());
                        preparedStatement.executeUpdate();

                        preparedStatement = dbConnection.prepareStatement("SELECT MAX(medicine_id) FROM medicine");
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if(resultSet.next())
                        {
                            medicine_id = resultSet.getInt(1);
                        }

                        preparedStatement = dbConnection.prepareStatement("INSERT INTO gst VALUES (?,?,?,?,?)");
                        preparedStatement.setInt(1, medicine_id);
                        preparedStatement.setInt(2, temp.getPurchaseSgst());
                        preparedStatement.setInt(3, temp.getPurchaseCgst());
                        preparedStatement.setInt(4, temp.getPurchaseIgst());
                        preparedStatement.setInt(5, medicine_id);
                        preparedStatement.executeUpdate();
                    }

                    catch (Exception e){e.printStackTrace();}
                }

                try {
                    Connection dbConnection = JDBC.databaseConnect();
                    PreparedStatement preparedStatement1 = dbConnection.prepareStatement("INSERT INTO medicine_info VALUES (?,?,?,?,?,?,DEFAULT )");
                    preparedStatement1.setInt(1, medicine_id);
                    preparedStatement1.setString(2, temp.getPurchaseBatch());
                    preparedStatement1.setString(3, temp.getPurchaseMfd());
                    preparedStatement1.setString(4, temp.getPurchaseExpiry());
                    preparedStatement1.setFloat(5, temp.getPurchaseMrp());
                    preparedStatement1.setFloat(6, temp.getPurchaseCost());
                    preparedStatement1.executeUpdate();

                    preparedStatement1 = dbConnection.prepareStatement("SELECT MAX (medicine_info_id) FROM medicine_info");
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    if (resultSet1.next())
                    {
                        medicine_info_id = resultSet1.getInt(1);
                    }

                    preparedStatement1 = dbConnection.prepareStatement("INSERT INTO retailer_purchase_bill_info VALUES (?,?,DEFAULT )");
                    preparedStatement1.setLong(1, rp_bill_id);
                    preparedStatement1.setInt(2, medicine_info_id);
                    preparedStatement1.executeUpdate();

                    // Code to populate quantities

                    unit = temp.getPurchaseQuantity();
                    ip_unit = temp.getPurchaseIpunit();
                    pp_item = temp.getPurchasePpitem();
                    items = unit * ip_unit;
                    piece = items * pp_item;

                    preparedStatement1 = dbConnection.prepareStatement("INSERT INTO quantity VALUES (?,?,?,?,?,?,DEFAULT )");
                    preparedStatement1.setInt(1, medicine_info_id);
                    preparedStatement1.setInt(2, unit);
                    preparedStatement1.setInt(3, ip_unit);
                    preparedStatement1.setInt(4, items);
                    preparedStatement1.setInt(5, pp_item);
                    preparedStatement1.setInt(6, piece);
                    preparedStatement1.executeUpdate();

                    InventoryController.medicines.add(new Medicine(medicine_id,temp.getPurchaseItem(),temp.getPurchaseSalt(),temp.getPurchaseCompany(),temp.getPurchaseType(),temp.getPurchaseHsn(),temp.getPurchaseBatch(),temp.getPurchaseExpiry(),temp.getPurchaseQuantity(),temp.getPurchaseMrp(),temp.getPurchaseCost(),temp.getPurchaseSgst(),temp.getPurchaseCgst(),temp.getPurchaseIgst()));
                }
                catch (Exception e) {e.printStackTrace();}
            }
            new AlertBox(currentStage, fxmlLoader, true, "Saved Successfully !!");

            // updating side search table

            if(flag == 0)
            {
                search_bill_table_list.add(new Purchase_history(wholesaler_name.getText(), BillNoLong, Date, totalAmount));
                ViewPurchaseController.purchaseList.add(new Purchase(Date,BillNoLong,wholesaler_name.getText(), Mode,Float.parseFloat(Amount)));
                for (int i = 0; i < 7; i++) {
                    int chk = viewPurchaseEvent(i,Date,Float.parseFloat(Amount));
                    if (chk == 1)
                        break;
                }

                String date="";
                try {
                    Connection conn=JDBC.databaseConnect();
                    Statement sqlStatement=conn.createStatement();
                    ResultSet purchaseProfitResultSet=sqlStatement.executeQuery("SELECT date from retailer_purchase_bill where bill_no='"+BillNoLong+"' AND user_access_id='"+UserInfo.accessId+"' AND wholesaler_name='"+wholesaler_name.getText()+"'");
                    while(purchaseProfitResultSet.next())
                    {
                        date=purchaseProfitResultSet.getString(1);
                    }
                }catch (Exception e)
                {e.printStackTrace();}

                Iterator<ProfitLoss> plIterator = ProfitLossController.profitList.iterator();
                while (plIterator.hasNext()) {
                    ProfitLoss profitLoss=plIterator.next();
                    if(date.equals(profitLoss.getDate()))
                    {
                        profitLoss.setTotalPurchase(Float.parseFloat(Amount)+profitLoss.getTotalPurchase());
                        profitLoss.setProfit(profitLoss.getProfit()-Float.parseFloat(Amount));
                        for (int i = 0; i < 7; i++) {
                            int chk = viewPurchaseProfitEvent(i, date,Float.parseFloat(Amount));
                            if (chk == 1)
                                break;
                        }
                    }
                }
                ObservableList<ProfitLoss> temporary_profit_data = FXCollections.observableArrayList(ProfitLossController.profitList);
                ProfitLossController.profitList.clear();
                ProfitLossController.profitList.addAll(temporary_profit_data);
            }
            else
            {
                Iterator<Purchase_history> billingHistoryIterator = search_bill_table_list.iterator();
                while (billingHistoryIterator.hasNext()) {
                    Purchase_history bill = billingHistoryIterator.next();
                    if (BillNoLong == bill.getSearchBillNo() && WholesalerName.equals(bill.getSearchWholesaler())) {
                        bill.setSearchAmount(totalAmount);
                    }
                }
                ObservableList<Purchase_history> temporary_add_sale_data = FXCollections.observableArrayList(search_bill_table_list);
                search_bill_table_list.clear();
                search_bill_table_list.addAll(temporary_add_sale_data);
                float prevAmount=0;
                Iterator<Purchase> purchaseIterator= ViewPurchaseController.purchaseList.iterator();
                while(purchaseIterator.hasNext())
                {
                    Purchase purchase=purchaseIterator.next();
                    if(purchase.getWholesalerName().equals(wholesaler_name.getText()) && purchase.getBillNumber()==BillNoLong && purchase.getDate().equals(Date))
                    {
                        prevAmount=purchase.getAmount();
                        purchase.setAmount(Float.parseFloat(Amount));
                        break;
                    }
                }
                ObservableList<Purchase> temp=FXCollections.observableArrayList(ViewPurchaseController.purchaseList);
                ViewPurchaseController.purchaseList.clear();
                ViewPurchaseController.purchaseList.addAll(temp);
                for (int i = 0; i < 7; i++) {
                    int chk = viewPurchaseEvent(i,Date,Float.parseFloat(Amount)-prevAmount);
                    if (chk == 1)
                        break;
                }
                String date="";
                try {
                    Connection conn=JDBC.databaseConnect();
                    Statement sqlStatement=conn.createStatement();
                    ResultSet purchaseProfitResultSet=sqlStatement.executeQuery("SELECT date from retailer_purchase_bill where bill_no='"+BillNoLong+"' AND user_access_id='"+UserInfo.accessId+"' AND wholesaler_name='"+wholesaler_name.getText()+"'");
                    while(purchaseProfitResultSet.next())
                    {
                        date=purchaseProfitResultSet.getString(1);
                    }
                }catch (Exception e)
                {e.printStackTrace();}

                Iterator<ProfitLoss> plIterator = ProfitLossController.profitList.iterator();
                while (plIterator.hasNext()) {
                    ProfitLoss profitLoss=plIterator.next();
                    if(date.equals(profitLoss.getDate()))
                    {
                        profitLoss.setTotalPurchase(Float.parseFloat(Amount)-prevAmount+profitLoss.getTotalPurchase());
                        profitLoss.setProfit(profitLoss.getProfit()-(Float.parseFloat(Amount)-prevAmount));
                        for (int i = 0; i < 7; i++) {
                            int chk = viewPurchaseProfitEvent(i, date,Float.parseFloat(Amount)-prevAmount);
                            if (chk == 1)
                                break;
                        }
                    }
                }
                ObservableList<ProfitLoss> temporary_profit_data = FXCollections.observableArrayList(ProfitLossController.profitList);
                ProfitLossController.profitList.clear();
                ProfitLossController.profitList.addAll(temporary_profit_data);
            }

        }
    }

    public void onNewBill(ActionEvent actionEvent) {
        purchase_table.getItems().clear();
        wholesaler_name.setText(null);
        bill_no.setText(null);
        wholesaler_name.setStyle(null);
        bill_no.setStyle(null);
    }

    public void searchKeyReleaseEvent() {
        search_bill.setOnKeyReleased((KeyEvent keyEvent) -> {
            long bill_no = 0;
            String date = "";
            String whoesaler = "";
            float total_amount = 0;

            ObservableList<Purchase_history> search_bill_data = FXCollections.observableArrayList();

            search_bill_table.setItems(search_bill_data);
            String search = search_bill.getText();

            if (search.equals("")) {
                search_bill_table.setItems(search_bill_table_list);
            } else {
                Iterator<Purchase_history> iterator = search_bill_table_list.iterator();
                while (iterator.hasNext()) {
                    Purchase_history temp = iterator.next();
                    bill_no = temp.getSearchBillNo();
                    if (String.valueOf(bill_no).contains(search)) {
                        whoesaler = temp.getSearchWholesaler();
                        date = temp.getSearchDate();
                        total_amount = temp.getSearchAmount();
                        search_bill_data.add(new Purchase_history(whoesaler,bill_no, date, total_amount));
                    }

                }

            }

        });
    }

    public void searchTableClickEvent() {
        search_bill_table.setRowFactory(tv -> {
            TableRow<Purchase_history> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    table_data.clear();
                    Purchase_history rowData = row.getItem();
                    long billNo = rowData.getSearchBillNo();
                    String Date = rowData.getSearchDate();
                    float totalAmount = rowData.getSearchAmount();
                    String Wholesaler = rowData.getSearchWholesaler();
                    int medicine_id = 0;
                    int medicine_info_id = 0;
                    String Mode = "";
                    long rp_bill_id = 0;
                    String batch_number = "";
                    String manufacture_date = "";
                    String expiry_date = "";
                    float mrp = 0;
                    float cost_price = 0;
                    String med_name = "";
                    String med_salt = "";
                    String med_company = "";
                    String med_hsn = "";
                    String med_type = "";
                    int sgst = 0;
                    int cgst = 0;
                    int igst = 0;
                    int unit = 0;
                    int ip_unit = 0;
                    int pp_item = 0;

                    try {
                        Connection dbConnection = JDBC.databaseConnect();
                        PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT mode, rp_bill_id FROM retailer_purchase_bill WHERE user_access_id=? AND bill_no=? AND date=? AND total_amount=? AND wholesaler_name=?");
                        preparedStatement.setInt(1, LoginController.userAccessId);
                        preparedStatement.setLong(2, billNo);
                        preparedStatement.setString(3, Date);
                        preparedStatement.setFloat(4, totalAmount);
                        preparedStatement.setString(5, Wholesaler);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            Mode = resultSet.getString("mode");
                            rp_bill_id = resultSet.getLong("rp_bill_id");

                            mode.getSelectionModel().select(Mode);
                            date.setValue(LocalDate.parse(Date));
                            bill_no.setText(String.valueOf(billNo));
                            total_amount.setText(String.valueOf(totalAmount));
                            wholesaler_name.setText(Wholesaler);

                            preparedStatement = dbConnection.prepareStatement("SELECT medicine_info_id FROM retailer_purchase_bill_info WHERE rp_bill_id=?");
                            preparedStatement.setLong(1, rp_bill_id);
                            ResultSet resultSet1 = preparedStatement.executeQuery();
                            while (resultSet1.next()) {
                                medicine_info_id = resultSet1.getInt(1);

                                PreparedStatement preparedStatement1 = dbConnection.prepareStatement("SELECT medicine_id, batch_number, manufacture_date, expiry_date, mrp, cost_price FROM medicine_info WHERE medicine_info_id=?");
                                preparedStatement1.setInt(1, medicine_info_id);
                                ResultSet resultSet2 = preparedStatement1.executeQuery();
                                if(resultSet2.next())
                                {
                                    medicine_id = resultSet2.getInt("medicine_id");
                                    batch_number = resultSet2.getString("batch_number");
                                    manufacture_date = resultSet2.getString("manufacture_date");
                                    expiry_date = resultSet2.getString("expiry_date");
                                    mrp = resultSet2.getFloat("mrp");
                                    cost_price = resultSet2.getFloat("cost_price");
                                }

                                preparedStatement1 = dbConnection.prepareStatement("SELECT medicine.name, medicine.salt, medicine.company, medicine.hsn_number, medicine.type, gst.sgst, gst.cgst, gst.igst FROM medicine JOIN gst ON medicine.medicine_id = gst.medicine_id WHERE medicine.medicine_id=?");
                                preparedStatement1.setInt(1, medicine_id);
                                ResultSet resultSet3 = preparedStatement1.executeQuery();
                                if (resultSet3.next())
                                {
                                    med_name = resultSet3.getString("name");
                                    med_salt = resultSet3.getString("salt");
                                    med_company = resultSet3.getString("company");
                                    med_hsn = resultSet3.getString("hsn_number");
                                    med_type = resultSet3.getString("type");
                                    sgst = resultSet3.getInt("sgst");
                                    cgst = resultSet3.getInt("cgst");
                                    igst = resultSet3.getInt("igst");
                                }

                                preparedStatement1 = dbConnection.prepareStatement("SELECT unit, ip_unit, pp_item FROM quantity WHERE medicine_info_id=?");
                                preparedStatement1.setInt(1, medicine_info_id);
                                ResultSet resultSet4 = preparedStatement1.executeQuery();
                                if(resultSet4.next())
                                {
                                    unit = resultSet4.getInt("unit");
                                    ip_unit = resultSet4.getInt("ip_unit");
                                    pp_item = resultSet4.getInt("pp_item");
                                }

                                table_data.add(new Add_purchase(med_hsn, med_name, batch_number, unit, cost_price, mrp, med_salt, med_company, med_type, expiry_date, manufacture_date, sgst, cgst, igst, ip_unit, pp_item));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    public int viewPurchaseEvent(int i, String billDate, Float Amount) {
        long date = ViewPurchaseController.day[i];
        long month = ViewPurchaseController.month[i];
        long year = ViewPurchaseController.year[i];
        String datechk;
        if(month<10 && date<10)
            datechk=year+"-0"+month+"-0"+date;
        else if(month<10)
            datechk=year+"-0"+month+"-"+date;
        else if(date<10)
            datechk=year+"-"+month+"-0"+date;
        else
            datechk=year+"-"+month+"-"+date;
        if (datechk.equals(billDate)) {
            ViewPurchaseController.sum[i] = ViewPurchaseController.sum[i] + Amount;
            try {
                mainFeaturesTabSceneController.setPurchaseLabel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
        return 0;
    }

    public int viewPurchaseProfitEvent(int i, String billDate, Float Amount) {
        long date = ProfitLossController.day[i];
        long month = ProfitLossController.month[i];
        long year = ProfitLossController.year[i];
        String datechk;
        if(month<10 && date<10)
            datechk=year+"-0"+month+"-0"+date;
        else if(month<10)
            datechk=year+"-0"+month+"-"+date;
        else if(date<10)
            datechk=year+"-"+month+"-0"+date;
        else
            datechk=year+"-"+month+"-"+date;
        if (datechk.equals(billDate)) {
            ProfitLossController.sum[i] = ProfitLossController.sum[i] - Amount;
            ProfitLossController.totalPurchase[i]=ProfitLossController.totalPurchase[i]+Amount;
            try {
                mainFeaturesTabSceneController.setProfitLabel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
        return 0;
    }

    public void init(MainFeaturesTabSceneController mainFeaturesTabSceneController) {
    this.mainFeaturesTabSceneController=mainFeaturesTabSceneController;
    }
}
