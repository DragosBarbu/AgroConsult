package com.agro.gusutri.agroconsult;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.ProblemEvent;

import java.util.ArrayList;

/**
 * Created by dragos on 4/7/15.
 */
public class UserInfoFragment extends Fragment {

    private Dao dao=Dao.getInstance();
    private ListView listView;

    public UserInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);


       listView= (ListView) rootView.findViewById(R.id.user_info_list);
        TextView txtName=(TextView) rootView.findViewById(R.id.user_info_name);
        TextView txtEmail=(TextView) rootView.findViewById(R.id.user_info_email);

        GetProblemEventsAsyncTask t= new GetProblemEventsAsyncTask();
        t.execute();

        txtName.setText(MainActivity.user.getName());
        txtEmail.setText(MainActivity.user.getEmail());

        return rootView;
    }

    private class GetProblemEventsAsyncTask extends AsyncTask<Void,Void,ArrayList<ProblemEvent>> {

        @Override
        protected ArrayList<ProblemEvent> doInBackground(Void... params) {
            ArrayList<ProblemEvent> events = dao.getProblemEvents(MainActivity.user);
            return events;
        }

        @Override
        protected void onPostExecute(ArrayList<ProblemEvent> events) {
            ProblemListAdapter problemListAdapter= new ProblemListAdapter(getActivity(),events);
            listView.setAdapter(problemListAdapter);
        }


    }


}
