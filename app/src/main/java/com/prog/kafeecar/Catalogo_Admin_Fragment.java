package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

public class Catalogo_Admin_Fragment extends Fragment implements Adaptador_Lista_Catalogo.RecyclerItemClick {

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
    public Button cancelar_btn;
    private Button eliminar_btn;
    private Button aniadir_vehiculo_btn;


    private ScrollView verVehiculo;
    private ScrollView editar_vehiculo;
    private RecyclerView listaview;

    private LinearLayout verCatalogo;
    private ScrollView aniadir_vehiculo;

    private PatioVenta patio;


    private Vehiculo vMostrar;
    private Uri foto;

    private final StorageReference mStorageRef =FirebaseStorage.getInstance().getReference();
    private Adaptador_Lista_Catalogo adptadorlistaview;
    //Variables extras

    public boolean guardarEdit = false;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        //Botones
        selec_vehiculo_img = mainView.findViewById(R.id.aniadir_vehiculo_imagen_btn);
        irAniadirVehiculo = mainView.findViewById(R.id.ir_aniadir_btn);
        irVerVehiculo1 = mainView.findViewById(R.id.vehiculo_lista1_lyt);
        irEditarVehiculo = mainView.findViewById(R.id.editar_vehiculo_btn);
        aniadir_vehiculo_btn = mainView.findViewById(R.id.aniadir_vehiculo_btn);
        deshacer_btn = mainView.findViewById(R.id.editar_v_deshacer_btn);
        cancelar_btn = mainView.findViewById(R.id.cancelar_ca_ad_btn);
        eliminar_btn = mainView.findViewById(R.id.eliminar_vehiculo_btn);
        editar_btn = mainView.findViewById(R.id.editar_v_editar_btn);
        buscar_btn = mainView.findViewById(R.id.busqueda_admin_btn);
        //Layouts
        verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
        verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
        editar_vehiculo = mainView.findViewById(R.id.editar_vehiculo_lyt);
        aniadir_vehiculo =  mainView.findViewById(R.id.aniadir_vehiculo_lyt);
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


