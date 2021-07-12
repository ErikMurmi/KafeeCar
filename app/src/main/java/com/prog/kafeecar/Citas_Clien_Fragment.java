package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Citas_Clien_Fragment extends Fragment implements Adaptador_Lista_Cliente_Cita.RecyclerItemClick, SearchView.OnQueryTextListener{

    private PatioVenta patio;

    private View mainView;
    private SearchView busqueda_c_cli_srv;
    private Adaptador_Lista_Cliente_Cita adptadorlistaview;
    private Cliente cliente_actual =(Cliente) Patioventainterfaz.usuarioActual;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = inflater.inflate(R.layout.citas_cliente, container, false);
        busqueda_c_cli_srv = mainView.findViewById(R.id.busqueda_c_cli_srv);
        patio = Patioventainterfaz.patioventa;
        busqueda_c_cli_srv.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }

    public void cargar() {
        RecyclerView listacitas_rv = mainView.findViewById(R.id.listacitas_Rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listacitas_rv.setLayoutManager(manager);
        listacitas_rv.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Cliente_Cita(patio.buscarCitas(cliente_actual), this);
        listacitas_rv.setAdapter(adptadorlistaview);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void itemClick(String placa, String cedula_cliente) {

    }
}
