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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
                e.printStackTrace();
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
                Toast.makeText(mainview.getContext(), "No se pudo editar el perfil", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        salirsinguardar.setOnClickListener(v -> {
            try {
                salirsinGuardar();
                verperfilCliente();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

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
                e.printStackTrace();
            }
        }
        catch(Exception e){
            Toast.makeText(mainview.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }


    public void editarCliente(){
        try {
            boolean cambiar_clave = false;
            int c = 0;

            EditText nombre_pe_cli_etxt = mainview.findViewById(R.id.nombreEditCliente_etxt);
            String nombre_str = nombre_pe_cli_etxt.getText().toString();

            if(nombre_str.isEmpty()){
                Toast.makeText(mainview.getContext(), "Campo vacío: *Nombre*", Toast.LENGTH_SHORT).show();
                c++;
            }

            EditText anio_pe_cli_etxt = mainview.findViewById(R.id.anioNacimientoEditCliente_etxt);
            String anio_str = anio_pe_cli_etxt.getText().toString();
            int anio = Integer.parseInt(anio_str);
            if(anio_str.isEmpty()){
                Toast.makeText(mainview.getContext(), "Campo vacío: *Año*", Toast.LENGTH_SHORT).show();
                c++;
            }else{
                if (anio < 1900 || anio > 2003) {
                    Toast.makeText(mainview.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                    anio_pe_cli_etxt.setText("");
                    c++;
                }
            }
            
            EditText mes_pe_cli_etxt = mainview.findViewById(R.id.mesNacimientoEditCliente_etxt);
            String mes_str = mes_pe_cli_etxt.getText().toString();
            int mes = Integer.parseInt(mes_str);
            if(mes_str.isEmpty()){
                Toast.makeText(mainview.getContext(), "Campo vacío: *Mes*", Toast.LENGTH_SHORT).show();
                c++;
            }else{
                if (mes < 1 || mes > 12) {
                    Toast.makeText(mainview.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                    mes_pe_cli_etxt.setText("");
                    c++;
                }
            }

            EditText dia_pe_cli_etxt = mainview.findViewById(R.id.diaNacimientoEditCliente_etxt);
            String dia_str = dia_pe_cli_etxt.getText().toString();
            int dia = Integer.parseInt(dia_str);
            if(dia_str.isEmpty()){
                Toast.makeText(mainview.getContext(), "Campo vacío: *Día*", Toast.LENGTH_SHORT).show();
                c++;
            }else{
                if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                    Toast.makeText(mainview.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                    dia_pe_cli_etxt.setText("");
                    c++;
                }
            }

            EditText cedula_pe_cli_etxt = mainview.findViewById(R.id.cedulaEditCliente_etxt);
            String cedula_str = cedula_pe_cli_etxt.getText().toString();
            if(cedula_str.isEmpty()){
                Toast.makeText(mainview.getContext(), "Campo vacío: *Cédula*", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mainview.getContext(), "Campo vacío: *Teléfono*", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mainview.getContext(), "Campo vacío: *Correo*", Toast.LENGTH_SHORT).show();
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
            if(!contrasenia_str.isEmpty() && !confirmarcontrasenia_str.isEmpty()){
                if ( (contrasenia_str.compareTo(confirmarcontrasenia_str) != 0)) {
                    Toast.makeText(mainview.getContext(), "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                    contrasenia_pe_cli_etxt.setText("");
                    confirmarcontrasenia_pe_cli_etxt.setText("");
                    c++;
                }else{
                    cambiar_clave = true;
                }
            }

            if (c == 0) {
                String fecha = dia_str + "-" + mes_str + "-" + anio_str;
                if(cambiar_clave) {
                    cliente.cambiarDatos(
                            nombre_pe_cli_etxt.getText().toString(),
                            cedula_pe_cli_etxt.getText().toString(),
                            telefono_pe_cli_etxt.getText().toString(),
                            correo_pe_cli_etxt.getText().toString(),
                            fecha, contrasenia_str);
                }else{
                    cliente.cambiarDatos(
                            nombre_pe_cli_etxt.getText().toString(),
                            cedula_pe_cli_etxt.getText().toString(),
                            telefono_pe_cli_etxt.getText().toString(),
                            correo_pe_cli_etxt.getText().toString(),
                            fecha, cliente.getClave());
                }
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
        }
        catch (Exception e){
            Toast.makeText(mainview.getContext(), "No se pudo actualizar la información del perfil", Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
        }

    }


    public void openGalery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }


    public void volverpantallaprincipal(){
        editarperfil.setVisibility(View.GONE);
        verperfil.setVisibility(View.VISIBLE);
        verperfilCliente();
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
