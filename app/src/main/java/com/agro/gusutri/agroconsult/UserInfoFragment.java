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
 * Created by dragos on 4/7/15.
 */
public class UserInfoFragment extends Fragment {

    private Dao dao=Dao.getInstance();
    public static final int USER_INFO_LIST=0;

    public UserInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.user_info_list);
        ArrayList tasks = dao.getTasks();
        ItemListAdapter itemListAdapter = new ItemListAdapter(getActivity(),tasks,USER_INFO_LIST);
        listView.setAdapter(itemListAdapter);


        return rootView;
    }


}
