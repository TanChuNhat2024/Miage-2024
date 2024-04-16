import socket
print(" je suis dans le serveur")
serv = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serv.bind(('localhost', 9000))
serv.listen(5)
print("je suis en ecoute ....")
while True:
    conn, addr = serv.accept()
    from_client = ''
    data = conn.recv(4096)
    
    if not data:
       print(" le client n'a rien envoye")
    else:
        from_client += str(data)
        print(from_client)

    conn.send("Je suis le SERVEUR principale <br>".encode())
    conn.close()
    print('client deconnecté')
