package com.example.asilva.bookbuy.basicas;

import java.io.Serializable;

public class Restaurante implements Serializable {

    public int idRestaurante;
    public String cnpj;
    public String email;
    public String nome;
    public String telefone;
    public float latitude;
    public float longitude;
    public String bairro;
    public String cep;
    public String cidade;
    public String rua;
    public String numero;
    public String complemento;
    public byte[] logo;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurante)) return false;

        Restaurante that = (Restaurante) o;

        if (getIdRestaurante() != that.getIdRestaurante()) return false;
        if (Float.compare(that.getLatitude(), getLatitude()) != 0) return false;
        if (Float.compare(that.getLongitude(), getLongitude()) != 0) return false;
        if (!getNome().equals(that.getNome())) return false;
        return getTelefone().equals(that.getTelefone());

    }
}
