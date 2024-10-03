# Task 3: specification (mixing events and threads)

The aim of the Task3 is to develop a communication framework using both event-driven programming and threads. The system will allow tasks to communicate asynchronously via message queues. Later, a fully event-driven version will be developed.

## QueueBroker

The **QueueBroker** is responsible for managing connections between tasks. It acts as a broker for initiating or accepting connections over ports.

### Constructor

The constructor takes a string name as the identifier for the broker.

```
QueueBroker(String name);
```

### Interface AcceptListener

This interface is used to listen for incoming connections and handle them in the background using threads.

```
interface AcceptListener
```
#### Methods
This method is called when a connection is successfully accepted. A separate thread will handle this connection.
The MessageQueue object is passed to allow message exchange.

```
void accepted(MessageQueue queue);
```

### Interface ConnectListener

This interface handles connection attempts and results asynchronously, using threads to manage concurrent connection requests.

```
interface ConnectListener
```

#### Methods

This method is called when a connection is successfully established between two tasks. The connection will be managed in a thread while the MessageQueue enables message communication.
```
void connected(MessageQueue queue);
```

This method is called when a connection request is refused or fails, handled asynchronously by the thread managing the request.
```
void refused();
```

### Other methods

This method binds the QueueBroker to a port and listens for incoming connection requests. The AcceptListener is notified when a connection is accepted. A new thread is created to handle each incoming connection.
Returns true if the port is successfully bound, false otherwise.

```
boolean bind(int port, AcceptListener listener);
```

This method unbinds the QueueBroker from the port, stopping further connection attempts on this port.
Returns true if the port is successfully unbound, false otherwise.

```
boolean unbind(int port);
```

This method attempts to connect to another task via the specified name and port. A separate thread is spawned to manage the connection process. The ConnectListener will be triggered to notify whether the connection was successful or refused.
Returns true if the connection request is initiated successfully, false otherwise.
```
boolean connect(String name, int port, ConnectListener listener);
```

## MessageQueue

A **MessageQueue** represents a communication channel between two tasks. It is either open or closed. It handles the sending and receiving of messages, where each message is sent or received as a whole. A task can send a message by providing a range of bytes from a byte array, and a task can receive a message as a byte array. Once closed, no further send or receive operations are possible.

### Interface Listener

This interface allows tasks to be notified when messages are received or when the connection is closed.

```
interface Listener
```

#### Methods

This method is riggered when a message is received.

```
void received(byte[] msg);
```

This method is triggered when a message is successfully sent.
```
void sent();
```

This method is triggered when the MessageQueue is closed, signaling that no more messages can be exchanged.
```
void closed();
```

### Other methods

This method sets the listener to handle incoming messages, outgoing message confirmations, and closure events.

```
void setListener(Listener l);
```

These methods allows sending message using a byte array. These methods will trigger background threads to process the message while events notify the listener if the send operation is possible or not.
```
boolean send(byte[] bytes);
```
```
boolean send(byte[] bytes, int offset, int length);
```
This method closes the MessageQueue, making it no longer possible to send or receive messages.
```
void close();
```
This method returns true if the queue is closed, false otherwise.
```
boolean closed();
```

## Message

This class encapsulates a message to be sent over a MessageQueue. It includes the byte array representing the message and optional offset and length parameters.

### Constructor

The first constructor creates a message using the entire byte array.

```
Message(byte[] bytes);
```

The second constructor creates a message using a range of the byte array.

```
Message(byte[] bytes, int offset, int length);
```

## Task

A **Task** represents a task that can post operations.

### Methods

This method posts a runnable task for execution.
```
void post(Runnable r);
```

This method returns the current task.
```
static Task task();
```

This method rerminates the task.
```
void kill();
```

This method returns true if the task has been terminated, false otherwise.
```
boolean killed();
```










