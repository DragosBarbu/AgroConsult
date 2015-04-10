package com.agro.gusutri.agroconsult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by dragos on 4/7/15.
 */
public class UserInfoFragment extends Fragment {

    public UserInfoFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_user_info, container, false);

        ListView listView= (ListView) rootView.findViewById(R.id.user_info_list);
        ArrayList<String> x= new ArrayList<>();
        x.add("a");x.add("b");x.add("C");x.add("D");x.add("sda");x.add("asdfsd");x.add("sdef");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, x);


        listView.setAdapter(itemsAdapter);

        return rootView;
    }
}
