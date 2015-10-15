package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.callbacks.CadastroClienteListener;
import com.example.asilva.bookbuy.callbacks.ClienteListener;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.callbacks.EfetuarReservaListener;
import com.example.asilva.bookbuy.callbacks.EsqueceuSenhaListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DAOCliente {

    private static final String URL = "http://52.25.165.254:8080/WSbookbuy/services/ClienteDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";

    private static final String INSERIR = "inserirCliente";
    private static final String ATUALIZAR = "atualizarCliente";
    private static final String BUSCAR_POR_LOGIN = "buscarClientePorLogin";
    private static final String BUSCAR_POR_EMAIL = "buscarClientePorEmail";
    public static final String ESQUECEU_SENHA = "recuperarSenha";

    boolean retorno = false;

    public boolean inserirCliente(Cliente cliente, CadastroClienteListener listener) {
        new ClienteTask(listener).execute(cliente);
        return retorno;
    }

    public void pesquisarClientePorLogin(String login, ClienteListener listener) {
        new PesquisarClienteTask(listener).execute(login);
    }

    public void pesquisarClientePorEmail(String email, ClienteListener listener) {
        new PesquisarClienteTaskEmail(listener).execute(email);
    }

    public boolean atualizarCliente(Cliente cliente, CadastroClienteListener listener) {
        new AtualizarClienteTask(listener).execute(cliente);
        return retorno;
    }

    public boolean esqueceuSenha(String email, EsqueceuSenhaListener listener){
        new EsqueceuSenhaTask(listener).execute(email);
        return retorno;
    }

    class EsqueceuSenhaTask extends AsyncTask<String, Void, Boolean> {

        private final EsqueceuSenhaListener listener;

        public EsqueceuSenhaTask(final EsqueceuSenhaListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            SoapObject buscarCliente = new SoapObject(NAMESPACE, ESQUECEU_SENHA);
            buscarCliente.addProperty("email", params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + ESQUECEU_SENHA, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno = Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.esqueceuSenha(retorno);
        }
    }

    class ClienteTask extends AsyncTask<Cliente, Void, Boolean> {

        private final CadastroClienteListener listener;

        private ClienteTask(final CadastroClienteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Cliente... params) {

            SoapObject cli = new SoapObject(NAMESPACE, "cliente");

            Cliente cliente = params[0];

            cli.addProperty("id", cliente.getId());
            cli.addProperty("login", cliente.getLogin());
            cli.addProperty("nome", cliente.getNome());
            cli.addProperty("email", cliente.getEmail());
            cli.addProperty("telefone", cliente.getTelefone());
            cli.addProperty("senha", cliente.getSenha());
            cli.addProperty("situacao", cliente.getSituacao());

            SoapObject inserirCliente = new SoapObject(NAMESPACE, INSERIR);

            inserirCliente.addSoapObject(cli);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(inserirCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + INSERIR, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno =  Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.onCliente(retorno);
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
                cliente.setTelefone(resposta.getProperty("telefone").toString());

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

    class PesquisarClienteTaskEmail extends AsyncTask<String, Void, Cliente> {

        private final ClienteListener listener;

        private PesquisarClienteTaskEmail(final ClienteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Cliente doInBackground(String... params) {

            SoapObject buscarCliente = new SoapObject(NAMESPACE, BUSCAR_POR_EMAIL);
            buscarCliente.addProperty("email", params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {

                http.call("urn:" + BUSCAR_POR_EMAIL, envelope);

                SoapObject resposta = (SoapObject) envelope.getResponse();

                final Cliente cliente = new Cliente();

                cliente.setId(Integer.parseInt(resposta.getProperty("id").toString()));
                cliente.setNome(resposta.getProperty("nome").toString());
                cliente.setEmail(resposta.getProperty("email").toString());
                cliente.setLogin(resposta.getProperty("login").toString());
                cliente.setSenha(resposta.getProperty("senha").toString());
                cliente.setTelefone(resposta.getProperty("telefone").toString());

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

        private final CadastroClienteListener listener;

        private AtualizarClienteTask(final CadastroClienteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Cliente... params) {

            SoapObject cli = new SoapObject(NAMESPACE, "cliente");

            Cliente cliente = params[0];

            cli.addProperty("id", cliente.getId());
            cli.addProperty("nome", cliente.getNome());
            cli.addProperty("email", cliente.getEmail());
            cli.addProperty("telefone", cliente.getTelefone());
            cli.addProperty("login", cliente.getLogin());
            cli.addProperty("senha", cliente.getSenha());

            SoapObject atualizarCliente = new SoapObject(NAMESPACE, ATUALIZAR);

            atualizarCliente.addSoapObject(cli);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(atualizarCliente);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + ATUALIZAR, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno = Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.onCliente(retorno);
        }
    }
}
