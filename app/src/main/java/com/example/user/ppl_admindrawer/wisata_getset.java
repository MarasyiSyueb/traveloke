package com.example.user.ppl_admindrawer;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 23/03/2018.
 */

public class wisata_getset {

    private String lokasi;
    private  String harga;
    private  String deskripsi;




    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    private String imgurl;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    String des;
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    
    public wisata_getset() {
    }

    private  String nama;

    public wisata_getset(String nama, String lokasi, String harga, String deskripsi, String imgurl) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.imgurl = imgurl;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nama", nama);
        result.put("harga", harga);
        result.put("deskripsi", deskripsi);
        return result;
    }
}
