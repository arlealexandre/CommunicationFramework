# Task1 : design

## BrokerManager

The **BrokerManager** is implemented as a thread-safe singleton to ensure centralized management of brokers, allowing tasks to access brokers by name without risk of duplication or conflict. It uses a synchronized HashMap to store broker instances and provides methods to safely add or retrieve brokers.

## Channel

The design of **Channel** relies on the use of shared **CircularBuffer** instances for full-duplex communication, where one buffer handles incoming data and the other handles outgoing data. Thread safety is achieved by ensuring that only one task reads from or writes to each buffer at a time. Concurrent read or write operations on the same end of the channel are not allowed. Also, blocking behavior is implemented for both methods, ensuring that if no data is available to read or no room is available to write, the methods will block until progress can be made.

## RendezVous
The **RendezVous** object coordinates the connection establishment between brokers. It holds references to the connecting and accepting brokers along with the associated port number. A RendezVous is created when either a connect or accept request is made, and the connection is established when both requests match. This mechanism ensures that connections are set up only when both parties are ready, ensuring synchronization.
