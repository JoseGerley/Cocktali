package Modelo;

import java.util.HashMap;

public class Pedido {

    private String id;
    private String idCoctelero;
    private String idUsuario;
    private String celularCoctelero;
    private String direccionCliente;
    private String estado;
    private HashMap<String,Coctel> cocteles;
    private String fotoCoctelero;
    private String fotoUsuario;
    private String nombreCoctelero;
    private String nombreUsuario;


    public Pedido() {
    }

    public Pedido(String id, String idCoctelero, String idUsuario,  //LLaves foraneas
                  String celularCoctelero, String direccionCliente, String estado, String fotoCoctelero, String fotoUsuario,
                  String nombreCoctelero, String nombreUsuario) {
        this.id = id;
        this.idCoctelero = idCoctelero;
        this.idUsuario = idUsuario;
        this.celularCoctelero = celularCoctelero;
        this.direccionCliente = direccionCliente;
        this.estado = estado;
        this.fotoCoctelero = fotoCoctelero;
        this.fotoUsuario = fotoUsuario;
        this.nombreCoctelero = nombreCoctelero;
        this.nombreUsuario = nombreUsuario;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCoctelero() {
        return idCoctelero;
    }

    public void setIdCoctelero(String idCoctelero) {
        this.idCoctelero = idCoctelero;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCelularCoctelero() {
        return celularCoctelero;
    }

    public void setCelularCoctelero(String celularCoctelero) {
        this.celularCoctelero = celularCoctelero;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public HashMap<String, Coctel> getCocteles() {
        return cocteles;
    }

    public void setCocteles(HashMap<String, Coctel> cocteles) {
        this.cocteles = cocteles;
    }

    public String getFotoCoctelero() {
        return fotoCoctelero;
    }

    public void setFotoCoctelero(String fotoCoctelero) {
        this.fotoCoctelero = fotoCoctelero;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getNombreCoctelero() {
        return nombreCoctelero;
    }

    public void setNombreCoctelero(String nombreCoctelero) {
        this.nombreCoctelero = nombreCoctelero;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
