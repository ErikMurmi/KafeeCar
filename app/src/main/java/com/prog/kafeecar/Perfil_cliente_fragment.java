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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Button regresar;

    //Imagenes
    private ImageButton perfil_img;
    private ImageView perfil_imagen;
    //Layouts
    private LinearLayout verperfil;
    private LinearLayout editarperfil;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.perfil_cliente, container, false);
        iniciaciones();



        perfil_img.setOnClickListener(v ->{
            openGalery();
        });

        editar.setOnClickListener(v ->{
            try{
                verperfil.setVisibility(View.GONE);
                editarperfil.setVisibility(View.VISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        });


        guardar.setOnClickListener(v ->{
            try{
                editarCliente();
            } catch (Exception e){
                Toast.makeText(mainview.getContext(), "No se pudo editar el perfil", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        regresar.setOnClickListener(v ->{
            try{
                editarperfil.setVisibility(View.GONE);
                verperfil.setVisibility(View.VISIBLE);
                verperfilCliente();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        return mainview;
    }

    public void iniciaciones(){
        //Botones
        editar = mainview.findViewById(R.id.editar_Cliente_btn);
        guardar = mainview.findViewById(R.id.botonEditListoCliente_btn);
        regresar = mainview.findViewById(R.id.botonEditDeshacerCliente_btn);
        //Layouts
        verperfil = mainview.findViewById(R.id.visualizar_cliente_lyt);
        editarperfil = mainview.findViewById(R.id.editar_cliente_lyt);
        //Imagenes
        perfil_img = mainview.findViewById(R.id.imagenPerfilEditCliente_ibtn);
        perfil_imagen = mainview.findViewById(R.id.imagen_perfil_cliente_img);
        verperfilCliente();
    }


    public void verperfilCliente(){
        try{
            TextView nombre = mainview.findViewById(R.id.nombre_cliente_txt);
            TextView fecha = mainview.findViewById(R.id.fecha_nacimiento_cliente_txt);
            TextView cedula = mainview.findViewById(R.id.cedula_cliente_txt);
            TextView telefono = mainview.findViewById(R.id.telefono_cliente_txt);
            TextView correo = mainview.findViewById(R.id.correo_cliente_txt);
            String fechanacimiento = Patioventainterfaz.getFechaMod(cliente.getFechaNacimiento());
            nombre.setText(cliente.getNombre());
            fecha.setText(fechanacimiento);
            cedula.setText(cliente.getCedula());
            telefono.setText(cliente.getTelefono());
            correo.setText(cliente.getCorreo());

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


    public void editarCliente() throws ParseException {

        EditText nombreCliente;
        EditText cedulaCliente;
        EditText diaNacimientoCliente;
        EditText mesNacimientoCliente;
        EditText anioNacimientoCliente;
        EditText telefonoCliente;
        EditText correoCliente;
        String contraseniaCliente_str = cliente.getClave();
        int c = 0;

        nombreCliente = mainview.findViewById(R.id.nombreEditCliente_etxt);
        String nombreCampoCliente_str = nombreCliente.getText().toString();
        if(nombreCampoCliente_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Nombre*", Toast.LENGTH_SHORT).show();
            c++;
        }

        cedulaCliente = mainview.findViewById(R.id.cedulaEditCliente_etxt);
        String cedulaCliente_str = cedulaCliente.getText().toString();
        if(cedulaCliente_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Cédula*", Toast.LENGTH_SHORT).show();
            c++;
        }else{
            if(cedulaCliente_str.length()!=10){
                Toast.makeText(mainview.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cedulaCliente.setText("");
                c++;
            }
        }

        anioNacimientoCliente = mainview.findViewById(R.id.anioNacimientoEditCliente_etxt);
        String anio_str = anioNacimientoCliente.getText().toString();
        int anio = -1;
        if(anio_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Año*", Toast.LENGTH_SHORT).show();
            c++;
        }else{
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(mainview.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                anioNacimientoCliente.setText("");
                c++;
            }
        }

        mesNacimientoCliente = mainview.findViewById(R.id.mesNacimientoEditCliente_etxt);
        String mes_str = mesNacimientoCliente.getText().toString();
        int mes = -1;
        if(mes_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Mes*", Toast.LENGTH_SHORT).show();
            c++;
        }else{
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainview.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                mesNacimientoCliente.setText("");
                c++;
            }
        }

        diaNacimientoCliente = mainview.findViewById(R.id.diaNacimientoEditCliente_etxt);
        String dia_str = diaNacimientoCliente.getText().toString();
        int dia = -1;
        if(dia_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Día*", Toast.LENGTH_SHORT).show();
            c++;
        }else{
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainview.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                diaNacimientoCliente.setText("");
                c++;
            }
        }

        telefonoCliente = mainview.findViewById(R.id.telefonoEditCliente_etxt);
        String telefonoCliente_str = telefonoCliente.getText().toString();
        if(telefonoCliente_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Teléfono*", Toast.LENGTH_SHORT).show();
            c++;
        }else{
            if (telefonoCliente_str.length() != 10) {
                Toast.makeText(mainview.getContext(), "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                telefonoCliente.setText("");
                c++;
            }
        }

        correoCliente = mainview.findViewById(R.id.correoEditCliente_etxt);
        String correoCliente_str = correoCliente.getText().toString();
        if(correoCliente_str.isEmpty()){
            Toast.makeText(mainview.getContext(), "Campo vacío: *Correo*", Toast.LENGTH_SHORT).show();
            c++;
        }else{
            if (!Patioventainterfaz.validarMail(correoCliente_str)) {
                Toast.makeText(mainview.getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
                correoCliente.setText("");
                c++;
            }
        }


        if(foto == null){
            Toast.makeText(mainview.getContext(), "No se ha escogido una imagen", Toast.LENGTH_SHORT).show();
            c++;
        }

        if (c == 0) {
            String fecha = dia_str + "-" + mes_str + "-" + anio_str;
            cliente.cambiarDatos(nombreCampoCliente_str,cedulaCliente_str,telefonoCliente_str,correoCliente_str,contraseniaCliente_str,fecha);
            cliente.setImagen(String.format("%s.jpg",cedulaCliente.getText().toString()));
            try {
                if (patio.buscarClientes("Cedula", cliente.getCedula()) != null) {
                    Toast.makeText(mainview.getContext(), "Se edito el cliente correctamente", Toast.LENGTH_SHORT).show();
                    volverpantallaprincipal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

   public void visualizarperfileditable_Cliente()
    {
        TextView textonombre;
        TextView textotelefono;
        TextView textocedula;
        TextView textofecha;
        TextView textocorreo;


        textonombre = mainview.findViewById(R.id.nombre_cliente_txt);
        textotelefono = mainview.findViewById(R.id.telefono_cliente_txt);
        textocedula = mainview.findViewById(R.id.cedula_cliente_txt);
        textofecha = mainview.findViewById(R.id.fecha_nacimiento_cliente_txt);
        textocorreo = mainview.findViewById(R.id.correo_cliente_txt);


        textonombre.setText(cliente.getNombre());
        textotelefono.setText(cliente.getTelefono());
        textocedula.setText(cliente.getCedula());
        String fecha = Patioventainterfaz.getFechaMod(cliente.getFechaNacimiento());
        textofecha.setText(fecha);
        textocorreo.setText(cliente.getCorreo());
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
                perfil_img.setImageURI(foto);
            }else{
                Toast.makeText(mainview.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
