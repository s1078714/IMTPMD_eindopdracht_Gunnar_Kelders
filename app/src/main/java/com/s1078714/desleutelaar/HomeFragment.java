package com.s1078714.desleutelaar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gunnar on 9-4-2015.
 */
public class HomeFragment extends Fragment {

    public static String serverIp;
    public static int serverPort = 4444;
    public static ArrayList<String> serviceLijst;
    public static ArrayList<JSONObject> beknopteInformatielijst;
    public static ArrayList<JSONObject> informatieLijst;

    public static String informatiebeknopt = null;
    private static View rootview;
    private Spinner service_spinner;
    public static String servicenaam;
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
            service_spinner.setSelection(selectedPosition);
        }
        return rootview;

    }
}
