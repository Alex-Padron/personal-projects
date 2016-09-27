import socket
import sys
import json 
import threading

class Client:
    def __init__(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    def connect(self, ip, port):
        self.ip = ip
        self.port = port
        msg = json.dumps({status: "connect"})
        self.socket.sendto(msg, (host, port))

    def send_ping_repeated(self):
        self.send_ping()
        sleep(1)
        self.send_ping_repeated()

    def start_pinging(self):
        self.ping_thread = threading.Thread(target=self.send_ping_repeated)
        self.ping_thread.start()

    def close(self):
        
