package rpl.barbelq;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Date;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
    private String nama,usiaTahun,usiaBulan,Kategori,jKelamin,email,password,tinggi;
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
    private Button btnEdit,btnCancel,Update;
    @FXML
    private Label rataRata, minimal, maksimal;
    @FXML
    private TextField usiaAnakBulan;
    @FXML
    private ComboBox<String> cbKategoriupdate;
    ObservableList<String> options = FXCollections.observableArrayList("Bayi","Anak-Anak","Remaja/Dewasa");
    @FXML
    private ComboBox<String> cbUsiaBayi;
    ObservableList<String> options2 = FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11");
    @FXML
    private ComboBox<String> cbJenisKelamin;
    ObservableList<String> options3 = FXCollections.observableArrayList("Bayi","Anak-Anak","Remaja/Dewasa");
    @FXML
    private HBox hboxNama,hboxKategori,hboxUsiaBayi,hboxUsiaAnakDewasa,hboxJenisKelamin,hboxEmail,hboxPassword,hboxTinggi;
    
     /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbKategoriupdate.setItems(options);
        cbKategoriupdate.setValue("");
        cbUsiaBayi.setItems(options2);
        cbUsiaBayi.setValue("");
        cbJenisKelamin.setItems(options3);
        cbJenisKelamin.setValue("");
    } 
    
    @FXML
    private void btnUpdate(ActionEvent event) throws SQLException {
//        if(!Changename.getText().isEmpty() && !Changeusia.getText().isEmpty() && !Changeemail.getText().isEmpty() && !Changepassword.getText().isEmpty() && !Changetinggi.getText().isEmpty()){
//            if(Double.parseDouble(Changetinggi.getText()) < 145 || Double.parseDouble(Changetinggi.getText()) > 200){
//                bantuAlert("Data Tidak Valid", "Min Tinggi = 145 cm dan Max Tinggi = 200");
//            }else{
//                dbModel.InsertOrUpdate("update DataPengguna set nama= '" +Changename.getText()+ "',password= '" +Changepassword.getText()+ "',usia= '" +Changeusia.getText()+ "',tinggi= '" +Changetinggi.getText()+ "' where id_pengguna =  "+session+"");
//                if(ambil.equals(Changeemail.getText())){
//                    bantuUpdate();
//                }else{
//                    if(cekEmail(Changeemail)){
//                        bantuAlert(null, "Email Sudah Digunakan");
//                    }else{
//                        dbModel.InsertOrUpdate("update DataPengguna set email ='"+Changeemail.getText()+"'where id_pengguna =  "+session+"");
//                        bantuUpdate();
//                    }
//                }
//                hitungBeratIdeal();
//            }  
//        }else{
//            bantuAlert(null, "Data Tidak Boleh Kosong");
//        }
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
                    dbModel.rs = dbModel.resultset("select tanggal from Berat_badan where id_pengguna ='" +session+"'");
                    Date d = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                    while(dbModel.rs.next()) {
                        if(ft.format(d).equals(dbModel.rs.getString("tanggal"))){
                            dbModel.InsertOrUpdate("update Berat_Badan set berat_badan = '" +inpBerat.getText()+ "' where  id_pengguna = '"+session+"' and tanggal = date('now')");
                            inpBerat.setText(""); 
                            cekdataBerat();
                            bantuAlert(null, "Data Berhasil Dimasukkan");
                        }else{
                            dbModel.InsertOrUpdate("insert into Berat_badan(berat_badan,id_pengguna,tanggal) values ('" +inpBerat.getText()+ "','"+session+"',date('now'))");
                            inpBerat.setText(""); 
                            cekdataBerat();
                            bantuAlert(null, "Data Berhasil Dimasukkan");
                        }
                    }
                    dbModel.rs.close();
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
        hboxNama.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxKategori.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxUsiaBayi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxUsiaAnakDewasa.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxJenisKelamin.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxEmail.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxPassword.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxTinggi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        Changename.setText(nama);
        cbKategoriupdate.setValue(Kategori);
        if(Kategori.equals("Bayi")){
            cbUsiaBayi.setValue(usiaBulan);
        }if(Kategori.equals("Anak-Anak")){
            Changeusia.setText(usiaTahun);
            usiaAnakBulan.setText(usiaBulan);
        }if(Kategori.equals("Remaja/Dewasa")){
            Changeusia.setText(usiaTahun);
        }
        cbJenisKelamin.setValue(jKelamin);
        Changeemail.setText(email);
        Changepassword.setText(password);
        Changetinggi.setText(tinggi);
    }
    
    @FXML
    private void handleKategori(ActionEvent event) {
        if(cbKategoriupdate.getValue().equals("Bayi")){
            hboxUsiaBayi.visibleProperty().set(true);
            hboxUsiaAnakDewasa.visibleProperty().set(false);
            usiaAnakBulan.visibleProperty().set(false);
        }if(cbKategoriupdate.getValue().equals("Anak-Anak")){
            hboxUsiaBayi.visibleProperty().set(false);
            hboxUsiaAnakDewasa.visibleProperty().set(true);
            usiaAnakBulan.visibleProperty().set(true);
        }if(cbKategoriupdate.getValue().equals("Remaja/Dewasa")){
            hboxUsiaBayi.visibleProperty().set(false);
            hboxUsiaAnakDewasa.visibleProperty().set(true);
            usiaAnakBulan.visibleProperty().set(false);
        }
    }
    
    private void handleGrafix(ActionEvent event) {
//        grafikBerat.getData().clear();
//        XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
//        series1.getData().add(new XYChart.Data<String,Number>("Aldo",200));
//        series1.getData().add(new XYChart.Data<String,Number>("Yosam",300));
//        series1.getData().add(new XYChart.Data<String,Number>("Krisna",550));
//        grafikBerat.getData().add(series1);
    }
  
    
