package com.muru.margus.mycalculator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextView;
    private final String NUMBERS = "1234567890.";

    //saveData
    private String operand1;
    private String operand2;
    private String operator;
    private boolean replaceDisplayView;
    //end saveData

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.textViewDisplay);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/digital-7.ttf");
        mTextView.setTypeface(custom_font);

        operand1 = "";
        operand2 = "";
        operator = "";

        //set default text
        if(mTextView.getText().equals("000"))
            mTextView.setText("0.0");
        replaceDisplayView = true;
    }

    public void buttonClicked(View view){
        String button = ((Button) view).getText().toString();
        Log.d(TAG, "button: " + button);

        //check if number or an operand
        if(NUMBERS.contains(button) && mTextView.getText().toString().length() < 8){
            if(replaceDisplayView){
                mTextView.setText(button);
            }else{
                mTextView.append(button);
            }
        }
        else if(!button.equals("=")){
            operand1 = mTextView.getText().toString();
            operator = button;
            replaceDisplayView = true;
        }
        else{
            operand2 = mTextView.getText().toString();

            GetResultFromRemote();

        }
    }

    private void GetResultFromRemote(){
        Intent intentCallRemoteCalculator = new Intent();
        intentCallRemoteCalculator.setAction("com.muru.margus.calculationRequest");
        intentCallRemoteCalculator.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        intentCallRemoteCalculator.putExtra("operand1", Double.parseDouble(operand1));
        intentCallRemoteCalculator.putExtra("operand2", Double.parseDouble(operand2));
        intentCallRemoteCalculator.putExtra("operator", operator);
        sendOrderedBroadcast(
                intentCallRemoteCalculator,
                null,
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String responseData = getResultData();
                        mTextView.setText(responseData);
                    }
                },
                null,
                Activity.RESULT_OK,
                null,
                null
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("operand1", operand1);
        outState.putString("operand2", operand2);
        outState.putString("operator", operator);
        outState.putString("textView", mTextView.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTextView.setText(String.valueOf(savedInstanceState.getString("textView")));
        operand1 = String.valueOf(savedInstanceState.getString("operand1"));
        operand2 = String.valueOf(savedInstanceState.getString("operand2"));
        operator = String.valueOf(savedInstanceState.getString("operator"));
    }
}
