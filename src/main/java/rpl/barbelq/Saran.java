/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Krisna
 */
public class Saran {

    private SimpleStringProperty kategori;
    private SimpleStringProperty aktivitas;
    private SimpleStringProperty makanan;
    private SimpleIntegerProperty id_saran;
    public Saran(){
    }
    
    public Saran(int id_saran, String makanan, String aktivitas, String kategori) {
        this.makanan = new SimpleStringProperty(makanan);
        this.aktivitas = new SimpleStringProperty(aktivitas);
        this.kategori = new SimpleStringProperty(kategori);
    }

    
    /**
     * @param id_saran the id_saran to set
     */
    public void setId_saran(SimpleIntegerProperty id_saran) {
        this.id_saran = id_saran;
    }
    
    /**
     * @return the kategori
     */
    public String getKategori() {
        return kategori.get();
    }

    /**
     * @param kategori the kategori to set
     */
    public void setKategori(String kategori) {
        this.kategori = new SimpleStringProperty(kategori);
    }

    /**
     * @return the aktivitas
     */
    public String getAktivitas() {
        return aktivitas.get();
    }

    /**
     * @param aktivitas the aktivitas to set
     */
    public void setAktivitas(String aktivitas) {
        this.aktivitas = new SimpleStringProperty(aktivitas);
    }

    /**
     * @return the makanan
     */
    public String getMakanan() {
        return makanan.get();
    }

    /**
     * @param makanan the makanan to set
     */
    public void setMakanan(String makanan) {
        this.makanan = new SimpleStringProperty(makanan);
    }

    
    @Override
    public String toString() {
        return getMakanan() + " " + getAktivitas() + " " + getKategori();
    }

    /**
     * @return the id_saran
     */
    public int getId_saran() {
        return id_saran.get();
    }

    
    
}
