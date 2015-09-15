package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.activities.ClienteListener;
import com.example.asilva.bookbuy.basicas.Cliente;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by wildsonsantos on 08/09/2015.
 */
public class DAOCliente {

    private static final String URL = "http://54.149.96.214:8080/bookbuyWS/services/ClienteDAO?wsdl";
    private static final String NAMESPACE = "http://bookbuyWS";

    private static final String INSERIR = "inserirCliente";
    private static final String EXCLUIR = "excluirCliente";
    private static final String ATUALIZAR = "atualizarCliente";
    private static final String BUSCAR_TODOS = "buscarTodosClientes";
    private static final String BUSCAR_POR_ID = "buscarClientePorId";
    private static final String BUSCAR_POR_LOGIN = "buscarClientePorLogin";
    Cliente cli;

    public boolean inserirCliente(Cliente cliente) {
        ClienteTask clienteTask = new ClienteTask();
        clienteTask.execute(cliente);
        return true;
    }

    public void pesquisarClientePorLogin(String login, ClienteListener listener) {
        new PesquisarClienteTask(listener).execute(login);
    }

    public boolean atualizarCliente(Cliente cliente) {
        AtualizarClienteTask atualizarClienteTask = new AtualizarClienteTask();
        atualizarClienteTask.execute(cliente);
        return true;
    }

    class ClienteTask extends AsyncTask<Cliente, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Cliente... params) {

            SoapObject cli = new SoapObject(NAMESPACE, "cliente");

            Cliente cliente = params[0];

            cli.addProperty("id", cliente.getId());
            cli.addProperty("login", cliente.getUsuario());
            cli.addProperty("nome", cliente.getNome());
            cli.addProperty("email", cliente.getEmail());
            cli.addProperty("telefone", cliente.getTelefone());
            cli.addProperty("senha", cliente.getSenha());

            SoapObject inserirCliente = new SoapObject(NAMESPACE, INSERIR);

            inserirCliente.addSoapObject(cli);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(inserirCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + INSERIR, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    class PesquisarClienteTask extends AsyncTask<String, Void, Cliente> {

        private final ClienteListener listener;

        private PesquisarClienteTask(final ClienteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Cliente doInBackground(String... params) {

            SoapObject buscarCliente = new SoapObject(NAMESPACE, BUSCAR_POR_LOGIN);
            buscarCliente.addProperty("login", params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {

                http.call("urn:" + BUSCAR_POR_LOGIN, envelope);

                SoapObject resposta = (SoapObject) envelope.getResponse();

                final Cliente cliente = new Cliente();

                cliente.setId(Integer.parseInt(resposta.getProperty("id").toString()));
                cliente.setNome(resposta.getProperty("nome").toString());
                cliente.setEmail(resposta.getProperty("email").toString());
                cliente.setLogin(resposta.getProperty("login").toString());
                cliente.setSenha(resposta.getProperty("senha").toString());

                return cliente;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Cliente cliente) {
            listener.onLogin(cliente);
        }
    }

    class AtualizarClienteTask extends AsyncTask<Cliente, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Cliente... params) {

            SoapObject cli = new SoapObject(NAMESPACE, "cliente");

            Cliente cliente = params[0];

            cli.addProperty("id", cliente.getId());
            cli.addProperty("nome", cliente.getNome());
            cli.addProperty("email", cliente.getEmail());
            cli.addProperty("telefone", cliente.getTelefone());

            SoapObject atualizarCliente = new SoapObject(NAMESPACE, ATUALIZAR);

            atualizarCliente.addSoapObject(cli);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(atualizarCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + ATUALIZAR, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
