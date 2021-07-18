package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.net.Uri;
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

import static com.prog.kafeecar.Patioventainterfaz.sdf;

public class Citas_vendedor_fragment extends Fragment implements Adaptador_Lista_Citas.RecyclerItemClick, SearchView.OnQueryTextListener {

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private final Vendedor usuarioActual = (Vendedor) Patioventainterfaz.usuarioActual;
    public AutoCompleteTextView placa_ci_vn_etxt;
    Cita cita_mostrar;
    String fecha_nueva_cita;
    private View mainView;
    private PatioVenta patio;
    private boolean horas_mostradas = false;
    private boolean dias_mostrados = false;
    private boolean meses_mostrados = false;
    private boolean anios_mostrados = false;
    public boolean add_cliente_ed = false;
    public boolean add_cliente_an = false;
    private int posicion_hora = -1;
    private int posicion_mes = -1;
    private int posicion_anio = -1;
    private int posicion_dia = -1;
    private int hora_nueva_cita = -1;
    private SearchView busqueda_citas;
    private LinearLayout aniadir_ci_vn_lyt;
    private LinearLayout citas_vendedor_lyt;
    private LinearLayout ver_ci_vn_lyt;
    private LinearLayout editar_ci_vn_lyt;
    private LinearLayout aniadir_cliente_ci_vn_lyt;
    private Adaptador_Lista_Citas adptadorlistaview;
    private FloatingActionButton ir_aniadir_ci_vn_btn;

    @SuppressLint("CutPasteId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;

        busqueda_citas = mainView.findViewById(R.id.busqueda_c_ve_srv);
        placa_ci_vn_etxt = mainView.findViewById(R.id.placa_ci_vn_actv);
        ver_ci_vn_lyt = mainView.findViewById(R.id.ver_ci_vn_lyt);
        citas_vendedor_lyt = mainView.findViewById(R.id.citas_vendedor_lyt);
        aniadir_ci_vn_lyt = mainView.findViewById(R.id.aniadir_ci_vn_lyt);
        editar_ci_vn_lyt = mainView.findViewById(R.id.editar_ci_vn_lyt);
        aniadir_cliente_ci_vn_lyt = mainView.findViewById(R.id.aniadir_cliente_ci_vn_lyt);

        ir_aniadir_ci_vn_btn = mainView.findViewById(R.id.ir_aniadir_ci_vn_btn);
        Button guardar_ci_vn_btn = mainView.findViewById(R.id.guardar_ci_vn_btn);
        Button descartar_ci_vn_btn = mainView.findViewById(R.id.descartar_ci_vn_btn);
        Button anular_ci_vn_btn = mainView.findViewById(R.id.anular_ci_vn_btn);
        Button editar_ci_vn_btn = mainView.findViewById(R.id.editar_ci_vn_btn);
        Button ed_guardar_ci_vn_btn = mainView.findViewById(R.id.ed_guardar_ci_vn_btn);
        Button ed_descartar_ci_vn_btn = mainView.findViewById(R.id.ed_descartar_ci_vn_btn);
        Button reg_list_ci_vn_btn = mainView.findViewById(R.id.reg_list_ci_vn_btn);
        ImageButton add_cliente_ci_vn_btn = mainView.findViewById(R.id.add_cliente_ci_vn_btn);
        ImageButton ed_add_cliente_ci_vn_btn = mainView.findViewById(R.id.ed_add_cliente_ci_vn_btn);

        TextView cedula_vendedor_ci_vn_etxt = mainView.findViewById(R.id.cedula_vendedor_ci_vn_etxt);
        TextView ed_cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.ed_cedula_vendedor_ci_vn_etxt);

        ir_aniadir_ci_vn_btn.setOnClickListener(v -> {
            ir_aniadir_ci_vn_btn.setVisibility(View.GONE);
            citas_vendedor_lyt.setVisibility(View.GONE);
            editar_ci_vn_lyt.setVisibility(View.GONE);
            ver_ci_vn_lyt.setVisibility(View.GONE);
            clearRegistrarCI();
            aniadir_ci_vn_lyt.setVisibility(View.VISIBLE);
            adaptadorAniadir();
            listasDesplegableAniadir();
        });

