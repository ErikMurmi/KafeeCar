
package com.prog.kafeecar;

import java.util.ArrayList;
import java.util.Date;

public class PatioVenta {
    private Lista ventasGenerales;
    private Lista vendedores;
    private Lista clientes;
    private Lista vehiculos;
    private Lista citas;
    
    private Vendedor administrador;
    private String telefono;

    public PatioVenta() {
        vendedores = new Lista();
        ventasGenerales = new Lista();
        clientes = new Lista();
        vehiculos = new Lista();
        citas = new Lista();
        administrador = null;
    }

    public PatioVenta(Lista ventasGenerales, Lista vendedores, Lista clientes, Lista vehiculos, Vendedor administrador) {
        setVentasGenerales(ventasGenerales);
        setVendedores(vendedores);
        setClientes(clientes);
        setVehiculos(vehiculos);
        setAdministrador(administrador);
    }

    public Lista getCitas() {
        return citas;
    }

    public void setCitas(Lista citas) {
        this.citas = citas;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
    public Lista getVentasGenerales() {
        return ventasGenerales;
    }

    public void setVentasGenerales(Lista ventasGenerales) {
        this.ventasGenerales = ventasGenerales;
    }

    public Lista getVendedores() {
        return vendedores;
    }

    public void setVendedores(Lista vendedores) {
        this.vendedores = vendedores;
    }

    public Lista getClientes() {
        return clientes;
    }

    public void setClientes(Lista clientes) {
        this.clientes = clientes;
    }

    public Lista getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(Lista vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Vendedor getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Vendedor administrador) {
        this.administrador = administrador;
    }

    @Override
    public String toString() {
        return "PatioDeVentas" 
                + "\nVentas Generales: " + ventasGenerales 
                + "\nVendedores: " + vendedores 
                + "\nClientes: " + clientes 
                + "\nVehiculos: " + vehiculos 
                + "\nAdministrador: " + administrador;
    }
    
    //Metodos que necesita el sistema
    
    /**
     * Agrega una cita
     * @param nueva cita a ser agregada
     * @return true si se agrego , false en caso de que no.
     */
    public void aniadirCita(Cita nueva){
        citas.add(nueva);
        
    }

    /**
     * Agrega un vehiculo al catalogo
     * @param nuevo vehiculo a aniadir a la lista
     * @return true si se agrego el vehiculo, false en caso de que no.
     */
    public boolean aniadirVehiculo(Vehiculo nuevo){
        vehiculos.add(nuevo);
        return vehiculos.contiene(nuevo);
    }

    /**
     * Agrega una venta
     * @param nueva venta a ser agregada
     * @return true si se agrego , false en caso de que no.
     */
    public void aniadirVenta(Venta nueva) throws Exception {
        ventasGenerales.add(nueva);
        Vehiculo actual = nueva.getVehiculo();
        removerVehiculo(actual.getPlaca());
    }

    /**
     * Agrega un usuario al sistema
     * @param nuevo usuario a ser agregado
     * @param tipo cliente o vendedor
     * @return true si se agrego el usuario, false en caso de que no.
     */
    public boolean aniadirUsuario(Usuario nuevo, String tipo){
        boolean agregado =false;

        if(tipo.compareTo("Vendedor")==0){
            vendedores.add((Vendedor) nuevo);
            agregado = vendedores.contiene(nuevo);
        }else if(tipo.compareTo("Cliente")==0){
            clientes.add(nuevo);
            agregado = clientes.contiene(nuevo);
        }
        if(tipo.compareTo("Administrador")==0){
            vendedores.add(nuevo);
            setAdministrador((Vendedor) nuevo);
            agregado = vendedores.contiene(nuevo);
        }
        return agregado;
    }
    /**
     * Elimina una cita de la lista de citas
     * @param placa del vehiculo que se desea eliminiar de la lista
     * @param cedula del cliente que deseaba el vehiculo
     * @return true si se elimino de la lista, caso contrario false
     */
    
    public boolean removerCita(String placa, String cedula) {
        int cont = 0;
        boolean removido = false;
        while(cont<citas.contar() && removido==false){
            Cita actual = (Cita) citas.getPos(cont);
            if(actual.getCliente().getCedula().compareTo(cedula)==0){
                if(actual.getVehiculo().getPlaca().compareTo(placa)==0){
                    try {
                        citas.eliminarPos(cont);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    removido = true;
                }
            }
            cont++;
        }
        return removido;
    }

    /**
     * Elimina un vehiculo del catalogo
     * @param placa vehiculo a eliminar a la lista
     * @return true si se elimino el vehiculo, false en caso de que no.
     */
    public boolean removerVehiculo(String placa) throws Exception {
        boolean encontrado = false;
        int cont=0;
        while(cont<vehiculos.contar() && !encontrado){
            Vehiculo actual = (Vehiculo) vehiculos.getPos(cont);
            if(actual.getPlaca().compareToIgnoreCase(placa) ==0 ){
                vehiculos.eliminarPos(cont);
                encontrado=true;
            }
            cont++;
        }
        return encontrado;
    }

    public boolean removerVenta(String placa) throws Exception {

        boolean encontrado = false;
        int cont =0;
        while(cont<ventasGenerales.contar() && !encontrado){
            Venta actual= (Venta) ventasGenerales.getPos(cont);
            if(actual.getVehiculo().getPlaca().compareToIgnoreCase(placa)==0){
                ventasGenerales.eliminarPos(cont);
                vehiculos.add(actual.getVehiculo());
                encontrado= true;
            }
            cont++;
        }
        return encontrado;
    }
    /**
     * Busca citas por distintos criterios
     * @param criterio atributo por el cual buscar del cliente
     * @param texto palabra a buscar en el criterrio
     * @return Lista de citas que corresponden al criterio de busquda
     */
    public Cita buscarCita(String criterio, String texto, String cliente){
        Cita citaEncontrada;

        if(criterio.compareToIgnoreCase("Vehiculo")==0){
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getVehiculo().getPlaca().compareTo(texto)==0 && actual.getCliente().getCedula().compareTo(cliente)==0){
                    citaEncontrada = actual;
                    return citaEncontrada;
                }
                cont++;
            }
        }else if(criterio.compareToIgnoreCase("Vendedor")==0){
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getVendedorCita().getNombre().compareTo(texto)==0){
                    citaEncontrada = actual;
                    return citaEncontrada;
                }
                cont++;
            }
        } else if(criterio.compareToIgnoreCase("Cliente")==0){
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getCliente().getCedula().compareTo(texto)==0){
                    citaEncontrada = actual;
                    return citaEncontrada;
                }
                cont++;
            }
        }
        return null;
    }

    public Lista buscarCitas(Cliente cliente) {

        Lista citaslista=new Lista();
        int cont = 0;
        while(cont<citas.contar()){
            Cita actual = (Cita) citas.getPos(cont);
            if(actual.getCliente().getCedula().compareTo(cliente.getCedula())==0){
                citaslista.add(actual);

            }
            cont++;
        }
        return citaslista;
    }
    /**
     *
     * Busca vehiculos por sus caracteristicas
     * @param criterio caracteristica de busqueda de vehiculos
     * @param codigo palabra a buscar dentro del criterio
     * @return Vehiculo que coincide con el codigo buscado en el criterio de busqueda.
     */
    public Vehiculo buscarVehiculos(String criterio, String codigo) {
        Vehiculo buscado = null;

        if(criterio.compareTo("Placa")==0){
            int cont = 0;
            while(cont<vehiculos.contar()){
                Vehiculo actual = (Vehiculo) vehiculos.getPos(cont);
                if(actual.getPlaca().compareToIgnoreCase(codigo)==0){
                    buscado = actual;
                    break;
                }
                cont++;
            }
        }else if(criterio.compareToIgnoreCase("Matricula")==0){
            int cont = 0;
            while(cont<vehiculos.contar()){
                Vehiculo actual = (Vehiculo) vehiculos.getPos(cont);
                if(actual.getMatricula().compareTo(codigo)==0){
                    buscado = actual;
                    break;
                }
                cont++;
            }
        }
        return buscado;
    }
    public Lista buscarVehiculosFav(String criterio, Lista favoritos) throws Exception {

        Lista listaBuscadosFav= new Lista();

        if(criterio.compareTo("Placa")==0) {
            int cont = 0;
            while (cont < favoritos.contar()) {
                Vehiculo v=buscarVehiculos("Placa",(String)favoritos.getPos(cont));
                if(v!=null){
                    listaBuscadosFav.add(v);

                }
                cont++;
            }
        }
        return listaBuscadosFav;
    }
    /**
     * Busca ventas bajo disintos criterios
     * @param cedula cedula del cliente de la venta
     * @param placa matricula del vehiculo de la venta
     * @return venta que coincide con los criterios de busqueda
     */
    public Venta buscarVentas(String placa, String cedula){
        Venta buscada = null;
        int cont =0;
        while(cont<ventasGenerales.contar() && buscada==null){
            Venta actual = (Venta) ventasGenerales.getPos(cont);
            if(actual.getComprador().getCedula().compareTo(cedula)==0 && actual.getVehiculo().getPlaca().compareTo(placa)==0){
                buscada = actual;
            }
            cont++;
        }
        return buscada;
    }   

    /**
     * Busca un cliente en la lista
     * @param criterio tipo de busqueda por cedula o nombre
     * @param codigo dato de la cedula o nombre
     * @return clientes que cumplen parametros de busqueda
     */
    public Cliente buscarClientes(String criterio, String codigo) {
        Cliente buscado = null;
        boolean encontrado = false;
        if(criterio.compareToIgnoreCase("Nombre")==0) 
        {
            int cont=0;
            while(cont<clientes.contar() && !encontrado){
                Cliente actual =(Cliente) clientes.getPos(cont);
                if(actual.getNombre().compareToIgnoreCase(codigo)==0)
                {
                    buscado = actual;
                    encontrado =true;
                }
                cont++;
            }
                    
        }
        else if(criterio.compareToIgnoreCase("Cedula")==0)
        {
            int cont=0;
            while(cont<clientes.contar() && !encontrado){
                Cliente actual =(Cliente) clientes.getPos(cont);
                if(actual.getCedula().compareToIgnoreCase(codigo)==0)
                {
                    buscado = actual;
                    encontrado =true;
                }
                cont++;
            }
                  
        }
        else if(criterio.compareToIgnoreCase("Correo")==0)
        {
            int cont=0;
            while(cont<clientes.contar() && !encontrado){
                Cliente actual =(Cliente) clientes.getPos(cont);
                if(actual.getCorreo().compareToIgnoreCase(codigo)==0)
                {
                    buscado = actual;
                    encontrado =true;
                }
                cont++;
            }
        }
        return buscado;
    }
    
    /**
     * Busca un vendedor en la lista
     * @param criterio tipo de busqueda por correo,cedula o nombre
     * @param texto dato de la cedula o nombre
     * @return vendedores que cumplen parametros de busqueda
     */
    public Vendedor buscarVendedores(String criterio, String texto) {

        boolean encontrado = false;
        Vendedor buscado = null;

        if(criterio.compareTo("Correo")==0 ){
            int cont =0;
            while(cont<vendedores.contar() && !encontrado){
                Vendedor actual = (Vendedor) vendedores.getPos(cont);
                if(actual.getCorreo().compareToIgnoreCase(texto)==0){
                    buscado = actual;
                    encontrado=true;
                }
                cont++;
            }

        }else if(criterio.compareTo("Nombre")==0 ){
            int cont =0;
            while(cont<vendedores.contar() && !encontrado){
                Vendedor actual = (Vendedor) vendedores.getPos(cont);
                if(actual.getNombre().compareTo(texto)==0){
                    buscado = actual;
                    encontrado=true;
                }
                cont++;
            }
        }else if(criterio.compareTo("Cedula")==0 ){
            int cont =0;
            while(cont<vendedores.contar() && !encontrado){
                Vendedor actual = (Vendedor) vendedores.getPos(cont);
                if(actual.getCedula().compareTo(texto)==0){
                    buscado = actual;
                    encontrado=true;
                }
                cont++;
            }
        }

        return buscado;
    }

    public ArrayList<String> getCedulasVendedores(){
        ArrayList<String> cedulas = new ArrayList<>();
        int cont = 0;
        while(cont<vendedores.contar()){
            Vendedor actual = (Vendedor) vendedores.getPos(cont);
            cedulas.add(actual.getCedula());
            cont++;
        }
        return cedulas;
    }

    public ArrayList<String> getCedulasClientes(){
        ArrayList<String> cedulas = new ArrayList<>();
        int cont = 0;
        while(cont<clientes.contar()){
            Cliente actual = (Cliente) clientes.getPos(cont);
            cedulas.add(actual.getCedula());
            cont++;
        }
        return cedulas;
    }

    public ArrayList<String> getPlacasVehiculo(){
        ArrayList<String> placas = new ArrayList<>();
        int cont = 0;
        while(cont<vehiculos.contar()){
            Vehiculo actual = (Vehiculo) vehiculos.getPos(cont);
            placas.add(actual.getPlaca());
            cont++;
        }
        return placas;
    }

    public Vendedor asignarVendedor(String hora,Date fecha) throws Exception{
        boolean encontrado = false;
        Vendedor buscado = null;
        int con = 0;

        while(con<vendedores.contar()&& !encontrado)
        {
            Vendedor actual = (Vendedor) vendedores.getPos(con);
            int entero = Integer.parseInt(hora);
            if(actual.disponible(fecha,entero)){
                buscado = actual;
                encontrado=true;
            }
            con++;
        }
        return buscado;
    }

}

