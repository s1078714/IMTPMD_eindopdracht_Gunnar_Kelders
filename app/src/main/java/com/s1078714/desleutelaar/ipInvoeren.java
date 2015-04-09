package com.s1078714.desleutelaar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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


    }

    private void checkServer() {
        //controleren of de server bestaat en compatibel is.
        TextView ipVeld = (TextView) findViewById(R.id.ipInvoer);
        ip = ipVeld.getText().toString();
        Log.i("ip", ip);

        String response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("slotlijst", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            try {
                response = new ServerCommunicator(ip,
                        4444, jsonObject.toString()).execute().get();

            } catch (InterruptedException e) {

            }
        } catch (ExecutionException e1) {

        }
        if (response == null) {
            serverCheck = false;
            Toast.makeText(this, "Server reageert niet, of is niet compatibel", Toast.LENGTH_LONG).show();

        } else {
            serverCheck = true;
            HomeFragment.serverIp = ip;
            Toast.makeText(this, "Server succesvol verbonden", Toast.LENGTH_SHORT).show();
            Intent startApp = new Intent(this, MainActivity.class);
            startActivity(startApp);

        }
    }

    public void ServerCheckexternal(Context context){
        //check om voor een andere klasse de controle uit te voeren of de server nog steeds online is
        String response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("slotlijst", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            try {
                response = new ServerCommunicator(ip,
                        4444, jsonObject.toString()).execute().get();

            } catch (InterruptedException e) {

            }
        } catch (ExecutionException e1) {

        }
        if (response == null) {
            serverCheck = false;
            Toast.makeText(context, "Geen verbinding met server, u kunt momenteel geen bestelling doen", Toast.LENGTH_LONG).show();

        } else {
            serverCheck = true;
        }
    }
}
