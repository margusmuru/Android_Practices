package com.muru.margus.demoprax2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private TextView mTextViewGreetingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText);
        mTextViewGreetingText = (TextView) findViewById(R.id.greetingText);
    }

    public void buttonClicked(View view){
        mTextViewGreetingText.setText("Hello, " + mEditText.getText() + "!");
    }
}
