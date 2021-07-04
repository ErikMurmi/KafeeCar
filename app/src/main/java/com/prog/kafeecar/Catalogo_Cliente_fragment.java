package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;
public class Catalogo_Cliente_fragment extends Fragment implements Adaptador_Lista_Catalogo_Cl.RecyclerItemClick, SearchView.OnQueryTextListener {

    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Catalogo";
    private View mainView;
    private LinearLayout irCitaNueva;
    private ScrollView verCatalogo;
    private ScrollView vistaVehiculo;
    private PatioVenta patio;
    private Vehiculo m_vehiculo;
    private Vehiculo vMostrar;
    private Uri foto;
    private Button favoritoBoton;
    private Button agendarcita;
    private Button regresarVistaVehiculo;
    private Drawable estrelladorada;


    private Adaptador_Lista_Catalogo_Cl adptadorlistaview;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_cliente, container, false);
        SearchView busqueda_placa = mainView.findViewById(R.id.busqueda_placa_cl_bar);
        patio = Patioventainterfaz.patioventa;

        //
        //Botones
        favoritoBoton = mainView.findViewById(R.id.aniadir_favorito_btn);
        agendarcita = mainView.findViewById(R.id.agendarcita_cliente_btn);
        regresarVistaVehiculo = mainView.findViewById(R.id.regresar_VV_cliente_btn);
        //Recursos
        estrelladorada = favoritoBoton.getBackground();
        if (!esfavorito()) {
            favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);
        }

        //Layouts
        verCatalogo = mainView.findViewById(R.id.catalogoautos_cliente_scl);
        vistaVehiculo = mainView.findViewById(R.id.vista_vehiculo_VV_scl);

        irCitaNueva = mainView.findViewById(R.id.nueva_cita_cliente_lay);
        //Edit Text necesarios




        regresarVistaVehiculo.setOnClickListener(v -> {

            try {
                visualizarVehiculo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        favoritoBoton.setOnClickListener(v -> {
            modificarFavorito();
        });
        agendarcita.setOnClickListener(v -> {

            try {
                vistaVehiculo.setVisibility(View.GONE);
                irCitaNueva.setVisibility(View.VISIBLE);
             aniadirCita();
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
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        busqueda_placa.setOnQueryTextListener(this);
        //cargar();


        return mainView;
    }
    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.rc_autos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo_Cl(patio.getVehiculos(), this);
        listaview.setAdapter(adptadorlistaview);
    }
    public void irVer() throws Exception {


        verCatalogo.setVisibility(View.GONE);

        //Activar el diseño deseado
        vistaVehiculo.setVisibility(View.VISIBLE);
        visualizarVehiculo();
    }
    public void irCatalogo() {

        vistaVehiculo.setVisibility(View.GONE);

        //Activar el diseño deseado
        verCatalogo.setVisibility(View.VISIBLE);
        cargar();
    }

    public void aniadirCita(){


    }
   /* public void irCatalogo(){


      vistaVehiculo.setVisibility(View.GONE);
        //irEditar.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verCatalogo.setVisibility(View.VISIBLE);

        try {
            verLista("PSD-1234","GHC-2434");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    @SuppressLint("DefaultLocale")
    public void verLista(String placa, String placa1) throws Exception {
    }


    public void modificarFavorito(){
        Cliente clienteActual=(Cliente)Patioventainterfaz.usuarioActual;
        TextView matricula = mainView.findViewById(R.id.matricula_cliente_txt);
        if(esfavorito()){
           clienteActual.getFavoritos().eliminar(matricula.getText());
           favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);
        }else{
            clienteActual.getFavoritos().add(matricula.getText());
            favoritoBoton.setBackground(estrelladorada);
        }
    }
    public boolean esfavorito(){
        Cliente clienteActual=(Cliente)Patioventainterfaz.usuarioActual;
        TextView matricula = mainView.findViewById(R.id.matricula_cliente_txt);
        return clienteActual.getFavoritos().contiene(matricula.getText());
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

        String titulo_str = vMostrar.getMarca()+" "+m_vehiculo.getModelo();//ojo
        String precioTitulo = "$"+vMostrar.getPrecioVenta();
        precio.setText(precioTitulo);
        titulo.setText(titulo_str);
        placa.setText(format("Placa: %s", vMostrar.getPlaca()));
        matricula.setText(format(getString(R.string.matricula_frmt), vMostrar.getMatricula()));
        anio.setText(format("Año :%s",vMostrar.getAnio()));
        marca.setText(format("Marca :%s",vMostrar.getMarca()));
        modelo.setText(format("Modelo :%s",vMostrar.getModelo()));
        descripcion.setText(format("Descripción :%s",vMostrar.getDescripcion()));
        color.setText(format("Color :%s",vMostrar.getColor()));
        preciVenta.setText(format("Precio venta :%.2f",vMostrar.getPrecioVenta()));
        promocion.setText(format("Precio promoción:%.2f",vMostrar.getPromocion()));

        //Cargar imagen
        StorageReference filePath = mStorageRef.child("Vehiculos/"+vMostrar.getimagen());
        //Glide.with(mainView)
        // .load(filePath)
        // .into(v_img);
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    v_img.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
        //

        if(vMostrar.isMatriculado()){
            matriculado.setText("Matriculado: Si");
        }else{
            matriculado.setText("Matriculado: No");
        }
        verCatalogo.setVisibility(View.GONE);
        vistaVehiculo.setVisibility(View.VISIBLE);
        //irVehiculo();


    }

    public void irVehiculo(){

        vistaVehiculo.setVisibility(View.VISIBLE);
        //irEditar.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verCatalogo.setVisibility(View.GONE);
    }

    /*public void aniadirCitaCliente() throws Exception {
        Lista citanueva;
        Cliente clienten= (Cliente) Patioventainterfaz.usuarioActual;


        EditText diacita = mainView.findViewById (R.id.dia_cita_nueva_etxt);
        EditText mescita = mainView.findViewById(R.id.mes_cita_nueva_etxt);
        EditText aniocita = mainView.findViewById(R.id.anio_cita_nueva_etxt);
        EditText minutocita = mainView.findViewById(R.id.minutos_clita_nueva_etxt);
        EditText horacita = mainView.findViewById(R.id.hora_cita_nueva_cl_etxt);
        int cont=0;

        String fechacita_str = aniocita.getText().toString() + "-" + mescita.getText().toString() + "-" + diacita.getText().toString();
        int hora = Integer.parseInt(horacita.getText().toString());

        if(hora>24 || hora<0){
            Toast.makeText(mainView.getContext(), "Hora invalido", Toast.LENGTH_SHORT).show();
            cont++;
        }
        if(cont==0){
           // Cita nuevo=new Cita();//meter inof de la cita
          //  citanueva.add(nuevo);
        }
    }*/
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        try {
            adptadorlistaview.filtro(newText);
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void itemClick(String placa) {
        //irVer(placa);
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            irVer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
