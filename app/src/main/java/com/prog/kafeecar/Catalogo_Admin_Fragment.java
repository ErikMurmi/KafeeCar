package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import static java.lang.String.format;

public class Catalogo_Admin_Fragment extends Fragment {

    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Catalogo";
    private View mainView;

    private FloatingActionButton irAniadirVehiculo;
    private Button deshacer_btn;
    private LinearLayout irVerVehiculo;
    private LinearLayout irVerVehiculo1;
    private ImageButton selec_vehiculo_img;
    private ImageButton buscar_btn;
    private Button irEditarVehiculo;
    public Button editar_btn;
    private Button eliminar_btn;
    private Button aniadir_vehiculo_btn;


    private ScrollView verVehiculo;
    private ScrollView editar_vehiculo;

    private LinearLayout verCatalogo;
    private ScrollView aniadir_vehiculo;

    private PatioVenta patio;
    private Vehiculo m_vehiculo;


    private Vehiculo vMostrar;
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
        irAniadirVehiculo = mainView.findViewById(R.id.ir_aniadir_btn);
        irVerVehiculo = mainView.findViewById(R.id.vehiculo_lista_lyt);
        irVerVehiculo1 = mainView.findViewById(R.id.vehiculo_lista1_lyt);
        irEditarVehiculo = mainView.findViewById(R.id.editar_vehiculo_btn);
        aniadir_vehiculo_btn = mainView.findViewById(R.id.aniadir_vehiculo_btn);
        deshacer_btn = mainView.findViewById(R.id.editar_v_deshacer_btn);
        eliminar_btn = mainView.findViewById(R.id.eliminar_vehiculo_btn);
        editar_btn = mainView.findViewById(R.id.editar_v_editar_btn);
        buscar_btn = mainView.findViewById(R.id.busqueda_admin_btn);
        //Layouts
        verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
        verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
        editar_vehiculo = mainView.findViewById(R.id.editar_vehiculo_lyt);
        aniadir_vehiculo =  mainView.findViewById(R.id.aniadir_vehiculo_lyt);
        irVerVehiculo = mainView.findViewById(R.id.vehiculo_lista_lyt);
        irVerVehiculo1 = mainView.findViewById(R.id.vehiculo_lista1_lyt);


