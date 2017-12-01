package redes;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class ClientUDP {

	private static int SERVER_PORT =9091;
	
	public static void main(String[] args) throws IOException {
		
		String serverAddress=JOptionPane.showInputDialog("Enter IP Address of a machine that  is \n" +
				"running the date service on port "+ SERVER_PORT +";");
		
       //SEND PAQUEST (REQUEST)
        DatagramSocket clientSocket= new  DatagramSocket();
         byte bufferSend[]=serverAddress.getBytes();//obtiene en bytes la direccion IP
         DatagramPacket sendPacket = new DatagramPacket(bufferSend, bufferSend.length);
         clientSocket.send(sendPacket);//envia el paquete
         
       //Receive Paquet   
         byte bufferReceive[]= new byte[128];
			DatagramPacket receivePacket = new  DatagramPacket(bufferReceive, bufferReceive.length);
		  clientSocket.receive(receivePacket);//recive el paquete por parte del server
		  
	  //Transforma bytes a string
	  InputStream myInputStream = new  ByteArrayInputStream(receivePacket.getData());
	  BufferedReader input = new BufferedReader( new InputStreamReader(myInputStream));
	  String answer =  input.readLine();
	  
	  //Despliega mensaje
	  JOptionPane.showMessageDialog(null,answer);//muestra el mensaje que recibio
	  clientSocket.close();
	  System.exit(0);
	}  
		   
}