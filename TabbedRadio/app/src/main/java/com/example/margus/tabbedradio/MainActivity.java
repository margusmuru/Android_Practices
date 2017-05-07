package com.example.margus.tabbedradio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private int mMusicPlayerStatus = C.STREAM_STATUS_STOPPED;
    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        //application-wide broadcast receiver
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(C.INTENT_STREAM_STATUS_BUFFERING);
        mIntentFilter.addAction(C.INTENT_STREAM_STATUS_PLAYING);
        mIntentFilter.addAction(C.INTENT_STREAM_STATUS_STOPPED);

        mBroadcastReceiver = new BroadcastReceiverInMainActivity();

        //initialize components
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);



    }
    /*
        //could be removed if music view would use List<String> instead of String[].
    // Didnt change that just in case!
    public String[] GetStationNames(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultNames = getResources()
                .getString(R.string.save_station_names_default);
        String stationNames = sharedPref
                .getString(getString(R.string.save_station_names), defaultNames);
        return stationNames.split(",");
    }
    public List<String> GetStationNamesAsList(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultNames = getResources()
                .getString(R.string.save_station_names_default);
        String stationNames = sharedPref
                .getString(getString(R.string.save_station_names), defaultNames);
        String[] result = stationNames.split(",");
        List<String> resultList = new ArrayList<>();
        for(int i = 0; i < result.length;i++){
            resultList.add(result[i]);
        }
        return resultList;
    }

    //could be removed if music view would use List<String> instead of String[].
    // Didnt change that just in case!
    public String[] GetStationSources(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultUrls = getResources()
                .getString(R.string.save_station_urls_default);
        String stationUrls = sharedPref
                .getString(getString(R.string.save_station_urls), defaultUrls);
        return stationUrls.split(",");
    }

    public List<String> GetStationSourcesAsList(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultUrls = getResources()
                .getString(R.string.save_station_urls_default);
        String stationUrls = sharedPref
                .getString(getString(R.string.save_station_urls), defaultUrls);
        String[] result = stationUrls.split(",");
        List<String> resultList = new ArrayList<>();
        for(int i = 0; i < result.length;i++){
            resultList.add(result[i]);
        }
        return resultList;
    }

    public void SaveStationData(List<String> names, String destination){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < names.size(); i++){
            builder.append(names.get(i) + ",");
        }
        editor.putString(destination, builder.toString());
        editor.commit();
    }

*/

    public int getmMusicPlayerStatus(){
        return mMusicPlayerStatus;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentOneMusic(), "Music");
        adapter.addFragment(new FragmentTwo(), "Two");
        adapter.addFragment(new FragmentThree(), "Three");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPageChangeListener(adapter));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mTitleList.get(position);
        }
    }

    class ViewPageChangeListener extends ViewPager.SimpleOnPageChangeListener{

        ViewPagerAdapter mAdapter;

        public ViewPageChangeListener(ViewPagerAdapter adapter){
            mAdapter = adapter;
        }

        @Override
        public void onPageSelected(int position){
            Log.d(TAG, "Tab changed: " + mAdapter.getPageTitle(position));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MainActivity");
        //start listening for local broadcasts
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MainActivity");
        //stop listening for local broadcasts
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private class BroadcastReceiverInMainActivity extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            switch (intent.getAction()){
                case C.INTENT_STREAM_STATUS_BUFFERING:
                    mMusicPlayerStatus = C.STREAM_STATUS_BUFFERING;
                    break;
                case C.INTENT_STREAM_STATUS_PLAYING:
                    mMusicPlayerStatus = C.STREAM_STATUS_PLAYING;
                    break;
                case C.INTENT_STREAM_STATUS_STOPPED:
                    mMusicPlayerStatus = C.STREAM_STATUS_STOPPED;
                    break;
            }
        }
    }
}
