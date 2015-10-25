package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.callbacks.EfetuarReservaListener;
import com.example.asilva.bookbuy.callbacks.ReservaListener;
import com.example.asilva.bookbuy.callbacks.ReservasClienteListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAOReserva {

    private static final String URL = "http://52.25.38.52:8080/WSbookbuy/services/ReservaDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";
    List<Reserva> listaReservas = new ArrayList<Reserva>();

    private static final String BUSCAR_TODOS = "buscarTodasReservas";
    private static final String ATUALIZAR_RESERVA  = "atualizarReserva";
    public static final String BUSCAR_RESERVAS_CLIENTE = "buscarTodasReservasDoCliente";
    public static final String BUSCAR_RESERVAS_CLIENTE_RESTAURANTE = "buscarTodasReservasClienteRestaurante";

    boolean retorno = false;

    public void buscarTodasReservas(int idRestaurante, ReservaListener listener) {
        new ReservasTask(listener).execute(idRestaurante);
    }

    public boolean atualizarReserva(Reserva reserva, EfetuarReservaListener listener){
        new AtualizarReservaTask(listener).execute(reserva);
        return retorno;
    }

    public void buscarReservasDoCliente(int idCliente, ReservasClienteListener listener){
        new BuscarReservasDoClienteTask(listener).execute(idCliente);
    }

    public void buscarReservasDoClienteRestaurante(int idRestaurante,int idCliente, ReservasClienteListener listener){
        new BuscarReservasDoClienteRestauranteTask(listener).execute(idRestaurante, idCliente);
    }

    class BuscarReservasDoClienteRestauranteTask extends AsyncTask<Integer, Void, List<Reserva>>{

        private final ReservasClienteListener listener;

        public BuscarReservasDoClienteRestauranteTask(final ReservasClienteListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Reserva> doInBackground(Integer... params) {

            listaReservas = new ArrayList<Reserva>();

            SoapObject buscarReservas = new SoapObject(NAMESPACE, BUSCAR_RESERVAS_CLIENTE_RESTAURANTE);
            buscarReservas.addProperty("idRestaurante", params[0]);
            buscarReservas.addProperty("idCliente", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarReservas);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_RESERVAS_CLIENTE_RESTAURANTE, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    Reserva res = new Reserva();

                    res.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));
                    res.setDataHora(resposta.getProperty("dataHora").toString());
                    res.setQtdPessoas(Integer.parseInt(resposta.getProperty("qtdPessoas").toString()));
                    res.setSituacao(resposta.getProperty("situacao").toString());
                    res.setStatus(resposta.getProperty("status").toString());
                    res.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                    res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));

                    listaReservas.add(res);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        Reserva res = new Reserva();

                        res.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));
                        res.setDataHora(resposta.getProperty("dataHora").toString());
                        res.setQtdPessoas(Integer.parseInt(resposta.getProperty("qtdPessoas").toString()));
                        res.setSituacao(resposta.getProperty("situacao").toString());
                        res.setStatus(resposta.getProperty("status").toString());
                        res.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                        res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));

                        listaReservas.add(res);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaReservas;
        }

        @Override
        protected void onPostExecute(final List<Reserva> reservas) {
            listener.onReserva(reservas);
        }
    }

    class BuscarReservasDoClienteTask extends AsyncTask<Integer, Void, List<Reserva>>{

        private final ReservasClienteListener listener;

        public BuscarReservasDoClienteTask(final ReservasClienteListener listener) {
            this.listener = listener;
        }

            @Override
            protected List<Reserva> doInBackground(Integer... params) {

                listaReservas = new ArrayList<Reserva>();

                SoapObject buscarReservas = new SoapObject(NAMESPACE, BUSCAR_RESERVAS_CLIENTE);
                buscarReservas.addProperty("idCliente", params[0]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(buscarReservas);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call("urn:" + BUSCAR_RESERVAS_CLIENTE, envelope);

                    if (envelope.getResponse() instanceof SoapObject) {
                        SoapObject resposta = (SoapObject) envelope.getResponse();

                        Reserva res = new Reserva();

                        res.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));
                        res.setDataHora(resposta.getProperty("dataHora").toString());
                        res.setQtdPessoas(Integer.parseInt(resposta.getProperty("qtdPessoas").toString()));
                        res.setSituacao(resposta.getProperty("situacao").toString());
                        res.setStatus(resposta.getProperty("status").toString());
                        res.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                        res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                        res.setNomeRestaurante(resposta.getPropertyAsString("nomeRestaurante").toString());

                        listaReservas.add(res);

                    } else {
                        Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                        for (SoapObject resposta : retorno) {

                            Reserva res = new Reserva();

                            res.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));
                            res.setDataHora(resposta.getProperty("dataHora").toString());
                            res.setQtdPessoas(Integer.parseInt(resposta.getProperty("qtdPessoas").toString()));
                            res.setSituacao(resposta.getProperty("situacao").toString());
                            res.setStatus(resposta.getProperty("status").toString());
                            res.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                            res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                            res.setNomeRestaurante(resposta.getPropertyAsString("nomeRestaurante").toString());

                            listaReservas.add(res);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return listaReservas;
            }

            @Override
            protected void onPostExecute(final List<Reserva> reservas) {
                listener.onReserva(reservas);
            }
        }

    class AtualizarReservaTask extends AsyncTask<Reserva, Void, Boolean> {

        private final EfetuarReservaListener listener;

        public AtualizarReservaTask(final EfetuarReservaListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Reserva... params) {

            SoapObject res = new SoapObject(NAMESPACE, "reserva");

            Reserva reserva = params[0];

            res.addProperty("idReserva", reserva.getIdReserva());
            res.addProperty("dataHora", reserva.getDataHora());
            res.addProperty("qtdPessoas", reserva.getQtdPessoas());
            res.addProperty("situacao", reserva.getSituacao());
            res.addProperty("status", reserva.getStatus());
            res.addProperty("idCliente", reserva.getIdCliente());
            res.addProperty("idRestaurante", reserva.getIdRestaurante());

            SoapObject atualizarReserva = new SoapObject(NAMESPACE, ATUALIZAR_RESERVA);

            atualizarReserva.addSoapObject(res);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(atualizarReserva);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + ATUALIZAR_RESERVA, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno = Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.atualizarReserva(retorno);
        }
    }

    class ReservasTask extends AsyncTask<Integer, Void, List<Reserva>> {

        private final ReservaListener listener;

        public ReservasTask(final ReservaListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Reserva> doInBackground(Integer... params) {
            listaReservas = new ArrayList<Reserva>();

            SoapObject buscarReservas = new SoapObject(NAMESPACE, BUSCAR_TODOS);
            buscarReservas.addProperty("idRestaurante", params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarReservas);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_TODOS, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    Reserva res = new Reserva();

                    res.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));
                    res.setDataHora(resposta.getProperty("dataHora").toString());
                    res.setQtdPessoas(Integer.parseInt(resposta.getProperty("qtdPessoas").toString()));
                    res.setSituacao(resposta.getProperty("situacao").toString());
                    res.setStatus(resposta.getProperty("status").toString());
                    res.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                    res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));

                    listaReservas.add(res);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        Reserva res = new Reserva();

                        res.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));
                        res.setDataHora(resposta.getProperty("dataHora").toString());
                        res.setQtdPessoas(Integer.parseInt(resposta.getProperty("qtdPessoas").toString()));
                        res.setSituacao(resposta.getProperty("situacao").toString());
                        res.setStatus(resposta.getProperty("status").toString());
                        res.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                        res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));

                        listaReservas.add(res);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaReservas;
        }
        @Override
        protected void onPostExecute(final List<Reserva> reservas) {
            listener.onReserva(reservas);
        }
    }
}
