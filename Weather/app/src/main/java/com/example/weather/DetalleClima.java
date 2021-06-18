package com.example.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
//import okhttp3.Response;


public class DetalleClima extends AppCompatActivity {

    TextView txt_ciudad, txt_humedad, txt_temperatura, txt_clima, txt_latitud, txt_longitud, txt_presion;
    Button btnMap;
    AlertDialog dialogo;


    String ciudad = "";
    String clima = "";
    String log = "";
    String lat = "";
    String humedad = "";
    String presion = "";
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_clima);
        mostrarDialogo(DetalleClima.this);

        Bundle i = getIntent().getExtras();
        ciudad = i.getString("ciudad", "");
        obtenerClima(ciudad);

        txt_ciudad = findViewById(R.id.textView_ciudad);
        txt_humedad = findViewById(R.id.textView_humedad);
        txt_temperatura = findViewById(R.id.textView_temperatura);
        txt_presion = findViewById(R.id.textView_presion);
        txt_latitud = findViewById(R.id.textView_latitud);
        txt_longitud = findViewById(R.id.textView_longitud);
        txt_clima = findViewById(R.id.textView_clima);

        btnMap = findViewById(R.id.buttonMapa);
    }

    public void obtenerClima(String ciudad) {

        String url = "http://api.openweathermap.org/data/2.5/weather?q=CITY&APPID=e53301e27efa0b66d05045d91b2742d3".replace("CITY", ciudad);
        // StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Exitoso
                Log.d("DATA", response);
                try {
                    JSONObject jsonObjectResponse = new JSONObject(response);
                    JSONArray weather = jsonObjectResponse.getJSONArray("weather");
                    clima = weather.getJSONObject(0).getString("main");
                    log = jsonObjectResponse.getJSONObject("coord").getString("lon");
                    lat = jsonObjectResponse.getJSONObject("coord").getString("lat");
                    humedad = jsonObjectResponse.getJSONObject("main").getString("humidity");
                    presion = jsonObjectResponse.getJSONObject("main").getString("pressure");
                    temp = jsonObjectResponse.getJSONObject("main").getString("temp");

                    txt_ciudad.setText(ciudad);
                    txt_clima.setText(clima);
                    txt_temperatura.setText(temp);
                    txt_humedad.setText(humedad);
                    txt_latitud.setText(lat);
                    txt_longitud.setText(log);
                    txt_presion.setText(presion);

                    btnMap.setVisibility(View.VISIBLE);
                    quitarDialogo();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error al consultar
                Log.d("Error", error.toString());
                quitarDialogo();
                Toast.makeText(DetalleClima.this, "La ciudad seleccionada no existe", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void verMapa(View view) {
        // Search for restaurants in San Francisco
        Uri gmmIntentUri = Uri.parse("geo:lat,log".replace("lat", lat).replace("log", log));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void mostrarDialogo(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = DetalleClima.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loader, null));
        builder.setCancelable(false);
        dialogo = builder.create();
        dialogo.show();
    }

    public void quitarDialogo() {
        dialogo.dismiss();
    }
}