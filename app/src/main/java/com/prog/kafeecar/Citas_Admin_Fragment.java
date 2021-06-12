package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.prog.kafeecar.Patioventainterfaz.getFechaMod;

public class Citas_Admin_Fragment extends Fragment {

    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Citas_Admin";
    private PatioVenta patio;
    private View mainView;

    //Botones
    private Button irVerCita;
    private Button irAniadirCita;

    private LinearLayout verCita;
    private LinearLayout listaCitas;
    private LinearLayout aniadirCita;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_admin,container,false);
        patio = Patioventainterfaz.patioventa;
        //Layouts
        verCita = mainView.findViewById(R.id.ver_cita_lyt);
        aniadirCita = mainView.findViewById(R.id.add_cita_admin_lyt);
        listaCitas = mainView.findViewById(R.id.citas_admin_lyt);

        //Botones
        irVerCita = mainView.findViewById(R.id.ir_ver_cita);
        irAniadirCita = mainView.findViewById(R.id.ir_editar_cita);

        irVerCita.setOnClickListener(v -> {
            aniadirCita.setVisibility(View.GONE);
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.VISIBLE);
            try {
                visualizarCita();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        irAniadirCita.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.VISIBLE);
        });
        return mainView;
    }



    public void visualizarCita() throws Exception {

        TextView fecha = mainView.findViewById(R.id.fechaCita_txt);
        TextView hora = mainView.findViewById(R.id.horaCita_txt);
        TextView cliente = mainView.findViewById(R.id.clienteCita_txt);
        TextView contacto = mainView.findViewById(R.id.contactoCita_txt);
        TextView vendedor = mainView.findViewById(R.id.vendedorCita_txt);
        TextView vehiculo = mainView.findViewById(R.id.vehiculoCita_txt);
        TextView descripcion = mainView.findViewById(R.id.descripcionCita_txt);
        TextView resolucion = mainView.findViewById(R.id.resolucionCita_txt);
        TextView precio = mainView.findViewById(R.id.precioVentaCita_txt);
        Cita citaPrueba = (Cita) patio.getCitas().getPos(0);
        fecha.setText(new String(fecha.getText().toString() + getFechaMod(citaPrueba.getFechaCita())));
        hora.setText(new String(hora.getText().toString() + citaPrueba.getHora()));
        cliente.setText(new String(cliente.getText().toString() + citaPrueba.getVisitante().getNombre()));
        contacto.setText(new String(contacto.getText().toString() + citaPrueba.getVisitante().getTelefono()));
        vendedor.setText(new String(vendedor.getText().toString() + citaPrueba.getVendedorCita().getNombre()));
        vehiculo.setText(new String(vehiculo.getText().toString() + citaPrueba.getVehiculo().getModelo()));
        descripcion.setText(new String(descripcion.getText().toString() + citaPrueba.getVehiculo().getDescripcion()));
        resolucion.setText(new String(resolucion.getText().toString() + citaPrueba.getResolucion()));
        precio.setText(new String(precio.getText().toString() + " $" + citaPrueba.getVehiculo().getPrecioVenta()));

    }


}
