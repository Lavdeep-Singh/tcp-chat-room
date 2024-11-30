# TCP Chat Room

Welcome to the **TCP Chat Room**! This project is a simple, multithreaded chat room implemented in Java. It demonstrates server-client communication using **TCP sockets**, allowing multiple clients to connect to a central server and exchange messages in real-time. 

---

## 🚀 Features

- **Multi-client Support**: The server handles multiple clients concurrently using threads, enabling real-time group chats.
- **Nickname Management**: Each client sets a unique nickname when joining, which is broadcast to others.
- **Command Handling**:
  - `/nick <new_name>`: Change your nickname.
  - `/quit`: Leave the chat gracefully.
- **Broadcast Messaging**: Messages from one client are broadcast to all connected clients.
- **Clean Shutdown**: Both the client and server handle shutdown operations gracefully, ensuring resources are released properly.
- **Dynamic Threading**: Uses a thread pool for managing connections, ensuring scalability for multiple clients.

---

## 🛠️ Project Structure

```plaintext
tcp-chat-room/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── chat/
│                   ├── Main.java         # Entry point (dummy file, no logic)
│                   ├── Server.java       # Server-side code
│                   ├── Client.java       # Client-side code
│
├── README.md          # This file!
└── out/               # Compiled Java files (after `javac`)
```

---

## 💻 How It Works

### Server
The **Server**:
1. Listens for incoming client connections on **port 9999**.
2. Creates a new thread for each client using a thread pool.
3. Maintains a list of active connections to broadcast messages.
4. Handles special commands like nickname changes and disconnections.

### Client
The **Client**:
1. Connects to the server on **127.0.0.1:9999**.
2. Reads user input from the console and sends messages to the server.
3. Displays messages broadcast by the server.
4. Handles commands (`/nick`, `/quit`) to enhance user experience.

---

## 🛠️ Setup Instructions

### Prerequisites
- Java Development Kit (JDK) installed (Java 8+ recommended).
- Basic knowledge of using a terminal/command prompt.

### Step-by-Step Guide

1. **Clone or Download the Project**:
   ```bash
   git clone https://github.com/<your-repo-name>/tcp-chat-room.git
   cd tcp-chat-room
   ```

2. **Compile the Code**:
   Navigate to the `src` directory and compile the code:
   ```bash
   javac -d out src/main/java/com/chat/*.java
   ```

3. **Run the Server**:
   In one terminal, start the server:
   ```bash
   java -cp out com.chat.Server
   ```

4. **Run the Client**:
   In another terminal, start the client:
   ```bash
   java -cp out com.chat.Client
   ```

5. **Test the Chat**:
   - Open multiple terminals and run the client program in each.
   - Send messages from one client and watch them appear in others.

---

## 🔧 Commands

### Available Commands in the Chat Room:
1. **Send Messages**: Type any message and press Enter to send it to all participants.
2. **Change Nickname**:
   - Command: `/nick <new_name>`
   - Example: `/nick John`
3. **Quit the Chat**:
   - Command: `/quit`

---

## ⚙️ How It Works Internally

### Server
- Listens for incoming connections using a `ServerSocket` on **port 9999**.
- Each client connection is represented by a `Socket` object.
- A dedicated `ConnectionHandler` class manages the interaction with each client, running on its own thread.

### Client
- Connects to the server using a `Socket`.
- Uses two threads:
  - **Main Thread**: Reads messages from the server and displays them.
  - **Input Handler Thread**: Sends user input to the server.

---

## 👀 Example Interaction

### Terminal 1: Start the Server
```bash
java -cp out com.chat.Server
```
Output:
```
Initializing server
```

### Terminal 2: Start Client 1
```bash
java -cp out com.chat.Client
```
Output:
```
Initializing client
Please enter a nick name:
John
John connected!
```

### Terminal 3: Start Client 2
```bash
java -cp out com.chat.Client
```
Output:
```
Initializing client
Please enter a nick name:
Jane
Jane connected!
```

### Chat Session
- **Client 1** (John):
  ```plaintext
  Hello everyone!
  ```
- **Client 2** (Jane):
  ```plaintext
  John: Hello everyone!
  Hi John!
  ```

---

## 🎯 Future Improvements

- Add support for private messages.
- Implement a graphical user interface (GUI) for better user experience.
- Introduce authentication for secure chat sessions.
- Add server-side message logging for audit purposes.

---

## 📝 License

This project is open-source and available under the MIT License. Feel free to fork, modify, and share!

---

## 💬 Contact

Have questions or want to contribute? Feel free to reach out at:
- **Email**: lavdeep2002feb@gmail.com
- **GitHub**: [Lavdeep's GitHub](https://github.com/Lavdeep-Singh)

Happy chatting! 🎉