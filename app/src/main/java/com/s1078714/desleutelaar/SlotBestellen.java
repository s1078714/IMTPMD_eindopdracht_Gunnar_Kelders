package com.s1078714.desleutelaar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Gunnar.
 */
public class SlotBestellen extends Activity {
    private String slotnaam;
    private String ip;
    private int port = 4444;

    String responseFix;
    Button annuleer;
    Button bestelknop;
    private static String naam;
    private static String adres;
    private static String telefoon;
    private static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_bestellen);
        slotnaam = InfoScherm.slotnaam;
        ip = HomeFragment.serverIp;
        setTitle("Service aanvragen");
        final TextView koperNaam = (TextView) findViewById(R.id.naamVeld);
        final TextView koperAdres = (TextView) findViewById(R.id.adresVeld);
        final TextView koperTelefoon = (TextView) findViewById(R.id.telefoonVeld);
        final TextView koperEmail = (TextView) findViewById(R.id.emailVeld);

        koperNaam.setText(naam);
        koperAdres.setText(adres);
        koperTelefoon.setText(telefoon);
        koperEmail.setText(email);

        final TextView serviceNaam = (TextView) findViewById(R.id.serviceNaam);
        serviceNaam.setText("U staat op het punt om de service: " + slotnaam + " aan te vragen. Vul hieronder uw gegevens in om uw aanvraag te bevestigen.");
        final TextView serviceBeknopteinformatie = (TextView) findViewById(R.id.aanvraagBeknopteinformatie);
        try{
            serviceBeknopteinformatie.setText("Service: " + slotnaam + ", " + HomeFragment.beknopteInformatielijst.get(HomeFragment.selectedPosition).getString("informatiebeknopt"));
        }
        catch(JSONException e)
        {

        }

        //buttons definieren

        bestelknop = (Button) findViewById(R.id.bestelBevestigen);
        bestelknop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                plaatsBestelling();
            }
        });
        annuleer = (Button) findViewById(R.id.bestelAnnuleer);
        annuleer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // velden ophalen en converteren naar een string
                naam = koperNaam.getText().toString();
                adres = koperAdres.getText().toString();
                telefoon = koperTelefoon.getText().toString();
                email = koperEmail.getText().toString();

                Intent i = new Intent(SlotBestellen.this, InfoScherm.class);

                startActivity(i);
            }
        });

    }

    private void plaatsBestelling() {
        final TextView koperNaam = (TextView) findViewById(R.id.naamVeld);
        final TextView koperAdres = (TextView) findViewById(R.id.adresVeld);
        final TextView koperTelefoon = (TextView) findViewById(R.id.telefoonVeld);
        final TextView koperEmail = (TextView) findViewById(R.id.emailVeld);

        naam = koperNaam.getText().toString();
        adres = koperAdres.getText().toString();
        telefoon = koperTelefoon.getText().toString();
        email = koperEmail.getText().toString();


        //JSONObjecten aanmaken en een JSONArray
        JSONObject bestelling = new JSONObject();
        JSONObject service = new JSONObject();
        JSONObject gegevens = new JSONObject();
        JSONArray bestelArray = new JSONArray();

        try {
            //JSONObjects invullen met de juiste gegevens
            service.put("slotnaam", slotnaam);
            gegevens.put("kopernaam", naam);
            gegevens.put("koperadres", adres);
            gegevens.put("kopertelnr", telefoon);
            gegevens.put("koperemail", email);

            //De twee JSONObjects in een JSONArray plaatsen
            bestelArray.put(service);
            bestelArray.put(gegevens);

            //JSONArray opnieuw in een JSONObject doen zodat de gegevens als een string naar de server kunnen worden gestuurd.
            bestelling.put("aanvraag", bestelArray);

        } catch (JSONException e) {

        }
        String response = null;

        try {
            try {
                //Hier communiceer ik met de server
                response = new ServerCommunicator(ip,
                        port, bestelling.toString()).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if(response == null)
        {
            Toast.makeText(SlotBestellen.this, "Server is momenteel niet bereikbaar", Toast.LENGTH_LONG).show();
        }
        else{
            responseFix = response.replace("null", "");

            Toast.makeText(SlotBestellen.this, responseFix, Toast.LENGTH_LONG).show();
            bestelknop.setVisibility(View.GONE);

        }
        //Sla de velden op zoals de gebruiker deze heeft achtergelaten
        naam = koperNaam.getText().toString();
        adres = koperAdres.getText().toString();
        telefoon = koperTelefoon.getText().toString();
        email = koperEmail.getText().toString();

        annuleer.setText("terug");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slot_bestellen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
