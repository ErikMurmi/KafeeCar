package com.prog.kafeecar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patioventainterfaz extends AppCompatActivity {

    public static PatioVenta patioventa;
    public static Cliente listaFav=new Cliente();
    public static Usuario usuarioActual = null;
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    public static final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Uri foto;
    public static Boolean CITA_CON_VEHICULO = false;
    private ImageButton reg_img;
    public static Vehiculo v_aux_cita;
    private BottomNavigationView navBar;
    public static final String[] filtros_vehiculos = new String[]{"Marca","Modelo","Placa","Año","Color","Precio"};
    public static final String[] anios = new String[]{"2021","2022"};
    public static final String[] meses = new String[]{"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
    public static final int[] diasLista = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};

    public static Lista vehiculos_orignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Login normal
        //setContentView(R.layout.login);

        // Login de pruebas
        patioventa = new PatioVenta();
        setContentView(R.layout.login_sinclaves);
        try {
            cargarDatos();
        } catch (Exception e) {
            Toast.makeText(Patioventainterfaz.this, "Error 301: Datos no quemados", Toast.LENGTH_SHORT).show();
        }

        if (patioventa.getAdministrador() == null) {
            setContentView(R.layout.registrar_admin_lyt);
            reg_img = findViewById(R.id.reg_imagen_admin_btn);
            Button reg_list = findViewById(R.id.reg_list_btn);
            reg_list.setOnClickListener(v -> registrarAdministrador());
        }
    }

    public static void cargarDatos() throws Exception {
        patioventa.aniadirVehiculo(new Vehiculo("PSD-1234", "Y3553", "Mercedes", "GLB SUV", "Gris", "Tiene 5 años de uso", 70000, 95000, 80000, true, 2016, "PSD-1234.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("GHC-2434", "I3748", "Chevrolet", "Cruze", "Blanco", "Muestra un choque en la parte lateral", 17000, 25000, 18500, true, 2014, "GHC-2434.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IMH-2233", "O8394", "Hyundai", "Elantra", "Rojo", "Automático", 14000, 15000, 0, false, 2013, "IMH-2233.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("PHG-1225", "382I83", "Ford", "EcoSport", "Negro", "Exelente estado documentación y matrícula al día", 14000, 15800, 0, true, 2014, "PHG-1225.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("PCR-1832", "38J382", "Chevrolet", "Optra", "Negro", "EL dueño lo vende por viaje", 5000, 6000, 0, true, 2004, "PCR-1832.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("PDF-7894", "J43569", "Toyota", "Fortuner", "Negro", "SUV deportivo", 75000, 76000, 70000, true, 2021, "PDF-7894.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("ADA-9578", "M55789", "Ford", "Explorer", "Blanco", "SUV deportivo", 85000, 84000, 80000, true, 2020, "ADA-9578.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("DCD-4879", "J36978", "Tesla", "Model x", "Blanco", "SUV deportivo", 65000, 66000, 64000, true, 2021, "DCD-4879.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("WER-1236", "T19478", "Toyota", "BZ4X", "Gris", "SUV deportivo", 55000, 76000, 70000, true, 2020, "WER-1236.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("XAB-8541", "C76948", "Toyota", "Venza", "Azul", "SUV deportivo", 77000, 76000, 70000, true, 2020, "XAB-8541.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IOT-3687", "K89457", "Peugeot", "3008", "Negro", "SUV deportivo", 78000, 77000, 70000, true, 2020, "IOT-3687.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("GUN-1764", "283I32", "Chevrolet", "Sail", "Negro", "Cahsis de sedán y mecaanismo manual ", 13000, 16000, 0, true, 2019, "GUN-1764.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IPO-1963", "23JW22", "Jeep", "Compass Sport", "Gris", "Autómático poco uso", 70000, 95000, 80000, true, 2017, "IPO-1963.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IOS-1275", "M382N3", "Hyundai", "Tucson", "Gris", "Vidrios electricos radio de pantalla", 17000, 20300, 0, true, 2015, "IOS-1275.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("HPO-2517", "JD8382", "Chevrolet", "DMAX Optima", "Gris", "Camioneta una cavina", 14500, 16500, 16000, true, 2013, "HPO-2517.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("SGD-0916", "D3828E", "Hyundai", "HD270", "Blanca", "Volqueta para trabajo", 40000, 42000, 41500, true, 2011, "SGD-0916.jpg"));

        Vendedor admin = new Vendedor("1721053207.jpg", 8, 17, 13, patioventa, "Juan Jácome", "1721053207", "0987654321", "juanj@gmail.com", "clave", sdf.parse("05-06-2003"));
        //patioventa.aniadirUsuario(new Vendedor("1721053207.jpg",8, 17, 13, patioventa, "Juan Jácome", "1721053207", "1721053207", "juanj@gmail.com", "clave", sdf.parse("05-06-2006")), "Vendedor");
        patioventa.aniadirUsuario(admin, "Administrador");
        patioventa.aniadirUsuario((new Vendedor("1732221032.jpg", 8, 17, 13, patioventa, "Elizabeth Perez", "1732221032", "1721053207", "eli.perez@gmail.com", "Spe123", sdf.parse("09-05-2000"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor("1721835213.jpg", 8, 17, 13, patioventa, "David Montalvo", "1721835213", "1721053207", "david_m@gmail.com", "DM12pc", sdf.parse("19-02-2001"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor("1928364726.jpg", 8, 17, 13, patioventa, "Luiz Velasquez", "1928364726", "1721053207", "luisvelasquesz@outlook.es", "super1015", sdf.parse("12-01-1990"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor("0923837273.jpg", 8, 17, 13, patioventa, "Jessica Alvarez", "1721053207", "0923837273", "jessyesperanza@gmail.com", "0912jessy", sdf.parse("08-4-2001"))), "Vendedor");
        patioventa.aniadirUsuario(new Cliente("Daniel", "1750140489", "0999548928", "daniel@gmail.com", "1207", sdf.parse("08-4-2001"), "175014048.jpg"), "Cliente");
        patioventa.aniadirUsuario(new Cliente("Erik", "1750115623", "0999548928", "erik@gmail.com", "1207", sdf.parse("08-4-2001"), "1750115623.jpg"), "Cliente");
        patioventa.aniadirUsuario(new Cliente("Diana","1750115233", "0995648998", "diana@gmail.com", "1207", sdf.parse("08-4-2001"), "1750115233.jpg"), "Cliente");
        Cliente c = (Cliente) patioventa.getClientes().getPos(1);
        //c.aniadirFavorito("IPO-1963");
        Date fechaCita = sdf.parse("08-9-2021");
        Date fechaCita1 = sdf.parse("08-10-2021");
        Date fechaCita2 = sdf.parse("01-9-2021");
        Date fechaCita3 = sdf.parse("02-7-2021");
        Date fechaCita4 = sdf.parse("12-7-2021");
        Date fechaCita5 = sdf.parse("21-7-2021");
        Venta ven1 = new Venta(fechaCita, (Cliente) patioventa.getClientes().getPos(0), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1),45000.50f);
        patioventa.aniadirVenta(ven1);
        Venta ven2 = new Venta(fechaCita1, (Cliente) patioventa.getClientes().getPos(0), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1),12000.50f);
        patioventa.aniadirVenta(ven2);
        Venta ven3 = new Venta(fechaCita2,  (Cliente) patioventa.getClientes().getPos(0), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1),54000.50f);
        patioventa.aniadirVenta(ven3);
        Venta ven4 = new Venta(fechaCita3, (Cliente) patioventa.getClientes().getPos(0), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1),67000.50f);
        patioventa.aniadirVenta(ven4);
        Venta ven5 = new Venta(fechaCita4, (Cliente) patioventa.getClientes().getPos(0), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1),80000.50f);
        patioventa.aniadirVenta(ven5);
        Venta ven6 = new Venta(fechaCita5, (Cliente) patioventa.getClientes().getPos(0), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1),90000.50f);
        patioventa.aniadirVenta(ven6);
        Cita c1 = new Cita(fechaCita, 8, "", (Cliente) patioventa.getClientes().getPos(0), patioventa.getAdministrador(), (Vehiculo) patioventa.getVehiculos().getPos(1));
        Cita c2 = new Cita(fechaCita, 9, "", (Cliente) patioventa.getClientes().getPos(0), patioventa.getAdministrador(), (Vehiculo) patioventa.getVehiculos().getPos(2));
        Cita c3 = new Cita(fechaCita2, 16, "", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(3));
        Cita c4 = new Cita(fechaCita3, 9, "", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(4));
        Cita c5 = new Cita(fechaCita4, 12, "", (Cliente) patioventa.getClientes().getPos(2), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(5));
        Cita c6 = new Cita(fechaCita5, 8, "", (Cliente) patioventa.getClientes().getPos(2), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(6));
        patioventa.aniadirCita(c1);
        patioventa.aniadirCita(c2);
        patioventa.aniadirCita(c3);
        patioventa.aniadirCita(c4);
        patioventa.aniadirCita(c5);
        patioventa.aniadirCita(c6);
        Cliente temp = (Cliente)patioventa.getClientes().getPos(1);
        temp.aniadirFavorito((((Vehiculo) patioventa.getVehiculos().getPos(1)).getPlaca()));
    }

    private void irAplicacion(String tipo) {
        Window w = getWindow();
        w.setStatusBarColor(0);
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (tipo.compareTo("ADMIN") == 0) {
            setContentView(R.layout.home_admin);
            navBar= findViewById(R.id.barra_nav);
            navBar.setOnNavigationItemSelectedListener(navListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Catalogo_Admin_Fragment()).commit();
        } else if (tipo.compareTo("VENDEDOR") == 0) {
            setContentView(R.layout.home_vendedor);
            navBar = findViewById(R.id.barra_nav_vendedor);
            navBar.setOnNavigationItemSelectedListener(nav_vendedor_Listener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor_ven, new Catalogo_Vendedor_Fragment()).commit();
        } else if (tipo.compareTo("CLIENTE") == 0) {
            setContentView(R.layout.home_cliente);
            navBar = findViewById(R.id.barra_nav_cliente);
            navBar.setOnNavigationItemSelectedListener(nav_cliente_Listener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Catalogo_Cliente_fragment()).commit();
        }
    }

    public void irRegistarCitaVehiculoVendedor(View v) {
        try {
            CITA_CON_VEHICULO = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor_ven, new Citas_vendedor_fragment()).commit();
            navBar.setSelectedItemId(R.id.nav_citas_ven);
        } catch (Exception e) {
            Toast.makeText(Patioventainterfaz.this, "Error 302: No se puede ejecutar la acción", Toast.LENGTH_SHORT).show();
        }
    }

    public void irRegistarCitaVehiculo(View v) {
        try {
            CITA_CON_VEHICULO = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Citas_Admin_Fragment()).commit();
            navBar.setSelectedItemId(R.id.nav_citas);
        } catch (Exception e) {
            Toast.makeText(Patioventainterfaz.this, "Error 303: No se puede ejecutar la acción", Toast.LENGTH_SHORT).show();
        }
    }

    public void irAdmin(View v) {
        usuarioActual = patioventa.getAdministrador();
        irAplicacion("ADMIN");
    }

    public void irVendedor(View v){
        usuarioActual = (Usuario) patioventa.getVendedores().getPos(1);
        irAplicacion("VENDEDOR");
    }

    public void irCliente(View v){
        usuarioActual = (Usuario) patioventa.getClientes().getPos(1);
        irAplicacion("CLIENTE");
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragement = null;
                switch (item.getItemId()) {
                    case R.id.nav_citas:
                        selectedFragement = new Citas_Admin_Fragment();
                        //vehiculos_orignal = patioventa.getVehiculos();
                        break;
                    case R.id.nav_cat:
                        selectedFragement = new Catalogo_Admin_Fragment();
                        break;
                    case R.id.nav_vendedores:
                        selectedFragement = new Vendedores_Admin_Fragment();
                        break;
                    case R.id.nav_admin_perfil:
                        selectedFragement = new Perfil_admin_Fragment();
                        break;
                    case R.id.nav_ventas:
                        selectedFragement = new Ventas_admin_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, selectedFragement).commit();
                return true;
            };

    private final BottomNavigationView.OnNavigationItemSelectedListener nav_cliente_Listener =
            item -> {
                Fragment selectedFragement = null;
                switch (item.getItemId()) {
                    case R.id.nav_cat_cl:
                        selectedFragement = new Catalogo_Cliente_fragment();
                        break;
                    case R.id.nav_citas_cl:
                        selectedFragement = new Citas_Cliente_Fragment();
                        break;
                    case R.id.nav_fav_cl:
                        selectedFragement = new Favoritos_cliente_fragment();
                        break;
                    case R.id.nav_perfil_cl:
                        selectedFragement = new Perfil_cliente_fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, selectedFragement).commit();
                return true;
            };

    private final BottomNavigationView.OnNavigationItemSelectedListener nav_vendedor_Listener =
            item -> {
                Fragment selectedFragement = null;
                switch (item.getItemId()) {
                    case R.id.nav_citas_ven:
                        selectedFragement = new Citas_vendedor_fragment();
                        break;
                    case R.id.nav_cat_ven:
                        selectedFragement = new Catalogo_Vendedor_Fragment();
                        break;
                    case R.id.nav_ventas_ven:
                        selectedFragement = new Ventas_vendedor_fragment();
                        break;
                    case R.id.nav_perfil_ven:
                        selectedFragement = new Perfil_Vendedor_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor_ven, selectedFragement).commit();
                return true;
            };

    public void logIn(View v) throws Exception {
        String msg;

        EditText correo = findViewById(R.id.email_etxt);
        EditText clave = findViewById(R.id.clave_etxt);
        String correo_str = correo.getText().toString();
        String clave_str = clave.getText().toString();
        Usuario usuario;
        String tipo = "";
        if (patioventa.getAdministrador() != null && correo_str.compareTo(patioventa.getAdministrador().getCorreo()) == 0) {
            usuario = patioventa.getAdministrador();
            tipo = "ADMIN";
        } else {
            usuario = patioventa.buscarVendedores("Correo", correo_str);
            if (usuario == null) {
                usuario = patioventa.buscarClientes("Correo", correo_str);
                tipo = "CLIENTE";
            } else {
                tipo = "VENDEDOR";
            }
        }

        if (usuario != null) {
            if (usuario.getClave().compareTo(clave_str) == 0) {
                msg = "Se ha iniciado secion correctamente";
                usuarioActual = usuario;
                irAplicacion(tipo);
            } else {
                msg = "Contraseña incorrecta";
            }

        } else {
            msg = "Correo no registrado";
        }
        Toast.makeText(Patioventainterfaz.this, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getFechaMod(Date fechaMod) {
        return sdf.format(fechaMod);
    }

    //Seleccion de imagen en la galeria
    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    public void botonImagenPerfilVendedor(View view) {
        if (ActivityCompat.checkSelfPermission(Patioventainterfaz.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGalery();
        } else {
            ActivityCompat.requestPermissions(Patioventainterfaz.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_GALERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                foto = data.getData();
                reg_img.setImageURI(foto);
            } else {
                Toast.makeText(Patioventainterfaz.this, "Error 305: Datos no quemados", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalery();
            } else {
                Toast.makeText(Patioventainterfaz.this, "Se necesita habilitar los permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void irRegistarse(View view) {
        setContentView(R.layout.registrar_usuario);
    }

    public void aniadirCliente(View v) {
        EditText textonombre;
        EditText textapellido;
        EditText textocedula;
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textotelefono;
        EditText textocorreo;
        EditText textoclave;
        EditText textorepetirclave;
        int vacios = 0;
        int invalidos = 0;

        if(foto == null){
            Toast.makeText(Patioventainterfaz.this, "No ha seleccionado Imagen de Perfil", Toast.LENGTH_SHORT).show();
            vacios++;
        }
        
        textocedula = findViewById(R.id.reg_cedula_us_etxt);
        String cedula_str = textocedula.getText().toString();
        if (cedula_str.isEmpty()) {
            vacios++;
        } else {
            if (cedula_str.length() != 10) {
                Toast.makeText(Patioventainterfaz.this, "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                textocedula.setText("");
                invalidos++;
            }
        }
        textonombre = findViewById(R.id.reg_nombre_us_etxt);
        String nombre_str = textonombre.getText().toString();
        textapellido = findViewById(R.id.reg_apellido_us_etxt);
        String apellido_str = textapellido.getText().toString();
        String nombre_Completo = null;
        if (nombre_str.isEmpty()) {
            vacios++;
        }else if (apellido_str.isEmpty()) {
            vacios++;
        }else{
            nombre_Completo = nombre_str + " " + apellido_str;
        }

        textotelefono = findViewById(R.id.reg_telefono_us_etxt);
        String telefono_str = textotelefono.getText().toString();
        if (telefono_str.isEmpty()) {
            vacios++;
        } else {
            if (telefono_str.length() != 10) {
                Toast.makeText(Patioventainterfaz.this, "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                textotelefono.setText("");
                invalidos++;
            }
        }

        textocorreo = findViewById(R.id.reg_correo_us_etxt);
        String correo_str = textocorreo.getText().toString();
        if (correo_str.isEmpty()) {
            vacios++;
        } else {
            if (!Patioventainterfaz.validarMail(correo_str)) {
                Toast.makeText(Patioventainterfaz.this, "Correo no valido", Toast.LENGTH_SHORT).show();
                textocorreo.setText("");
                invalidos++;
            }
        }

        textoclave = findViewById(R.id.reg_contraseña_us_etxt);
        String clave_str = textoclave.getText().toString();
        textorepetirclave = findViewById(R.id.reg_confir_contraseña_us_etxt);
        String repetirclave_str = textorepetirclave.getText().toString();
        if (clave_str.isEmpty()) {
            vacios++;
        } else {
            if (repetirclave_str.isEmpty()) {
                vacios++;
            } else {
                if(clave_str.length()<8 || repetirclave_str.length()<8){
                    Toast.makeText(Patioventainterfaz.this, "La clave debe contener 8 caracteres mínimo", Toast.LENGTH_SHORT).show();
                    textoclave.setText("");
                    textorepetirclave.setText("");
                    invalidos++;
                }else if (clave_str.compareTo(repetirclave_str) != 0) {
                    Toast.makeText(Patioventainterfaz.this, "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                    textoclave.setText("");
                    textorepetirclave.setText("");
                    invalidos++;
                }
            }
        }

        textomes = findViewById(R.id.reg_mes_us_etxt);
        String mes_str = textomes.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            vacios++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(Patioventainterfaz.this, "Mes inválido", Toast.LENGTH_SHORT).show();
                textomes.setText("");
                invalidos++;
            }
        }

        textoanio = findViewById(R.id.reg_anio_us_etxt);
        String anio_str = textoanio.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            vacios++;
        } else {
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(Patioventainterfaz.this, "Año inválido", Toast.LENGTH_SHORT).show();
                textoanio.setText("");
                invalidos++;
            }
        }

        textodia = findViewById(R.id.reg_dia_us_etxt);
        String dia_str = textodia.getText().toString();
        int dia;
        if (dia_str.isEmpty()) {
            vacios++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(Patioventainterfaz.this, "Día inválido", Toast.LENGTH_SHORT).show();
                textodia.setText("");
                invalidos++;
            }
        }

        if (vacios == 0 && invalidos == 0) {
            Date fecha = null;
            try {
                fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            StorageReference filePath = mStorageRef.child("Clientes").child(cedula_str + ".jpg");
            filePath.putFile(foto).addOnSuccessListener(taskSnapshot ->
                    Toast.makeText(Patioventainterfaz.this, "Se subio la imagen", Toast.LENGTH_SHORT).show()
            );
            try {
                Cliente cliente_comprobacion = patioventa.buscarClientes("Cedula",cedula_str);
                if(cliente_comprobacion== null){
                    Cliente cliente = new Cliente(nombre_Completo, cedula_str, telefono_str, correo_str, clave_str, fecha, cedula_str + ".jpg");
                    if(patioventa.aniadirUsuario(cliente, "Cliente")){
                        Toast.makeText(Patioventainterfaz.this, "Se aniadio el cliente correctamente", Toast.LENGTH_SHORT).show();
                        irAplicacion("CLIENTE");
                    }
                }else{
                    cliente_comprobacion.cambiarDatos(nombre_Completo,cedula_str,telefono_str,correo_str,clave_str,getFechaMod(fecha));
                    cliente_comprobacion.setImagen(cedula_str + ".jpg");
                    irAplicacion("CLIENTE");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(Patioventainterfaz.this, "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarAdministrador() {
        EditText nombreAdmin;
        EditText apellidoAdmin;
        EditText cedulaAdmin;
        EditText diaNacimientoAdmin;
        EditText mesNacimientoAdmin;
        EditText anioNacimientoAdmin;
        EditText telefonoAdmin;
        EditText correoAdmin;
        EditText contraseniaAdmin;
        EditText confirmarContraseniaAdmin;
        EditText horaEntradaAdmin;
        EditText horaSalidaAdmin;
        EditText horaAlmuerzoAdmin;
        int vacios = 0;
        int invalidos = 0;

        if(foto == null){
            Toast.makeText(Patioventainterfaz.this, "No ha seleccionado Imagen de Perfil", Toast.LENGTH_SHORT).show();
            vacios++;
        }

        nombreAdmin = findViewById(R.id.reg_nombre_admin_etxt);
        apellidoAdmin = findViewById(R.id.reg_apellido_admin_etxt);
        String nombreCampoAdmin_str = nombreAdmin.getText().toString();
        String apellidoCampoAdmin_str = apellidoAdmin.getText().toString();
        String nombreCompletoAdmin_str = "";
        if (nombreCampoAdmin_str.isEmpty()) {
            vacios++;
        } else {
            if (apellidoCampoAdmin_str.isEmpty()) {
                vacios++;
            } else {
                nombreCompletoAdmin_str = nombreAdmin.getText().toString() + "" + apellidoAdmin.getText().toString();
            }
        }

        cedulaAdmin = findViewById(R.id.reg_cedula_admin_etxt);
        String cedulaAdmin_str = cedulaAdmin.getText().toString();
        if (cedulaAdmin_str.isEmpty()) {
            vacios++;
        } else {
            if (cedulaAdmin_str.length() != 10) {
                Toast.makeText(Patioventainterfaz.this, "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cedulaAdmin.setText("");
                invalidos++;
            }
        }

        anioNacimientoAdmin = findViewById(R.id.reg_anio_admin_etxt);
        String anio_str = anioNacimientoAdmin.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            vacios++;
        } else {
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(Patioventainterfaz.this, "Año inválido", Toast.LENGTH_SHORT).show();
                anioNacimientoAdmin.setText("");
                invalidos++;
            }
        }

        mesNacimientoAdmin = findViewById(R.id.reg_mes_admin_etxt);
        String mes_str = mesNacimientoAdmin.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            vacios++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(Patioventainterfaz.this, "Mes inválido", Toast.LENGTH_SHORT).show();
                mesNacimientoAdmin.setText("");
                invalidos++;
            }
        }

        diaNacimientoAdmin = findViewById(R.id.reg_dia_admin_etxt);
        String dia_str = diaNacimientoAdmin.getText().toString();
        int dia;
        if (dia_str.isEmpty()) {
            vacios++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(Patioventainterfaz.this, "Día inválido", Toast.LENGTH_SHORT).show();
                diaNacimientoAdmin.setText("");
                invalidos++;
            }
        }

        telefonoAdmin = findViewById(R.id.reg_telefono_admin_etxt);
        String telefonoAdmin_str = telefonoAdmin.getText().toString();
        if (telefonoAdmin_str.isEmpty()) {
            vacios++;
        } else {
            if (telefonoAdmin_str.length() != 10) {
                Toast.makeText(Patioventainterfaz.this, "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                telefonoAdmin.setText("");
                invalidos++;
            }
        }

        correoAdmin = findViewById(R.id.reg_correo_admin_etxt);
        String correoAdmin_str = correoAdmin.getText().toString();
        if (correoAdmin_str.isEmpty()) {
            vacios++;
        } else {
            if (!validarMail(correoAdmin_str)) {
                Toast.makeText(Patioventainterfaz.this, "Correo no valido", Toast.LENGTH_SHORT).show();
                correoAdmin.setText("");
                invalidos++;
            }
        }

        contraseniaAdmin = findViewById(R.id.reg_clave_admin_etxt);
        confirmarContraseniaAdmin = findViewById(R.id.reg_claveconfim_admin_etxt);
        String contraseniaAdmin_str = contraseniaAdmin.getText().toString();
        String confirmarContraseniaAdmin_str = confirmarContraseniaAdmin.getText().toString();
        if (contraseniaAdmin_str.isEmpty()) {
            vacios++;
        } else {
            if (confirmarContraseniaAdmin_str.isEmpty()) {
                vacios++;
            } else{
                if(contraseniaAdmin_str.length()<8 || confirmarContraseniaAdmin_str.length()<8) {
                    Toast.makeText(Patioventainterfaz.this, "La clave debe contener 8 caracteres mínimo", Toast.LENGTH_SHORT).show();
                    contraseniaAdmin.setText("");
                    confirmarContraseniaAdmin.setText("");
                    invalidos++;
                }else {
                    if (contraseniaAdmin_str.compareTo(confirmarContraseniaAdmin_str) != 0) {
                        Toast.makeText(Patioventainterfaz.this, "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                        contraseniaAdmin.setText("");
                        confirmarContraseniaAdmin.setText("");
                        invalidos++;
                    }
                }
            }
        }

        horaEntradaAdmin = findViewById(R.id.reg_entrada_admin_etxt);
        String horaEntradaAdmin_str = horaEntradaAdmin.getText().toString();
        int horaEntradaAdmin_int = -1;
        if (horaEntradaAdmin_str.isEmpty()) {
            vacios++;
        } else {
            horaEntradaAdmin_int = Integer.parseInt(horaEntradaAdmin.getText().toString());
            if (horaEntradaAdmin_int < 0 || horaEntradaAdmin_int > 24) {
                Toast.makeText(Patioventainterfaz.this, "Hora de entrada inválida", Toast.LENGTH_SHORT).show();
                horaEntradaAdmin.setText("");
                invalidos++;
            }
        }

        horaAlmuerzoAdmin = findViewById(R.id.reg_almuerzo_admin_etxt);
        String horaAlmuerzoAdmin_str = horaAlmuerzoAdmin.getText().toString();
        int horaAlmuerzoAdmin_int = -1;
        if (horaAlmuerzoAdmin_str.isEmpty()) {
            vacios++;
        } else {
            horaAlmuerzoAdmin_int = Integer.parseInt(horaAlmuerzoAdmin.getText().toString());
            if (horaAlmuerzoAdmin_int < 0 || horaAlmuerzoAdmin_int > 24) {
                Toast.makeText(Patioventainterfaz.this, "Hora de almuerzo inválida", Toast.LENGTH_SHORT).show();
                horaAlmuerzoAdmin.setText("");
                invalidos++;
            }
        }

        horaSalidaAdmin = findViewById(R.id.reg_salida_admin_etxt);
        String horaSalidaAdmin_str = horaSalidaAdmin.getText().toString();
        int horaSalidaAdmin_int = -1;
        if (horaSalidaAdmin_str.isEmpty()) {
            vacios++;
        } else {
            horaSalidaAdmin_int = Integer.parseInt(horaSalidaAdmin.getText().toString());
            if (horaSalidaAdmin_int < 0 || horaSalidaAdmin_int > 24) {
                Toast.makeText(Patioventainterfaz.this, "Hora de salida inválida", Toast.LENGTH_SHORT).show();
                horaSalidaAdmin.setText("");
                invalidos++;
            }
        }

        if (vacios == 0 && invalidos ==0) {
            Date fecha = null;
            try {
                fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            StorageReference filePath = mStorageRef.child("Vendedores").child(cedulaAdmin_str + ".jpg");
            filePath.putFile(foto).addOnSuccessListener(taskSnapshot ->
                    Toast.makeText(Patioventainterfaz.this, "Se subio la imagen", Toast.LENGTH_SHORT).show()
            );
            Vendedor user = new Vendedor(
                    String.format("%s.jpg", cedulaAdmin_str),
                    horaEntradaAdmin_int,
                    horaSalidaAdmin_int,
                    horaAlmuerzoAdmin_int,
                    patioventa,
                    nombreCompletoAdmin_str,
                    cedulaAdmin_str,
                    telefonoAdmin_str,
                    correoAdmin_str,
                    contraseniaAdmin_str,
                    fecha);
            usuarioActual = user;
            patioventa.aniadirUsuario(user, "Administrador");
            try {
                irAplicacion("ADMIN");
                Toast.makeText(Patioventainterfaz.this, "Administrador registrado correctamente", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(Patioventainterfaz.this, "Error 304: No se pudo completar el registro (Administrador)", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(Patioventainterfaz.this, "Existen campos vacíos", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean validarMail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public static boolean validarDia(int anio, int mes, int dia) {
        boolean valido = true;
        int numeroDias = -1;
        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numeroDias = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numeroDias = 30;
                break;
            case 2:
                numeroDias = 28;
                if (esBisiesto(anio))
                    numeroDias = 29;
                break;
        }

        if (dia > numeroDias || dia < 1) {
            valido = false;
        }
        return valido;
    }

    public static boolean esBisiesto(int anio) {
        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;
    }

    public void salir(View view) {
        AlertDialog.Builder msg = new AlertDialog.Builder(Patioventainterfaz.this);
        msg.setMessage("¿Estás seguro de cerrar sesión?");
        msg.setTitle("LOG OUT");
        msg.setPositiveButton("Aceptar", (dialog, which) -> {
            usuarioActual = null;
            Window w = getWindow();
            w.setStatusBarColor(getColor(R.color.botones));
            w.getDecorView().setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setContentView(R.layout.login_sinclaves);
        });
        msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        msg.show();
    }

    public static String formatoHora(int hora) {
        if (hora >= 12) {
            return "pm";
        }
        return "am";
    }

    public static int[] contadores (Lista ventas){
        int[] datos = new int[12];
        int sep =0,en=0,feb=0,mar=0,abr=0,may=0,jun=0,jul=0,ago=0,oct=0;
        for (int i =0;i < ventas.contar();i++){
            Venta actual =(Venta) ventas.getPos(i);
            if(actual.getFecha().getMonth()==1){
                en++;
                datos[1]=en;

            }
            if(actual.getFecha().getMonth()==2){
                feb++;
                datos[2]=feb;

            }
            if(actual.getFecha().getMonth()==3){
                mar++;
                datos[3]=mar;

            }
            if(actual.getFecha().getMonth()==4){
                abr++;
                datos[4]=abr;

            }
            if(actual.getFecha().getMonth()==5){
                may++;
                datos[5]=may;

            }
            if(actual.getFecha().getMonth()==6){
                jun++;
                datos[6]=jun;

            }
            if(actual.getFecha().getMonth()==7){
                jul++;
                datos[7]=jul;

            }
            if(actual.getFecha().getMonth()==8){
                ago++;
                datos[8]=ago;

            }
            if(actual.getFecha().getMonth()==9){
                sep++;
                datos[9]=sep;
                
            }


        }
        
        return datos;
        
    }

}