        irAniadirVehiculo.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            aniadir_vehiculo.setVisibility(View.VISIBLE);
        });

        irVerVehiculo.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            irVerVehiculo.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseadow
            verVehiculo.setVisibility(View.VISIBLE);
            try {
                visualizarVehiculo("PSD-1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        irVerVehiculo1.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            irVerVehiculo.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseadow
            verVehiculo.setVisibility(View.VISIBLE);
            try {
                visualizarVehiculo("GHC-2434");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        irEditarVehiculo.setOnClickListener(v -> {
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            irVerVehiculo.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            editar_vehiculo.setVisibility(View.VISIBLE);
            verVehiculoEditable("PSD-1234");
        });


        selec_vehiculo_img.setOnClickListener(v -> {
            openGalery();
        });

        eliminar_btn.setOnClickListener(v -> {
            try {
                patio.removerVehiculo(m_vehiculo.getMatricula());
                Toast.makeText(mainView.getContext(),"Se ha eliminado el vehiculo", Toast.LENGTH_SHORT).show();
                irCatalogo();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainView.getContext(),"No se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        deshacer_btn.setOnClickListener(v -> {
            EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
            verVehiculoEditable(placa_ed.getText().toString());
        });

        editar_btn.setOnClickListener(v -> {
            EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
            editarVehiculo(placa_ed.getText().toString());
            irCatalogo();
        });

        aniadir_vehiculo_btn.setOnClickListener(v -> {
            aniadirVehiculo();
            irCatalogo();
        });

        buscar_btn.setOnClickListener(v -> {
            buscarVehiculos();
        });
        return mainView;
    }


    public void irCatalogo(){
        irAniadirVehiculo.setVisibility(View.GONE);

        verVehiculo.setVisibility(View.GONE);
        //irEditar.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verCatalogo.setVisibility(View.VISIBLE);
        irAniadirVehiculo.setVisibility(View.VISIBLE);
        try {
            verLista("PSD-1234","GHC-2434");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("DefaultLocale")
    public void verLista(String placa, String placa1) throws Exception {


        ImageView v_img = mainView.findViewById(R.id.v_lista_img);
        TextView titulo = mainView.findViewById(R.id.v_marca_modelo_txt);
        TextView anio = mainView.findViewById(R.id.v_anio_lista_txt);
        TextView matricula =  mainView.findViewById(R.id.v_matricula_lista_txt);
        TextView precio = mainView.findViewById(R.id.v_precio_lista_txt);
        TextView placa_v = mainView.findViewById(R.id.v_placa_lista_txt);

        ImageView v_img1 = mainView.findViewById(R.id.v_lista1_img);
        TextView titulo1 = mainView.findViewById(R.id.v_marca_modelo1_txt);
        TextView  anio1 = mainView.findViewById(R.id.v_anio_lista1txt);
        TextView matricula1 =  mainView.findViewById(R.id.v_matricula_lista1_txt);
        TextView placa_v1 = mainView.findViewById(R.id.v_placa_lista1_txt);
        TextView precio1 = mainView.findViewById(R.id.v_precio_lista1_txt);


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


    public void aniadirVehiculo(){
        String msg = "";
        EditText placa_ad = mainView.findViewById(R.id.placa_aniadir_etxt);
        EditText matricula_ad = mainView.findViewById(R.id.aniadir_matricula_etxt);
        EditText anio_ad = mainView.findViewById(R.id.aniadir_anio_etxt);
        EditText descripcion_ad = mainView.findViewById(R.id.aniadir_descripcion_etxt);
        EditText marca_ad = mainView.findViewById(R.id.aniadir_marca_etxt);
        EditText modelo_ad = mainView.findViewById(R.id.aniadir_modelo_etxt);
        EditText color_ad = mainView.findViewById(R.id.aniadir_color_etxt);
        EditText pinicial_ad = mainView.findViewById(R.id.aniadir_pinicial_etxt);
        EditText pventa_ad = mainView.findViewById(R.id.aniadir_precio_venta_etxt);
        EditText ppromocion_ad = mainView.findViewById(R.id.aniadir_precio_promocion_etxt);
        CheckBox matriculado_ad = mainView.findViewById(R.id.matricula_chkbox);

        patio.aniadirVehiculo(
                new Vehiculo(
                        placa_ad.getText().toString(),
                        matricula_ad.getText().toString(),
                        marca_ad.getText().toString(),
                        modelo_ad.getText().toString(),
                        color_ad.getText().toString(),
                        descripcion_ad.getText().toString(),
                        Float.parseFloat(pinicial_ad.getText().toString()),
                        Float.parseFloat(pventa_ad.getText().toString()),
                        Float.parseFloat(ppromocion_ad.getText().toString()),
                        matriculado_ad.isChecked(),
                        Integer.parseInt(anio_ad.getText().toString()),
                        String.format(placa_ad.getText().toString()+".jpg")
                )
        );
        StorageReference filePath = mStorageRef.child("Vehiculos").child(placa_ad.getText().toString()+"_img");
        filePath.putFile(foto).addOnSuccessListener(taskSnapshot ->
                Toast.makeText(mainView.getContext(), "Imagen subida satisfactoriamente",Toast.LENGTH_SHORT).show());
        try {
            if(patio.buscarVehiculos("Placa",placa_ad.getText().toString())!=null)
                msg="Se agrego correctamente el vehiculo";
        } catch (Exception e) {
            msg="No se agrego el vehiculo";
        }
        Toast.makeText(mainView.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void verVehiculoEditable(String placa){
        String msg = "";

        try {

            EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
            EditText matricula_ed = mainView.findViewById(R.id.editar_matricula_etxt);
            EditText anio_ed = mainView.findViewById(R.id.editar_vehiculo_anio_etxt);
            EditText descripcion_ed = mainView.findViewById(R.id.editar_descripcion_etxt);
            EditText marca_ed = mainView.findViewById(R.id.editar_vehiculo_marca_etxt);
            EditText modelo_ed = mainView.findViewById(R.id.editar_vehiculo_modelo_etxt);
            EditText color_ed = mainView.findViewById(R.id.editar_vehiculo_color_etxt);
            EditText pinicial_ed = mainView.findViewById(R.id.editar_v_pinicial_etxt);
            EditText pventa_ed = mainView.findViewById(R.id.editar_v_pventa_etxt);
            EditText ppromocion_ed = mainView.findViewById(R.id.editar_v_ppromocion_etxt);
            CheckBox matriculado_ed = mainView.findViewById(R.id.editar_matriculado_chkbox);

            placa_ed.setText(m_vehiculo.getPlaca());
            matricula_ed.setText(m_vehiculo.getMatricula());
            anio_ed.setText(String.valueOf(m_vehiculo.getAnio()));
            descripcion_ed.setText(m_vehiculo.getDescripcion());
            marca_ed.setText(m_vehiculo.getMarca());
            modelo_ed.setText(m_vehiculo.getModelo());
            color_ed.setText(m_vehiculo.getColor());
            pinicial_ed.setText(String.valueOf(m_vehiculo.getPrecioInicial()));
            pventa_ed.setText(String.valueOf(m_vehiculo.getPrecioVenta()));
            ppromocion_ed.setText(String.valueOf(m_vehiculo.getPromocion()));
            matriculado_ed.setChecked(m_vehiculo.isMatriculado());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarVehiculo(String placa){
        String msg = "";

        try {
            EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
            EditText matricula_ed = mainView.findViewById(R.id.editar_matricula_etxt);
            EditText anio_ed = mainView.findViewById(R.id.editar_vehiculo_anio_etxt);
            EditText descripcion_ed = mainView.findViewById(R.id.editar_descripcion_etxt);
            EditText marca_ed = mainView.findViewById(R.id.editar_vehiculo_marca_etxt);
            EditText modelo_ed = mainView.findViewById(R.id.editar_vehiculo_modelo_etxt);
            EditText color_ed = mainView.findViewById(R.id.editar_vehiculo_color_etxt);
            EditText pinicial_ed = mainView.findViewById(R.id.editar_v_pinicial_etxt);
            EditText pventa_ed = mainView.findViewById(R.id.editar_v_pventa_etxt);
            EditText ppromocion_ed = mainView.findViewById(R.id.editar_v_ppromocion_etxt);
            CheckBox matriculado_ed = mainView.findViewById(R.id.editar_matriculado_chkbox);

            m_vehiculo.actualizarDatos(
                            placa_ed.getText().toString(),
                            matricula_ed.getText().toString(),
                            marca_ed.getText().toString(),
                            modelo_ed.getText().toString(),
                            color_ed.getText().toString(),
                            descripcion_ed.getText().toString(),
                            Float.parseFloat(pinicial_ed.getText().toString()),
                            Float.parseFloat(pventa_ed.getText().toString()),
                            Float.parseFloat(ppromocion_ed.getText().toString()),
                            matriculado_ed.isChecked(),
                            Integer.parseInt(anio_ed.getText().toString()),
                            String.format(placa_ed.getText().toString()+".jpg")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visualizarVehiculo(String placa_buscar) throws Exception {

        m_vehiculo = patio.buscarVehiculos("Placa",placa_buscar);
        ImageView v_img = mainView.findViewById(R.id.vehiculo_img);
        TextView titulo = mainView.findViewById(R.id.auto_titulo_txt);
        TextView placa = mainView.findViewById(R.id.placa_txt);
        TextView matricula = mainView.findViewById(R.id.matricula_txt);
        TextView anio = mainView.findViewById(R.id.vehiculo_anio_txt);
        TextView marca = mainView.findViewById(R.id.vehiculo_marca_txt);
        TextView modelo = mainView.findViewById(R.id.vehiculo_modelo_txt);
        TextView color = mainView.findViewById(R.id.vehiculo_color_txt);
        TextView descripcion = mainView.findViewById(R.id.vehiculo_descripcion_txt);
        TextView precioInicial = mainView.findViewById(R.id.vehiculo_pinicial_txt);
        TextView preciVenta = mainView.findViewById(R.id.vehiculo_pventa_txt);
        TextView promocion = mainView.findViewById(R.id.vehiculo_promocion_txt);
        TextView matriculado = mainView.findViewById(R.id.vehiculo_matriculado_txt);

        Vehiculo vMostrar  = null;
        try {
            vMostrar = patio.buscarVehiculos("Placa",placa_buscar);
            m_vehiculo = vMostrar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String titulo_str = vMostrar.getMarca()+" "+vMostrar.getModelo();
        titulo.setText(titulo_str);
        placa.setText(vMostrar.getPlaca());
        matricula.setText(format(getString(R.string.matricula_frmt), vMostrar.getMatricula()));
        anio.setText(format("Año :%s",vMostrar.getAnio()));
        marca.setText(format("Marca :%s",vMostrar.getMarca()));
        modelo.setText(format("Modelo :%s",vMostrar.getModelo()));
        descripcion.setText(format("Descripción :%s",vMostrar.getDescripcion()));
        color.setText(format("Color :%s",vMostrar.getColor()));
        precioInicial.setText(format("Precio inicial :%.2f",vMostrar.getPrecioInicial()));
        preciVenta.setText(format("Precio venta :%.2f",vMostrar.getPrecioVenta()));
        promocion.setText(format("Precio promoción:%.2f",vMostrar.getPromocion()));

        //Cargar imagen
        StorageReference filePath = mStorageRef.child("Vehiculos/"+vMostrar.getimagen());
        Glide.with(mainView)
               .load(filePath)
                .into(v_img);
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
