package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Ventas_admin_Fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick, SearchView.OnQueryTextListener {
    private View mainView;
    private SearchView busqueda_ventas;
    private PatioVenta patio;
    private Button irVentasGenerales;
    private Button irRegistrarNuevaVenta;
    private Button realizarVenta;
    private LinearLayout verVentasGenerales;
    private LinearLayout verRegistroNuevaVenta;
    private LinearLayout verVentasAdmin;
    private Adaptador_Lista_Ventas adptadorlistaview;
    private TextView contar;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ventas_admin, container, false);
        patio = Patioventainterfaz.patioventa;

        //Botones
        irVentasGenerales=mainView.findViewById(R.id.administrar_venta_generales_btn);
        irRegistrarNuevaVenta=mainView.findViewById(R.id.administrar_venta_aniadirventa_btn);
        realizarVenta=mainView.findViewById(R.id.aniadirVenta_btn);

        // layouts
        verVentasGenerales=mainView.findViewById(R.id.ventas_admin_generales_lyt);
        verRegistroNuevaVenta= mainView.findViewById(R.id.adminregistro_lyt);
        verVentasAdmin=mainView.findViewById(R.id.ventas_admin_lyt);


        irVentasGenerales.setOnClickListener(view -> {
            irVentasGenerales.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.VISIBLE);

        });

        irRegistrarNuevaVenta.setOnClickListener(view -> {
            irRegistrarNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.GONE);
            verVentasAdmin.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.VISIBLE);

        });
        realizarVenta.setOnClickListener(view -> {

            try {
                aniadirVenta();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        contar= mainView.findViewById(R.id.contar_txt);
        contar.setText(String.valueOf(patio.getVehiculos().contar()));

        cargar();

        return mainView;
    }

    public void aniadirVenta() throws Exception {
        Venta nueva=null;
        Cliente clienteventa=null;
        Vendedor vendedorventa=null;
        Vehiculo autoventa =null;


        EditText fechaventadia = mainView.findViewById(R.id.fecha_venta_dia_etxt);
        EditText fechaventames = mainView.findViewById(R.id.fecha_venta_mes_etxt);
        EditText fechaventaanio = mainView.findViewById(R.id.fecha_venta_anio_etxt);

        String fechaventa_str ="";



        int c = 0;

        if((!isEmpty(fechaventadia) && !isEmpty(fechaventames)) && (!isEmpty(fechaventaanio))){
            fechaventa_str = fechaventaanio.getText().toString() + "-" + fechaventames.getText().toString() + "-" + fechaventadia.getText().toString();
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campos de fecha vacios",Toast.LENGTH_SHORT).show();

        }
        EditText clientes = mainView.findViewById(R.id.cliente_venta_txt);
        EditText vendedor = mainView.findViewById(R.id.vendedor_venta_txt);
        EditText auto = mainView.findViewById(R.id.vehiculo_venta_txt);
        if(!isEmpty(clientes)){
            String clientes_str = clientes.getText().toString();
            clienteventa = patio.buscarClientes("Nombre", clientes_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campo de cliente vacio",Toast.LENGTH_SHORT).show();
        }
        if(!isEmpty(vendedor)){
            String vendedores_str = vendedor.getText().toString();
            vendedorventa = patio.buscarVendedores("Nombre", vendedores_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campo de Vendedor vacio",Toast.LENGTH_SHORT).show();
        }
        if(!isEmpty(auto)){
            String autos_str = auto.getText().toString();
            autoventa = patio.buscarVehiculos("Placa", autos_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campo de Vehiculo vacio",Toast.LENGTH_SHORT).show();
        }



        if(c==0){
            nueva = new Venta(Patioventainterfaz.sdf.parse(fechaventa_str), clienteventa, vendedorventa, autoventa);
            patio.aniadirVenta(nueva);
            if (patio.getVentasGenerales().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se registro la venta.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void cargar(){
        RecyclerView listaview = mainView.findViewById(R.id.lista_ventas_admin_rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Ventas(patio.getVentasGenerales(), this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String b = newText;
        if(newText.length()==2){
            b+="-";
            busqueda_ventas.setQuery(b,false);
        }
        if(newText.length()==5){
            b+="-";
            busqueda_ventas.setQuery(b,false);
        }
        if(newText.length()>10){
            b= b.substring(0,10);
            busqueda_ventas.setQuery(b,false);
        }
        adptadorlistaview.filtro(b);
        return false;
    }

    @Override
    public void itemClick(String placa, String cliente) {
        Toast.makeText(mainView.getContext(), "Se registro la venta.", Toast.LENGTH_SHORT).show();
    }
}
