import sys
import socket

class Server:
    def __init__(self, port):
        self.server = socket.socket(socket.AF_INET, socket.DGRAM)
        self.server.bind(('localhost', port))
        self.connected_ip = None
        self.seq_number = 0

    def run_server(self, callback):
        data, addr = s.recv_from(1024);
        if not addr.equals(connected_ip):
            #create new tcp connection 
            print "establishing new connection with ip address", addr
    
