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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ALDO
 */
public class PrimaryHomeController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    Reminder reminder = new Reminder();

    private int session;
    private String nama,usiaTahun="0",usiaBulan="0",Kategori,jKelamin,email,password,tinggi;
    private double berat;
    private String ambil;
    
    @FXML
    private PasswordField Changepassword;
    @FXML
    private Label beratIdeal,namaUser, namaUser2, namaUser3,rataRata, minimal, maksimal,Mulai,Baru,Sisa,Saran;
    @FXML
    private Button btnEdit,btnCancel,Update,btnLogout;
    @FXML
    private HBox hboxNama,hboxUsia,hboxKategori,hboxJenisKelamin,hboxEmail,hboxPassword,hboxTinggi;
    @FXML
    private TextField Changename,ChangeusiaTahun,ChangeusiaBulan,Changeemail,Changetinggi,inpBerat;
    @FXML
    private ComboBox<String> ChangecbKategori,ChangecbJenisKelamin,cbTampilData;
    ObservableList<String> options = FXCollections.observableArrayList("Bayi","Anak-Anak","Remaja/Dewasa");
    ObservableList<String> options2 = FXCollections.observableArrayList("Laki-Laki","Perempuan");
    ObservableList<String> options3 = FXCollections.observableArrayList("5","10","15","All");
    @FXML
    private LineChart<String,Double> grafikBerat;
    XYChart.Series<String,Double> simpanSemuaData;
    XYChart.Series<String,Double> tampilGrafik;
    XYChart.Series<String,Double> tampilGrafikBeratIdeal;
    
     /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ChangecbKategori.setItems(options);
        ChangecbKategori.setValue("");
        ChangecbJenisKelamin.setItems(options2);
        ChangecbJenisKelamin.setValue("");
        cbTampilData.setItems(options3);
        cbTampilData.setValue("5");
    } 
    
    @FXML
    void btnUpdate(ActionEvent event) throws SQLException {
        if(!Changename.getText().isEmpty() && !ChangecbKategori.getValue().isEmpty() && cekinputUsia() && !ChangecbJenisKelamin.getValue().isEmpty() && !Changeemail.getText().isEmpty() && !Changepassword.getText().isEmpty() && !Changetinggi.getText().isEmpty()){
            if(!cekTinggi1() && !cekTinggi2()){
                if(ubahkategori()){
                    dbModel.InsertOrUpdate("update DataPengguna set nama= '" +Changename.getText()+ "',kategori = '"+ChangecbKategori.getValue()+"',usiaTahun = '"+ChangeusiaTahun.getText()+"',usiaBulan = '"+ChangeusiaBulan.getText()+"',jenis_kelamin = '"+ChangecbJenisKelamin.getValue()+"',password= '" +Changepassword.getText()+ "',tinggi= '" +Changetinggi.getText()+ "' where id_pengguna =  "+session+"");
                    if(ambil.equals(Changeemail.getText())){
                        bantuUpdate();
                    }else{
                        if(cekEmail(Changeemail) || Double.parseDouble(Changetinggi.getText()) < Double.parseDouble(tinggi)){
                            bantuAlert(null, "Email Sudah Digunakan");
                        }else{
                            dbModel.InsertOrUpdate("update DataPengguna set email ='"+Changeemail.getText()+"'where id_pengguna =  "+session+"");
                            bantuUpdate();
                        }
                    }
                    hitungBeratIdeal();
                    tampilGrafik();
                }
            }
        }else{
            bantuAlert(null, "Data Tidak Boleh Kosong");
        }
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
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
                if(!cekBatasanBerat(Kategori)){
                    Date d = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                    dbModel.rs = dbModel.resultset("select tanggal from Berat_badan where id_pengguna ='" +session+"' and tanggal = '"+ft.format(d)+"'");
                    if(dbModel.rs.next()) {
                        dbModel.InsertOrUpdate("update Berat_Badan set berat_badan = '" +inpBerat.getText()+ "' where  id_pengguna = '"+session+"' and tanggal = '"+ft.format(d)+"'");
                        inpBerat.setText(""); 
                        cekdataBerat();
                        bantuAlert(null, "Data Berhasil Dimasukkan");
                    }else{
                        dbModel.InsertOrUpdate("insert into Berat_badan(berat_badan,id_pengguna,tanggal) values ('" +inpBerat.getText()+ "','"+session+"',date('now','localtime'))");
                        inpBerat.setText(""); 
                        cekdataBerat();
                        
                        bantuAlert(null, "Data Berhasil Dimasukkan");
                    }
                    dbModel.rs.close();
                    cbTampilData.setValue("5");
                    tampilGrafik();
                    if(Kategori.equals("Remaja/Dewasa")){
                        reminder.ambilDataBerat(Double.parseDouble(beratIdeal.getText()), berat);
                        reminder.aturSaran(Saran);
                    } 
                } 
            } 
        }
    }
    
    @FXML
    void handleEdit(ActionEvent event) {
        btnEdit.disableProperty().set(true);
        Update.disableProperty().set(false);
        btnCancel.disableProperty().set(false);
        bukaField();
        ambil = email;
    }

    @FXML
    void handlecancel(ActionEvent event) {
        btnEdit.disableProperty().set(false);
        Update.disableProperty().set(true);
        btnCancel.disableProperty().set(true);
        tutupField();
        hboxNama.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxKategori.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxUsia.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxJenisKelamin.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxEmail.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxPassword.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxTinggi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        Changename.setText(nama);
        ChangecbKategori.setValue(Kategori);
        if(Kategori.equals("Bayi") || Kategori.equals("Anak-Anak") || Kategori.equals("Remaja/Dewasa")){
            ChangeusiaTahun.setText(usiaTahun);
            ChangeusiaBulan.setText(usiaBulan);
        }
        ChangecbJenisKelamin.setValue(jKelamin);
        Changeemail.setText(email);
        Changepassword.setText(password);
        Changetinggi.setText(tinggi);
    }
    
    @FXML
    void handleKategori(ActionEvent event) {
        if(ChangecbKategori.getValue().equals("Bayi")){
            cekKategori(true,false,"0","");  
        }if(ChangecbKategori.getValue().equals("Anak-Anak")){
            cekKategori(false,false,"","0");
        }if(ChangecbKategori.getValue().equals("Remaja/Dewasa")){
            cekKategori(false,true,"","0");
        }
    }
    
    @FXML
    void handleTampilData(ActionEvent event) throws SQLException {
        tampilGrafik = new XYChart.Series();
        tampilGrafik.setName("Berat User");
        grafikBerat.getData().clear();
        int total = simpanSemuaData.getData().size();
        if(total<5){
            for(int i = total-total;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(total);
        }
        else if(cbTampilData.getValue().equals("5")){
            for(int i = total-5;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(5);
        }
        else if(cbTampilData.getValue().equals("10") && total==10){
            for(int i = total-10;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(10);
        }
        else if(cbTampilData.getValue().equals("15") && total==15){
            for(int i = total-15;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(15);
        }
        else if(total>5){
            for(int i = total-total;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(total);
        }

        
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
            ChangecbKategori.setValue(dbModel.rs.getString("kategori"));
            Kategori = dbModel.rs.getString("kategori");
            ChangeusiaTahun.setText(dbModel.rs.getString("usiaTahun"));
            usiaTahun = dbModel.rs.getString("usiaTahun");
            ChangeusiaBulan.setText(dbModel.rs.getString("usiaBulan"));
            usiaBulan = dbModel.rs.getString("usiaBulan");
            ChangecbJenisKelamin.setValue(dbModel.rs.getString("jenis_kelamin"));
            jKelamin =  ChangecbJenisKelamin.getValue();
            Changeemail.setText(dbModel.rs.getString("email")); 
            email = Changeemail.getText();
            Changepassword.setText(dbModel.rs.getString("password"));
            password = Changepassword.getText();
            Changetinggi.setText(String.valueOf(dbModel.rs.getDouble("tinggi"))); 
            tinggi = Changetinggi.getText();
        }
        ChangeusiaTahun.disableProperty().set(true);
        ChangeusiaBulan.disableProperty().set(true);
        dbModel.rs.close();
        hitungBeratIdeal();
        cekdataBerat();
        tampilGrafik();
        if(Kategori.equals("Remaja/Dewasa")){
            reminder.tampilSaran(Saran,user);
        }
    }
    
    private void tampilGrafik() throws SQLException{
        simpanSemuaData = new XYChart.Series();
        tampilGrafik = new XYChart.Series();
        tampilGrafik.setName("Berat User");
        grafikBerat.getData().clear();
        dbModel.resultset("select * from Berat_badan where id_pengguna = "+session+"");
        while(dbModel.rs.next()){
            simpanSemuaData.getData().add(new XYChart.Data(dbModel.rs.getString("tanggal"),dbModel.rs.getDouble("berat_badan")));
        }
        dbModel.rs.close();
        int total = simpanSemuaData.getData().size();
        if(total < 5){
            for(int i = total-total;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(total);
        }else{
            for(int i = total-5;i<total;i++){
                tampilGrafik.getData().add(simpanSemuaData.getData().get(i));
            }
            
            grafikBerat.getData().add(tampilGrafik);
            garisBeratBadanIdeal(5);
        } 
    }
    
    private void garisBeratBadanIdeal(int total){
        tampilGrafikBeratIdeal = new XYChart.Series();
        tampilGrafikBeratIdeal.setName("Berat Ideal");
        for(int i=0;i<total;i++){
            tampilGrafikBeratIdeal.getData().add(new XYChart.Data(tampilGrafik.getData().get(i).getXValue(),Double.parseDouble(beratIdeal.getText())));
        }
        grafikBerat.getData().add(tampilGrafikBeratIdeal);
    }

    private void hitungBeratIdeal() throws SQLException{
        if(Kategori.equals("Bayi")){
            double ubahBulan = Double.parseDouble(usiaBulan);
            double hasil = (ubahBulan/2)+4;
            beratIdeal.setText(String.valueOf(hasil));
        }
        if(Kategori.equals("Anak-Anak")){
            int totalUsia = (Integer.parseInt(usiaTahun)*12) + Integer.parseInt(usiaBulan);
            int rumus = (totalUsia*2) + 96;
            int usiaTahun = rumus/12;
            int usiaBulan = rumus%12;
            String hasil = String.valueOf(usiaTahun)+"."+String.valueOf(usiaBulan);
            beratIdeal.setText(hasil);
        }
        if(Kategori.equals("Remaja/Dewasa") && jKelamin.equals("Laki-Laki")){
            double tampilTinggi = Double.parseDouble(tinggi);
            double hasil = (tampilTinggi - 100) - ((tampilTinggi - 100) * 0.1);
            beratIdeal.setText(String.valueOf(hasil));
        }
        if(Kategori.equals("Remaja/Dewasa") &&jKelamin.equals("Perempuan")){
            double tampilTinggi = Double.parseDouble(tinggi);
            double hasil = (tampilTinggi - 100) - ((tampilTinggi - 100) * 0.15);
            beratIdeal.setText(String.valueOf(hasil));
        } 
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
        dbModel.rs = dbModel.resultset("select berat_badan FROM Berat_badan where id_pengguna ='"+session+"' order by tanggal desc limit 1");
        if(dbModel.rs.next()){
            berat = dbModel.rs.getDouble("berat_badan");
            Baru.setText(String.valueOf(dbModel.rs.getDouble("berat_badan")));
            String Ideal=beratIdeal.getText();
            double hasil= dbModel.rs.getDouble("berat_badan") - Double.parseDouble(Ideal);
            if(hasil < 0){
               Sisa.textFillProperty().setValue(Paint.valueOf("red"));
               Sisa.setText(String.valueOf(hasil));
            }
            else if(hasil==0){
                 Sisa.setText(String.valueOf(hasil));
            }
            else{
                Sisa.textFillProperty().setValue(Paint.valueOf("green"));
                Sisa.setText("+"+String.valueOf(hasil));
            }
        }
        dbModel.rs.close();
        dbModel.rs = dbModel.resultset("select berat_badan FROM Berat_badan where id_pengguna ='"+session+"' order by tanggal asc limit 1");
        if(dbModel.rs.next()){
            Mulai.setText(String.valueOf(dbModel.rs.getDouble("berat_badan")));
        }
        dbModel.rs.close();
    }

    private boolean ubahkategori(){
        boolean hasil = false;
        if(ChangecbKategori.getValue().equals(Kategori)){
            hasil = true;
        }else{
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Apakah Anda Yakin Merubah Kategori?");
            Optional<ButtonType> result = a.showAndWait();
            if(result.get() == ButtonType.OK){
                dbModel.InsertOrUpdate("DELETE FROM Berat_Badan WHERE id_pengguna = "+session+"");
                Mulai.setText("");
                Baru.setText("");
                Sisa.setText("");
                rataRata.setText("");
                minimal.setText("");
                maksimal.setText("");
                hasil = true;
            }
        }
        return hasil;
    }
    
    private boolean cekBatasanBerat(String Ktgori){
        boolean cek = false;
        if(Ktgori.equals("Bayi")){
            if(Double.parseDouble(inpBerat.getText()) < 2.5 || Double.parseDouble(inpBerat.getText()) > 11.8 ){
                bantuAlert("Data Tidak Valid","Min Berat = "+2.5+" kg Dan Max Berat = "+11.8+" kg" );
                cek = true;
            }
        }if(Ktgori.equals("Anak-Anak")){
            if(Double.parseDouble(inpBerat.getText()) < 14.8 || Double.parseDouble(inpBerat.getText()) > 33 ){
                bantuAlert("Data Tidak Valid","Min Berat = "+14.8+" kg Dan Max Berat = "+33+" kg" );
                cek = true;
            }
        }if(Ktgori.equals("Remaja/Dewasa")){
            if(Double.parseDouble(inpBerat.getText()) < 36 || Double.parseDouble(inpBerat.getText()) > 105 ){
                bantuAlert("Data Tidak Valid","Min Berat = "+36+" kg Dan Max Berat = "+105+" kg" );
                cek = true;
            }
        }
        return cek;
    }
        
    private boolean cekinputUsia(){
        return (!ChangeusiaTahun.getText().isEmpty() && !ChangeusiaBulan.getText().isEmpty());
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
    

    
    private void cekKategori(boolean tahun, boolean bulan, String setData, String setdata2){
        ChangeusiaTahun.setText(setData);
        ChangeusiaBulan.setText(setdata2);
        ChangeusiaTahun.disableProperty().set(tahun);
        ChangeusiaBulan.disableProperty().set(bulan);
    }
     
    private void bantuUpdate(){
        btnEdit.disableProperty().set(false);
        Update.disableProperty().set(true);
        btnCancel.disableProperty().set(true);
        namaUser.setText(Changename.getText());
        namaUser2.setText(Changename.getText());
        namaUser3.setText(Changename.getText());
        tutupField();
        hboxNama.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxKategori.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxUsia.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxJenisKelamin.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxEmail.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxPassword.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        hboxTinggi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: black;");
        bantuAlert(null, "Update Berhasil");
        
        nama = Changename.getText();
        Kategori = ChangecbKategori.getValue();
        usiaTahun = ChangeusiaTahun.getText();
        usiaBulan = ChangeusiaBulan.getText();
        jKelamin = ChangecbJenisKelamin.getValue();
        email = Changeemail.getText();
        password = Changepassword.getText();
        tinggi = Changetinggi.getText();
    }
    
    private void bukaField(){
        Changename.disableProperty().set(false);
        ChangecbKategori.disableProperty().set(false);
        if(Kategori.equals("Bayi")){
            cekKategori(true,false,"0","");
        }if(Kategori.equals("Anak-Anak")){
            cekKategori(false,false,"","0");
        }if(Kategori.equals("Remaja/Dewasa")){
            cekKategori(false,true,"","0");
        }
        Changename.setPromptText(nama);
        ChangeusiaTahun.setPromptText(usiaTahun);
        ChangeusiaBulan.setPromptText(usiaBulan);
        Changeemail.setPromptText(email);
        ChangecbJenisKelamin.disableProperty().set(false);
        Changeemail.disableProperty().set(false);
        Changepassword.disableProperty().set(false);   
        Changetinggi.disableProperty().set(false);
        hboxNama.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxKategori.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxUsia.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxJenisKelamin.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxEmail.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxPassword.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
        hboxTinggi.styleProperty().set("-fx-border-width: 0px 0px 2px 0px;"+"-fx-border-color: red;");
    }
    
    private void tutupField(){
        Changename.disableProperty().set(true);
        ChangecbKategori.disableProperty().set(true);
        ChangeusiaTahun.disableProperty().set(true);
        ChangeusiaBulan.disableProperty().set(true);
        ChangecbJenisKelamin.disableProperty().set(true);
        Changeemail.disableProperty().set(true);
        Changepassword.disableProperty().set(true);
        Changetinggi.disableProperty().set(true);
    }
    
    private boolean cekTinggi2(){
        boolean hasil = false;
        if(ChangecbKategori.getValue().equals(Kategori)){
            hasil = Double.parseDouble(Changetinggi.getText()) < Double.parseDouble(tinggi);
        }
        if(hasil){
            bantuAlert(null, "Tinggi Tidak Boleh Kurang dari "+tinggi+"");
        }
        return hasil;
    }
    private boolean cekTinggi1(){
        boolean cek = true;
        int batasBawah=0,batasAtas=0;
        if(ChangecbKategori.getValue().equals("Bayi")){
            cek = Double.parseDouble(Changetinggi.getText()) < 50 || Double.parseDouble(Changetinggi.getText()) > 78;
            batasBawah = 50;batasAtas = 78;
        }
        if(ChangecbKategori.getValue().equals("Anak-Anak")){
            cek = Double.parseDouble(Changetinggi.getText()) < 69 || Double.parseDouble(Changetinggi.getText()) > 139;
            batasBawah = 69;batasAtas = 139;
        }
        if(ChangecbKategori.getValue().equals("Remaja/Dewasa")){
            cek = Double.parseDouble(Changetinggi.getText()) < 140 || Double.parseDouble(Changetinggi.getText()) > 200;
            batasBawah = 140;batasAtas = 200;
        }
        if(cek){
            bantuAlert("Data Tidak Valid","Min Tinggi = "+batasBawah+" cm Dan Max Tinggi = "+batasAtas+" cm");
        }
        return cek;
    }
    
    private void bantuAlert(String header, String isi){
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(header);
        a.setContentText(isi);
        a.showAndWait();
    }
}