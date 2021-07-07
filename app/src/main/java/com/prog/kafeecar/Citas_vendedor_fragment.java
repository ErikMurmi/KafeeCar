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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Citas_vendedor_fragment extends Fragment implements Adaptador_Lista_Citas.RecyclerItemClick {

    private View mainView;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private PatioVenta patio;
    Cita cita_mostrar;

    public EditText placa_ci_vn_etxt;

    private LinearLayout aniadir_ci_vn_lyt;
    private LinearLayout citas_vendedor_lyt;
    private LinearLayout ver_ci_vn_lyt;
    private LinearLayout editar_ci_vn_lyt;

    private final Vendedor usuarioActual = (Vendedor) Patioventainterfaz.usuarioActual;

    private FloatingActionButton ir_aniadir_ci_vn_btn;

    @SuppressLint("CutPasteId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;

        placa_ci_vn_etxt = mainView.findViewById(R.id.placa_ci_vn_actv);
        ver_ci_vn_lyt = mainView.findViewById(R.id.ver_ci_vn_lyt);
        citas_vendedor_lyt = mainView.findViewById(R.id.citas_vendedor_lyt);
        aniadir_ci_vn_lyt = mainView.findViewById(R.id.aniadir_ci_vn_lyt);
        editar_ci_vn_lyt = mainView.findViewById(R.id.editar_ci_vn_lyt);

        ir_aniadir_ci_vn_btn = mainView.findViewById(R.id.ir_aniadir_ci_vn_btn);
        Button guardar_ci_vn_btn = mainView.findViewById(R.id.guardar_ci_vn_btn);
        Button anular_ci_vn_btn = mainView.findViewById(R.id.anular_ci_vn_btn);
        Button editar_ci_vn_btn = mainView.findViewById(R.id.editar_ci_vn_btn);
        Button ed_guardar_ci_vn_btn = mainView.findViewById(R.id.ed_guardar_ci_vn_btn);
        Button ed_descartar_ci_vn_btn = mainView.findViewById(R.id.ed_descartar_ci_vn_btn);

        TextView ed_cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.ed_cedula_vendedor_ci_vn_txt);

        ir_aniadir_ci_vn_btn.setOnClickListener(v -> {
            ir_aniadir_ci_vn_btn.setVisibility(View.GONE);
            citas_vendedor_lyt.setVisibility(View.GONE);
            editar_ci_vn_lyt.setVisibility(View.GONE);
            ver_ci_vn_lyt.setVisibility(View.GONE);
            aniadir_ci_vn_lyt.setVisibility(View.VISIBLE);
            adaptador();
        });

        guardar_ci_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("AÑADIR");
            msg.setMessage("¿Está seguro de añadir la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    registarCita();
                    irListaCitas();
                } catch (Exception e) {
                    citas_vendedor_lyt.setVisibility(View.VISIBLE);
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

        ed_guardar_ci_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("GUARDAR");
            msg.setMessage("¿Está seguro de guardar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(editarCita()){
                        irListaCitas();
                    }
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "No se pudo actualizar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
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
                e.printStackTrace();
            }
        }
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainView;
    }

    public void adaptador(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_vn_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.cedula_vendedor_ci_vn_etxt);
        cedula_vendedor_ci_vn_txt.setText(usuarioActual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_vn_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void irListaCitas(){
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ver_ci_vn_lyt.setVisibility(View.GONE);
        aniadir_ci_vn_lyt.setVisibility(View.GONE);
        ir_aniadir_ci_vn_btn.setVisibility(View.VISIBLE);
        citas_vendedor_lyt.setVisibility(View.VISIBLE);
    }

    public void cargar() throws Exception {
        RecyclerView listaview = mainView.findViewById(R.id.rc_citas_vendedor);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        Adaptador_Lista_Citas adptadorlistaview = new Adaptador_Lista_Citas(usuarioActual.obtenerCitas(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    public void visualizarEditable(){
        ImageView imagen_ed = mainView.findViewById(R.id.ed_carro_ci_vn_img);
        TextView dia_ed = mainView.findViewById(R.id.ed_dia_ci_vn_etxt);
        TextView mes_ed = mainView.findViewById(R.id.ed_mes_ci_vn_etxt);
        TextView anio_ed = mainView.findViewById(R.id.ed_anio_ci_vn_etxt);
        TextView hora_ed = mainView.findViewById(R.id.ed_hora_ci_vn_etxt);
        TextView cliente_ed = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_etxt);
        TextView vendedor_ed = mainView.findViewById(R.id.ed_cedula_vendedor_ci_vn_txt);
        TextView vehiculo_ed = mainView.findViewById(R.id.ed_placa_ci_vn_etxt);
        TextView resolucion_ed = mainView.findViewById(R.id.ed_resolucion_ci_vn_etxt);

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
        if(!resolucion_str.equals(" ")){
            resolucion.setVisibility(View.VISIBLE);
            resolucion.setText(resolucion_str);
        }else{
            resolucion.setVisibility(View.GONE);
        }
        precio.setText("$" + cita_mostrar.getVehiculo().getPrecioVenta());


    }

    public boolean editarCita() throws Exception {
        Toast.makeText(mainView.getContext(), "10", Toast.LENGTH_SHORT).show();
        EditText fechacitadia = mainView.findViewById(R.id.ed_dia_ci_vn_etxt);
        EditText fechacitames = mainView.findViewById(R.id.ed_mes_ci_vn_etxt);
        EditText fechacitaanio = mainView.findViewById(R.id.ed_anio_ci_vn_etxt);
        EditText fechacitahora = mainView.findViewById(R.id.ed_hora_ci_vn_etxt);
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

        EditText cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_vn_etxt);
        EditText auto = mainView.findViewById(R.id.ed_placa_ci_vn_etxt);

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
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora*", Toast.LENGTH_SHORT).show();
            c++;
        }

        Date fecha = Patioventainterfaz.sdf.parse(dia + "-" + mes + "-" + anio);
        if((hora < 7 || hora > 24) || !usuarioActual.disponible(fecha, hora)){
            Toast.makeText(mainView.getContext(), "El vendedor no está disponible", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText resolucion = mainView.findViewById(R.id.ed_resolucion_ci_vn_etxt);
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
                Toast.makeText(mainView.getContext(), "Se agrego correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void registarCita() throws Exception {
        Toast.makeText(mainView.getContext(), "10", Toast.LENGTH_SHORT).show();
        EditText fechacitadia = mainView.findViewById(R.id.dia_ci_vn_etxt);
        EditText fechacitames = mainView.findViewById(R.id.mes_ci_vn_etxt);
        EditText fechacitaanio = mainView.findViewById(R.id.anio_ci_vn_etxt);
        EditText fechacitahora = mainView.findViewById(R.id.hora_ci_vn_etxt);
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

        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_vn_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_vn_actv);

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
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora*", Toast.LENGTH_SHORT).show();
            c++;
        }

        Date fecha = Patioventainterfaz.sdf.parse(dia + "-" + mes + "-" + anio);
        if((hora < 7 || hora > 24) || !usuarioActual.disponible(fecha, hora)){
            Toast.makeText(mainView.getContext(), "El vendedor no está disponible", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText resolucion = mainView.findViewById(R.id.resolucion_ci_vn_etxt);
        String resolucion_str = resolucion.getText().toString();

        if (c == 0) {
            Cita nueva = new Cita(
                    fecha,
                    hora,
                    resolucion_str,
                    cliente_c,
                    usuarioActual,
                    vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se agrego correctamente la cita", Toast.LENGTH_SHORT).show();
            }
        }
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
            cita_mostrar = patio.buscarCitas("Vehiculo",placa,cedula_cliente);
            irVer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
