package com.example.margus.tabbedradio;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Margus Muru on 27/04/2017.
 */

public class FragmentThree extends Fragment {

    private static final String TAG = FragmentThree.class.getSimpleName();

    private Spinner nSpinnerStreamSource;
    private EditText mEditTextStationName;
    private EditText mEditTextStationSource;

    private Button mButtonRemove;
    private Button mButtonUpdate;
    private Button mButtonAddNew;
    private Button mButtonReset;

    private static List<String> streamNames;
    private static List<String> streamSources;

    private int savedListPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        SettingsRepository repo = new SettingsRepository(getActivity());
        //streamNames = ((MainActivity) getActivity()).GetStationNames();
        streamNames = repo.GetStationNamesAsList();
        //streamSources = ((MainActivity) getActivity()).GetStationSources();
        streamSources = repo.GetStationSourcesAsList();

        mEditTextStationName = (EditText) view.findViewById(R.id.editTextStationName);
        mEditTextStationSource = (EditText) view.findViewById(R.id.editTextStationSource);

        mButtonAddNew = (Button) view.findViewById(R.id.btnAddNew);
        mButtonAddNew.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                streamNames.add(mEditTextStationName.getText().toString());
                streamSources.add(mEditTextStationSource.getText().toString());
                setSpinnerAdapter();
                saveData();
            }
        });
        mButtonUpdate = (Button) view.findViewById(R.id.btnUpdate);
        mButtonUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                streamNames.set(savedListPosition, mEditTextStationName.getText().toString());
                streamSources.set(savedListPosition, mEditTextStationSource.getText().toString());
                setSpinnerAdapter();
                saveData();
            }
        });
        mButtonRemove = (Button) view.findViewById(R.id.btnRemove);
        mButtonRemove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                streamNames.remove(savedListPosition);
                streamSources.remove(savedListPosition);
                setSpinnerAdapter();
                saveData();
            }
        });
        mButtonReset = (Button) view.findViewById(R.id.btnReset);
        mButtonReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //clear lists
                streamNames.clear();
                streamSources.clear();
                //get default names
                String defaultNames = getResources()
                        .getString(R.string.save_station_names_default);
                String[] result = defaultNames.split(",");
                for(int i = 0; i < result.length;i++){
                    streamNames.add(result[i]);
                }
                //get default sources
                String defaultSources = getResources()
                        .getString(R.string.save_station_urls_default);
                result = defaultSources.split(",");
                for(int i = 0; i < result.length;i++){
                    streamSources.add(result[i]);
                }
                //setup
                setSpinnerAdapter();
                saveData();
            }
        });

        nSpinnerStreamSource = (Spinner) view.findViewById(R.id.spinnerEditSources);
        setSpinnerAdapter();

        nSpinnerStreamSource.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "Stream Source OnItemSelectedListener " + (String)parent.getItemAtPosition(position));
                        //mSelectedStreamSource = (String)parent.getItemAtPosition(position);
                        //mSelectedStreamSource = (String) streamSources[position];
                        mEditTextStationName.setText(streamNames.get(position));
                        mEditTextStationSource.setText(streamSources.get(position));
                        savedListPosition = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //nothing to go
                    }
                }
        );



        return view;
    }

    private void setSpinnerAdapter(){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        streamNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        nSpinnerStreamSource.setAdapter(adapter);
    }

    private void saveData(){
        SettingsRepository repo = new SettingsRepository(getActivity());
        repo.SaveStationData(streamNames, getString(R.string.save_station_names));
        repo.SaveStationData(streamSources, getString(R.string.save_station_urls));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "onAttach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView");
    }
}
