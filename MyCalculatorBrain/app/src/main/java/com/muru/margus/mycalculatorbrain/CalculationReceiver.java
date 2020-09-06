package com.muru.margus.mycalculatorbrain;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Created by margu on 05.04.2017.
 */

public class CalculationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Double operand1 = intent.getDoubleExtra("operand1", 0.0);
        Double operand2 = intent.getDoubleExtra("operand2", 0.0);
        String operator = intent.getStringExtra("operator");

        Double result = calculateResult(operand1, operand2, operator);

        setResultCode(Activity.RESULT_OK);
        setResultData(result.toString());
    }

    private Double calculateResult(Double a, Double b, String operator){
        switch (operator){
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            case "√":
                return Math.sqrt(a);
            case "Sin":
                return Math.sin(Math.toRadians(a));
            case "Cos":
                return Math.cos(Math.toRadians(a));
            case "Tan":
                return Math.tan(Math.toRadians(a));
            case "%":
                return a/100;
            case "Log":
                return Math.log(a);
            case "e":
                return Math.E;
            case "π":
                return Math.PI;
            default:
                return 0.0;
        }
    }

}
