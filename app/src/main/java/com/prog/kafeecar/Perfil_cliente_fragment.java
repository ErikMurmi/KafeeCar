package com.prog.kafeecar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Perfil_cliente_fragment extends Fragment{
    private View mainview;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private PatioVenta patio = Patioventainterfaz.patioventa;
    private Uri foto;

    //Botones
    private Button editar;
    private Button guardar;
    private Button salirsinguardar;
    private Button cerrarsesion;

    //Imagenes
    private ImageButton perfil_img_btn;
    private ImageView perfil_imagen;
    //Layouts
    private ScrollView verperfil;
    private LinearLayout editarperfil;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.perfil_cliente, container, false);
        iniciaciones();

        perfil_img_btn.setOnClickListener(v ->{
            openGalery();
        });

        editar.setOnClickListener(v ->{
            try{
                visualizarperfileditable_Cliente();
                verperfil.setVisibility(View.GONE);
                editarperfil.setVisibility(View.VISIBLE);
            } catch (Exception e){
                Toast.makeText(mainview.getContext(), "Error 276: no se pudo visualizar el perfil ", Toast.LENGTH_SHORT).show();
            }
        });

        guardar.setOnClickListener(v -> {
            try {
                AlertDialog.Builder msg = new AlertDialog.Builder(mainview.getContext());
                msg.setTitle("Editar Perfil");
                msg.setMessage("¿Estás seguro editar los datos de su perfil?");
                msg.setPositiveButton("Aceptar", (dialog, which) -> editarCliente());
                msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                msg.show();
            } catch (Exception e) {
                Toast.makeText(mainview.getContext(), "Error 277: no se pudo editar el perfil", Toast.LENGTH_SHORT).show();
            }
        });

        salirsinguardar.setOnClickListener(v -> {
            try {
                salirsinGuardar();
                verperfilCliente();
                } catch (Exception e) {
                 Toast.makeText(mainview.getContext(), "Error 278: no se pudo ver el perfil", Toast.LENGTH_SHORT).show();
                }
            });

        //Metodo para el control del boton atras
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (editarperfil.getVisibility() == View.VISIBLE) {
                    editarperfil.setVisibility(View.GONE);
                    verperfil.setVisibility(View.VISIBLE);
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return mainview;
        }
   

    public void iniciaciones() {
        //Botones
        editar = mainview.findViewById(R.id.editar_Cliente_btn);
        guardar = mainview.findViewById(R.id.botonEditListoCliente_btn);
        salirsinguardar = mainview.findViewById(R.id.botonEditDeshacerCliente_btn);
        //Layouts
        verperfil = mainview.findViewById(R.id.visualizar_cliente_lyt);
        editarperfil = mainview.findViewById(R.id.editar_cliente_lyt);
        //Imagenes
        perfil_img_btn = mainview.findViewById(R.id.imagenPerfilEditCliente_ibtn);
        perfil_imagen = mainview.findViewById(R.id.imagen_perfil_cliente_img);
        verperfilCliente();
    }


    public void salirsinGuardar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(mainview.getContext());
        msg.setTitle("NO GUARDAR");
        msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
        msg.setPositiveButton("Aceptar", (dialog, which) -> {
            verperfil.setVisibility(View.VISIBLE);
            editarperfil.setVisibility(View.GONE);
        });
        msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        msg.show();
    }

    public void verperfilCliente(){
        try{
            TextView nombre_pe_ci_txt = mainview.findViewById(R.id.nombre_cliente_txt);
            TextView fecha_pe_ci_txt = mainview.findViewById(R.id.fecha_nacimiento_cliente_txt);
            TextView cedula_pe_ci_txt = mainview.findViewById(R.id.cedula_cliente_txt);
            TextView telefono_pe_ci_txt = mainview.findViewById(R.id.telefono_cliente_txt);
            TextView correo_pe_ci_txt = mainview.findViewById(R.id.correo_cliente_txt);
            String fechanacimiento_pe_ci_str = Patioventainterfaz.getFechaMod(cliente.getFechaNacimiento());
            nombre_pe_ci_txt.setText(cliente.getNombre());
            fecha_pe_ci_txt.setText(fechanacimiento_pe_ci_str);
            cedula_pe_ci_txt.setText(cliente.getCedula());
            telefono_pe_ci_txt.setText(cliente.getTelefono());
            correo_pe_ci_txt.setText(cliente.getCorreo());

            StorageReference filePath = mStorageRef.child("Clientes/"+cliente.getImagen());
            Glide.with(mainview)
                    .load(filePath)
                    .into(perfil_imagen);
            try {
                final File localFile = File.createTempFile(cliente.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        perfil_imagen.setImageBitmap(bitmap);
                    }
                });
            }catch (IOException e) {
                Toast.makeText(mainview.getContext(), "Error 279: no se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            Toast.makeText(mainview.getContext(), "Error 280: no se pudo mostrar la informacion", Toast.LENGTH_SHORT).show();
        }
    }


    public void editarCliente(){
        try {
            int c = 0;

            EditText nombre_pe_cli_etxt = mainview.findViewById(R.id.nombreEditCliente_etxt);
            String nombre_str = nombre_pe_cli_etxt.getText().toString();

            if(nombre_str.isEmpty()){
                c++;
            }

            EditText anio_pe_cli_etxt = mainview.findViewById(R.id.anioNacimientoEditCliente_etxt);
            String anio_str = anio_pe_cli_etxt.getText().toString();
            int anio = Integer.parseInt(anio_str);
            if(anio_str.isEmpty()){
                c++;
            }else{
                if (anio < 1900 || anio > 2003) {
                    anio_pe_cli_etxt.setText("");
                    c++;
                }
            }
            
            EditText mes_pe_cli_etxt = mainview.findViewById(R.id.mesNacimientoEditCliente_etxt);
            String mes_str = mes_pe_cli_etxt.getText().toString();
            int mes = Integer.parseInt(mes_str);
            if(mes_str.isEmpty()){
                c++;
            }else{
                if (mes < 1 || mes > 12) {
                    mes_pe_cli_etxt.setText("");
                    c++;
                }
            }

            EditText dia_pe_cli_etxt = mainview.findViewById(R.id.diaNacimientoEditCliente_etxt);
            String dia_str = dia_pe_cli_etxt.getText().toString();
            int dia = Integer.parseInt(dia_str);
            if(dia_str.isEmpty()){
                c++;
            }else{
                if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                    dia_pe_cli_etxt.setText("");
                    c++;
                }
            }

            EditText cedula_pe_cli_etxt = mainview.findViewById(R.id.cedulaEditCliente_etxt);
            String cedula_str = cedula_pe_cli_etxt.getText().toString();
            if(cedula_str.isEmpty()){
                c++;
            }else{
                if(cedula_str.length()!=10){
                    Toast.makeText(mainview.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                    cedula_pe_cli_etxt.setText("");
                }
            }

            EditText telefono_pe_cli_etxt = mainview.findViewById(R.id.telefonoEditCliente_etxt);
            String telefono_str = telefono_pe_cli_etxt.getText().toString();
            if(telefono_str.isEmpty()){
                c++;
            }else{
                if (telefono_str.length() != 10) {
                    Toast.makeText(mainview.getContext(), "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                    telefono_pe_cli_etxt.setText("");
                    c++;
                }
            }

            EditText correo_pe_cli_etxt = mainview.findViewById(R.id.correoEditCliente_etxt);
            String correo_str = correo_pe_cli_etxt.getText().toString();
            if(correo_str.isEmpty()){
                c++;
            }else{
                if (!Patioventainterfaz.validarMail(correo_str)) {
                    Toast.makeText(mainview.getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
                    correo_pe_cli_etxt.setText("");
                    c++;
                }
            }

            EditText contrasenia_pe_cli_etxt = mainview.findViewById(R.id.contraseniaEditCliente_etxt);
            String contrasenia_str = contrasenia_pe_cli_etxt.getText().toString();
            EditText confirmarcontrasenia_pe_cli_etxt = mainview.findViewById(R.id.confirmarcontraseniaEditCliente_etxt);
            String confirmarcontrasenia_str = confirmarcontrasenia_pe_cli_etxt.getText().toString();
            String contra = null;
            if(!contrasenia_str.isEmpty() && !confirmarcontrasenia_str.isEmpty()){
                if ( (contrasenia_str.compareTo(confirmarcontrasenia_str) != 0)) {

                    Toast.makeText(mainview.getContext(), "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                    contrasenia_pe_cli_etxt.setText("");
                    confirmarcontrasenia_pe_cli_etxt.setText("");
                    c++;
                }else{
                    if(contrasenia_str.compareTo(cliente.getClave())==0){
                        contra = cliente.getClave();
                    }else{
                        contra = contrasenia_str;
                    }
                }
            }

            if (c == 0) {
                String fecha = dia_str + "-" + mes_str + "-" + anio_str;
                    cliente.cambiarDatos(
                            nombre_pe_cli_etxt.getText().toString(),
                            cedula_pe_cli_etxt.getText().toString(),
                            telefono_pe_cli_etxt.getText().toString(),
                            correo_pe_cli_etxt.getText().toString(),
                            contra,fecha);
                if(foto!=null){
                    StorageReference filePath = mStorageRef.child("Clientes").child(cedula_str+".jpg");
                    filePath.putFile(foto);
                }
                else
                {
                    cliente.setImagen(String.format("%s.jpg",cedula_pe_cli_etxt.getText().toString()));
                }
                cliente.setImagen(String.format("%s.jpg",cedula_str));
                if(patio.buscarClientes("Cedula", cedula_str) != null) {
                    Toast.makeText(mainview.getContext(), "Se actualizaron los datos correctamente", Toast.LENGTH_SHORT).show();
                    irVerPerfilCliente();
                    verperfilCliente();
                }
            }
            else
            {
                Toast.makeText(mainview.getContext(), "Campos Vacios", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(mainview.getContext(), "Error 281: no se pudo actualizar la informaicon del perfil", Toast.LENGTH_SHORT).show();
        }
    }

    public void irVerPerfilCliente(){
        editarperfil.setVisibility(View.GONE);
        verperfil.setVisibility(View.VISIBLE);
    }

   public void visualizarperfileditable_Cliente()
    {
        EditText nombre_pe_ci_etxt;
        EditText telefono_pe_ci_etxt;
        EditText cedula_pe_ci_etxt;
        EditText dia_pe_ci_etxt;
        EditText mes_pe_ci_etxt;
        EditText anio_pe_ci_etxt;
        EditText correo_pe_ci_etxt;

        nombre_pe_ci_etxt = mainview.findViewById(R.id.nombreEditCliente_etxt);
        telefono_pe_ci_etxt = mainview.findViewById(R.id.telefonoEditCliente_etxt);
        cedula_pe_ci_etxt = mainview.findViewById(R.id.cedulaEditCliente_etxt);
        correo_pe_ci_etxt = mainview.findViewById(R.id.correoEditCliente_etxt);
        dia_pe_ci_etxt = mainview.findViewById(R.id.diaNacimientoEditCliente_etxt);
        mes_pe_ci_etxt = mainview.findViewById(R.id.mesNacimientoEditCliente_etxt);
        anio_pe_ci_etxt = mainview.findViewById(R.id.anioNacimientoEditCliente_etxt);

        nombre_pe_ci_etxt.setText(cliente.getNombre());
        telefono_pe_ci_etxt.setText(cliente.getTelefono());
        cedula_pe_ci_etxt.setText(cliente.getCedula());
        String fecha = Patioventainterfaz.getFechaMod(cliente.getFechaNacimiento());
        String dia = fecha.split("-")[0];
        String mes = fecha.split("-")[1];
        String anio = fecha.split("-")[2];
        dia_pe_ci_etxt.setText(dia);
        mes_pe_ci_etxt.setText(mes);
        anio_pe_ci_etxt.setText(anio);
        correo_pe_ci_etxt.setText(cliente.getCorreo());

        StorageReference filePath = mStorageRef.child("Clientes/"+cliente.getImagen());
        Glide.with(mainview)
                .load(filePath)
                .into(perfil_img_btn);
        try {
            final File localFile = File.createTempFile(cliente.getImagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    perfil_img_btn.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e) {
            Toast.makeText(mainview.getContext(), "Error 282:no se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
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
                perfil_img_btn.setImageURI(foto);
            }else{
                Toast.makeText(mainview.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
