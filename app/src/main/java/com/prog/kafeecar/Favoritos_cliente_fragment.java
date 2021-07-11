package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Favoritos_cliente_fragment extends Fragment implements Adaptador_Lista_Catalogo.RecyclerItemClick, SearchView.OnQueryTextListener {
    public Catalogo_Cliente_fragment clienteF;
    private View mainView;
    private View mainView2;
    private PatioVenta patio;
    private Vehiculo vMostrar;
    private LinearLayout verCatalogofav;
    private ScrollView vistaVehiculo;
    private final Cliente cliente_actual = (Cliente) Patioventainterfaz.usuarioActual;
    private Adaptador_Lista_Catalogo adptadorlistaview;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.favoritos_cliente, container, false);

        SearchView busqueda_placa = mainView.findViewById(R.id.busqueda_placa_fav_bar);
        patio = Patioventainterfaz.patioventa;
        //vistaVehiculo = mainView.findViewById(R.id.vista_vehiculo_VV_scl);
        verCatalogofav = mainView.findViewById(R.id.favorito_cliente_lyt);

        //Metodo para el control del boton atras
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (vistaVehiculo.getVisibility() == View.VISIBLE) {
                    clienteF.irCatalogo();
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        busqueda_placa.setOnQueryTextListener(this);
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainView;
    }

    public void irVer() {

        clienteF.visualizarVehiculo();
    }

    public void cargar() throws Exception {
        RecyclerView listaview = mainView.findViewById(R.id.listafavoritos_Rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.buscarVehiculosFav("Placa", cliente_actual.getFavoritos()), this);
        //adptadorlistaview=new Adaptador_Lista_Favoritos( patio.getVehiculos(),this);
        listaview.setAdapter(adptadorlistaview);
    }


    @Override
    public void itemClick(String placa) {
        //irVer(placa);
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            //verCatalogofav.setVisibility(View.GONE);
            //vistaVehiculo.setVisibility(View.VISIBLE);
            //clienteF.visualizarVehiculo();
            irVer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        try {
            adptadorlistaview.buscar(newText,"Placa");
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    // @Override
    //public void itemClick(String placa) {
    //   int i=0;
    //}
}






