package com.example.examenparcial2;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Integrantes extends AppCompatActivity {

    WebView html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrantes);


        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Se vincula el webview al layout
        html = findViewById(R.id.wvContenido);

        String texto;
        texto = "<html><body>";
        texto+="<div style=\"border-radius: 25px; border-style:1px #AAB7B8 solid;background-color: #3498DB; color:white; ";
        texto+="padding: 60px;border-width: 2px\">";
        texto+="<p align=\"justify\">";
        texto+="<h2>";
        texto+="Integrantes del Grupo: <br/ >";
        texto+="</h2>";
        texto+="<p >";
        texto+="--------DS39-----------<br/ >";
        texto+="Oscar Alfredo Caceres Hernandez <br/ >";
        texto+="Douglas Alberto Menjivar Quiterio<br/ >";
        texto+="Oscar Napoleon Saenz Quintanilla<br/ >";
        texto+="<br><br><strong>Grupo</strong><br> DS39 </p>";
        texto+="<p><strong>2021</strong>";
        texto +="</p></div></body></html>";


        html.loadData(texto,"text/html","utf-8");
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
}