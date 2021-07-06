package com.prog.kafeecar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




public class Favoritos_cliente_fragment extends Fragment implements Adaptador_Lista_Favoritos.RecyclerItemClick {
    View mainview;
    Cliente cliente;
    PatioVenta patio;
    RecyclerView listaview;
    Context context;
    Adaptador_Lista_Favoritos adptadorlistaview;
    public Favoritos_cliente_fragment( Context contex){
        this.context=contex;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.favorito_cliente, container, false);
        patio = Patioventainterfaz.patioventa;

        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainview;
            }

        public void cargar() {
            RecyclerView listaview = mainview.findViewById(R.id.listafavoritos_Rv);
            RecyclerView.LayoutManager manager=new LinearLayoutManager(mainview.getContext());
            listaview.setLayoutManager(manager);
            listaview.setItemAnimator(new DefaultItemAnimator());
            adptadorlistaview=new Adaptador_Lista_Favoritos( patio.getVehiculos(), this);
            listaview.setAdapter(adptadorlistaview);
            listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
        }

    @Override
    public void itemClick(String placa) {
        int i =0;
    }
}






