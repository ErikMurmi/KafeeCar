package com.prog.kafeecar;

import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author Erik
 */
public class Vendedor extends Usuario{
    
    private int horaEntrada;
    private int horaSalida;
    private int horaComida;
    private PatioVenta sucursal;
    private boolean activo;
    private String imagen;
    
    public Vendedor(String imagen, int horaEntrada, int horaSalida, int horaComida, PatioVenta sucursal, String nombre, String cedula, String telefono, String correo, String clave, Date fechaNacimiento) {
        super(nombre, cedula, telefono, correo, clave, fechaNacimiento);
        this.imagen = imagen;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.horaComida = horaComida;
        this.sucursal = sucursal;
        setActivo(true);
    }

    protected void cambiarDatosSinClaveVendedor(int horaEntrada, int horaSalida, int horaComida, String nombre, String cedula, String telefono, String correo, String fechaNacimiento) throws ParseException {
        setHoraEntrada(horaEntrada);
        setHoraComida(horaComida);
        setHoraSalida(horaSalida);
        setNombre(nombre);
        setCedula(cedula);
        setTelefono(telefono);
        setCorreo(correo);
        setFechaNacimiento(fechaNacimiento);
    }
    
   public Vendedor(String nombre, String cedula) { super(nombre, cedula); }

    public int getHoraEntrada() { return horaEntrada; }

    public void setHoraEntrada(int horaEntrada) { this.horaEntrada = horaEntrada; }

    public int getHoraSalida() { return horaSalida; }

    public void setHoraSalida(int horaSalida) { this.horaSalida = horaSalida; }

    public int getHoraComida() { return horaComida; }

    public void setHoraComida(int horaComida) { this.horaComida = horaComida; }

    public PatioVenta getSucursal() { return sucursal;  }

    public void setSucursal(PatioVenta sucursal) { this.sucursal = sucursal; }

    public boolean getActivo(){ return activo;}

    public void setActivo(boolean activo){ this.activo = activo; }

    public void setImagen(String imagen){ this.imagen = imagen; }

    public String getImagen(){ return imagen; }
   
    @Override
    public String toString() {
        String datos = super.toString();
        return "\nVendedor" +
                datos +
                "\nHora de entrada: " + horaEntrada + 
                "\nHora de salida: " + horaSalida + 
                "\nHora de comida: " + horaComida + 
                "\nSucursal: " + sucursal;
    }
    
    /**
     * Busca las citas del vendedor en la sucursal
     * @return citas del vendedor 
     */
    public Lista obtenerCitas() throws Exception {
        Lista citas = new Lista();
        Lista copiaSitema=sucursal.getCitas();
        int cont =0;
        
       while(cont< copiaSitema.contar())
       {
            Cita act= (Cita) copiaSitema.getPos(cont) ;
            if(this.getNombre().compareTo(act.getVendedorCita().getNombre())==0){
                citas.add(act);}
                                
                 cont++;
       }
         
        return citas;
    }
    
    /**
     * Busca las ventas del vendedor en la sucursal
     * @return ventas del vendedor 
     */
    public void obtenerVentas() throws Exception {
        Lista ventas =  new Lista();
        Lista copiaSitema=sucursal.getVentasGenerales();
        int cont =0;
        System.out.println(copiaSitema.contar());
       while(cont< copiaSitema.contar())
       {
            Venta act= (Venta) copiaSitema.getPos(cont) ;
            if(act.getVendedor().getNombre().compareTo(this.getNombre())==0){
                System.out.println("Encontrado");
                ventas.add(act);
            }
                                
                 cont++;
       }
         
       System.out.println("las ventas son "+ventas.contar()); 
       
    }
    
    /**
     * Metodo para verificar si un vendedor se encuentra disponible para una cita
     * @param fechaCita dato sobre la fecha que se hará la cita
     * @param hora dato sobre la hora a la que se realizara la cita
     * @return true si esta disponible, caso contrario false
     */
    
    public boolean disponible(Date fechaCita, int hora) throws Exception {
        Lista lsvendedor= obtenerCitas();
        int cont=0;
        boolean disponible=true;
        if ((hora < horaEntrada || hora > horaSalida) || hora==horaComida){
            disponible=false;   
        }else{
            while(cont<lsvendedor.contar()){
                Cita act= (Cita) lsvendedor.getPos(cont);
                if(act.getFechaCita().compareTo(fechaCita)==0 && act.getHora()== hora){
                    disponible=false;
                }
                cont++;            
            }
        }
        return disponible;
    }
}
