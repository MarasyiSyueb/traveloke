package com.example.user.ppl_admindrawer;

/**
 * Created by user on 23/03/2018.
 */

public class hotel_getset {
    public hotel_getset(String nama, String lokasi, String harga, String imgurl) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.harga = harga;
        this.imgurl = imgurl;
    }

    public hotel_getset() {
    //jangan dihapus ini buat parsing firebase
    }

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    private String nama,lokasi,harga,imgurl;
}
