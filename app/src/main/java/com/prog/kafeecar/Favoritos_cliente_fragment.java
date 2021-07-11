package com.prog.kafeecar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class Favoritos_cliente_fragment extends Fragment implements Adaptador_Lista_Catalogo.RecyclerItemClick, SearchView.OnQueryTextListener {

    private View mainView;
    //private View mainView2;
    private PatioVenta patio;
    private Vehiculo vMostrar;
    private LinearLayout verCatalogofav;
    private ScrollView vistaVehiculo;
    private final Cliente clienteActual = (Cliente) Patioventainterfaz.usuarioActual;
    private Adaptador_Lista_Catalogo adptadorlistaview;
    private Button favoritoBoton;
    //private Drawable estrelladorada;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.favoritos_cliente, container, false);

        SearchView busqueda_placa = mainView.findViewById(R.id.busqueda_placa_fav_bar);
        patio = Patioventainterfaz.patioventa;
        vistaVehiculo = mainView.findViewById(R.id.vista_vehiculo_VVF_scl);
        verCatalogofav = mainView.findViewById(R.id.favorito_cliente_lyt);
        favoritoBoton = mainView.findViewById(R.id.aniadir_favorito_F_btn);
        //estrelladorada = favoritoBoton.getBackground();

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

            }
        };
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

    public void irVer() {

        visualizarVehiculoF();
    }

    public void cargar() throws Exception {
        RecyclerView listaview = mainView.findViewById(R.id.listafavoritos_Rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.buscarVehiculosFav("Placa", clienteActual.getFavoritos()), this);
        listaview.setAdapter(adptadorlistaview);
    }
    public void irCatalogo() throws Exception {
        vistaVehiculo.setVisibility(View.GONE);
        //Activar el diseño deseado
        mainView.setVisibility(View.VISIBLE);
        cargar();
    }
    public void modificarFavorito() {

        //if (clienteActual.esFavorito(vMostrar.getPlaca())) {

        /*} else {
            clienteActual.getFavoritos().add(vMostrar.getPlaca());
            favoritoBoton.setBackground(estrelladorada);
        }*/
    }
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
        anio.setText(vMostrar.getAnio());
        marca.setText( vMostrar.getMarca());
        modelo.setText( vMostrar.getModelo());
        descripcion.setText( vMostrar.getDescripcion());
        color.setText(vMostrar.getColor());
        preciVenta.setText(format("$%.2f", vMostrar.getPrecioVenta()));
        promocion.setText(format("$%.2f", vMostrar.getPromocion()));
        /*if (clienteActual.esFavorito(vMostrar.getPlaca())) {
            favoritoBoton.setBackground(estrelladorada);
        } else {
            favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);
        }*/
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


    @Override
    public void itemClick(String placa) {
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            //clienteF.visualizarVehiculo();
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
    // @Override
    //public void itemClick(String placa) {
    //   int i=0;
    //}
}






