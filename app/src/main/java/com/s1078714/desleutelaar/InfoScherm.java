package com.s1078714.desleutelaar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

/**
 * Created by Gunnar.
 */
public class InfoScherm extends Activity {
    public static String ip;
    public static int port;
    public static String slotnaam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_scherm);
        // ip adres, port en de gekozen slotnaam ophalen van HomeFragment
        ip = HomeFragment.serverIp;
        port = HomeFragment.serverPort;
        slotnaam = HomeFragment.slotnaam;

        //zet titel van de pagina
        setTitle("Slot informatie");

        //velden vullen met de benodigde teksten
        TextView infoSlotNaam = (TextView) findViewById(R.id.infoSlotNaam);
        infoSlotNaam.setText("Slot: " + slotnaam);
        TextView InfoVeld = (TextView) findViewById(R.id.infoVeld);
        try{
            InfoVeld.setText(HomeFragment.informatieLijst.get(HomeFragment.selectedPosition).getString("informatie"));
        }
        catch(JSONException e)
        {

        }
        Button bestelKnop = (Button) findViewById(R.id.bestel);
        Button annuleerKnop = (Button) findViewById(R.id.annuleer);

        //wanneer op annuleer wordt geklikt opent het home venster zich opnieuw
        annuleerKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(home);
            }
        });
        ipInvoeren ipinvoeren= new ipInvoeren();
        ipinvoeren.ServerCheckexternal(this);
        if(ipInvoeren.serverCheck == false){
            bestelKnop.setVisibility(View.GONE);
        }

        bestelKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent bestel = new Intent(getApplicationContext(), SlotBestellen.class);

                startActivity(bestel);
            }
        });
    }
}
