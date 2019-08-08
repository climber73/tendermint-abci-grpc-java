package io.example;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

class GrpcServer {
    private Server server;
    private int port;

    GrpcServer(BindableService service, int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    void start() throws IOException {
        server.start();
        System.out.println("gRPC server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            System.out.println("server shut down");
        }));
    }

    private void stop() {
        server.shutdown();
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    void blockUntilShutdown() throws InterruptedException {
        server.awaitTermination();
    }
}
