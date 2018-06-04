package com.example.user.ppl_admindrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_hotel extends RecyclerView.Adapter<adapter_hotel.TampilDataViewHolder> {
    List<hotel_getset> tampilhotel;
    Context ctx;
    private int PiCK_IMAGE_REQUEST=1;
    // ke sebelas contructor klik kanan

    public adapter_hotel(Context ct, List<hotel_getset> tampilhotel)
    {   ctx=ct;
        this.tampilhotel = tampilhotel;
    }

    @Override
    public adapter_hotel.TampilDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_hotel, parent, false);
        adapter_hotel.TampilDataViewHolder holder = new adapter_hotel.TampilDataViewHolder(view);
        return holder;
    }
    
    @Override
    public void onBindViewHolder(final adapter_hotel.TampilDataViewHolder holder, int position) {
        final hotel_getset hot = tampilhotel.get(position);
        holder.txtmJudul.setText(hot.getNama()+"-"+hot.getLokasi());

        holder.txtmHarga.setText("Rp. "+hot.getHarga() +" ,-");
        Picasso.with(ctx)
                .load(hot.getImgurl())
                .into(holder.gambar);
        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(hot.getImgurl());
            }

        });

        //tombol Hapus
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setMessage("Apakah Anda Yakin Menghapus?")
                        .setCancelable(false)
                        .setPositiveButton("YA",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int id){
                                hapus(hot.getImgurl(),hot.getNama());
                            }
                        })
                        .setNegativeButton("TIDAK",null)
                        .show();


            }
        });

        //tombol edit
        holder.edit.setOnClickListener(new View.OnClickListener() {
            private MaterialEditText txtNama, txtDeskripsi, txtHarga,txtLokasi, txtlong, txtlat;
            private TextView txt_tambah_gambar;
            private Button btnUpdate;
            private ImageButton btnTambah;
            ImageView gambar;
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(ctx);
                dialog.setContentView(R.layout.dialoghotel);
                dialog.setTitle("Edit Hotel");

                //Set Komponen
                txtNama=(MaterialEditText) dialog.findViewById(R.id.txt_namahotel);
                txtLokasi=(MaterialEditText)dialog.findViewById(R.id.txt_lokasihotel);
                txtHarga=(MaterialEditText) dialog.findViewById(R.id.txt_hargahotel);
                txtlong =(MaterialEditText)dialog.findViewById(R.id.txt_long);
                txtlat =(MaterialEditText)dialog.findViewById(R.id.txt_lat);
                btnUpdate=(Button) dialog.findViewById(R.id.btn_submit_hotel);
                btnTambah=(ImageButton) dialog.findViewById(R.id.btn_tambah_gambarhotel);
                gambar=(ImageView)dialog.findViewById(R.id.image_hotel);
               txt_tambah_gambar=(TextView)dialog.findViewById(R.id.txt_tambah_gambar);
                txtNama.setText(hot.getNama());
                txtLokasi.setText(hot.getLokasi());
                txtHarga.setText(hot.getHarga());
                txtlong.setText(hot.getLongitude());
                txtlat.setText(hot.getLatitude());
                //Menghilangkan tulisan dan gambar
                txt_tambah_gambar.setText("");
                btnTambah.setImageBitmap(null);
                btnTambah.setClickable(false);
                gambar.setImageBitmap(null);
                //mengganti tambah dengan update
                btnUpdate.setText("Update");
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ///Update data
                        final DatabaseReference data=FirebaseDatabase.getInstance().getReference();
                        data.child("Hotel").child(hot.getNama()).removeValue();
                        Map<String,Object> taskMap= new HashMap<>();
                        taskMap.put("nama",txtNama.getText().toString());
                        taskMap.put("lokasi",txtLokasi.getText().toString());
                        taskMap.put("harga",txtHarga.getText().toString());
                        taskMap.put("longitude",txtlong.getText().toString());
                        taskMap.put("latitude",txtlat.getText().toString());
                        taskMap.put("imgurl",hot.getImgurl().toString());
                        data.child("Hotel").child(txtNama.getText().toString()).updateChildren(taskMap);
                        Toast.makeText(ctx,"Berhasil di Update",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }
                });


                dialog.show();
            }
        });


    }



    @Override
    public int getItemCount() {

        return tampilhotel.size();
    }



    //Holder pengganti id
    public static class TampilDataViewHolder extends RecyclerView.ViewHolder{

        // ke dua TextView txtmNama;
        TextView txtmJudul, txtmHarga,txtmLokasi;
        CircleImageView gambar;
        ImageView popupGambar;
        Button edit;
        Button hapus;
        public TampilDataViewHolder(View itemView) {
            super(itemView);

            txtmJudul = (TextView)itemView.findViewById(R.id.nama_edit_hotel);

            txtmHarga = (TextView)itemView.findViewById(R.id.harga_edit_hotel);
            gambar=(CircleImageView) itemView.findViewById(R.id.imghotel);
            hapus=(Button)itemView.findViewById(R.id.btn_hapus_hotel);
            edit=(Button)itemView.findViewById(R.id.btn_edit_hotel);
            popupGambar=(ImageView)itemView.findViewById(R.id.showgambar);
        }
    }
    // Image POP UP

    public void showImage(String URL) {
        Dialog builder = new Dialog(ctx);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(ctx);
        Picasso.with(ctx)
                .load(URL)
                .into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    //Hapus file gambar dan database
    public void hapus(String URL, String Child)
    {
        ///Hapus Database
        DatabaseReference rev= FirebaseDatabase.getInstance().getReference();
        DatabaseReference menu=rev.child("Hotel");
        menu.child(Child).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ctx,"Hapus dari Database Sukses..",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx,"Hapus dari Database Gagal..",Toast.LENGTH_SHORT).show();
            }
        });

        StorageReference mStorageRef;

        ///Hapus File
        mStorageRef= FirebaseStorage.getInstance().getReferenceFromUrl(URL);
        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ctx,"Hapus Gambar Dari Storege Sukses..",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx,"Hapus Gambar Dari Storege Gagal..",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
