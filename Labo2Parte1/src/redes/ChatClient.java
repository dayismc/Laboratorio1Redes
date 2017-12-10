package redes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ChatClient {

	BufferedReader in; // se crea un objeto string para ser leido por tecaldo con el metodo readLine()
    PrintWriter out;//es creado para mandar el string leido al servidor 
    //se diseña la interfaz grafica de la pantalla
    JFrame frame = new JFrame("Chat");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public ChatClient() {

        // se diseña la ventana para mostrar los mensajes
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {// envia el contenido del campo de texto al servidor
            
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());//Obtiene el texto enviado
                textField.setText("");// limpia el cuadro de texto despues de ser enviado
            }
        });
    }
    
    // Se diseña la ventana donde se ingresa la IP 
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Ingrese la dirección IP del servidor:",
            "Bienvenido al chat",
            JOptionPane.QUESTION_MESSAGE);
    }

    //Se diseña la ventana donde se ingresa el nombre del cliente
    private String getName() {
        return JOptionPane.showInputDialog(//manda el nombre del cliente ingresado por teclado
            frame,
            "Escoja un nombre de ventana:",
            "Selección de nombre de pantalla",
            JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
        String serverAddress = getServerAddress();// obtiene la IP del servidor
        Socket socket = new Socket(serverAddress, 9001);//socket creado para la conexion
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream())); //servira para leer los datos
        out = new PrintWriter(socket.getOutputStream(), true);//servira para enviar los datos

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {//si la cadena empieza con SUBMITNAME
                out.println(getName());// la impresiòn obtiene el dato nombre
                
            } else if (line.startsWith("NAMEACCEPTED")) {//si lacadena empieza con ACEPTADO
                textField.setEditable(true);// se abilita un campo de texto
            } else if (line.startsWith("MESSAGE")) {//si la cadena empieza con MENSAJE
                messageArea.append(line.substring(8) + "\n");// se espera la entrada de un mensaje
            }
        }
    }
    
    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    
}
