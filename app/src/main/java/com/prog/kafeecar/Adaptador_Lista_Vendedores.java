package com.prog.kafeecar;

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

public class Adaptador_Lista_Vendedores extends RecyclerView.Adapter<Adaptador_Lista_Vendedores.clienteHolder> {
    Lista ven_original;
    Lista vendedores;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    View view;
    private RecyclerItemClick itemClick;
    public Adaptador_Lista_Vendedores(Lista vendedores, RecyclerItemClick itemClick){
        this.vendedores = vendedores;
        this.itemClick = itemClick;
        ven_original = new Lista();
        for(int i=0; i<vendedores.contar();i++){
            try {
                ven_original.add(vendedores.getPos(i));
            } catch (Exception e) {
                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendedor,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  clienteHolder holder, int position) {
        try {
            Vendedor vn = (Vendedor) vendedores.getPos(position);

            String nombre =vn.getNombre();
            String entrada = String.format("%d:00 %s",vn.getHoraEntrada(),Patioventainterfaz.formatoHora(vn.getHoraEntrada()));
            String almuerzo= String.format("%d:00 %s",vn.getHoraComida(),Patioventainterfaz.formatoHora(vn.getHoraComida()));
            String salida= String.format("%d:00 %s",vn.getHoraSalida(),Patioventainterfaz.formatoHora(vn.getHoraSalida()));

            StorageReference filePath = mStorageRef.child("Vendedores/"+vn.getImagen());
            try {
                final File localFile = File.createTempFile(vn.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri foto = Uri.parse(localFile.getAbsolutePath());
                    holder.imagenVendedor.setImageURI(foto);
                });
            }catch (IOException e){
                e.printStackTrace();
            }

            holder.nombre.setText(nombre);
            holder.horaEntrada.setText(entrada);
            holder.horaAlmuerzo.setText(almuerzo);
            holder.horaSalida.setText(salida);

            holder.itemView.setOnClickListener(v -> {
                itemClick.itemClick(vn.getCedula());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return vendedores.contar();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenVendedor;
        public TextView nombre;
        public TextView horaEntrada;
        public TextView horaAlmuerzo;
        public TextView horaSalida;

        public clienteHolder(@NonNull View view) {
            super(view);
            imagenVendedor = view.findViewById(R.id.AVimagenPerfil1_img);
            nombre = view.findViewById(R.id.AVnombre_txt);
            horaEntrada = view.findViewById(R.id.AVhoraEntrada_txt);
            horaAlmuerzo = view.findViewById(R.id.AVhoraAlmuerzo_txt);
            horaSalida = view.findViewById(R.id.AVhoraSalida_txt);

        }
    }

    public interface RecyclerItemClick{
        void itemClick(String cedula);
    }

    public void busqueda(String strBusqueda){
        if(strBusqueda.length()==0){
            vendedores.vaciar();
            vendedores.copiar(ven_original);
            /*for(int i=0; i<vendedores.contar();i++){
                try {
                    venBuscados.add(vendedores.getPos(i));
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }*/
        }else{
            vendedores.vaciar();
            for(int i=0; i<ven_original.contar();i++){
                Vendedor actual=null;
                try {
                    actual = (Vendedor) ven_original.getPos(i);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                if(actual.getCedula().contains(strBusqueda)){
                    vendedores.add(actual);
                }
            }
        }
        notifyDataSetChanged();
    }
}

