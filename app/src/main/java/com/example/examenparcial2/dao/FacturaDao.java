package com.example.examenparcial2.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examenparcial2.entities.Factura;
import com.example.examenparcial2.entities.Promedio;

import java.sql.Date;
import java.util.List;

@Dao
public interface FacturaDao {
    @Insert
    Long insert(Factura fa);
    @Query("Select tipocombustible as 'tipo'," +
                        "avg(monto) as 'montoPromedio'," +
                       "avg(km) as 'kmPromedio'" +
                        ",fechacompra from factura group by tipo")
    List<Promedio> findAll();

    @Query("Select tipocombustible as 'tipo'," +
            "avg(monto) as 'montoPromedio'," +
            "avg(km) as 'kmPromedio'" +
            ",fechacompra from factura where fechacompra between :fecha1 and :fecha2 group by tipo")
    List<Promedio> findByFecha1andFecha2(Long fecha1, Long  fecha2);
    @Query("DELETE FROM factura")
    int deleteFactura();

    @Query("select * from factura where ((:fecha1 is null  and :fecha2 is null) or (fechacompra between :fecha1 and :fecha2) ) and tipocombustible = :tipo")
    List<Factura> findAllByFecha1andFecha2(Long  fecha1, Long  fecha2, String tipo);
}
