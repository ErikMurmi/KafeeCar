package com.prog.kafeecar;

import android.annotation.SuppressLint;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.patioventa;
import static com.prog.kafeecar.Patioventainterfaz.sdf;

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
    private LinearLayout editar_vt_vn_lyt;
    private LinearLayout lista_ventas;
    private FloatingActionButton irAniadirVenta;
    private PatioVenta patio;
    private final Vendedor vendedor_actual = (Vendedor) Patioventainterfaz.usuarioActual;
    private Venta venta_mostrar;
    private Adaptador_Lista_Ventas adaptadorVentas;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.ventas_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;
        busqueda_ventas = mainView.findViewById(R.id.busqueda_vt_vn_srv);
        //Botones
        irAniadirVenta = mainView.findViewById(R.id.aniadir_vt_vn_ftbn);
        //ver
        Button vt_vn_ver_venta_eliminar_btn = mainView.findViewById(R.id.vt_vn_ver_venta_eliminar_btn);
        Button vt_vn_ver_venta_editar_btn = mainView.findViewById(R.id.vt_vn_ver_venta_editar_btn);
        //editar
        Button ed_descartar_vt_vn_btn = mainView.findViewById(R.id.ed_descartar_vt_vn_btn);
        Button ed_guardar_vt_vn_btn = mainView.findViewById(R.id.ed_guardar_vt_vn_btn);
        //Add
        Button guardar_vt_vn_btn = mainView.findViewById(R.id.guardar_vt_vn_btn);
        Button descartar_vt_vn_btn = mainView.findViewById(R.id.descartar_vt_vn_btn);
        //Layouts
        lista_ventas = mainView.findViewById(R.id.lista_vt_vn_lyt);
        ver_vt_vn_lyt = mainView.findViewById(R.id.ver_vt_vn_lyt);
        aniadirVenta = mainView.findViewById(R.id.add_vt_vn_lyt);
        editar_vt_vn_lyt = mainView.findViewById(R.id.editar_vt_vn_lyt);

        irAniadirVenta.setOnClickListener(v -> {
            lista_ventas.setVisibility(View.GONE);
            ver_vt_vn_lyt.setVisibility(View.GONE);
            editar_vt_vn_lyt.setVisibility(View.GONE);
            cargar();
            adaptador();
            aniadirVenta.setVisibility(View.VISIBLE);
        });

        TextView ed_cedula_vendedor_vt_vn_etxt = mainView.findViewById(R.id.ed_cedula_vendedor_vt_vn_etxt);
        ed_cedula_vendedor_vt_vn_etxt.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());
        TextView ed_placa_vt_vn_actv = mainView.findViewById(R.id.ed_placa_vt_vn_actv);
        ed_placa_vt_vn_actv.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());

        TextView cedula_vendedor_vt_vn_etxt = mainView.findViewById(R.id.cedula_vendedor_vt_vn_etxt);
        cedula_vendedor_vt_vn_etxt.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());


        //add
        descartar_vt_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Estás seguro de descartar los cambios?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    irVerListaVentas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        guardar_vt_vn_btn.setOnClickListener(v -> {
            android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
            msg.setTitle("AÑADIR");
            msg.setMessage("¿Está seguro de añadir la venta?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(registarVenta()){
                        irVerListaVentas();
                    }
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "No se pudo añadir la venta", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        //ver
        vt_vn_ver_venta_eliminar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Eliminar Venta");
            msg.setMessage("¿Estás seguro de eliminar esta venta?");
            //Vehiculo vh = venta_mostrar.getVehiculo();
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    Toast.makeText(mainView.getContext(), "Error 201", Toast.LENGTH_SHORT).show();
                    if(patio.removerVenta(venta_mostrar.getVehiculo().getPlaca())){
                        //Toast.makeText(mainView.getContext(), "Error 202", Toast.LENGTH_SHORT).show();
                        Toast.makeText(mainView.getContext(), "Se elimino correctamente la venta", Toast.LENGTH_SHORT).show();
                        irVerListaVentas();
                    }
                    /*if(patio.getVentasGenerales().contiene(venta_mostrar)){
                        Toast.makeText(mainView.getContext(), "Error 202", Toast.LENGTH_SHORT).show();
                        String ms = "ESTA EN LA POSICION " + patio.removerVenta(venta_mostrar.getVehiculo().getPlaca());
                        Toast.makeText(mainView.getContext(), ms, Toast.LENGTH_SHORT).show();
                        irVerListaVentas();
                    }*/
                    //patio.removerVenta(vh.getPlaca());
                    Toast.makeText(mainView.getContext(), "Error 203", Toast.LENGTH_SHORT).show();
                    //todo
                    //revisar que el metodo que devuelve el carro despues de
                    //eliminar la venta este correcto
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainView.getContext(), "Error 200", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        vt_vn_ver_venta_editar_btn.setOnClickListener(v -> {
            lista_ventas.setVisibility(View.GONE);
            irAniadirVenta.setVisibility(View.GONE);
            aniadirVenta.setVisibility(View.GONE);
            ver_vt_vn_lyt.setVisibility(View.GONE);
            adaptadorEditar();
            cargar();
            editar_vt_vn_lyt.setVisibility(View.VISIBLE);
            try{
                visualizarVentaEditable();
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "Error 102", Toast.LENGTH_SHORT).show();
                irVerListaVentas();
            }

        });

        //editar
        ed_guardar_vt_vn_btn.setOnClickListener(v -> {
            android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
            msg.setTitle("GUARDAR");
            msg.setMessage("¿Está seguro de guardar los datos?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(editarVenta()){
                        verVenta();
                    }
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "Error 103", Toast.LENGTH_SHORT).show();
                    irVerListaVentas();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        ed_descartar_vt_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR CAMBIOS");
            msg.setMessage("¿Estás seguro de de salir sin guardar?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    verVenta();
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainView.getContext(), "Error 104", Toast.LENGTH_SHORT).show();
                    irVerListaVentas();
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

        //Menus desplegables para la funcion editar

        TextInputLayout ed_anio_vt_vn_til = mainView.findViewById(R.id.ed_anio_vt_vn_til);
        TextInputLayout ed_mes_vt_vn_til = mainView.findViewById(R.id.ed_mes_vt_vn_til);
        TextInputLayout ed_dia_vt_vn_til = mainView.findViewById(R.id.ed_dia_vt_vn_til);
        AutoCompleteTextView ed_anio_vt_vn_acv = mainView.findViewById(R.id.ed_anio_vt_vn_acv);
        AutoCompleteTextView ed_mes_vt_vn_acv = mainView.findViewById(R.id.ed_mes_vt_vn_acv);
        AutoCompleteTextView ed_dia_vt_vn_acv = mainView.findViewById(R.id.ed_dia_vt_vn_acv);

        ed_anio_vt_vn_til.setEndIconOnClickListener(v -> ed_anio_vt_vn_acv.performClick());
        ed_anio_vt_vn_acv.setOnClickListener(v -> {
            if(anios_mostradas){
                ed_anio_vt_vn_acv.dismissDropDown();
                anios_mostradas =false;
            }else{
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.anios);
                ed_anio_vt_vn_acv.setAdapter(adapt);
                ed_anio_vt_vn_acv.showDropDown();
                anios_mostradas = true;
            }
        });

        ed_anio_vt_vn_acv.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));

        ed_mes_vt_vn_til.setEndIconOnClickListener(v -> ed_mes_vt_vn_acv.performClick());
        ed_mes_vt_vn_acv.setOnClickListener(v -> {
            if(mes_mostrados){
                ed_mes_vt_vn_acv.dismissDropDown();
                mes_mostrados =false;
            }else{
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.meses);
                ed_mes_vt_vn_acv.setAdapter(adapt_mes);
                ed_mes_vt_vn_acv.showDropDown();
                mes_mostrados = true;
            }
        });

        ed_mes_vt_vn_acv.setOnItemClickListener((parent, view, position, id) -> setPosicion_mes(position));

        ed_dia_vt_vn_til.setEndIconOnClickListener(v -> ed_dia_vt_vn_acv.performClick());
        ed_dia_vt_vn_acv.setOnClickListener(v -> {
            if(posicion_mes!=-1 && posicion_anio!=-1){
                if(dias_mostrados){
                    ed_dia_vt_vn_acv.dismissDropDown();
                    dias_mostrados =false;
                }else{
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,diaListaDesplegable());
                    ed_dia_vt_vn_acv.setAdapter(adapt);
                    ed_dia_vt_vn_acv.showDropDown();
                    dias_mostrados = true;
                }
            }else{
                Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
            }
        });

        ed_dia_vt_vn_acv.setOnItemClickListener((parent, view, position, id) -> setPosicion_dia(position));

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

    public void adaptador(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_vn_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.cedula_vendedor_vt_vn_etxt);
        cedula_vendedor_ci_vn_txt.setText(vendedor_actual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_vn_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void adaptadorEditar(){
        AutoCompleteTextView ed_cedula_cliente_vt_vn_actv = mainView.findViewById(R.id.ed_cedula_cliente_vt_vn_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        ed_cedula_cliente_vt_vn_actv.setAdapter(adapter);

        TextView ed_cedula_vendedor_vt_vn_etxt = mainView.findViewById(R.id.ed_cedula_vendedor_vt_vn_etxt);
        ed_cedula_vendedor_vt_vn_etxt.setText(vendedor_actual.getCedula());

        TextView ed_placa_vt_vn_actv = mainView.findViewById(R.id.ed_placa_vt_vn_actv);
        ed_placa_vt_vn_actv.setText(venta_mostrar.getVehiculo().getPlaca());
    }

    public void irVerListaVentas(){
        cargar();
        editar_vt_vn_lyt.setVisibility(View.GONE);
        ver_vt_vn_lyt.setVisibility(View.GONE);
        aniadirVenta.setVisibility(View.GONE);

        irAniadirVenta.setVisibility(View.VISIBLE);
        lista_ventas.setVisibility(View.VISIBLE);
    }

    public boolean registarVenta() throws Exception {
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

        EditText precio_vt_ad_etxt = mainView.findViewById(R.id.precio_vt_vn_etxt);
        float precio = 0;
        if(!isEmpty(precio_vt_ad_etxt)){
            String precio_str = precio_vt_ad_etxt.getText().toString();
            precio = Float.parseFloat(precio_str);
        }else{
            Toast.makeText(mainView.getContext(), "Campo vacío: *Precio*", Toast.LENGTH_SHORT).show();
            c++;
        }


        if (c == 0) {
            String fecha_nueva_venta = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
            Date fecha = sdf.parse(fecha_nueva_venta);
            Venta nueva = new Venta(
                    fecha,
                    cliente_c,
                    vendedor_actual,
                    vehiculo,
                    precio);
            patio.aniadirVenta(nueva);
            if (patio.getVentasGenerales().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se agrego correctamente la venta", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
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
        anio.setText(String.valueOf(venta_mostrar.getVehiculo().getAnio()));
    }

    @SuppressLint("DefaultLocale")
    public void visualizarVentaEditable() {
        try {
            ImageView imagen = mainView.findViewById(R.id.ed_auto_vt_vn_img);
            AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_vt_vn_acv);
            AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_vt_vn_acv);
            AutoCompleteTextView dia = mainView.findViewById(R.id.ed_dia_vt_vn_acv);
            AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_vt_vn_actv);
            TextView vendedor = mainView.findViewById(R.id.ed_cedula_vendedor_vt_vn_etxt);
            TextView placa = mainView.findViewById(R.id.ed_placa_vt_vn_actv);
            EditText precioV = mainView.findViewById(R.id.ed_precio_vt_vn_etxt);

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

            String fechaVenta = Patioventainterfaz.getFechaMod(venta_mostrar.getFecha());
            String dia_s = fechaVenta.split("-")[0];
            String mes_s = fechaVenta.split("-")[1];
            String anio_s = fechaVenta.split("-")[2];

            for(int i =0;i<Patioventainterfaz.anios.length;i++){
                if(Patioventainterfaz.anios[i].equals(anio_s))
                    posicion_anio = i;
            }
            posicion_dia = Integer.parseInt(dia_s);
            posicion_mes = Integer.parseInt(mes_s)-1;

            anio.setText(anio_s);
            mes.setText(Patioventainterfaz.meses[posicion_mes]);
            dia.setText(dia_s);

            cliente.setText(venta_mostrar.getCliente().getCedula());
            vendedor.setText(vendedor_actual.getCedula());
            placa.setText(venta_mostrar.getVehiculo().getPlaca());
            precioV.setText(String.format("%.2f", venta_mostrar.getPrecio()));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mainView.getContext(), "Error 101", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean editarVenta() throws Exception {
        Cliente cliente_c = null;
        int c = 0;

        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_vt_vn_actv);

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

        EditText precio = mainView.findViewById(R.id.ed_precio_vt_vn_etxt);

        float precio_flt = 0;
            if(!isEmpty(precio)){
                String precio_s = precio.getText().toString();
                precio_flt = Float.parseFloat(precio_s);
            }else{
                Toast.makeText(mainView.getContext(), "Campo vacío: *Precio*", Toast.LENGTH_SHORT).show();
                c++;
            }


        if (c == 0) {
            fecha_nueva_cita = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
            Date fecha = null;
            try {
                fecha = sdf.parse(fecha_nueva_cita);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(mainView.getContext(), "Error 106", Toast.LENGTH_SHORT).show();
            }
            venta_mostrar.actualizar(
                    fecha,
                    precio_flt,
                    venta_mostrar.getVehiculo(),
                    cliente_c,
                    vendedor_actual);
            if (patio.buscarVentas(venta_mostrar.getVehiculo().getPlaca(), cliente_c.getCedula()) != null) {
                Toast.makeText(mainView.getContext(), "Se editó correctamente la venta", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void verVenta(){
        cargar();
        lista_ventas.setVisibility(View.GONE);
        irAniadirVenta.setVisibility(View.GONE);
        aniadirVenta.setVisibility(View.GONE);
        editar_vt_vn_lyt.setVisibility(View.GONE);
        ver_vt_vn_lyt.setVisibility(View.VISIBLE);
        visualizarVenta();
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
//todo arreglar busqueda
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
