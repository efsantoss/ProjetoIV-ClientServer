import java.io.*;
import java.net.*;
import com.google.gson.Gson;

public class Server extends Thread {

    private static final int PORT = 12345;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from " + clientSocket.getInetAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clientHandler.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean processPedido(Pedido pedido) {
        return pedido.getQuantidade() > 0 && enderecoExiste(pedido.getEndereco());
    }

    private static boolean enderecoExiste(String endereco) {
        return !endereco.isEmpty();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                String jsonData;

                while ((jsonData = reader.readLine()) != null) {
                    System.out.println("Received from client: " + jsonData);

                    Gson gson = new Gson();
                    Pedido pedido = gson.fromJson(jsonData, Pedido.class);

                    boolean isValid = processPedido(pedido);
                    writer.write(String.valueOf(isValid));
                    writer.newLine();
                    writer.flush();
                }

                // Client disconnected
                System.out.println("Client disconnected from " + clientSocket.getInetAddress());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
