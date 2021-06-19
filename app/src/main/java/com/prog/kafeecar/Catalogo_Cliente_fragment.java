package com.prog.kafeecar;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

public class Catalogo_Cliente_fragment extends Fragment {
    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Catalogo";
    private View mainView;
    private LinearLayout irVerVehiculo;
    private LinearLayout irVerVehiculo1;
    private LinearLayout verCatalogo;
    private LinearLayout vistaVehiculo;
    private PatioVenta patio;
    private Vehiculo m_vehiculo;
    TextView placa_v;
    TextView placa_v1;
    private Vehiculo vMostrar;
    private Uri foto;
    private Button favoritoBoton;
    private Button agendarcita;
    private Drawable estrelladorada;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_cliente, container, false);
        patio = Patioventainterfaz.patioventa;

        try {
            verLista("PSD-1234","GHC-2434");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        //Botones
        irVerVehiculo = mainView.findViewById(R.id.vehiculo1_lista_cliente_lyt);
        irVerVehiculo1 = mainView.findViewById(R.id.vehiculo2_lista_cliente_lyt);
        favoritoBoton=mainView.findViewById(R.id.aniadir_favorito_btn);
        agendarcita=mainView.findViewById(R.id.agendarcita_btn);
        //Recursos
        estrelladorada=favoritoBoton.getBackground();
        if(!esfavorito()){
            favoritoBoton.setBackgroundResource(R.drawable.favoritos_icono);
        }

        //Layouts
        verCatalogo = mainView.findViewById(R.id.vehiculos_cliente);
        vistaVehiculo=mainView.findViewById(R.id.vista_vehiculo_lay);
        irVerVehiculo = mainView.findViewById(R.id.vehiculo1_lista_cliente_lyt);
        irVerVehiculo1 = mainView.findViewById(R.id.vehiculo2_lista_cliente_lyt);
        //Edit Text necesarios
        placa_v = mainView.findViewById(R.id.vehiculo1_placa_lista_cliente_txt);
        placa_v1 = mainView.findViewById(R.id.vehiculo2_placa_lista_cliente_txt);


        irVerVehiculo.setOnClickListener(v -> {

            try {
                visualizarVehiculo("PSD-1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        irVerVehiculo1.setOnClickListener(v -> {

            try {
                visualizarVehiculo("GHC-2434");
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
                agendarcita.setVisibility(View.VISIBLE);
             aniadirCita();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });




        return mainView;
    }

    public void aniadirCita(){


    }
    public void irCatalogo(){


      vistaVehiculo.setVisibility(View.GONE);
        //irEditar.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verCatalogo.setVisibility(View.VISIBLE);

        try {
            verLista("PSD-1234","GHC-2434");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("DefaultLocale")
    public void verLista(String placa, String placa1) throws Exception {


        ImageView v_img = mainView.findViewById(R.id.vehiculo1_lista_cliente_img);
        ImageView v_img1 = mainView.findViewById(R.id.vehiculo2_lista_cliente_img);

        TextView titulo = mainView.findViewById(R.id.vehiculo1_marca_modelo_cliente_txt);
        TextView  anio = mainView.findViewById(R.id.vehiculo1_anio_lista_cliente_txt);
        TextView matricula =  mainView.findViewById(R.id.vehiculo1_matricula_lista_cliente_txt);
        TextView precio = mainView.findViewById(R.id.vehiculo1_precio_lista_cliente_txt);

        TextView titulo1 = mainView.findViewById(R.id.vehiculo2_marca_modelo_cliente_txt);
        TextView  anio1 = mainView.findViewById(R.id.vehiculo2_anio_lista_cliente_txt);
        TextView matricula1 =  mainView.findViewById(R.id.vehiculo2_matricula_lista_cliente_txt);
        TextView precio1 = mainView.findViewById(R.id.vehiculo2_precio_lista_cliente_txt);


        Vehiculo v_Mostrar = patio.buscarVehiculos("Placa",placa);
        Vehiculo v_Mostrar1 = patio.buscarVehiculos("Placa",placa1);
        StorageReference filePath = mStorageRef.child("Vehiculos/"+v_Mostrar.getimagen());
        //Glide.with(mainView)
        //      .load(filePath)
        //    .into(v_img);
        try {
            final File localFile = File.createTempFile(v_Mostrar.getimagen(),"jpg");
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
        //Imagen 2
        filePath = mStorageRef.child("Vehiculos/"+v_Mostrar1.getimagen());
        //Glide.with(mainView)
        //      .load(filePath)
        //    .into(v_img1);
        try {
            final File localFile = File.createTempFile(v_Mostrar1.getimagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    v_img1.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
        titulo.setText(new String(v_Mostrar.getMarca()+" "+ v_Mostrar.getModelo()));
        titulo1.setText(new String(v_Mostrar1.getMarca()+" "+ v_Mostrar1.getModelo()));
        anio.setText(String.valueOf(v_Mostrar.getAnio()));
        anio1.setText(String.valueOf(v_Mostrar1.getAnio()));
        matricula.setText(v_Mostrar.getMatricula());
        matricula1.setText(v_Mostrar1.getMatricula());
        placa_v.setText(v_Mostrar.getPlaca());
        placa_v1.setText(v_Mostrar1.getPlaca());
        precio.setText(String.format("$ %.2f",v_Mostrar.getPrecioVenta()));
        precio1.setText(String.format("$ %.2f",v_Mostrar1.getPrecioVenta()));
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


    public void visualizarVehiculo(String placa_buscar) throws Exception {

        m_vehiculo = patio.buscarVehiculos("Placa",placa_buscar);
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

        Vehiculo vMostrar  = null;
        try {
            vMostrar = patio.buscarVehiculos("Placa",placa_buscar);
            m_vehiculo = vMostrar;
        } catch (Exception e) {
            Toast t= Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG);
            t.show();
        }
        String titulo_str = vMostrar.getMarca()+" "+vMostrar.getModelo();
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
        irVehiculo();


    }

    public void irVehiculo(){

        vistaVehiculo.setVisibility(View.VISIBLE);
        //irEditar.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verCatalogo.setVisibility(View.GONE);
    }

    public void aniadirCitaCliente() throws Exception {
        Lista citanueva;
        Cliente clienten= (Cliente) Patioventainterfaz.usuarioActual;


        EditText diacita = mainView.findViewById (R.id.dia_cita_nueva_etxt);
        EditText mescita = mainView.findViewById(R.id.mes_cita_nueva_etxt);
        EditText aniocita = mainView.findViewById(R.id.anio_cita_nueva_etxt);
        EditText minutocita = mainView.findViewById(R.id.minutos_clita_nueva_etxt);
        EditText horacita = mainView.findViewById(R.id.hora_clita_nueva_etxt);
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
    }

}
