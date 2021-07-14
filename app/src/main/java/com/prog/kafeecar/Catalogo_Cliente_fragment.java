package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.sdf;
import static java.lang.String.format;

public class Catalogo_Cliente_fragment extends Fragment implements Adaptador_Lista_Catalogo_Cl.RecyclerItemClick, SearchView.OnQueryTextListener {

    private View mainView;
    private PatioVenta patio;
    private Vehiculo vMostrar;
    private Cita cita_mostrar;
    private final Cliente clienteActual = (Cliente) Patioventainterfaz.usuarioActual;
    private LinearLayout irCitaNueva;
    private LinearLayout verCatalogo;
    private ScrollView vistaVehiculo;

    private Button favoritoBoton;

    private SearchView busqueda_placa;
    private Drawable estrelladorada;
    private Adaptador_Lista_Catalogo_Cl adptadorlistaview;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = inflater.inflate(R.layout.catalogo_cliente, container, false);
        busqueda_placa = mainView.findViewById(R.id.busqueda_placa_cl_bar);
        patio = Patioventainterfaz.patioventa;
        //Botones
        favoritoBoton = mainView.findViewById(R.id.aniadir_favorito_btn);
        Button agendarcita = mainView.findViewById(R.id.agendarcita_cliente_btn);
        Button regresarVistaVehiculo = mainView.findViewById(R.id.regresar_VV_cliente_btn);
        Button descartarcita = mainView.findViewById(R.id.descartar_ci_cli_vv_btn);
        Button guardarcita = mainView.findViewById(R.id.guardar_ci_cli_vv_btn);
        //Recursos
        estrelladorada = favoritoBoton.getBackground();
        //Layouts
        verCatalogo = mainView.findViewById(R.id.catalogoautos_cliente_lyt);
        vistaVehiculo = mainView.findViewById(R.id.vista_vehiculo_VV_scl);
        irCitaNueva = mainView.findViewById(R.id.add_cita_cli_vv_lyt);

        //Edit Text necesarios

