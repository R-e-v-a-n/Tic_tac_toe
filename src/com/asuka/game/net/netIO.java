package com.asuka.game.net;

import java.io.*;
import java.net.*;

/**
 * Created by Asuka on 12.08.2016.
 */
public class netIO extends Thread{
    Socket socket;

    DataInputStream in;
    DataOutputStream out;

    NetReader reader;

    private boolean isQuit = false;

    private void closeIfQuit(){
        if (isQuit) try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setQuit(boolean quit) {
        isQuit = quit;
        closeIfQuit();
    }

    public netIO(NetReader cl, Socket sock) {
        reader = cl;
        try {
            socket = sock;
            InputStream s_in = socket.getInputStream();
            OutputStream s_out = socket.getOutputStream();

            in = new DataInputStream(s_in);
            out = new DataOutputStream(s_out);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void send(String command, String text){
        try {
            out.writeUTF(command + text);
            out.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        String line;
        try{
            while(!isQuit){
                line = in.readUTF();
                if (line.length() > 3) {
                    reader.read(line.substring(0, 4), line.substring(4));
                }
            }
            socket.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
        reader.close();
    }
}
