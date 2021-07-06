package com.prog.kafeecar;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Adaptador_Lista_Citas extends RecyclerView.Adapter<Adaptador_Lista_Citas.clienteHolder> {
    Lista citas_originales;
    private Lista citas_buscadas;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
   // public Adaptador_Lista_Catalogo.clienteHolder lyt;
    View view;
    private RecyclerItemClick itemClick;
    public Adaptador_Lista_Citas(Lista citas, RecyclerItemClick itemClick){
        this.citas_buscadas =citas;
        this.itemClick = itemClick;
        citas_originales = new Lista();
        citas_originales.copiar(citas);
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Citas.clienteHolder holder, int position) {
        try {
            Cita c= (Cita) citas_buscadas.getPos(position);
            String nombre= c.getCliente().getNombre();
            String telefono =c.getCliente().getTelefono();
            //String vehiculo = c.getVehiculo().getMarca() + c.getVehiculo().getModelo();
            String placa = c.getVehiculo().getPlaca();
            String horario = String.format("%02d:00%s-%02d:00%s",c.getHora(),formatoHora(c.getHora()),c.getHora()+1,formatoHora(c.getHora()+1));
                    formatoHora(c.getHora());
            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getVehiculo().getimagen());
            try {
                final File localFile = File.createTempFile(c.getVehiculo().getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri foto = Uri.parse(localFile.getAbsolutePath());
                    holder.imagenauto.setImageURI(foto);
                });
            }catch (IOException e){
                e.printStackTrace();
            }


            holder.nombre.setText(nombre);
            holder.horario.setText(horario);
            holder.telefono.setText(telefono);
            holder.placa.setText(placa);
            //holder.vehiculo.setText(vehiculo);
            holder.itemView.setOnClickListener(v -> itemClick.itemClick(placa,c.getCliente().getCedula()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return citas_buscadas.contar();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenauto;
        public TextView telefono;
        public TextView horario;
        public TextView nombre;
        //public TextView vehiculo;
        public TextView placa;

        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.c_lista_img);
            nombre=view.findViewById(R.id.nombre_c_lista_txt);
            horario=view.findViewById(R.id.hora_c_lista_txt);
            //vehiculo = view.findViewById(R.id.vehiculo_c_lista_text);
            placa = view.findViewById(R.id.matricula_c_lista_txt);
            telefono = view.findViewById(R.id.telefono_c_lista_txt);
        }
    }

    public interface RecyclerItemClick{
        void itemClick(String placa,String cedula_cliente);
    }

    /*public void buscar(String dia,String mes,String anio){
        citas_buscadas.vaciar();
        for(int i=0; i<citas_originales.contar();i++){
            Cita actual=null;
            try {
                actual = (Cita) citas_originales.getPos(i);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            String f = Patioventainterfaz.getFechaMod(actual.getFechaCita());
            String dia_c = f.split("-")[0];
            String mes_c = f.split("-")[1];
            String anio_c = f.split("-")[2];
            if(!dia_c.isEmpty() && !mes_c.isEmpty() && !anio_c.isEmpty()){
                if( (dia_c.compareTo(dia)==0 && mes_c.compareTo(mes)==0) && anio_c.compareTo(anio)==0){
                    citas_buscadas.add(actual);
                }
            }
        }

        notifyDataSetChanged();
    }*/
    public void buscar(String fecha){
        if(fecha.length()==0){
            citas_buscadas.vaciar();
            citas_buscadas.copiar(citas_originales);
        }else {
            citas_buscadas.vaciar();
            for (int i = 0; i < citas_originales.contar(); i++) {
                Cita actual = null;
                try {
                    actual = (Cita) citas_originales.getPos(i);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                String f = Patioventainterfaz.getFechaMod(actual.getFechaCita());
                if (f.contains(fecha)) {
                    citas_buscadas.add(actual);
                }
            }
        }

        notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    public String formatoHora(int hora){
        if(hora>12){
            return "pm";
        }
        return "am";
    }
/*
    @Override
    public void itemClick(ItemList item){

    }*/
}

