package com.agro.gusutri.agroconsult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agro.gusutri.agroconsult.model.Dao;

import java.util.ArrayList;

/**
 * Created by dragos on 4/16/15.
 */
public class FieldFragment extends Fragment {

    private Dao dao= Dao.getInstance();
    public static final int FIELD_LIST=1;

    public FieldFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ListView rootView = new ListView(getActivity());

        ArrayList fields = dao.getFields();
        ItemListAdapter itemListAdapter = new ItemListAdapter(getActivity(),fields,FIELD_LIST);
        rootView.setAdapter(itemListAdapter);

        return rootView;
    }


}
