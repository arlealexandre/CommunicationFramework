
# Task2 : design

## BrokerManager

The **BrokerManager** remains identical to Task1. It is a centralized, thread-safe singleton that manages the lifecycle of Broker instances. The BrokerManager uses a HashMap to store brokers by name, ensuring unique brokers across tasks and preventing duplication or conflicts. Tasks can retrieve brokers by their name using this manager, and it guarantees that no two tasks will access the same broker instance concurrently without proper synchronization.

## QueueBroker

The **QueueBroker** in Task2 uses an existing Broker to handle the connection process between tasks. The QueueBroker doesn't establish connections directly but wraps the Channel provided by the Broker. It then creates MessageQueues to facilitate communication. This way, the QueueBroker reuses the core logic from the Broker to handle accepting and connecting, focusing only on message handling.

## MessageQueue

The core changes in Task2 are centered around the sending and receiving methods in MessageQueue.

To enable full-duplex communication, two MessageQueues are created based on the underlying Channel, one for sending and one for receiving. This allows tasks to send and receive messages simultaneously without conflicts.

- **Sending**:
        Before sending the actual message, the MessageQueue first sends the size of the message. This helps the receiver know how many bytes to expect.
        The system ensures that the entire message is sent through the channel, and the process may block (wait) if the channel is full, ensuring that no data is lost.

- **Receiving**:
        On the receiving end, the size of the incoming message is read first, letting the receiver know how many bytes to collect.
        The system then reads the full message, and the process may block if no message is available.

This approach guarantees that messages are sent and received as complete units, without any loss or partial transmissions.

## Closing QueueMessage
The closing for Task2 is tied to the Channel used by the MessageQueue. If the Channel is closed or disconnected, the MessageQueue will reflect this state and no further messages can be sent or received. This prevents tasks from trying to communicate after the connection has been severed.
