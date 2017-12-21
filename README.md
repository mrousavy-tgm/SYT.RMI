# Systemtechnik RMI
**R**emote **M**ethod **I**nvokation: Java

## 1. Interface
Generic Interface `Task<T>`: Ausführen von Tasks auf dem Server via Lambdas/Delegates

## 2. Stub
Es muss ein Stub am Server erstellt werden;
```java
(Compute) UnicastRemoteObject.exportObject(this, 0);
```
wobei `0` der erste freie Port ist.

Der sinn eines **Stub**s ist, ein Objekt zu erstellen welches "ge-**Marshalled**" wird (also in eine Reihe an binary Data verpackt) um über das Netzwerk I/O zu senden.

## 3. Registry
#### a. Server
Registry erstellen auf Server mit
```java
LocateRegistry.createRegistry(port);
```
wobei der `port` der Port ist auf dem die Registry erstellt wird (wichtig!)
    
#### b. Client
Der Client findet die Registry mit
```java
LocateRegistry.locateRegistry(port);
```
    
## 4. Engine
Nun kann die `ComputeEngine` erstellt werden
```java
ComputeEngine engine = new ComputeEngine("Compute", port);
```
Nun kann die Engine gestartet werden.

Auf dieser engine kann jeder `Task<T>` ausgeführt werden.
#### Beispiel: 
```java
Pi task = new Pi(digits);   
return compute.executeTask(task);
``` 



## OSI Modell
* Application
* Presentation
* Session
* Transport
* Network
* Data Link
* Physical
