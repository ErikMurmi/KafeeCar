package com.prog.kafeecar;
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

public class Adaptador_Lista_Ventas extends RecyclerView.Adapter<Adaptador_Lista_Ventas.clienteHolder> {
    Lista ventas_original;
    Lista ventas_buscadas;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
   // public Adaptador_Lista_Catalogo.clienteHolder lyt;
    View view;
    private RecyclerItemClick itemClick;
    public Adaptador_Lista_Ventas(Lista ventas, RecyclerItemClick itemClick){
        this.ventas_buscadas =ventas;
        this.itemClick = itemClick;
        ventas_original=new Lista();
        ventas_original.copiar(ventas);
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Ventas.clienteHolder holder, int position) {
        try {
            Venta vt= (Venta) ventas_buscadas.getPos(position);
            String nombre= vt.getCliente().getNombre();
            String fecha = Patioventainterfaz.getFechaMod(vt.getFecha());
            String vendedor  = vt.getVendedor().getNombre();
            String placas = vt.getVehiculo().getPlaca();
            String precio = "$ "+ vt.getPrecio();
            Vehiculo actual = (Vehiculo) vt.getVehiculo();
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

            holder.itemView.setOnClickListener(v -> itemClick.itemClick(actual.getPlaca(),vt.getCliente().getCedula()));
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

    public static class clienteHolder extends RecyclerView.ViewHolder{
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

    /*public void filtro(String strBuscar){
        if(strBuscar.length()==0){
            ventas_buscadas.vaciar();
            ventas_buscadas.copiar(ventas_original);
        }else {
            ventas_buscadas.vaciar();
            for(int i=0; i<ventas_original.contar();i++){
                Vehiculo actual = (Vehiculo) ventas_original.getPos(i);
                if(actual.getPlaca().contains(strBuscar)){
                    ventas_buscadas.add(actual);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    public void buscar(String fecha){
        if(fecha.length()==0){
            ventas_buscadas.vaciar();
            ventas_buscadas.copiar(ventas_original);
        }else {
            ventas_buscadas.vaciar();
            for (int i = 0; i < ventas_original.contar(); i++) {
                Venta actual = (Venta) ventas_original.getPos(i);
                String f = Patioventainterfaz.getFechaMod(actual.getFecha());
                if (f.contains(fecha)) {
                    ventas_buscadas.add(actual);
                }
            }
        }

        notifyDataSetChanged();
    }

    public interface RecyclerItemClick{
        void itemClick(String placa, String cliente);
    }

/*
    @Override
    public void itemClick(ItemList item){

    }*/
}

