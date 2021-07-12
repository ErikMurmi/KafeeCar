package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

public class Catalogo_Cliente_fragment extends Fragment implements Adaptador_Lista_Catalogo_Cl.RecyclerItemClick, SearchView.OnQueryTextListener {

    //private static final int REQUEST_IMAGE_GALERY = 101;
    private final Cliente clienteActual = (Cliente) Patioventainterfaz.usuarioActual;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private String TAG = "Catalogo";
    private View mainView;
    private LinearLayout irCitaNueva;
    private LinearLayout verCatalogo;
    private ScrollView vistaVehiculo;
    private PatioVenta patio;
    private Vehiculo vMostrar;
    private Button favoritoBoton;
    private Drawable estrelladorada;
    private Citas_c_fragment crearCita;
    private Adaptador_Lista_Catalogo_Cl adptadorlistaview;
    private SearchView busqueda_placa;

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
        //Recursos
        estrelladorada = favoritoBoton.getBackground();
        //Layouts
        verCatalogo = mainView.findViewById(R.id.catalogoautos_cliente_lyt);
        vistaVehiculo = mainView.findViewById(R.id.vista_vehiculo_VV_scl);
        irCitaNueva = mainView.findViewById(R.id.nueva_cita_cliente_lay);
        //Edit Text necesarios

        regresarVistaVehiculo.setOnClickListener(v -> {
            try {
                vistaVehiculo.setVisibility(View.GONE);
                verCatalogo.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        favoritoBoton.setOnClickListener(v -> modificarFavorito());
        agendarcita.setOnClickListener(v -> {

            try {
                vistaVehiculo.setVisibility(View.GONE);
                irCitaNueva.setVisibility(View.VISIBLE);
                crearCita.visualizarcitaEditable();
            } catch (Exception e) {
                e.printStackTrace();
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

    public void irVer() {

        visualizarVehiculo();
    }

    public void irCatalogo() {
        vistaVehiculo.setVisibility(View.GONE);
        //Activar el diseño deseado
        verCatalogo.setVisibility(View.VISIBLE);
        cargar();
    }


    @SuppressLint("DefaultLocale")
    public void verLista(String placa, String placa1) throws Exception {
    }


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
            matriculado.setText("Matriculado: Si");
        } else {
            matriculado.setText("Matriculado: No");
        }
        verCatalogo.setVisibility(View.GONE);
        vistaVehiculo.setVisibility(View.VISIBLE);


    }

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
