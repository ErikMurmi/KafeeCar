package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.patioventa;
import static java.lang.String.format;

public class Favoritos_cliente_fragment extends Fragment implements Adaptador_Lista_Catalogo.RecyclerItemClick, SearchView.OnQueryTextListener {

    private View mainView;
    private PatioVenta patio;
    private Vehiculo vMostrar;
    private LinearLayout verCatalogofav;
    private LinearLayout agenCitaNueva;
    private ScrollView vistaVehiculo;
    private final Cliente clienteActual = (Cliente) Patioventainterfaz.usuarioActual;
    private Adaptador_Lista_Catalogo adptadorlistaview;
    private Button favoritoBoton;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private final String[] horascita =new String[]{"8","9","10","11","12","13","14","15","16"};
    private final SimpleDateFormat sdf = Patioventainterfaz.sdf;

    private int posicion_dia=-1;
    private int posicion_mes=-1;
    private int posicion_anio=-1;
    private int hora_nueva_cita=-1;

    private boolean horas_mostradas = false;
    private boolean mes_mostrados = false;
    private boolean anios_mostradas = false;
    public boolean dias_mostrados = false;

    String fecha_nueva_cita;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.favoritos_cliente, container, false);

        SearchView busqueda_placa = mainView.findViewById(R.id.busqueda_placa_fav_bar);
        patio = Patioventainterfaz.patioventa;
        vistaVehiculo = mainView.findViewById(R.id.vista_vehiculo_VVF_scl);
        verCatalogofav = mainView.findViewById(R.id.favorito_cliente_lyt);
        agenCitaNueva = mainView.findViewById(R.id.add_cita_fav_cli_vv_lyt);
        favoritoBoton = mainView.findViewById(R.id.aniadir_favorito_F_btn);
        Button agendarcita = mainView.findViewById(R.id.guardar_ci_fav_cli_btn);
        Button descartarcita = mainView.findViewById(R.id.descartar_ci_fav_cli_btn);
        Button iragendar = mainView.findViewById(R.id.agendarcita_cliente_F_btn);
        Button regresarFavorito = mainView.findViewById(R.id.regresar_VVF_cliente_btn);
        listasDesplegableAniadir();

        //Metodo para el control del boton atras
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (vistaVehiculo.getVisibility() == View.VISIBLE) {
                    try {
                        irCatalogo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (agenCitaNueva.getVisibility() == View.VISIBLE){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("Guardar");
                    msg.setMessage("¿Está seguro de cancelar el registro de la cita?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            vistaVehiculo.setVisibility(View.VISIBLE);
                            visualizarVehiculoF();
                            verCatalogofav.setVisibility(View.GONE);
                            agenCitaNueva.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
            }
        };

        descartarcita.setOnClickListener(v ->{
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Guardar");
            msg.setMessage("¿Está seguro de cancelar el registro de la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                        vistaVehiculo.setVisibility(View.VISIBLE);
                        visualizarVehiculoF();
                        verCatalogofav.setVisibility(View.GONE);
                        agenCitaNueva.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        iragendar.setOnClickListener(v ->{
            try {
                listasDesplegableAniadir();
                vistaVehiculo.setVisibility(View.GONE);
                verCatalogofav.setVisibility(View.GONE);
                agenCitaNueva.setVisibility(View.VISIBLE);
                mostraragendamiento();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        regresarFavorito.setOnClickListener(v -> {
            try {
                vistaVehiculo.setVisibility(View.GONE);
                verCatalogofav.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        agendarcita.setOnClickListener(v ->{
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Guardar");
            msg.setMessage("¿Está seguro de registrar la cita?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(registarCita()){
                        vistaVehiculo.setVisibility(View.GONE);
                        verCatalogofav.setVisibility(View.VISIBLE);
                        agenCitaNueva.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        favoritoBoton.setOnClickListener(v ->{
            clienteActual.getFavoritos().eliminar(vMostrar.getPlaca());
            favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        busqueda_placa.setOnQueryTextListener(this);
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainView;
    }

    public void cargar() throws Exception {
        RecyclerView listaview = mainView.findViewById(R.id.listafavoritos_Rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.buscarVehiculosFav("Placa", clienteActual.getFavoritos()).copiar(), this);
        listaview.setAdapter(adptadorlistaview);
    }
    public void irCatalogo() throws Exception {
        vistaVehiculo.setVisibility(View.GONE);
        agenCitaNueva.setVisibility(View.GONE);
        //Activar el diseño deseado
        verCatalogofav.setVisibility(View.VISIBLE);
        cargar();
    }
    public void irVistaVehiculo() {
        try {
            vistaVehiculo.setVisibility(View.VISIBLE);
            visualizarVehiculoF();
            verCatalogofav.setVisibility(View.GONE);
            agenCitaNueva.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void mostraragendamiento(){
        TextView cedulaclien = mainView.findViewById(R.id.cedula_cliente_fav_ci_cli_actv);
        TextView palcaVe = mainView.findViewById(R.id.placa_ci_fav_cli_actv);
        cedulaclien.setText(Patioventainterfaz.usuarioActual.getCedula());
        palcaVe.setText(vMostrar.getMatricula());
    }

    public boolean registarCita() throws Exception {
        Cliente clien = (Cliente) Patioventainterfaz.usuarioActual;
        Vendedor vendedor;
        int c = 0;
        String prueba = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);
        String hora = String.valueOf(hora_nueva_cita);
        vendedor = patioventa.asignarVendedor(hora,fecha);
        AutoCompleteTextView anio_cita = mainView.findViewById(R.id.anio_ci_fav_cli_acv);
        AutoCompleteTextView mes_cita = mainView.findViewById(R.id.mes_ci_fav_cli_acv);
        AutoCompleteTextView dia_cita = mainView.findViewById(R.id.dia_ci_fav_cli_acv);
        AutoCompleteTextView hora_cita = mainView.findViewById(R.id.hora_ci_fav_cli_acv);

        if(isEmpty(hora_cita)){
            c++;
        }
        if(isEmpty(dia_cita)){
            c++;
        }
        if(isEmpty(mes_cita)){
            c++;
        }
        if(isEmpty(anio_cita)){
            c++;
        }
        if (c == 0) {
            Cita nueva = new Cita(
                    fecha,
                    hora_nueva_cita,
                    "sin especificar",
                    clien,
                    vendedor,
                    vMostrar);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se registro correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        else
        {
            Toast.makeText(mainView.getContext(), "Campos Vacios", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void visualizarVehiculoF() {
        ImageView v_img = mainView.findViewById(R.id.foto_auto_F_imageView);
        TextView titulo = mainView.findViewById(R.id.titulo_auto_F_txt);
        TextView placa = mainView.findViewById(R.id.placa_cliente_F_txt);
        TextView precio = mainView.findViewById(R.id.precio_auto_F_txt);
        TextView matricula = mainView.findViewById(R.id.matricula_cliente_F_txt);
        TextView anio = mainView.findViewById(R.id.vehiculo_anio_cliente_F_txt);
        TextView marca = mainView.findViewById(R.id.vehiculo_marca_cliente_F_txt);
        TextView modelo = mainView.findViewById(R.id.vehiculo_modelo_cliente_F_txt);
        TextView color = mainView.findViewById(R.id.vehiculo_color_cliente_F_txt);
        TextView descripcion = mainView.findViewById(R.id.vehiculo_descripcion_cliente_F_txt);
        TextView preciVenta = mainView.findViewById(R.id.vehiculo_pventa_cliente_F_txt);
        TextView promocion = mainView.findViewById(R.id.vehiculo_promocion_cliente_F_txt);
        TextView matriculado = mainView.findViewById(R.id.vehiculo_matriculado_cliente_F_txt);

        String titulo_str = vMostrar.getMarca() + " " + vMostrar.getModelo();//ojo
        String precioTitulo = "$" + vMostrar.getPrecioVenta();
        precio.setText(precioTitulo);
        titulo.setText(titulo_str);
        placa.setText(vMostrar.getPlaca());
        matricula.setText(format(getString(R.string.matricula_frmt), vMostrar.getMatricula()));
        anio.setText(format("%s", vMostrar.getAnio()));
        marca.setText(format("%s", vMostrar.getMarca()));
        modelo.setText(format("%s", vMostrar.getModelo()));
        descripcion.setText(format("%s", vMostrar.getDescripcion()));
        color.setText(format("%s", vMostrar.getColor()));
        preciVenta.setText(format("$ %.2f", vMostrar.getPrecioVenta()));
        promocion.setText(format("$ %.2f", vMostrar.getPromocion()));
        //Cargar imagen
        StorageReference filePath = mStorageRef.child("Vehiculos/" + vMostrar.getimagen());
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                v_img.setImageBitmap(bitmap);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (vMostrar.isMatriculado()) {
            matriculado.setText("Si");
        } else {
            matriculado.setText("No");
        }
        verCatalogofav.setVisibility(View.GONE);
        vistaVehiculo.setVisibility(View.VISIBLE);

    }
    public void listasDesplegableAniadir(){
        TextInputLayout anio_lyt = mainView.findViewById(R.id.anio_ci_fav_cli_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_ci_fav_cli_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.dia_ci_fav_cli_til);
        TextInputLayout horas_lyt = mainView.findViewById(R.id.hora_ci_fav_cli_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_ci_fav_cli_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_ci_fav_cli_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_ci_fav_cli_acv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.hora_ci_fav_cli_acv);
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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa) {
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            visualizarVehiculoF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        try {
            adptadorlistaview.buscar(newText,"Placa");
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
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






