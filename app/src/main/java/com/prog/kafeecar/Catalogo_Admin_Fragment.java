package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.lang.String.format;

public class Catalogo_Admin_Fragment extends Fragment {

    View mainView;

    private FloatingActionButton irAniadirVehiculo;
    private Button irEditar;
    private Button aniadir_vehiculo_btn;

    private ScrollView verVehiculo;
    private ScrollView editar_vehiculo;

    private LinearLayout verCatalogo;
    private ScrollView aniadir_vehiculo;

    private PatioVenta patio;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        //Botones
        irAniadirVehiculo = mainView.findViewById(R.id.ir_aniadir_btn);
        aniadir_vehiculo_btn = mainView.findViewById(R.id.aniadir_vehiculo_btn);

        //Layouts
        verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
        verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
        editar_vehiculo = mainView.findViewById(R.id.editar_vehiculo_lyt);
        aniadir_vehiculo =  mainView.findViewById(R.id.aniadir_vehiculo_lyt);

        irAniadirVehiculo.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            aniadir_vehiculo.setVisibility(View.VISIBLE);
        });

        irEditar = mainView.findViewById(R.id.ir_editar_vehiculo);
        irEditar.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            irEditar.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseadow
            editar_vehiculo.setVisibility(View.VISIBLE);
        });

        aniadir_vehiculo_btn.setOnClickListener(v -> {
            aniadirVehiculo();
        });
        return mainView;
    }

    public void aniadirVehiculo(){
        String msg = "";
        EditText placa_ad = mainView.findViewById(R.id.placa_aniadir_etxt);
        EditText matricula_ad = mainView.findViewById(R.id.aniadir_matricula_etxt);
        EditText anio_ad = mainView.findViewById(R.id.aniadir_anio_etxt);
        EditText descripcion_ad = mainView.findViewById(R.id.aniadir_descripcion_etxt);
        EditText marca_ad = mainView.findViewById(R.id.aniadir_marca_etxt);
        EditText modelo_ad = mainView.findViewById(R.id.aniadir_modelo_etxt);
        EditText color_ad = mainView.findViewById(R.id.aniadir_color_etxt);
        EditText pinicial_ad = mainView.findViewById(R.id.aniadir_pinicial_etxt);
        EditText pventa_ad = mainView.findViewById(R.id.aniadir_precio_venta_etxt);
        EditText ppromocion_ad = mainView.findViewById(R.id.aniadir_precio_promocion_etxt);
        CheckBox matriculado_ad = mainView.findViewById(R.id.matricula_chkbox);

        patio.aniadirVehiculo(
                new Vehiculo(
                        placa_ad.getText().toString(),
                        matricula_ad.getText().toString(),
                        marca_ad.getText().toString(),
                        modelo_ad.getText().toString(),
                        color_ad.getText().toString(),
                        descripcion_ad.getText().toString(),
                        Float.parseFloat(pinicial_ad.getText().toString()),
                        Float.parseFloat(pventa_ad.getText().toString()),
                        Float.parseFloat(ppromocion_ad.getText().toString()),
                        matriculado_ad.isChecked(),
                        Integer.parseInt(anio_ad.getText().toString()),
                        String.format(placa_ad.getText().toString()+".jpg")
                )
        );
        try {
            if(patio.buscarVehiculos("Placa",placa_ad.getText().toString())!=null)
                msg="Se agrego correctamente el vehiculo";
        } catch (Exception e) {
            msg="No se agrego el vehiculo";
        }
        Toast.makeText(mainView.getContext(), msg, Toast.LENGTH_SHORT).show();
    }



}
