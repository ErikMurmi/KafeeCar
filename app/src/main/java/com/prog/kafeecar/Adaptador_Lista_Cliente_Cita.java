package com.prog.kafeecar;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Adaptador_Lista_Cliente_Cita extends RecyclerView.Adapter<Adaptador_Lista_Cliente_Cita.clienteHolder> {

    Lista citas;
    View view;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public Adaptador_Lista_Cliente_Cita(Lista citas){

        this.citas=citas;
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantallitacita,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Cliente_Cita.clienteHolder holder, int position) {
        try {
            Cliente cienteactual=(Cliente)Patioventainterfaz.usuarioActual;
            citas=Patioventainterfaz.patioventa.getCitas().listabusqueda(cienteactual);

            Cita c=(Cita)citas.getPos(position);
            String hora= "Hora: "+c.getHora();
            String nombre= "Nombre: "+c.getVisitante().getNombre();
            String telefono= "Telefono: "+c.getVisitante().getTelefono();
            String matricula="Matricula N#= "+c.getVehiculo().getMatricula();


            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getVehiculo().getimagen());
            Glide.with(view)
                    .load(filePath)
                    .into(holder.imagen);
            Toast.makeText(view.getContext(),"1", Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(view.getContext(),"2", Toast.LENGTH_SHORT).show();
                final File localFile = File.createTempFile(c.getVehiculo().getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        holder.imagen.setImageBitmap(bitmap);
                        Toast.makeText(view.getContext(),"3", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (IOException e){
                Toast.makeText(view.getContext(),"4", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            //holder.imagen.setImageDrawable();
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
        public ImageView imagen;
        public TextView horacita;
        public TextView nombre;
        public TextView matricula;
        public TextView telefono;


        public clienteHolder(@NonNull View view) {
            super(view);
            imagen=view.findViewById(R.id.imagen_cita_cliente_img);
            horacita=view.findViewById(R.id.hora_cita_cliente_txt);
            nombre=view.findViewById(R.id.nombre_cita_cliente_txt);
            telefono=view.findViewById(R.id.telefono_cita_cliente_txt);
            matricula=view.findViewById(R.id.matricula_cita_cliente_txt);
        }
    }
}
