package com.example.margus.tabbedradio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RequiresPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Margus Muru on 07.05.2017.
 */

public class SettingsRepository {

    private Activity activity;

    public SettingsRepository(Activity activity){
        this.activity = activity;
    }

    //could be removed if music view would use List<String> instead of String[].
    // Didnt change that just in case!
    public String[] GetStationNames(){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultNames = activity.getResources()
                .getString(R.string.save_station_names_default);
        String stationNames = sharedPref
                .getString(activity.getString(R.string.save_station_names), defaultNames);
        return stationNames.split(",");
    }
    public List<String> GetStationNamesAsList(){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultNames = activity.getResources()
                .getString(R.string.save_station_names_default);
        String stationNames = sharedPref
                .getString(activity.getString(R.string.save_station_names), defaultNames);
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
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultUrls = activity.getResources()
                .getString(R.string.save_station_urls_default);
        String stationUrls = sharedPref
                .getString(activity.getString(R.string.save_station_urls), defaultUrls);
        return stationUrls.split(",");
    }

    public List<String> GetStationSourcesAsList(){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultUrls = activity.getResources()
                .getString(R.string.save_station_urls_default);
        String stationUrls = sharedPref
                .getString(activity.getString(R.string.save_station_urls), defaultUrls);
        String[] result = stationUrls.split(",");
        List<String> resultList = new ArrayList<>();
        for(int i = 0; i < result.length;i++){
            resultList.add(result[i]);
        }
        return resultList;
    }

    public void SaveStationData(List<String> names, String destination){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < names.size(); i++){
            builder.append(names.get(i) + ",");
        }
        editor.putString(destination, builder.toString());
        editor.commit();
    }
}

