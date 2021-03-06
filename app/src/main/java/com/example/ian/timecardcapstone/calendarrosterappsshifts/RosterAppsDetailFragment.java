package com.example.ian.timecardcapstone.calendarrosterappsshifts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ian.timecardcapstone.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RosterAppsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RosterAppsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RosterAppsDetailFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROSTERAPPS_DATA = "param1";
    private static final String ARG_DIALOG_TITLE = "param2";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = mRosterAppsDate;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(mRosterAppsData);
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }

    // TODO: Rename and change types of parameters
    private String mRosterAppsData;
    private String mRosterAppsDate;

    private OnFragmentInteractionListener mListener;

    public RosterAppsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rosterAppsData contains the string for the rosterapps data
     * @return A new instance of fragment RosterAppsDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RosterAppsDetailFragment newInstance(String rosterAppsData, String dateTitle) {
        RosterAppsDetailFragment fragment = new RosterAppsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROSTERAPPS_DATA, rosterAppsData);
        args.putString(ARG_DIALOG_TITLE, dateTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRosterAppsData = getArguments().getString(ARG_ROSTERAPPS_DATA);
            mRosterAppsDate = getArguments().getString(ARG_DIALOG_TITLE, "Today's Shift");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView rosterAppsDetailTextView = (TextView) view.findViewById(R.id
                .rosterAppsDetailFragmentString);
        rosterAppsDetailTextView.setText(mRosterAppsData);
        String title = mRosterAppsDate;
        getDialog().setTitle(title);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        return rootView;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
