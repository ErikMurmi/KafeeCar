package com.prog.kafeecar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adaptador_Lista_Catalogo extends RecyclerView.Adapter<Adaptador_Lista_Catalogo.clienteHolder> {
    Lista favoritos;
    public Adaptador_Lista_Catalogo(Lista fav){

        this.favoritos=fav;
    }

    @NonNull
    @Override
    public clienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantallitacita,parent,false);
        return new clienteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adaptador_Lista_Catalogo.clienteHolder holder, int position) {
        try {
            Cliente cienteactual=(Cliente)Patioventainterfaz.usuarioActual;
            favoritos=Patioventainterfaz.patioventa.getCitas().listabusqueda(cienteactual);

            Vehiculo c=(Vehiculo)favoritos.getPos(position);
            //Drawable foto= (Drawable)c.getimagen();
            String precio="";
            String nombre= "Auto: "+c.getMarca()+" "+c.getModelo();
            if (c.getPromocion()==0){
                 precio= " Precio "+c.getPrecioVenta();
            }else{
                 precio= " Precio promoción "+c.getPromocion();
            }
            String matricula="Matricula N#= "+c.getMatricula();


            //holder.imagenauto.setBackground(R.);
            holder.nombre.setText(nombre);
            holder.precioauto.setText(precio);
            holder.matricula.setText(matricula);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return favoritos.contar();
    }

    public class clienteHolder extends RecyclerView.ViewHolder{
        public ImageView imagenauto;
        public TextView precioauto;
        public TextView nombre;
        public TextView matricula;



        public clienteHolder(@NonNull View view) {
            super(view);
            imagenauto=view.findViewById(R.id.fotoauto_Lista_cliente_pantallita_img);
            precioauto=view.findViewById(R.id.precio_lista_cliente_pantallita_txt);
            nombre=view.findViewById(R.id.nombreauto_lista_cliente_pantallita_txt);
            matricula=view.findViewById(R.id.matricula_lista_cliente_pantallita_txt);
        }
    }
}

