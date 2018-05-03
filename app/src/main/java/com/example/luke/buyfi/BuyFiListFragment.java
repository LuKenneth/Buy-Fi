package com.example.luke.buyfi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class BuyFiListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "list";
    private static final String ARG_PARAM2 = "adapter";

    private ListView list;
    private BuyFiAdapter adapter;
    private View view;
    private NetworkManager nm;
    private ArrayList<BuyFiNetwork> networks;
    private ArrayList<NetworkListing> networkListings;
    private NetworkTransactionHandler nth;
    private FirebaseManager fm;
    private SwipeRefreshLayout refresh;

    private OnFragmentInteractionListener mListener;

    public BuyFiListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyFiListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyFiListFragment newInstance(String param1, String param2) {
        BuyFiListFragment fragment = new BuyFiListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            buyFiList = (BuyFiListView) getArguments().getSerializable(ARG_PARAM1);
//            adapter = (BuyFiAdapter) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.buyfi_list_fragment, container, false);

        initializeObjects(view);
        addPullToRefresh(view);
        loadNetworks();

        return view;
    }

    public void initializeObjects(View v) {
        nm = NetworkManager.getSharedInstance(getContext());
        fm = new FirebaseManager(this);
        networkListings = new ArrayList<NetworkListing>();
        list = (ListView) v.findViewById(R.id.list);
    }

    public void addPullToRefresh(View v) {
        refresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNetworks();
            }
        });
    }

    public void loadNetworks() {
        nm.obtainNetworks();
        networks = nm.getNetworks();
        networkListings.clear();
        for(int i = 0; i < networks.size(); i++) {
            //default when creating a new network list
            NetworkListing networkList = new NetworkListing(networks.get(i), false, "N/A", "Phone number");
            networkListings.add(networkList);
//            nth = new NetworkTransactionHandler(networkList);
//            fm.getReference().runTransaction(nth);
        }
        fm.setNetworkListing(networkListings);
        fm.getNetworks();
    }

    public void showNetworks(ArrayList<NetworkListing> networkListings) {
        this.networkListings = networkListings;
        adapter = new BuyFiAdapter(networkListings, getContext());
        list.setAdapter(adapter);
        refresh.setRefreshing(false);
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