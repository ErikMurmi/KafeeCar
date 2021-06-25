package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Ventas_vendedor_fragment extends Fragment{
    private View mainview;
    private LinearLayout verventa;
    private LinearLayout aniadirventa;
    private LinearLayout editarventa;
    private Button aniadir;
    private Button descartar;
    private Button actualizar;
    private Button guardar;
    private PatioVenta patioventa = new PatioVenta();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.home_vendedor, container, false);


        //Botones
        //aniadir = mainview.findViewById(R.id.aniadir_btn);
       // descartar = mainview.findViewById(R.id.descartar_btn);
        //actualizar = mainview.findViewById(R.id.actualizar_btn);
        guardar = mainview.findViewById(R.id.guardar_clita_nueva_btn);

        //Layouts
        //verventa = mainview.findViewById(R.id.verventa_layout);
        //aniadirventa = mainview.findViewById(R.id.aniadirventa_layout);
        //editarventa = mainview.findViewById(R.id.editarventa_layout);
        /*
        aniadir.setOnClickListener(v ->{
            try {
                aniadirVenta();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        descartar.setOnClickListener(v ->{
            mainview.setVisibility(View.VISIBLE);
            verventa.setVisibility(View.GONE);
            aniadirventa.setVisibility(View.GONE);
            editarventa.setVisibility(View.GONE);
        });

        actualizar.setOnClickListener(v ->{

        });

        guardar.setOnClickListener(v ->{

        });*/
        return mainview;
    }

    public void aniadirVenta() throws Exception {
        EditText precio= mainview.findViewById(R.id.precio_venta_txt);
        EditText clientes= mainview.findViewById(R.id.cliente_venta_txt);
        EditText vendedor= mainview.findViewById(R.id.vendedor_venta_txt);
        EditText auto = mainview.findViewById(R.id.vehiculo_venta_txt);
        EditText fechaventadia= mainview.findViewById(R.id.fecha_venta_dia_etxt);
        EditText fechaventames= mainview.findViewById(R.id.fecha_venta_mes_etxt);
        EditText fechaventaanio= mainview.findViewById(R.id.fecha_venta_anio_etxt);
        EditText fechaventahora= mainview.findViewById(R.id.fecha_venta_hora_etxt);
        String fechaventa_str=fechaventaanio.getText().toString()+"-"+fechaventames.getText().toString()+"-"+fechaventadia.getText().toString();
        String clientes_str=clientes.getText().toString();
        String vendedores_str= vendedor.getText().toString();
        String autos_str = auto.getText().toString();
        int hora= Integer.parseInt(fechaventahora.getText().toString());
        float precioventa= Float.parseFloat(precio.getText().toString());
        Cliente clienteventa= patioventa.buscarClientes("Nombre",clientes_str);
        Vendedor vendedorventa= patioventa.buscarVendedores("Nombre",vendedores_str);
        Vehiculo autoventa= patioventa.buscarVehiculos("Matricula",autos_str);


        Venta nueva= new Venta(sdf.parse(fechaventa_str),precioventa,clienteventa,vendedorventa,autoventa);
        patioventa.aniadirVenta(nueva);

        if(patioventa.getVentasGenerales().contiene(nueva)){
            Toast.makeText(mainview.getContext(),"Se registro la venta.",Toast.LENGTH_SHORT).show();
        }

    }
}
