package com.example.user.ppl_admindrawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_wisataTampil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_wisataTampil extends Fragment {
    RecyclerView rv;
    List<wisata_getset>wisas;
    adapter_wisata edit_wis;

    FragmentActivity context=getActivity();
    DatabaseReference dbRef,wisata;

    private OnFragmentInteractionListener mListener;

    public Fragment_wisataTampil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_wisata_tampil, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mengubah judul pada toolbar
        getActivity().setTitle("Edit Wisata");
        rv = (RecyclerView)getActivity().findViewById(R.id.recycler_edit);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        wisas = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference();
        wisata=dbRef.child("Wisata");
        edit_wis = new adapter_wisata(getActivity(),wisas);
        rv.setAdapter(edit_wis);

        wisata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wisas.removeAll(wisas);
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    wisata_getset dataku= snapshot.getValue(wisata_getset.class);
                    wisas.add(dataku);
                }
                edit_wis.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
