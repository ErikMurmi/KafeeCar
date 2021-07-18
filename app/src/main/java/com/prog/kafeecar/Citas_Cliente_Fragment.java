package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.getFechaMod;
import static com.prog.kafeecar.Patioventainterfaz.patioventa;
import static com.prog.kafeecar.Patioventainterfaz.sdf;

public class Citas_Cliente_Fragment extends Fragment implements Adaptador_Lista_Cliente_Cita.RecyclerItemClick, SearchView.OnQueryTextListener {
    private View mainView;
    private final String[] horascita =new String[]{"8","9","10","11","12","13","14","15","16"};

    private FloatingActionButton irAniadirCita;
    private Cita cita_mostrar;

    private boolean horas_mostradas = false;
    private boolean mes_mostrados = false;
    private boolean anios_mostradas = false;
    public boolean dias_mostrados = false;

    private int posicion_dia=-1;
    private int posicion_mes=-1;
    private int posicion_anio=-1;
    private int hora_nueva_cita=-1;

    String fecha_nueva_cita;

    private LinearLayout aniadirCita;
    private LinearLayout verCitaLista;
    private LinearLayout verCita;
    private LinearLayout modificarCita;

    private PatioVenta patio;

    private SearchView busqueda_citas;
    private Adaptador_Lista_Cliente_Cita adptadorlistaview;

