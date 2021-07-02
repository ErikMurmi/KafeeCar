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

public class Adaptador_Lista_Catalogo extends RecyclerView.Adapter<Adaptador_Lista_Catalogo.clienteHolder> {
    private Lista autos_original;
    private Lista autos;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    View view;
    private RecyclerItemClick itemClick;

    public Adaptador_Lista_Catalogo(Lista autos, RecyclerItemClick itemClick){
        this.autos =autos;
        this.itemClick = itemClick;
        autos_original = new Lista();
        //autos.copiar(autos_original);
        for(int i=0; i<autos.contar();i++){
            try {
                autos_original.add(autos.getPos(i));
            } catch (Exception e) {
                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
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
            Vehiculo c=(Vehiculo) autos.getPos(position);
            String precio;
            String nombre= c.getMarca()+" "+c.getModelo();
            if (c.getPromocion()==0){
                 precio= "$ "+c.getPrecioVenta();
            }else{
                 precio= "$ "+c.getPromocion();
            }
            String matricula=c.getMatricula();
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
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imagenauto.setImageBitmap(bitmap);
                });
            }catch (IOException e){
                e.printStackTrace();
            }

            holder.nombre.setText(nombre);
            holder.precioauto.setText(precio);
            holder.matricula.setText(matricula);
            holder.placa.setText(placa);
            holder.anio.setText(anio);
            holder.itemView.setOnClickListener(v -> {
                itemClick.itemClick(placa);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return autos.contar();
    }

    public void filtro(String strBuscar){
        if(strBuscar.length()==0){
            autos.vaciar();
            for(int i=0; i<autos_original.contar();i++){
                try {
                    autos.add(autos_original.getPos(i));
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            autos.vaciar();
            for(int i=0; i<autos_original.contar();i++){
                Vehiculo actual=null;
                try {
                    actual = (Vehiculo) autos_original.getPos(i);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                if(actual.getPlaca().contains(strBuscar)){
                    autos.add(actual);
                }
            }
        }
        notifyDataSetChanged();
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
}

