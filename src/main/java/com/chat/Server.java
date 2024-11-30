package com.chat;
// Runnable means this class can be passed to a thread pool
// and can be executed concurrently, along side other classes
// that have implemented runnable interface

//you need to implement run function when you implement a
// runnable interface

// whenever a class with runnable interface is passed to a thread pool,
// run function is invoked

/*
Example Workflow:
    1) The server starts listening on port 9999.
    2) Client A connects to the server:
        The server accepts the connection and creates a new Socket for Client A.
        A ConnectionHandler is created for Client A, running in its own thread.
    3) Client B connects to the server:
        The server accepts the connection and creates another Socket for Client B.
        A separate ConnectionHandler is created for Client B, running in another thread.
    4) When Client A sends a message:
        The server broadcasts it to all connected clients (including Client B).

Port and ServerSocket:
    A port is a number that identifies a specific process or service on a machine. In this case, the server is listening on port 9999 for incoming connections. This port is fixed and shared by all clients connecting to the server.
    The ServerSocket is used to listen for incoming client connections. When a client connects, the accept() method of ServerSocket returns a new Socket object for communication with that specific client.

Socket:
    A Socket represents the connection between the server and a specific client. Each client gets its own Socket object when it connects to the server.
    Even though all clients connect to the same port (9999), the server creates a separate Socket for each client, which is used to exchange data independently.

Thread Per Connection:
    The server uses a thread pool (ExecutorService) to handle each client connection in a separate thread. This ensures multiple clients can communicate with the server concurrently without blocking each other.

Runnable and Threads:
    The Server class implements Runnable, which means it can be executed as a separate thread.
    The ConnectionHandler class also implements Runnable to handle individual client communication in separate threads.

Broadcasting Messages:
    The server keeps track of all active client connections in the connections list.
    The broadcast() method sends a message to all connected clients by iterating through this list.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors; // The ExecutorService ensures the server can handle multiple clients concurrently by running each ConnectionHandler in a separate thread.

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections; //list of all the active connections
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool; //using thread pool for execution

    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run(){
        try {
            // port on which server is listening to
            // no need to mention I/P unless specifically needed
            server = new ServerSocket(9999); // All clients connect to the server on port 9999, but each gets its own Socket for independent communication.
            pool = Executors.newCachedThreadPool();
            while(!done){ //flag to stop accepting connections when needed
                Socket client = server.accept(); // accept incoming connection to the server port
                ConnectionHandler handler = new ConnectionHandler(client);//class to handle each connection
                connections.add(handler);//store connections for broadcasting
                pool.execute(handler);//thread pool will execute the handler
            }

        } catch (Exception e) {
            shutdown();
        }
    }

    public void broadcast(String message){
        // send this same message to all the active clients
        for(ConnectionHandler ch : connections){
            if(ch != null){
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown() {
        try{
            done = true;
            pool.shutdown();
            if(!server.isClosed()){
                server.close();
            }
            for (ConnectionHandler ch : connections){
                ch.shutdown();
            }
        } catch (IOException e){
            // ignore
        }
    }

    class ConnectionHandler implements Runnable {

        private Socket client; //representing socket which client is connected to
        private BufferedReader in; //used to read from socket
        private PrintWriter out; //used to write in socket
        private String nickname;

        public ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run(){
            try{
                out = new PrintWriter(client.getOutputStream(), true); // write to socket, by getting outputStream of socket and using PrintWriter to convert chars to bytes
                in = new BufferedReader(new InputStreamReader(client.getInputStream())); //read from the socket

                out.println("Please enter a nick name: "); //writing to socket
                nickname = in.readLine(); //accept whatever client has sent -- skipping edge cases handling like empty string or invalid nickname
                System.out.println(nickname + " connected!");
                broadcast(nickname + " joined the chat!");

                String message;
                while((message = in.readLine()) != null){ // new message received from client
                    if(message.startsWith("/nick")){
                        String [] messageSplit = message.split(" ", 2);
                        if(messageSplit.length == 2){
                            broadcast(nickname + "renamed themselved to " + messageSplit[1]);
                            System.out.println(nickname + "renamed themselves to " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("Successfully changed nickname to " + nickname);
                        }else {
                            out.println("No nickname provided");
                        }
                    }else if (message.startsWith("/quit")){
                        broadcast(nickname + " left the chat!");
                        shutdown();
                    }else {
                        broadcast(nickname + " : " + message);
                    }
                }
            } catch (IOException e){
                shutdown();
            }
        }

        public void sendMessage(String message){
            out.println(message);
        }

        public void shutdown(){
            try{
                in.close();
                out.close();
                if(!client.isClosed()){
                    client.close();
                }
            } catch (IOException e){
                //ignore
            }
        }
    }

    public static void main(String[] args) {
        System.err.println("Initializing server");
        Server server = new Server();
        server.run();
    }

}
