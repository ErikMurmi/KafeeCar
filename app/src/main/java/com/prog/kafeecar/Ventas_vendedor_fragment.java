package com.prog.kafeecar;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Ventas_vendedor_fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick, SearchView.OnQueryTextListener {
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private int posicion_dia=-1;
    private int posicion_mes=-1;
    private int posicion_anio=-1;

    private boolean mes_mostrados = false;
    private boolean anios_mostradas = false;
    public boolean dias_mostrados = false;

    String fecha_nueva_cita;

    private View mainView;
    private SearchView busqueda_ventas;
    private LinearLayout ver_vt_vn_lyt;
    private LinearLayout aniadirVenta;
    private LinearLayout editarventa;
    private LinearLayout lista_ventas;
    private FloatingActionButton irAniadirVenta;
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
        irAniadirVenta = mainView.findViewById(R.id.aniadir_vt_vn_ftbn);
        eliminar = mainView.findViewById(R.id.vt_vn_ver_venta_eliminar_btn);

        //eliminar = mainView.findViewById(R.id.eliminar_vt_vn_btn);

        //actualizar = mainview.findViewById(R.id.actualizar_btn);
        //guardar = mainview.findViewById(R.id.guardar_clita_nueva_btn);

        //Layouts
        lista_ventas = mainView.findViewById(R.id.lista_vt_vn_lyt);
        ver_vt_vn_lyt = mainView.findViewById(R.id.ver_vt_vn_lyt);
        aniadirVenta = mainView.findViewById(R.id.add_vt_vn_lyt);
        //aniadirventa = mainview.findViewById(R.id.aniadirventa_layout);
        //editarventa = mainview.findViewById(R.id.editarventa_layout);

        irAniadirVenta.setOnClickListener(v -> {
            lista_ventas.setVisibility(View.GONE);
            ver_vt_vn_lyt.setVisibility(View.GONE);
            aniadirVenta.setVisibility(View.VISIBLE);
        });

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

        //Menus desplegables

        TextInputLayout anio_lyt = mainView.findViewById(R.id.anio_vt_vn_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_vt_vn_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.dia_vt_vn_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_vt_vn_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_vt_vn_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_vt_vn_acv);

        anio_lyt.setEndIconOnClickListener(v -> anio.performClick());
        anio.setOnClickListener(v -> {
            if(anios_mostradas){
                anio.dismissDropDown();
                anios_mostradas =false;
            }else{
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.anios);
                anio.setAdapter(adapt);
                anio.showDropDown();
                anios_mostradas = true;
            }
        });

        anio.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));

        mes_lyt.setEndIconOnClickListener(v -> mes.performClick());
        mes.setOnClickListener(v -> {
            if(mes_mostrados){
                mes.dismissDropDown();
                mes_mostrados =false;
            }else{
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.meses);
                mes.setAdapter(adapt_mes);
                mes.showDropDown();
                mes_mostrados = true;
            }
        });

        mes.setOnItemClickListener((parent, view, position, id) -> setPosicion_mes(position));

        dias_lyt.setEndIconOnClickListener(v -> dias.performClick());

        dias.setOnClickListener(v -> {
            if(posicion_mes!=-1 && posicion_anio!=-1){
                if(dias_mostrados){
                    dias.dismissDropDown();
                    dias_mostrados =false;
                }else{
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,diaListaDesplegable());
                    dias.setAdapter(adapt);
                    dias.showDropDown();
                    dias_mostrados = true;
                }
            }else{
                Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
            }
        });

        dias.setOnItemClickListener((parent, view, position, id) -> setPosicion_dia(position));

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

    public boolean registarCita() throws Exception {
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int c = 0;

        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_vn_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_vn_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                c++;
            }
            cliente_c = patio.buscarClientes("Cedula", cliente_str);
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Cédula Cliente*", Toast.LENGTH_SHORT).show();
            c++;
        }

        if (!isEmpty(auto)) {
            String vehiculo_str = auto.getText().toString();
            vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);
            if (vehiculo == null) {
                Toast.makeText(mainView.getContext(), "No existe el vehículo", Toast.LENGTH_SHORT).show();
                auto.setText("");
                c++;
            }
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Placa Vehiculo*", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText precio = mainView.findViewById(R.id.resolucion_ci_vn_etxt);
        float precio_flt = Float.parseFloat(precio.getText().toString());

        if (c == 0) {
            fecha_nueva_cita = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
            Date fecha = sdf.parse(fecha_nueva_cita);
            Venta nueva = new Venta(fecha,cliente_c,vendedor_actual,vehiculo,precio_flt);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se agrego correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

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

    public ArrayList<String> diaListaDesplegable(){
        ArrayList<String> dias = new ArrayList<>();
        int anioa = Integer.parseInt(Patioventainterfaz.anios[posicion_anio]);
        int i;
        for (i = 1; i<=Patioventainterfaz.diasLista[posicion_mes];i++){
            dias.add(String.valueOf(i));
        }
        if(Patioventainterfaz.esBisiesto(anioa) && posicion_mes==1){
            dias.add(String.valueOf(i+1));
        }
        return dias;
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    public final void setPosicion_mes(int pos){
        posicion_mes= pos;
    }
    public final void setPosicion_anio(int pos){
        posicion_anio= pos;
    }
    private void setPosicion_dia(int pos) {posicion_dia = pos; }
}
