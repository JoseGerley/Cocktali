package Modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class Coctelero {

    private String id;

    private String nombre;

    private String description;

    private String numero;

    private String foto;

    private HashMap<String,Coctel> cocteles;

    private  String correo;

    private  String contraseña;



    public Coctelero() {

    }

    public Coctelero(String id, String nombre, String description, String numero, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.description = description;
        this.numero = numero;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }



    /**
     * Constructor principal para registrar Coctelero
     * @param id
     * @param nombre
     * @param description
     * @param numero
     * @param correo
     * @param contraseña
     * @param foto
     */
    public Coctelero(String id, String nombre, String description, String numero, String correo, String contraseña, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.description = description;
        this.numero = numero;
        this.correo = correo;
        this.contraseña = contraseña;
        this.foto = foto;

    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public HashMap<String, Coctel> getCocteles() {
        return cocteles;
    }

    public void setCocteles(HashMap<String, Coctel> cocteles) {
        this.cocteles = cocteles;
    }
}
