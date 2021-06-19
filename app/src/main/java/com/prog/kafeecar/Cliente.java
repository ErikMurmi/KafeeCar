/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.kafeecar;

import java.util.Date;


public class Cliente extends Usuario{

    
    Lista favoritos;
    private String imagen;
    /**
     * Este es el constructor que inicialeza los parámetros 
     * @param nombre Parámetro de cliente
     * @param cedula Parámetro de cliente
     * @param telefono Parámetro de cliente
     * @param correo Parámetro de cliente
     * @param clave Parámetro de cliente
     */
    public Cliente(String nombre, String cedula, String telefono, String correo, String clave, Date fechaNacimiento,String imagen) {
        super(nombre, cedula,telefono, correo, clave,fechaNacimiento);
        this.imagen = imagen;
        favoritos = new Lista();
    }
    public Cliente(){

    }
    public Lista getFavoritos() {
        return favoritos;
    }


    public void setFavoritos(Lista favoritos) {
        this.favoritos = favoritos;
    }

    @Override
    public String toString() {
        String datos = super.toString();
        return "Cliente\n" +
                datos + 
                "\nFavoritos: \n" + favoritos;
    }
    public String getImagen(){
        return imagen;
    }
    public void setImagen(String imagen){
        this.imagen = imagen;
    }


    //Metodos para el sistemas
     
     /**
     * Este método permite al cliente poder crear una lista de los vehículos favoritos
     * @param matricula del vehiculo favorito a agregar
     * @return  true si se agrego,caso contrario false
     */
    public Boolean aniadirFavorito(String matricula ){
        favoritos.add(matricula);
        return false;
    }
    
    /**
     * Este método ayuda al cliente a borrar elementos de la lista de favoritos
     * @param matricula del vehiculo a remover de favoritos
     * @return true si se borro, caso contrario false
     */
    public void removeFavorito(String matricula) throws Exception {

        if (favoritos.esVacia()) {
            throw new Exception("Esta vacia");
        }
        int cont=0;
        while (cont< favoritos.contar()) {
            String vehiculo=(String)favoritos.getPos(cont);
            if (vehiculo.compareToIgnoreCase(matricula) == 0) {
                favoritos.eliminarPos(cont);
                break;
            }
            cont++;
        }

    }

    
}
