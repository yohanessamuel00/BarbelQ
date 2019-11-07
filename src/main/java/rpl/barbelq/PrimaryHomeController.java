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
    private String cek,cek1;
    
    @FXML
    private TextField Changename;
    @FXML
    private TextField Changeusia;
    @FXML
    private TextField Changeemail;
    @FXML
    private PasswordField Changepassword;
    @FXML
    private Button btnLogout;
     @FXML
    private Button btnUpdate;
    @FXML
    private TextField Changetinggi;
    @FXML
    private TextField inpBerat;
    @FXML
    private Label label1;
    @FXML
    private Label namaUser;
    @FXML
    private Label namaUser2;
    @FXML
    private Label namaUser3;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private HBox hbox3;
    @FXML
    private HBox hbox4;
    @FXML
    private HBox hbox5;
    @FXML
    private Hyperlink edit1;
    @FXML
    private Hyperlink cancel1;
    @FXML
    private Hyperlink edit2;
    @FXML
    private Hyperlink cancel2;
    @FXML
    private Hyperlink edit3;
    @FXML
    private Hyperlink cancel3;
    @FXML
    private Hyperlink edit4;
    @FXML
    private Hyperlink cancel4;
    @FXML
    private Hyperlink edit5;
    @FXML
    private Hyperlink cancel5;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }   
    
    private void coba1(String a, String b){
        if(cek.equals("nama")){
             Changename.setText(b);
             Changename.disableProperty().set(true);
             cancel1.disableProperty().set(true);
             hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        }
    }
    
    private String coba(String a){
        if(cek.equals("nama")){
             return Changename.getText();
        }else{
            return "";
        }
    }
    
    @FXML
    private void handleUpdate(ActionEvent event) {
        if(!Changename.getText().isEmpty() || !Changeusia.getText().isEmpty() || !Changeemail.getText().isEmpty() || !Changepassword.getText().isEmpty() || !Changetinggi.getText().isEmpty()){
            dbModel.InsertOrUpdate("update DataPengguna set "+cek+" = '" +coba(cek)+ "' where id_pengguna =  "+session+"");
            coba1(cek,coba(cek));
        }
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(null);
        a.setContentText("Update Berhasil");
        // show the dialog 
        a.show();
        btnUpdate.disableProperty().set(true);
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
            dbModel.InsertOrUpdate("insert into Berat_badan(berat_badan,id_pengguna) values ('" +inpBerat.getText()+ "','"+session+"')");
            inpBerat.setText("");
        }
    }
    
    @FXML
    void handleedit1(ActionEvent event) {
        Changename.setText("");
        Changename.disableProperty().set(false);
        cancel1.disableProperty().set(false);
        hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        btnUpdate.disableProperty().set(false);
        edit2.disableProperty().set(true);
        edit3.disableProperty().set(true);
        edit4.disableProperty().set(true);
        edit5.disableProperty().set(true);
        cek = "nama";
        
    }
    
    @FXML
    void handlecancel1(ActionEvent event) {
        Changename.setText(name);
        Changename.disableProperty().set(true);
        cancel1.disableProperty().set(true);
        hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        btnUpdate.disableProperty().set(true);
        edit2.disableProperty().set(false);
        edit3.disableProperty().set(false);
        edit4.disableProperty().set(false);
        edit5.disableProperty().set(false);
    }

    @FXML
    void handleedit2(ActionEvent event) {
        Changeusia.setText("");
        Changeusia.disableProperty().set(false);
        cancel2.disableProperty().set(false);
        hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        btnUpdate.disableProperty().set(false);
        edit1.disableProperty().set(true);
        edit3.disableProperty().set(true);
        edit4.disableProperty().set(true);
        edit5.disableProperty().set(true);
        cek = "usia";
    }
    
    @FXML
    void handlecancel2(ActionEvent event) {
        Changeusia.setText(usia);
        Changeusia.disableProperty().set(true);
        cancel2.disableProperty().set(true);
        hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        btnUpdate.disableProperty().set(true);
        edit1.disableProperty().set(false);
        edit3.disableProperty().set(false);
        edit4.disableProperty().set(false);
        edit5.disableProperty().set(false);
    }
    
    @FXML
    void handleedit3(ActionEvent event) {
        Changeemail.setText("");
        Changeemail.disableProperty().set(false);
        cancel3.disableProperty().set(false);
        hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        btnUpdate.disableProperty().set(false);
        edit1.disableProperty().set(true);
        edit2.disableProperty().set(true);
        edit4.disableProperty().set(true);
        edit5.disableProperty().set(true);
        cek = "email";
    }
    
    @FXML
    void handlecancel3(ActionEvent event) {
        Changeemail.setText(email);
        Changeemail.disableProperty().set(true);
        cancel3.disableProperty().set(true);
        hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        btnUpdate.disableProperty().set(true);
        edit1.disableProperty().set(false);
        edit2.disableProperty().set(false);
        edit4.disableProperty().set(false);
        edit5.disableProperty().set(false);
    }
    
    @FXML
    void handleedit4(ActionEvent event) {
        Changepassword.setText("");
        Changepassword.disableProperty().set(false);
        cancel4.disableProperty().set(false);
        hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        btnUpdate.disableProperty().set(false);
        edit1.disableProperty().set(true);
        edit2.disableProperty().set(true);
        edit3.disableProperty().set(true);
        edit5.disableProperty().set(true);
        cek = "password";
    }
    
    @FXML
    void handlecancel4(ActionEvent event) {
        Changepassword.setText(password);
        Changepassword.disableProperty().set(true);
        cancel4.disableProperty().set(true);
        hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        btnUpdate.disableProperty().set(true);
        edit1.disableProperty().set(false);
        edit2.disableProperty().set(false);
        edit3.disableProperty().set(false);
        edit5.disableProperty().set(false);
    }
    
    @FXML
    void handleedit5(ActionEvent event) {
        Changetinggi.setText("");
        Changetinggi.disableProperty().set(false);
        cancel5.disableProperty().set(false);
        hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        btnUpdate.disableProperty().set(false);
        edit1.disableProperty().set(true);
        edit2.disableProperty().set(true);
        edit3.disableProperty().set(true);
        edit4.disableProperty().set(true);
        cek = "tinggi";
    }
    
    @FXML
    void handlecancel5(ActionEvent event) {
        Changetinggi.setText(tinggi);
        Changetinggi.disableProperty().set(true);
        cancel5.disableProperty().set(true);
        hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        btnUpdate.disableProperty().set(true);
        edit1.disableProperty().set(true);
        edit2.disableProperty().set(true);
        edit3.disableProperty().set(true);
        edit4.disableProperty().set(true);
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
        String tinggi = "";
        dbModel.rs = dbModel.resultset("select tinggi from DataPengguna where id_pengguna ='" +session+"'");
        if (dbModel.rs.next()) {
            tinggi = dbModel.rs.getString("tinggi");
        }
        dbModel.rs.close();
        double hasil = Double.parseDouble(tinggi);
        label1.setText(String.valueOf(hasil - 110));
    }
    
}

