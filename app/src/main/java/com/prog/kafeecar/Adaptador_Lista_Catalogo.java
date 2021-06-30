package com.prog.kafeecar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Adaptador_Lista_Catalogo extends RecyclerView.Adapter<Adaptador_Lista_Catalogo.clienteHolder> {
    Lista autos;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
   // public Adaptador_Lista_Catalogo.clienteHolder lyt;
    View view;
    private RecyclerItemClick itemClick;
    public Adaptador_Lista_Catalogo(Lista fav, RecyclerItemClick itemClick){

        this.autos=fav;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantallita_auto,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Catalogo.clienteHolder holder, int position) {
        try {
            Vehiculo c=(Vehiculo)autos.getPos(position);
            String precio="";
            String nombre= c.getMarca()+" "+c.getModelo();
            if (c.getPromocion()==0){
                 precio= "$ "+c.getPrecioVenta();
            }else{
                 precio= "$ "+c.getPromocion();
            }
            String matricula=c.getMatricula();
            String placa = c.getPlaca();
            String anio = String.valueOf(c.getAnio());
            StorageReference filePath = mStorageRef.child("Vehiculos/"+c.getimagen());
            Glide.with(view)
                    .load(filePath)
                    .into(holder.imagenauto);
            try {
                final File localFile = File.createTempFile(c.getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imagenauto.setImageBitmap(bitmap);
                });
            }catch (IOException e){
                e.printStackTrace();
            }

            //lyt = holder;
            holder.nombre.setText(nombre);
            holder.precioauto.setText(precio);
            holder.matricula.setText(matricula);
            holder.placa.setText(placa);
            holder.anio.setText(anio);
            holder.itemView.setOnClickListener(v -> {
                itemClick.itemClick(placa);
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
        return autos.contar();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenauto;
        public TextView precioauto;
        public TextView anio;
        public TextView nombre;
        public TextView matricula;
        public TextView placa;

        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.v_lista_img);
            precioauto=view.findViewById(R.id.v_precio_lista_txt);
            nombre=view.findViewById(R.id.v_marca_modelo_txt);
            matricula=view.findViewById(R.id.v_matricula_lista_txt);
            placa = view.findViewById(R.id.v_placa_lista_txt);
            anio = view.findViewById(R.id.v_anio_lista_txt);
        }
    }

    public interface RecyclerItemClick{
        void itemClick(String placa);
    }
/*
    @Override
    public void itemClick(ItemList item){

    }*/
}

