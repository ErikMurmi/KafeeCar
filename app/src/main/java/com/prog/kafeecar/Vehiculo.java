package com.prog.kafeecar;

public class Vehiculo {
    
    private String placa;
    private String matricula;
    private String marca;
    private String modelo;
    private String color;
    private String descripcion;
    private String imagen;
    
    private float precioInicial;
    private float precioVenta;
    private float promocion;
    private boolean matriculado;
    private int anio;

    public Vehiculo(String placa, String matricula, String marca, String modelo, String color, String descripcion,
                    float precioInicial, float precioVenta, float promocion, boolean matriculado, int anio, String imagen) {
        this.placa = placa;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.descripcion = descripcion;
        this.precioInicial = precioInicial;
        this.precioVenta = precioVenta;
        this.promocion = promocion;
        this.matriculado = matriculado;
        this.anio = anio;
        this.imagen = imagen;
    }
   
    public String getPlaca() {
        return placa;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getColor() {
        return color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getPrecioInicial() {
        return precioInicial;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public float getPromocion() {
        return promocion;
    }

    public boolean isMatriculado() {
        return matriculado;
    }

    public int getAnio() {
        return anio;
    }

    public String getimagen()
    {
        return imagen;
    }

   /***
    * Metodo actualizardatos se encargara de actualizar todos los datos de un vehiculo
    * @param placa actualiza la placa
    * @param matricula actualiza la matricula
    * @param marca actualiza la marca
    * @param modelo actualiza el modelo
    * @param color actualiza el color
    * @param descripcion actualiza la descripcion 
    * @param precioInicial actualiza el precio inicial 
    * @param precioVenta actualiza el precio de venta
    * @param promocion actualiza la promocion del vehiculo
    * @param matriculado actualiza el estado de matricula
    * @param anio actualiza el a??o del vehiculo
    * @param imagen actualiza la imagen del vehiculo
    */
    public void actualizarDatos(String placa, String matricula, String marca, String modelo, String color, String descripcion,
                                float precioInicial, float precioVenta, float promocion, boolean matriculado, int anio, String imagen)
    {
        this.placa = placa;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.descripcion = descripcion;
        this.precioInicial = precioInicial;
        this.precioVenta = precioVenta;
        this.promocion = promocion;
        this.matriculado = matriculado;
        this.anio = anio;
        this.imagen = imagen;
    }
    
    /***
     * El metodo ActualizarPlaca nos ayudara a actualizar solamente el dato de la placa del vehiculo
     * @param placa contiene la nueva placa
     */
    public void actualizarPlaca(String placa)
    {
        this.placa = placa;
    }
    
    /***
     * El metodo ActualizarMatricula cambiara solo la matricula del objeto deseado
     * @param matricula contiene la nueva matricula
     */
    public void actualizarMatricula(String matricula)
    {
        this.matricula = matricula;
    }
    
    /***
     * El metodo ActualizarMarca nos permitira actualizar solamente la marca del vehiculo
     * @param Marca contiene la nueva marca
     */
    public void actualizarMarca(String Marca)
    {
        this.marca = Marca;
    }
    
    /***
     * El metodo ActualizarModelo nos permite cambiar solamente el modelo del vehiculo
     * @param Modelo contiene el nuevo modelo de vehiculo
     */
    public void actualizarModelo(String Modelo)
    {
        this.modelo = Modelo;
    }
    
    /***
     * El metodo ActualizarColor nos permite cambiar solamente el dato de color del vehiculo
     * @param color contiene el nuevo color del vehiculo
     */
    public void actualizarColor(String color)
    {
        this.color = color;
    }
    
    /***
     * El metodo ActualizarDescripcion cambia unicamente la descripcion del vehiculo
     * @param Descripcion contiene la nueva descripcion
     */
    public void actualizarDescripcion(String Descripcion)
    {
        this.descripcion = Descripcion;
    }
    
    /***
     * El metodo ActiualizarPrecioInicial cambia el dato del Precio Inicial
     * @param precioInicial contiene el nuevo precio inicial
     */
    public void actualizarPrecioInicial(float precioInicial)
    {
        this.precioInicial = precioInicial;
    }
    
    /***
     * E metodo ActualizarPrecioVenta cambia el precio de venta del vehiculo
     * @param precioVenta contiene el nuevo precio de venta
     */
    public void actualizarPrecioVenta(float precioVenta)
    {
        this.precioVenta = precioVenta;
    }
    
    /***
     * El metodo ActualizarPromocion cambia el valor de promocion del vehiculo
     * @param promocion contiene el nuevo valor de la promocion
     */
    public void actualizaPromocion(float promocion)
    {
        this.promocion = promocion;
    }
    
    /***
     * El metodo ActualizarMatriculado cambia el estado del vehiculo enre matriculado y no matriculado
     * @param matriculado contiene el nuevo estado de matricula del vehiculo
     */
    public void actualizarMatriculado(boolean matriculado)
    {
        this.matriculado = matriculado;
    }
    
    /***
     * El metodo ActualizarAnio cabia el a??o de Vehiculo
     * @param Anio contiene el nuevo a??o del vehiculo
     */
    public void actualizarAnio(int Anio)
    {
        this.anio = Anio;
    }
    
    /***
     * El metodo actualizarImagen cambia la imagen del vehiculo
     * @param imagen contiene la nueva imagen a cambiar
     */
    public void actualizarImagen(String imagen)
    {
        this.imagen= imagen;
    }    
    
    @Override
    public String toString() {
        return "Vehiculo" 
                + "\nPlaca: " + placa 
                + "\nMatricula: " + matricula 
                + "\nMarca: " + marca 
                + "\nModelo: " + modelo 
                + "\nColor: " + color 
                + "\nDescripcion: " + descripcion 
                + "\nPrecio Inicial: " + precioInicial 
                + "\nPrecio Venta: " + precioVenta 
                + "\nPromocion: " + promocion 
                + "\nMatriculado: " + matriculado 
                + "\nAnio: " + anio
                + "\nImagen: "+ imagen;
    }
}
  


