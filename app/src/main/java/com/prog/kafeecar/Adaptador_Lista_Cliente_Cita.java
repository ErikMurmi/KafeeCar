package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class Adaptador_Lista_Cliente_Cita extends RecyclerView.Adapter<Adaptador_Lista_Cliente_Cita.clienteHolder> {

    View view;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Lista citas_buscadas;
    Lista citas_originales;
    private RecyclerItemClick itemClick;


    public Adaptador_Lista_Cliente_Cita(Lista citas, RecyclerItemClick itemClick){
        this.citas_buscadas=citas;
        this.itemClick = itemClick;
        citas_originales = new Lista();
        citas_originales.copiar(citas);
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita_cliente,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  clienteHolder holder, int position) {//uno
        try {
            Cita c=(Cita)citas_buscadas.getPos(position);
            String nombre= c.getCliente().getNombre();
            String telefono= c.getCliente().getTelefono();
            String placa= c.getVehiculo().getPlaca();
            String hora = String.format("%02d:00%s-%02d:00%s",c.getHora(),formatoHora(c.getHora()),c.getHora()+1,formatoHora(c.getHora()+1));
            formatoHora(c.getHora());
            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getVehiculo().getimagen());

            try {
                final File localFile = File.createTempFile(c.getVehiculo().getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imagen.setImageBitmap(bitmap);
                });
            }catch (IOException e){
                e.printStackTrace();
            }
            holder.horacita.setText(hora);
            holder.nombre.setText(nombre);
            holder.telefono.setText(telefono);
            holder.placa.setText(placa);
            holder.itemView.setOnClickListener(v -> itemClick.itemClick(placa,c.getCliente().getCedula()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return citas_buscadas.contar();
    }

   /* public void buscar(String fecha){
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
    }*/




    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagen;
        public TextView horacita;
        public TextView nombre;
        public TextView placa;
        public TextView telefono;


        public clienteHolder(@NonNull View view) {
            super(view);
            imagen=view.findViewById(R.id.c_lista_cliente_img);
            nombre=view.findViewById(R.id.nombre_c_lista_cliente_txt);
            horacita=view.findViewById(R.id.hora_c_lista_cliente_txt);
            placa = view.findViewById(R.id.placa_c_lista_cliente_txt);
            telefono = view.findViewById(R.id.telefono_c_lista_cliente_txt);
        }
    }
    public interface RecyclerItemClick {
        void itemClick(String placa,String cedula_cliente);
    }
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
}
