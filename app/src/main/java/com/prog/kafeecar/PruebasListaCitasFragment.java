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

public class PruebasListaCitasFragment extends Fragment {
    View mainView;
    PatioVenta patio;
    RecyclerView listaview;
    Context context;
    Adaptador_Lista_Cliente_Cita adptadorlistaview;
    public PruebasListaCitasFragment( Context contex){
        this.context=contex;
    }
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.lista_citas, container, false);
        patio = Patioventainterfaz.patioventa;
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainView;
    }
    public void cargar() throws Exception {
        Cita cita1=(Cita)patio.getCitas().getInicio().getDato();
        listaview=mainView.findViewById(R.id.listacitas_Rv);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(context);
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview=new Adaptador_Lista_Cliente_Cita( patio.getCitas().listabusqueda(cita1.getVisitante()));
        listaview.setAdapter(adptadorlistaview);
        listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }

}
