package com.example.examenparcial2.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.examenparcial2.dao.FacturaDao;
import com.example.examenparcial2.entities.Factura;

@Database(entities = {Factura.class},version=1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract FacturaDao facturaDao();

}
