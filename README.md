# publisher

steps: 

- 1 connect to openVPN, ping 172.16.0.1 to check if it works.

- 2 run the publisher

- 3 you can call the command line argument for the client from C

publisher:
java -jar publisher-1.0-SNAPSHOT.jar publisher.yaml

client:
echo {"type":"falling","value":true} | java -jar publisher-1.0-SNAPSHOT.jar client.yaml

