package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.Date;

public class Citas_vendedor_fragment extends Fragment implements Adaptador_Lista_Citas_Admin.RecyclerItemClick {

    private View mainView;
    private PatioVenta patio;
    Cita cita_mostrar;

    public EditText placa_ci_vn_etxt;

    private LinearLayout aniadir_ci_vn_lyt;
    private LinearLayout citas_vn_lyt;
    private LinearLayout ver_ci_vn_lyt;

    private final Vendedor usuarioActual = (Vendedor) Patioventainterfaz.usuarioActual;

    private FloatingActionButton ir_aniadir_ci_vn_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;

        placa_ci_vn_etxt = mainView.findViewById(R.id.placa_ci_vn_etxt);
        ver_ci_vn_lyt = mainView.findViewById(R.id.ver_ci_vn_lyt);

        //citas_vn_lyt = mainView.findViewById(R.id.citas_vn_lyt);
        aniadir_ci_vn_lyt = mainView.findViewById(R.id.aniadir_ci_vn_lyt);

        ir_aniadir_ci_vn_btn = mainView.findViewById(R.id.ir_aniadir_ci_vn_btn);
        Button guardar_ci_vn_btn = mainView.findViewById(R.id.guardar_ci_vn_btn);
        Button anular_ci_vn_btn = mainView.findViewById(R.id.anular_ci_vn_btn);

        ir_aniadir_ci_vn_btn.setOnClickListener(v -> {
            ir_aniadir_ci_vn_btn.setVisibility(View.GONE);
            aniadir_ci_vn_lyt.setVisibility(View.VISIBLE);
        });

        guardar_ci_vn_btn.setOnClickListener(v -> {
            try {
                registarCita();
                Toast.makeText(mainView.getContext(), "Cita Registrada", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "No se pudo registrar la cita", Toast.LENGTH_SHORT).show();
                //citas_vn_lyt.setVisibility(View.VISIBLE);
            }
        });

        anular_ci_vn_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("ANULAR");
            msg.setMessage("¿Está seguro de anular la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> irVer());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        if (Patioventainterfaz.CITA_CON_VEHICULO) {
            ir_aniadir_ci_vn_btn.callOnClick();
            placa_ci_vn_etxt.setText(Patioventainterfaz.v_aux_cita.getPlaca());
        } else {
            try {
                //verLista("GHC-2434","IMH-2233");
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

    public void cargar() throws Exception {
        RecyclerView listaview = mainView.findViewById(R.id.rc_citas_vendedor);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        Adaptador_Lista_Citas_Admin adptadorlistaview = new Adaptador_Lista_Citas_Admin(usuarioActual.obtenerCitas(), this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void visualizarCita() {
        TextView fecha = mainView.findViewById(R.id.fechaCita_txt);
        TextView hora = mainView.findViewById(R.id.horaCita_txt);
        TextView cliente = mainView.findViewById(R.id.clienteCita_txt);
        TextView contacto = mainView.findViewById(R.id.contactoCita_txt);
        TextView vendedor = mainView.findViewById(R.id.vendedorCita_txt);
        TextView vehiculo = mainView.findViewById(R.id.vehiculoCita_txt);
        TextView descripcion = mainView.findViewById(R.id.descripcionCita_txt);
        TextView resolucion = mainView.findViewById(R.id.resolucionCita_txt);
        TextView precio = mainView.findViewById(R.id.precioVentaCita_txt);

        fecha.setText(Patioventainterfaz.getFechaMod(cita_mostrar.getFechaCita()));
        hora.setText(String.format("%d:00 %s", cita_mostrar.getHora(), Patioventainterfaz.formatoHora(cita_mostrar.getHora())));
        cliente.setText(cita_mostrar.getVisitante().getNombre());
        contacto.setText(cita_mostrar.getVisitante().getTelefono());
        vendedor.setText(cita_mostrar.getVendedorCita().getNombre());
        vehiculo.setText(cita_mostrar.getVehiculo().getModelo());
        descripcion.setText(cita_mostrar.getVehiculo().getDescripcion());
        resolucion.setText(cita_mostrar.getResolucion());
        precio.setText(cita_mostrar.getVehiculo().getPrecioVenta() + " $" );
    }

    public void registarCita() throws Exception {
        EditText fechacitadia = mainView.findViewById(R.id.dia_ci_vn_etxt);
        EditText fechacitames = mainView.findViewById(R.id.mes_ci_vn_etxt);
        EditText fechacitaanio = mainView.findViewById(R.id.anio_ci_vn_etxt);
        EditText fechacitahora = mainView.findViewById(R.id.hora_ci_vn_etxt);
        Vendedor vendedor_v = null;
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

        EditText cliente = mainView.findViewById(R.id.cedula_cliente_ci_cn_etxt);
        EditText vendedor = mainView.findViewById(R.id.cedula_vendedor_ci_vn_etxt);
        EditText auto = mainView.findViewById(R.id.placa_ci_vn_etxt);

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

        if (!isEmpty(vendedor)) {
            String vendedor_str = cliente.getText().toString();
            if (vendedor_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cliente.setText("");
                c++;
            }
            vendedor_v = patio.buscarVendedores("Cedula", vendedor_str);
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Cédula Vendedor*", Toast.LENGTH_SHORT).show();
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
            if (hora < 7 || hora > 17 || hora == vendedor_v.getHoraComida()) {
                Toast.makeText(mainView.getContext(), "Hora inválida", Toast.LENGTH_SHORT).show();
                fechacitahora.setText("");
                c++;
            }
        } else {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora*", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText resolucion = mainView.findViewById(R.id.resolucion_ci_vn_etxt);
        String resolucion_str = resolucion.getText().toString();

        if (c == 0) {
            Date fecha = null;
            try {
                fecha = Patioventainterfaz.sdf.parse(dia + "-" + mes + "-" + anio);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Cita nueva = new Cita(
                    fecha,
                    hora,
                    resolucion_str,
                    cliente_c,
                    vendedor_v,
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

    public void irAniadirCita(String placa) {
        if (!placa.equals("")) {
            try {
                Vehiculo v = patio.buscarVehiculos("Placa", placa);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void irVer() {
        //verCatalogo.setVisibility(View.GONE);
        ir_aniadir_ci_vn_btn.setVisibility(View.GONE);
        aniadir_ci_vn_lyt.setVisibility(View.GONE);
        ver_ci_vn_lyt.setVisibility(View.VISIBLE);
        visualizarCita();
    }

    @Override
    public void itemClick(String placa) {
        irAniadirCita("PSD-1234");
    }
}
