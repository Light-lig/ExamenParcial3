package com.example.examenparcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examenparcial2.entities.Factura;
import com.example.examenparcial2.util.Constantes;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class compra extends AppCompatActivity {
    TextInputLayout  cmbCombustible,txtFactura, txtFecha,txtMonto, txtKm;;
    AutoCompleteTextView cmbComb;
    Button btnFecha, btnGuardar;
    String selectedType;
    private Constantes con = new Constantes();
    String URL = "http://"+con.IP+":8080/ExamenFinalPhpAndroid/endpoint/agregarFactura.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        cmbCombustible = (TextInputLayout) findViewById(R.id.cmbTipoCombustibleContainer);
        cmbComb = (AutoCompleteTextView) findViewById(R.id.cmbTipoCombustible);
        txtFactura = (TextInputLayout) findViewById(R.id.txtNumeroFacturaLayout);
        txtFecha = (TextInputLayout) findViewById(R.id.txtFechaContainer);
        txtMonto = (TextInputLayout) findViewById(R.id.txtMontoComproa);
        txtKm = (TextInputLayout) findViewById(R.id.txtKmContainer);
        btnFecha = (Button) findViewById(R.id.btnFecha);
        btnGuardar = (Button) findViewById(R.id.btnRegistrarCompra);
        txtFecha.setEnabled(false);

        List<String> item = new ArrayList();
        item.add("DIESEL");
        item.add("PREMIUM");
        item.add("REGULAR");

        final ArrayAdapter<String> adapterType = new ArrayAdapter<>(
                compra.this,
                R.layout.list_item,
                item
        );
        cmbComb.setAdapter(adapterType);
        ((AutoCompleteTextView)cmbCombustible.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterType.getItem(position);
                selectedType = ((AutoCompleteTextView)cmbCombustible.getEditText()).getText().toString();

            }
        });


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        btnFecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");


            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        txtFecha.getEditText().setText(materialDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar(txtFactura) &&
                    validar(txtFecha) &&
                    validar(txtKm) &&
                    validar(cmbCombustible) &&
                    validar(txtMonto)){
                    //Insertando valores
                    Factura factura =new Factura(
                            Integer.parseInt(txtFactura.getEditText().getText().toString()),
                            new Date(txtFecha.getEditText().getText().toString()),
                            cmbCombustible.getEditText().getText().toString(),
                            Double.parseDouble(txtMonto.getEditText().getText().toString()),
                            Double.parseDouble(txtKm.getEditText().getText().toString())
                    );
                    insertarCompra(factura);
                    limpiarCajas();

                }

            }
        });



    }
    public void insertarCompra(Factura fac){
        ProgressDialog barraProgreso = new ProgressDialog(this);
        barraProgreso.show();


        StringRequest  request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String  response) {
                     try {
                         if(response != null){
                             if(response.equals("Se ingreso correctamente")){
                                 Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                                 barraProgreso.dismiss();
                             }else{
                                 Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                                 barraProgreso.dismiss();
                             }
                         }else{
                             Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();

                             barraProgreso.dismiss();

                         }


                     }catch (Exception e){
                        e.printStackTrace();
                     }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            barraProgreso.dismiss();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            barraProgreso.dismiss();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            barraProgreso.dismiss();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            barraProgreso.dismiss();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            barraProgreso.dismiss();
                        }
                    }

                }
                ){
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("$txtnumeroFactura",String.valueOf(fac.getNumeroFactura()));
                String newstring = new SimpleDateFormat("yyyy-MM-dd").format(fac.getFechaDeCompra());
                params.put("$txtfechadeCompra", newstring);
                params.put("$txttipoCombustible", fac.getTipoCombustible());
                params.put("$txtmontoCompra", String.valueOf(fac.getMontoCompra()));
                params.put("$txtKm", String.valueOf(fac.getKm()));
                return params;
            }
        };

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getBaseContext()).add(request);
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
    void limpiarCajas(){
        txtFactura.getEditText().getText().clear();
        txtFecha.getEditText().getText().clear();
        txtKm.getEditText().getText().clear();
        txtMonto.getEditText().getText().clear();
    }
    boolean validar(TextInputLayout txt){
            if(txt.getEditText().getText().toString().isEmpty()){
                txt.setErrorEnabled(true);
                txt.setError("El campo es requerido");
                return false;
            }
            txt.setErrorEnabled(false);
            return true;
    }
}