package redes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerUDP {


	private static int PORT= 9091;
	public static void main(String[] args) throws IOException {
		DatagramSocket serverSocket = new DatagramSocket ();
		System.err.println("Server listenig on port " + PORT+ " usisng UDP  connection\n");
		long initialTime = System.currentTimeMillis();
		System.out.println("Tiempo Inicial: "+initialTime+"\n");
		
		try{
			while(true){
				//RECIEVE pACKET
				byte bufferReceive[]= new byte[128];
				DatagramPacket receivePacket = new  DatagramPacket(bufferReceive, bufferReceive.length);
			   serverSocket.receive(receivePacket);//obtiene en bytes la direccion IP
			   InetAddress clientAddress= receivePacket.getAddress();//obtiene el IP de donde se envio el paquete
			   int clientPort= receivePacket.getPort(); //obtiene el puerto que utilza el cliente
			   System.out.println("Client port : "+ clientPort + "\n");//muestra en consola el puerto usado por el cliente
			   
			   //Send packet
			   String msg = "Mensaje de Carolina Cevallos ";//es elmensaje que se envia
			   byte bufferSend[]=msg.getBytes();//codifica el mensaje en un arreglo
			   DatagramPacket sendPacket= new DatagramPacket(bufferSend , bufferSend.length);//se pone en el datagrama el mensaje codificado y el tamaño
			   serverSocket.send(sendPacket);//envia el mensaje
			   
			}
		}finally{
			serverSocket.close();
		}
	}

}
