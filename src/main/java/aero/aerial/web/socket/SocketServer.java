package aero.aerial.web.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by andrewsimmons on 6/30/15.
 */
public class SocketServer {

    public void startListening(int portNumber) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = null;

            System.out.println("Socket Server listening on port " + portNumber + " ...");

            clientSocket = serverSocket.accept();

            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            // Initiate conversation with client
            RadioSocketProtocol kkp = new RadioSocketProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Socket Server got '" + inputLine + "' ...");
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("q"))
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
