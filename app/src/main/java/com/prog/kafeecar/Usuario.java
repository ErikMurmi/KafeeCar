package com.prog.kafeecar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario {
    
    private String nombre;
    private String cedula;
    private String telefono;
    private String correo;
    private String clave;
    private Date fechaNacimiento;
    
    public Usuario(){}
    
    public Usuario(String nombre, String cedula, String telefono, String correo, String clave, Date fechaNacimiento) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.clave = clave;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public Usuario(String nombre, String cedula) {
        this.nombre = nombre;
        this.cedula = cedula;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) throws ParseException {
        this.fechaNacimiento = Patioventainterfaz.sdf.parse(fechaNacimiento);
    }

    @Override
    public String toString() {
        return "\nUsuario" + 
                "\nNombre: " + nombre +
                "\nFecha de Nacimiento: " + fechaNacimiento +
                "\nCedula: " + cedula + 
                "\nCorreo: " + correo + 
                "\nClave: " + clave;
    }
    //Metodos del sistema

    /**
     * Cambia todos los datos de un usuario
     * @param nombre dato nuevo de nombre
     * @param cedula dato nuevo de cedula
     * @param telefono dato nuevo de telefono
     * @param correo dato nuevo de correo
     * @param clave dato nuevo de clave
     * @param fechaNacimiento dato tipo string se la fecha de nacimiento
     * @return usuario con los datos actualizados
     * @throws java.text.ParseException debido a la transformacion de un objeto de la clase date en string
     */
    protected Usuario cambiarDatos(String nombre, String cedula, String telefono, String correo, String clave, String fechaNacimiento) throws ParseException{
        Usuario usuario = null;
        setNombre(nombre);
        setCedula(cedula);
        setTelefono(telefono);
        setCorreo(correo);
        setClave(clave);
        setFechaNacimiento(fechaNacimiento);
        return usuario;
    }
}
