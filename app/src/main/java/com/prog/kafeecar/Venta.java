/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.kafeecar;

import java.util.Date;

/**
 *
 * @author Erik
 */
public class Venta {
    
    private Date fecha;
    
    private Cliente comprador;
    private Vendedor vendedor;
    private Lista vehiculos;

    public Venta(Date fecha, float precio, Cliente comprador, Vendedor vendedor, Vehiculo v) {
        this.fecha = fecha;
        this.comprador = comprador;
        this.vendedor = vendedor;
        vehiculos = new Lista();
        vehiculos.add(v);
    }


    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getPrecio() throws Exception {
        float precio =0;
        for(int i=0; i<vehiculos.contar();i++){
            Vehiculo actual = (Vehiculo) vehiculos.getPos(i);
            if(actual.getPromocion()==0){
                precio += actual.getPromocion();
            }else{
                precio += actual.getPrecioVenta();
            }
        }
        return precio;
    }


    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Lista getVehiculos() {
        return vehiculos;
    }

    public void setVehiculo(Lista vehiculos) {
        this.vehiculos = vehiculos;
    }
    
    /**
     * Aniade un vehiculo a la venta
     * @param vehiculo que se va a aniadir
     * @return true si se agrego, caso contrario false
     */
    public boolean aniadirVehiculo(Vehiculo vehiculo){
        vehiculos.add(vehiculo);
        return false;
    }
    
    /**
     * Elimina un vehiculo de la venta
     * @param matricula que se va a remover
     * @return true si se removio, caso contrario false
     */
    public boolean removerVehiculo(String matricula) throws Exception {
        boolean removido = false;
        int c = 0;
        while(c<vehiculos.contar() && !removido)
        {
            Vehiculo actual = (Vehiculo) vehiculos.getPos(c);
            if(actual.getMatricula().compareTo(matricula)==0)
            {
                vehiculos.eliminar(actual);
                removido = true;
            }
            c++;
        }    
        return removido;
    }
    
    /**
     * Busca un vehiculo en la venta
     * @param matricula del vehiculo buscado
     * @return el vehiculo busado
     */
    public Vehiculo buscarVehiculo(String matricula){
        
        return null;
    }
    
    @Override
    public String toString() {
        return "Venta" + 
                "\nFecha: " + fecha +
                "\nComprador: " + comprador +
                "\nVendedor: " + vendedor + 
                "\nVehiculo=" + vehiculos;
    }
    
    /**
     * Método que ayuda al vendedor o administrador a actualizar la información de una venta
     * @param fechaventa Parametro de venta para establecer como nuevo dato 
     * @param precioFinal Parametro de venta para establecer como nuevo dato 
     * @param vehiculos Parametro de venta para establecer como nuevo dato 
     * @param cliente Parametro de venta para establecer como nuevo dato 
     * @param vendedor Parametro de venta para establecer como nuevo dato 
     * @return Retorna los datos de la venta del vehiculo 
     */
    public Vehiculo actualizar(Date fechaventa, Float precioFinal, Vehiculo vehiculos, Cliente cliente, Vendedor vendedor){
        return null;
     
    }
    
    
    
    
}
