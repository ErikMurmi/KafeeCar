package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.activity.OnBackPressedCallback;
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

    private PatioVenta patio;
    private final Vendedor usuarioActual = (Vendedor) Patioventainterfaz.usuarioActual;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private Adaptador_Lista_Citas adptadorlistaview;
    private View mainView;
    private SearchView busqueda_citas;
    private Cita cita_mostrar;
    private boolean horas_mostradas = false;
    private boolean mes_mostrados = false;
    private boolean anios_mostradas = false;
    public boolean dias_mostrados = false;

    private boolean add_cliente_ci_an = false;
    private boolean add_cliente_ci_ed = false;

    private int posicion_dia=-1;
    private int posicion_mes=-1;
    private int posicion_anio=-1;
    private int hora_nueva_cita=-1;
    String fecha_nueva_cita;

    private FloatingActionButton ir_aniadir_cita_ad_fbtn;

    private TextView cedula_vendedor_ci_ad_txt;
    private TextView ed_cedula_vendedor_ci_ad_txt;
    private AutoCompleteTextView placa_ci_ad_actv;

    private LinearLayout verCita;
    private LinearLayout ed_cita_admin_lyt;
    private LinearLayout listaCitas;
    private LinearLayout add_cita_admin_lyt;
    private LinearLayout aniadir_cliente_ci_ad_lyt;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        mainView = inflater.inflate(R.layout.citas_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        //Layouts
        busqueda_citas = mainView.findViewById(R.id.busqueda_c_ad_srv);
        verCita = mainView.findViewById(R.id.ver_ci_ad_lyt);
        add_cita_admin_lyt = mainView.findViewById(R.id.add_cita_admin_lyt);
        ed_cita_admin_lyt = mainView.findViewById(R.id.ed_cita_admin_lyt);
        listaCitas = mainView.findViewById(R.id.citas_lyt);
        aniadir_cliente_ci_ad_lyt = mainView.findViewById(R.id.aniadir_cliente_ci_ad_lyt);

        //Botones
        ir_aniadir_cita_ad_fbtn = mainView.findViewById(R.id.ir_aniadir_cita_ad_fbtn);
        //Botones
        Button irVerEditable = mainView.findViewById(R.id.editar_ci_ad_btn);
        Button anular = mainView.findViewById(R.id.anular_ci_ad_btn);
        Button ed_descartar_ci_ad_btn = mainView.findViewById(R.id.ed_descartar_ci_ad_btn);
        Button ed_guardar_ci_ad_btn = mainView.findViewById(R.id.ed_guardar_ci_ad_btn);
        Button guardar_cita = mainView.findViewById(R.id.guardar_ci_ad_btn);
        Button descartar_ci_ad_btn = mainView.findViewById(R.id.descartar_ci_ad_btn);
        ImageButton add_cliente_ci_ad_btn = mainView.findViewById(R.id.add_cliente_ci_ad_btn);
        ImageButton ed_add_cliente_ci_ad_btn = mainView.findViewById(R.id.ed_add_cliente_ci_ad_btn);
        Button reg_list_ci_ad_btn = mainView.findViewById(R.id.reg_list_ci_ad_btn);

        //Textview permanentes
        cedula_vendedor_ci_ad_txt =mainView.findViewById(R.id.cedula_vendedor_ci_ad_txt);
        ed_cedula_vendedor_ci_ad_txt = mainView.findViewById(R.id.ed_cedula_vendedor_ci_ad_txt);
        cedula_vendedor_ci_ad_txt.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());
        ed_cedula_vendedor_ci_ad_txt.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());

        ir_aniadir_cita_ad_fbtn.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            ir_aniadir_cita_ad_fbtn.setVisibility(View.GONE);
            add_cita_admin_lyt.setVisibility(View.VISIBLE);
            adaptadorAniadir();
        });

        descartar_ci_ad_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de salir sin guardar?");
            msg.setPositiveButton("Si", (dialog, which) -> irListaCitas());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        guardar_cita.setOnClickListener(v -> {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_ad_actv);
            String cli = cliente.getText().toString();
            try {
                Cliente aux = patio.buscarClientes("Cedula",cli);
                AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                if(aux==null){
                    msg.setTitle("CLIENTE NO REGISTRADO");
                    msg.setMessage("Presione 'Si' para añadir al cliente" );
                    msg.setPositiveButton("Si", (dialog, which) -> add_cliente_ci_ad_btn.callOnClick());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                }else{
                    msg.setTitle("AÑADIR");
                    msg.setMessage("¿Está seguro de añadir la venta?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            if(registarCita()){
                                irListaCitas();
                            }
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 21: No se pudo añadir la cita", Toast.LENGTH_SHORT).show();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                }
                msg.show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainView.getContext(), "Error 22: Búsqueda fallida del cliente", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mainView.getContext(), "Error 23: No se pudo remover la cita", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });


        ed_descartar_ci_ad_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de salir sin guardar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                    irListaCitas();
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        ed_guardar_ci_ad_btn.setOnClickListener(v -> {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_ad_actv);
            String cli = cliente.getText().toString();
            try {
                Cliente aux = patio.buscarClientes("Cedula",cli);
                AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                if(aux==null){
                    msg.setTitle("CLIENTE NO REGISTRADO");
                    msg.setMessage("Presione 'Si' para añadir al cliente" );
                    msg.setPositiveButton("Si", (dialog, which) -> ed_add_cliente_ci_ad_btn.callOnClick());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                }else{
                    msg.setTitle("GUARDAR");
                    msg.setMessage("¿Está seguro de guardar los cambios en la cita?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            if(editarCita()){
                                irListaCitas();
                            }
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 157: No se pudo editar la venta", Toast.LENGTH_SHORT).show();
                            irListaCitas();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                }
                msg.show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainView.getContext(), "Error 163: busqueda fallida del cliente", Toast.LENGTH_SHORT).show();
            }
        });

        irVerEditable.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            add_cita_admin_lyt.setVisibility(View.GONE);
            ed_cita_admin_lyt.setVisibility(View.VISIBLE);
            visualizarEditable();
        });

        //Add cliente
        add_cliente_ci_ad_btn.setOnClickListener(v -> {
            add_cita_admin_lyt.setVisibility(View.GONE);
            add_cliente_ci_an = true;
            aniadir_cliente_ci_ad_lyt.setVisibility(View.VISIBLE);
        });

        ed_add_cliente_ci_ad_btn.setOnClickListener(v -> {
            ed_cita_admin_lyt.setVisibility(View.GONE);
            add_cliente_ci_ed = true;
            aniadir_cliente_ci_ad_lyt.setVisibility(View.VISIBLE);
        });

        reg_list_ci_ad_btn.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder msg = new androidx.appcompat.app.AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Registrar Cliente");
            msg.setMessage("¿Estás seguro de registrar un cliente con estos datos?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                if(registrarCliente()){
                    ir_aniadir_cita_ad_fbtn.callOnClick();
                }else{
                    Toast.makeText(mainView.getContext(), "Error 154: No se pudo registrar el cliente", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        listasDesplegableAniadir();

        if(Patioventainterfaz.CITA_CON_VEHICULO){
            ir_aniadir_cita_ad_fbtn.callOnClick();
            placa_ci_ad_actv.setText(Patioventainterfaz.v_aux_cita.getPlaca());
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if ((ed_cita_admin_lyt.getVisibility() == View.VISIBLE)||(add_cita_admin_lyt.getVisibility() == View.VISIBLE)) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            onCreate(savedInstanceState);
                            irListaCitas();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if(verCita.getVisibility()==View.VISIBLE){
                    irListaCitas();
                }
                if(aniadir_cliente_ci_ad_lyt.getVisibility()==View.VISIBLE && add_cliente_ci_an){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        aniadir_cliente_ci_ad_lyt.setVisibility(View.GONE);
                        add_cita_admin_lyt.setVisibility(View.VISIBLE);
                        add_cliente_ci_an =false;
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();

                }
                if (aniadir_cliente_ci_ad_lyt.getVisibility()==View.VISIBLE && add_cliente_ci_ed) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        aniadir_cliente_ci_ad_lyt.setVisibility(View.GONE);
                        ed_cita_admin_lyt.setVisibility(View.VISIBLE);
                        add_cliente_ci_an =false;
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        busqueda_citas.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }

    public boolean registrarCliente(){
        EditText add_nombre_ci_ad_etxt = mainView.findViewById(R.id.add_nombre_ci_ad_etxt);
        EditText reg_apellido_ci_ad_etxt = mainView.findViewById(R.id.reg_apellido_ci_ad_etxt);
        EditText reg_cedula_ci_ad_etxt = mainView.findViewById(R.id.reg_cedula_ci_ad_etxt);
        EditText reg_dia_ci_ad_etxt = mainView.findViewById(R.id.reg_dia_ci_ad_etxt);
        EditText reg_mes_ci_ad_etxt = mainView.findViewById(R.id.reg_mes_ci_ad_etxt);
        EditText reg_anio_ci_ad_etxt = mainView.findViewById(R.id.reg_anio_ci_ad_etxt);
        EditText reg_telefono_ci_ad_etxt = mainView.findViewById(R.id.reg_telefono_ci_ad_etxt);
        EditText reg_correo_ci_ad_etxt = mainView.findViewById(R.id.reg_correo_ci_ad_etxt);
        int vacios = 0;
        int invalidos = 0;

        String nombre_str = add_nombre_ci_ad_etxt.getText().toString();
        if (nombre_str.isEmpty()) {
            vacios++;
        }

        String apellido_str = reg_apellido_ci_ad_etxt.getText().toString();
        if (apellido_str.isEmpty()) {
            vacios++;
        }

        String cedula_str = reg_cedula_ci_ad_etxt.getText().toString();
        if (cedula_str.isEmpty()) {
            vacios++;
        } else {
            if (cedula_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                reg_cedula_ci_ad_etxt.setText("");
                invalidos++;
            }
        }

        String mes_str = reg_mes_ci_ad_etxt.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            vacios++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                reg_mes_ci_ad_etxt.setText("");
                invalidos++;
            }
        }

        String anio_str = reg_anio_ci_ad_etxt.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            vacios++;
        } else {
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                reg_anio_ci_ad_etxt.setText("");
                invalidos++;
            }
        }

        String dia_str = reg_dia_ci_ad_etxt.getText().toString();
        int dia;
        if (dia_str.isEmpty()) {
            vacios++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainView.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                reg_dia_ci_ad_etxt.setText("");
                invalidos++;
            }
        }

        String telefono_str = reg_telefono_ci_ad_etxt.getText().toString();
        if (telefono_str.isEmpty()) {
            vacios++;
        } else {
            if (telefono_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Numero de telefono inválido", Toast.LENGTH_SHORT).show();
                reg_telefono_ci_ad_etxt.setText("");
                invalidos++;
            }
        }

        String correo_str = reg_correo_ci_ad_etxt.getText().toString();
        if (correo_str.isEmpty()) {
            vacios++;
        } else {
            if (!Patioventainterfaz.validarMail(correo_str)) {
                Toast.makeText(mainView.getContext(), "Correo no válido", Toast.LENGTH_SHORT).show();
                reg_correo_ci_ad_etxt.setText("");
                invalidos++;
            }
        }

        if (vacios == 0 && invalidos == 0) {
            try {
                Date fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
                Cliente cliente = new Cliente(nombre_str, cedula_str, telefono_str, correo_str,fecha);
                if (patio.aniadirUsuario(cliente, "Cliente")) {
                    if(add_cliente_ci_an){
                        AutoCompleteTextView cliente_ed = mainView.findViewById(R.id.cedula_cliente_ci_ad_actv);
                        cliente_ed.setText(cedula_str);
                    }else if(add_cliente_ci_ed) {
                        AutoCompleteTextView cliente_an = mainView.findViewById(R.id.ed_cedula_cliente_ci_ad_actv);
                        cliente_an.setText(cedula_str);
                    }
                    Toast.makeText(mainView.getContext(), "Se registro el cliente correctamente", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 158: No se registro el cliente", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mainView.getContext(), "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void listasDesplegableAniadir(){
        TextInputLayout anio_lyt = mainView.findViewById(R.id.anio_ci_ad_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_ci_ad_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.dia_ci_ad_til);
        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_ad_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_ci_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_ci_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_ci_ad_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.hora_ci_ad_acv);
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

        horas_lyt.setEndIconOnClickListener(v -> horas.performClick());

        horas.setOnClickListener(v -> {
            if(horas_mostradas){
                horas.dismissDropDown();
                horas_mostradas =false;
            }else{
                try {
                    fecha_nueva_cita = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
                    Date fecha = sdf.parse(fecha_nueva_cita);
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,horasDisponible(fecha));
                    horas.setAdapter(adapt);
                    horas.showDropDown();
                    horas.setOnItemClickListener((parent, view, position, id) -> setHora_nueva_cita(Integer.parseInt(adapt.getItem(position))));
                    horas_mostradas = true;
                } catch (ParseException e) {
                    Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void listasDesplegableEditar(){
        TextInputLayout anio_lyt = mainView.findViewById(R.id.ed_anio_ci_ad_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.ed_mes_ci_ad_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.ed_dia_ci_ad_til);
        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_ad_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_ci_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_ci_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_ci_ad_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.ed_hora_ci_ad_acv);
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
        anio.setListSelection(1);

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

        horas_lyt.setEndIconOnClickListener(v -> horas.performClick());

        horas.setOnClickListener(v -> {
            if(horas_mostradas){
                horas.dismissDropDown();
                horas_mostradas =false;
            }else{
                try {
                    fecha_nueva_cita = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
                    Date fecha = sdf.parse(fecha_nueva_cita);
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,horasDisponible(fecha));
                    horas.setAdapter(adapt);
                    horas.showDropDown();
                    horas.setOnItemClickListener((parent, view, position, id) -> setHora_nueva_cita(Integer.parseInt(adapt.getItem(position))));
                    horas_mostradas = true;
                } catch (ParseException e) {
                    Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void adaptadorAniadir(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_ad_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        cedula_vendedor_ci_ad_txt.setText(usuarioActual.getCedula());

        placa_ci_ad_actv = mainView.findViewById(R.id.placa_ci_ad_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        placa_ci_ad_actv.setAdapter(adapterPla);
    }

    public void adaptadorEditar(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_ad_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        ed_cedula_vendedor_ci_ad_txt.setText(usuarioActual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_ad_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.rc_citas_admin);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Citas(patio.getCitas().copiar(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    public void irListaCitas(){
        cargar();
        listaCitas.setVisibility(View.VISIBLE);
        ir_aniadir_cita_ad_fbtn.setVisibility(View.VISIBLE);
        verCita.setVisibility(View.GONE);
        add_cita_admin_lyt.setVisibility(View.GONE);
        ed_cita_admin_lyt.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void visualizarCita() {
        listaCitas.setVisibility(View.GONE);
        ir_aniadir_cita_ad_fbtn.setVisibility(View.GONE);
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
        vehiculo.setText(cita_mostrar.getVehiculo().getMarca() +" "+ cita_mostrar.getVehiculo().getModelo());
        String resolucion_str =cita_mostrar.getResolucion();
        descripcion.setText(cita_mostrar.getVehiculo().getDescripcion());
        if(!resolucion_str.equals("")){
            resolucion.setVisibility(View.VISIBLE);
            resolucion.setText(resolucion_str);
        }else{
            resolucion.setVisibility(View.GONE);
        }

        precio.setText(" $" + cita_mostrar.getVehiculo().getPrecioVenta());
    }

    public void visualizarEditable(){

        adaptadorEditar();
        listasDesplegableEditar();
        ImageView imagen_ed = mainView.findViewById(R.id.ed_carro_ci_ad_img);
        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_ad_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_ad_actv);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_ci_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_ci_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_ci_ad_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.ed_hora_ci_ad_acv);
        EditText resolucion_ed = mainView.findViewById(R.id.ed_resolucion_ci_ad_etxt);

        String fecha = Patioventainterfaz.getFechaMod(cita_mostrar.getFechaCita());
        String dia_str = fecha.split("-")[0];
        String mes_str = fecha.split("-")[1];
        String anio_str = fecha.split("-")[2];

        for(int i =0;i<Patioventainterfaz.anios.length;i++){
            if(Patioventainterfaz.anios[i].equals(anio_str))
                posicion_anio = i;
        }
        posicion_dia = Integer.parseInt(dia_str);
        posicion_mes = Integer.parseInt(mes_str);

        dias.setText(dia_str);
        mes.setText(Patioventainterfaz.meses[posicion_mes]);
        anio.setText(anio_str);

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

        horas.setText(String.valueOf(cita_mostrar.getHora()));
        cliente.setText(cita_mostrar.getCliente().getCedula());
        auto.setText(cita_mostrar.getVehiculo().getPlaca());
        resolucion_ed.setText(cita_mostrar.getResolucion());
    }

    public boolean registarCita() throws Exception {
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int c = 0;

        String prueba = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);

        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_ad_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_ad_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                c++;
            }else{
                cliente_c = patio.buscarClientes("Cedula", cliente_str);
                if(cliente_c==null){
                    Toast.makeText(mainView.getContext(), "Cliente no encontrados", Toast.LENGTH_SHORT).show();
                    cliente.setText("");
                    c++;
                }
            }
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

        EditText resolucion = mainView.findViewById(R.id.ed_resolucion_ci_ad_etxt);
        String resolucion_str = resolucion.getText().toString();

        if (c == 0) {
            Cita nueva = new Cita(
                    fecha,
                    hora_nueva_cita,
                    resolucion_str,
                    cliente_c,
                    usuarioActual,
                    vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se edito correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public boolean editarCita() throws Exception {
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int c = 0;

        String prueba = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);

        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_ad_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_ad_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                c++;
            }else{
                cliente_c = patio.buscarClientes("Cedula", cliente_str);
                if(cliente_c==null){
                    Toast.makeText(mainView.getContext(), "Cliente no encontrados", Toast.LENGTH_SHORT).show();
                    cliente.setText("");
                    c++;
                }
            }
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

        EditText resolucion = mainView.findViewById(R.id.resolucion_ci_ad_etx);
        String resolucion_str = resolucion.getText().toString();

        if (c == 0) {
            cita_mostrar.actualizarVen(
                    fecha,
                    hora_nueva_cita,
                    vehiculo,
                    usuarioActual,
                    cliente_c,
                    resolucion_str);
            if (patio.buscarCita("Vehiculo",vehiculo.getPlaca(),cliente_c.getCedula())!=null) {
                Toast.makeText(mainView.getContext(), "Se edito la cita correctamente", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa, String cedula_cliente) {
        try {
            cita_mostrar = patio.buscarCita("Vehiculo",placa,cedula_cliente);
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

    public final void setPosicion_mes(int pos){
        posicion_mes= pos;
    }
    public final void setPosicion_anio(int pos){
        posicion_anio= pos;
    }
    private void setPosicion_dia(int pos) {posicion_dia = pos; }
    private void setHora_nueva_cita(int hora) {
        hora_nueva_cita = hora;
    }
}
