# Task 3: design (mixing events and threads)

The mixed communication framework is designed around a model combining threads for handling task execution, with an event-driven system for managing non-blocking communication. The event-driven system lies the event pump, which is responsible for dispatching events to the appropriate listeners.

## EventPump

The **event pump** is a mechanism that handles the flow of events, ensuring that when specific events occur (such as a message being received or a connection established), the correct listener is notified.

A queue holds all pending events, ensuring they are processed in a first-in, first-out (FIFO) manner. This ensures that events are handled in the order they were triggered.

A thread pool executor service runs in the background, constantly checking the event queue.

When an event is posted, it is added to the event queue.
The event pump continuously checks for new events in the queue and processes them by dispatching them to the appropriate listener.

Once an event is dequeued, the event pump invokes the corresponding listener method.

## QueueBroker
The **QueueBroker** manages the connection lifecycle (binding, accepting, and connecting) using threads and the event pump. While threads are responsible for processing the actual connection, the event pump dispatches events related to the connection status to listeners:

- **AcceptListener**: accepted() method is called when a new connection is established. The event pump ensures that this event is dispatched to the listener as soon as the connection is ready, allowing the task to handle the event asynchronously.

- **ConnectListener**: the event pump dispatches the connected() and refused() events to the respective listeners when an outgoing connection attempt is completed, either successfully or unsuccessfully.

When a connection is accepted or established, the event pump triggers the associated listeners.

## MessageQueue

The **MessageQueue** handles the asynchronous exchange of messages between tasks.

Methods of Listener interface (received, sent and closed) are event-driven. The event pump dispatches the relevant events to the listener when:

- A message is received: the pump processes the incoming data and triggers the received() method.

- A message is successfully sent: the event pump triggers the sent() event to notify the listener.

- The message queue is closed: the pump triggers the closed() event, signaling that no further communication is possible.
