/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

/**
 *
 * @author yohan
 */

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
import javafx.scene.control.*;
import javafx.stage.Stage;

public class HomeController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
    private int session;
    private String namaUser;
    
    @FXML
    private TextField inputTinggi;
    

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Label lbl1;

    @FXML
    private TextField inputBerat;
    
    @FXML
    void handleSubmit(ActionEvent event) throws SQLException, IOException {
        if(Integer.parseInt(inputTinggi.getText()) < 80 || Integer.parseInt(inputTinggi.getText()) > 250 || Integer.parseInt(inputBerat.getText()) < 10 || Integer.parseInt(inputBerat.getText()) > 500 ){
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText("Data Tidak Valid");
            a.setContentText("Min Tinggi = 80 cm Dan Max Tinggi = 250 cm\nMin Berat = 10 kg Dan Max Berat = 500 kg");
            a.show();    
        }else{
            try{
                dbModel.InsertOrUpdate("update DataPengguna set tinggi = '" +inputTinggi.getText()+ "' where id_pengguna =  "+session+"");
                dbModel.InsertOrUpdate("update Berat_badan set berat_badan = '" +inputBerat.getText()+ "' where id_pengguna =  "+session+"");
                Stage stage1 = (Stage) btnSubmit.getScene().getWindow();
                stage1.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrimaryHome.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                PrimaryHomeController primaryHome = (PrimaryHomeController)fxmlLoader.getController();
                primaryHome.GetUser(session,namaUser);
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
            }catch(SQLException e){
                System.out.println(e.getMessage());
            } 
        }  
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    
    } 
    
    public void GetUser(int user, String nama) {
        // TODO Auto-generated method stub
        session = user;
        namaUser = nama;
    }
    
    @FXML
    void inputBerat(ActionEvent event) {

    }

    @FXML
    void inputTinggi(ActionEvent event) {

    }
}