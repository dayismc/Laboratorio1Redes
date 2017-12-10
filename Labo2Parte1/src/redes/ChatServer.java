package redes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;


public class ChatServer{

	/**
     * The port that the server listens on.
     */
    private static final int PORT = 9001;

    //para ingresar los nombres de los clinetes en el chat , y verifica que no se repitan los nombres 
    private static HashSet<String> names = new HashSet<String>();
    
    //es el conjunto de todos los mensajes que seran enviado por el cliente
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    /**
     * The appplication main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("El Servidor de Chat esta corriendo.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();//empezando la comunicacion
            }
        } finally {
            listener.close();//para cerrar la comunicacion
        }
    }
    
    //se crea handlers a partir de un lazo
    //trabaja con un cliente y se encarga de transmitir sus mensajes
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {//el constructr recibe un socket
            this.socket = socket;
        }
        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(//se lee los mensajes
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                
                while (true) {
                    out.println("SUBMITNAME");//se solicita el nombre del cliente
                    name = in.readLine();//lee el nombre
                    if (name == null) {//verifica si se dejo vacio el campo de nombre
                        return;
                    }
                    synchronized (names) {//comprueba que el nombre no estè siendo utlizado por otro cliente
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

               
                out.println("NAMEACCEPTED");//indica que el nombre puede ser utilizado 
                writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                
                while (true) {
                    String input = in.readLine();// lee los datos del cliente
                    if (input == null) {
                        return;//sale del bucle si el cliente envia una cadena vacía
                    }
                    for (PrintWriter writer : writers) {// obtiene el mensaje ingresado
                        writer.println("MESSAGE " + name + ": " + input);
                        //envia a los demas clientes el mensaje con el nombre de quien enviò 
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
