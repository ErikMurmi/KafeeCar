package com.prog.kafeecar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adaptador_Lista_Cliente_Cita extends RecyclerView.Adapter<Adaptador_Lista_Cliente_Cita.clienteHolder> {

    Lista citas;
    public Adaptador_Lista_Cliente_Cita(Lista citas){

        this.citas=citas;
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantallitacita,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Cliente_Cita.clienteHolder holder, int position) {
        try {
            Cliente cienteactual=(Cliente)Patioventainterfaz.usuarioActual;
            citas=Patioventainterfaz.patioventa.getCitas().listabusqueda(cienteactual);

            Cita c=(Cita)citas.getPos(position);
            String imagenauto=c.getVehiculo().getimagen() ;
            String hora= "Hora: "+c.getHora();
            String nombre= "Nombre: "+c.getVisitante().getNombre();
            String telefono= "Telefono: "+c.getVisitante().getTelefono();
            String matricula="Matricula N#= "+c.getVehiculo().getMatricula();

           // holder.imagenauto.setBackground(@imagenauto);
            holder.horacita.setText(hora);
            holder.nombre.setText(nombre);
            holder.telefono.setText(telefono);
            holder.matricula.setText(matricula);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return citas.contar();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenauto;
        public TextView horacita;
        public TextView nombre;
        public TextView matricula;
        public TextView telefono;


        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.imagen_cita_cliente_img);
            horacita=view.findViewById(R.id.hora_cita_cliente_txt);
            nombre=view.findViewById(R.id.nombre_cita_cliente_txt);
            telefono=view.findViewById(R.id.telefono_cita_cliente_txt);
            matricula=view.findViewById(R.id.matricula_cita_cliente_txt);
        }
    }
}
