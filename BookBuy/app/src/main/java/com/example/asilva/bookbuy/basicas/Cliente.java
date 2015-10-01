package com.example.asilva.bookbuy.basicas;

import android.widget.ImageView;

import java.util.List;

/**
 * Created by wildsonsantos on 08/09/2015.
 */
public class Cliente{

    private int id;
    private String login;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private byte[] fotoPerfil;
    private List<Pedido> pedidos;
    private List<Reserva> reservas;


    public Cliente(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;

        Cliente cliente = (Cliente) o;

        if (getId() != cliente.getId()) return false;
        if (!getLogin().equals(cliente.getLogin())) return false;
        if (!getNome().equals(cliente.getNome())) return false;
        if (!getEmail().equals(cliente.getEmail())) return false;
        if (!getTelefone().equals(cliente.getTelefone())) return false;
        if (!getSenha().equals(cliente.getSenha())) return false;
        if (!getFotoPerfil().equals(cliente.getFotoPerfil())) return false;
        if (!getPedidos().equals(cliente.getPedidos())) return false;
        return getReservas().equals(cliente.getReservas());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + getNome().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getTelefone().hashCode();
        result = 31 * result + getSenha().hashCode();
        result = 31 * result + getFotoPerfil().hashCode();
        result = 31 * result + getPedidos().hashCode();
        result = 31 * result + getReservas().hashCode();
        return result;
    }
}
