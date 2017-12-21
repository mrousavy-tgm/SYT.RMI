/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import compute.Compute;
import compute.Task;
import loadbalancer.LoadBalancer;

public class ComputeServer implements Compute {
    private Compute stub;
    private Registry registry;
    private String name;
    private int port;
    private LoadBalancer proxy;

    public ComputeServer(String name, int port) {
        super();
        this.name = name;
        this.port = port;
        this.proxy = new LoadBalancer();
    }

    public <T> T executeTask(Task<T> t) {
        return t.execute();
    }

    public void start() throws IllegalArgumentException, RemoteException, SecurityManagerException {
        if (name == null || name.length() < 1) throw new IllegalArgumentException("name");
        if (port < 0) throw new IllegalArgumentException("port");
        if (System.getSecurityManager() == null) throw new SecurityManagerException("Security Manager cannot be null!");

        stub = (Compute) UnicastRemoteObject.exportObject(this, 0);
        registry = LocateRegistry.createRegistry(port);
        try {
            registry.unbind(name);
        } catch (NotBoundException ignored) { }
        registry.rebind(name, stub);
    }

    public void stop() throws java.rmi.NotBoundException, java.rmi.RemoteException {
        registry.unbind(this.name);
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }
}
