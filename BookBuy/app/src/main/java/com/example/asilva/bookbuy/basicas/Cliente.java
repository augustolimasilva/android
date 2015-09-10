package com.example.asilva.bookbuy.basicas;

import android.widget.ImageView;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

/**
 * Created by wildsonsantos on 08/09/2015.
 */
public class Cliente extends Usuario{

    private int id;

    @NotEmpty(message = "É necessário preencher este campo!")
    private String login;

    @NotEmpty(message = "É necessário preencher este campo!")
    private String nome;

    @NotEmpty(message = "É necessário preencher este campo!")
    @Email(message = "Email inválido")
    private String email;

    @NotEmpty(message = "É necessário preencher este campo!")
    private String telefone;

    @NotEmpty(message = "É necessário preencher este campo!")
    @Password(min = 6, scheme = Password.Scheme.NUMERIC, message = "Senha Inválida")
    private String senha;

    private ImageView fotoPerfil;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public ImageView getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(ImageView fotoPerfil) {
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


    public String getUsuario() {
        return login;
    }

    public void setUsuario(String usuario) {
        this.login = usuario;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
