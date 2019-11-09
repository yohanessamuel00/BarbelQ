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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author yohan
 */
public class DaftarController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
    @FXML
    private Button btnDaftar;
    
    @FXML
    private Hyperlink btnLogin;

    @FXML
    private TextField inputEmail;

    @FXML
    private TextField inputNama;

    @FXML
    private PasswordField inputPassword;
    
    @FXML
    private TextField inputUsia;
    
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
    
    private boolean cekField(){
        return !(inputNama.getText().equals("") || inputUsia.getText().equals("") || inputEmail.getText().equals("") || inputPassword.getText().equals(""));
    }
    
    
    @FXML
    void btnDaftar(ActionEvent event) throws IOException, SQLException {
        try {
            if(cekEmail() && cekField()){
                int id = 0;
                String namaUser = "";
                dbModel.InsertOrUpdate("insert into DataPengguna (nama, email, password,usia) values ('"+inputNama.getText()+"','"+inputEmail.getText()+"','"+inputPassword.getText()+"','"+inputUsia.getText()+"')");
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
    void inputNama(ActionEvent event) {

    }

    @FXML
    void inputEmail(ActionEvent event) {

    }

    @FXML
    void inputPassword(ActionEvent event) {

    }

    @FXML
    void inputUsia(ActionEvent event) {

    } 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
}