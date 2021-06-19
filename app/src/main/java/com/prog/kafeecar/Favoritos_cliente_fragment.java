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




public class Favoritos_cliente_fragment extends Fragment{
    private View mainview;
    Cliente cliente;
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
        cliente = Patioventainterfaz.clientefav;

        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainview;
            }
            public void cargar() throws Exception {
                Cliente autofavorito =(Cliente)cliente.getFavoritos().getInicio().getDato();
                listaview=mainview.findViewById(R.id.listafavoritos_Rv);
                RecyclerView.LayoutManager manager=new LinearLayoutManager(context);
                listaview.setLayoutManager(manager);
                listaview.setItemAnimator(new DefaultItemAnimator());
                adptadorlistaview=new Adaptador_Lista_Favoritos( cliente.getFavoritos().listabusqueda(autofavorito.getFavoritos()));
                listaview.setAdapter(adptadorlistaview);
                listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
            }

        }


