package com.example.user.ppl_admindrawer;

/**
 * Created by user on 24/03/2018.
 */

public class user_getset {
    String nama,alamat,email,no_hp,pass,imgurl;

    public user_getset(String nama, String alamat, String email, String no_hp, String pass, String imgurl) {
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.no_hp = no_hp;
        this.pass = pass;
        this.imgurl = imgurl;
    }

    public user_getset() {
        //buat firebase
    }

    public String getNama() {

        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
