package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.lang.String.format;

public class Catalogo_Admin_Fragment extends Fragment {
    private FloatingActionButton aniadirVehiculo;
    private Button irEditar;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.catalogo_admin, container, false);
        aniadirVehiculo = mainView.findViewById(R.id.aniadir_vehiculo_btn);
        ScrollView verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
        LinearLayout verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
        ScrollView editar_vehiculo = mainView.findViewById(R.id.editar_vehiculo_lyt);
        aniadirVehiculo.setOnClickListener(v -> {
            aniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.VISIBLE);
        });

        irEditar = mainView.findViewById(R.id.ir_editar_vehiculo);
        irEditar.setOnClickListener(v -> {
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            irEditar.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.VISIBLE);
        });
        return mainView;
    }





}
