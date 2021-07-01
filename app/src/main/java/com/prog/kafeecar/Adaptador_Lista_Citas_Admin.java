package com.prog.kafeecar;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Adaptador_Lista_Citas_Admin extends RecyclerView.Adapter<Adaptador_Lista_Citas_Admin.clienteHolder> {
    Lista citas;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
   // public Adaptador_Lista_Catalogo.clienteHolder lyt;
    View view;
    private RecyclerItemClick itemClick;
    public Adaptador_Lista_Citas_Admin(Lista citas, RecyclerItemClick itemClick){
        this.citas =citas;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Citas_Admin.clienteHolder holder, int position) {
        try {
            Cita c= (Cita) citas.getPos(position);
            String nombre= c.getVisitante().getNombre();
            String telefono =c.getVisitante().getTelefono();
            String placa = c.getVehiculo().getPlaca();
            String horario = String.format("%02d:00%s-%02d:00%s",c.getHora(),formatoHora(c.getHora()),c.getHora()+1,formatoHora(c.getHora()+1));
                    formatoHora(c.getHora());
            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getVehiculo().getimagen());
            Glide.with(view)
                    .load(filePath)
                    .into(holder.imagenauto);
            try {
                final File localFile = File.createTempFile(c.getVehiculo().getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imagenauto.setImageBitmap(bitmap);
                });
            }catch (IOException e){
                e.printStackTrace();
            }


            holder.nombre.setText(nombre);
            holder.horario.setText(horario);
            holder.telefono.setText(telefono);
            holder.placa.setText(placa);

            holder.itemView.setOnClickListener(v -> {
                itemClick.itemClick("PSD-1234");
            });
            /*
            holder.itemView.setOnClickListener(v -> {
                Catalogo_Admin_Fragment.irVer(placa);
            });*/

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
        public TextView telefono;
        public TextView horario;
        public TextView nombre;
        public TextView placa;

        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.c_lista_img);
            nombre=view.findViewById(R.id.nombre_c_lista_txt);
            horario=view.findViewById(R.id.hora_c_lista_txt);
            placa = view.findViewById(R.id.matricula_c_lista_txt);
            telefono = view.findViewById(R.id.telefono_c_lista_txt);
        }
    }

    public interface RecyclerItemClick{
        void itemClick(String placa);
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

