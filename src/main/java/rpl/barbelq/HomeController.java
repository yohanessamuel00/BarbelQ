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
    
    private int session;
    
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
        try{
            int cek = 0;
            int id = 0;
            String berat = inputBerat.getText();
            String tinggi = inputTinggi.getText();
            dbModel.InsertOrUpdate("update DataPengguna set tinggi = '" +tinggi+ "' where id_pengguna =  "+session+"");
            dbModel.InsertOrUpdate("update Berat_badan set berat_badan = '" +berat+ "' where id_pengguna =  "+session+"");
            if (cek > 0) {
                System.out.println("Data Berhasil Masuk");
            }
            dbModel.statement.close();
            Stage stage1 = (Stage) btnSubmit.getScene().getWindow();
            stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrimaryHome.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            PrimaryHomeController primaryHome = (PrimaryHomeController)fxmlLoader.getController();
            primaryHome.GetUser(session);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    
    } 
    
    public void GetUser(int user) {
        // TODO Auto-generated method stub
        session = user;
    }
    
    @FXML
    void inputBerat(ActionEvent event) {

    }

    @FXML
    void inputTinggi(ActionEvent event) {

    }
}