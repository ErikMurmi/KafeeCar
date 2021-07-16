package com.prog.kafeecar;

import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    private FloatingActionButton irAniadirVehiculo;
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
        irAniadirVehiculo = mainView.findViewById(R.id.ir_aniadir_btn);
        Button irEditarVehiculo = mainView.findViewById(R.id.editar_vehiculo_btn);
        Button aniadir_vehiculo_btn = mainView.findViewById(R.id.aniadir_vehiculo_btn);
        Button deshacer_btn = mainView.findViewById(R.id.editar_v_deshacer_btn);
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
        irAniadirVehiculo.setOnClickListener(v -> {
            //Desactivar otros diseños
            irAniadirVehiculo.setVisibility(View.GONE);
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            editar_vehiculo.setVisibility(View.GONE);
            //Activar el diseño deseado
            aniadir_vehiculo.setVisibility(View.VISIBLE);
        });

        irEditarVehiculo.setOnClickListener(v -> {
            irAniadirVehiculo.setVisibility(View.GONE);
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
        //TODO ALER DIALOG
        aniadir_vehiculo_btn.setOnClickListener(v -> aniadirVehiculo());

        eliminar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Eliminar vehículo");
            msg.setMessage("¿Está seguro de eliminar el vehículo con la placa " + vMostrar.getPlaca() + " ?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    patio.removerVehiculo(vMostrar.getPlaca());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(mainView.getContext(), "Se ha eliminado el vehículo", Toast.LENGTH_SHORT).show();
                irCatalogo();
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        //TODO CAMBIAR POR DESCARTAR
        deshacer_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Deshacer Cambios");
            msg.setMessage("¿Está seguro de no guardar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> verVehiculoEditable());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        //TODO I
        editar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Editar vehículo");
            msg.setMessage("¿Está seguro de editar el vehículo con la placa " + vMostrar.getPlaca() + " ?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                editarVehiculo();
                irCatalogo();
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

    @Override
    public void onResume() {
        super.onResume();
        busqueda_placa.setOnQueryTextListener(this);
        cargar();
    }


    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.rc_autos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.getVehiculos().copiar(),this);
        listaview.setAdapter(adptadorlistaview);
    }

    public void irCatalogo() {
        busqueda_placa.setOnQueryTextListener(this);
        cargar();
        AutoCompleteTextView filtros = mainView.findViewById(R.id.v_filtros_ad_ddm);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.filtros_vehiculos);
        filtros.setAdapter(adapt);
        filtros.setOnItemClickListener((parent, view, position, id) -> {
            cargar();
            busqueda_placa.setQueryHint(adapt.getItem(position));
        });
        irAniadirVehiculo.setVisibility(View.GONE);
        verVehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        //Activar el diseño deseado
        verCatalogo.setVisibility(View.VISIBLE);
        irAniadirVehiculo.setVisibility(View.VISIBLE);
    }

    public void irVer() {

        irAniadirVehiculo.setVisibility(View.GONE);
        verCatalogo.setVisibility(View.GONE);
        editar_vehiculo.setVisibility(View.GONE);
        aniadir_vehiculo.setVisibility(View.GONE);
        //Activar el diseño deseado
        verVehiculo.setVisibility(View.VISIBLE);
        visualizarVehiculo();
    }


    public void aniadirVehiculo() {
        int c = 0;
        String placa_str;
        EditText placa_ad = mainView.findViewById(R.id.placa_aniadir_etxt);
        //TODO REEEMPLAZAR POR UN UNICO MENSAJE DE ERROR

        if(foto==null){
            c++;
            Toast.makeText(mainView.getContext(), "No se ha escogido una imagen", Toast.LENGTH_SHORT).show();
        }
        if (isEmpty(placa_ad)) {
            Toast.makeText(mainView.getContext(), "Campo de placa vacio", Toast.LENGTH_SHORT).show();
            c++;
        } else if (placa_ad.getText().toString().length() != 8) {
            Toast.makeText(mainView.getContext(), "Placa no válida", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText matricula_ad = mainView.findViewById(R.id.aniadir_matricula_etxt);
        /*if(isEmpty(matricula_ad)){
            Toast.makeText(mainView.getContext(),"Campo de matricula vacio", Toast.LENGTH_SHORT).show();
            c++;
        }else if(matricula_ad.getText().toString().length() != 8){
            Toast.makeText(mainView.getContext(),"Matricula no válida", Toast.LENGTH_SHORT).show();
            c++;
        }*/

        EditText anio_ad = mainView.findViewById(R.id.aniadir_anio_etxt);
        if (isEmpty(anio_ad)) {
            Toast.makeText(mainView.getContext(), "Campo de anio vacio", Toast.LENGTH_SHORT).show();
            c++;
        } else if (Integer.parseInt(anio_ad.getText().toString()) < 1900 || Integer.parseInt(anio_ad.getText().toString()) > 2021) {
            Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
            anio_ad.setText("");
            c++;
        }

        EditText descripcion_ad = mainView.findViewById(R.id.aniadir_descripcion_etxt);
        if (isEmpty(descripcion_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de descripcion vacia", Toast.LENGTH_SHORT).show();
        }

        EditText marca_ad = mainView.findViewById(R.id.aniadir_marca_etxt);
        if (isEmpty(marca_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de marca vacio", Toast.LENGTH_SHORT).show();
        }

        EditText modelo_ad = mainView.findViewById(R.id.aniadir_modelo_etxt);
        if (isEmpty(modelo_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de modelo vacio", Toast.LENGTH_SHORT).show();
        }

        EditText color_ad = mainView.findViewById(R.id.aniadir_color_etxt);
        if (isEmpty(color_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de color vacio", Toast.LENGTH_SHORT).show();
        }

        EditText pinicial_ad = mainView.findViewById(R.id.aniadir_pinicial_etxt);
        if (isEmpty(pinicial_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de precio inicial vacio", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(pinicial_ad.getText().toString()) <= 0) {
            c++;
            Toast.makeText(mainView.getContext(), "Valor incial invalido", Toast.LENGTH_SHORT).show();
        }

        EditText pventa_ad = mainView.findViewById(R.id.aniadir_precio_venta_etxt);
        if (isEmpty(pventa_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de valor de venta vacio", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(pventa_ad.getText().toString()) <= 0) {
            c++;
            Toast.makeText(mainView.getContext(), "Valor de venta invalido", Toast.LENGTH_SHORT).show();
        }
        EditText ppromocion_ad = mainView.findViewById(R.id.aniadir_precio_promocion_etxt);
        if (isEmpty(ppromocion_ad)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de promoción inicial vacio", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(pventa_ad.getText().toString()) < 0) {
            c++;
            Toast.makeText(mainView.getContext(), "Valor de promoción invalido", Toast.LENGTH_SHORT).show();
        }
        CheckBox matriculado_ad = mainView.findViewById(R.id.matricula_chkbox);


        if (c == 0) {
            placa_str = placa_ad.getText().toString();
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
            patio.aniadirVehiculo(nuevo);
            try {
                if (patio.getVehiculos().contiene(nuevo)) {
                    Toast.makeText(mainView.getContext(), "Se agrego correctamente el vehiculo", Toast.LENGTH_SHORT).show();
                    irCatalogo();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    }

    public void editarVehiculo() {
        int c = 0;

        EditText placa_ed = mainView.findViewById(R.id.editar_placa_etxt);
        if (isEmpty(placa_ed)) {
            Toast.makeText(mainView.getContext(), "Campo de placa vacio", Toast.LENGTH_SHORT).show();
            c++;
        } else if (placa_ed.getText().toString().length() != 8) {
            Toast.makeText(mainView.getContext(), "Placa no válida", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText matricula_ed = mainView.findViewById(R.id.editar_matricula_etxt);
            /*if(isEmpty(matricula_ed)){
                Toast.makeText(mainView.getContext(),"Campo de matricula vacio", Toast.LENGTH_SHORT).show();
                c++;
            }else if(matricula_ed.getText().toString().length() != 8){
                Toast.makeText(mainView.getContext(),"Matricula no válida", Toast.LENGTH_SHORT).show();
                c++;
            }*/

        EditText anio_ed = mainView.findViewById(R.id.editar_vehiculo_anio_etxt);
        if (isEmpty(anio_ed)) {
            Toast.makeText(mainView.getContext(), "Campo de anio vacio", Toast.LENGTH_SHORT).show();
            c++;
        } else if (Integer.parseInt(anio_ed.getText().toString()) < 1900 || Integer.parseInt(anio_ed.getText().toString()) > 2021) {
            Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
            anio_ed.setText("");
            c++;
        }

        EditText descripcion_ed = mainView.findViewById(R.id.editar_descripcion_etxt);
        if (isEmpty(descripcion_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de descripcion vacia", Toast.LENGTH_SHORT).show();
        }

        EditText marca_ed = mainView.findViewById(R.id.editar_vehiculo_marca_etxt);
        if (isEmpty(marca_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de marca vacio", Toast.LENGTH_SHORT).show();
        }
        EditText modelo_ed = mainView.findViewById(R.id.editar_vehiculo_modelo_etxt);
        if (isEmpty(modelo_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de modelo vacio", Toast.LENGTH_SHORT).show();
        }
        EditText color_ed = mainView.findViewById(R.id.editar_vehiculo_color_etxt);
        if (isEmpty(color_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de color vacio", Toast.LENGTH_SHORT).show();
        }
        EditText pinicial_ed = mainView.findViewById(R.id.editar_v_pinicial_etxt);
        if (isEmpty(pinicial_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de precio inicial vacio", Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(pinicial_ed.getText().toString()) <= 0) {
            c++;
            Toast.makeText(mainView.getContext(), "Valor incial invalido", Toast.LENGTH_SHORT).show();
        }

        EditText pventa_ed = mainView.findViewById(R.id.editar_v_pventa_etxt);
        if (isEmpty(pventa_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de promoción inicial vacio", Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(pventa_ed.getText().toString()) < 0) {
            c++;
            Toast.makeText(mainView.getContext(), "Valor de promoción invalido", Toast.LENGTH_SHORT).show();
        }
        EditText ppromocion_ed = mainView.findViewById(R.id.editar_v_ppromocion_etxt);
        if (isEmpty(ppromocion_ed)) {
            c++;
            Toast.makeText(mainView.getContext(), "Campo de promoción inicial vacio", Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(ppromocion_ed.getText().toString()) < 0) {
            c++;
            Toast.makeText(mainView.getContext(), "Valor de promoción invalido", Toast.LENGTH_SHORT).show();
        }
        CheckBox matriculado_ed = mainView.findViewById(R.id.editar_matriculado_chkbox);
        if (c == 0) {
            String placa_n = placa_ed.getText().toString();
            if (foto != null) {
                StorageReference filePath = mStorageRef.child("Vehiculos").child(placa_n + ".jpg");
                filePath.putFile(foto);
            }
            vMostrar.actualizarDatos(
                    placa_n,
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
            try {
                if(patio.buscarVehiculos("Placa",placa_n)!=null){
                    Toast.makeText(mainView.getContext(), "Se edito correctamente el auto", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "Error 2", Toast.LENGTH_SHORT).show();
            }

        }
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
            e.printStackTrace();
        }
        String matriculado_str;
        if (vMostrar.isMatriculado()) {
            matriculado_str = "Si";
        } else {
            matriculado_str = "No";
        }
        matriculado.setText(matriculado_str);
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
    public void itemClick(String placa){
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            irVer();
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
            adptadorlistaview.buscar(newText,busqueda_placa.getQueryHint().toString());
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
