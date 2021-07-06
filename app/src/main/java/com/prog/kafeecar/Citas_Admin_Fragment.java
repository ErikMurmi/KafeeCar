package com.prog.kafeecar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.prog.kafeecar.Patioventainterfaz.getFechaMod;

public class Citas_Admin_Fragment extends Fragment implements Adaptador_Lista_Citas.RecyclerItemClick, SearchView.OnQueryTextListener {

    private String TAG = "Citas_Admin";
    private PatioVenta patio;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private Adaptador_Lista_Citas adptadorlistaview;
    //Auxiliar para pasar del vehiculo a registrar cita
    //TODO
    private static Vehiculo aux;
    private View mainView;
    private SearchView busqueda_citas;
    private Cita cita_mostrar;
    //Image Buttons
    private ImageButton buscar_btn;

    private EditText vehiculo_nuevacita;
    private EditText dia_b;
    private EditText mes_b;
    private EditText anio_b;
    //Botones
    private Button irVerCita;
    private FloatingActionButton irAniadirCita;

    private LinearLayout verCita;
    private LinearLayout listaCitas;
    private LinearLayout aniadirCita;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        //Layouts
        busqueda_citas = mainView.findViewById(R.id.busqueda_c_ad_srv);
        verCita = mainView.findViewById(R.id.ver_ci_ad_lyt);
        aniadirCita = mainView.findViewById(R.id.add_cita_admin_lyt);
        listaCitas = mainView.findViewById(R.id.citas_lyt);

        //Botones
        irAniadirCita = mainView.findViewById(R.id.ir_aniadir_cita_ad_fbtn);
        //TextViews
        vehiculo_nuevacita = mainView.findViewById(R.id.vehiculo_txt);
        //Edit Text
        dia_b = mainView.findViewById(R.id.dia_busqueda_etxt);
        mes_b = mainView.findViewById(R.id.mes_busqueda_etxt);
        anio_b = mainView.findViewById(R.id.anio_busqueda_etxt);
        //Image Buttons
        buscar_btn = mainView.findViewById(R.id.busqueda_citas_admin_btn);
        //OnClick

        irAniadirCita.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.VISIBLE);
        });


        /*buscar_btn.setOnClickListener(v -> {
            CheckBox dia = mainView.findViewById(R.id.filtro_dia_ckb);
            CheckBox mes = mainView.findViewById(R.id.filtro_mes_ckb);
            CheckBox anio = mainView.findViewById(R.id.filtro_anio_ckb);
            CheckBox hora = mainView.findViewById(R.id.filtro_hora_ckb);
        });*/

        if(Patioventainterfaz.CITA_CON_VEHICULO){
            irAniadirCita.callOnClick();
            vehiculo_nuevacita.setText(Patioventainterfaz.v_aux_cita.getPlaca());
            vehiculo_nuevacita.setTextColor(Color.BLACK);
        }else{
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        /*buscar_btn.setOnClickListener(v -> {
            String dia = dia_b.getText().toString();
            String mes = mes_b.getText().toString();
            String anio = anio_b.getText().toString();
            adptadorlistaview.buscar(dia,mes,anio);
        });*/
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
        busqueda_citas.setVisibility(View.VISIBLE);
        cargar();
        verCita.setVisibility(View.GONE);
    }

    public void visualizarCita() {
        listaCitas.setVisibility(View.GONE);
        irAniadirCita.setVisibility(View.GONE);
        verCita.setVisibility(View.VISIBLE);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_ci_ad_txt);
        TextView hora = mainView.findViewById(R.id.ver_hora_ci_ad_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_ci_ad_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_ci_ad_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_ci_ad_txt);
        TextView vehiculo = mainView.findViewById(R.id.ver_vehiculo_ci_ad_txt);
        TextView precio = mainView.findViewById(R.id.ver_precioVenta_ci_ad_txt);
        TextView descripcion = mainView.findViewById(R.id.ver_descripcion_ci_ad_txt);
        TextView resolucion = mainView.findViewById(R.id.ver_resolucion_ci_ad_txt);

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

    public void registarCita(View v) throws Exception {
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
}