        regresarVistaVehiculo.setOnClickListener(v -> {
            try {
                vistaVehiculo.setVisibility(View.GONE);
                verCatalogo.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        descartarcita.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("DESCARTAR");
            msg.setMessage("¿Está seguro de salir sin guardar?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    patio.removerCita(cita_mostrar.getVehiculo().getPlaca(), cita_mostrar.getCliente().getCedula());
                    visualizarVehiculo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        favoritoBoton.setOnClickListener(v -> modificarFavorito());

        agendarcita.setOnClickListener(v -> {
            try {
                vistaVehiculo.setVisibility(View.GONE);
                irCitaNueva.setVisibility(View.VISIBLE);
                verAgendarCita();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        guardarcita.setOnClickListener(v -> {
            try {
                if(registarCita()){
                    irCatalogo();
                    Toast.makeText(mainView.getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
            }
        });
        //Metodo para el control del boton atras
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (vistaVehiculo.getVisibility() == View.VISIBLE) {
                    irCatalogo();
                }
            }
        };

        AutoCompleteTextView filtros = mainView.findViewById(R.id.v_filtros_CC_ddm);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.filtros_vehiculos);
        filtros.setAdapter(adapt);
        filtros.setOnItemClickListener((parent, view, position, id) -> busqueda_placa.setQueryHint(adapt.getItem(position)));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        busqueda_placa.setOnQueryTextListener(this);
        cargar();


        return mainView;
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.lista_autos_cl_Rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo_Cl(patio.getVehiculos(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    /*public void irVer() {

        visualizarVehiculo();
    }*/

    public void irCatalogo() {
        cargar();
        vistaVehiculo.setVisibility(View.GONE);
        irCitaNueva.setVisibility(View.GONE);
        //Activar el diseño deseado
        verCatalogo.setVisibility(View.VISIBLE);

    }


    /*@SuppressLint("DefaultLocale")
    public void verLista(String placa, String placa1) throws Exception {
    }*/


    public void modificarFavorito() {

        if (clienteActual.esFavorito(vMostrar.getPlaca())) {
            clienteActual.getFavoritos().eliminar(vMostrar.getPlaca());
            favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);
        } else {
            clienteActual.getFavoritos().add(vMostrar.getPlaca());
            favoritoBoton.setBackground(estrelladorada);
        }
    }


    public void visualizarVehiculo() {
        verCatalogo.setVisibility(View.GONE);
        irCitaNueva.setVisibility(View.GONE);
        vistaVehiculo.setVisibility(View.VISIBLE);

        ImageView v_img = mainView.findViewById(R.id.foto_auto_imageView);
        TextView titulo = mainView.findViewById(R.id.titulo_auto_txt);
        TextView placa = mainView.findViewById(R.id.placa_cliente_txt);
        TextView precio = mainView.findViewById(R.id.precio_auto_txt);
        TextView matricula = mainView.findViewById(R.id.matricula_cliente_txt);
        TextView anio = mainView.findViewById(R.id.vehiculo_anio_cliente_txt);
        TextView marca = mainView.findViewById(R.id.vehiculo_marca_cliente_txt);
        TextView modelo = mainView.findViewById(R.id.vehiculo_modelo_cliente_txt);
        TextView color = mainView.findViewById(R.id.vehiculo_color_cliente_txt);
        TextView descripcion = mainView.findViewById(R.id.vehiculo_descripcion_cliente_txt);
        TextView preciVenta = mainView.findViewById(R.id.vehiculo_pventa_cliente_txt);
        TextView promocion = mainView.findViewById(R.id.vehiculo_promocion_cliente_txt);
        TextView matriculado = mainView.findViewById(R.id.vehiculo_matriculado_cliente_txt);

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
        if (clienteActual.esFavorito(vMostrar.getPlaca())) {
            favoritoBoton.setBackground(estrelladorada);
        } else {
            favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);

        }
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
            matriculado.setText(" Si");
        } else {
            matriculado.setText(" No");
        }



    }
    public void verAgendarCita(){
        verCatalogo.setVisibility(View.GONE);
        vistaVehiculo.setVisibility(View.GONE);
        irCitaNueva.setVisibility(View.VISIBLE);
        ImageView vehiculo_img = mainView.findViewById(R.id.auto_cita_cli_vv_img);
        TextView cedula = mainView.findViewById(R.id.cedula_ci_cli_vv_etxt);
        AutoCompleteTextView placavehiculo = mainView.findViewById(R.id.placa_ci_cli_vv_actv);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_ci_cli_vv_actv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_ci_cli_vv_actv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_cita_cli_vv_actv);
        AutoCompleteTextView horas = mainView.findViewById(R.id.hora_ci_cli_vv_actv);

        String fecha = Patioventainterfaz.getFechaMod(cita_mostrar.getFechaCita());
        String dia_str = fecha.split("-")[0];
        String mes_str = fecha.split("-")[1];
        String anio_str = fecha.split("-")[2];



        StorageReference filePath = mStorageRef.child("Vehiculos/" + cita_mostrar.getVehiculo().getimagen());
        try {
            final File localFile = File.createTempFile(cita_mostrar.getVehiculo().getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                vehiculo_img.setImageURI(nuevo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        horas.setText(String.valueOf(cita_mostrar.getHora()));
        cedula.setText(cita_mostrar.getCliente().getCedula());
        placavehiculo.setText(cita_mostrar.getVehiculo().getPlaca());

    }
    public boolean registarCita() throws Exception {
        Cliente cliente_c = clienteActual;
        Vehiculo vehiculo = vMostrar;
        int c = 0;

        /*String prueba = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
        Date fecha = sdf.parse(prueba);

        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_ad_actv);
        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_ad_actv);

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
                    cliente_c,
                    vehiculo);
            patio.aniadirCita(nueva);
            if (patio.getCitas().contiene(nueva)) {
                Toast.makeText(mainView.getContext(), "Se edito correctamente la cita", Toast.LENGTH_SHORT).show();
                return true;
            }
        }*/
        return false;
    }

    /*public void adaptadorAniadir(){
        TextView cedulaCliente = mainView.findViewById(R.id.cedula_ci_cli_vv_etxt);
        cedulaCliente.setText(clienteActual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_cli_vv_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }*/

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        try {
            adptadorlistaview.buscar(newText,busqueda_placa.getQueryHint().toString());
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return false;
        /* newText = newText.toUpperCase();
        try {
            adptadorlistaview.buscar(newText);
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return false;*/
    }

    @Override
    public void itemClick(String placa) {
        //irVer(placa);
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            //irVer();
            visualizarVehiculo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
