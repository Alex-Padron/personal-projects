from time import time
import socket

"""
Class to store all the state needed for the TCP server
"""
class Server_state:
    def __init__(self, timeout, heartbeat_frequency, callback):
        self.timeout = timeout
        self.heartbeat_frequency = heartbeat_frequency
        self.callback = callback
        self.last_heartbeat_time = 0
        self.stored_result = None
        self.clients = {}
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.setblocking(0)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    def get_callback(self):
        return self.callback

    def set_stored_result(self, result):
        self.stored_result = result

    def get_socket(self):
        return self.sock

    def update_heartbeat_time(self):
        self.last_heartbeat_time = time()

    def should_send_heartbeats(self, time):
        return (time > self.last_heartbeat_time + self.heartbeat_frequency)

    def check_timeouts(self, time):
        for addr in self.clients.keys():
            if (self.clients[addr]["recv_time"] + self.timeout < time):
                clients.pop(addr)

    def get_clients(self):
        return self.clients.keys()

    def get_timeout(self):
        return self.timeout

    def get_seq_num(self):
        return self.seq_num

    def contains(self, client_addr):
        return client_addr in clients

    def add_client(self, client_addr):
        self.clients[client_addr] = {"seq_num": 0, "recv_time": time()}

    def inc(self, client_addr):
        self.clients[client_addr]["seq_num"] += 1

    def heartbeat(self, client_addr):
        self.clients[client_addr]["recv_time"] = time()
