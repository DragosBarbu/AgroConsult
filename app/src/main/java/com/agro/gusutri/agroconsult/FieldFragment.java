package com.agro.gusutri.agroconsult;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragos on 4/16/15.
 */
public class FieldFragment extends Fragment {

    private Dao dao= Dao.getInstance();
    public static final int FIELD_LIST=1;
    private ListView rootView;

    public FieldFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new ListView(getActivity());
        FieldsAsync f= new FieldsAsync(getActivity());
        f.execute();
        return rootView;
    }

    public class FieldsAsync extends AsyncTask<Void,Void,ArrayList<Field>>
    {

        private Context mContext;

        public FieldsAsync(Context context){
            mContext=context;
        }
        @Override
        protected ArrayList<Field> doInBackground(Void... params) {
            return  dao.getFields(MainActivity.user.getId());

        }

        @Override
        protected void onPostExecute(ArrayList<Field> fields) {
            ItemListAdapter itemListAdapter = new ItemListAdapter(mContext,fields,FIELD_LIST);
            rootView.setAdapter(itemListAdapter);
        }
    }
}
