package com.example.examenparcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.examenparcial2.adaptadores.FacturaAdapter;
import com.example.examenparcial2.entities.DateConverter;
import com.example.examenparcial2.entities.Factura;
import com.example.examenparcial2.entities.Promedio;
import com.example.examenparcial2.util.Constantes;
import com.example.examenparcial2.util.RecyclerItemClickListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Promedios extends AppCompatActivity {
    private RecyclerView rcvFacturas;
    private FacturaAdapter adapter;
    private List<Promedio> list = new ArrayList<Promedio>();
    private Button btnFechaDesde, btnFechahasta, btnFiltrar;
    private TextInputLayout txtFechaLayout1, txtFechaLayout2;
    private Constantes con = new Constantes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promedios);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        btnFechaDesde = findViewById(R.id.btnFecha);
        btnFechahasta = findViewById(R.id.btnFecha2);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        txtFechaLayout1 = findViewById(R.id.txtFechaContainer);
        txtFechaLayout2 = findViewById(R.id.txtFechaContainer2);
        txtFechaLayout1.setEnabled(false);
        txtFechaLayout2.setEnabled(false);
        rcvFacturas = findViewById(R.id.rcvReporte);
        rcvFacturas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FacturaAdapter(list);
        rcvFacturas.setAdapter(adapter);
        CargarDatos();
        rcvFacturas.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                rcvFacturas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean usarFechas = false;
                String date1 = null;
                String date2 = null;
                if(validar(txtFechaLayout1,false) && validar(txtFechaLayout2,false) && validarFechas(txtFechaLayout1,txtFechaLayout2,false)){
                     date1 =  new SimpleDateFormat("yyyy-MM-dd").format(new Date(txtFechaLayout1.getEditText().getText().toString()));
                     date2 =  new SimpleDateFormat("yyyy-MM-dd").format(new Date(txtFechaLayout2.getEditText().getText().toString()));
                }


                    String tipo = list.get(position).getTipo();
                CargarDatosPorFechasTipo(date1, date2, tipo);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker.Builder<Long> materialDateBuilder2 = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder2.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        final MaterialDatePicker materialDatePicker2 = materialDateBuilder2.build();

        btnFechaDesde.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");


            }
        });
        btnFechahasta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                materialDatePicker2.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");


            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {

                    @Override
                    public void onPositiveButtonClick(Object selection) {


                        txtFechaLayout1.getEditText().setText(materialDatePicker.getHeaderText());

                    }
                });
        materialDatePicker2.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {


                        txtFechaLayout2.getEditText().setText(materialDatePicker2.getHeaderText());
                    }
                });
        btnFiltrar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                    if(validar(txtFechaLayout1,true) && validar(txtFechaLayout2,true) && validarFechas(txtFechaLayout1,txtFechaLayout2,true)){

                        String fecha1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date(txtFechaLayout1.getEditText().getText().toString()));
                        String fecha2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date(txtFechaLayout2.getEditText().getText().toString()));
                        CargarDatosPorFechas(fecha1, fecha2);
                    }
            }
        });

    }
    public void CargarDatos(){
        String URL = "http://"+con.IP+":8080/ExamenFinalPhpAndroid/endpoint/findAll.php";

        ProgressDialog barraProgreso = new ProgressDialog(this);
        barraProgreso.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            try {

                                JSONArray items = response.getJSONArray("items");

                                for(int i=0;i<items.length();i++){
                                    // Get current json object
                                    JSONObject factura = items.getJSONObject(i);
                                    Promedio pro = new Promedio();
                                    // Get the current student (json object) data
                                    pro.setTipo(factura.getString("tipo"));
                                    pro.setKmPromedio(factura.getDouble("kmPromedio"));

                                    pro.setMontoPromedio(factura.getDouble("montoPromedio"));
                                    list.add(pro);
                                    adapter.notifyDataSetChanged();
                                }
                                barraProgreso.dismiss();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
                        barraProgreso.dismiss();
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(request);

    }
    public void CargarDatosPorFechas(String fecha1, String fecha2){
        String URL = "http://"+con.IP+":8080/ExamenFinalPhpAndroid/endpoint/filtro1.php?$txtfecha1="+fecha1+"&$txtfecha2="+fecha2;

        ProgressDialog barraProgreso = new ProgressDialog(this);
        barraProgreso.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray items = response.getJSONArray("items");
                    list.clear();
                    for(int i=0;i<items.length();i++){
                        // Get current json object
                        JSONObject factura = items.getJSONObject(i);
                        Promedio pro = new Promedio();
                        // Get the current student (json object) data
                        pro.setTipo(factura.getString("tipo"));
                        pro.setKmPromedio(factura.getDouble("kmPromedio"));

                        pro.setMontoPromedio(factura.getDouble("montoPromedio"));
                        list.add(pro);
                        adapter.notifyDataSetChanged();
                    }
                    if(items.length() == 0){
                        Toast.makeText(getBaseContext(),"No hay resultados.",Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
                    barraProgreso.dismiss();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
                        barraProgreso.dismiss();
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(request);

    }
    public void CargarDatosPorFechasTipo(String fecha1, String fecha2, String tipo){
        String URL = "http://"+con.IP+":8080/ExamenFinalPhpAndroid/endpoint/filtro2.php?$txtfecha1="+fecha1+"&$txtfecha2="+fecha2+"&$txtTipo="+tipo;

        ProgressDialog barraProgreso = new ProgressDialog(this);
        barraProgreso.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray items = response.getJSONArray("items");
                            List<Factura> listNueva = new ArrayList<>();
                            for(int i=0;i<items.length();i++){
                                // Get current json object
                                JSONObject factura = items.getJSONObject(i);
                                Factura fa = new Factura();
                                // Get the current student (json object) data
                                Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(factura.getString("fechadeCompra"));
                                fa.setFechaDeCompra(fecha);
                                fa.setMontoCompra(factura.getDouble("montoCompra"));

                                listNueva.add(fa);

                            }
                            String Facturas = "";

                            AlertDialog.Builder builder = new AlertDialog.Builder(Promedios.this);
                            Facturas += tipo + "\n";
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                            for (Factura fac : listNueva){
                                Facturas += "- " + formatter.format(fac.getFechaDeCompra()) + ":" +"$"+fac.getMontoCompra() + "\n";
                            }

                            builder.setMessage(Facturas)
                                    .setTitle("Detalles de compras").setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            if(items.length() == 0){
                                Toast.makeText(getBaseContext(),"No hay resultados.",Toast.LENGTH_LONG).show();
                            }
                            adapter.notifyDataSetChanged();
                            barraProgreso.dismiss();

                        }catch (JSONException | ParseException e){
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
                        barraProgreso.dismiss();
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(request);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    boolean validar(TextInputLayout txt, boolean mostrar){
        if(txt.getEditText().getText().toString().isEmpty()){
            txt.setErrorEnabled(true);
            if(mostrar){
                txt.setError("El campo es requerido");
            }
            return false;
        }
        return true;
    }

    boolean validarFechas(TextInputLayout txt1, TextInputLayout txt2,boolean mostrar)  {
        boolean estado = true;
        Date date1 = new Date(DateConverter.toTimestamp(new java.util.Date(txt1.getEditText().getText().toString())));
        Date date2 = new Date(DateConverter.toTimestamp(new java.util.Date(txt2.getEditText().getText().toString())));

        if(date1.compareTo(date2) > 0){
            if(mostrar){
                Toast.makeText(this,"La fecha desde no debe mayor a la fecha hasta." + date1.toString() + " | " + date2.toString(),Toast.LENGTH_LONG).show();
            }
            estado =  false;
        }
        return estado;
    }
}