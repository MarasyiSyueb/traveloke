package com.example.user.ppl_admindrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//ketiga Adapter di extends kan
// keempat klik kanan implement
public class adapter_wisata extends RecyclerView.Adapter<adapter_wisata.TampilDataViewHolder>  {
    // kelima list
    List<wisata_getset> tampilWisata;
    Context ctx;
    private int PiCK_IMAGE_REQUEST=1;
    // ke sebelas contructor klik kanan

    public adapter_wisata(Context ct, List<wisata_getset> tampilWisata)
    {   ctx=ct;
        this.tampilWisata = tampilWisata;
    }

    @Override
    public TampilDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_wisata, parent, false);
        TampilDataViewHolder holder = new TampilDataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TampilDataViewHolder holder, int position) {
        final wisata_getset wisa = tampilWisata.get(position);
        holder.txtmJudul.setText(wisa.getNama()+"-"+wisa.getLokasi());

        holder.txtmHarga.setText("Rp. "+wisa.getHarga() +" ,-");
        Picasso.with(ctx)
                .load(wisa.getImgurl())
                .into(holder.gambar);
        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(wisa.getImgurl());
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
                                hapus(wisa.getImgurl(),wisa.getNama());
                            }
                        })
                        .setNegativeButton("Batal",null)
                        .show();


            }
        });

        //tombol edit
        holder.edit.setOnClickListener(new View.OnClickListener() {
            private MaterialEditText txtNama, txtDeskripsi, txtHarga,txtLokasi;
            private TextView txt_tambah_gambar;
            private Button btnUpdate;
            private ImageButton btnTambah;
            ImageView gambar;
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(ctx);
                dialog.setContentView(R.layout.dialogwisata);
                dialog.setTitle("Edit Wisata");

                //Set Komponen
                txtNama=(MaterialEditText) dialog.findViewById(R.id.txt_nama);
                txtLokasi=(MaterialEditText)dialog.findViewById(R.id.txt_lokasi);
                txtDeskripsi=(MaterialEditText) dialog.findViewById(R.id.txt_deskripsi);
                txtHarga=(MaterialEditText) dialog.findViewById(R.id.txt_harga);
                btnUpdate=(Button) dialog.findViewById(R.id.btn_submit);
                btnTambah=(ImageButton) dialog.findViewById(R.id.btn_tambah_gambar);
                gambar=(ImageView)dialog.findViewById(R.id.image);
                txt_tambah_gambar=(TextView)dialog.findViewById(R.id.txt_tambah_gambar);
                txtNama.setText(wisa.getNama());
                txtLokasi.setText(wisa.getLokasi());
                txtHarga.setText(wisa.getHarga());
                txtDeskripsi.setText(wisa.getDeskripsi());
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
                        data.child("Wisata").child(wisa.getNama()).removeValue();
                        Map<String,Object> taskMap= new HashMap<>();
                        taskMap.put("nama",txtNama.getText().toString());
                        taskMap.put("lokasi",txtLokasi.getText().toString());
                        taskMap.put("deskripsi",txtDeskripsi.getText().toString());
                        taskMap.put("harga",txtHarga.getText().toString());
                        taskMap.put("imgurl",wisa.getImgurl().toString());
                        data.child("Wisata").child(txtNama.getText().toString()).updateChildren(taskMap);
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

        return tampilWisata.size();
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

            txtmJudul = (TextView)itemView.findViewById(R.id.nama_edit_wisata);

            txtmHarga = (TextView)itemView.findViewById(R.id.harga_edit);
            gambar=(CircleImageView) itemView.findViewById(R.id.imgwisata);
            hapus=(Button)itemView.findViewById(R.id.btn_hapus_wisata);
            edit=(Button)itemView.findViewById(R.id.btn_edit_wisata);
            popupGambar=(ImageView)itemView.findViewById(R.id.showgambar);
        }
    }

    // Image POP UP

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

    //Hapus file gambar dan database
    public void hapus(String URL, String Child)
    {
        ///Hapus Database
        DatabaseReference rev= FirebaseDatabase.getInstance().getReference();
        DatabaseReference menu=rev.child("Wisata");
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
