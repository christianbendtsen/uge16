package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {

    static HashMap<InetAddress, Integer> clients = new HashMap<>();

    public static void main(String[] args) {
        try {

            DatagramSocket datagramSocket = new DatagramSocket(7888);
            System.out.println("Socket oprettet. Lytter til klient.");
            byte[] receiveArray = new byte[1000];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveArray, receiveArray.length);
                datagramSocket.receive(receivePacket); // Modtager data fra Clienten - receive() "blokere".
                clients.put(receivePacket.getAddress(), receivePacket.getPort());
                // Modtag besked fra Client.
                String inMsg = new String(receiveArray, 0, receivePacket.getLength());
                System.out.println("Besked modtaget : " + inMsg);
                // Sende svar tilbage til alle klienter:
                for (Map.Entry<InetAddress, Integer> entry : clients.entrySet()) {
                    //sendTo(datagramSocket, entry.getKey(), entry.getValue(), inMsg);
                    System.out.println("Sendt data til IP: " + entry.getKey() + ", Portnummer: " + entry.getValue());
                }

                if (inMsg.equalsIgnoreCase("quit")) {
                    System.out.println("Client har lukket forbindelsen.");
                    break;
                }

            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }


    }

    static void sendTo(DatagramSocket datagramSocket, InetAddress address, int port, String message) throws IOException{
        byte[] sendArr = message.getBytes();
        DatagramPacket sendPacket= new DatagramPacket(sendArr, sendArr.length,
                address, port);
        datagramSocket.send(sendPacket);
    }

}
