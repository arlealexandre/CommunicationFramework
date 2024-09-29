# Task1 : specification

The aim of this project is to develop a communication framework enabling two local processes to communicate with each other.

## Channel
Communication takes place via a communication channel. This channel enables two tasks to write and read messages in byte form.
The channel guarantees retransmission of messages in FIFO order and are lossless. It also enables the exchange of full-duplex messages.

### Methods
The **read** method reads a maximum number of bytes (length) from an array of bytes, starting from the offset index. This method returns the number of bytes read from the array.
```
int read(byte[] bytes, int offset, int length);
```
The **write** method allows you to write a maximum number of bytes (length) to a byte array, starting from the offset index. This method returns the number of bytes written to the array.
```
int write(byte[] bytes, int offset, int length);
```
The **disconnect** method is used to close the connection of a communication channel.
```
void disconnect();
```
The **disconnected** method returns true if the channel is disconnected, false otherwise.
```
boolean disconnected();
```

## Broker
The **Broker** is a component responsible for creating a communication channel and establishing connections. It is identified by a unique name and port number. This component accepts several connections simultaneously, so it is synchronized.

### Methods
The following constructor instantiates a **Broker** object from a string representing its name.
```
Broker(String name);
```
The **accept** method accepts an incoming connection on the specified port, and returns a Channel object. This method is blocking.
```
Channel accept(int port);
```
The **connect** method establishes an outgoing connection to the Broker named name on the specified port and returns a Channel object.
```
Channel connect(String name, int port);
```
## Task
A **Task** represents an executable task. A task is linked to a Broker, and can communicate with several Brokers.

### Methods
The following constructor instantiates a **Task** object from a Broker b and a Runnable r object to be executed.
```
Task(Broker b, Runnable r);
```
The **getBroker** method returns the Broker associated with the task currently running.
```
static Broker getBroker();

