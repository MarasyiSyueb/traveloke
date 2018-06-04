package com.example.user.ppl_admindrawer;

import android.app.ProgressDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;



public class fragment_inputWis extends Fragment {


    MaterialEditText txtnm,txtlok,txthtm,txtdes,txtlong,txtlat;
    ImageButton tmb;
    Button but;
    ImageView img;
    private int PiCK_IMAGE_REQUEST=1;
    int colum_index;
    String image_path;
    TextView path;

    String Nama,Lokasi, Deskripsi, Harga;
    DatabaseReference databaseReference;
    // Create a storage reference from our app
    StorageReference storageRef;

    ///Creat URI
    Uri filePathUri;
    //creat Progres dialog
    ProgressDialog progresDialog;

    public fragment_inputWis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       
        return inflater.inflate(R.layout.fragment_input, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mengubah judul pada toolbar
        getActivity().setTitle("Detail Wisata");

        txtnm = (MaterialEditText)view.findViewById(R.id.txt_nama);
        txtlok = (MaterialEditText)view.findViewById(R.id.txt_lokasi);
        txthtm = (MaterialEditText) view.findViewById(R.id.txt_harga);
        txtdes = (MaterialEditText) view.findViewById(R.id.txt_deskripsi);
        txtlong = (MaterialEditText) view.findViewById(R.id.txt_long);
        txtlat = (MaterialEditText) view.findViewById(R.id.txt_lat);
        but = (Button)  view.findViewById(R.id.btn_submit);
        tmb=(ImageButton) view.findViewById(R.id.btn_tambah_gambar);
        path=(TextView) view.findViewById(R.id.txt_path);
        img=(ImageView) view.findViewById(R.id.image);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef= FirebaseStorage.getInstance().getReference();

        progresDialog=new ProgressDialog(getActivity());

        tmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Pilih Gambar"),PiCK_IMAGE_REQUEST);

            }
        });


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()==true)
                {
                    proses();
                }
                else
                {
                    Toast.makeText(getActivity(), "Cek Koneksi Internet Anda" ,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void proses() {
        if (txtnm.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Nama Wisata Belum Di Isi", Toast.LENGTH_SHORT);
            txtnm.setError("Error:Masukan Nama Wisata");
            txtnm.requestFocus();

        } else if (txtlok.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Lokasi Wisata Belum Di Isi", Toast.LENGTH_SHORT);
            txtlok.setError("Error:Masukan Lokasi Wisata");
            txtlok.requestFocus();

        } else if (txtlong.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Longitude Wisata Belum Di Isi", Toast.LENGTH_SHORT);
            txtdes.setError("Error:Masukan Longitude Wisata");
            txtdes.requestFocus();

        } else if (txtlat.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Latitude Wisata Belum Di Isi", Toast.LENGTH_SHORT);
            txtdes.setError("Error:Masukan Latitude Wisata");
            txtdes.requestFocus();

        } else if (txthtm.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "HTM Belum Di Isi", Toast.LENGTH_SHORT);
            txthtm.setError("Error:Masukan HTM");
            txthtm.requestFocus();

        } else if (txtdes.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Deskripsi Wisata Belum Di Isi", Toast.LENGTH_SHORT);
            txtdes.setError("Error:Masukan Deskripsi Wisata");
            txtdes.requestFocus();

        } else {
            if (filePathUri != null) {
                progresDialog.setTitle("Upload Gambar");
                progresDialog.show();
                StorageReference Storage = storageRef.child("Gambar_wisata/" + System.currentTimeMillis() + "." + getpath(filePathUri));
                Storage.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Nama = txtnm.getText().toString();
                        Lokasi = txtlok.getText().toString();
                        Harga = txthtm.getText().toString();
                        Deskripsi = txtdes.getText().toString();
                        progresDialog.dismiss();
                        Toast.makeText(getActivity(), "Tesimpan", Toast.LENGTH_SHORT);

                        wisata_getset wisa = new wisata_getset(Nama, Lokasi, Harga, Deskripsi, taskSnapshot.getDownloadUrl().toString());
                        databaseReference.child("Wisata").child(Nama).setValue(wisa);
                        txtnm.setText("");
                        txtlok.setText("");
                        txthtm.setText("");
                       txtdes.setText("");
                       img.setImageBitmap(null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progresDialog.dismiss();
                        //tampilkan pesan eror
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progresDialog.setTitle("Gambar Sedang di Unggah...");
                    }
                });
            }
            else{
                Toast.makeText(getActivity(), "Pilih Gambar Terlebih Dahuli", Toast.LENGTH_LONG).show();
            }
        }

    }
    public String getpath(Uri uri)
    {
        String[] projection={MediaStore.MediaColumns.DATA};
        Cursor cursor=getActivity().managedQuery(uri,projection,null,null,null);
        colum_index=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        image_path=cursor.getString(colum_index);
        return cursor.getString(colum_index);
    }


    private boolean isOnline() {
        ConnectivityManager konek =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo inf = konek.getActiveNetworkInfo();
        if(inf!=null && inf.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }

    // TODO: Rename method, update argument and hook method into UI event


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PiCK_IMAGE_REQUEST&&resultCode==getActivity().RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filePathUri=data.getData();
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePathUri);
                img.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
