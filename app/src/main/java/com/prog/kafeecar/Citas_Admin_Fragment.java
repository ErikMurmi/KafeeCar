package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static com.prog.kafeecar.Patioventainterfaz.getFechaMod;
import static com.prog.kafeecar.Patioventainterfaz.patioventa;

public class Citas_Admin_Fragment extends Fragment implements Adaptador_Lista_Citas_Admin.RecyclerItemClick{

    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Citas_Admin";
    private PatioVenta patio;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private RecyclerView listaview;
    private Adaptador_Lista_Citas_Admin adptadorlistaview;
    //Auxiliar para pasar del vehiculo a registrar cita
    //TODO
    private static Vehiculo aux;
    private View mainView;


    //Image Buttons
    private ImageButton buscar_btn;

    private EditText vehiculo_nuevacita;
    //Botones
    private Button irVerCita;
    private Button irAniadirCita;

    private LinearLayout verCita;
    private LinearLayout listaCitas;
    private LinearLayout aniadirCita;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_admin,container,false);
        patio = Patioventainterfaz.patioventa;
        //Layouts
        verCita = mainView.findViewById(R.id.ver_cita_lyt);
        aniadirCita = mainView.findViewById(R.id.add_cita_admin_lyt);
        listaCitas = mainView.findViewById(R.id.citas_admin_lyt);

        //Botones
        irAniadirCita = mainView.findViewById(R.id.ir_aniadir_btn2);
        //TextViews
        vehiculo_nuevacita = mainView.findViewById(R.id.vehiculo_txt);
        //Image Buttons
        buscar_btn = mainView.findViewById(R.id.busqueda_citas_admin_btn);
        //OnClick

        /*irVerCita.setOnClickListener(v -> {
            aniadirCita.setVisibility(View.GONE);
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.VISIBLE);
            try {
                visualizarCita();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        irAniadirCita.setOnClickListener(v -> {
            listaCitas.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.VISIBLE);
        });
*/
        buscar_btn.setOnClickListener(v -> {
            CheckBox dia = mainView.findViewById(R.id.filtro_dia_ckb);
            CheckBox mes = mainView.findViewById(R.id.filtro_mes_ckb);
            CheckBox anio = mainView.findViewById(R.id.filtro_anio_ckb);
            CheckBox hora = mainView.findViewById(R.id.filtro_hora_ckb);
        });

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
        return mainView;
    }

    public void cargar() throws Exception {
        listaview = mainView.findViewById(R.id.rc_citas_admin);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Citas_Admin(patio.getCitas(),this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }


    public void visualizarCita() throws Exception {
        TextView fecha = mainView.findViewById(R.id.fechaCita_txt);
        TextView hora = mainView.findViewById(R.id.horaCita_txt);
        TextView cliente = mainView.findViewById(R.id.clienteCita_txt);
        TextView contacto = mainView.findViewById(R.id.contactoCita_txt);
        TextView vendedor = mainView.findViewById(R.id.vendedorCita_txt);
        TextView vehiculo = mainView.findViewById(R.id.vehiculoCita_txt);
        TextView descripcion = mainView.findViewById(R.id.descripcionCita_txt);
        TextView resolucion = mainView.findViewById(R.id.resolucionCita_txt);
        TextView precio = mainView.findViewById(R.id.precioVentaCita_txt);
        Cita citaPrueba = (Cita) patio.getCitas().getPos(0);
        fecha.setText(new String(fecha.getText().toString() + getFechaMod(citaPrueba.getFechaCita())));
        hora.setText(new String(hora.getText().toString() + citaPrueba.getHora()));
        cliente.setText(new String(cliente.getText().toString() + citaPrueba.getVisitante().getNombre()));
        contacto.setText(new String(contacto.getText().toString() + citaPrueba.getVisitante().getTelefono()));
        vendedor.setText(new String(vendedor.getText().toString() + citaPrueba.getVendedorCita().getNombre()));
        vehiculo.setText(new String(vehiculo.getText().toString() + citaPrueba.getVehiculo().getModelo()));
        descripcion.setText(new String(descripcion.getText().toString() + citaPrueba.getVehiculo().getDescripcion()));
        resolucion.setText(new String(resolucion.getText().toString() + citaPrueba.getResolucion()));
        precio.setText(new String(precio.getText().toString() + " $" + citaPrueba.getVehiculo().getPrecioVenta()));
    }

    public void registarCita(View v) throws Exception {
        EditText fechacitadia = mainView.findViewById(R.id.fechacitadia_txt);
        EditText fechacitames = mainView.findViewById(R.id.fechacitames_txt);
        EditText fechacitaanio = mainView.findViewById(R.id.fechacitaanio_txt);
        EditText fechacitahora = mainView.findViewById(R.id.fechacitahoracita_txt);
        Vendedor vendedor_v =null;
        Cliente cliente_c =null;
        Vehiculo vehiculo =null;
        String fechacita_str ="";
        int c = 0;
        int hora =-1;
        if((!isEmpty(fechacitadia) && !isEmpty(fechacitames)) && (!isEmpty(fechacitaanio) && !isEmpty(fechacitahora))){
            fechacita_str = fechacitadia.getText().toString()+ "-" + fechacitames.getText().toString() + "-" + fechacitaanio.getText().toString();
            hora = Integer.parseInt(fechacitahora.getText().toString());
        }else{
            c++;
            Toast.makeText(mainView.getContext(), "Campos de fecha vacíos", Toast.LENGTH_SHORT).show();
        }
        EditText cliente = mainView.findViewById(R.id.cliente_txt);
        EditText vendedor = mainView.findViewById(R.id.vendedor_txt);
        EditText auto= mainView.findViewById(R.id.vehiculo_txt);
        if(!isEmpty(cliente)){
            String cliente_str = cliente.getText().toString();
            cliente_c = patio.buscarClientes("Cedula", cliente_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(), "Campo de cliente vacio", Toast.LENGTH_SHORT).show();
        }

        if(!isEmpty(vendedor)){
            String vendedor_str = vendedor.getText().toString();
            vendedor_v = patio.buscarVendedores("Cedula", vendedor_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(), "Campo de vendedor vacio", Toast.LENGTH_SHORT).show();
        }

        if(!isEmpty(auto)){
            String vehiculo_str = auto.getText().toString();
            vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(), "Campo de vehiculo vacio", Toast.LENGTH_SHORT).show();
        }

        EditText resolucion = mainView.findViewById(R.id.resolucion_txt);
        String resolucion_str = resolucion.getText().toString();

        if(c==0){
            Cita nueva = new Cita(Patioventainterfaz.sdf.parse(fechacita_str), hora, resolucion_str, cliente_c, vendedor_v, vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se agrego correctamente.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String formatoHora(int hora){
        if(hora>12){
            return "pm";
        }
        return "am";
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa) {

    }
}
