package com.prog.kafeecar;

import android.graphics.Color;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.sdf;

public class Ventas_admin_Fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick, SearchView.OnQueryTextListener {
    private final Vendedor usuario_admin = (Vendedor) Patioventainterfaz.usuarioActual;
    public LinearLayout editar_vt_ad_lyt;
    public boolean dias_mostrados = false;
    @Nullable

    BarChart barChart;
    BarChart Usuarios;
    private View mainView;
    private PatioVenta patio;
    private LinearLayout ventas_admin_generales_lyt;
    private LinearLayout add_vt_ad_lyt;
    private LinearLayout ventas_admin_lyt;
    private SearchView busqueda_ventas;
    private int posicion_dia = -1;
    private int posicion_mes = -1;
    private int posicion_anio = -1;
    private boolean mes_mostrados = false;
    private boolean anios_mostradas = false;

    private Button administrar_venta_aniadirventa_btn;
    private Button vt_ad_ver_venta_editar_btn;
    private FloatingActionButton ir_aniadir_vt_ftbtn;

    private boolean add_cliente_an =false;
    private boolean add_cliente_ed = false;
    private LinearLayout vt_estadisticasgenerales_admin_lyt;
    private LinearLayout vt_estadisticasusuario_lyt;
    private LinearLayout ver_vt_ad_lyt;
    private Adaptador_Lista_Ventas adptadorlistaview;
    private Venta venta_mostrar;
    private String[] meses = new String[]{"ENERO", "FEBRERO", "MAYO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    private int[] sales;
    private StorageReference mStorageRef;
    private int[] colors = new int[]{Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51), Color.rgb(255, 87, 51)};
    private Lista ventas;
    private Button ed_guardar_vt_ad_btn;
    private Button guardar_vt_ad_btn;
    private String cedulaVendedor="";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ventas_admin, container, false);
        patio = Patioventainterfaz.patioventa;

        mStorageRef = Patioventainterfaz.mStorageRef;

        //Botones
        //Pag principal
        Button administrar_venta_generales_btn = mainView.findViewById(R.id.administrar_venta_generales_btn);
        administrar_venta_aniadirventa_btn = mainView.findViewById(R.id.administrar_venta_aniadirventa_btn);
        Button vt_admin_estadisticas_btn = mainView.findViewById(R.id.vt_admin_estadisticas_btn);
        Button vt_admin_estadiciticas_usuario_btn= mainView.findViewById(R.id.vt_admin_estadiciticas_usuario_btn);
        //ADD
        Button descartar_vt_ad_btn = mainView.findViewById(R.id.descartar_vt_ad_btn);
        Button vt_ad_ver_venta_eliminar_btn= mainView.findViewById(R.id.vt_ad_ver_venta_eliminar_btn);
        ImageButton add_cliente_vt_ad_btn = mainView.findViewById(R.id.add_cliente_vt_ad_btn);
        ImageButton ed_add_cliente_vt_ad_btn = mainView.findViewById(R.id.ed_add_cliente_vt_ad_btn);
        guardar_vt_ad_btn = mainView.findViewById(R.id.guardar_vt_ad_btn);
        Button reg_list_vt_ad_btn = mainView.findViewById(R.id.reg_list_vt_ad_btn);
        ir_aniadir_vt_ftbtn = mainView.findViewById(R.id.ir_aniadir_vt_ftbtn);
        //Ver
        vt_ad_ver_venta_editar_btn = mainView.findViewById(R.id.vt_ad_ver_venta_editar_btn);
        ed_guardar_vt_ad_btn = mainView.findViewById(R.id.ed_guardar_vt_ad_btn);
        //LYT
        ventas_admin_generales_lyt = mainView.findViewById(R.id.ventas_admin_generales_lyt);
        add_vt_ad_lyt = mainView.findViewById(R.id.add_vt_ad_lyt);
        ventas_admin_lyt = mainView.findViewById(R.id.ventas_admin_lyt);
        ver_vt_ad_lyt = mainView.findViewById(R.id.ver_vt_ad_lyt);
        editar_vt_ad_lyt = mainView.findViewById(R.id.editar_vt_ad_lyt);
        vt_estadisticasgenerales_admin_lyt =mainView.findViewById(R.id.vt_estadisticasgenerales_admin_lyt);
        busqueda_ventas = mainView.findViewById(R.id.vt_busqueda_plcas_admin);
        vt_estadisticasusuario_lyt =mainView.findViewById(R.id.vt_estadisticasusuario_lyt);
        LinearLayout aniadir_cliente_vt_ad_lyt = mainView.findViewById(R.id.aniadir_cliente_vt_ad_lyt);

        administrar_venta_generales_btn.setOnClickListener(view -> irListaGenerales());
        administrar_venta_aniadirventa_btn.setOnClickListener(view -> {
            ventas_admin_generales_lyt.setVisibility(View.GONE);
            ventas_admin_lyt.setVisibility(View.GONE);
            add_vt_ad_lyt.setVisibility(View.VISIBLE);
            clearAniadirVenta();
            adaptadorAniadir();
            listaDesplegablesAniadir();
        });

        vt_ad_ver_venta_eliminar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Eliminar Venta");
            msg.setMessage("??Est??s seguro de eliminar esta venta?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    if(patio.removerVenta(venta_mostrar.getVehiculo().getPlaca())){
                        Toast.makeText(mainView.getContext(), "Se elimino correctamente la venta", Toast.LENGTH_SHORT).show();
                        irListaGenerales();
                    }
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "Error 155: No se pudo eliminar la venta", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        ir_aniadir_vt_ftbtn.setOnClickListener(v -> {
            administrar_venta_aniadirventa_btn.callOnClick();
        });

        vt_admin_estadisticas_btn.setOnClickListener(view -> {
            ir_aniadir_vt_ftbtn.setVisibility(View.GONE);
            sales = Patioventainterfaz.contadores(patio.getVentasGenerales());
            barChart= (BarChart)mainView.findViewById(R.id.gaficaVentasGenerales);
            createcharts(barChart);
            ventas_admin_generales_lyt.setVisibility(View.GONE);
            ventas_admin_lyt.setVisibility(View.GONE);
            vt_estadisticasgenerales_admin_lyt.setVisibility(View.VISIBLE);

        });

        vt_admin_estadiciticas_usuario_btn.setOnClickListener(view -> {
            ventas_admin_generales_lyt.setVisibility(View.GONE);
            ventas_admin_lyt.setVisibility(View.GONE);
            vt_estadisticasusuario_lyt.setVisibility(View.VISIBLE);
            adaptadorVendedoresGrafico();
        } );

        vt_ad_ver_venta_editar_btn.setOnClickListener(v -> {
            add_vt_ad_lyt.setVisibility(View.GONE);
            ventas_admin_generales_lyt.setVisibility(View.GONE);
            ventas_admin_lyt.setVisibility(View.GONE);
            ver_vt_ad_lyt.setVisibility(View.GONE);
            editar_vt_ad_lyt.setVisibility(View.VISIBLE);
            visualizarVentaEditable();
        });

        //add cliente
        add_cliente_vt_ad_btn.setOnClickListener(v -> {
            add_vt_ad_lyt.setVisibility(View.GONE);
            add_cliente_an = true;
            clearRegistrarCli();
            aniadir_cliente_vt_ad_lyt.setVisibility(View.VISIBLE);
        });

        ed_add_cliente_vt_ad_btn.setOnClickListener(v -> {
            editar_vt_ad_lyt.setVisibility(View.GONE);
            add_cliente_ed = true;
            clearRegistrarCli();
            aniadir_cliente_vt_ad_lyt.setVisibility(View.VISIBLE);
        });

        reg_list_vt_ad_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Registrar Cliente");
            msg.setMessage("??Est??s seguro de registrar un cliente con estos datos?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {
                if(registrarCliente()) {
                    Toast.makeText(mainView.getContext(), "Se registr?? el cliente", Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        ed_guardar_vt_ad_btn.setOnClickListener(v -> {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_vt_ad_actv);
            String cli = cliente.getText().toString();
            Cliente aux = patio.buscarClientes("Cedula",cli);
            if(aux==null){
                android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                msg.setTitle("CLIENTE NO REGISTRADO");
                msg.setMessage("Presione 'Si' para a??adir al cliente" );
                msg.setPositiveButton("Si", (dialog, which) -> add_cliente_vt_ad_btn.callOnClick());
                msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                msg.show();
            }else{
                AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                msg.setTitle("Editar Venta");
                msg.setMessage("??Est??s seguro de editar esta venta?");
                msg.setPositiveButton("Aceptar", (dialog, which) -> {
                    try {
                        if (editarVenta()) {
                            irListaGenerales();
                        }
                    } catch (Exception e) {
                        Toast.makeText(mainView.getContext(), "Error 61: No se pudo editar la venta", Toast.LENGTH_SHORT).show();
                    }
                });
                msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                msg.show();
            }
        });

        guardar_vt_ad_btn.setOnClickListener(view -> {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
            String cli = cliente.getText().toString();
            Cliente aux = patio.buscarClientes("Cedula",cli);
            if(aux==null){
                android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                msg.setTitle("CLIENTE NO REGISTRADO");
                msg.setMessage("Presione 'Si' para a??adir al cliente" );
                msg.setPositiveButton("Si", (dialog, which) -> add_cliente_vt_ad_btn.callOnClick());
                msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                msg.show();
            }else{
                AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                msg.setTitle("Registrar Venta");
                msg.setMessage("??Est??s seguro de registrar esta nueva venta?");
                msg.setPositiveButton("Aceptar", (dialog, which) -> {
                    try {
                        if (registarVenta()) {
                            irListaGenerales();
                            Toast.makeText(mainView.getContext(), "Se registro la venta", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(mainView.getContext(), "Error 62: No se registro la venta", Toast.LENGTH_SHORT).show();
                    }
                });
                msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                msg.show();
            }
        });

        descartar_vt_ad_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Descartar Venta");
            msg.setMessage("??Est??s seguro de descartar la nueva venta?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> irPaginaPrincipal());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //todo poner en la condicion el lyt de editar
                if (add_vt_ad_lyt.getVisibility() == View.VISIBLE) {
                    android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("??Est??s seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> irListaGenerales());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if (editar_vt_ad_lyt.getVisibility() == View.VISIBLE) {
                    android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("??Est??s seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> irListaGenerales());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if(aniadir_cliente_vt_ad_lyt.getVisibility() == View.VISIBLE  && add_cliente_an){
                    android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("??Est??s seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            aniadir_cliente_vt_ad_lyt.setVisibility(View.GONE);
                            add_vt_ad_lyt.setVisibility(View.VISIBLE);
                            add_cliente_an = false;
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 164: No se pudo ejecutar la acci??n", Toast.LENGTH_SHORT).show();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }else if(aniadir_cliente_vt_ad_lyt.getVisibility() == View.VISIBLE  && add_cliente_ed){
                    android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("??Est??s seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            editar_vt_ad_lyt.setVisibility(View.VISIBLE);
                            aniadir_cliente_vt_ad_lyt.setVisibility(View.GONE);
                            add_cliente_ed = false;
                        } catch (Exception e) {
                            Toast.makeText(mainView.getContext(), "Error 165: No se pudo ejecutar la acci??n", Toast.LENGTH_SHORT).show();

                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if(vt_estadisticasgenerales_admin_lyt.getVisibility()==View.VISIBLE ||vt_estadisticasusuario_lyt.getVisibility()==View.VISIBLE){
                    irPaginaPrincipal();
                }
                if( ventas_admin_generales_lyt.getVisibility() == View.VISIBLE){
                    irPaginaPrincipal();
                }
                if (ver_vt_ad_lyt.getVisibility() == View.VISIBLE) {
                    irListaGenerales();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        busqueda_ventas.setOnQueryTextListener(this);
        ventas = patio.getVentasGenerales();
        return mainView;
    }

    public void setCedulaVendedor(String cedula){
        cedulaVendedor=cedula;
    }

    public void listaDesplegablesAniadir() {
        posicion_dia =-1;
        posicion_mes=-1;
        posicion_anio=-1;
        TextInputLayout anio_lyt = mainView.findViewById(R.id.anio_vt_ad_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_vt_ad_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.dia_vt_ad_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_vt_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_vt_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_vt_ad_acv);

        anio_lyt.setEndIconOnClickListener(v -> anio.performClick());
        anio.setOnClickListener(v -> {
            if (anios_mostradas) {
                anio.dismissDropDown();
                anios_mostradas = false;
            } else {
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.anios);
                anio.setAdapter(adapt);
                anio.showDropDown();
                anios_mostradas = true;
            }
        });

        anio.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));

        mes_lyt.setEndIconOnClickListener(v -> mes.performClick());
        mes.setOnClickListener(v -> {
            if (mes_mostrados) {
                mes.dismissDropDown();
                mes_mostrados = false;
            } else {
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.meses);
                mes.setAdapter(adapt_mes);
                mes.showDropDown();
                mes_mostrados = true;
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
    }

    public void listaDesplegablesEditar() {
        posicion_dia =-1;
        posicion_mes=-1;
        posicion_anio=-1;
        TextInputLayout anio_lyt = mainView.findViewById(R.id.ed_anio_vt_ad_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.ed_mes_vt_ad_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.ed_dia_vt_ad_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_vt_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_vt_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_vt_ad_acv);

        anio_lyt.setEndIconOnClickListener(v -> anio.performClick());
        anio.setOnClickListener(v -> {
            if (anios_mostradas) {
                anio.dismissDropDown();
                anios_mostradas = false;
            } else {
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.anios);
                anio.setAdapter(adapt);
                anio.showDropDown();
                anios_mostradas = true;
            }
        });

        anio.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));

        mes_lyt.setEndIconOnClickListener(v -> mes.performClick());
        mes.setOnClickListener(v -> {
            if (mes_mostrados) {
                mes.dismissDropDown();
                mes_mostrados = false;
            } else {
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.meses);
                mes.setAdapter(adapt_mes);
                mes.showDropDown();
                mes_mostrados = true;
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
    }

    public void irListaGenerales() {
        add_vt_ad_lyt.setVisibility(View.GONE);
        editar_vt_ad_lyt.setVisibility(View.GONE);
        ver_vt_ad_lyt.setVisibility(View.GONE);
        ventas_admin_lyt.setVisibility(View.GONE);
        cargar();
        ir_aniadir_vt_ftbtn.setVisibility(View.VISIBLE);
        ventas_admin_generales_lyt.setVisibility(View.VISIBLE);
    }

    public void irVerVenta() {
        add_vt_ad_lyt.setVisibility(View.GONE);
        ventas_admin_generales_lyt.setVisibility(View.GONE);
        ventas_admin_lyt.setVisibility(View.GONE);
        ver_vt_ad_lyt.setVisibility(View.VISIBLE);
        ir_aniadir_vt_ftbtn.setVisibility(View.GONE);
        visualizarVenta();
    }

    public void irPaginaPrincipal() {
        vt_estadisticasgenerales_admin_lyt.setVisibility(View.GONE);
        vt_estadisticasusuario_lyt.setVisibility(View.GONE);
        ventas_admin_generales_lyt.setVisibility(View.GONE);
        add_vt_ad_lyt.setVisibility(View.GONE);
        editar_vt_ad_lyt.setVisibility(View.GONE);
        ver_vt_ad_lyt.setVisibility(View.GONE);
        ir_aniadir_vt_ftbtn.setVisibility(View.GONE);
        ventas_admin_lyt.setVisibility(View.VISIBLE);
    }

    public void adaptadorAniadir() {
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_vt_ad_txt = mainView.findViewById(R.id.cedula_vendedor_vt_ad_txt);
        cedula_vendedor_vt_ad_txt.setText(usuario_admin.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_ad_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void adaptadorVendedoresGrafico() {
        AutoCompleteTextView vendedor = mainView.findViewById(R.id.buscar_vendedor_vt_ad_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasVendedores());
        vendedor.setAdapter(adapter);
        vendedor.setOnItemClickListener((adapterView, view, i, l) -> {
            setCedulaVendedor((String) adapterView.getItemAtPosition(i));
            try {
                Vendedor temp =patio.buscarVendedores("Cedula",cedulaVendedor);
                sales=Patioventainterfaz.contadores(temp.obtenerVentas());
                Usuarios=(BarChart)mainView.findViewById(R.id.gaficaVentasusuario);
                createcharts(Usuarios);
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 63: No se pudo generar el gr??fico", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void adaptadorEditar() {
        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_vt_ad_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.ed_cedula_vendedor_vt_ad_etxt);
        cedula_vendedor_ci_vn_txt.setText(usuario_admin.getCedula());

        TextView auto = mainView.findViewById(R.id.ed_placa_vt_ad_txt);
        auto.setText(venta_mostrar.getVehiculo().getPlaca());
    }

    public void visualizarVentaEditable() {
            adaptadorEditar();
            listaDesplegablesEditar();
            ImageView ed_auto_vt_ad_img = mainView.findViewById(R.id.ed_auto_vt_ad_img);
            AutoCompleteTextView ed_anio_vt_ad_acv = mainView.findViewById(R.id.ed_anio_vt_ad_acv);
            AutoCompleteTextView ed_mes_vt_ad_acv = mainView.findViewById(R.id.ed_mes_vt_ad_acv);
            AutoCompleteTextView ed_dia_vt_ad_acv = mainView.findViewById(R.id.ed_dia_vt_ad_acv);
            AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_vt_ad_actv);
            TextView vendedor = mainView.findViewById(R.id.ed_cedula_vendedor_vt_ad_etxt);
            TextView placa = mainView.findViewById(R.id.ed_placa_vt_ad_txt);
            EditText precioV = mainView.findViewById(R.id.ed_precio_vt_ad_etxt);

            StorageReference filePath = mStorageRef.child("Vehiculos/" + venta_mostrar.getVehiculo().getimagen());
            try {
                final File localFile = File.createTempFile(venta_mostrar.getVehiculo().getimagen(), "jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                    ed_auto_vt_ad_img.setImageURI(nuevo);
                });
            } catch (IOException e) {
                Toast.makeText(mainView.getContext(), "Error 64: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }

            String fechaVenta = Patioventainterfaz.getFechaMod(venta_mostrar.getFecha());
            String dia_s = fechaVenta.split("-")[0];
            String mes_s = fechaVenta.split("-")[1];
            String anio_s = fechaVenta.split("-")[2];

            for (int i = 0; i < Patioventainterfaz.anios.length; i++) {
                if (Patioventainterfaz.anios[i].equals(anio_s))
                    posicion_anio = i;
            }
            posicion_dia = Integer.parseInt(dia_s)-1;
            posicion_mes = Integer.parseInt(mes_s) - 1;

            ed_anio_vt_ad_acv.setText(anio_s);
            ed_mes_vt_ad_acv.setText(Patioventainterfaz.meses[posicion_mes]);
            ed_dia_vt_ad_acv.setText(dia_s);

            cliente.setText(venta_mostrar.getComprador().getCedula());
            vendedor.setText(usuario_admin.getCedula());
            placa.setText(venta_mostrar.getVehiculo().getPlaca());
            precioV.setText(String.format("%.2f", venta_mostrar.getPrecio()));
    }

    public boolean editarVenta() {
        Cliente cliente_c = null;
        int vacios = 0;
        int invalidos = 0;

        AutoCompleteTextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_vt_ad_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "N??mero de c??dula inv??lido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                invalidos++;
            }
            cliente_c = patio.buscarClientes("Cedula", cliente_str);
        } else {
            vacios++;
        }

        EditText precio = mainView.findViewById(R.id.ed_precio_vt_ad_etxt);

        float precio_flt = 0;
        if (!isEmpty(precio)) {
            String precio_s = precio.getText().toString();
            precio_flt = Float.parseFloat(precio_s);
        } else {
            vacios++;
        }

        if((posicion_anio==-1 || posicion_mes==-1) || posicion_dia==-1){
            vacios++;
        }

        if (vacios == 0 && invalidos==0) {
            String fecha_nueva_venta = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
            Date fecha = null;
            try {
                fecha = sdf.parse(fecha_nueva_venta);
            } catch (ParseException e) {
                Toast.makeText(mainView.getContext(), "Error 65: No se pudo generar la fecha (Parse)", Toast.LENGTH_SHORT).show();
            }
            venta_mostrar.actualizar(
                    fecha,
                    precio_flt,
                    venta_mostrar.getVehiculo(),
                    cliente_c,
                    usuario_admin);
            if (patio.buscarVentas(venta_mostrar.getVehiculo().getPlaca(), cliente_c.getCedula()) != null) {
                Toast.makeText(mainView.getContext(), "Se edit?? correctamente la venta", Toast.LENGTH_SHORT).show();
                return true;
            }
        }else if(vacios>0){
            Toast.makeText(mainView.getContext(), "Existen campos vac??os", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean registarVenta()  {

        adaptadorAniadir();
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int vacios = 0;
        int invalidos =0;

        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_ad_actv);

        if (!isEmpty(cliente)) {
            String cliente_str = cliente.getText().toString();
            if (cliente_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "N??mero de c??dula inv??lido", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mainView.getContext(), "No existe el veh??culo", Toast.LENGTH_SHORT).show();
                auto.setText("");
                invalidos++;
            }
        } else {
            vacios++;
        }

        EditText precio_vt_ad_etxt = mainView.findViewById(R.id.precio_vt_ad_etxt);
        String precio_str = precio_vt_ad_etxt.getText().toString();
        float precio = Float.parseFloat(precio_str);

        if((posicion_anio==-1 || posicion_mes==-1) || posicion_dia==-1){
            vacios++;
        }

        if (vacios == 0 && invalidos==0) {

            try {
                String fecha_nueva_venta = (posicion_dia + 1) + "-" + (posicion_mes + 1) + "-" + Patioventainterfaz.anios[posicion_anio];
                Date fecha = sdf.parse(fecha_nueva_venta);
                Venta nueva = new Venta(
                        fecha,
                        cliente_c,
                        usuario_admin,
                        vehiculo,
                        precio);
                patio.aniadirVenta(nueva);
                if (patio.getVentasGenerales().contiene(nueva)) {
                    Toast.makeText(mainView.getContext(), "Se agrego correctamente la venta", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 0", Toast.LENGTH_SHORT).show();
            }

        }else if(vacios>0){
            Toast.makeText(mainView.getContext(), "Existen campos vac??os", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.lista_ventas_admin_rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Ventas(patio.getVentasGenerales().copiar(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    public void clearRegistrarCli(){
        EditText add_nombre_vt_ad_etxt = mainView.findViewById(R.id.add_nombre_vt_ad_etxt);
        EditText reg_apellido_vt_ad_etxt = mainView.findViewById(R.id.reg_apellido_vt_ad_etxt);
        EditText reg_cedula_vt_ad_etxt = mainView.findViewById(R.id.reg_cedula_vt_ad_etxt);
        EditText reg_dia_vt_ad_etxt = mainView.findViewById(R.id.reg_dia_vt_ad_etxt);
        EditText reg_mes_vt_ad_etxt = mainView.findViewById(R.id.reg_mes_vt_ad_etxt);
        EditText reg_anio_vt_ad_etxt = mainView.findViewById(R.id.reg_anio_vt_ad_etxt);
        EditText reg_telefono_vt_ad_etxt = mainView.findViewById(R.id.reg_telefono_vt_ad_etxt);
        EditText reg_correo_vt_ad_etxt = mainView.findViewById(R.id.reg_correo_vt_ad_etxt);

        add_nombre_vt_ad_etxt.getText().clear();
        reg_apellido_vt_ad_etxt.getText().clear();
        reg_cedula_vt_ad_etxt.getText().clear();
        reg_dia_vt_ad_etxt.getText().clear();
        reg_mes_vt_ad_etxt.getText().clear();
        reg_anio_vt_ad_etxt.getText().clear();
        reg_telefono_vt_ad_etxt.getText().clear();
        reg_correo_vt_ad_etxt.getText().clear();
    }

    public void clearAniadirVenta(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_ad_actv);
        EditText precio_vt_ad_etxt = mainView.findViewById(R.id.precio_vt_ad_etxt);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_vt_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_vt_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_vt_ad_acv);

        cliente.getText().clear();
        auto.getText().clear();
        precio_vt_ad_etxt.getText().clear();
        anio.getText().clear();
        mes.getText().clear();
        dias.getText().clear();
    }

    public boolean registrarCliente(){

        EditText add_nombre_vt_ad_etxt = mainView.findViewById(R.id.add_nombre_vt_ad_etxt);
        EditText reg_apellido_vt_ad_etxt = mainView.findViewById(R.id.reg_apellido_vt_ad_etxt);
        EditText reg_cedula_vt_ad_etxt = mainView.findViewById(R.id.reg_cedula_vt_ad_etxt);
        EditText reg_dia_vt_ad_etxt = mainView.findViewById(R.id.reg_dia_vt_ad_etxt);
        EditText reg_mes_vt_ad_etxt = mainView.findViewById(R.id.reg_mes_vt_ad_etxt);
        EditText reg_anio_vt_ad_etxt = mainView.findViewById(R.id.reg_anio_vt_ad_etxt);
        EditText reg_telefono_vt_ad_etxt = mainView.findViewById(R.id.reg_telefono_vt_ad_etxt);
        EditText reg_correo_vt_ad_etxt = mainView.findViewById(R.id.reg_correo_vt_ad_etxt);

        int vacios = 0;
        int invalidos = 0;
        String nombre_str = add_nombre_vt_ad_etxt.getText().toString();
        if (nombre_str.isEmpty()) {
            vacios++;
        }

        String apellido_str = reg_apellido_vt_ad_etxt.getText().toString();
        if (apellido_str.isEmpty()) {
            vacios++;
        }

        String cedula_str = reg_cedula_vt_ad_etxt.getText().toString();
        if (cedula_str.isEmpty()) {
            vacios++;
        } else {
            if (cedula_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "N??mero de c??dula inv??lido", Toast.LENGTH_SHORT).show();
                reg_cedula_vt_ad_etxt.setText("");
                invalidos++;
            }
        }

        String mes_str = reg_mes_vt_ad_etxt.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            vacios++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainView.getContext(), "Mes inv??lido", Toast.LENGTH_SHORT).show();
                reg_mes_vt_ad_etxt.setText("");
                invalidos++;
            }
        }

        String anio_str = reg_anio_vt_ad_etxt.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            vacios++;
        } else {
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(mainView.getContext(), "A??o inv??lido", Toast.LENGTH_SHORT).show();
                reg_anio_vt_ad_etxt.setText("");
                invalidos++;
            }
        }

        String dia_str = reg_dia_vt_ad_etxt.getText().toString();
        int dia=-1;
        if (dia_str.isEmpty()) {
            vacios++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainView.getContext(), "D??a inv??lido", Toast.LENGTH_SHORT).show();
                reg_dia_vt_ad_etxt.setText("");
                invalidos++;
            }
        }

        String telefono_str = reg_telefono_vt_ad_etxt.getText().toString();
        if (telefono_str.isEmpty()) {
            vacios++;
        } else {
            if (telefono_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Numero de telefono inv??lido", Toast.LENGTH_SHORT).show();
                reg_telefono_vt_ad_etxt.setText("");
                invalidos++;
            }
        }

        String correo_str = reg_correo_vt_ad_etxt.getText().toString();
        if (correo_str.isEmpty()) {
            vacios++;
        } else {
            if (!Patioventainterfaz.validarMail(correo_str)) {
                Toast.makeText(mainView.getContext(), "Correo no v??lido", Toast.LENGTH_SHORT).show();
                reg_correo_vt_ad_etxt.setText("");
                invalidos++;
            }
        }

        if (vacios == 0 && invalidos == 0) {
            try {
                Date fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
                Cliente cliente = new Cliente(nombre_str, cedula_str, telefono_str, correo_str,fecha);
                if (patio.aniadirUsuario(cliente, "Cliente")) {
                    if(add_cliente_an){
                        ventas_admin_generales_lyt.setVisibility(View.GONE);
                        ventas_admin_lyt.setVisibility(View.GONE);
                        add_vt_ad_lyt.setVisibility(View.VISIBLE);
                        adaptadorAniadir();
                        AutoCompleteTextView cliente_an = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
                        cliente_an.setText(cedula_str);
                    }else if(add_cliente_ed){
                        vt_ad_ver_venta_editar_btn.callOnClick();
                        AutoCompleteTextView cliente_ed = mainView.findViewById(R.id.ed_cedula_cliente_vt_ad_actv);
                        cliente_ed.setText(cedula_str);
                    }
                    Toast.makeText(mainView.getContext(), "Se registro el cliente correctamente", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 66: No se pudo registrar el cliente", Toast.LENGTH_SHORT).show();
            }
        }else if(vacios>0){
            Toast.makeText(mainView.getContext(), "Existen campos vac??os", Toast.LENGTH_SHORT).show();
        }
        return false;
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
        if (newText.length() == 2) {
            b += "-";
            busqueda_ventas.setQuery(b, false);
        }
        if (newText.length() == 5) {
            b += "-";
            busqueda_ventas.setQuery(b, false);
        }
        if (newText.length() > 10) {
            b = b.substring(0, 10);
            busqueda_ventas.setQuery(b, false);
        }
        adptadorlistaview.buscar(b);
        return false;
    }

    private Chart getSameChart(Chart chart, String leyenda, int colores, int fondo, int yxes) {
        chart.getDescription().setText(leyenda);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(fondo);
        chart.animateY(yxes);
        legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < meses.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = meses[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sales.length; i++) {
            entries.add(new BarEntry(i, sales[i]));
        }

        return entries;
    }

    private void axisX(XAxis axis) {

        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(meses));

    }

    private void axisXleft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }

    private void axisXRight(YAxis axis) {
        axis.setEnabled(false);

    }

    public void createcharts(BarChart barChart) {
        barChart = (BarChart) getSameChart(barChart, "", Color.RED, Color.rgb(253, 254, 254), 30000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);
        barChart.setData(getbardata());
        barChart.invalidate();
        axisX(barChart.getXAxis());
        axisXleft(barChart.getAxisLeft());
        axisXRight(barChart.getAxisRight());
    }

    public ArrayList<String> diaListaDesplegable() {
        ArrayList<String> dias = new ArrayList<>();
        int anioa = Integer.parseInt(Patioventainterfaz.anios[posicion_anio]);
        int i;
        for (i = 1; i <= Patioventainterfaz.diasLista[posicion_mes]; i++) {
            dias.add(String.valueOf(i));
        }
        if (Patioventainterfaz.esBisiesto(anioa) && posicion_mes == 1) {
            dias.add(String.valueOf(i + 1));
        }
        return dias;
    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getbardata() {
        BarDataSet barDataSet = (BarDataSet) getData(new BarDataSet(getBarEntries(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }

    public void visualizarVenta() {
        ImageView imagen = mainView.findViewById(R.id.vt_ad_vehiculo_img);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_vt_ad_txt);
        TextView precioV = mainView.findViewById(R.id.ver_pventa_vt_ad_txt);
        TextView precioI = mainView.findViewById(R.id.ver_pinicial_vt_ad_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_vt_ad_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_vt_ad_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_cliente_vt_ad_txt);

        TextView placa = mainView.findViewById(R.id.ver_placa_vt_ad_txt);
        TextView matricula = mainView.findViewById(R.id.ver_matricula_vt_ad_txt);
        TextView matriculado = mainView.findViewById(R.id.ver_matriculado_vt_ad_txt);
        TextView marca = mainView.findViewById(R.id.ver_marca_vt_ad_txt);
        TextView modelo = mainView.findViewById(R.id.ver_modelo_vt_ad_txt);
        TextView anio = mainView.findViewById(R.id.ver_anio_vt_ad_txt);

        StorageReference filePath = mStorageRef.child("Vehiculos/" + venta_mostrar.getVehiculo().getimagen());
        try {
            final File localFile = File.createTempFile(venta_mostrar.getVehiculo().getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                imagen.setImageURI(nuevo);
            });
        } catch (IOException e) {
            Toast.makeText(mainView.getContext(), "Error 67: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }

        fecha.setText(Patioventainterfaz.getFechaMod(venta_mostrar.getFecha()));
        precioV.setText(String.format("$ %.2f", venta_mostrar.getPrecio()));
        precioI.setText(String.format("$ %.2f", venta_mostrar.getVehiculo().getPrecioInicial()));
        vendedor.setText(venta_mostrar.getVendedor().getNombre());
        cliente.setText(venta_mostrar.getComprador().getNombre());
        contacto.setText(venta_mostrar.getComprador().getTelefono());

        placa.setText(venta_mostrar.getVehiculo().getPlaca());
        matricula.setText(venta_mostrar.getVehiculo().getMatricula());
        if (venta_mostrar.getVehiculo().isMatriculado()) {
            matriculado.setText("Si");
        } else {
            matriculado.setText("No");
        }
        marca.setText(venta_mostrar.getVehiculo().getMarca());
        modelo.setText(venta_mostrar.getVehiculo().getModelo());
        anio.setText(String.valueOf(venta_mostrar.getVehiculo().getAnio()));
    }

    @Override
    public void itemClick(String placa, String cliente) {
        venta_mostrar = patio.buscarVentas(placa, cliente);
        irVerVenta();
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



}
