package com.example.examenparcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.examenparcial2.DB.AppDataBase;
import com.example.examenparcial2.adaptadores.FacturaAdapter;
import com.example.examenparcial2.entities.DateConverter;
import com.example.examenparcial2.entities.Factura;
import com.example.examenparcial2.entities.Promedio;
import com.example.examenparcial2.util.RecyclerItemClickListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Promedios extends AppCompatActivity {
    private RecyclerView rcvFacturas;
    private FacturaAdapter adapter;
    private List<Promedio> list;
    private Button btnFechaDesde, btnFechahasta, btnFiltrar;
    private TextInputLayout txtFechaLayout1, txtFechaLayout2;
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
        AppDataBase db = Room.databaseBuilder(Promedios.this,
                AppDataBase.class,"dbFactura").allowMainThreadQueries().build();
        list = db.facturaDao().findAll();
        adapter = new FacturaAdapter(list);
        rcvFacturas.setAdapter(adapter);
        rcvFacturas.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                rcvFacturas, new RecyclerItemClickListener.OnItemClickListener() {


            @Override
            public void onItemClick(View view, int position) {
                boolean usarFechas = false;
                Date date1 = null;
                Date date2 = null;
                if(validar(txtFechaLayout1,false) && validar(txtFechaLayout2,false) && validarFechas(txtFechaLayout1,txtFechaLayout2,false)){
                     date1 = new Date(DateConverter.toTimestamp(new java.util.Date(txtFechaLayout1.getEditText().getText().toString())));
                     date2 = new Date(DateConverter.toTimestamp(new java.util.Date(txtFechaLayout2.getEditText().getText().toString())));
                }
                    AppDataBase db = Room.databaseBuilder(Promedios.this,
                            AppDataBase.class,"dbFactura").allowMainThreadQueries().build();

                    String tipo = list.get(position).getTipo();
                    String Facturas = "";
                    List<Factura> listNueva = db.facturaDao().findAllByFecha1andFecha2(DateConverter.toTimestamp(date1),DateConverter.toTimestamp(date2),tipo);
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
                        AppDataBase db = Room.databaseBuilder(Promedios.this,
                                AppDataBase.class,"dbFactura").allowMainThreadQueries().build();
                        Date date1 = new Date(DateConverter.toTimestamp(new java.util.Date(txtFechaLayout1.getEditText().getText().toString())));
                        Date date2 = new Date(DateConverter.toTimestamp(new java.util.Date(txtFechaLayout2.getEditText().getText().toString())));

                        List<Promedio> listNueva = db.facturaDao().findByFecha1andFecha2(DateConverter.toTimestamp(date1),DateConverter.toTimestamp(date2));
                        list.clear();
                        list.addAll(listNueva);
                        adapter.notifyDataSetChanged();
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