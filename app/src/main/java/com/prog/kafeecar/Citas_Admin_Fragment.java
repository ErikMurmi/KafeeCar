package com.prog.kafeecar;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.getFechaMod;
import static com.prog.kafeecar.Patioventainterfaz.sdf;

public class Citas_Admin_Fragment extends Fragment implements Adaptador_Lista_Citas.RecyclerItemClick, SearchView.OnQueryTextListener {

    private String TAG = "Citas_Admin";
    private PatioVenta patio;
    private Vendedor usuarioActual = (Vendedor) Patioventainterfaz.usuarioActual;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private Adaptador_Lista_Citas adptadorlistaview;
    //Auxiliar para pasar del vehiculo a registrar cita
    //TODO
    private static Vehiculo aux;
    private View mainView;
    private SearchView busqueda_citas;
    private Cita cita_mostrar;
    private boolean horas_mostradas = false;

    //Image Buttons
    private ImageButton buscar_btn;

    private EditText vehiculo_nuevacita;
    private EditText dia_b;
    private EditText mes_b;
    private EditText anio_b;
    //Botones
    private Button irVerEditable;
    private Button anular;
    private Button descartar;
    private Button guardar_edit;
    private FloatingActionButton irAniadirCita;

    private LinearLayout verCita;
    private LinearLayout citaEditable;
    private LinearLayout listaCitas;
    private LinearLayout aniadirCita;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        //Layouts
        busqueda_citas = mainView.findViewById(R.id.busqueda_c_ad_srv);
        verCita = mainView.findViewById(R.id.ver_ci_ad_lyt);
        aniadirCita = mainView.findViewById(R.id.add_cita_admin_lyt);
        citaEditable = mainView.findViewById(R.id.editar_cita_ad_lyt);
        listaCitas = mainView.findViewById(R.id.citas_lyt);

        //Botones
        irAniadirCita = mainView.findViewById(R.id.ir_aniadir_cita_ad_fbtn);
        irVerEditable = mainView.findViewById(R.id.editar_ci_ad_btn);
        anular = mainView.findViewById(R.id.anular_ci_ad_btn);
        descartar = mainView.findViewById(R.id.ed_descartar_ci_ad_btn);
        guardar_edit = mainView.findViewById(R.id.ed_editar_ci_vn_btn);
        //TextViews
        //vehiculo_nuevacita = mainView.findViewById(R.id.vehiculo_txt);
        //Edit Text
        dia_b = mainView.findViewById(R.id.fechacitadia_txt);
        mes_b = mainView.findViewById(R.id.fechacitames_txt);
        anio_b = mainView.findViewById(R.id.fechacitaanio_txt);
        //Image Buttons
        //OnClick

