import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

client.connect(('localhost', 9000))

client.send("Je suis le CLIENT<br>".encode())

from_server = client.recv(4096)

client.close()

print(from_server)
