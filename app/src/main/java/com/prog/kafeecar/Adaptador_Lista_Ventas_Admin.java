package com.prog.kafeecar;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Adaptador_Lista_Ventas_Admin extends RecyclerView.Adapter<Adaptador_Lista_Ventas_Admin.clienteHolder> {
    Lista ventas_original;
    Lista ventas_buscadas;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
   // public Adaptador_Lista_Catalogo.clienteHolder lyt;
    View view;
    private RecyclerItemClick itemClick;
    public Adaptador_Lista_Ventas_Admin(Lista ventas, RecyclerItemClick itemClick){
        this.ventas_original =ventas;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Ventas_Admin.clienteHolder holder, int position) {
        try {
            Venta vt= (Venta) ventas_original.getPos(position);
            String nombre= vt.getComprador().getNombre();
            String fecha = Patioventainterfaz.getFechaMod(vt.getFecha());
            String vendedor  = vt.getVendedor().getNombre();
            String placas = "";
            for (int i=0;i<vt.getVehiculos().contar();i++){
                Vehiculo actual = (Vehiculo) vt.getVehiculos().getPos(i);
                if(i==0){
                    placas += actual.getPlaca();
                }else{
                    placas += " ,"+actual.getPlaca();
                }
            }
            String precio =String.valueOf(vt.getPrecio());
            Vehiculo actual = (Vehiculo) vt.getVehiculos().getPos(0);
            StorageReference filePath = mStorageRef.child("Vehiculos/"+actual.getimagen());

            try {

                final File localFile = File.createTempFile(actual.getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri foto= Uri.parse(localFile.getAbsolutePath());
                    holder.imagenauto.setImageURI(foto);
                });
            }catch (IOException e){
                e.printStackTrace();
            }


            holder.cliente.setText(nombre);
            holder.fecha.setText(fecha);
            holder.vendedor.setText(vendedor);
            holder.placas.setText(placas);
            holder.precio.setText(precio);

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
        return ventas_buscadas.contar();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenauto;
        public TextView vendedor;
        public TextView fecha;
        public TextView cliente;
        public TextView placas;
        public TextView precio;

        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.carro_lista_img);
            vendedor=view.findViewById(R.id.vt_vendedor_lista_txt);
            fecha =view.findViewById(R.id.vt_fecha_lista_txt);
            cliente = view.findViewById(R.id.vt_cliente_txt);
            placas = view.findViewById(R.id.vt_placas_lista_txt);
            precio = view.findViewById(R.id.vt_carro_precio_lista_txt);
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

