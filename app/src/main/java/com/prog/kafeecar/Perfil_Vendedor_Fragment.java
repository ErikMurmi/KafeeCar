package com.prog.kafeecar;

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

public class Perfil_Vendedor_Fragment extends Fragment {


    private static final int REQUEST_IMAGE_GALERY = 101;
    private String TAG = "Perfil vendedor";
    private View mainview;
    private Uri foto;
    private Vendedor user = (Vendedor) Patioventainterfaz.usuarioActual;

    private PatioVenta patio;
    //Layouts
    private LinearLayout perfil_lyt;
    private LinearLayout editar_perfil_lyt;
    private LinearLayout perfil_btns_lyt;


    //Imagenes
    private ImageView perfil_img;
    //Botones
    private Button cancelar;
    private Button irEditar;

    //Botones con imagen
    private ImageButton admin_img_btn;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.perfil_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;
        initViews();
        verPerfil();
        return mainview;
    }

    public void initViews(){
        //Layouts
        perfil_btns_lyt = mainview.findViewById(R.id.visualizar_ven_btns);
        perfil_lyt = mainview.findViewById(R.id.visualizar_ven_lyt);
        editar_perfil_lyt = mainview.findViewById(R.id.perfil_vn_lyt);
        //Imagenes
        perfil_img = mainview.findViewById(R.id.ven_img);
        //Botones
        admin_img_btn = mainview.findViewById(R.id.vendedor_pe_vn_btn);
        cancelar = mainview.findViewById(R.id.cancelar_edit_pe_vn_lyt);
        irEditar = mainview.findViewById(R.id.ireditar_admin_btn);

    }

    public void verPerfil(){
        try{
            TextView nombre_ed = mainview.findViewById(R.id.nombre_pe_vn_txt);
            TextView fecha_ed = mainview.findViewById(R.id.fecha_pe_vn_txt);
            TextView cedula_ed = mainview.findViewById(R.id.cedula_pe_vn_txt);
            TextView telefono_ed = mainview.findViewById(R.id.telefono_cedula_pe_vn_txt);
            TextView correo_ed = mainview.findViewById(R.id.correo_cedula_pe_vn_txt);
            String fechaNacimiento = Patioventainterfaz.getFechaMod(user.getFechaNacimiento());
            nombre_ed.setText(user.getNombre());
            fecha_ed.setText(fechaNacimiento);
            cedula_ed.setText(user.getCedula());
            telefono_ed.setText(user.getTelefono());
            correo_ed.setText(user.getCorreo());
            StorageReference filePath = mStorageRef.child("Vendedores/"+user.getImagen());
            Glide.with(mainview)
                    .load(filePath)
                    .into(perfil_img);
            try {
                final File localFile = File.createTempFile(user.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        perfil_img.setImageBitmap(bitmap);
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            Toast.makeText(mainview.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    public void verPerfilEditable(){
        try{
            EditText nombre_ed = mainview.findViewById(R.id.nombre_admin_etxt);
            EditText dia_ed = mainview.findViewById(R.id.diaNacimiento_admin_etxt);
            EditText mes_ed = mainview.findViewById(R.id.mesNacimiento_admin_etxt);
            EditText anio_ed = mainview.findViewById(R.id.anioNacimiento_admin_etxt);
            EditText cedula_ed = mainview.findViewById(R.id.cedula_admin_etxt);
            EditText telefono_ed = mainview.findViewById(R.id.telefono_admin_etxt);
            EditText correo_ed = mainview.findViewById(R.id.correo_admin_etxt);
            EditText contrasenia_ed = mainview.findViewById(R.id.contrasenia_admin_etxt);
            EditText confirm_c_ed = mainview.findViewById(R.id.contrasenia_confirm_admin_etxt);

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

            StorageReference filePath = mStorageRef.child("Vendedores/"+user.getImagen());
            Glide.with(mainview)
                    .load(filePath)
                    .into(admin_img_btn);
            try {
                final File localFile = File.createTempFile(user.getImagen(),"jpg");
                filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        admin_img_btn.setImageBitmap(bitmap);
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            Toast.makeText(mainview.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }*/
}
