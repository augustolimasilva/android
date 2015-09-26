package com.example.asilva.bookbuy.basicas;

import java.io.Serializable;

public class Restaurante implements Serializable {

    private int idRestaurante;
    private String nome;
    private String telefone;
    private float latitude;
    private String endereco;
    private String bairro;
    private float longitude;

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
