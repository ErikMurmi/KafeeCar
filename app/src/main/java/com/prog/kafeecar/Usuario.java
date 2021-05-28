/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.kafeecar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Erik
 */
public class Usuario {
    
    private String nombre;
    private String cedula;
    private String telefono;
    private String correo;
    private String clave;
    private Date fechaNacimiento;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    protected boolean activo;

    
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
    
    /**
     * Muestra si el usuario esta activo 
     * @return estado actual del usuario.
     */
    protected boolean isActivo(){
        return activo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) throws ParseException {
        this.fechaNacimiento = sdf.parse(fechaNacimiento);
    }


    @Override
    public String toString() {
        return "\nUsuario" + 
                "\nNombre: " + nombre +
                "\nFecha de Nacimiento: " + fechaNacimiento +
                "\nCedula: " + cedula + 
                "\nCorreo: " + correo + 
                "\nClave: " + clave + 
                "\nActivo: " + activo ;
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
    
    /**
     * Cambia un dato especifico del usuario
     * @param criterio identificar del dato que se desea cambiar
     * @param dato indentificador por el cual se actualizará la informacion
     * @return usuario con el dato actualizado
     */
    
    public Usuario cambiarDato(String criterio, String dato) throws ParseException{
        Usuario usuario = null;
        if(criterio.compareToIgnoreCase("nombre")==0){
            setNombre(dato);
        }else if(criterio.compareToIgnoreCase("cedula")==0){
            setCedula(dato);
        }else if(criterio.compareToIgnoreCase("correo")==0){
            setCorreo(dato);
        }else if(criterio.compareToIgnoreCase("clave")==0){
            setClave(dato);
        }else if(criterio.compareToIgnoreCase("telefono")==0){
            setTelefono(dato);
        }else if(criterio.compareTo("fechaNacimiento")==0){
            setFechaNacimiento(dato);
        }
        return usuario;
    }
    
        
    /**
     * Cambia el estado de un usuario de activo a no activo y viceversa
     * @param activo de tipo booleano que indicara el nuevo estado actual
     */
    
    public void cambiarEstadoUsuario(boolean activo){
        
    }
    
    
}
