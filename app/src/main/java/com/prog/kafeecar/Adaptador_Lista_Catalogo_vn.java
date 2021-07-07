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

public class Adaptador_Lista_Catalogo_vn extends RecyclerView.Adapter<Adaptador_Lista_Catalogo_vn.clienteHolder> {
    private Lista autos_vn_original;
    private Lista autos_vn_buscados;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    View view;
    private RecyclerItemClick itemClick;

    public Adaptador_Lista_Catalogo_vn(Lista autos, RecyclerItemClick itemClick){
        this.autos_vn_buscados =autos;
        this.itemClick = itemClick;
        autos_vn_original = new Lista();
        autos_vn_original.copiar(autos);
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_vn,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Catalogo_vn.clienteHolder holder, int position) {
        try {
            Vehiculo c =(Vehiculo) autos_vn_buscados.getPos(position);
            String precio;
            String modelo = c.getModelo().toUpperCase();
            if (c.getPromocion()==0){
                 precio = "$ "+c.getPrecioVenta();
            }else{
                 precio = "$ "+c.getPromocion();
            }
            String marca = c.getMarca();
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
        return autos_vn_buscados.contar();
    }

    public void filtro(String strBuscar){
        if(strBuscar.length()==0){
            autos_vn_buscados.vaciar();
            autos_vn_buscados.copiar(autos_vn_original);
        }else {
            autos_vn_buscados.vaciar();
            for(int i=0; i<autos_vn_original.contar();i++){
                Vehiculo actual=null;
                try {
                    actual = (Vehiculo) autos_vn_original.getPos(i);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                if(actual.getPlaca().contains(strBuscar)){
                    autos_vn_buscados.add(actual);
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
            imagenauto=view.findViewById(R.id.v_lista_vendedor_img);
            precioauto=view.findViewById(R.id.v_precio_lista_vendedor_txt);
            modelo=view.findViewById(R.id.v_modelo_lista_vendedor_txt);
            marca=view.findViewById(R.id.v_marca_lista_vendedor_txt);
            placa = view.findViewById(R.id.v_placa_lista_vendedor_txt);
            anio = view.findViewById(R.id.v_anio_lista_vendedor_txt);
        }
    }

    public interface RecyclerItemClick{
        void itemClick(String placa);
    }
}