        guardar_ci_vn_btn.setOnClickListener(v -> {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_vn_actv);
            String cli = cliente.getText().toString();
            try {
                Cliente aux = patio.buscarClientes("Cedula",cli);
                if(aux!=null){
                    android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("CLIENTE NO REGISTRADO");
                    msg.setMessage("Presione 'Si' para añadir al cliente" );
                    msg.setPositiveButton("Si", (dialog, which) -> add_cliente_ci_vn_btn.callOnClick());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }else{
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("AÑADIR");
                    msg.setMessage("¿Está seguro de añadir la cita?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            if (registarCita()) {
                                irListaCitas();
                            }
                        } catch (Exception e) {
                            citas_vendedor_lyt.setVisibility(View.VISIBLE);
                            Toast.makeText(mainView.getContext(), "Error 126: No se pudo añadir la cita", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 141: busqueda fallida del cliente", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        descartar_ci_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de descartar la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    irListaCitas();
                } catch (Exception e) {
                    citas_vendedor_lyt.setVisibility(View.VISIBLE);
                    Toast.makeText(mainView.getContext(), "Error 127: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        anular_ci_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("ANULAR");
            msg.setMessage("¿Está seguro de anular la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    patio.removerCita(cita_mostrar.getVehiculo().getPlaca(), cita_mostrar.getCliente().getCedula());
                    irListaCitas();
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "Error 128: No se pudo eliminar la cita", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        editar_ci_vn_btn.setOnClickListener(v -> {
            ir_aniadir_ci_vn_btn.setVisibility(View.GONE);
            citas_vendedor_lyt.setVisibility(View.GONE);
            ver_ci_vn_lyt.setVisibility(View.GONE);
            aniadir_ci_vn_lyt.setVisibility(View.GONE);
            editar_ci_vn_lyt.setVisibility(View.VISIBLE);
            visualizarEditable();
        });

        ed_cedula_vendedor_ci_vn_txt.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());
        cedula_vendedor_ci_vn_etxt.setOnClickListener(v -> Toast.makeText(mainView.getContext(), "No se puede editar este campo", Toast.LENGTH_SHORT).show());

        ed_guardar_ci_vn_btn.setOnClickListener(v -> {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_actv);
            String cli = cliente.getText().toString();
            try {
                Cliente aux = patio.buscarClientes("Cedula",cli);
                AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                if(aux==null){
                    msg.setTitle("CLIENTE NO REGISTRADO");
                    msg.setMessage("Presione 'Si' para añadir al cliente" );
                    msg.setPositiveButton("Si", (dialog, which) -> add_cliente_ci_vn_btn.callOnClick());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                }else{
                    msg.setTitle("GUARDAR");
                    msg.setMessage("¿Está seguro de guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            if (editarCita()) {
                                irListaCitas();
                            }
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 144: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                }
                msg.show();
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 145: No se pudo registrar el cliente", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        ed_descartar_ci_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de descartar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> irVer());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        if (Patioventainterfaz.CITA_CON_VEHICULO) {
            ir_aniadir_ci_vn_btn.callOnClick();
            placa_ci_vn_etxt.setText(Patioventainterfaz.v_aux_cita.getPlaca());
        } else {
            try {
                cargar();
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 130: No se pudo cargar", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        //cliente
        reg_list_ci_vn_btn.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder msg = new androidx.appcompat.app.AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Registrar Cliente");
            msg.setMessage("¿Estás seguro de registrar un cliente con estos datos?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                if(registrarCliente()){
                    ir_aniadir_ci_vn_btn.callOnClick();
                }else{
                    Toast.makeText(mainView.getContext(), "Error 131: No se pudo registrar el cliente", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        add_cliente_ci_vn_btn.setOnClickListener(v -> {
            aniadir_ci_vn_lyt.setVisibility(View.GONE);
            add_cliente_an = true;
            clearRegistrarCli();
            aniadir_cliente_ci_vn_lyt.setVisibility(View.VISIBLE);
        });

        ed_add_cliente_ci_vn_btn.setOnClickListener(v -> {
            editar_ci_vn_lyt.setVisibility(View.GONE);
            add_cliente_ed = true;
            aniadir_cliente_ci_vn_lyt.setVisibility(View.VISIBLE);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if ((editar_ci_vn_lyt.getVisibility() == View.VISIBLE) || (aniadir_ci_vn_lyt.getVisibility() == View.VISIBLE)) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            irListaCitas();
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 132: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if (ver_ci_vn_lyt.getVisibility() == View.VISIBLE) {
                    irListaCitas();
                }
                if(aniadir_cliente_ci_vn_lyt.getVisibility() == View.VISIBLE && add_cliente_an){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            aniadir_ci_vn_lyt.setVisibility(View.VISIBLE);
                            aniadir_cliente_ci_vn_lyt.setVisibility(View.GONE);
                            add_cliente_an = false;
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 142: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }else if(aniadir_cliente_ci_vn_lyt.getVisibility() == View.VISIBLE  && add_cliente_ed){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            editar_ci_vn_lyt.setVisibility(View.VISIBLE);
                            aniadir_cliente_ci_vn_lyt.setVisibility(View.GONE);
                            add_cliente_ed = false;
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 143: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        busqueda_citas.setOnQueryTextListener(this);
        try {
            cargar();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error 133: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
        }
        return mainView;
    }

    public void adaptadorAniadir() {
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_vn_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.cedula_vendedor_ci_vn_etxt);
        cedula_vendedor_ci_vn_txt.setText(usuarioActual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_vn_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void adaptadorEditar() {
        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_ci_ad_txt = mainView.findViewById(R.id.ed_cedula_vendedor_ci_vn_etxt);
        cedula_vendedor_ci_ad_txt.setText(usuarioActual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_vn_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void listasDesplegableAniadir() {

        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_vn_til);
        AutoCompleteTextView horas = mainView.findViewById(R.id.hora_ci_vn_acv);
        TextInputLayout anios_lyt = mainView.findViewById(R.id.anio_ci_vn_til);
        AutoCompleteTextView anios = mainView.findViewById(R.id.anio_ci_vn_acv);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_ci_vn_til);
        AutoCompleteTextView meses = mainView.findViewById(R.id.mes_ci_vn_acv);
        TextInputLayout d = mainView.findViewById(R.id.dia_ci_vn_til);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_ci_vn_acv);

        horas_lyt.setEndIconOnClickListener(v -> horas.performClick());

        horas.setOnClickListener(v -> {
            if (horas_mostradas) {
                horas.dismissDropDown();
                horas_mostradas = false;
            } else {
                try {
                    fecha_nueva_cita = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
                    Date fecha = sdf.parse(fecha_nueva_cita);
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, horasDisponible(fecha));
                    horas.setAdapter(adapt);
                    horas.showDropDown();
                    horas.setOnItemClickListener((parent, view, position, id) -> setHora_nueva_cita(Integer.parseInt(adapt.getItem(position))));
                    horas_mostradas = true;
                } catch (ParseException e) {
                    Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        anios_lyt.setEndIconOnClickListener(v -> anios.performClick());
        anios.setOnClickListener(v -> {
            if (anios_mostrados) {
                anios.dismissDropDown();
                anios_mostrados = false;
            } else {
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.anios);
                anios.setAdapter(adapt);
                anios.showDropDown();
                anios_mostrados = true;
            }
        });

        anios.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));

        mes_lyt.setEndIconOnClickListener(v -> meses.performClick());
        meses.setOnClickListener(v -> {
            if (meses_mostrados) {
                meses.dismissDropDown();
                meses_mostrados = false;
            } else {
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.meses);
                meses.setAdapter(adapt_mes);
                meses.showDropDown();
                meses_mostrados = true;
            }
        });

        d.setEndIconOnClickListener(v -> dias.performClick());
        dias.setOnClickListener(v -> {
            if (posicion_mes == -1 || posicion_anio == -1) {
                Toast.makeText(mainView.getContext(), "Campo de año o mes no seleccionados", Toast.LENGTH_SHORT).show();
            } else {
                if (dias_mostrados) {
                    dias.dismissDropDown();
                    dias_mostrados = false;
                } else {
                    try {
                        ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, diaListaDesplegable());
                        dias.setAdapter(adapt);
                        dias.showDropDown();
                        dias_mostrados = true;
                    } catch (Exception e) {
                        Toast.makeText(mainView.getContext(), "Error 134: No se puede mostrar (Adapter)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dias.setOnItemClickListener((parent, view, position, id) -> setPosicion_dia(position));
    }

    public void listasDesplegableEditar() {
        TextInputLayout anio_lyt = mainView.findViewById(R.id.ed_anio_ci_vn_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.ed_mes_ci_vn_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.ed_dia_ci_vn_til);
        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_vn_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_ci_vn_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_ci_vn_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_ci_vn_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.ed_hora_ci_vn_acv);
        anio_lyt.setEndIconOnClickListener(v -> anio.performClick());
        anio.setOnClickListener(v -> {
            if (anios_mostrados) {
                anio.dismissDropDown();
                anios_mostrados = false;
            } else {
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.anios);
                anio.setAdapter(adapt);
                anio.showDropDown();
                anios_mostrados = true;
            }
        });

        anio.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));
        anio.setListSelection(1);

        mes_lyt.setEndIconOnClickListener(v -> mes.performClick());
        mes.setOnClickListener(v -> {
            if (meses_mostrados) {
                mes.dismissDropDown();
                meses_mostrados = false;
            } else {
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.meses);
                mes.setAdapter(adapt_mes);
                mes.showDropDown();
                meses_mostrados = true;
            }
        });

        mes.setOnItemClickListener((parent, view, position, id) -> setPosicion_mes(position));

        dias_lyt.setEndIconOnClickListener(v -> dias.performClick());
        dias.setOnClickListener(v -> {
            if (posicion_mes != -1 && posicion_anio != -1) {
                if (dias_mostrados) {
                    dias.dismissDropDown();
                    dias_mostrados = false;
                } else {
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, diaListaDesplegable());
                    dias.setAdapter(adapt);
                    dias.showDropDown();
                    dias_mostrados = true;
                }
            } else {
                Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
            }
        });

        dias.setOnItemClickListener((parent, view, position, id) -> setPosicion_dia(position));
        horas_lyt.setEndIconOnClickListener(v -> horas.performClick());
        horas.setOnClickListener(v -> {
            if (horas_mostradas) {
                horas.dismissDropDown();
                horas_mostradas = false;
            } else {
                try {
                    fecha_nueva_cita = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
                    Date fecha = sdf.parse(fecha_nueva_cita);
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, horasDisponible(fecha));
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

    public void irListaCitas() {
        try {
            cargar();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error 135: No se pudo ejecutar la acción", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        ver_ci_vn_lyt.setVisibility(View.GONE);
        aniadir_ci_vn_lyt.setVisibility(View.GONE);
        editar_ci_vn_lyt.setVisibility(View.GONE);
        ir_aniadir_ci_vn_btn.setVisibility(View.VISIBLE);
        citas_vendedor_lyt.setVisibility(View.VISIBLE);
    }

    public void cargar() throws Exception {
        RecyclerView listaview = mainView.findViewById(R.id.rc_citas_vendedor);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Citas(usuarioActual.obtenerCitas().copiar(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    public void clearRegistrarCli(){
        EditText add_nombre_vt_vn_etxt = mainView.findViewById(R.id.add_nombre_ci_vn_etxt);
        EditText reg_apellido_vt_vn_etxt = mainView.findViewById(R.id.reg_apellido_ci_vn_etxt);
        EditText reg_cedula_vt_vn_etxt = mainView.findViewById(R.id.reg_cedula_ci_vn_etxt);
        EditText reg_dia_vt_vn_etxt = mainView.findViewById(R.id.reg_dia_ci_vn_etxt);
        EditText reg_mes_vt_vn_etxt = mainView.findViewById(R.id.reg_mes_ci_vn_etxt);
        EditText reg_anio_vt_vn_etxt = mainView.findViewById(R.id.reg_anio_ci_vn_etxt);
        EditText reg_telefono_vt_vn_etxt = mainView.findViewById(R.id.reg_telefono_ci_vn_etxt);
        EditText reg_correo_vt_vn_etxt = mainView.findViewById(R.id.reg_correo_ci_vn_etxt);

        add_nombre_vt_vn_etxt.getText().clear();
        reg_apellido_vt_vn_etxt.getText().clear();
        reg_cedula_vt_vn_etxt.getText().clear();
        reg_dia_vt_vn_etxt.getText().clear();
        reg_mes_vt_vn_etxt.getText().clear();
        reg_anio_vt_vn_etxt.getText().clear();
        reg_telefono_vt_vn_etxt.getText().clear();
        reg_correo_vt_vn_etxt.getText().clear();
    }

    public boolean registrarCliente(){
        EditText add_nombre_vt_vn_etxt = mainView.findViewById(R.id.add_nombre_ci_vn_etxt);
        EditText reg_apellido_vt_vn_etxt = mainView.findViewById(R.id.reg_apellido_ci_vn_etxt);
        EditText reg_cedula_vt_vn_etxt = mainView.findViewById(R.id.reg_cedula_ci_vn_etxt);
        EditText reg_dia_vt_vn_etxt = mainView.findViewById(R.id.reg_dia_ci_vn_etxt);
        EditText reg_mes_vt_vn_etxt = mainView.findViewById(R.id.reg_mes_ci_vn_etxt);
        EditText reg_anio_vt_vn_etxt = mainView.findViewById(R.id.reg_anio_ci_vn_etxt);
        EditText reg_telefono_vt_vn_etxt = mainView.findViewById(R.id.reg_telefono_ci_vn_etxt);
        EditText reg_correo_vt_vn_etxt = mainView.findViewById(R.id.reg_correo_ci_vn_etxt);
        int vacios = 0;
        int invalidos = 0;

        String nombre_str = add_nombre_vt_vn_etxt.getText().toString();
        if (nombre_str.isEmpty()) {
            vacios++;
        }

        String apellido_str = reg_apellido_vt_vn_etxt.getText().toString();
        if (apellido_str.isEmpty()) {
            vacios++;
        }

        String cedula_str = reg_cedula_vt_vn_etxt.getText().toString();
        if (cedula_str.isEmpty()) {
            vacios++;
        } else {
            if (cedula_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                reg_cedula_vt_vn_etxt.setText("");
                invalidos++;
            }
        }

        String mes_str = reg_mes_vt_vn_etxt.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            vacios++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                reg_mes_vt_vn_etxt.setText("");
                invalidos++;
            }
        }

        String anio_str = reg_anio_vt_vn_etxt.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            vacios++;
        } else {
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                reg_anio_vt_vn_etxt.setText("");
                invalidos++;
            }
        }

        String dia_str = reg_dia_vt_vn_etxt.getText().toString();
        int dia;
        if (dia_str.isEmpty()) {
            vacios++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainView.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                reg_dia_vt_vn_etxt.setText("");
                invalidos++;
            }
        }

        String telefono_str = reg_telefono_vt_vn_etxt.getText().toString();
        if (telefono_str.isEmpty()) {
            vacios++;
        } else {
            if (telefono_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Numero de telefono inválido", Toast.LENGTH_SHORT).show();
                reg_telefono_vt_vn_etxt.setText("");
                invalidos++;
            }
        }

        String correo_str = reg_correo_vt_vn_etxt.getText().toString();
        if (correo_str.isEmpty()) {
            vacios++;
        } else {
            if (!Patioventainterfaz.validarMail(correo_str)) {
                Toast.makeText(mainView.getContext(), "Correo no válido", Toast.LENGTH_SHORT).show();
                reg_correo_vt_vn_etxt.setText("");
                invalidos++;
            }
        }

        if (vacios == 0 && invalidos == 0) {
            try {
                Date fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
                Cliente cliente = new Cliente(nombre_str, cedula_str, telefono_str, correo_str,fecha);
                if (patio.aniadirUsuario(cliente, "Cliente")) {
                    if(add_cliente_an){
                        AutoCompleteTextView cliente_ed = mainView.findViewById(R.id.cedula_cliente_ci_vn_actv);
                        cliente_ed.setText(cedula_str);
                    }else if(add_cliente_ed){
                        AutoCompleteTextView cliente_an = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_actv);
                        cliente_an.setText(cedula_str);
                    }
                    Toast.makeText(mainView.getContext(), "Se registro el cliente correctamente", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 136: No se registro el cliente", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mainView.getContext(), "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void visualizarEditable() {
        adaptadorEditar();
        listasDesplegableEditar();
        ImageView imagen_ed = mainView.findViewById(R.id.ed_carro_ci_vn_img);
        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_vn_actv);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_ci_vn_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_ci_vn_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_ci_vn_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.ed_hora_ci_vn_acv);
        TextView vendedor_ed = mainView.findViewById(R.id.ed_cedula_vendedor_ci_vn_etxt);
        EditText resolucion_ed = mainView.findViewById(R.id.ed_resolucion_ci_vn_etxt);

        String fecha = Patioventainterfaz.getFechaMod(cita_mostrar.getFechaCita());
        String dia_str = fecha.split("-")[0];
        String mes_str = fecha.split("-")[1];
        String anio_str = fecha.split("-")[2];

        for (int i = 0; i < Patioventainterfaz.anios.length; i++) {
            if (Patioventainterfaz.anios[i].equals(anio_str))
                posicion_anio = i;
        }
        posicion_dia = Integer.parseInt(dia_str);
        posicion_mes = Integer.parseInt(mes_str) - 1;
        posicion_hora = cita_mostrar.getHora();

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
            Toast.makeText(mainView.getContext(), "Error 137: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        horas.setText(String.valueOf(cita_mostrar.getHora()));
        cliente.setText(cita_mostrar.getCliente().getCedula());
        vendedor_ed.setText(cita_mostrar.getVendedorCita().getCedula());
        auto.setText(cita_mostrar.getVehiculo().getPlaca());
        resolucion_ed.setText(cita_mostrar.getResolucion());
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void visualizarCita() {
        ImageView imagen = mainView.findViewById(R.id.foto_auto_ci_vn_img);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_ci_vn_txt);
        TextView hora = mainView.findViewById(R.id.ver_hora_ci_vn_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_ci_vn_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_ci_vn_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_ci_vn_txt);
        TextView vehiculo = mainView.findViewById(R.id.ver_vehiculo_ci_vn_txt);
        TextView descripcion = mainView.findViewById(R.id.ver_descripcion_ci_vn_txt);
        TextView resolucion = mainView.findViewById(R.id.ver_resolucion_ci_vn_txt);
        TextView precio = mainView.findViewById(R.id.ver_precioVenta_ci_vn_txt);

        StorageReference filePath = mStorageRef.child("Vehiculos/" + cita_mostrar.getVehiculo().getimagen());
        try {
            final File localFile = File.createTempFile(cita_mostrar.getVehiculo().getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                imagen.setImageURI(nuevo);
            });
        } catch (IOException e) {
            Toast.makeText(mainView.getContext(), "Error 137: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        fecha.setText(Patioventainterfaz.getFechaMod(cita_mostrar.getFechaCita()));
        hora.setText(String.format("%d:00 %s", cita_mostrar.getHora(), Patioventainterfaz.formatoHora(cita_mostrar.getHora())));
        cliente.setText(cita_mostrar.getCliente().getNombre());
        contacto.setText(cita_mostrar.getCliente().getTelefono());
        vendedor.setText(cita_mostrar.getVendedorCita().getNombre());
        vehiculo.setText(cita_mostrar.getVehiculo().getModelo());
        descripcion.setText(cita_mostrar.getVehiculo().getDescripcion());
        resolucion.setText(cita_mostrar.getResolucion());
        String resolucion_str = cita_mostrar.getResolucion();
        if (resolucion_str.isEmpty()) {
            resolucion.setHint("");
        } else {
            resolucion.setText(resolucion_str);
        }
        precio.setText("$ " + cita_mostrar.getVehiculo().getPrecioVenta());
    }

    public boolean editarCita() throws Exception {
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int vacios = 0;
        int invalidos = 0;

        String prueba = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);

        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_vn_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                invalidos++;
            }
            cliente_c = patio.buscarClientes("Cedula", cliente_str);
        } else {
            vacios++;
        }

        if (!isEmpty(auto)) {
            String vehiculo_str = auto.getText().toString();
            vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);
            if (vehiculo == null) {
                Toast.makeText(mainView.getContext(), "No existe el vehículo", Toast.LENGTH_SHORT).show();
                auto.setText("");
                invalidos++;
            }
        } else {
            vacios++;
        }

        EditText resolucion = mainView.findViewById(R.id.ed_resolucion_ci_vn_etxt);
        String resolucion_str = resolucion.getText().toString();

        if (vacios == 0 && invalidos ==0) {
            cita_mostrar.actualizarVen(
                    fecha,
                    posicion_hora,
                    vehiculo,
                    usuarioActual,
                    cliente_c,
                    resolucion_str);
            if (patio.buscarCita("Vehiculo", vehiculo.getPlaca(), cliente_c.getCedula()) != null) {
                Toast.makeText(mainView.getContext(), "Se edito correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }else{
            Toast.makeText(mainView.getContext(), "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void clearRegistrarCI(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_vn_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_vn_actv);
        EditText resolucion = mainView.findViewById(R.id.resolucion_ci_vn_etxt);
        AutoCompleteTextView horas = mainView.findViewById(R.id.hora_ci_vn_acv);
        AutoCompleteTextView anios = mainView.findViewById(R.id.anio_ci_vn_acv);
        AutoCompleteTextView meses = mainView.findViewById(R.id.mes_ci_vn_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_ci_vn_acv);
        posicion_dia = -1;
        posicion_mes = -1;
        posicion_anio = -1;

        cliente.getText().clear();
        auto.getText().clear();
        resolucion.getText().clear();
        dias.getText().clear();
        meses.getText().clear();
        anios.getText().clear();
        horas.getText().clear();
    }

    public boolean registarCita() throws Exception {
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int vacios = 0;
        int invalidos = 0;

        String prueba = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);

        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_vn_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_vn_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                invalidos++;
            }
            cliente_c = patio.buscarClientes("Cedula", cliente_str);
        } else {
            vacios++;
        }

        if (!isEmpty(auto)) {
            String vehiculo_str = auto.getText().toString();
            vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);
            if (vehiculo == null) {
                Toast.makeText(mainView.getContext(), "No existe el vehículo", Toast.LENGTH_SHORT).show();
                auto.setText("");
                invalidos++;
            }
        } else {
            vacios++;
        }

        EditText resolucion = mainView.findViewById(R.id.resolucion_ci_vn_etxt);
        String resolucion_str = resolucion.getText().toString();
        if (isEmpty(resolucion)) {
            resolucion.setHint("");
        }

        if (vacios == 0 && invalidos == 0) {
            Cita nueva = new Cita(
                    fecha,
                    posicion_hora,
                    resolucion_str,
                    cliente_c,
                    usuarioActual,
                    vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se agrego correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }else{
            Toast.makeText(mainView.getContext(), "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public ArrayList<String> horasDisponible(Date fechaCita) {
        ArrayList<String> horas = new ArrayList<>();
        for (int i = usuarioActual.getHoraEntrada(); i < usuarioActual.getHoraComida(); i++) {
            try {
                if (usuarioActual.disponible(fechaCita, i)) {
                    horas.add(String.valueOf(i));
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 138: busqueda fallida del usuario actual", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        for (int i = usuarioActual.getHoraComida() + 1; i < usuarioActual.getHoraSalida(); i++) {
            try {
                if (usuarioActual.disponible(fechaCita, i)) {
                    horas.add(String.valueOf(i));
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 139: busqueda fallida del usuario actual", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return horas;
    }

    public ArrayList<String> diaListaDesplegable() {
        ArrayList<String> dias = new ArrayList<>();
        int anioa = Integer.parseInt(Patioventainterfaz.anios[posicion_anio]);
        int i;
        for (i = 1; i <= Patioventainterfaz.diasLista[posicion_mes]; i++) {
            dias.add(String.valueOf(i));
        }
        if (Patioventainterfaz.esBisiesto(anioa)) {
            dias.add(String.valueOf(i + 1));
        }
        return dias;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void irVer() {
        citas_vendedor_lyt.setVisibility(View.GONE);
        ir_aniadir_ci_vn_btn.setVisibility(View.GONE);
        aniadir_ci_vn_lyt.setVisibility(View.GONE);
        ver_ci_vn_lyt.setVisibility(View.VISIBLE);
        visualizarCita();
    }

    @Override
    public void itemClick(String placa, String cedula_cliente) {
        try {
            cita_mostrar = patio.buscarCita("Vehiculo", placa, cedula_cliente);
            irVer();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error 140: busqueda fallida de la cita", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onQueryTextChange(String newText) {
        String b = newText;
        if (newText.length() == 2) {
            b += "-";
            busqueda_citas.setQuery(b, false);
        }
        if (newText.length() == 5) {
            b += "-";
            busqueda_citas.setQuery(b, false);
        }
        if (newText.length() > 10) {
            b = b.substring(0, 10);
            busqueda_citas.setQuery(b, false);
        }
        adptadorlistaview.buscar(b);
        return false;
    }

    public final void setPosicion_mes(int pos) {
        posicion_mes = pos;
    }

    public final void setPosicion_anio(int pos) {
        posicion_anio = pos;
    }

    private void setPosicion_dia(int pos) {
        posicion_dia = pos;
    }

    private void setHora_nueva_cita(int hora) {
        hora_nueva_cita = hora;
    }

}
