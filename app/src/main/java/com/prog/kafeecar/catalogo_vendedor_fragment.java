package com.prog.kafeecar;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;
public class catalogo_vendedor_fragment extends Fragment {

    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Catalogo";
    private View mainView;

    private ImageButton selec_vehiculo_img;
    private Button buscarPlacaVn_btn;


    private ScrollView verVehiculo;
    private ScrollView editar_vehiculo;

    //lyts de los vendedores en la lista
    private LinearLayout verCatalogo;
    private LinearLayout verVehiculoLista_lyt;
    private LinearLayout verVehiculoLista1_lyt;

    private PatioVenta patio;

    private Uri foto;

    private final StorageReference mStorageRef =FirebaseStorage.getInstance().getReference();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_admin, container, false);
        patio = Patioventainterfaz.patioventa;

        try {
            verLista("PSD-1234","GHC-2434");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Botones
        selec_vehiculo_img = mainView.findViewById(R.id.aniadir_vehiculo_imagen_btn);
        buscarPlacaVn_btn = mainView.findViewById(R.id.busqueda_placa_vn_btn);

        //Layouts
        verCatalogo = mainView.findViewById(R.id.vehiculos_catalogo_vendedor);
        //verVehiculo = mainView.findViewById(R.id.ver_vehiculo_catalogo_vendedor);
        verVehiculoLista_lyt = mainView.findViewById(R.id.vehiculo_lista_vendedor_lyt);
        verVehiculoLista1_lyt = mainView.findViewById(R.id.vehiculo_lista1_vendedor_lyt);

        verVehiculoLista_lyt.setOnClickListener(v -> {
            //Desactivar otros diseños
            verCatalogo.setVisibility(View.GONE);
            //Activar el diseño deseadow
            verVehiculo.setVisibility(View.VISIBLE);
            try {
                visualizarVehiculo("PSD-1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        verVehiculoLista1_lyt.setOnClickListener(v -> {
            //Desactivar otros diseños
            verCatalogo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseadow
            verVehiculo.setVisibility(View.VISIBLE);
            try {
                visualizarVehiculo("GHC-2434");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        selec_vehiculo_img.setOnClickListener(v -> {
            openGalery();
        });

        buscarPlacaVn_btn.setOnClickListener(view -> {

        });
;
        return mainView;
    }

    public void verLista(String placa, String placa1) throws Exception {

        ImageView v_img = mainView.findViewById(R.id.v_lista_vendedor_img);
        TextView titulo = mainView.findViewById(R.id.v_marca_modelo_vendedor_txt);
        TextView anio = mainView.findViewById(R.id.v_anio_lista_vendedor_txt);
        TextView matricula =  mainView.findViewById(R.id.v_matricula_lista_vendedor_txt);
        TextView precio = mainView.findViewById(R.id.v_precio_lista_vendedor_txt);
        TextView placa_v = mainView.findViewById(R.id.v_placa_lista_vendedor_txt);

        ImageView v_img1 = mainView.findViewById(R.id.v_lista1_vendedor_img);
        TextView titulo1 = mainView.findViewById(R.id.v_marca_modelo1_vendedor_txt);
        TextView  anio1 = mainView.findViewById(R.id.v_anio_lista1_vendedor_txt);
        TextView matricula1 =  mainView.findViewById(R.id.v_matricula_lista1_vendedor_txt);
        TextView precio1 = mainView.findViewById(R.id.v_precio_lista1_vendedor_txt);
        TextView placa_v1 = mainView.findViewById(R.id.v_placa_lista1_vendedor_txt);


        Vehiculo v_Mostrar = patio.buscarVehiculos("Placa",placa);
        titulo.setText(new String(v_Mostrar.getMarca()+" "+ v_Mostrar.getModelo()));
        anio.setText(String.valueOf(v_Mostrar.getAnio()));
        matricula.setText(v_Mostrar.getMatricula());
        placa_v.setText(placa);
        precio.setText(String.format("$ %.2f",v_Mostrar.getPrecioVenta()));


        Vehiculo v_Mostrar1 = patio.buscarVehiculos("Placa",placa1);
        titulo1.setText(new String(v_Mostrar1.getMarca()+" "+ v_Mostrar1.getModelo()));
        placa_v1.setText(v_Mostrar1.getPlaca());
        precio1.setText(String.format("$ %.2f",v_Mostrar1.getPrecioVenta()));
        anio1.setText(String.valueOf(v_Mostrar1.getAnio()));
        matricula1.setText(v_Mostrar1.getMatricula());

        StorageReference filePath = mStorageRef.child("Vehiculos/"+v_Mostrar.getimagen());
        Glide.with(mainView)
                .load(filePath)
                .into(v_img);
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
        Glide.with(mainView)
                .load(filePath)
                .into(v_img1);
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
    }

    public void visualizarVehiculo(String placa_buscar){

        ImageView v_img = mainView.findViewById(R.id.v_lista_vendedor_img);
        TextView titulo = mainView.findViewById(R.id.auto_titulo_txt);
        TextView placa = mainView.findViewById(R.id.placa_cliente_txt);
        TextView matricula = mainView.findViewById(R.id.matricula_cliente_txt);
        TextView anio = mainView.findViewById(R.id.vehiculo_anio_cliente_txt);
        TextView marca = mainView.findViewById(R.id.vehiculo_marca_cliente_txt);
        TextView modelo = mainView.findViewById(R.id.vehiculo_modelo_cliente_txt);
        TextView color = mainView.findViewById(R.id.vehiculo_color_cliente_txt);
        TextView descripcion = mainView.findViewById(R.id.vehiculo_descripcion_cliente_txt);
        //TextView precioInicial = mainView.findViewById(R.id.vehiculo_pinicial_cliente_txt);
        TextView preciVenta = mainView.findViewById(R.id.vehiculo_pventa_cliente_txt);
        TextView promocion = mainView.findViewById(R.id.vehiculo_promocion_cliente_txt);
        TextView matriculado = mainView.findViewById(R.id.vehiculo_matriculado_cliente_txt);

        Vehiculo vMostrar  = null;
        try {
            vMostrar = patio.buscarVehiculos("Placa",placa_buscar);
        } catch (Exception e) {
            e.printStackTrace();
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
       // precioInicial.setText(format("Precio inicial :%.2f",vMostrar.getPrecioInicial()));
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
    }

    public void buscarVehiculos () {
        EditText placa = mainView.findViewById(R.id.busqueda_placa_etxt);
        String placa_str = placa.getText().toString();
        Vehiculo buscado = null;
        try {
            buscado = patio.buscarVehiculos("Placa",placa_str);
            if (buscado == null) {
                Toast.makeText(mainView.getContext(), "No existe el vehículo buscado", Toast.LENGTH_SHORT).show();
            } else {
                verLista(placa_str, "GHC-2434");
            }
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "No existe el vehiculo", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGalery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_GALERY){
            if(resultCode == Activity.RESULT_OK && data != null){
                foto = data.getData();
                selec_vehiculo_img.setImageURI(foto);
            }else{
                Toast.makeText(mainView.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
