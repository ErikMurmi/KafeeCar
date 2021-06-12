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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vendedores_Admin_Fragment extends Fragment {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private PatioVenta patio;
    Vendedor venMostrar;

    private View mainView;

    private EditText cedulaVendedorE;

    private ImageView imagenPerfil_img;

    private ImageView imagenPerfilV_img;
    private ImageView imagenPerfilV1_img;

    private LinearLayout irRegistrarVendedor;
    private LinearLayout irVisualizarVendedor;
    private LinearLayout irAdministrarVendedor;
    private LinearLayout irEditarVendedor;

    //lyts de los vendedores en la lista
    private LinearLayout verVendedor_lyt;
    private LinearLayout verVendedor1_lyt;


    private FloatingActionButton aniadirVendedor_btn;
    private Button deshabilitar_btn;
    private Button editar_btn;
    private Button listo_btn;
    private Button editarlisto_btn;
    private Button editarDeshacer_btn;
    private Button cancelar_btn;
    private ImageButton buscarCedulaVendedor_btn;
    private ImageButton imagenPerfilVendedor_btn;
    private ImageButton imagenPerfilVendedorEdit_btn;

    private Uri foto;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.vendedor_admin,container,false);
        patio = Patioventainterfaz.patioventa;
        verListaVendedores("1732221032", "1721835213");

        cedulaVendedorE = mainView.findViewById(R.id.cedulaEditVendedor_etxt);

        //botones
        aniadirVendedor_btn = mainView.findViewById(R.id.boton_mas_admin_btn);
        deshabilitar_btn = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        editar_btn = mainView.findViewById(R.id.editar_vendedor_btn);
        listo_btn = mainView.findViewById(R.id.botonListo_btn);
        editarDeshacer_btn = mainView.findViewById(R.id.botonEditDeshacerVendedor_btn);
        editarlisto_btn = mainView.findViewById(R.id.botonEditListo_btn);
        cancelar_btn = mainView.findViewById(R.id.botonCancelarVendedores_btn);

        buscarCedulaVendedor_btn = mainView.findViewById(R.id.busquedaCedulaVendedor_btn2);
        imagenPerfilVendedor_btn = mainView.findViewById(R.id.imagenPerfilVendedor_ibtn);
        imagenPerfilVendedorEdit_btn = mainView.findViewById(R.id.imagenPerfilEditVendedor_ibtn);

        imagenPerfil_img = mainView.findViewById(R.id.imagen_perfil_vendedor_img);


        //declaracion de los lyts de los vendedores en la lista
        verVendedor_lyt = mainView.findViewById(R.id.AVvendedor_lyt);
        verVendedor1_lyt = mainView.findViewById(R.id.AVvendedor2_lyt);

        //layouts
        irRegistrarVendedor = mainView.findViewById(R.id.registrar_vendedor_lyt);
        irVisualizarVendedor = mainView.findViewById(R.id.visualizar_vendedor_lyt);
        irAdministrarVendedor = mainView.findViewById(R.id.administrar_vendedor_lyt);
        irEditarVendedor = mainView.findViewById(R.id.editar_vendedor_lyt);

        aniadirVendedor_btn.setOnClickListener(v -> {
            //Desactivar otros diseños
            irVisualizarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            //Activar el diseño deseado
            irRegistrarVendedor.setVisibility(View.VISIBLE);

        });

        verVendedor_lyt.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.VISIBLE);
            try {
                visualizarVendedor("1732221032");
            }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se pudo realizar la peticion deseada", Toast.LENGTH_SHORT).show();
        }
        });

        verVendedor1_lyt.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.VISIBLE);
            try {
                visualizarVendedor("1721835213");
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo realizar la peticion deseada", Toast.LENGTH_SHORT).show();
            }
        });

        editar_btn.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            //verVendedorEditable(cedulaVendedorE.getText().toString());
            verVendedorEditable();
            irEditarVendedor.setVisibility(View.VISIBLE);
        });

        deshabilitar_btn.setOnClickListener(v -> {
            try {
                TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
                Vendedor vendedor = patio.buscarVendedores("Cedula", cedula.getText().toString());
                if (deshabilitar_btn.getText().toString().compareToIgnoreCase("Deshabilitar") == 0) {
                    Toast.makeText(mainView.getContext(), "¡ADVETENCIA: ESTE USUARIO SE DESHABILITARÁ DEL SISTEMA!", Toast.LENGTH_SHORT).show();
                    vendedor.setActivo(false);
                    deshabilitar_btn.setText("Habilitar");
                } else {
                    deshabilitar_btn.setText("Deshabilitar");
                    Toast.makeText(mainView.getContext(), "¡ADVETENCIA: ESTE USUARIO SE HABILITARÁ EN EL SISTEMA!", Toast.LENGTH_SHORT).show();
                    vendedor.setActivo(true);
                }
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo ejecutar la petición", Toast.LENGTH_SHORT).show();
            }
        });

        listo_btn.setOnClickListener(v -> {
            try{
                registrarVendedor();
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo añadir el vendedor", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }
        });

        cancelar_btn.setOnClickListener(v -> {
            regresarPantallaPrncipal();
            Toast.makeText(mainView.getContext(), "Se canceló la acción", Toast.LENGTH_SHORT).show();
        });

        editarlisto_btn.setOnClickListener(v -> {
            try{
                editarVendedor();
                Toast.makeText(mainView.getContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }
        });


        editarDeshacer_btn.setOnClickListener(v -> {
            try{
                irRegistrarVendedor.setVisibility(View.GONE);
                irAdministrarVendedor.setVisibility(View.GONE);
                aniadirVendedor_btn.setVisibility(View.GONE);
                irEditarVendedor.setVisibility(View.GONE);

                irVisualizarVendedor.setVisibility(View.VISIBLE);
                visualizarVendedor(cedulaVendedorE.getText().toString());
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo realizar la peticion deseada", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }
        });

        imagenPerfilVendedor_btn.setOnClickListener(v -> {
            openGalery();
        });

        buscarCedulaVendedor_btn.setOnClickListener(view -> {
            buscarVendedores();
        });

        return mainView;
    }

    public void verListaVendedores(String cedula, String cedula1){
        try{
            String am = "am";
            imagenPerfilV_img = mainView.findViewById(R.id.AVimagenPerfil1_img);
            imagenPerfilV1_img = mainView.findViewById(R.id.AVimagenPerfil2_img);
            Vendedor v_Mostrar = patio.buscarVendedores("Cedula",cedula);
            Vendedor v_Mostrar1 = patio.buscarVendedores("Cedula",cedula1);

            TextView nombreV = mainView.findViewById(R.id.AVnombre_txt);
            TextView entradaV = mainView.findViewById(R.id.AVhoraEntrada_txt);
            TextView almuerzoV = mainView.findViewById(R.id.AVhoraAlmuerzo_txt);
            TextView salidaV = mainView.findViewById(R.id.AVhoraSalida_txt);

            TextView nombreV1 = mainView.findViewById(R.id.AVnombre1_txt);
            TextView entradaV1 = mainView.findViewById(R.id.AVhoraEntrada1_txt);
            TextView almuerzoV1 = mainView.findViewById(R.id.AVhoraAlmuerzo1_txt);
            TextView salidaV1 = mainView.findViewById(R.id.AVhoraSalida1_txt);

            nombreV.setText(v_Mostrar.getNombre());
            entradaV.setText(String.format("%d:00 %s",v_Mostrar.getHoraEntrada(),formatoHora(v_Mostrar.getHoraEntrada())));
            almuerzoV.setText(String.format("%d:00 %s",v_Mostrar.getHoraComida(),formatoHora(v_Mostrar.getHoraComida())));
            salidaV.setText(String.format("%d:00 %s",v_Mostrar.getHoraSalida(),formatoHora(v_Mostrar.getHoraSalida())));

            nombreV1.setText(v_Mostrar1.getNombre());
            entradaV1.setText(String.format("%d:00 %s",v_Mostrar1.getHoraEntrada(),formatoHora(v_Mostrar1.getHoraEntrada())));
            almuerzoV1.setText(String.format("%d:00 %s",v_Mostrar1.getHoraComida(),formatoHora(v_Mostrar1.getHoraComida())));
            salidaV1.setText(String.format("%d:00 %s",v_Mostrar1.getHoraSalida(),formatoHora(v_Mostrar1.getHoraSalida())));

            StorageReference filePath = mStorageRef.child("Vendedores/"+v_Mostrar.getImagen());
            Glide.with(mainView)
                    .load(filePath)
                    .into(imagenPerfilV_img);
            try {
                final File localFile = File.createTempFile(v_Mostrar.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imagenPerfilV_img.setImageBitmap(bitmap);

                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
            filePath = mStorageRef.child("Vendedores/"+v_Mostrar1.getImagen());
            Glide.with(mainView)
                    .load(filePath)
                    .into(imagenPerfilV1_img);
            try {
                final File localFile = File.createTempFile(v_Mostrar1.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imagenPerfilV1_img.setImageBitmap(bitmap);

                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public String formatoHora(int hora){
        if(hora>12){
            return "pm";
        }
        return "am";
    }

    public void regresarPantallaPrncipal(){
        verListaVendedores("1732221032", "1721835213");
        irRegistrarVendedor.setVisibility(View.GONE);
        irVisualizarVendedor.setVisibility(View.GONE);
        irEditarVendedor.setVisibility(View.GONE);
        //boton y pantalla visible
        aniadirVendedor_btn.setVisibility(View.VISIBLE);
        irAdministrarVendedor.setVisibility(View.VISIBLE);
    }

    public void registrarVendedor() throws ParseException {

        EditText nombreVendedor;
        EditText apellidoVendedor;
        EditText cedulaVendedor;
        EditText diaNacimientoVendedor;
        EditText mesNacimientoVendedor;
        EditText anioNacimientoVendedor;
        EditText telefonoVendedor;
        EditText correoVendedor;
        EditText contraseniaVendedor;
        EditText confirmarContraseniaVendedor;
        EditText horaEntradaVendedor;
        EditText horaSalidaVendedor;
        EditText horaAlmuerzoVendedor;
        int c =0;

        nombreVendedor = mainView.findViewById(R.id.nombreVendedor_etxt);
        apellidoVendedor = mainView.findViewById(R.id.apellidoVendedor_etxt);
        String nombreVendedor_str = nombreVendedor.getText().toString() + "" + apellidoVendedor.getText().toString();

        cedulaVendedor = mainView.findViewById(R.id.cedulaVendedor_etxt);
        String cedulaVendedor_str = cedulaVendedor.getText().toString();
        if(cedulaVendedor_str.length()!=10){
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cedulaVendedor.setText("");
        }

        anioNacimientoVendedor = mainView.findViewById(R.id.anioNacimientoVendedor_etxt);
        String anio_str = anioNacimientoVendedor.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if (anio < 1900 || anio > 2003) {
            Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
            anioNacimientoVendedor.setText("");
            c++;
        }

        mesNacimientoVendedor = mainView.findViewById(R.id.mesNacimientoVendedor_etxt);
        String mes_str = mesNacimientoVendedor.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if (mes < 1 || mes > 12) {
            Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
            mesNacimientoVendedor.setText("");
            c++;
        }

        diaNacimientoVendedor = mainView.findViewById(R.id.diaNacimientoVendedor_etxt);
        String dia_str = diaNacimientoVendedor.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
            Toast.makeText(mainView.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
            diaNacimientoVendedor.setText("");
            c++;
        }

        telefonoVendedor = mainView.findViewById(R.id.telefonoVendedor_etxt);
        String telefonoVendedor_str = telefonoVendedor.getText().toString();
        if (telefonoVendedor_str.length() != 10) {
            Toast.makeText(mainView.getContext(), "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
            telefonoVendedor.setText("");
            c++;
        }

        correoVendedor = mainView.findViewById(R.id.correoVendedor_etxt);
        String correoVendedor_str = correoVendedor.getText().toString();
        if (!Patioventainterfaz.validarMail(correoVendedor_str)) {
            Toast.makeText(mainView.getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
            correoVendedor.setText("");
            c++;
        }

        contraseniaVendedor = mainView.findViewById(R.id.contraseniaVendedor_etxt);
        confirmarContraseniaVendedor = mainView.findViewById(R.id.confirmarContraseniaVendedor_etxt);
        String contraseniaVendedor_str = contraseniaVendedor.getText().toString();
        String confirmarContraseniaVendedor_str = confirmarContraseniaVendedor.getText().toString();
        if (contraseniaVendedor_str.compareTo(confirmarContraseniaVendedor_str) != 0) {
            Toast.makeText(mainView.getContext(), "Las claves no coinciden", Toast.LENGTH_SHORT).show();
            contraseniaVendedor.setText("");
            confirmarContraseniaVendedor.setText("");
            c++;
        }

        horaEntradaVendedor = mainView.findViewById(R.id.horaEntradaVendedor_etxt);
        int horaEntradaVendedor_int = Integer.parseInt(horaEntradaVendedor.getText().toString());
        if(horaEntradaVendedor_int >0 && horaEntradaVendedor_int < 24){
            Toast.makeText(mainView.getContext(), "Hora de entrada inválida", Toast.LENGTH_SHORT).show();
            horaEntradaVendedor.setText("");
            c++;
        }

        horaAlmuerzoVendedor = mainView.findViewById(R.id.horaAlmuerzoVendedor_etxt);
        int horaAlmuerzoVendedor_int = Integer.parseInt(horaAlmuerzoVendedor.getText().toString());
        if(horaAlmuerzoVendedor_int >0 && horaAlmuerzoVendedor_int < 24){
            Toast.makeText(mainView.getContext(), "Hora de almuerzo inválida", Toast.LENGTH_SHORT).show();
            horaAlmuerzoVendedor.setText("");
            c++;
        }

        horaSalidaVendedor = mainView.findViewById(R.id.horaSalidaVendedor_etxt);
        int horaSalidaVendedor_int = Integer.parseInt(horaSalidaVendedor.getText().toString());
        if(horaSalidaVendedor_int >0 && horaSalidaVendedor_int < 24){
            Toast.makeText(mainView.getContext(), "Hora de salida inválida", Toast.LENGTH_SHORT).show();
            horaSalidaVendedor.setText("");
            c++;
        }

        String contraseniaVerificada = contraseniaVendedor_str;

        if (c == 0) {
            Date fecha = null;
            try {
                fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Vendedor vendedor = new Vendedor(
                    String.format("%s.jpg",cedulaVendedor_str),
                    horaEntradaVendedor_int,
                    horaSalidaVendedor_int,
                    horaAlmuerzoVendedor_int,
                    patio,
                    nombreVendedor_str,
                    cedulaVendedor_str,
                    telefonoVendedor_str,
                    correoVendedor_str,
                    contraseniaVerificada,
                    sdf.parse(String.valueOf(fecha)));
            patio.aniadirUsuario(vendedor,"Vendedor");
            try {
                if (patio.buscarClientes("Cedula", vendedor.getCedula()) != null) {
                    Toast.makeText(mainView.getContext(), "Se añadió el vendedor correctamente", Toast.LENGTH_SHORT).show();
                    regresarPantallaPrncipal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        StorageReference filePath = mStorageRef.child("Vendedores").child(cedulaVendedor.getText().toString()+"_img");
        filePath.putFile(foto).addOnSuccessListener(taskSnapshot ->
                Toast.makeText(mainView.getContext(), "Imagen subida satisfactoriamente",Toast.LENGTH_SHORT).show());
    }

    public void visualizarVendedor(String ced) throws Exception {

        TextView nombre = mainView.findViewById(R.id.nombre_vendedor_txt);
        TextView fechaNacimiento = mainView.findViewById(R.id.fecha_nacimiento_vendedor_txt);
        TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
        TextView telefono = mainView.findViewById(R.id.telefono_vendedor_txt);
        TextView correo = mainView.findViewById(R.id.correo_vendedor_txt);
        TextView entrada = mainView.findViewById(R.id.entrada_vendedor_txt);
        TextView almuerzo = mainView.findViewById(R.id.almuerzo_vendedor_txt);
        TextView salida = mainView.findViewById(R.id.salida_vendedor_txt);

        venMostrar = patio.buscarVendedores("Cedula",ced);
        nombre.setText(venMostrar.getNombre());
        fechaNacimiento.setText(Patioventainterfaz.getFechaMod(venMostrar.getFechaNacimiento()));
        cedula.setText(venMostrar.getCedula());
        telefono.setText(venMostrar.getTelefono());
        correo.setText(venMostrar.getCorreo());
        entrada.setText(String.format("%d:00 %s",venMostrar.getHoraEntrada(),formatoHora(venMostrar.getHoraEntrada())));
        almuerzo.setText(String.format("%d:00 %s",venMostrar.getHoraComida(),formatoHora(venMostrar.getHoraComida())));
        salida.setText(String.format("%d:00 %s",venMostrar.getHoraSalida(),formatoHora(venMostrar.getHoraSalida())));

        Button habilitar = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        if (venMostrar.getActivo()) {
            habilitar.setText("Deshabilitar");
        } else {
            habilitar.setText("Habilitar");
        }
        StorageReference filePath = mStorageRef.child("Vendedores/"+venMostrar.getImagen());
        Glide.with(mainView)
                .load(filePath)
                .into(imagenPerfilVendedor_btn);
        try {
            final File localFile = File.createTempFile(venMostrar.getImagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imagenPerfil_img.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verVendedorEditable(){
        try{
            Vendedor cedulaVen = venMostrar;
            EditText nombre_ed = mainView.findViewById(R.id.nombreEditVendedor_etxt);
            EditText dia_ed = mainView.findViewById(R.id.diaNacimientoEditVendedor_etxt);
            EditText mes_ed = mainView.findViewById(R.id.mesNacimientoEditVendedor_etxt);
            EditText anio_ed = mainView.findViewById(R.id.anioNacimientoEditVendedor_etxt);
            EditText cedula_ed = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            EditText telefono_ed = mainView.findViewById(R.id.telefonoEditVendedor_etxt);
            EditText correo_ed = mainView.findViewById(R.id.correoEditVendedor_etxt);
            EditText entrada_ed = mainView.findViewById(R.id.horaEntradaEditVendedor_etxt);
            EditText almuerzo_ed = mainView.findViewById(R.id.horaAlmuerzoEditVendedor_etxt);
            EditText salida_ed = mainView.findViewById(R.id.horaSalidaEditVendedor_etxt);

            String fechaNacimiento = Patioventainterfaz.getFechaMod(cedulaVen.getFechaNacimiento());
            String dia = fechaNacimiento.split("-")[0];
            String mes = fechaNacimiento.split("-")[1];
            String anio = fechaNacimiento.split("-")[2];

            nombre_ed.setText(cedulaVen.getNombre());
            dia_ed.setText(dia);
            mes_ed.setText(mes);
            anio_ed.setText(anio);
            cedula_ed.setText(cedulaVen.getCedula());
            telefono_ed.setText(cedulaVen.getTelefono());
            correo_ed.setText(cedulaVen.getCorreo());
            entrada_ed.setText(String.valueOf(cedulaVen.getHoraEntrada()));
            almuerzo_ed.setText(String.valueOf(cedulaVen.getHoraComida()));
            salida_ed.setText(String.valueOf(cedulaVen.getHoraSalida()));

            StorageReference filePath = mStorageRef.child("Vendedores/"+cedulaVen.getImagen());
            Glide.with(mainView)
                    .load(filePath)
                    .into(imagenPerfilVendedorEdit_btn);
            try {
                final File localFile = File.createTempFile(cedulaVen.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imagenPerfilVendedorEdit_btn.setImageBitmap(bitmap);
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarVendedor(){
        try {
            Vendedor cedulaVen = venMostrar;
            EditText nombre_ed = mainView.findViewById(R.id.nombreEditVendedor_etxt);
            EditText dia_ed = mainView.findViewById(R.id.diaNacimientoEditVendedor_etxt);
            EditText mes_ed = mainView.findViewById(R.id.mesNacimientoEditVendedor_etxt);
            EditText anio_ed = mainView.findViewById(R.id.anioNacimientoEditVendedor_etxt);
            EditText cedula_ed = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            EditText telefono_ed = mainView.findViewById(R.id.telefonoEditVendedor_etxt);
            EditText correo_ed = mainView.findViewById(R.id.correoEditVendedor_etxt);

            String fechaNacimientoVendedor= dia_ed.getText().toString()
                    + "-" + mes_ed.getText().toString()
                    + "-" + anio_ed.getText().toString();

            cedulaVen.cambiarDatosSinClave(
                    nombre_ed.getText().toString(),
                    cedula_ed.getText().toString(),
                    telefono_ed.getText().toString(),
                    correo_ed.getText().toString(),
                    fechaNacimientoVendedor);
            cedulaVen.setImagen(String.format("%s.jpg",cedula_ed.getText().toString()));

        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void buscarVendedores (){
        EditText cedula= mainView.findViewById(R.id.busquedaCedulaVendedor_etxt2);
        String cedula_str= cedula.getText().toString();
        Vendedor buscado=null;
        try {
         buscado = patio.buscarVendedores("Cedula", cedula_str);
         if(buscado==null){
             Toast.makeText(mainView.getContext(), "No existe el vendedor buscado", Toast.LENGTH_SHORT).show();
         }else{
             verListaVendedores(cedula_str,"1721835213");
             verVendedor1_lyt.setVisibility(View.GONE);
         }
        }catch(Exception e){
        Toast.makeText(mainView.getContext(), "No existen vendedores", Toast.LENGTH_SHORT).show();
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
                imagenPerfilVendedor_btn.setImageURI(foto);
            }else{
                Toast.makeText(mainView.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
