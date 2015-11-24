package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Pedido;
import com.example.asilva.bookbuy.callbacks.AtualizarPedidoListener;
import com.example.asilva.bookbuy.callbacks.PedidoClienteListener;
import com.example.asilva.bookbuy.callbacks.PedidoListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAOPedido {

    private static final String URL = "http://52.32.87.153:8080/WSbookbuy/services/PedidoDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";
    List<Pedido> listaPedidos = new ArrayList<Pedido>();

    int idPedido = 0;
    boolean retorno = false;

    private static final String INSERIR_PEDIDO = "inserirPedido";
    public static final String BUSCAR_TODOS_PEDIDOS_CLIENTE = "buscarTodosPedidosDoCliente";
    public static final String CANCELAR_PEDIDO = "atualizarPedido";

    public int inserirPedido(Pedido pedido, PedidoListener listener) {
        new InserirPedidoTask(listener).execute(pedido);
        return idPedido;
    }

    public void buscarTodosPedidosDoCliente(int idCliente, PedidoClienteListener listener){
        new BuscarTodosPedidosDoClienteTask(listener).execute(idCliente);
    }

    public void atualizarPedido(Pedido pedido, AtualizarPedidoListener listener){
        new AtualizarPedidoTask(listener).execute(pedido);
    }

    class AtualizarPedidoTask extends AsyncTask<Pedido, Void, Boolean>{

        private final AtualizarPedidoListener  listener;

        public AtualizarPedidoTask(final AtualizarPedidoListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Pedido... params) {

            SoapObject ped = new SoapObject(NAMESPACE, "pedido");

            Pedido pedido = params[0];

            ped.addProperty("idRestaurante", pedido.getIdRestaurante());
            ped.addProperty("dataHora", pedido.getDataHora());
            ped.addProperty("idCliente", pedido.getIdCliente());
            ped.addProperty("situacao", pedido.getSituacao());
            ped.addProperty("idReserva", pedido.getIdReserva());
            ped.addProperty("status", pedido.getStatus());
            ped.addProperty("idPedido", pedido.getIdPedido());
            ped.addProperty("idMesa", pedido.getIdMesa());
            ped.addProperty("tempoEstimado", pedido.getTempoEstimado());

            SoapObject atualizarReserva = new SoapObject(NAMESPACE, CANCELAR_PEDIDO);

            atualizarReserva.addSoapObject(ped);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(atualizarReserva);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + CANCELAR_PEDIDO, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno = Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.onPedido(retorno);
        }
    }

    class BuscarTodosPedidosDoClienteTask extends AsyncTask<Integer, Void, List<Pedido>>{

        private final PedidoClienteListener  listener;

        public BuscarTodosPedidosDoClienteTask(final PedidoClienteListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Pedido> doInBackground(Integer... params) {
            listaPedidos = new ArrayList<Pedido>();

            SoapObject buscarPedidos = new SoapObject(NAMESPACE, BUSCAR_TODOS_PEDIDOS_CLIENTE);
            buscarPedidos.addProperty("idCliente", params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarPedidos);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_TODOS_PEDIDOS_CLIENTE, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    Pedido ped = new Pedido();

                    ped.setIdPedido(Integer.parseInt(resposta.getProperty("idPedido").toString()));
                    ped.setDataHora(resposta.getProperty("dataHora").toString());
                    ped.setIdMesa(Integer.parseInt(resposta.getProperty("idMesa").toString()));
                    ped.setSituacao(resposta.getProperty("situacao").toString());
                    ped.setStatus(resposta.getProperty("status").toString());
                    ped.setTempoEstimado(resposta.getProperty("tempoEstimado").toString());
                    ped.setNomeRestaurante(resposta.getProperty("nomeRestaurante").toString());
                    ped.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                    ped.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                    ped.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));

                    listaPedidos.add(ped);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        Pedido ped = new Pedido();

                        ped.setIdPedido(Integer.parseInt(resposta.getProperty("idPedido").toString()));
                        ped.setDataHora(resposta.getProperty("dataHora").toString());
                        ped.setIdMesa(Integer.parseInt(resposta.getProperty("idMesa").toString()));
                        ped.setSituacao(resposta.getProperty("situacao").toString());
                        ped.setStatus(resposta.getProperty("status").toString());
                        ped.setTempoEstimado(resposta.getProperty("tempoEstimado").toString());
                        ped.setNomeRestaurante(resposta.getProperty("nomeRestaurante").toString());
                        ped.setIdCliente(Integer.parseInt(resposta.getProperty("idCliente").toString()));
                        ped.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                        ped.setIdReserva(Integer.parseInt(resposta.getProperty("idReserva").toString()));

                        listaPedidos.add(ped);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaPedidos;
        }

        @Override
        protected void onPostExecute(final List<Pedido> pedidos) {
            listener.onPedidoCliente(pedidos);
        }
    }

    class InserirPedidoTask extends AsyncTask<Pedido, Void, Integer> {

        private final PedidoListener listener;

        public InserirPedidoTask(final PedidoListener listener) {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(Pedido... params) {
            SoapObject ped = new SoapObject(NAMESPACE, "pedido");

            Pedido pedido = params[0];

            ped.addProperty("idPedido", pedido.getIdPedido());
            ped.addProperty("dataHora", pedido.getDataHora());
            ped.addProperty("situacao", pedido.getSituacao());
            ped.addProperty("status", pedido.getStatus());
            ped.addProperty("tempoEstimado", pedido.getTempoEstimado());
            ped.addProperty("idCliente", pedido.getIdCliente());
            ped.addProperty("idReserva", pedido.getIdReserva());
            ped.addProperty("idMesa", 0);
            ped.addProperty("idRestaurante", pedido.getIdRestaurante());

            SoapObject inserirPedido = new SoapObject(NAMESPACE, INSERIR_PEDIDO);

            inserirPedido.addSoapObject(ped);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(inserirPedido);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + INSERIR_PEDIDO, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return idPedido = Integer.parseInt(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return idPedido;
            }
        }

        @Override
        protected void onPostExecute(final Integer idPedido) {
            listener.onPedido(idPedido);
        }
    }
}
