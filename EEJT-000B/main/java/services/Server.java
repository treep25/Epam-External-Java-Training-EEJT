package services;

public abstract class Server extends Thread {
    public abstract void run();

    public abstract void end();
}
