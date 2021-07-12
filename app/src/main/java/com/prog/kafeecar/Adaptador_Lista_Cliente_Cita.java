package com.prog.kafeecar;

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

    Lista citas;
    View view;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Lista citas_buscadas;
    private RecyclerItemClick itemClick;
    Lista citas_originales;

    public Adaptador_Lista_Cliente_Cita(Lista citas, Citas_cliente_fragment itemClick){
        this.citas=citas;
        this.citas_buscadas =citas;
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
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Cliente_Cita.clienteHolder holder, int position) {
        try {
            Cliente cienteactual=(Cliente)Patioventainterfaz.usuarioActual;
            citas=Patioventainterfaz.patioventa.getCitas().listabusqueda(cienteactual);
            Cita c=(Cita)citas.getPos(position);
            String hora= "Hora: "+c.getHora();
            String nombre= "Nombre: "+c.getCliente().getNombre();
            String telefono= "Telefono: "+c.getCliente().getTelefono();
            String matricula="Matricula N#= "+c.getVehiculo().getMatricula();
            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getVehiculo().getimagen());
            //TODO AJUSTAR INFORMACION A LA VISTA DE DESDE UN CLIENTE

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
            holder.matricula.setText(matricula);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return citas.contar();
    }

    public void buscar(String fecha){
        if(fecha.length()==0){
            citas.vaciar();
            citas.copiar(citas_originales);
        }else {
            citas.vaciar();
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

    public interface RecyclerItemClick {
        void itemClick(String placa,String cedula_cliente);
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
