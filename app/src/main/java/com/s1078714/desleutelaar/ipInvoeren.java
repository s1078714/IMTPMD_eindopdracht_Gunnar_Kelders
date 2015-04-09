package com.s1078714.desleutelaar;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Gunnar.
 */
public class ipInvoeren extends Activity {
    public static Boolean serverCheck;
    public static String ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_invoeren);

        //Enter key submit afvangen
        EditText ipInvoer = (EditText) findViewById(R.id.ipInvoer);
        ipInvoer.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int key, KeyEvent event) {

                switch(key) {
                    case KeyEvent.KEYCODE_ENTER:
                        checkServer();
                        break;

                    default:
                        return false;
                }

                return true;

            }
        });
        Button ipButton = (Button) findViewById(R.id.ipKnop);
        ipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkServer();
            }

        });


    }}