        irVerVehiculo1.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            irVerVehiculo.setVisibility(View.GONE);
            irVerVehiculo1.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseadow
            verVehiculo.setVisibility(View.VISIBLE);
            try {
                visualizarVehiculo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        irEditarVehiculo.setOnClickListener(v -> {
            Toast.makeText(mainView.getContext(), "Entra al metodo", Toast.LENGTH_SHORT).show();
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            editar_vehiculo.setVisibility(View.VISIBLE);
            guardarEdit = false;
            verVehiculoEditable();
        });


        selec_vehiculo_img.setOnClickListener(v -> {
            openGalery();
        });

        eliminar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Eliminar vehículo");
            msg.setMessage("¿Está seguro de eliminar el vehículo con la placa "+ vMostrar.getPlaca()+ " ?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    patio.removerVehiculo(vMostrar.getMatricula());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(mainView.getContext(),"Se ha eliminado el vehículo", Toast.LENGTH_SHORT).show();
                irCatalogo();
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        deshacer_btn.setOnClickListener(v -> {
            verVehiculoEditable();
        });

        editar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Editar vehículo");
            msg.setMessage("¿Está seguro de editar el vehículo con la placa "+ vMostrar.getPlaca()+ " ?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
                editarVehiculo(placa_ed.getText().toString());
                Toast.makeText(mainView.getContext(),"Se actualizaron los datos", Toast.LENGTH_SHORT).show();
                irCatalogo();
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        aniadir_vehiculo_btn.setOnClickListener(v -> {
            aniadirVehiculo();
            irCatalogo();
        });

        buscar_btn.setOnClickListener(v -> buscarVehiculos());

        cancelar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Cancelar");
            msg.setMessage("¿Estás seguro de salir sin aniadir el vehiculo?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> irCatalogo());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });
        try {
            cargar();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(),"No se cargaron los autos", Toast.LENGTH_SHORT).show();
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                /*
                verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
                verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
                editar_vehiculo = mainView.findViewById(R.id.editar_vehiculo_lyt);
                aniadir_vehiculo =  mainView.findViewById(R.id.aniadir_vehiculo_lyt);
                irVerVehiculo = mainView.findViewById(R.id.vehiculo_lista_lyt);
                irVerVehiculo1 = mainView.findViewById(R.id.vehiculo_lista1_lyt);
                 */
                if(editar_vehiculo.getVisibility()==View.VISIBLE){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Aceptar", (dialog, which) -> irVer());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if(verVehiculo.getVisibility()== View.VISIBLE){
                    irCatalogo();
                }
                if(aniadir_vehiculo.getVisibility()==View.VISIBLE){
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("Volver");
                    msg.setMessage("¿Estás seguro de salir sin aniadir el vehiculo?");
                    msg.setPositiveButton("Aceptar", (dialog, which) -> irCatalogo());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                //Intent myIntent = new Intent(nombreClase.this,activityDestiny.class);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        return mainView;
    }

    public void cargar() throws Exception {
        listaview=mainView.findViewById(R.id.rc_autos);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.getVehiculos(),this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void irCatalogo(){
        irAniadirVehiculo.setVisibility(View.GONE);
        verVehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        //Activar el diseño deseado
        verCatalogo.setVisibility(View.VISIBLE);
        irAniadirVehiculo.setVisibility(View.VISIBLE);
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void irV1(){
        irAniadirVehiculo.setVisibility(View.GONE);
        verCatalogo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        irVerVehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verVehiculo.setVisibility(View.VISIBLE);
        try {
            visualizarVehiculo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void irVer(){

        irAniadirVehiculo.setVisibility(View.GONE);
        verCatalogo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        //Activar el diseño deseadow
        verVehiculo.setVisibility(View.VISIBLE);
        try {
            visualizarVehiculo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void aniadirVehiculo(){
        String msg = "";
        int c=0;
        EditText placa_ad = mainView.findViewById(R.id.placa_aniadir_etxt);
        if(!isEmpty(placa_ad)){
            Toast.makeText(mainView.getContext(),"Campo de placa vacio", Toast.LENGTH_SHORT).show();
            c++;
        }else if(placa_ad.getText().toString().length() != 8){
            Toast.makeText(mainView.getContext(),"Placa no válida", Toast.LENGTH_SHORT).show();
            c++;
        }


        EditText matricula_ad = mainView.findViewById(R.id.aniadir_matricula_etxt);
        if(!isEmpty(matricula_ad)){
            Toast.makeText(mainView.getContext(),"Campo de matricula vacio", Toast.LENGTH_SHORT).show();
            c++;
        }else if(matricula_ad.getText().toString().length() != 8){
            Toast.makeText(mainView.getContext(),"Matricula no válida", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText anio_ad = mainView.findViewById(R.id.aniadir_anio_etxt);
        if(!isEmpty(anio_ad)){
            Toast.makeText(mainView.getContext(),"Campo de anio vacio", Toast.LENGTH_SHORT).show();
            c++;
        }else if(Integer.parseInt(anio_ad.getText().toString()) < 1900 || Integer.parseInt(anio_ad.getText().toString()) > 2021){
                Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                anio_ad.setText("");
                c++;
        }


        EditText descripcion_ad = mainView.findViewById(R.id.aniadir_descripcion_etxt);
        if(!isEmpty(descripcion_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de descripcion vacia", Toast.LENGTH_SHORT).show();
        }

        EditText marca_ad = mainView.findViewById(R.id.aniadir_marca_etxt);
        if(!isEmpty(marca_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de marca vacio", Toast.LENGTH_SHORT).show();
        }

        EditText modelo_ad = mainView.findViewById(R.id.aniadir_modelo_etxt);
        if(!isEmpty(modelo_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de modelo vacio", Toast.LENGTH_SHORT).show();
        }

        EditText color_ad = mainView.findViewById(R.id.aniadir_color_etxt);
        if(!isEmpty(color_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de color vacio", Toast.LENGTH_SHORT).show();
        }

        EditText pinicial_ad = mainView.findViewById(R.id.aniadir_pinicial_etxt);
        if(!isEmpty(pinicial_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de precio inicial vacio", Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(pinicial_ad.getText().toString())<=0){
            c++;
            Toast.makeText(mainView.getContext(),"Valor incial invalido", Toast.LENGTH_SHORT).show();
        }

        EditText pventa_ad = mainView.findViewById(R.id.aniadir_precio_venta_etxt);
        if(!isEmpty(pventa_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de valor de venta vacio", Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(pventa_ad.getText().toString())<=0){
            c++;
            Toast.makeText(mainView.getContext(),"Valor de venta invalido", Toast.LENGTH_SHORT).show();
        }
        EditText ppromocion_ad = mainView.findViewById(R.id.aniadir_precio_promocion_etxt);
        if(!isEmpty(ppromocion_ad)){
            c++;
            Toast.makeText(mainView.getContext(),"Campo de promoción inicial vacio", Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(pventa_ad.getText().toString())<0){
            c++;
            Toast.makeText(mainView.getContext(),"Valor de promoción invalido", Toast.LENGTH_SHORT).show();
        }
        CheckBox matriculado_ad = mainView.findViewById(R.id.matricula_chkbox);
        AtomicBoolean isFoto = new AtomicBoolean(false);
        StorageReference filePath = mStorageRef.child("Vehiculos").child(placa_ad.getText().toString()+"_img");
        filePath.putFile(foto).addOnSuccessListener(taskSnapshot ->
                        isFoto.set(true)
                );
        if(!isFoto.get()){
            c++;
            Toast.makeText(mainView.getContext(),"No se ha escogido una imagen", Toast.LENGTH_SHORT).show();
        }
        if(c==0 ){
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
        }

        try {
            if(patio.buscarVehiculos("Placa",placa_ad.getText().toString())!=null)
                msg="Se agrego correctamente el vehiculo";
        } catch (Exception e) {
            msg="No se agrego el vehiculo";
        }
        Toast.makeText(mainView.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void verVehiculoEditable(){
        String msg = "";

        try {
            ImageButton perfil_img_bt = mainView.findViewById(R.id.perfil_img_bt);
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
            StorageReference filePath = mStorageRef.child("Vehiculos/"+vMostrar.getimagen());

            Glide.with(mainView).load(filePath).into(perfil_img_bt);

            try {
                final File localFile = File.createTempFile(vMostrar.getimagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    perfil_img_bt.setImageBitmap(bitmap);
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
            placa_ed.setText(vMostrar.getPlaca());
            matricula_ed.setText(vMostrar.getMatricula());
            anio_ed.setText(String.valueOf(vMostrar.getAnio()));
            descripcion_ed.setText(vMostrar.getDescripcion());
            marca_ed.setText(vMostrar.getMarca());
            modelo_ed.setText(vMostrar.getModelo());
            color_ed.setText(vMostrar.getColor());
            pinicial_ed.setText(String.valueOf(vMostrar.getPrecioInicial()));
            pventa_ed.setText(String.valueOf(vMostrar.getPrecioVenta()));
            ppromocion_ed.setText(String.valueOf(vMostrar.getPromocion()));
            matriculado_ed.setChecked(vMostrar.isMatriculado());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarVehiculo(String placa){
        String msg = "";
        int c=0;
        try {

            EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
            if(!isEmpty(placa_ed)){
                Toast.makeText(mainView.getContext(),"Campo de placa vacio", Toast.LENGTH_SHORT).show();
                c++;
            }else if(placa_ed.getText().toString().length() != 8){
                Toast.makeText(mainView.getContext(),"Placa no válida", Toast.LENGTH_SHORT).show();
                c++;
            }

            EditText matricula_ed = mainView.findViewById(R.id.editar_matricula_etxt);
            if(!isEmpty(matricula_ed)){
                Toast.makeText(mainView.getContext(),"Campo de matricula vacio", Toast.LENGTH_SHORT).show();
                c++;
            }else if(matricula_ed.getText().toString().length() != 8){
                Toast.makeText(mainView.getContext(),"Matricula no válida", Toast.LENGTH_SHORT).show();
                c++;
            }

            EditText anio_ed = mainView.findViewById(R.id.editar_vehiculo_anio_etxt);
            if(!isEmpty(anio_ed)){
                Toast.makeText(mainView.getContext(),"Campo de anio vacio", Toast.LENGTH_SHORT).show();
                c++;
            }else if(Integer.parseInt(anio_ed.getText().toString()) < 1900 || Integer.parseInt(anio_ed.getText().toString()) > 2021){
                Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                anio_ed.setText("");
                c++;
            }

            EditText descripcion_ed = mainView.findViewById(R.id.editar_descripcion_etxt);
            if(!isEmpty(descripcion_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de descripcion vacia", Toast.LENGTH_SHORT).show();
            }

            EditText marca_ed = mainView.findViewById(R.id.editar_vehiculo_marca_etxt);
            if(!isEmpty(marca_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de marca vacio", Toast.LENGTH_SHORT).show();
            }

            EditText modelo_ed = mainView.findViewById(R.id.editar_vehiculo_modelo_etxt);
            if(!isEmpty(modelo_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de modelo vacio", Toast.LENGTH_SHORT).show();
            }

            EditText color_ed = mainView.findViewById(R.id.editar_vehiculo_color_etxt);
            if(!isEmpty(color_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de color vacio", Toast.LENGTH_SHORT).show();
            }

            EditText pinicial_ed = mainView.findViewById(R.id.editar_v_pinicial_etxt);
            if(!isEmpty(pinicial_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de precio inicial vacio", Toast.LENGTH_SHORT).show();
            }else if(Integer.parseInt(pinicial_ed.getText().toString())<=0){
                c++;
                Toast.makeText(mainView.getContext(),"Valor incial invalido", Toast.LENGTH_SHORT).show();
            }

            EditText pventa_ed = mainView.findViewById(R.id.editar_v_pventa_etxt);
            if(!isEmpty(pventa_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de promoción inicial vacio", Toast.LENGTH_SHORT).show();
            }else if(Integer.parseInt(pventa_ed.getText().toString())<0){
                c++;
                Toast.makeText(mainView.getContext(),"Valor de promoción invalido", Toast.LENGTH_SHORT).show();
            }

            EditText ppromocion_ed = mainView.findViewById(R.id.editar_v_ppromocion_etxt);
            if(!isEmpty(ppromocion_ed)){
                c++;
                Toast.makeText(mainView.getContext(),"Campo de promoción inicial vacio", Toast.LENGTH_SHORT).show();
            }else if(Integer.parseInt(ppromocion_ed.getText().toString())<0){
                c++;
                Toast.makeText(mainView.getContext(),"Valor de promoción invalido", Toast.LENGTH_SHORT).show();
            }

            CheckBox matriculado_ed = mainView.findViewById(R.id.editar_matriculado_chkbox);
            if(c==0){
                vMostrar.actualizarDatos(
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
                guardarEdit = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visualizarVehiculo() throws Exception {
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

        String titulo_str = vMostrar.getMarca()+" "+vMostrar.getModelo();
        titulo.setText(titulo_str);
        placa.setText(vMostrar.getPlaca());
        matricula.setText(vMostrar.getMatricula());
        anio.setText(String.valueOf(vMostrar.getAnio()));
        marca.setText(vMostrar.getMarca());
        modelo.setText(vMostrar.getModelo());
        descripcion.setText(vMostrar.getDescripcion());
        color.setText(vMostrar.getColor());
        precioInicial.setText(String.valueOf(vMostrar.getPrecioInicial()));
        preciVenta.setText(String.valueOf(vMostrar.getPrecioVenta()));
        promocion.setText(String.valueOf(vMostrar.getPromocion()));

        //Cargar imagen
        StorageReference filePath = mStorageRef.child("Vehiculos/"+vMostrar.getimagen());
        Glide.with(mainView)
               .load(filePath)
                .into(v_img);
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                v_img.setImageBitmap(bitmap);
            });
        }catch (IOException e){
            e.printStackTrace();
        }
        //

        if(vMostrar.isMatriculado()){
            matriculado.setText("Si");
        }else{
            matriculado.setText("No");
        }


    }

    public void buscarVehiculos () {
        EditText placa = mainView.findViewById(R.id.busqueda_placa_etxt);
        String placa_str = placa.getText().toString();
        Vehiculo buscado;
        try {
            buscado = patio.buscarVehiculos("Placa",placa_str);
            if (buscado == null) {
                Toast.makeText(mainView.getContext(), "No existe el vehículo buscado", Toast.LENGTH_SHORT).show();
            } else {
                //verLista(placa_str, "GHC-2434");
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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa) {
        //irVer(placa);
        try {
            vMostrar = patio.buscarVehiculos("Placa",placa);
            irVer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void irAtras(){
        ScrollView verVehiculo = ca.findViewById(R.id.vehiculo_admin);
        if(verVehiculo.getVisibility()==View.VISIBLE){
            Toast.makeText(mainView.getContext(), "Metodo funcionando",Toast.LENGTH_SHORT).show();
        }
    }*/

}
