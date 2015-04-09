package com.s1078714.desleutelaar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Gunnar.
 */
public class HomeFragment extends Fragment {

    public static String serverIp = "80.114.142.171";
    public static int serverPort = 4444;
    public static ArrayList<String> slotenLijst;
    public static ArrayList<JSONObject> beknopteInformatielijst;
    public static ArrayList<JSONObject> informatieLijst;

    public static String informatiebeknopt = null;
    private static View rootview;
    private Spinner slot_spinner;
    public static String slotnaam;
    public static Boolean gegevensOpgehaald = false;
    public static int selectedPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.home_layout, container, false);

        //bepaal of gegevens al eerder zijn opgehaald
        if (gegevensOpgehaald == false) {
            dataOphalen();
            dataInvullen();
        } else {
            dataInvullen();
            slot_spinner.setSelection(selectedPosition);
        }
        return rootview;

    }
    public void dataOphalen() {

        //ophalen van de diverse sloten
        slotenLijst = new ArrayList<String>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("slotenlijst", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response = null;
        try {
            try {
                // Dit IP adres moet IP adres van server zijn.
                response = new ServerCommunicator(serverIp,
                        serverPort, jsonObject.toString()).execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (response == null) {

            Toast.makeText(rootview.getContext(), "Kan geen verbinding maken met de server.", Toast.LENGTH_LONG).show();
        } else {
            // Haal de null naam weg van de JSONArray (Voorkomt error)
            String jsonFix = response.replace("null", "");

            JSONArray JArray = null;
            try {
                JArray = new JSONArray(jsonFix);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jObject = null;
            String value = null;
           slotenLijst = new ArrayList<String>();

            for (int i = 0; i < JArray.length(); i++) {
                try {
                    jObject = JArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    value = jObject.getString("naam");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                slotenLijst.add(value);

            }
            // haal beknopte informatie op
            beknopteInformatielijst = new ArrayList<JSONObject>();
            JSONObject beknoptjObject = new JSONObject();
            try {
                for (int i = 0; i < slotenLijst.size(); i++) {
                    beknoptjObject.put("informatiebeknopt", slotenLijst.get(i));
                    try {
                        try {
                            informatiebeknopt = new ServerCommunicator(serverIp,
                                    serverPort, beknoptjObject.toString()).execute().get();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    String infoFix = informatiebeknopt.replace("null", "");
                    JSONObject fixedjObject = new JSONObject(infoFix);
                    beknopteInformatielijst.add(fixedjObject);

                    Log.i("informatiebeknopt", infoFix);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //haal de gedetailleerde informatie op
            informatieLijst = new ArrayList<JSONObject>();
            JSONObject informatieObject = new JSONObject();
            try {
                for (int i = 0; i < slotenLijst.size(); i++) {
                    informatieObject.put("informatie", slotenLijst.get(i));
                    try {
                        try {
                            String informatie = new ServerCommunicator(serverIp,
                                    serverPort, informatieObject.toString()).execute().get();
                            String infoFix = informatie.replace("null", "");
                            JSONObject fixedjObject = new JSONObject(infoFix);
                            informatieLijst.add(fixedjObject);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        gegevensOpgehaald = true;

    }

    private void dataInvullen() {
        //spinner vullen met de opgehaalde data
        slot_spinner = (Spinner) rootview.findViewById(R.id.spinner);


        slot_spinner
                .setAdapter(new ArrayAdapter<String>(rootview.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        slotenLijst));


        slot_spinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {
                        // TODO Auto-generated method stub
                        // Locate the textviews in activity_main.xml
                        TextView beknopteinfo = (TextView) rootview.findViewById(R.id.textView4);

                        try {
                            // Set the text followed by the position
                            beknopteinfo.setText(beknopteInformatielijst.get(position).getString("informatiebeknopt"));
                            slotnaam = slotenLijst.get(position);

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });


        Button infoknop = (Button) rootview.findViewById(R.id.infoknop);
        infoknop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(rootview.getContext(), InfoScherm.class);
                selectedPosition = slot_spinner.getSelectedItemPosition();

                startActivity(i);
            }
        });

    }
}
