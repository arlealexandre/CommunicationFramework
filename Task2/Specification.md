# Task2: specification

The aim of this extension to Task1 is to add a new message layer on top of the communication channel layer.
Now, the framework allow you to send and receive whole messages. A message has a variable-sized payload of bytes.

## MessageQueue
Now, communication takes place via message queues. Each message queue represents a point-to-point communication channel, similar to the previous channel-based approach but message-oriented. These are directly linked to the lower channel layer.

A message queue is either open or closed. Once disconnected, no send or receive operations are possible.

A MessageQueue handles the sending and receiving of messages, where each message is sent or received as a whole. A task can send a message by providing a range of bytes from a byte array, and a task can receive a message as a byte array.

### Methods
The **receive** method returns a complete message as a byte array. If no message is available, the receive call blocks until a message becomes available. If the queue is disconnected, an exception is thrown.
```
byte[] receive();
```
The **send** method allows you to send a range of bytes. The send operation blocks if the queue is not ready to receive the message, ensuring no message is lost. If the MessageQueue is disconnected, invoking send will throw an exception.
```
void send(byte[] bytes, int offset, int length);
```
The **close** method is used to close the connection of a message queue.
```
void close();
```
The **closed** method returns true if the message queue is closed, false otherwise.
```
boolean closed();
```
#### Closing a QueueMessage
A MessageQueue can be closed at any time using the close method. Once closed, it's not permit to invoke send or receive methods. If the queue is disconnected, an exception will be throwed.

## QueueBroker
The new **QueueBroker** is a component responsible for creating a message queue and establishing connections between tasks based on lower broker layer.

### Constructor
The following constructor instantiates a **QueueBroker** object from an instance of lower layer Broker.
```
QueueBroker(Broke broker);
```

### Methods
The **accept** method accepts an incoming connection on the specified port, and returns a fully connected MessageQueue once the connection is established. This method is blocking. 
```
Channel accept(int port);
```
The **connect** method establishes an outgoing connection to another QueueBroker named name on the specified port and returns an instance of a MessageQueue. If the remote QueueBroker is unavailable, connect returns null. Otherwise, it blocks until a matching accept is found.
```
Channel connect(String name, int port);

```
#### Connection management
The connection rules are similar to those of the previous channels but applied to message queues. Multiple tasks can accept connections on different ports within the same QueueBroker, but only one task can accept on a given port.

## Task
A **Task** represents an executable unit of work. A task is either associated with a Broker or a QueueBroker, enabling it to communicate with other tasks either through brokers or message queues. Each task executes a Runnable provided at its creation.

### Constructors
The first constructor instantiates a Task with the given Broker and a Runnable object to be executed. The Broker is used for communication.
```
Task(Broker b, Runnable r);
```
The second constructor instantiates a Task with the given QueueBroker and a Runnable object to be executed. The QueueBroker manages communication using message queues.
```
Task(Broker b, Runnable r);
```

### Methods
The **getBroker** method returns the Broker associated with the currently running task. This is the communication broker the task interacts with when using the traditional broker-based communication system.
```
Broker getBroker();
```
The **getQueueBroker** method returns the QueueBroker associated with the currently running task. This is the communication broker for tasks using the new message queue-based communication system.
```
QueueBroker getQueueBroker();
```
The **getTask** method returns the task that is currently executing.
```
Task getTask();
```
