package com.prog.kafeecar;

import java.util.Date;

public class Cita {

    private Date fechaCita;
    private int hora;
    private String resolucion;

    private Cliente cliente;
    private Vendedor vendedorCita;
    private Vehiculo vehiculo;

    public Cita(Date fechaCita, int hora, String resolucion, Cliente cliente, Vendedor vendedorCita, Vehiculo vehiculo) {
        this.fechaCita = fechaCita;
        this.hora = hora;
        this.resolucion = resolucion;
        this.cliente = cliente;
        this.vendedorCita = vendedorCita;
        this.vehiculo = vehiculo;
    }
    public Cita(Date fechaCita, int hora, Cliente cliente, Vehiculo vehiculo) {
        this.fechaCita = fechaCita;
        this.hora = hora;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
    }
    //
    public Cita(Date fechaCita, int hora, Vehiculo vehiculo) {
        this.fechaCita = fechaCita;
        this.hora = hora;
        this.vehiculo = vehiculo;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    
    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedorCita() {
        return vendedorCita;
    }

    public void setVendedorCita(Vendedor vendedorCita) {
        this.vendedorCita = vendedorCita;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Cita"
                + "\nFecha Cita: " + fechaCita
                + "\nHora: " + hora
                + "\nResolucion : " + resolucion
                + "\nCliente: " + cliente
                + "\nVendedorCita: " + vendedorCita
                + "\nVehiculo: " + vehiculo;
    }

    /**
     * Actualiza los datos de una cita
     *
     * @param fecha dia/mes/anio de la cita
     * @param hora hora a la que se realizara la cita
     * @param vehiculo auto por el cual se solicito la cita
     * @param vendedor nuevo agente que atendera la cita
     * @param visitante nuevo cliente de la cita
     * @return cita actualizada
     */
    public Cita actualizar(Date fecha, int hora, Vehiculo vehiculo, Vendedor vendedor, Cliente visitante) {
        fechaCita = fecha;
        this.hora = hora;
        this.vehiculo = vehiculo;
        vendedorCita = vendedor;
        this.cliente = visitante;
        return this;  
    }

    public Cita actualizarVen(Date fecha, int hora, Vehiculo vehiculo, Vendedor vendedor, Cliente visitante, String resolucion) {
        fechaCita = fecha;
        this.hora = hora;
        this.vehiculo = vehiculo;
        vendedorCita = vendedor;
        this.cliente = visitante;
        this.resolucion = resolucion;
        return this;
    }

}
