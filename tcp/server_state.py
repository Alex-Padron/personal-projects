from time import time
import socket

class Message_state:
    def __init__(self, data):
        self.data = data
        self.run_result = None

class Client_state:
    def __init__(self, callback, addr):
        self.last_heartbeat_time = time()
        self.next_to_recieve = 0
        self.window_start = 0
        self.seen_packets = {} # map from seq_num to msg_state
        self.callback = callback
        self.addr = addr

    def get_last_heartbeat_time(self):
        return self.last_heartbeat_time

    def get_next_to_recieve(self):
        return self.next_to_recieve

    def register_heartbeat(self):
        self.last_heartbeat_time = time()

    def update_next_to_recieve(self):
        while (self.next_to_recieve in self.seen_packets):
            msg_state = self.seen_packets[self.next_to_recieve]
            msg_state.run_result = self.callback(msg_state.data, self.addr)
            self.next_to_recieve += 1

    def register_message(self, seq_num, data):
        self.seen_packets[seq_num] = Message_state(data)
        self.update_next_to_recieve()

    def shift_window(new_window_start):
        self.window_start = new_window_start
        self.next_to_recieve = new_window_start
        self.seen_packets = {seq_num: data for seq_num, data in
                             self.seen_packets.items() if
                             seq_num >= self.window_start}

    def get_window_start(self):
        return self.window_start

    def get_response(self, seq_num):
        return self.seen_packets[seq_num].run_result

"""
Class to store all the state needed for the TCP server
"""
class Server_state:
    def __init__(self, callback, window_size, port, live_timeout,
                 heartbeat_timeout):
        self.live_timeout = live_timeout
        self.last_heartbeat_time = 0
        self.heartbeat_timeout = heartbeat_timeout
        self.window_size = window_size
        self.callback = callback
        self.clients = {} # map from addr to client
        self.addr = ("localhost", port)
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.setblocking(0)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    def remove_timeouts(self):
        tm = time()
        self.clients = {addr: client_state for addr, client_state in
                        self.clients.items() if
                        client_state.get_last_heartbeat_time() + self.live_timeout
                        > tm}

    def should_send_heartbeats(self):
        return time() > self.last_heartbeat_time + self.heartbeat_timeout

    def sent_heartbeats(self):
        self.last_heartbeat_time = time()

    def register_heartbeat(self, addr):
        self.clients[addr].register_heartbeat()

    def get_addr(self):
        return self.addr

    def get_sock(self):
        return self.sock

    def add_client(self, addr):
        self.clients[addr] = Client_state(self.callback, self.addr)

    def register_message(self, addr, seq_num, data):
        next_to_recieve = self.clients[addr].get_next_to_recieve()
        if (next_to_recieve > seq_num):
            return
        if (next_to_recieve + self.window_size < seq_num):
            self.clients[addr].shift_window(seq_num - self.window_size)
        self.clients[addr].register_message(seq_num, data)

    def get_window_start(self, addr):
        return self.clients[addr].get_window_start()

    def get_next_to_recieve(self, addr):
        return self.clients[addr].get_next_to_recieve()

    def get_response(self, addr, seq_num):
        return self.clients[addr].get_response(seq_num)

    def contains(self, client_addr):
        return client_addr in self.clients

    def get_client_addrs(self):
        return self.clients.keys()
