package com.example.examenparcial2.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class Factura {
    @PrimaryKey(autoGenerate = true)
    int idFactura;
    @ColumnInfo(name="numero")
    @Nullable
    int numeroFactura;
    @ColumnInfo(name="fechacompra")
    @TypeConverters(DateConverter.class)
    Date fechaDeCompra;
    @ColumnInfo(name="tipocombustible")
    String tipoCombustible;
    @ColumnInfo(name="monto")
    double montoCompra;
    @ColumnInfo(name="km")
    double km;

    public Factura( int numeroFactura, Date fechaDeCompra, String tipoCombustible, double montoCompra, double km) {

        this.numeroFactura = numeroFactura;
        this.fechaDeCompra = fechaDeCompra;
        this.tipoCombustible = tipoCombustible;
        this.montoCompra = montoCompra;
        this.km = km;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaDeCompra() {
        return fechaDeCompra;
    }

    public void setFechaDeCompra(Date fechaDeCompra) {
        this.fechaDeCompra = fechaDeCompra;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public double getMontoCompra() {
        return montoCompra;
    }

    public void setMontoCompra(double montoCompra) {
        this.montoCompra = montoCompra;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }
}
