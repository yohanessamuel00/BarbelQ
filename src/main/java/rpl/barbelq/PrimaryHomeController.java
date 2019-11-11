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
    private String ambil;
    
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
    @FXML
    private Label rataRata, minimal, maksimal;
    
    
    @FXML
    private void btnUpdate(ActionEvent event) throws SQLException {
        if(!Changename.getText().isEmpty() && !Changeusia.getText().isEmpty() && !Changeemail.getText().isEmpty() && !Changepassword.getText().isEmpty() && !Changetinggi.getText().isEmpty()){
            if(Double.parseDouble(Changetinggi.getText()) < 145 || Double.parseDouble(Changetinggi.getText()) > 200){
                bantuAlert("Data Tidak Valid", "Min Tinggi = 145 cm dan Max Tinggi = 200");
            }else{
                dbModel.InsertOrUpdate("update DataPengguna set nama= '" +Changename.getText()+ "',password= '" +Changepassword.getText()+ "',usia= '" +Changeusia.getText()+ "',tinggi= '" +Changetinggi.getText()+ "' where id_pengguna =  "+session+"");
                if(ambil.equals(Changeemail.getText())){
                    bantuUpdate();
                }else{
                    if(cekEmail(Changeemail)){
                        bantuAlert(null, "Email Sudah Digunakan");
                    }else{
                        dbModel.InsertOrUpdate("update DataPengguna set email ='"+Changeemail.getText()+"'where id_pengguna =  "+session+"");
                        bantuUpdate();
                    }
                }
                updateTinggi();
            }  
        }else{
            bantuAlert(null, "Data Tidak Boleh Kosong");
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
      
    @FXML
    void handleTambah(ActionEvent event) throws SQLException {
        if(inpBerat.getText().isEmpty()){
            bantuAlert(null, "Data Masih kosong");
        }else{
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Apakah Anda Yakin Menambah Data");
            Optional<ButtonType> result = a.showAndWait();
            if(result.get() == ButtonType.OK){
                if(Double.parseDouble(inpBerat.getText()) < 10 || Double.parseDouble(inpBerat.getText()) > 500){
                    bantuAlert(null, "Data Tidak Valid");
                }else{
                    dbModel.InsertOrUpdate("insert into Berat_badan(berat_badan,id_pengguna) values ('" +inpBerat.getText()+ "','"+session+"')");
                    inpBerat.setText(""); 
                    cekdataBerat();
                    bantuAlert(null, "Data Berhasil Dimasukkan");
                }  
            } 
        }
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
            Changetinggi.setText(String.valueOf(dbModel.rs.getDouble("tinggi"))); 
            tinggi = Changetinggi.getText();
        }
        System.out.println(session);
        dbModel.rs.close();
        updateTinggi();
        cekdataBerat();
    }
    
    private void bantuAlert(String header, String isi){
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(header);
        a.setContentText(isi);
        a.show();
    }
    
    private void updateTinggi(){
        double tampilTinggi = Double.parseDouble(tinggi);
        double hasil = (tampilTinggi - 100) - ((tampilTinggi - 100) * 0.1);
        label1.setText(String.valueOf(hasil));
    }
      
    private void cekdataBerat() throws SQLException{
        dbModel.rs = dbModel.resultset("select avg(berat_badan) as rata, min(berat_badan) as minimal, max(berat_badan) as maksimal from Berat_badan where id_pengguna ='"+session+"'");
        if(dbModel.rs.next()){
            double pembulatan = Math.round((dbModel.rs.getDouble("rata")));
            rataRata.setText(String.valueOf(pembulatan));
            minimal.setText(String.valueOf(dbModel.rs.getDouble("minimal")));
            maksimal.setText(String.valueOf(dbModel.rs.getDouble("maksimal")));
        }

        dbModel.rs.close();
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
    }
    
    private void tutupField(){
        Changename.disableProperty().set(true);
        Changeusia.disableProperty().set(true);
        Changeemail.disableProperty().set(true);
        Changepassword.disableProperty().set(true);
        Changetinggi.disableProperty().set(true);
        
    }
    
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
    
    private void bantuUpdate(){
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
        bantuAlert(null, "Update Berhasil");
        name = Changename.getText();
        usia = Changeusia.getText();
        email = Changeemail.getText();
        password = Changepassword.getText();
        tinggi = Changetinggi.getText();
    }
    
     /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    } 
    
}