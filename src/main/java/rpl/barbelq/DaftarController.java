/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author yohan
 */
public class DaftarController {
    
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
    void btnDaftar(ActionEvent event) {
        Connection connection = SqliteConnection.getInstance().Connector();
        try {
        String nama = inputNama.getText();
        String email = inputEmail.getText();
        String password = inputPassword.getText();
        
        Statement statement = connection.createStatement();
        
        int status = statement.executeUpdate("insert into DataPengguna (nama, email, password)" + 
                " values ('"+nama+"','"+email+"','"+password+"')");
       
        if (status > 0) {
            System.out.println("user registered");
        }
       
       } catch (SQLException e) {
            e.printStackTrace();
        }     
    }
    
    @FXML
    void btnLogin(ActionEvent event) {
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
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
    
    
}
