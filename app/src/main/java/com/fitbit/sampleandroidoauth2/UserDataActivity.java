package com.fitbit.sampleandroidoauth2;

import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.sampleandroidoauth2.databinding.ActivityUserDataBinding;
import com.fitbit.sampleandroidoauth2.fragments.HeartRateFragment;
import com.fitbit.sampleandroidoauth2.fragments.InfoFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class UserDataActivity extends Activity implements ServiceCallbacks{
    private ActivityUserDataBinding binding;
    private UserDataPagerAdapter userDataPagerAdapter;
    private BackgroundService myService;
    private boolean bound = false;

    @Override
    protected void onStart() {
        super.onStart();

    }
    protected void onResume() {
        super.onResume();
        /*
        // Unbind from service
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
            myService.switchON=false;
             myService.setCallbacks(null); // unregister
            Toast.makeText(getApplicationContext(), "Auto monitor service OFF",
                    Toast.LENGTH_LONG).show();
        }
        */
    }


    @Override
    protected void onStop() {
        super.onStop();


    }

    /** Callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setCallbacks(UserDataActivity.this); // register
            Log.d("REGISTERED","ON");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    /* Defined by ServiceCallbacks interface */
    public void update() {
        userDataPagerAdapter.heartRateFragment.onRefresh();
        userDataPagerAdapter.heartRateFragment.getStatus();
    }
    public void hello() {
        Toast.makeText(getApplicationContext(), "Hello world",
                Toast.LENGTH_LONG).show();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, UserDataActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_data);
        binding.setLoading(false);

        userDataPagerAdapter = new UserDataPagerAdapter(getFragmentManager());
        binding.viewPager.setAdapter(userDataPagerAdapter);

        binding.viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        addTabs();
        // bind to Service
        Toast.makeText(getApplicationContext(), "Auto monitor service ON",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, BackgroundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void addTabs() {
        final ActionBar actionBar = getActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        int numberOfTabs = userDataPagerAdapter.getCount();
        for (int i = 0; i < numberOfTabs; i++) {
            final int index = i;
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(getString(userDataPagerAdapter.getTitleResourceId(i)))
                            .setTabListener(new ActionBar.TabListener() {
                                @Override
                                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                                    binding.viewPager.setCurrentItem(index);
                                }

                                @Override
                                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                                }

                                @Override
                                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                                }
                            }));
        }
    }


    public void onLogoutClick(View view) {
        binding.setLoading(true);
        AuthenticationManager.logout(this);
    }

}