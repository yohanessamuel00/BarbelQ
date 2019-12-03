/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 *
 * @author yohan
 */
public class Reminder {
    private int session;
    private String tampung;
    private double tampungIdeal,tampungBerat;
    DBBarbelQ dbModel = new DBBarbelQ();
    ArrayList<ArrayList<String>> Saran;
    
    public void tampilSaran(Label saran, int id) throws SQLException{
        session = id;
        String tampil = "";
        String saranPengguna ="";
        dbModel.rs = dbModel.resultset("select saranPengguna from DataPengguna where id_pengguna = "+session+"");
        if(dbModel.rs.next()){
            if(!dbModel.rs.getString("saranPengguna").equals("")){
                tampil = "tampil";
                saranPengguna = dbModel.rs.getString("saranPengguna");
            }
        }
        dbModel.rs.close();
        String Aktivitas="";
        String Makanan="";
        if(tampil.equals("tampil")){
            dbModel.rs = dbModel.resultset("select Saran.makanan, Saran.aktivitas from Saran inner join DataPengguna On Saran.id_saran = DataPengguna.saranPengguna where DataPengguna.saranPengguna = "+saranPengguna+"");
            if(dbModel.rs.next()){
                Makanan = dbModel.rs.getString("Makanan");
                Aktivitas = dbModel.rs.getString("Aktivitas");
            }
            dbModel.rs.close();
            ButtonType sudah = new ButtonType("Sudah",ButtonBar.ButtonData.YES);
            ButtonType belum = new ButtonType("Belum",ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert a = new Alert(Alert.AlertType.NONE);
            a.getButtonTypes().setAll(sudah,belum);
            a.setTitle("Remainder");
            a.setHeaderText(null);
            a.setContentText("Makanan: \n"+Makanan+"\n\nAktivitas: \n"+Aktivitas+"");
            Optional<ButtonType> result = a.showAndWait();
            if(result.get() == sudah){
                dbModel.InsertOrUpdate("update DataPengguna set saranPengguna = '' where id_pengguna = "+session+"");
            }else{
                saran.setText("Makanan: \n"+Makanan+"\nAktivitas: \n"+Aktivitas+"");
            }
        }
    }
    
    public void aturSaran(Label saran) throws SQLException{
        Saran = new ArrayList<>();
        Random randomNumbers = new Random();
        dbModel.rs = dbModel.resultset("select * from Saran where kategori = '"+tampung+"'");
        while(dbModel.rs.next()){
            ArrayList<String> makananDanAktivitas = new ArrayList<>();
            makananDanAktivitas.add(dbModel.rs.getString("id_saran"));
            makananDanAktivitas.add(dbModel.rs.getString("aktivitas"));
            makananDanAktivitas.add(dbModel.rs.getString("makanan"));
            Saran.add(makananDanAktivitas);
        }
        dbModel.rs.close();
        int banyakData = Saran.size();
        int hasil = randomNumbers.nextInt(banyakData);
        String Aktivitas = Saran.get(hasil).get(1);
        String Makanan = Saran.get(hasil).get(2);
        saran.setText("Makanan: \n"+Makanan+"\nAktivitas: \n"+Aktivitas+"");
        dbModel.InsertOrUpdate("update DataPengguna set saranPengguna = "+Saran.get(hasil).get(0)+" where id_pengguna ="+session+"");
    }
    
    public void ambilDataBerat(double beratIdeal, double beratSekarang){
        tampungIdeal = beratIdeal;
        tampungBerat = beratSekarang;
        double hasil = tampungBerat - tampungIdeal;
        if(hasil<0) tampung = "Kekurangan";
        if(hasil>0) tampung = "Berlebihan";
    }
}
