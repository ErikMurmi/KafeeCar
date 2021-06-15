package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private Button realizarVenta;
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
        realizarVenta=mainView.findViewById(R.id.aniadirVenta_btn);

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
        realizarVenta.setOnClickListener(view -> {

            try {
                aniadirVenta();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

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
        EditText fechaventahora = mainView.findViewById(R.id.fecha_venta_hora_etxt);

        String fechaventa_str ="";



        float precioventa =0;
        int c = 0;
        int hora =-1;

        if((!isEmpty(fechaventadia) && !isEmpty(fechaventames)) && (!isEmpty(fechaventaanio) && !isEmpty(fechaventahora))){
            fechaventa_str = fechaventaanio.getText().toString() + "-" + fechaventames.getText().toString() + "-" + fechaventadia.getText().toString();
            hora=Integer.parseInt(fechaventahora.getText().toString());
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

        EditText precio = mainView.findViewById(R.id.precio_venta_txt);
        if(!isEmpty(precio)){
            precioventa = Float.parseFloat(precio.getText().toString());
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campo de precio vacio",Toast.LENGTH_SHORT).show();

        }


        if(c==0){
            nueva = new Venta(Patioventainterfaz.sdf.parse(fechaventa_str), precioventa, clienteventa, vendedorventa, autoventa);
            patio.aniadirVenta(nueva);
            if (patio.getVentasGenerales().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se registro la venta.", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
