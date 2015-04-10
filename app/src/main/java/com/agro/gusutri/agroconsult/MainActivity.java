package com.agro.gusutri.agroconsult;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.User;

/**
 * Created by dragos on 4/7/15.
 */
public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //     Bundle bundle=getIntent().getExtras();
        //    user=bundle.getParcelable(Dao.USER);


        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("user_info_tab").setIndicator(getString(R.string.main_tab_user)),
                UserInfoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator(getString(R.string.main_tab_map)),
                UserInfoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Random 3"),
                UserInfoFragment.class, null);
    }


}
