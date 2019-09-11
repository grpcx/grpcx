package com.github.grpcx;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class GrpcServer {

    public static Set<String> serviceImplPackageNames;

    public static void  strart() throws InterruptedException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        var serviceClazzSet = new HashSet<Class<? extends io.grpc.BindableService>>();
        for(var pck : serviceImplPackageNames){

            Reflections reflections = new Reflections(pck);
            Set<Class<? extends io.grpc.BindableService>> subTypes = reflections.getSubTypesOf(io.grpc.BindableService.class);
            var implLst = subTypes.stream().filter(p -> p.getPackageName().startsWith(pck)).collect(Collectors.toSet());
            serviceClazzSet.addAll(implLst);
        }
         // create server instance
        var builder = ServerBuilder.forPort(8080);

        for (var implClazz : serviceClazzSet) {

           builder.addService(implClazz.getDeclaredConstructor().newInstance());

        }
        //builder.addService(new UserServiceImpl());
        builder.addService(ProtoReflectionService.newInstance());

        Server server = builder.build();
        // starting the server
        server.start();
        System.out.println("Server start success on port:" + 8080);
        // stopping the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully Stopped the Server");
        }));

        // in gRPC this server needs to be blocking to the main thread
        server.awaitTermination();
    }

}
