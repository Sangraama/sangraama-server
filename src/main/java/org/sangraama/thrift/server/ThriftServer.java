package org.sangraama.thrift.server;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.sangraama.thrift.transmissionservice.PlayerTransmissionService;
import org.sangraama.thrift.transmissionservice.PlayerTransmissionServiceImpl;

public class ThriftServer implements Runnable {
    TServerSocket serverTransport;
    TServer server;
    private int port = 0;

    public ThriftServer(int port) {
        this.port = port;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        try {

            serverTransport = new TServerSocket(this.port);
            @SuppressWarnings("unchecked")
            PlayerTransmissionService.Processor processor = new PlayerTransmissionService.Processor(
                    new PlayerTransmissionServiceImpl());
            server = new TThreadPoolServer(new TThreadPoolServer.Args(
                    serverTransport).processor(processor));
            System.out.println("Starting Thrift server on port: " + this.port
                    + " ...");
            server.serve();

        } catch (TTransportException e) {
            e.printStackTrace();
        }

    }

    public void stopServer() {
        serverTransport.close();
        server.stop();
        System.out.println("Thrift server stopped on port: " + this.port + "...");
    }

}
