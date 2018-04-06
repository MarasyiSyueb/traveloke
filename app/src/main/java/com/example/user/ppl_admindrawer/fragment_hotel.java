package com.example.user.ppl_admindrawer;

import android.app.ProgressDialog;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileNotFoundException;
import java.io.IOException;

public class fragment_hotel extends Fragment {

    private OnFragmentInteractionListener mListener;
    MaterialEditText txtnm,txtlok,txthtm;
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
    public fragment_hotel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detail Hotel");

        txtnm = (MaterialEditText)view.findViewById(R.id.txt_namahotel);
        txtlok = (MaterialEditText)view.findViewById(R.id.txt_lokasihotel);
        txthtm = (MaterialEditText) view.findViewById(R.id.txt_hargahotel);

        but = (Button)  view.findViewById(R.id.btn_submit_hotel);
        tmb=(ImageButton) view.findViewById(R.id.btn_tambah_gambarhotel);
        path=(TextView) view.findViewById(R.id.txt_path);
        img=(ImageView) view.findViewById(R.id.image_hotel);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef= FirebaseStorage.getInstance().getReference();

        progresDialog=new ProgressDialog(getActivity());
        tmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Pilih Gambar"),1);
            }
        });
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()==true){
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
            Toast.makeText(getActivity(), "Nama Hotel Belum Di Isi", Toast.LENGTH_SHORT);
            txtnm.setError("Error:Masukan Nama Hotel");
            txtnm.requestFocus();
        } else if (txtlok.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Lokasi Hotel Belum Di Isi", Toast.LENGTH_SHORT);
            txtlok.setError("Error:Masukan Lokasi Hotel");
            txtlok.requestFocus();
        } else if (txthtm.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Harga Belum Di Isi", Toast.LENGTH_SHORT);
            txthtm.setError("Error:Masukan Harga");
            txthtm.requestFocus();
        } else {
            if (filePathUri != null) {
                progresDialog.setTitle("Upload Gambar");
                progresDialog.show();
                StorageReference Storage = storageRef.child("Gambar_hotel/" + System.currentTimeMillis() + "." + getpath(filePathUri));
                Storage.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Nama=txtnm.getText().toString();
                        Lokasi=txtlok.getText().toString();
                        Harga=txthtm.getText().toString();
                        progresDialog.dismiss();
                        Toast.makeText(getActivity(), "Tesimpan", Toast.LENGTH_SHORT);
                        hotel_getset hotel_getset=new hotel_getset(Nama,Lokasi,Harga,taskSnapshot.getDownloadUrl().toString());
                        databaseReference.child("Hotel").child(Nama).setValue(hotel_getset);
                        txtnm.setText("");
                        txtlok.setText("");
                        txthtm.setText("");
                        img.setImageBitmap(null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progresDialog.setTitle("Gambar Sedang di Unggah...");
                    }
                });
            }
        }
    }
    private boolean isOnline(){
        ConnectivityManager konek =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo inf=konek.getActiveNetworkInfo();
        if(inf!=null&&inf.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }
    public String getpath(Uri uri){
        String[] projecttion={MediaStore.MediaColumns.DATA};
        Cursor cursor=getActivity().managedQuery(uri,projecttion,null,null,null);
        colum_index=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        image_path=cursor.getString(colum_index);
        return cursor.getString(colum_index);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,requestCode,data);
        if(requestCode==1&&resultCode==getActivity().RESULT_OK&&data!=null&&data.getData()!=null){
            filePathUri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePathUri);
                img.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
