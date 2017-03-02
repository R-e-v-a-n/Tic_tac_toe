package com.asuka.game.net;

/**
 * Created by Asuka on 12.08.2016.
 */
public abstract class NetReader{
    public abstract void read(String command, String text);
    public abstract void send(String command, String text);
    public abstract void close();
}
