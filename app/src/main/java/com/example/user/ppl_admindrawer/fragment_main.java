package com.example.user.ppl_admindrawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class fragment_main extends Fragment {

    ImageView imgWisata,imgeditwis,imghotel,imgedithotel,imguser,imgexit;
    private OnFragmentInteractionListener mListener;

    public fragment_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgWisata=(ImageView)view.findViewById(R.id.wisata_simpan);
        imgeditwis=(ImageView)view.findViewById(R.id.wisata_tampil);
        imghotel=(ImageView)view.findViewById(R.id.hotel_simpan);
        imgedithotel=(ImageView)view.findViewById(R.id.hotel_tampil);
        imguser=(ImageView)view.findViewById(R.id.tampil_user);
        imgexit=(ImageView)view.findViewById(R.id.exit);

        imgWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_inputWis fragment = new fragment_inputWis();
                //buat object fragmentkedua

                getFragmentManager().beginTransaction()
                        .replace(R.id.tempatframe, fragment)
                        //menggant fragment
                        .addToBackStack("fragment_main")
                        //menyimpan fragment
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        //transisi fragment
                        .commit();
            }
        });
        imgeditwis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_wisataTampil fragment = new Fragment_wisataTampil();
                getFragmentManager().beginTransaction()
                        .replace(R.id.tempatframe, fragment)
                        .addToBackStack("fragment_main")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        imghotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_hotel fragment = new fragment_hotel();
                getFragmentManager().beginTransaction()
                        .replace(R.id.tempatframe, fragment)
                        .addToBackStack("fragment_main")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        imgedithotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_hotelTampil fragment = new fragment_hotelTampil();
                getFragmentManager().beginTransaction()
                        .replace(R.id.tempatframe, fragment)
                        .addToBackStack("fragment_main")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_user fragment = new fragment_user();
                getFragmentManager().beginTransaction()
                        .replace(R.id.tempatframe, fragment)
                        .addToBackStack("fragment_main")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        imgexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
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
