package com.example.asilva.bookbuy.basicas;

import java.util.List;

public class Cliente{

    private int id;
    private String login;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private byte[] fotoPerfil;
    private List<PedidoProduto> pedidoProdutos;
    private List<Reserva> reservas;
    private String situacao;

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

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

    public List<PedidoProduto> getPedidoProdutos() {
        return pedidoProdutos;
    }

    public void setPedidoProdutos(List<PedidoProduto> pedidoProdutos) {
        this.pedidoProdutos = pedidoProdutos;
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
        if (!getPedidoProdutos().equals(cliente.getPedidoProdutos())) return false;
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
        result = 31 * result + getPedidoProdutos().hashCode();
        result = 31 * result + getReservas().hashCode();
        return result;
    }
}
