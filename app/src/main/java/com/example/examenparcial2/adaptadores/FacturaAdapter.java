package com.example.examenparcial2.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenparcial2.R;
import com.example.examenparcial2.entities.Factura;
import com.example.examenparcial2.entities.Promedio;

import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo, monto, km;
        ImageView fotoAnimal;

        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
            monto = itemView.findViewById(R.id.txtPromedioGasto);
            km = itemView.findViewById(R.id.txtPromedioKm);
        }
    }

    public List<Promedio> listaFacturas;

    //Generar el constructor de la lista creada para que pueda recibir los valores
    public FacturaAdapter(List<Promedio> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.lista_combustibles, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titulo.setText(listaFacturas.get(position).getTipo());
        holder.km.setText(Double.toString(listaFacturas.get(position).getKmPromedio()));
        holder.monto.setText(Double.toString(listaFacturas.get(position).getMontoPromedio()));
    }

    @Override
    public int getItemCount() {
        return listaFacturas.size();
    }
}

