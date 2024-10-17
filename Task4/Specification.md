# Task 4: specification (full events)

The aim of Task4 is to develop a full-duplex communication framework using **event-driven programming**. Unlike thread-based systems, this design is entirely asynchronous, with all methods operating **non-blockingly**. Responses and operations are driven by **events**, ensuring that no method blocks execution, and responses are handled via **listeners**.

The core components include:

- **Broker**/**QueueBroker** for establishing and managing communication channels.
- **Channel**/**MessageQueue** for exchanging messages between tasks.
- **Listeners** to handle events triggered by message exchange or connection.

## Broker

The **Broker** is responsible for managing connections between tasks. It listens for incoming connection requests and facilitates the exchange of Channels that enable communication between tasks. The Broker operates asynchronously using events to handle connections.

#### Methods

```bind(int port, AcceptListener listener)```:
Binds the Broker to a specific port, enabling it to listen for incoming connections. When a connection is accepted, the provided AcceptListener is notified, and a new event is posted to manage the connection. Returns true if the port is successfully bound, false otherwise.

```unbind(int port)```:
Stops listening on the specified port and prevents any further connection attempts. Returns true if unbound successfully, false otherwise.

```connect(String name, int port, ConnectListener listener)```:
Attempts to connect to another task using broker's name and port. The ConnectListener handles connection success or refusal via events. Returns true if the connection attempt is initiated, false otherwise.

## Channel

The **Channel** class represents a bidirectional communication channel. It is designed to ensure that data is transmitted in a FIFO order and it is lossless.


#### Methods

`boolean write(byte[] bytes, int offset, int length)`: Attempts to write a specified range of bytes from the provided byte array to the channel. It returns true if the write operation is initiated successfully, and false if it fails.

`boolean read(byte[] bytes, int offset, int length)`: Attempts to read a specified range of bytes from the channel. It returns true if the write operation is initiated successfully, and false if it fails.

`void disconnect()`: Initiates the process of disconnecting the channel. This method triggers a background task that marks the channel as disconnected and notifies the associated listener about the disconnection event.

`boolean disconnected()`: Returns true if the channel is currently disconnected, and false otherwise.

## QueueBroker

The **QueueBroker** extends the functionality of the lower layer Broker. In addition to managing communication, it automatically creates a MessageQueue instance upon a successful connection.


## MessageQueue

A **MessageQueue** represents a communication channel between two tasks. It is either open or closed. It handles the sending and receiving of messages, where each message is sent or received as a whole. A task can send a message by providing a range of bytes from a byte array, and a task can receive a message as a byte array. Once closed, no further send or receive operations are possible.

#### Constructor
```MessageQueue(Channel channel)```: Creates a new MessageQueue by wrapping an existing Channel. The Channel provides the low-level communication link for message transmission. The MessageQueue will use the Channel to send and receive messages between tasks.

#### Methods

`void setListener(Listener listener)`: Sets the Listener that will receive notifications when events occur in the MessageQueue.

`boolean send(Message message)`: Sends a message via the MessageQueue. The message is encapsulated within a Message object that contains a byte array and possibly additional offset and length parameters. 

`void close()`: Closes the MessageQueue, preventing any further messages from being sent or received. It also triggers a disconnect event.

`boolean closed()`: Returns true if the MessageQueue is closed and no more messages can be sent or received, false otherwise.

## Interface Listener

This interface allows tasks to be notified when messages are received or when the connection is closed.


#### Methods

`void received(byte[] msg)`: This method is triggered when a message is received on the MessageQueue. The received message is passed as a byte array (msg).

`void sent()`: This method is triggered when a message has been successfully sent through the MessageQueue.

`void closed()`: This method is triggered when the MessageQueue is closed, signaling that no further messages can be sent or received.

`void wrote(int nbytes)`: This method is triggered when a specified number of bytes (nbytes) have been successfully written on the MessageQueue.

`void available(MessageQueue messageQueue)`: This method is triggered when there is data available to read from the MessageQueue.

## Interface AcceptListener

The **AcceptListener** interface is used to handle incoming connection requests. When a task binds to a port using a QueueBroker and a connection is accepted, the registered AcceptListener is notified. This interface ensures that each accepted connection can be processed without blocking the system.

#### Methods

`void accepted(MessageQueue queue)`: This method is called when a connection request has been successfully accepted by the broker. A MessageQueue object is passed to the method, which allows the two connected tasks to exchange messages asynchronously.

## Interface ConnectListener

The **ConnectListener** interface is responsible for handling the result of an outgoing connection request. When a task attempts to establish a connection with a remote broker via QueueBroker, the ConnectListener is notified of either a successful connection or a refusal. This interface ensures the connection process remains non-blocking and is handled asynchronously.

#### Methods

`void connected(MessageQueue queue)`: This method is called when a connection request to a remote broker has been successfully established. The MessageQueue object is passed to the method, allowing the task to start communicating with the remote task using the queue. The connection is handled asynchronously and can be processed in a background thread.

`void refused()`: This method is called when a connection request is refused or fails. Possible reasons for refusal include the target broker being unavailable, the port being closed, or the broker rejecting the connection. The method allows the task to handle the refusal asynchronously and take appropriate actions, such as retrying the connection or reporting the failure.

## Message

The **Message** class encapsulates the data that will be sent through the MessageQueue. It represents a set of bytes and can specify an optional offset and length for sending only part of the byte array.

#### Constructor

```Message(byte[] bytes)```: Initializes a Message using the entire byte array.

```Message(byte[] bytes, int offset, int length)```: Initializes a Message using only part of the byte array, starting at the specified offset and continuing for the specified length.

## Task

A **Task** represents a set of operations (via Runnable objects) that will be executed asynchronously. Tasks use the event system to post work.

#### Methods


```void post(Runnable r)```: Posts a runnable task for execution. The task will be executed asynchronously using the event system.

```static Task task()```: Retrieves the current task instance.

```void kill()```: Terminates the current task. Any unexecuted tasks will be canceled, and new runnables cannot be posted after the task is killed.


```boolean killed()```: Returns true if the task has been terminated, false otherwise.