//////////////////////////////////////////////////////////////////////////////////////////////////// 
    public void GetUser(int user, String nama1) throws SQLException {
        // TODO Auto-generated method stub
        session = user;
        namaUser.setText(" " + nama1);
        namaUser2.setText(" " + nama1);
        namaUser3.setText(" " + nama1);
        Changename.setText(nama1);
        nama = nama1;
        dbModel.rs = dbModel.resultset("select * from DataPengguna where id_pengguna ='" +session+"'");
        if (dbModel.rs.next()) {
            cbKategoriupdate.setValue(dbModel.rs.getString("kategori"));
            if(cbKategoriupdate.getValue().equals("Bayi")){
                hboxUsiaBayi.visibleProperty().set(true);
                cbUsiaBayi.setValue(dbModel.rs.getString("usiaBulan"));
            }
            if(cbKategoriupdate.getValue().equals("Anak-Anak")){
                hboxUsiaAnakDewasa.visibleProperty().set(true);
                usiaAnakBulan.visibleProperty().set(true);
                Changeusia.setText(dbModel.rs.getString("usiaTahun"));
                usiaAnakBulan.setText(dbModel.rs.getString("usiaBulan"));
            }
            if(cbKategoriupdate.getValue().equals("Remaja/Dewasa")){
                hboxUsiaAnakDewasa.visibleProperty().set(true);
                Changeusia.setText(dbModel.rs.getString("usiaTahun"));
            }
            Kategori = dbModel.rs.getString("kategori");
            usiaTahun = dbModel.rs.getString("usiaTahun");
            usiaBulan = dbModel.rs.getString("usiaBulan");
            cbJenisKelamin.setValue(dbModel.rs.getString("jenis_kelamin"));
            jKelamin =  cbJenisKelamin.getValue();
            Changeemail.setText(dbModel.rs.getString("email")); 
            email = Changeemail.getText();
            Changepassword.setText(dbModel.rs.getString("password"));
            password = Changepassword.getText();
            Changetinggi.setText(String.valueOf(dbModel.rs.getDouble("tinggi"))); 
            tinggi = Changetinggi.getText();
        }
        System.out.println(session);
        dbModel.rs.close();
        hitungBeratIdeal();
        cekdataBerat();
    }
    
    private void bantuAlert(String header, String isi){
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(header);
        a.setContentText(isi);
        a.show();
    }
    
    private void hitungBeratIdeal(){
        if(Kategori.equals("Bayi")){
            double ubahBulan = Double.parseDouble(usiaBulan);
            double hasil = (ubahBulan/2)+4;
            label1.setText(String.valueOf(hasil));
        }
        if(Kategori.equals("Anak-Anak")){
            int totalUsia = (Integer.parseInt(usiaTahun)*12) + Integer.parseInt(usiaBulan);
            int rumus = (totalUsia*2) + 96;
            int usiaTahun = rumus/12;
            int usiaBulan = rumus%12;
            String hasil = String.valueOf(usiaTahun)+"."+String.valueOf(usiaBulan);
            label1.setText(hasil);
            
        }
        if(Kategori.equals("Remaja/Orang Dewasa") && jKelamin.equals("Laki-Laki")){
            double tampilTinggi = Double.parseDouble(tinggi);
            double hasil = (tampilTinggi - 100) - ((tampilTinggi - 100) * 0.1);
            label1.setText(String.valueOf(hasil));
        }
        if(Kategori.equals("Remaja/Orang Dewasa") &&jKelamin.equals("Perempuan")){
            double tampilTinggi = Double.parseDouble(tinggi);
            double hasil = (tampilTinggi - 100) - ((tampilTinggi - 100) * 0.1);
            label1.setText(String.valueOf(hasil));
        }       
    }
//      
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
        cbKategoriupdate.disableProperty().set(false);
        Changeusia.disableProperty().set(false);
        cbUsiaBayi.disableProperty().set(false);
        usiaAnakBulan.disableProperty().set(false);
        cbJenisKelamin.disableProperty().set(false);
        Changeemail.disableProperty().set(false);
        Changepassword.disableProperty().set(false);
        Changetinggi.disableProperty().set(false);
        hboxNama.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxKategori.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxUsiaBayi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxUsiaAnakDewasa.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxJenisKelamin.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxEmail.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxPassword.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxTinggi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
    }
    
    private void tutupField(){
        Changename.disableProperty().set(true);
        cbKategoriupdate.disableProperty().set(true);
        Changeusia.disableProperty().set(true);
        cbUsiaBayi.disableProperty().set(true);
        usiaAnakBulan.disableProperty().set(true);
        cbJenisKelamin.disableProperty().set(true);
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
//    
//    private void bantuUpdate(){
//        btnEdit.disableProperty().set(false);
//        Update.disableProperty().set(true);
//        btnCancel.disableProperty().set(true);
//        namaUser.setText(Changename.getText());
//        namaUser2.setText(Changename.getText());
//        namaUser3.setText(Changename.getText());
//        tutupField();
//        hbox1.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
//        hbox2.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
//        hbox3.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
//        hbox4.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
//        hbox5.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
//        bantuAlert(null, "Update Berhasil");
//        name = Changename.getText();
//        usia = Changeusia.getText();
//        email = Changeemail.getText();
//        password = Changepassword.getText();
//        tinggi = Changetinggi.getText();
//    }
//    
//    
//
//
//    


    
}