        irAniadirCita.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.VISIBLE);
        });
        TextInputLayout n = mainView.findViewById(R.id.txt_lyt);
        AutoCompleteTextView horas = mainView.findViewById(R.id.horas_ddm);

        n.setEndIconOnClickListener(v -> horas.performClick());

        horas.setOnClickListener(v -> {
            if(horas_mostradas){
                horas.dismissDropDown();
                horas_mostradas =false;
            }else{
                try {
                    Date fecha = sdf.parse(dia_b.getText().toString()+"-"+mes_b.getText().toString()+"-"+anio_b.getText().toString());
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,horasDisponible(fecha));
                    horas.setAdapter(adapt);
                    horas.showDropDown();
                    horas_mostradas = true;
                } catch (ParseException e) {
                    Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });


        anular.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("ANULAR");
            msg.setMessage("¿Está seguro de anular la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    patio.removerCita(cita_mostrar.getVehiculo().getPlaca(), cita_mostrar.getCliente().getCedula());
                    irListaCitas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });


        descartar.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de salir sin guardar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    irListaCitas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        guardar_edit.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Guardar");
            msg.setMessage("¿Está seguro de guardar los cambios en la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(editarCita()){
                        irListaCitas();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        irVerEditable.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.GONE);
            citaEditable.setVisibility(View.VISIBLE);
            visualizarEditable();
        });


        if(Patioventainterfaz.CITA_CON_VEHICULO){
            irAniadirCita.callOnClick();
            //vehiculo_nuevacita.setText(Patioventainterfaz.v_aux_cita.getPlaca());
            //vehiculo_nuevacita.setTextColor(Color.BLACK);
        }
        /*
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                /*
                verCita = mainView.findViewById(R.id.ver_cita_lyt);
                aniadirCita = mainView.findViewById(R.id.add_cita_admin_lyt);
                listaCitas = mainView.findViewById(R.id.citas_admin_lyt);

                if(editar_vehiculo.getVisibility()== View.VISIBLE){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Aceptar", (dialog, which) -> {
                        irV1();
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if(verVehiculo.getVisibility()== View.VISIBLE){
                    irCatalogo();
                }
                //Intent myIntent = new Intent(nombreClase.this,activityDestiny.class);
            }
        };*/
        busqueda_citas.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.rc_citas_admin);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Citas(patio.getCitas(), this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void irListaCitas(){
        cargar();
        listaCitas.setVisibility(View.VISIBLE);
        irAniadirCita.setVisibility(View.VISIBLE);
        verCita.setVisibility(View.GONE);
        aniadirCita.setVisibility(View.GONE);
        citaEditable.setVisibility(View.GONE);
    }

    public void visualizarCita() {
        listaCitas.setVisibility(View.GONE);
        irAniadirCita.setVisibility(View.GONE);
        verCita.setVisibility(View.VISIBLE);
        ImageView imagen_ed = mainView.findViewById(R.id.ver_cita_im_ad_img);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_ci_ad_txt);
        TextView hora = mainView.findViewById(R.id.ver_hora_ci_ad_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_ci_ad_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_ci_ad_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_ci_ad_txt);
        TextView vehiculo = mainView.findViewById(R.id.ver_vehiculo_ci_ad_txt);
        TextView precio = mainView.findViewById(R.id.ver_precioVenta_ci_ad_txt);
        TextView descripcion = mainView.findViewById(R.id.ver_descripcion_ci_ad_txt);
        TextView resolucion = mainView.findViewById(R.id.ver_resolucion_ci_ad_txt);
        StorageReference filePath = mStorageRef.child("Vehiculos/" + cita_mostrar.getVehiculo().getimagen());
        try {
            final File localFile = File.createTempFile(cita_mostrar.getVehiculo().getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                imagen_ed.setImageURI(nuevo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        fecha.setText(getFechaMod(cita_mostrar.getFechaCita()));
        hora.setText(String.valueOf(cita_mostrar.getHora()));
        cliente.setText(cita_mostrar.getCliente().getNombre());
        contacto.setText(cita_mostrar.getCliente().getTelefono());
        vendedor.setText(cita_mostrar.getVendedorCita().getNombre());
        vehiculo.setText(cita_mostrar.getVehiculo().getMarca() + cita_mostrar.getVehiculo().getModelo());
        String resolucion_str =cita_mostrar.getResolucion();
        descripcion.setText(cita_mostrar.getVehiculo().getDescripcion());
        if(!resolucion_str.equals(" ")){
            resolucion.setVisibility(View.VISIBLE);
            resolucion.setText(resolucion_str);
        }else{
            resolucion.setVisibility(View.GONE);
        }

        precio.setText(" $" + cita_mostrar.getVehiculo().getPrecioVenta());
    }

    public void visualizarEditable(){
        ImageView imagen_ed = mainView.findViewById(R.id.ed_carro_ci_ad_img);
        TextView dia_ed = mainView.findViewById(R.id.ed_dia_ci_ad_etxt);
        TextView mes_ed = mainView.findViewById(R.id.ed_mes_ci_ad_etxt);
        TextView anio_ed = mainView.findViewById(R.id.ed_anio_ci_ad_etxt);
        TextView hora_ed = mainView.findViewById(R.id.ed_hora_ci_ad_etxt);
        TextView cliente_ed = mainView.findViewById(R.id.ed_cliente_ci_ad_etxt);
        TextView vendedor_ed = mainView.findViewById(R.id.ed_vendedor_ci_ad_etxt);
        TextView vehiculo_ed = mainView.findViewById(R.id.ed_placa_ci_ad_etxt);
        TextView resolucion_ed = mainView.findViewById(R.id.ed_resolucion_ci_ad_etxt);

        String fecha = Patioventainterfaz.getFechaMod(cita_mostrar.getFechaCita());
        String dia = fecha.split("-")[0];
        String mes = fecha.split("-")[1];
        String anio = fecha.split("-")[2];

        StorageReference filePath = mStorageRef.child("Vehiculos/" + cita_mostrar.getVehiculo().getimagen());
        try {
            final File localFile = File.createTempFile(cita_mostrar.getVehiculo().getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                imagen_ed.setImageURI(nuevo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        dia_ed.setText(dia);
        mes_ed.setText(mes);
        anio_ed.setText(anio);
        hora_ed.setText(String.valueOf(cita_mostrar.getHora()));
        cliente_ed.setText(cita_mostrar.getCliente().getCedula());
        vendedor_ed.setText(cita_mostrar.getVendedorCita().getCedula());
        vehiculo_ed.setText(cita_mostrar.getVehiculo().getPlaca());
        resolucion_ed.setText(cita_mostrar.getResolucion());
    }

    public void registarCita() throws Exception {
        EditText fechacitadia = mainView.findViewById(R.id.fechacitadia_txt);
        EditText fechacitames = mainView.findViewById(R.id.fechacitames_txt);
        EditText fechacitaanio = mainView.findViewById(R.id.fechacitaanio_txt);
        EditText fechacitahora = mainView.findViewById(R.id.fechacitahoracita_txt);
        Vendedor vendedor_v = null;
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        String fechacita_str = "";
        int c = 0;
        int hora = -1;
        if ((!isEmpty(fechacitadia) && !isEmpty(fechacitames)) && (!isEmpty(fechacitaanio) && !isEmpty(fechacitahora))) {
            fechacita_str = fechacitadia.getText().toString() + "-" + fechacitames.getText().toString() + "-" + fechacitaanio.getText().toString();
            hora = Integer.parseInt(fechacitahora.getText().toString());
        } else {
            c++;
            Toast.makeText(mainView.getContext(), "Campos de fecha vacíos", Toast.LENGTH_SHORT).show();
        }
        EditText cliente = mainView.findViewById(R.id.cliente_txt);
        EditText vendedor = mainView.findViewById(R.id.vendedor_txt);
        EditText auto = mainView.findViewById(R.id.vehiculo_txt);
        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            cliente_c = patio.buscarClientes("Cedula", cliente_str);
        } else {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de cliente vacio", Toast.LENGTH_SHORT).show();
        }

        if (!isEmpty(vendedor)) {
            String vendedor_str = vendedor.getText().toString();
            vendedor_v = patio.buscarVendedores("Cedula", vendedor_str);
        } else {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de vendedor vacio", Toast.LENGTH_SHORT).show();
        }

        if (!isEmpty(auto)) {
            String vehiculo_str = auto.getText().toString();
            vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);
        } else {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de vehiculo vacio", Toast.LENGTH_SHORT).show();
        }

        EditText resolucion = mainView.findViewById(R.id.resolucion_txt);
        String resolucion_str = resolucion.getText().toString();

        if (c == 0) {
            Cita nueva = new Cita(Patioventainterfaz.sdf.parse(fechacita_str), hora, resolucion_str, cliente_c, vendedor_v, vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se agrego correctamente.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean editarCita() throws Exception {
        EditText fechacitadia = mainView.findViewById(R.id.ed_dia_ci_ad_etxt);
        EditText fechacitames = mainView.findViewById(R.id.ed_mes_ci_ad_etxt);
        EditText fechacitaanio = mainView.findViewById(R.id.ed_anio_ci_ad_etxt);
        EditText fechacitahora = mainView.findViewById(R.id.ed_hora_ci_ad_etxt);
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int c = 0;
        int hora = -1;
        int dia = -1;
        int mes = -1;
        int anio = -1;

        if (!isEmpty(fechacitaanio)) {
            String anio_str = fechacitaanio.getText().toString();
            anio = Integer.parseInt(anio_str);
            if (anio < 2021) {
                Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                fechacitaanio.setText("");
                c++;
            }
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Año*", Toast.LENGTH_SHORT).show();
            c++;
        }

        if (!isEmpty(fechacitames)) {
            String mes_str = fechacitames.getText().toString();
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                fechacitames.setText("");
                c++;
            }
        } else {
            Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
            fechacitames.setText("");
            c++;
        }

        if (!isEmpty(fechacitadia)) {
            String dia_str = fechacitadia.getText().toString();
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainView.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                fechacitadia.setText("");
                c++;
            }
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Día*", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText cliente = mainView.findViewById(R.id.ed_cliente_ci_ad_etxt);
        EditText auto = mainView.findViewById(R.id.ed_placa_ci_ad_etxt);

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
            if (vehiculo_str.length() != 8) {
                Toast.makeText(mainView.getContext(), "Número de placa inválido", Toast.LENGTH_SHORT).show();
                auto.setText("");
                c++;
            }
            vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Placa Vehiculo*", Toast.LENGTH_SHORT).show();
            c++;
        }

        if (!isEmpty(fechacitahora)) {
            String hora_str = fechacitahora.getText().toString();
            hora = Integer.parseInt(hora_str);
            if(hora>24 || hora <1){
                Toast.makeText(mainView.getContext(), "Hora invalida", Toast.LENGTH_SHORT).show();
                c++;
            }
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora*", Toast.LENGTH_SHORT).show();
            c++;
        }

        Date fecha = Patioventainterfaz.sdf.parse(dia + "-" + mes + "-" + anio);
        if((hora>24 || hora <1) || !usuarioActual.disponible(fecha, hora)){
            Toast.makeText(mainView.getContext(), "El vendedor no está disponible", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText resolucion = mainView.findViewById(R.id.ed_resolucion_ci_ad_etxt);
        String resolucion_str = resolucion.getText().toString();

        if (c == 0) {
            Cita actualizada = cita_mostrar.actualizarVen(
                    fecha,
                    hora,
                    vehiculo,
                    usuarioActual,
                    cliente_c,
                    resolucion_str);
            if (patio.getCitas().contiene(actualizada)) {
                Toast.makeText(mainView.getContext(), "Se edito la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public String formatoHora(int hora) {
        if (hora > 12) {
            return "pm";
        }
        return "am";
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa, String cedula_cliente) {
        try {
            cita_mostrar = patio.buscarCitas("Vehiculo",placa,cedula_cliente);
            visualizarCita();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Errores", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String b = newText;
        if(newText.length()==2){
            b+="-";
            busqueda_citas.setQuery(b,false);
        }
        if(newText.length()==5){
            b+="-";
            busqueda_citas.setQuery(b,false);
        }
        if(newText.length()>10){
            b= b.substring(0,10);
            busqueda_citas.setQuery(b,false);
        }
        adptadorlistaview.buscar(b);
        return false;
    }

    public ArrayList<String> horasDisponible(Date fechaCita){
        ArrayList<String> horas = new ArrayList<>();
        for(int i = usuarioActual.getHoraEntrada();i<usuarioActual.getHoraComida();i++){
            try {
                if(usuarioActual.disponible(fechaCita,i)){
                    horas.add(String.valueOf(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i = usuarioActual.getHoraComida()+1;i<usuarioActual.getHoraSalida();i++){
            try {
                if(usuarioActual.disponible(fechaCita,i)){
                    horas.add(String.valueOf(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  horas;
    }
}
