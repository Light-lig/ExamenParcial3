package com.example.examenparcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.examenparcial2.DB.AppDataBase;
import com.example.examenparcial2.entities.Factura;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class compra extends AppCompatActivity {
    TextInputLayout  cmbCombustible,txtFactura, txtFecha,txtMonto, txtKm;;
    AutoCompleteTextView cmbComb;
    Button btnFecha, btnGuardar;
    String selectedType;
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
                AppDataBase db = Room.databaseBuilder(compra.this,
                        AppDataBase.class,"dbFactura").allowMainThreadQueries().build();
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
                    Long reg = db.facturaDao().insert(factura);
                    limpiarCajas();
                    //Mostrar un mensaje de confirmacion al usuario
                    Toast.makeText(getApplicationContext(),
                            "Registro almacenado correctamente",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });



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