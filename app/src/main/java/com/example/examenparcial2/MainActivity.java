package com.example.examenparcial2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.examenparcial2.DB.AppDataBase;

import java.util.EventListener;

public class MainActivity extends AppCompatActivity {
    ImageButton btnAgregar, btnReporte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnReporte = findViewById(R.id.btnReporte);


        btnAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent agregarCompra = new Intent(MainActivity.this, compra.class);
                startActivity(agregarCompra);
            }
        });
        btnReporte.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent reporte = new Intent(MainActivity.this, Promedios.class);
                startActivity(reporte);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoverflow,menu);
        return true;
    }
    //Metodo ara los item del menu
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        //Obtener el item seleccionado
        int id = item.getItemId();

        if (id == R.id.miIntegrantes) {
            Intent integrantes = new Intent(MainActivity.this,Integrantes.class );
            startActivity(integrantes);
        }

        return super.onOptionsItemSelected(item);
    }
}