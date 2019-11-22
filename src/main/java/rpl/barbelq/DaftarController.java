/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author yohan
 */
public class DaftarController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
    private String usia="0",bulan="0";
    @FXML
    private Button btnDaftar;
    
    @FXML
    private Hyperlink btnLogin;

    @FXML
    private TextField inputEmail,inputNama,inputUsia,usiaBulanRemaja;

    @FXML
    private PasswordField inputPassword;
    
    @FXML
    private HBox usiaRemajadanDewasa,usiaBayi;
    
    @FXML
    private ComboBox<String> cbUsiabayi;
    ObservableList<String> options = FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11");
    @FXML
    private ComboBox<String> cbJenisKelamin;
    ObservableList<String> options2 = FXCollections.observableArrayList("Laki-Laki","Perempuan");
    @FXML
    private ComboBox<String> cbKategori;
    ObservableList<String> options3 = FXCollections.observableArrayList("Bayi","Anak-Anak","Remaja/Dewasa");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cbUsiabayi.setItems(options);
       cbUsiabayi.setValue("");
       cbJenisKelamin.setItems(options2);
       cbJenisKelamin.setValue("");
       cbKategori.setItems(options3);
       cbKategori.setValue("");
    }
    
    private boolean cekEmail(){
        int id = 0;
        try{
            dbModel.rs =dbModel.resultset("select id_pengguna from DataPengguna where email ='" +inputEmail.getText()+"'");
            while(dbModel.rs.next()) {
                id = dbModel.rs.getInt("id_pengguna");
            }
            dbModel.rs.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }   
        return id == 0;
    }
    private boolean cekinputUsia(){
        if(!cbUsiabayi.getValue().isEmpty()){
            bulan = cbUsiabayi.getValue();
        }
        if(cbKategori.getValue().equals("Anak-Anak") && !inputUsia.getText().isEmpty()){
            usia = inputUsia.getText();
            bulan = usiaBulanRemaja.getText();
        }
        if(cbKategori.getValue().equals("Remaja/Dewasa") && !inputUsia.getText().isEmpty() ){
            usia = inputUsia.getText();
        }
        return (cbUsiabayi.getValue().isEmpty() || inputUsia.getText().isEmpty());
    }
    
    private boolean cekField(){
        return !(inputNama.getText().isEmpty() || inputEmail.getText().isEmpty() || inputPassword.getText().isEmpty() || cbJenisKelamin.getValue().isEmpty() || cbKategori.getValue().isEmpty());
    }
    
    
    @FXML
    void btnDaftar(ActionEvent event) throws IOException, SQLException {
        try {
            if(cekEmail() && cekField() && cekinputUsia()){
                int id = 0;
                String namaUser = "";
                dbModel.InsertOrUpdate("insert into DataPengguna(nama, email, password,kategori,usiaTahun,usiaBulan,jenis_kelamin,level) values('"+inputNama.getText()+"', '"+inputEmail.getText()+"','"+inputPassword.getText()+"' ,'"+cbKategori.getValue()+"','"+usia+"' ,'"+bulan+"' ,'"+cbJenisKelamin.getValue()+"', '1')");
                dbModel.rs = dbModel.resultset("select id_pengguna, nama from DataPengguna where email ='" +inputEmail.getText()+"'");
                while (dbModel.rs.next()) {
                   id = dbModel.rs.getInt("id_pengguna");
                   namaUser = dbModel.rs.getString("nama");
                }
                dbModel.rs.close();
                int cek  = dbModel.InsertOrUpdate("insert into Berat_badan (id_pengguna) values ('"+id+"')");
                if (cek > 0) {
                    System.out.println("user registered");
                }
                Stage stage1 = (Stage) btnDaftar.getScene().getWindow();
                stage1.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                HomeController userController = (HomeController)fxmlLoader.getController();
                userController.GetUser(id,namaUser);
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
                dbModel.statement.close(); 
            }else{
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("Register Gagal");
                a.setHeaderText(null);
                if(cekField()== false) a.setContentText("Field Tidak Boleh Kosong");
                else a.setContentText("Email telah terdaftar");
                
                // show the dialog 
                a.showAndWait(); 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    @FXML
    void btnLogin(ActionEvent event) {
        try {
            Stage stage1 = (Stage) btnDaftar.getScene().getWindow();
            stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleKategori(ActionEvent event) {
        if(cbKategori.getValue().equals("Bayi")){
            inputUsia.setText("");
            usiaBayi.visibleProperty().set(true);
            usiaRemajadanDewasa.visibleProperty().set(false);
            usiaBulanRemaja.visibleProperty().set(false); 
        }
        if(cbKategori.getValue().equals("Anak-Anak")){
            usiaBayi.visibleProperty().set(false);
            usiaRemajadanDewasa.visibleProperty().set(true);
            usiaBulanRemaja.visibleProperty().set(true);
            cbUsiabayi.setValue("");
        }
        if(cbKategori.getValue().equals("Remaja/Dewasa")){
            usiaBayi.visibleProperty().set(false);
            usiaRemajadanDewasa.visibleProperty().set(true);
            usiaBulanRemaja.visibleProperty().set(false);
            cbUsiabayi.setValue("");
        }
    }

}