    private final SimpleDateFormat sdf = Patioventainterfaz.sdf;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private final Cliente cliente_actual = (Cliente) Patioventainterfaz.usuarioActual;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_cliente, container, false);
        patio = Patioventainterfaz.patioventa;
        verCitaLista = mainView.findViewById(R.id.citas_cliente_cc_lyt);
        aniadirCita = mainView.findViewById(R.id.add_cita_cli_lyt);
        verCita = mainView.findViewById(R.id.ver_ci_cli_lyt);
        modificarCita = mainView.findViewById(R.id.ed_cita_cli_lyt);
        busqueda_citas = mainView.findViewById(R.id.busqueda_c_cli_srv);

        irAniadirCita = mainView.findViewById(R.id.ir_aniadir_cita_cli_fbtn);
        //Botones
        Button descartarnuevacita = mainView.findViewById(R.id.descartar_ci_cli_btn);
        Button guardarcambios = mainView.findViewById(R.id.ed_guardar_ci_cli_btn);
        Button guardarcita = mainView.findViewById(R.id.guardar_ci_cli_btn);
        Button anular = mainView.findViewById(R.id.anular_ci_cli_btn);
        Button irVerEditable = mainView.findViewById(R.id.editar_ci_cli_btn);

        listasDesplegableAniadir();

        //SetONClicks
        guardarcita.setOnClickListener(v ->{
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Guardar");
            msg.setMessage("¿Está seguro de registrar la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(registarCita()){
                        irListaCitas();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        guardarcambios.setOnClickListener(v -> {
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

        descartarnuevacita.setOnClickListener(v ->{
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de salir sin guardar?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    irAniadirCita.setVisibility(View.VISIBLE);
                    verCita.setVisibility(View.GONE);
                    verCitaLista.setVisibility(View.VISIBLE);
                    aniadirCita.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();

        });

        irAniadirCita.setOnClickListener(v -> {
            listasDesplegableAniadir();
            adaptadorAniadir();
            irAniadirCita.setVisibility(View.GONE);
            TextView c = mainView.findViewById(R.id.cedula_cliente_ci_cli_actv);
            c.setText(Patioventainterfaz.usuarioActual.getCedula());
            verCita.setVisibility(View.GONE);
            verCitaLista.setVisibility(View.GONE);
            modificarCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.VISIBLE);
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

        irVerEditable.setOnClickListener(v -> {
            modificarCita.setVisibility(View.VISIBLE);
            verCitaLista.setVisibility(View.GONE);
            irAniadirCita.setVisibility(View.GONE);
            verCita.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.GONE);
            visualizarEditable();
        });

        busqueda_citas.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }

    public void irListaCitas(){
        modificarCita.setVisibility(View.GONE);
        cargar();
        verCitaLista.setVisibility(View.VISIBLE);
        irAniadirCita.setVisibility(View.VISIBLE);
        verCita.setVisibility(View.GONE);
        aniadirCita.setVisibility(View.GONE);
    }

    public void visualizarEditable(){

        adaptadorEditar();
        listasDesplegableEditar();
        ImageView imagen_ed = mainView.findViewById(R.id.ed_carro_ci_cli_img);
        TextView cliente = mainView.findViewById(R.id.ed_cedula_cliente_ci_cli_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_cli_actv);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_ci_cli_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_ci_cli_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_ci_cli_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.ed_hora_ci_cli_acv);
        TextView vendedor_ed = mainView.findViewById(R.id.ed_cedula_vendedor_ci_cli_etxt);
        TextView descripcion_ed = mainView.findViewById(R.id.ed_descripcion_ci_cli_etxt);

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
        vendedor_ed.setText(cita_mostrar.getVendedorCita().getCedula());
        auto.setText(cita_mostrar.getVehiculo().getPlaca());
        descripcion_ed.setText(cita_mostrar.getVehiculo().getDescripcion());
    }

    public boolean editarCita() throws Exception {
        Vehiculo vehiculo = null;
        int c = 0;


        String prueba = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);

        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_cli_actv);

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

        if (c == 0) {
            cita_mostrar.actualizar(
                    fecha,
                    hora_nueva_cita,
                    vehiculo,
                    cita_mostrar.getVendedorCita(),
                    cliente_actual
                    );
            if (patio.buscarCita("Cliente",cliente_actual.getCedula(),cliente_actual.getCedula())!=null) {
                Toast.makeText(mainView.getContext(), "Se edito la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void listasDesplegableEditar(){
        TextInputLayout anio_lyt = mainView.findViewById(R.id.ed_anio_ci_cli_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.ed_mes_ci_cli_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.ed_dia_ci_cli_til);
        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_cli_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.ed_anio_ci_cli_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.ed_mes_ci_cli_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.ed_dia_ci_cli_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.ed_hora_ci_cli_acv);
        anio_lyt.setEndIconOnClickListener(v -> anio.performClick());
        anio.setOnClickListener(v -> {
            if(anios_mostradas){
                anio.dismissDropDown();
                anios_mostradas = false;
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
                    fecha_nueva_cita = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,horascita);
                    horas.setAdapter(adapt);
                    horas.showDropDown();
                    horas.setOnItemClickListener((parent, view, position, id) -> setHora_nueva_cita(Integer.parseInt(adapt.getItem(position))));
                    horas_mostradas = true;
            }
        });
    }

    public void adaptadorAniadir(){
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_cli_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void listasDesplegableAniadir(){
        TextInputLayout anio_lyt = mainView.findViewById(R.id.anio_ci_cli_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_ci_cli_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.dia_ci_cli_til);
        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_cli_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_ci_cli_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_ci_cli_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_ci_cli_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.hora_ci_cli_acv);
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
                    fecha_nueva_cita = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,horascita);
                    horas.setAdapter(adapt);
                    horas.showDropDown();
                    horas.setOnItemClickListener((parent, view, position, id) -> setHora_nueva_cita(Integer.parseInt(adapt.getItem(position))));
                    horas_mostradas = true;
            }
        });
    }


    public void adaptadorEditar(){
        AutoCompleteTextView auto = mainView.findViewById(R.id.ed_placa_ci_cli_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void cargar()  {
        RecyclerView listaview = mainView.findViewById(R.id.listacitas_rv);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview=new Adaptador_Lista_Cliente_Cita( patio.buscarCitas(cliente_actual).copiar(),this);
        listaview.setAdapter(adptadorlistaview);
    }

    @SuppressLint("SetTextI18n")
    public void visualizarCita() {
        aniadirCita.setVisibility(View.GONE);
        verCitaLista.setVisibility(View.GONE);
        verCita.setVisibility(View.VISIBLE);

        ImageView imagen_ed = mainView.findViewById(R.id.ver_cita_im_cli_img);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_ci_cli_txt);
        TextView hora = mainView.findViewById(R.id.ver_hora_ci_cli_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_ci_cli_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_ci_cli_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_ci_cli_txt);
        TextView vehiculo = mainView.findViewById(R.id.ver_vehiculo_ci_cli_txt);
        TextView precio = mainView.findViewById(R.id.ver_precioVenta_ci_cli_txt);
        TextView descripcion = mainView.findViewById(R.id.ver_descripcion_ci_cli_txt);
        TextView resolucion = mainView.findViewById(R.id.ver_resolucion_ci_cli_txt);
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
        if(!resolucion_str.equals("")){
            resolucion.setVisibility(View.VISIBLE);
            resolucion.setText(resolucion_str);
        }else{
            resolucion.setVisibility(View.GONE);
        }
        irAniadirCita.setVisibility(View.GONE);
        precio.setText(" $" + cita_mostrar.getVehiculo().getPrecioVenta());
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public boolean registarCita() throws Exception {
        Cliente clien = (Cliente) Patioventainterfaz.usuarioActual;
        Vehiculo vehiculo = null;
        Vendedor vendedor;
        int c = 0;
        String prueba = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_cli_actv);
        AutoCompleteTextView dia_cita = mainView.findViewById(R.id.dia_ci_cli_acv);
        AutoCompleteTextView mes_cita = mainView.findViewById(R.id.mes_ci_cli_acv);
        AutoCompleteTextView anio_cita = mainView.findViewById(R.id.anio_ci_cli_acv);
        AutoCompleteTextView hora_cita = mainView.findViewById(R.id.hora_ci_cli_acv);
        if(isEmpty(hora_cita)){
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora*", Toast.LENGTH_SHORT).show();
            c++;
        }
        if(isEmpty(dia_cita)){
            Toast.makeText(mainView.getContext(), "Campo vacío: *Dia fecha*", Toast.LENGTH_SHORT).show();
            c++;
        }
        if(isEmpty(mes_cita)){
            Toast.makeText(mainView.getContext(), "Campo vacío: *Mes fecha*", Toast.LENGTH_SHORT).show();
            c++;
        }
        if(isEmpty(anio_cita)){
            Toast.makeText(mainView.getContext(), "Campo vacío: *Año fecha*", Toast.LENGTH_SHORT).show();
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
        String hora = String.valueOf(hora_nueva_cita);
        vendedor = patioventa.asignarVendedor(hora,fecha);
        if (c == 0) {
            Cita nueva = new Cita(
                    fecha,
                    hora_nueva_cita,
                    "sin especificar",
                    clien,
                    vendedor,
                    vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se registro correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
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