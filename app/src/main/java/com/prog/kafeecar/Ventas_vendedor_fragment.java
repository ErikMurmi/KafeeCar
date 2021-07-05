package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;

public class Ventas_vendedor_fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick {
    private View mainview;
    private LinearLayout verventa;
    private LinearLayout aniadirventa;
    private LinearLayout editarventa;
    private LinearLayout lista_ventas;
    private FloatingActionButton aniadir;
    private Button eliminar;
    private Button actualizar;
    private Button guardar;
    private PatioVenta patio;
    private Vendedor vendedor_actual = (Vendedor) Patioventainterfaz.usuarioActual;
    private Venta venta_mostrar;
    private Adaptador_Lista_Ventas adaptadorVentas;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.ventas_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;

        //Botones
        aniadir = mainview.findViewById(R.id.aniadir_vt_vn_ftbn);

        eliminar = mainview.findViewById(R.id.eliminar_vt_vn_btn);
        //actualizar = mainview.findViewById(R.id.actualizar_btn);
        //guardar = mainview.findViewById(R.id.guardar_clita_nueva_btn);

        //Layouts
        lista_ventas = mainview.findViewById(R.id.lista_vt_vn_lyt);
        verventa = mainview.findViewById(R.id.ver_vt_vn_lyt);
        //aniadirventa = mainview.findViewById(R.id.aniadirventa_layout);
        //editarventa = mainview.findViewById(R.id.editarventa_layout);
        eliminar.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainview.getContext());
            msg.setTitle("Eliminar Venta");
            msg.setMessage("¿Estás seguro de eliminar esta venta?");
            Vehiculo vh = (Vehiculo)venta_mostrar.getVehiculos().getPos(0);
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    patio.removerVenta(vh.getPlaca());
                    irVerVentas();
                    //TODO
                    //Mensaje para aniadir los vehiculos de nuevo al catalogo
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        cargar();
        return mainview;
    }

    public void cargar(){
        RecyclerView listaview = mainview.findViewById(R.id.rc_ventas_vendedor);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainview.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adaptadorVentas = new Adaptador_Lista_Ventas(vendedor_actual.obtenerVentas(), this);
        listaview.setAdapter(adaptadorVentas);
    }

    public void irVerVentas(){
        cargar();
        lista_ventas.setVisibility(View.VISIBLE);
        verventa.setVisibility(View.GONE);
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
        Cliente clienteventa= patio.buscarClientes("Nombre",clientes_str);
        Vendedor vendedorventa= patio.buscarVendedores("Nombre",vendedores_str);
        Vehiculo autoventa= patio.buscarVehiculos("Matricula",autos_str);


        Venta nueva= new Venta(sdf.parse(fechaventa_str),clienteventa,vendedorventa,autoventa);
        patio.aniadirVenta(nueva);

        if(patio.getVentasGenerales().contiene(nueva)){
            Toast.makeText(mainview.getContext(),"Se registro la venta.",Toast.LENGTH_SHORT).show();
        }

    }

    public void verVenta(){
        lista_ventas.setVisibility(View.GONE);
        verventa.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClick(String placa, String cliente) {
        try {
            venta_mostrar = patio.buscarVentas(placa,cliente);
            if(venta_mostrar!=null){
                verVenta();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
