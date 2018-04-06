package com.example.user.ppl_admindrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_user extends RecyclerView.Adapter<adapter_user.TampilDataViewHolder> {

    List<user_getset> tampiluser;
    Context ctx;
    private int PiCK_IMAGE_REQUEST=1;

    public adapter_user(Context ctx,List<user_getset> tampiluser) {
        this.tampiluser = tampiluser;
        this.ctx = ctx;
    }

    @Override
    public TampilDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_user, parent, false);
        TampilDataViewHolder holder = new TampilDataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TampilDataViewHolder holder, int position) {
        final user_getset use = tampiluser.get(position);
        holder.txtnama.setText(use.getNama());

        holder.txtemail.setText(use.getEmail());
        hotel_getset getset=new hotel_getset();
        Picasso.with(ctx)
                .load(getset.getImgurl())
                .into(holder.gambar);
        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(use.getImgurl());
            }

        });
        holder.detail.setOnClickListener(new View.OnClickListener() {
            private MaterialEditText txtNama, txtEmail, txtAlamat,txtPass,txtnohp;
            ImageView gambar;

            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(ctx);
                dialog.setContentView(R.layout.popup_detail);
                dialog.setTitle("Detail User");
                txtNama=(MaterialEditText)dialog.findViewById(R.id.txt_nama_usr);
                txtEmail=(MaterialEditText)dialog.findViewById(R.id.txt_email_usr);
                txtAlamat=(MaterialEditText)dialog.findViewById(R.id.txt_lokasi_usr);
                txtPass=(MaterialEditText)dialog.findViewById(R.id.txt_pass_usr);
                txtnohp=(MaterialEditText)dialog.findViewById(R.id.txt_nohp_usr);

                gambar=(ImageView)dialog.findViewById(R.id.image_usr);
                txtNama.setText(use.getNama());
                txtEmail.setText(use.getEmail());
                txtAlamat.setText(use.getAlamat());
                txtnohp.setText(use.getNo_hp());
                txtPass.setText(use.getPass());

            }
        });
    }

    @Override
    public int getItemCount() {
        return tampiluser.size();
    }

    public static class TampilDataViewHolder extends RecyclerView.ViewHolder {
        TextView txtnama, txtemail;
        CircleImageView gambar;
        ImageView popupGambar;
        Button detail;
        public TampilDataViewHolder(View itemView) {
            super(itemView);
            txtnama =(TextView)itemView.findViewById(R.id.nama_user);
            txtemail =(TextView)itemView.findViewById(R.id.email_user);
            detail =(Button) itemView.findViewById(R.id.btn_lihat);
            gambar=(CircleImageView) itemView.findViewById(R.id.imguser);
            popupGambar=(ImageView)itemView.findViewById(R.id.showgambar);
        }
    }
    public void showImage(String URL) {
        Dialog builder = new Dialog(ctx);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView imageView = new ImageView(ctx);
        Picasso.with(ctx)
                .load(URL)
                .into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
