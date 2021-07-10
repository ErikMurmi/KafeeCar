package com.prog.kafeecar;

import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Ventas_vendedor_fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick, SearchView.OnQueryTextListener {
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private View mainView;
    private SearchView busqueda_ventas;
    private LinearLayout ver_vt_vn_lyt;
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
        mainView = inflater.inflate(R.layout.ventas_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;
        busqueda_ventas = mainView.findViewById(R.id.busqueda_vt_vn_srv);
        //Botones
        aniadir = mainView.findViewById(R.id.aniadir_vt_vn_ftbn);

        eliminar = mainView.findViewById(R.id.vt_vn_ver_venta_eliminar_btn);
        //actualizar = mainview.findViewById(R.id.actualizar_btn);
        //guardar = mainview.findViewById(R.id.guardar_clita_nueva_btn);

        //Layouts
        lista_ventas = mainView.findViewById(R.id.lista_vt_vn_lyt);
        ver_vt_vn_lyt = mainView.findViewById(R.id.ver_vt_vn_lyt);
        //aniadirventa = mainview.findViewById(R.id.aniadirventa_layout);
        //editarventa = mainview.findViewById(R.id.editarventa_layout);
        eliminar.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Eliminar Venta");
            msg.setMessage("¿Estás seguro de eliminar esta venta?");
            Vehiculo vh = (Vehiculo)venta_mostrar.getVehiculo();
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    patio.removerVenta(vh.getPlaca());
                    irVerListaVentas();
                    //TODO
                    //Mensaje para aniadir los vehiculos de nuevo al catalogo
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });
        busqueda_ventas.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }

    public void cargar(){
        RecyclerView listaview = mainView.findViewById(R.id.rc_ventas_vendedor);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adaptadorVentas = new Adaptador_Lista_Ventas(vendedor_actual.obtenerVentas(), this);
        listaview.setAdapter(adaptadorVentas);
    }

    public void irVerListaVentas(){
        cargar();
        lista_ventas.setVisibility(View.VISIBLE);
        ver_vt_vn_lyt.setVisibility(View.GONE);
    }
    /*public void aniadirVenta() throws Exception {
        EditText precio= mainView.findViewById(R.id.precio_venta_txt);
        EditText clientes= mainView.findViewById(R.id.cliente_venta_txt);
        EditText vendedor= mainView.findViewById(R.id.vendedor_venta_txt);
        EditText auto = mainView.findViewById(R.id.vehiculo_venta_txt);
        EditText fechaventadia= mainView.findViewById(R.id.fecha_venta_dia_etxt);
        EditText fechaventames= mainView.findViewById(R.id.fecha_venta_mes_etxt);
        EditText fechaventaanio= mainView.findViewById(R.id.fecha_venta_anio_etxt);

        String fechaventa_str=fechaventaanio.getText().toString()+"-"+fechaventames.getText().toString()+"-"+fechaventadia.getText().toString();
        String clientes_str=clientes.getText().toString();
        String vendedores_str= vendedor.getText().toString();
        String autos_str = auto.getText().toString();
        float precioventa= Float.parseFloat(precio.getText().toString());
        Cliente clienteventa= patio.buscarClientes("Nombre",clientes_str);
        Vendedor vendedorventa= patio.buscarVendedores("Nombre",vendedores_str);
        Vehiculo autoventa= patio.buscarVehiculos("Matricula",autos_str);


        Venta nueva= new Venta(sdf.parse(fechaventa_str),clienteventa,vendedorventa,autoventa);
        patio.aniadirVenta(nueva);

        if(patio.getVentasGenerales().contiene(nueva)){
            Toast.makeText(mainView.getContext(),"Se registro la venta.",Toast.LENGTH_SHORT).show();
        }

    }*/

    public void visualizarVenta() {
        ImageView imagen = mainView.findViewById(R.id.vt_vn_vehiculo_img);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_vt_vn_txt);
        TextView precioV = mainView.findViewById(R.id.ver_pventa_vt_vn_txt);
        TextView precioI = mainView.findViewById(R.id.ver_pinicial_vt_vn_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_vt_vn_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_vt_vn_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_cliente_vt_vn_txt);

        TextView placa = mainView.findViewById(R.id.ver_placa_vt_vn_txt);
        TextView matricula = mainView.findViewById(R.id.ver_matricula_vt_vn_txt);
        TextView matriculado = mainView.findViewById(R.id.ver_matriculado_vt_vn_txt);
        TextView marca = mainView.findViewById(R.id.ver_marca_vt_vn_txt);
        TextView modelo = mainView.findViewById(R.id.ver_modelo_vt_vn_txt);
        TextView anio = mainView.findViewById(R.id.ver_anio_vt_vn_txt);

        StorageReference filePath = mStorageRef.child("Vehiculos/" + venta_mostrar.getVehiculo().getimagen());
        try {
            final File localFile = File.createTempFile(venta_mostrar.getVehiculo().getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                imagen.setImageURI(nuevo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        fecha.setText(Patioventainterfaz.getFechaMod(venta_mostrar.getFecha()));
        precioV.setText(String.format("$ %.2f", venta_mostrar.getPrecio()));
        precioI.setText(String.format("$ %.2f", venta_mostrar.getVehiculo().getPrecioInicial()));
        vendedor.setText(venta_mostrar.getVendedor().getNombre());
        cliente.setText(venta_mostrar.getCliente().getNombre());
        contacto.setText(venta_mostrar.getCliente().getTelefono());

        placa.setText(venta_mostrar.getVehiculo().getPlaca());
        matricula.setText(venta_mostrar.getVehiculo().getMatricula());
        if(venta_mostrar.getVehiculo().isMatriculado()){
            matriculado.setText("Si");
        }else{
            matriculado.setText("No");
        }
        marca.setText(venta_mostrar.getVehiculo().getMarca());
        modelo.setText(venta_mostrar.getVehiculo().getModelo());
        anio.setText(venta_mostrar.getVehiculo().getAnio());
    }

    public void verVenta(){
        lista_ventas.setVisibility(View.GONE);
        ver_vt_vn_lyt.setVisibility(View.VISIBLE);
        visualizarVenta();
    }

    public void verVentaEditable(){
        lista_ventas.setVisibility(View.GONE);
        ver_vt_vn_lyt.setVisibility(View.VISIBLE);
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
        adaptadorVentas.buscar(b);
        return false;
    }
}
