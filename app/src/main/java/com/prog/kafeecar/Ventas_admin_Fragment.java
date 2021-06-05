package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Ventas_admin_Fragment extends Fragment {
    private View mainView;
    private PatioVenta patio;
    private FloatingActionButton irAniadirVehiculo;
    private Button irVentasPorVendedor;
    private Button irVentasGenerales;
    private Button irRegistrarNuevaVenta;
    private LinearLayout verVentasVendedor;
    private LinearLayout verVentasGenerales;
    private LinearLayout verRegistroNuevaVenta;
    private LinearLayout verVentasAdmin;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ventas_admin, container, false);
        patio = Patioventainterfaz.patioventa;

        //Botones
        irVentasPorVendedor=mainView.findViewById(R.id.administrar_venta_vendedor_btn);
        irVentasGenerales=mainView.findViewById(R.id.administrar_venta_generales_btn);
        irRegistrarNuevaVenta=mainView.findViewById(R.id.administrar_venta_aniadirventa_btn);
        // layouts
        verVentasVendedor=mainView.findViewById(R.id.ventas_admin_por_vendedor_lyt);
        verVentasGenerales=mainView.findViewById(R.id.ventas_admin_generales_lyt);
        verRegistroNuevaVenta= mainView.findViewById(R.id.adminregistro_lyt);
        verVentasAdmin=mainView.findViewById(R.id.ventas_admin_lyt);

        irVentasPorVendedor.setOnClickListener(view ->{
            irVentasPorVendedor.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.GONE);
            verVentasVendedor.setVisibility(View.VISIBLE);

        } );

        irVentasGenerales.setOnClickListener(view -> {
            irVentasGenerales.setVisibility(View.GONE);
            verVentasVendedor.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.VISIBLE);

        });

        irRegistrarNuevaVenta.setOnClickListener(view -> {
            irRegistrarNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.GONE);
            verVentasVendedor.setVisibility(View.GONE);
            verVentasAdmin.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.VISIBLE);

        });
        

        return mainView;
    }
}
