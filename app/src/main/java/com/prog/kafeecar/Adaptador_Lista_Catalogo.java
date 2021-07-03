package com.prog.kafeecar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class Adaptador_Lista_Catalogo extends RecyclerView.Adapter<Adaptador_Lista_Catalogo.clienteHolder> {
    private Lista autos_original;
    private Lista autos_buscados;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    View view;
    private RecyclerItemClick itemClick;

    public Adaptador_Lista_Catalogo(Lista autos, RecyclerItemClick itemClick){
        this.autos_buscados =autos;
        this.itemClick = itemClick;
        autos_original = new Lista();
        autos_original.copiar(autos);
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Catalogo.clienteHolder holder, int position) {
        try {
            Vehiculo c=(Vehiculo) autos_buscados.getPos(position);
            String precio;
            String modelo=c.getModelo();
            if (c.getPromocion()==0){
                 precio= "$ "+c.getPrecioVenta();
            }else{
                 precio= "$ "+c.getPromocion();
            }
            String marca=c.getMarca();
            //TODO
            /*
            Cambiar matricula por marca y titulo solo modelo
            * */
            String placa = c.getPlaca();
            String anio = String.valueOf(c.getAnio());
            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getimagen());
            try {
                final File localFile = File.createTempFile(c.getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri foto = Uri.parse(localFile.getAbsolutePath());
                    holder.imagenauto.setImageURI(foto);
                });
            }catch (IOException e){
                e.printStackTrace();
            }

            holder.modelo.setText(modelo);
            holder.precioauto.setText(precio);
            holder.marca.setText(marca);
            holder.placa.setText(placa);
            holder.anio.setText(anio);
            holder.itemView.setOnClickListener(v -> itemClick.itemClick(placa));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return autos_buscados.contar();
    }

    public void filtro(String strBuscar){
        if(strBuscar.length()==0){
            autos_buscados.vaciar();
            autos_buscados.copiar(autos_original);
        }else {
            autos_buscados.vaciar();
            for(int i=0; i<autos_original.contar();i++){
                Vehiculo actual=null;
                try {
                    actual = (Vehiculo) autos_original.getPos(i);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                if(actual.getPlaca().contains(strBuscar)){
                    autos_buscados.add(actual);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenauto;
        public TextView precioauto;
        public TextView anio;
        public TextView modelo;
        public TextView marca;
        public TextView placa;

        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.v_lista_img);
            precioauto=view.findViewById(R.id.v_precio_lista_txt);
            modelo=view.findViewById(R.id.v_marca_modelo_txt);
            marca=view.findViewById(R.id.v_matricula_lista_txt);
            placa = view.findViewById(R.id.v_placa_lista_txt);
            anio = view.findViewById(R.id.v_anio_lista_txt);
        }
    }

    public interface RecyclerItemClick{
        void itemClick(String placa);
    }
}

