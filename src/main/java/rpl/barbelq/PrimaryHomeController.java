package rpl.barbelq;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ALDO
 */
public class PrimaryHomeController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
    private int session;
    private String nama;
    private String name,usia,email,password,tinggi;
    private String cek,ambil;
    
    @FXML
    private TextField Changename, Changeusia, Changeemail, Changetinggi, inpBerat;
    @FXML
    private PasswordField Changepassword;
    @FXML
    private Button btnLogout;
    @FXML
    private Label label1,namaUser, namaUser2, namaUser3;
    @FXML
    private HBox hbox1,hbox2,hbox3,hbox4,hbox5;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button Update;
    
    
    private boolean cekEmail(TextField email) {
        boolean hasil = true;
        try{
            dbModel.rs =dbModel.resultset("select email from DataPengguna");
            while(dbModel.rs.next()) {
                String cekEmail = dbModel.rs.getString("email");
                if(email.getText().equals(cekEmail)){
                    hasil = true;
                    break;
                }else{
                    hasil = false;
                }
            }
            dbModel.rs.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return hasil;
    }
    @FXML
    private void btnUpdate(ActionEvent event) {
        if(!Changename.getText().isEmpty() && !Changeusia.getText().isEmpty() && !Changeemail.getText().isEmpty() && !Changepassword.getText().isEmpty() && !Changetinggi.getText().isEmpty()){
            dbModel.InsertOrUpdate("update DataPengguna set nama= '" +Changename.getText()+ "',password= '" +Changepassword.getText()+ "',usia= '" +Changeusia.getText()+ "',tinggi= '" +Changetinggi.getText()+ "' where id_pengguna =  "+session+"");
            if(ambil.equals(Changeemail.getText())){
                btnEdit.disableProperty().set(false);
                Update.disableProperty().set(true);
                btnCancel.disableProperty().set(true);
                namaUser.setText(Changename.getText());
                namaUser2.setText(Changename.getText());
                namaUser3.setText(Changename.getText());
                tutupField();
                hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Update Berhasil");
                a.show();
                name = Changename.getText();
                usia = Changeusia.getText();
                email = Changeemail.getText();
                password = Changepassword.getText();
                tinggi = Changetinggi.getText();
            }else{
                if(cekEmail(Changeemail)){
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setTitle("BarbelQ");
                    a.setHeaderText(null);
                    a.setContentText("Email Sudah Digunakan");
                    a.show();
                }else{
                    dbModel.InsertOrUpdate("update DataPengguna set email = '"+Changeemail.getText()+"' where id_pengguna = "+session+" ");
                    btnEdit.disableProperty().set(false);
                    Update.disableProperty().set(true);
                    btnCancel.disableProperty().set(true);
                    namaUser.setText(Changename.getText());
                    namaUser2.setText(Changename.getText());
                    namaUser3.setText(Changename.getText());
                    tutupField();
                    hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                    hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                    hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                    hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                    hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setTitle("BarbelQ");
                    a.setHeaderText(null);
                    a.setContentText("Update Berhasil");
                    a.show();
                    name = Changename.getText();
                    usia = Changeusia.getText();
                    email = Changeemail.getText();
                    password = Changepassword.getText();
                    tinggi = Changetinggi.getText();
                }
            }  
        }else{
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Data Tidak Boleh Kosong");
            a.show();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        try{
            Stage stage1 = (Stage) btnLogout.getScene().getWindow();
            stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
 ///////////////////////////////////////////////////////////////////////////////////////////////   
    @FXML
    void handleTambah(ActionEvent event) throws SQLException {
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(null);
        a.setContentText("Apakah Anda Yakin Menambah Data");
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.OK){
            if(Double.parseDouble(inpBerat.getText()) < 10 || Double.parseDouble(inpBerat.getText()) > 100){
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Data Tidak Valid");
                a.show();
            }else{
               dbModel.InsertOrUpdate("insert into Berat_badan(berat_badan,id_pengguna) values ('" +inpBerat.getText()+ "','"+session+"')");
                inpBerat.setText(""); 
            }  
        }
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////// 
    public void GetUser(int user, String nama1) throws SQLException {
        // TODO Auto-generated method stub
        session = user;
        nama = " " + nama1;
        namaUser.setText(nama);
        namaUser2.setText(nama);
        namaUser3.setText(nama);
        dbModel.rs = dbModel.resultset("select * from DataPengguna where id_pengguna ='" +session+"'");
        if (dbModel.rs.next()) {
            Changename.setText(dbModel.rs.getString("nama"));
            name = Changename.getText();
            Changeusia.setText(String.valueOf(dbModel.rs.getInt("usia"))); 
            usia = Changeusia.getText();
            Changeemail.setText(dbModel.rs.getString("email")); 
            email = Changeemail.getText();
            Changepassword.setText(dbModel.rs.getString("password"));
            password = Changepassword.getText();
            Changetinggi.setText(dbModel.rs.getString("tinggi")); 
            tinggi = Changetinggi.getText();
        }
        dbModel.rs = dbModel.resultset("select tinggi from DataPengguna where id_pengguna ='" +session+"'");
        if (dbModel.rs.next()) {
            String tinggi = dbModel.rs.getString("tinggi");
            double hasil = Double.parseDouble(tinggi);
            label1.setText(String.valueOf(hasil - 110));
        }
        dbModel.rs.close();
        double hasil = Double.parseDouble(tinggi);
        label1.setText(String.valueOf(hasil - 110));
    }
    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }   
    
    private void bukaField(){
        Changename.disableProperty().set(false);
        Changeusia.disableProperty().set(false);
        Changeemail.disableProperty().set(false);
        Changepassword.disableProperty().set(false);
        Changetinggi.disableProperty().set(false);
        hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
//        Changeemail.setText("");
    }
    
    private void tutupField(){
        Changename.disableProperty().set(true);
        Changeusia.disableProperty().set(true);
        Changeemail.disableProperty().set(true);
        Changepassword.disableProperty().set(true);
        Changetinggi.disableProperty().set(true);
        
    }
    
    @FXML
    private void handleEdit(ActionEvent event) {
        btnEdit.disableProperty().set(true);
        Update.disableProperty().set(false);
        btnCancel.disableProperty().set(false);
        bukaField();
        ambil = email;
        
    }

    @FXML
    private void handlecancel(ActionEvent event) {
        btnEdit.disableProperty().set(false);
        Update.disableProperty().set(true);
        btnCancel.disableProperty().set(true);
        tutupField();
        hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        Changename.setText(name);
        Changeusia.setText(usia);
        Changeemail.setText(email);
        Changepassword.setText(password);
        Changetinggi.setText(tinggi);
    }

    
}

