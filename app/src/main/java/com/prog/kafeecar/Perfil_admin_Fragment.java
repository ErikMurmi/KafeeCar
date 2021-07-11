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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Perfil_admin_Fragment extends Fragment {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    public final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private View mainview;
    private PatioVenta patio;
    private Vendedor user = (Vendedor) Patioventainterfaz.usuarioActual;
    private Uri foto = null;
    //Layouts
    private ScrollView perfil_lyt;
    private LinearLayout editar_perfil_lyt;
    private LinearLayout perfil_btns_lyt;
    //Imagenes
    private ImageView perfil_img;
    //Botones
    private Button cancelar;
    private Button irEditar;
    private Button guardar_editar;
    //Botones con imagen
    private ImageButton admin_img_btn;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainview = inflater.inflate(R.layout.perfil_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        initViews();
        verPerfil();

        irEditar.setOnClickListener(v -> {
            perfil_lyt.setVisibility(View.GONE);
            perfil_btns_lyt.setVisibility(View.VISIBLE);
            editar_perfil_lyt.setVisibility(View.VISIBLE);
            verPerfilEditable();
        });
        /*guardar_editar.setOnClickListener(v -> {

        });*/
        cancelar.setOnClickListener(v -> salirsinGuardar());
        admin_img_btn.setOnClickListener(v -> openGalery());

        guardar_editar.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainview.getContext());
            msg.setTitle("Editar Perfil");
            msg.setMessage("¿Estás seguro editar los datos de su perfil?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> editarAdmin());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();

        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (editar_perfil_lyt.getVisibility() == View.VISIBLE) {
                    salirsinGuardar();
                }
                //Intent myIntent = new Intent(nombreClase.this,activityDestiny.class);
            }
        };
        return mainview;
    }

    public void initViews() {
        //Layouts
        perfil_btns_lyt = mainview.findViewById(R.id.visualizar_admin_btns);
        perfil_lyt = mainview.findViewById(R.id.visualizar_admin_lyt);
        editar_perfil_lyt = mainview.findViewById(R.id.perfil_admin_ed_lyt);
        //Imagenes
        perfil_img = mainview.findViewById(R.id.admin_img);
        //Botones
        admin_img_btn = mainview.findViewById(R.id.admin_img_btn);
        cancelar = mainview.findViewById(R.id.cancelar_edit_admin_btn);
        irEditar = mainview.findViewById(R.id.ireditar_admin_btn);
        guardar_editar = mainview.findViewById(R.id.ed_pe_ven_btn);

    }

    public void salirsinGuardar() {
        AlertDialog.Builder msg = new AlertDialog.Builder(mainview.getContext());
        msg.setTitle("NO GUARDAR");
        msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
        msg.setPositiveButton("Aceptar", (dialog, which) -> {
            perfil_btns_lyt.setVisibility(View.GONE);
            perfil_lyt.setVisibility(View.VISIBLE);
            editar_perfil_lyt.setVisibility(View.GONE);
        });
        msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        msg.show();
    }

    public void irVerPerfil() {
        perfil_btns_lyt.setVisibility(View.GONE);
        perfil_lyt.setVisibility(View.VISIBLE);
        editar_perfil_lyt.setVisibility(View.GONE);
    }

    public void verPerfil() {
        try {
            TextView nombre_ed = mainview.findViewById(R.id.nombre_admin_txt);
            TextView fecha_ed = mainview.findViewById(R.id.fecha_admin_txt);
            TextView cedula_ed = mainview.findViewById(R.id.cedula_admin_txt);
            TextView telefono_ed = mainview.findViewById(R.id.telefono_admin_txt);
            TextView correo_ed = mainview.findViewById(R.id.correo_admin_txt);
            String fechaNacimiento = Patioventainterfaz.getFechaMod(user.getFechaNacimiento());
            nombre_ed.setText(user.getNombre());
            fecha_ed.setText(fechaNacimiento);
            cedula_ed.setText(user.getCedula());
            telefono_ed.setText(user.getTelefono());
            correo_ed.setText(user.getCorreo());
            StorageReference filePath = mStorageRef.child("Vendedores/" + user.getImagen());
            try {
                final File localFile = File.createTempFile(user.getImagen(), "jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    perfil_img.setImageBitmap(bitmap);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(mainview.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void verPerfilEditable() {
        try {
            EditText nombre_ed = mainview.findViewById(R.id.nombre_admin_etxt);
            EditText dia_ed = mainview.findViewById(R.id.diaNacimiento_admin_etxt);
            EditText mes_ed = mainview.findViewById(R.id.mesNacimiento_admin_etxt);
            EditText anio_ed = mainview.findViewById(R.id.anioNacimiento_admin_etxt);
            EditText cedula_ed = mainview.findViewById(R.id.cedula_admin_etxt);
            EditText telefono_ed = mainview.findViewById(R.id.telefono_admin_etxt);
            EditText correo_ed = mainview.findViewById(R.id.correo_admin_etxt);
            /*EditText contrasenia_ed = mainview.findViewById(R.id.contrasenia_admin_etxt);
            EditText confirm_c_ed = mainview.findViewById(R.id.contrasenia_confirm_admin_etxt);*/

            String fechaNacimiento = Patioventainterfaz.getFechaMod(user.getFechaNacimiento());
            String dia = fechaNacimiento.split("-")[0];
            String mes = fechaNacimiento.split("-")[1];
            String anio = fechaNacimiento.split("-")[2];

            nombre_ed.setText(user.getNombre());
            dia_ed.setText(dia);
            mes_ed.setText(mes);
            anio_ed.setText(anio);
            cedula_ed.setText(user.getCedula());
            telefono_ed.setText(user.getTelefono());
            correo_ed.setText(user.getCorreo());

            StorageReference filePath = mStorageRef.child("Vendedores/" + user.getImagen());
            try {
                final File localFile = File.createTempFile(user.getImagen(), "jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        admin_img_btn.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Toast.makeText(mainview.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarAdmin() {
        boolean cambiar_clave = false;
        int c = 0;
        EditText nombre_ed = mainview.findViewById(R.id.nombre_pe_vn_etxt);
        String nombre_str = nombre_ed.getText().toString();
        if (nombre_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Nombre*", Toast.LENGTH_SHORT).show();
            c++;
        }

        EditText cedula_ed = mainview.findViewById(R.id.cedula_pe_vn_etxt);
        String cedula_str = cedula_ed.getText().toString();
        if (cedula_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Cédula*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (cedula_str.length() != 10) {
                Toast.makeText(mainview.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cedula_ed.setText("");
            }
        }


        EditText anio_ed = mainview.findViewById(R.id.anioNacimiento_admin_etxt);
        String anio_str = anio_ed.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Año*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            anio = Integer.parseInt(anio_ed.getText().toString());
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(mainview.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                anio_ed.setText("");
                c++;
            }
        }

        EditText mes_ed = mainview.findViewById(R.id.mesNacimiento_admin_etxt);
        String mes_str = mes_ed.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Mes*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainview.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                mes_ed.setText("");
                c++;
            }
        }

        EditText dia_ed = mainview.findViewById(R.id.diaNacimiento_admin_etxt);
        String dia_str = dia_ed.getText().toString();
        int dia;
        if (dia_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Día*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainview.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                dia_ed.setText("");
                c++;
            }
        }


        EditText telefono_ed = mainview.findViewById(R.id.telefono_admin_etxt);
        String telefono_str = telefono_ed.getText().toString();
        if (telefono_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Teléfono*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (telefono_str.length() != 10) {
                Toast.makeText(mainview.getContext(), "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                telefono_ed.setText("");
                c++;
            }
        }

        EditText correo_ed = mainview.findViewById(R.id.correo_admin_etxt);
        String correo_str = correo_ed.getText().toString();
        if (correo_str.isEmpty()) {
            Toast.makeText(mainview.getContext(), "Campo vacío: *Correo*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (!Patioventainterfaz.validarMail(correo_str)) {
                Toast.makeText(mainview.getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
                correo_ed.setText("");
                c++;
            }
        }

        EditText textoclave = mainview.findViewById(R.id.contrasenia_admin_etxt);
        String clave_str = textoclave.getText().toString();
        EditText textorepetirclave = mainview.findViewById(R.id.contrasenia_confirm_admin_etxt);
        String repetirclave_str = textorepetirclave.getText().toString();
        if (!clave_str.isEmpty() && !repetirclave_str.isEmpty()) {
            if ((clave_str.compareTo(repetirclave_str) != 0)) {
                Toast.makeText(mainview.getContext(), "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                textoclave.setText("");
                textorepetirclave.setText("");
                c++;
            } else {
                cambiar_clave = true;
            }
        }

        if (c == 0) {
            String fecha = dia_str + "-" + mes_str + "-" + anio_str;
            try {
                if (cambiar_clave) {
                    user.cambiarDatosVendedor(
                            nombre_ed.getText().toString(),
                            cedula_ed.getText().toString(),
                            telefono_ed.getText().toString(),
                            correo_ed.getText().toString(),
                            fecha,
                            clave_str);
                } else {
                    user.cambiarDatosVendedor(
                            nombre_ed.getText().toString(),
                            cedula_ed.getText().toString(),
                            telefono_ed.getText().toString(),
                            correo_ed.getText().toString(),
                            fecha,
                            user.getClave());
                }
                if (foto != null) {
                    StorageReference filePath = mStorageRef.child("Vendedores").child(cedula_str + ".jpg");
                    filePath.putFile(foto);
                }
                user.setImagen(String.format("%s.jpg", cedula_str));
                if (patio.buscarVendedores("Cedula", cedula_str) != null) {
                    Toast.makeText(mainview.getContext(), "Se actualizaron los datos correctamente", Toast.LENGTH_SHORT).show();
                    irVerPerfil();
                    verPerfil();
                }
            } catch (Exception e) {
                Toast.makeText(mainview.getContext(), "Se actualizaron los datos correctamente", Toast.LENGTH_SHORT).show();
            }

        }

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
                admin_img_btn.setImageURI(foto);
            } else {
                Toast.makeText(mainview.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
