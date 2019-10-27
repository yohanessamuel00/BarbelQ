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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class UserController implements Initializable {
    
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
    void btnSubmit(ActionEvent event) throws IOException, SQLException {
        Connection connection = SqliteConnection.Connector();
        try {
            String berat = inputBerat.getText();
            String tinggi = inputTinggi.getText();
            Statement statement = connection.createStatement();
            
            int status2 = statement.executeUpdate("update DataPengguna set tinggi = '" +tinggi+ "'" + 
                " where id_pengguna =  "+session+"");
            int status = statement.executeUpdate("update Berat_badan set berat_badan = '" +berat+ "'" + 
                " where id_pengguna =  "+session+"");
            
            if (status > 0 && status2 > 0) {
                System.out.println("Data Berhasil Masuk");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
