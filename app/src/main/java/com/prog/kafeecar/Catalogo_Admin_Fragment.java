package com.prog.kafeecar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class Catalogo_Admin_Fragment extends Fragment implements Adaptador_Lista_Catalogo.RecyclerItemClick, SearchView.OnQueryTextListener {

    private static final int REQUEST_IMAGE_GALERY = 101;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public Button editar_btn;
    public Button cancelar_btn;
    private View mainView;
    private FloatingActionButton ir_aniadir_btn;
    private ImageButton selec_vehiculo_img;
    private ImageButton edit_vehiculo_img;
    private ScrollView verVehiculo;
    private ScrollView editar_vehiculo;
    private LinearLayout verCatalogo;
    private ScrollView aniadir_vehiculo;
    private PatioVenta patio;
    private Vehiculo vMostrar;
    private Uri foto = null;
    private boolean editar_imagen = false;
    private Adaptador_Lista_Catalogo adptadorlistaview;
    private SearchView busqueda_placa;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.catalogo_admin, container, false);
        busqueda_placa = mainView.findViewById(R.id.busqueda_placa_bar);
        patio = Patioventainterfaz.patioventa;
        //Botones
        selec_vehiculo_img = mainView.findViewById(R.id.aniadir_vehiculo_imagen_btn);
        ir_aniadir_btn = mainView.findViewById(R.id.ir_aniadir_btn);
        Button irEditarVehiculo = mainView.findViewById(R.id.editar_vehiculo_btn);
        Button aniadir_vehiculo_btn = mainView.findViewById(R.id.aniadir_vehiculo_btn);
        Button editar_v_descartar_btn = mainView.findViewById(R.id.editar_v_descartar_btn);
        cancelar_btn = mainView.findViewById(R.id.cancelar_ca_ad_btn);
        Button eliminar_btn = mainView.findViewById(R.id.eliminar_vehiculo_btn);
        editar_btn = mainView.findViewById(R.id.editar_v_editar_btn);
        //Botonoes imagen
        edit_vehiculo_img = mainView.findViewById(R.id.im_cat_ad_imgbtn);
        //Layouts
        verVehiculo = mainView.findViewById(R.id.vehiculo_admin);
        verCatalogo = mainView.findViewById(R.id.vehiculos_admin);
        editar_vehiculo = mainView.findViewById(R.id.editar_vehiculo_lyt);
        aniadir_vehiculo = mainView.findViewById(R.id.aniadir_vehiculo_lyt);

        //Metodos de los botones
        ir_aniadir_btn.setOnClickListener(v -> {
            //Desactivar otros diseños
            ir_aniadir_btn.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            aniadir_vehiculo.setVisibility(View.VISIBLE);
            clearRegistrar();
        });

        irEditarVehiculo.setOnClickListener(v -> {
            ir_aniadir_btn.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            aniadir_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            editar_vehiculo.setVisibility(View.VISIBLE);
            verVehiculoEditable();
        });

        selec_vehiculo_img.setOnClickListener(v -> openGalery());

        edit_vehiculo_img.setOnClickListener(v -> {
            openGalery();
            editar_imagen = true;
        });

        aniadir_vehiculo_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Añadir el vehiculo");
            msg.setMessage("¿Está seguro de añadir el vehículo con los datos ingresados?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                if(aniadirVehiculo()){
                    Toast.makeText(mainView.getContext(), "Se agrego correctamente el vehiculo", Toast.LENGTH_SHORT).show();
                    irCatalogo();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        eliminar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Eliminar vehículo");
            msg.setMessage("¿Está seguro de eliminar el vehículo con la placa " + vMostrar.getPlaca() + " ?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    patio.removerVehiculo(vMostrar.getPlaca());
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "Error 1: No se pudo eliminar el vehiculo", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(mainView.getContext(), "Se ha eliminado el vehículo", Toast.LENGTH_SHORT).show();
                irCatalogo();
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        editar_v_descartar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Descartar Cambios");
            msg.setMessage("¿Está seguro de no guardar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> irCatalogo());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        editar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Editar vehículo");
            msg.setMessage("¿Está seguro de editar el vehículo con la placa " + vMostrar.getPlaca() + " ?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                if(editarVehiculo()){
                    irCatalogo();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        cancelar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Cancelar");
            msg.setMessage("¿Estás seguro de salir sin aniadir el vehiculo?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> irCatalogo());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });


        //Metodo para el control del boton atras
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (editar_vehiculo.getVisibility() == View.VISIBLE) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Aceptar", (dialog, which) -> irVer());
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if (verVehiculo.getVisibility() == View.VISIBLE) {
                    irCatalogo();
                }
                if (aniadir_vehiculo.getVisibility() == View.VISIBLE) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("Volver");
                    msg.setMessage("¿Estás seguro de salir sin aniadir el vehiculo?");
                    msg.setPositiveButton("Aceptar", (dialog, which) -> {
                        irCatalogo();
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        irCatalogo();

        return mainView;
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.rc_autos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.getVehiculos().copiar(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    public void irCatalogo() {
        busqueda_placa.setOnQueryTextListener(this);
        cargar();
        AutoCompleteTextView filtros = mainView.findViewById(R.id.v_filtros_ad_ddm);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items, Patioventainterfaz.filtros_vehiculos);
        filtros.setAdapter(adapt);
        filtros.setOnItemClickListener((parent, view, position, id) -> {
            cargar();
            busqueda_placa.setQueryHint(adapt.getItem(position));
        });
        ir_aniadir_btn.setVisibility(View.GONE);
        verVehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        verCatalogo.setVisibility(View.VISIBLE);
        ir_aniadir_btn.setVisibility(View.VISIBLE);
    }

    public void irVer() {
        ir_aniadir_btn.setVisibility(View.GONE);
        verCatalogo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        verVehiculo.setVisibility(View.VISIBLE);
        visualizarVehiculo();
    }

    public boolean aniadirVehiculo() {
        int vacios = 0;
        int invalidos = 0;
        String placa_str ="";
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
        //TODO REEEMPLAZAR POR UN UNICO MENSAJE DE ERROR

        if (foto == null) {
            vacios++;
            Toast.makeText(mainView.getContext(), "No se ha escogido una imagen", Toast.LENGTH_SHORT).show();
        }
        if (isEmpty(placa_ad)) {
            vacios++;
        } else if (placa_ad.getText().toString().length() != 8) {
            Toast.makeText(mainView.getContext(), "Placa inválida", Toast.LENGTH_SHORT).show();
            invalidos++;
        }

        if(isEmpty(matricula_ad)){
            vacios++;
        }
        if (isEmpty(anio_ad)) {
            vacios++;
        } else if (Integer.parseInt(anio_ad.getText().toString()) < 1900 || Integer.parseInt(anio_ad.getText().toString()) > 2021) {
            Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
            anio_ad.setText("");
            invalidos++;
        }

        if (isEmpty(descripcion_ad)) {
            vacios++;
        }

        if (isEmpty(marca_ad)) {
            vacios++;
        }


        if (isEmpty(modelo_ad)) {
            vacios++;
        }


        if (isEmpty(color_ad)) {
            vacios++;
        }


        if (isEmpty(pinicial_ad)) {
            vacios++;
        } else if (Integer.parseInt(pinicial_ad.getText().toString()) <= 0) {
            invalidos++;
            Toast.makeText(mainView.getContext(), "Valor incial inválido", Toast.LENGTH_SHORT).show();
        }


        if (isEmpty(pventa_ad)) {
            vacios++;
        } else if (Integer.parseInt(pventa_ad.getText().toString()) <= 0) {
            invalidos++;
            Toast.makeText(mainView.getContext(), "Valor de venta inválido", Toast.LENGTH_SHORT).show();
        }

        if (isEmpty(ppromocion_ad)) {
            vacios++;
        } else if (Integer.parseInt(pventa_ad.getText().toString()) < 0) {
            invalidos++;
            Toast.makeText(mainView.getContext(), "Valor de promoción inválido", Toast.LENGTH_SHORT).show();
        }


        if (vacios == 0 && invalidos == 0) {
            placa_str = placa_ad.getText().toString();
            if(patio.buscarVehiculos("Placa",placa_str)==null){
                StorageReference filePath = mStorageRef.child("Vehiculos").child(placa_str + ".jpg");
                filePath.putFile(foto);
                Vehiculo nuevo = new Vehiculo(
                        placa_str,
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
                        placa_ad.getText().toString() + ".jpg"
                );

                try {
                    if (patio.aniadirVehiculo(nuevo)) {
                        Toast.makeText(mainView.getContext(), "Se agregó correctamente el vehículo", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "Error 2: Error al agregar el vehículo", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mainView.getContext(), "Placa ya registrada en el patio de venta", Toast.LENGTH_SHORT).show();
            }

        } else if(vacios>0){
            Toast.makeText(mainView.getContext(), "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void verVehiculoEditable() {
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
        StorageReference filePath = mStorageRef.child("Vehiculos/" + vMostrar.getimagen());

        final File localFile;
        try {
            localFile = File.createTempFile(vMostrar.getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                edit_vehiculo_img.setImageURI(nuevo);
            });
        } catch (IOException e) {
            Toast.makeText(mainView.getContext(), "Error 3: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
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

    }

    public boolean editarVehiculo() {
        int vacios = 0;
        int invalidos = 0;

        EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
        if (isEmpty(placa_ed)) {
            vacios++;
        } else if (placa_ed.getText().toString().length() != 8) {
            Toast.makeText(mainView.getContext(), "Placa inválida", Toast.LENGTH_SHORT).show();
            invalidos++;
        }

        EditText matricula_ed = mainView.findViewById(R.id.editar_matricula_etxt);
        if(isEmpty(matricula_ed)){
            Toast.makeText(mainView.getContext(),"Campo de matricula vacio", Toast.LENGTH_SHORT).show();
            vacios++;
        }

        EditText anio_ed = mainView.findViewById(R.id.editar_vehiculo_anio_etxt);
        if (isEmpty(anio_ed)) {
            vacios++;
        } else if (Integer.parseInt(anio_ed.getText().toString()) < 1900 || Integer.parseInt(anio_ed.getText().toString()) > 2021) {
            Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
            anio_ed.setText("");
            invalidos++;
        }

        EditText descripcion_ed = mainView.findViewById(R.id.editar_descripcion_etxt);
        if (isEmpty(descripcion_ed)) {
            vacios++;
        }

        EditText marca_ed = mainView.findViewById(R.id.editar_vehiculo_marca_etxt);
        if (isEmpty(marca_ed)) {
            vacios++;
        }

        EditText modelo_ed = mainView.findViewById(R.id.editar_vehiculo_modelo_etxt);
        if (isEmpty(modelo_ed)) {
            vacios++;
        }

        EditText color_ed = mainView.findViewById(R.id.editar_vehiculo_color_etxt);
        if (isEmpty(color_ed)) {
            vacios++;
        }

        EditText pinicial_ed = mainView.findViewById(R.id.editar_v_pinicial_etxt);
        if (isEmpty(pinicial_ed)) {
            vacios++;
        } else if (Float.parseFloat(pinicial_ed.getText().toString()) <= 0) {
            invalidos++;
            Toast.makeText(mainView.getContext(), "Valor incial inválido", Toast.LENGTH_SHORT).show();
        }

        EditText pventa_ed = mainView.findViewById(R.id.editar_v_pventa_etxt);
        if (isEmpty(pventa_ed)) {
            vacios++;
        } else if (Float.parseFloat(pventa_ed.getText().toString()) < 0) {
            invalidos++;
            Toast.makeText(mainView.getContext(), "Valor de promoción inválido", Toast.LENGTH_SHORT).show();
        }

        EditText ppromocion_ed = mainView.findViewById(R.id.editar_v_ppromocion_etxt);
        if (isEmpty(ppromocion_ed)) {
            vacios++;
        } else if (Float.parseFloat(ppromocion_ed.getText().toString()) < 0) {
            invalidos++;
            Toast.makeText(mainView.getContext(), "Valor de promoción inválido", Toast.LENGTH_SHORT).show();
        }

        CheckBox matriculado_ed = mainView.findViewById(R.id.editar_matriculado_chkbox);
        if (vacios == 0 && invalidos == 0) {
            String placa_n = placa_ed.getText().toString();
            if(placa_n.compareToIgnoreCase(vMostrar.getPlaca())==0 || patio.buscarVehiculos("Placa",placa_n)==null){
                if (foto != null) {
                    StorageReference filePath = mStorageRef.child("Vehiculos").child(placa_n + ".jpg");
                    filePath.putFile(foto);
                }
                vMostrar.actualizarDatos(
                        placa_n.toUpperCase(),
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
                        placa_n + ".jpg"
                );
                if (patio.buscarVehiculos("Placa", placa_n) != null) {
                    Toast.makeText(mainView.getContext(), "Se editó correctamente el auto", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }else if(patio.buscarVehiculos("Placa",placa_n)!=null){
                Toast.makeText(mainView.getContext(), "Placa ya registrada en el patio", Toast.LENGTH_SHORT).show();
            }
        } else if(vacios>0){
            Toast.makeText(mainView.getContext(), "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void visualizarVehiculo() {
        Patioventainterfaz.v_aux_cita = vMostrar;
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

        String titulo_str = vMostrar.getMarca() + " " + vMostrar.getModelo();
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
        StorageReference filePath = mStorageRef.child("Vehiculos/" + vMostrar.getimagen());
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                v_img.setImageURI(nuevo);
            });
        } catch (IOException e) {
            Toast.makeText(mainView.getContext(), "Error 5: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }
        String matriculado_str;
        if (vMostrar.isMatriculado()) {
            matriculado_str = "Si";
        } else {
            matriculado_str = "No";
        }
        matriculado.setText(matriculado_str);
    }

    public void clearRegistrar() {
        EditText placa_aniadir_etxt = mainView.findViewById(R.id.placa_aniadir_etxt);
        EditText aniadir_matricula_etxt = mainView.findViewById(R.id.aniadir_matricula_etxt);
        EditText aniadir_anio_etxt = mainView.findViewById(R.id.aniadir_anio_etxt);
        EditText aniadir_descripcion_etxt = mainView.findViewById(R.id.aniadir_descripcion_etxt);
        EditText aniadir_marca_etxt = mainView.findViewById(R.id.aniadir_marca_etxt);
        EditText aniadir_modelo_etxt = mainView.findViewById(R.id.aniadir_modelo_etxt);
        EditText aniadir_color_etxt = mainView.findViewById(R.id.aniadir_color_etxt);
        EditText aniadir_pinicial_etxt = mainView.findViewById(R.id.aniadir_pinicial_etxt);
        EditText aniadir_precio_venta_etxt = mainView.findViewById(R.id.aniadir_precio_venta_etxt);
        EditText aniadir_precio_promocion_etxt = mainView.findViewById(R.id.aniadir_precio_promocion_etxt);
        CheckBox matricula_chkbox = mainView.findViewById(R.id.matricula_chkbox);
        placa_aniadir_etxt.getText().clear();
        aniadir_matricula_etxt.getText().clear();
        aniadir_anio_etxt.getText().clear();
        aniadir_descripcion_etxt.setText("");
        aniadir_marca_etxt.getText().clear();
        aniadir_modelo_etxt.getText().clear();
        aniadir_color_etxt.getText().clear();
        aniadir_pinicial_etxt.getText().clear();
        aniadir_precio_venta_etxt.getText().clear();
        aniadir_precio_promocion_etxt.getText().clear();
        matricula_chkbox.setChecked(false);
        foto=null;
    }

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_GALERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                foto = data.getData();
                if (editar_imagen) {
                    edit_vehiculo_img.setImageURI(foto);
                    editar_imagen = false;
                } else {
                    selec_vehiculo_img.setImageURI(foto);
                }
            } else {
                Toast.makeText(mainView.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa) {
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            irVer();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error 6: Busqueda fallida del auto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        adptadorlistaview.buscar(newText, busqueda_placa.getQueryHint().toString());
        return false;
    }

}
