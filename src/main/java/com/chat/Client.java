package com.chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run(){
        try{
            client = new Socket("127.0.0.1", 9999); //local ip and port to connect to, a new socket object is returned representing a unique connection b/w client and server
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler); // spawn a new single thread (no need to of a connection pool, we only need a single thread for input handler)
            t.start(); //start the execution with separate thread

            String inMessage;
            while((inMessage = in.readLine()) != null){ // main thread will read messages from socket and print it to console
                System.err.println(inMessage);
            }

        } catch (IOException e){
            shutdown();
        }
    }

    public void shutdown(){
        done = true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        } catch (Exception e){
            //ignore
        }
    }

    class InputHandler implements Runnable {

        @Override
        public void run(){
            try{
                //read from command line input and send it to server via socket
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in)); // read from command line
                while(!done){
                    String message = inReader.readLine();
                    if(message.equals("/quit")){
                        out.println(message);
                        inReader.close();
                        shutdown();
                    } else {
                        out.println(message);
                    }
                }

            } catch (Exception e){
                shutdown();
            }
        }
    }

    public static void main(String[] args){
        System.err.println("Initializing client");
        Client client = new Client();
        client.run();
    }
}
