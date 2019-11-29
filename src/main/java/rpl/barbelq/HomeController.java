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
    private String namaUser,kategori;
    
    @FXML
    private TextField inputTinggi,inputBerat;
    @FXML
    private Button btnSubmit;
    
    private void cekKategori() throws SQLException{
        dbModel.resultset("select kategori from DataPengguna where id_pengguna = "+session+"");
        if(dbModel.rs.next()){
            kategori = dbModel.rs.getString("kategori");
        }
        dbModel.rs.close();   
    }
    
    private boolean cekData(int batasBawah, int batasAtas,int minberat, int maxberat){
        boolean cek;
        if(Double.parseDouble(inputTinggi.getText()) < batasBawah || Double.parseDouble(inputTinggi.getText()) > batasAtas || Double.parseDouble(inputBerat.getText()) < minberat || Double.parseDouble(inputBerat.getText()) > maxberat ){
            bantuAlert("Data Tidak Valid","Min Tinggi = "+batasBawah+" cm Dan Max Tinggi = "+batasAtas+" cm\nMin Berat = "+minberat+" kg Dan Max Berat = "+maxberat+" kg" );
            cek = true;
        }else{
            dbModel.InsertOrUpdate("update DataPengguna set tinggi = '" +inputTinggi.getText()+ "' where id_pengguna =  "+session+"");
            dbModel.InsertOrUpdate("update Berat_badan set berat_badan = '" +inputBerat.getText()+ "' where id_pengguna =  "+session+"");
            cek = false;
        }
        return cek;
    }
    
    @FXML
    void handleSubmit(ActionEvent event) throws SQLException, IOException {
        if(inputTinggi.getText().isEmpty() || inputBerat.getText().isEmpty()){
            bantuAlert(null, "Field Tidak Boleh Kosong");
        }else{
            cekKategori();
            boolean cek = true;
            if(kategori.equals("Bayi")){
                cek = cekData(50,78,2,5);
            }if(kategori.equals("Anak-Anak")){
                cek = cekData(69,139,6,23);
            }if(kategori.equals("Remaja/Dewasa")){
                cek = cekData(140,200,23,200);
            }
            if(!cek){
                Stage stage1 = (Stage) btnSubmit.getScene().getWindow();
                stage1.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrimaryHome.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                PrimaryHomeController primaryHome = (PrimaryHomeController)fxmlLoader.getController();
                primaryHome.GetUser(session,namaUser);
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
            }
            
        }   
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    
    } 
    
    private void bantuAlert(String header, String isi){
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(header);
        a.setContentText(isi);
        a.show();
    }
    
    public void GetUser(int user, String nama) {
        // TODO Auto-generated method stub
        session = user;
        namaUser = nama;
    }
 
}