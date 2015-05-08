package com.agro.gusutri.agroconsult;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;


import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.User;

/**
 * Created by dragos on 4/7/15.
 */
public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;
    private User user;

    public static final int REQUEST_CODE_IMAGE_CAPTURE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable(Dao.USER);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("info_tab").setIndicator(getString(R.string.main_tab_user)),
                UserInfoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fields_tab").setIndicator(getString(R.string.main_tab_fields)),
                FieldFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("map_tab").setIndicator(getString(R.string.main_tab_map)),
                MyMapFragment.class, null);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //DO NOT delete the super. It is needed for calling the fragments onActivityResult
        super.onActivityResult(requestCode, resultCode, data);
    }
}
