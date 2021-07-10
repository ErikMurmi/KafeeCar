
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
    private String direccion;
    private String telefono;

    public PatioVenta() {
        vendedores = new Lista();
        ventasGenerales = new Lista();
        clientes = new Lista();
        vehiculos = new Lista();
        citas = new Lista();
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
    public void aniadirVehiculo(Vehiculo nuevo){
        vehiculos.add(nuevo);

    }

    /**
     * Agrega una venta
     * @param nueva venta a ser agregada
     * @return true si se agrego , false en caso de que no.
     */
    public void aniadirVenta(Venta nueva) throws Exception {
        ventasGenerales.add(nueva);
        if (nueva.getVehiculo().contar()==1){
            Vehiculo actual = (Vehiculo) nueva.getVehiculo().getPos(0);
            removerVehiculo(actual.getPlaca());
        }else{
            for (int i = 1; i<nueva.getVehiculo().contar(); i++){
                Vehiculo actual = (Vehiculo) nueva.getVehiculo().getPos(i);
                removerVehiculo(actual.getPlaca());
            }
        }
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


    /**
     * Remueve una venta
     * @param placa dato del vehiculo de la venta a ser eliminada
     * @return true si se elimino , false en caso de que no.
     */
    public void removerVenta(String placa) throws Exception {

        boolean encontrado = false;
        int cont =0;
        while(cont<ventasGenerales.contar() && !encontrado){
            Venta actual = (Venta) ventasGenerales.getPos(cont);
            if(actual.buscarVehiculo(placa)!=null){
                ventasGenerales.eliminarPos(cont);
                encontrado=true;
            }
            cont++;
        }
    }


    /**
     * Agrega un cliente a la lista
     * @param cedula identificador del usuario a eliminar
     * @param tipo el tipo de usuario cliente o vendedor
     * @return true si se elimino el usuario, false en caso de que no.
     */
    public boolean removerUsuario(String cedula, String tipo) throws Exception {
        boolean removido = false;
        int c=0;
        if(tipo.compareToIgnoreCase("Vendedor")==0)
        {
            while(c < vendedores.contar() && !removido)
            {
                Vendedor actual = (Vendedor) vendedores.getPos(c);
                if(actual.getCedula().compareTo(cedula)==0)
                {
                    vendedores.eliminar(actual);
                    removido = true;
                }
                c++;
            }
        }
        else if(tipo.compareToIgnoreCase("Cliente")==0)
        {
            while(c< clientes.contar() && !removido)
            {
                Cliente actual = (Cliente) clientes.getPos(c);
                if(actual.getCedula().compareTo(cedula)==0)
                {
                    clientes.eliminar(actual);
                    removido = true;
                }
                c++;
            }
        }
        return removido;
    }

    
    /**
     * Busca citas por distintos criterios
     * @param criterio atributo por el cual buscar del cliente
     * @param vehiculo palabra a buscar en el criterrio
     * @return Lista de citas que corresponden al criterio de busquda
     */
    public Cita buscarCitas(String criterio, String vehiculo, String cliente){
        Cita citaEncontrada;

        if(criterio.compareToIgnoreCase("Vehiculo")==0){
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getVehiculo().getPlaca().compareTo(vehiculo)==0 && actual.getCliente().getCedula().compareTo(cliente)==0){
                    citaEncontrada = actual;
                    return citaEncontrada;
                }
                cont++;
            }
        }else if(criterio.compareToIgnoreCase("Vendedor")==0){
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getVendedorCita().getNombre().compareTo(vehiculo)==0){
                    citaEncontrada = actual;
                    return citaEncontrada;
                }
                cont++;
            }
        } else if(criterio.compareToIgnoreCase("Cliente")==0){
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getCliente().getCedula().compareTo(vehiculo)==0){
                    citaEncontrada = actual;
                    return citaEncontrada;
                }
                cont++;
            }
        }
        return null;
    }

    public Lista buscarporfecha(Date fech) {
        Cita citaEncontrada;
        Lista citaslista=new Lista();
            int cont = 0;
            while(cont<citas.contar()){
                Cita actual = (Cita) citas.getPos(cont);
                if(actual.getFechaCita().compareTo(fech)==0){
                    citaEncontrada=actual;
                    citaslista.add(citaEncontrada);
                    return citaslista;
                }
                cont++;
            }

        return null;

    }



    
    /**
     *
     * Busca vehiculos por sus caracteristicas
     * @param criterio caracteristica de busqueda de vehiculos
     * @param codigo palabra a buscar dentro del criterio
     * @return Vehiculo que coincide con el codigo buscado en el criterio de busqueda.
     */
    public Vehiculo buscarVehiculos(String criterio, String codigo) throws Exception {
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
            if(actual.getComprador().getCedula().compareTo(cedula)==0 && actual.buscarVehiculo(placa)!=null){
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
    public Cliente buscarClientes(String criterio, String codigo) throws Exception {
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
    public Vendedor buscarVendedores(String criterio, String texto) throws Exception {

        boolean encontrado = false;
        Vendedor buscado = null;

        if(criterio.compareTo("Correo")==0 ){
            int cont =0;
            while(cont<vendedores.contar() && !encontrado){
                Vendedor actual = (Vendedor) vendedores.getPos(cont);
                if(actual.getCorreo().compareTo(texto)==0){
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


    /**
     * Actualiza todos los datos de un vehículo
     * @param placa codigo para buscar el vehiculo
     * @param nuevaPlaca codigo para buscar el vehiculo
     * @param matricula actualiza la matricula
     * @param marca actualiza la marca
     * @param modelo actualiza el modelo
     * @param color actualiza el color
     * @param descripcion actualiza la descripcion
     * @param precioInicial actualiza el precio inicial
     * @param precioVenta actualiza el precio de venta
     * @param promocion actualiza la promocion del vehiculo
     * @param matriculado actualiza el estado de matricula
     * @param anio actualiza el año del vehiculo
     * @param imagen actualiza la imagen del vehiculo
     */
    public Vehiculo actualizarVehiculo(String placa, String nuevaPlaca,String matricula, String marca, String modelo, String color, String descripcion, float precioInicial, float precioVenta, float promocion, boolean matriculado, int anio, String imagen)
    {
        return null;
    }


    /**
     * Actualizar los datos de una venta
     * @param matricula identificador para buscar el vehiculo
     * @param fecha fecha de la venta
     * @param vendedor agente que la vendio
     * @param comprador cliente que compro el vehiculo
     * @param vehiculos vehiculos de la venta
     * @return Venta actualizada
     */
    public Venta actualizarVenta(String matricula,Date fecha, Vendedor vendedor,Cliente comprador,Lista vehiculos){
        //Comprobar si el nuevo vehiculo es el mismo que el que ya estaba en la cita
        // si no volver a aniadir el vehiculo al catalogo
        return null;
    }


    /**
     * Actualiza los datos de un cliente o vendedor
     * @param cedula identificador para buscar el usuario
     * @param nombre nombre dato actualizado
     * @param nuevaCedula cedula dato actualizado
     * @param correo correo dato actualizado
     * @param telefono telefono de contacto del cliente
     * @return cliente con los datos actulizados
     */
    public Cliente actualizarCliente(String cedula, String nombre,String nuevaCedula, String correo,String telefono){
        return null;
        
    }
    
    /**
     * Actualiza los datos de un cliente o vendedor
     * @param cedula identificador para buscar el usuario
     * @param nombre nombre dato actualizado
     * @param nuevaCedula cedula dato actualizado
     * @param correo correo dato actualizado
     * @param telefono telefono de contacto del cliente
     * @return Vendedor con los datos actualizados
     */
    public Vendedor actualizarVendedor(String cedula, String nombre,String nuevaCedula, String correo,String telefono){
        
        return null;
    }


    /**
     * Actualiza todos los datos de la cita
     * @param matricula del vehiculo de la cita que se busca
     * @param cedula del cliente que agendo esa cita
     * @param fechaCita dato nuevo de la fecha de la cita
     * @param hora dato nuevo de la hora de la cita
     * @param resolucion dato nuevo de la resolucion de la cita
     * @param cliente dato nuevo del cliente de la cita
     * @param vendedorCita dato nuevo del vendedor de la cita
     * @param vehiculo dato nuevo del vehiculo de la cita
     * @return cita con los datos actualizados
     */

    public Cita actualizarDatosCita(String matricula, String cedula,Date fechaCita, int hora, String resolucion, Cliente cliente, Vendedor vendedorCita, Vehiculo vehiculo){
        Cita cita = null;
        return cita;
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

