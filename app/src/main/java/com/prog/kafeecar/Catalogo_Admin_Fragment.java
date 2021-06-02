package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.lang.String.format;

public class Catalogo_Admin_Fragment extends Fragment {
    private View mainView;
    private FloatingActionButton aniadirVehiculo;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_admin,container,false);
        aniadirVehiculo = mainView.findViewById(R.id.aniadir_vehiculo_btn);
        aniadirVehiculo.setOnClickListener(v -> {
            ScrollView verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
            LinearLayout verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
            aniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.VISIBLE);
        });
        return mainView;
    }





}
