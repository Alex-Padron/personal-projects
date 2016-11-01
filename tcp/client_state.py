from time import time
import socket

class Message_state:
    def __init__(self, data, callback):
        self.data = data
        self.callback = callback
        self.time = time()
        self.recieved = False
        self.recieved_data = None

"""
Rep of the state needed for a tcp client. This state is used
by both the client and server
"""

class Client_state:
    def __init__(self, live_timeout, window_size, server_addr, heartbeat_timeout
                 , data_timeout):
        self.server_addr = server_addr
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.setblocking(0)
        self.live_timeout = live_timeout
        self.data_timeout = data_timeout
        self.heartbeat_timeout = heartbeat_timeout
        self.last_heartbeat_sent = 0
        self.last_heartbeat_recv = time()
        self.next_to_send = 0
        self.next_to_recieve = 0
        self.to_send = [] # list of data to send
        self.window_size = window_size
        self.send_requests = {} # requests that have been sent but not completed

    def is_timed_out(self):
        return time() > self.last_heartbeat_recv + self.live_timeout

    def should_send_heartbeat(self):
        return time() > self.last_heartbeat_sent + self.heartbeat_timeout

    def sent_heartbeat(self):
        self.last_heartbeat_sent = time()

    def recv_heartbeat(self):
        self.last_heartbeat_recv = time()

    def get_sock(self):
        return self.sock

    def get_server_addr(self):
        return self.server_addr

    def add_message(self, data, callback):
        self.to_send.append(Message_state(data, callback))

    def can_send(self):
        return ((self.next_to_send < self.next_to_recieve + self.window_size)
                and (len(self.to_send) > 0))

    def inc_window(self):
        msg_state = self.to_send.pop(0)
        msg_state.time = time()
        self.send_requests[self.next_to_send] = msg_state
        self.next_to_send += 1
        return msg_state.data, (self.next_to_send - 1)

    def waiting(self):
        return self.send_requests.keys()

    def msg_timeout(self, seq_num, tm):
        return self.send_requests[seq_num].time + self.data_timeout < tm

    def update_send_request(self, seq_num):
        self.send_requests[seq_num].time = time()
        return self.send_requests[seq_num].data

    def free_requests(self):
        while (self.next_to_recieve < self.next_to_send):
            msg_state = self.send_requests[self.next_to_recieve]
            if (not msg_state.recieved):
                break
            msg_state.callback(msg_state.recieved_data)
            self.send_requests.pop(self.next_to_recieve)
            self.next_to_recieve += 1

    def recv_seq_num(self, seq_num, data):
        if (not seq_num in self.send_requests):
            return
        self.send_requests[seq_num].recieved = True
        self.send_requests[seq_num].recieved_data = data
        self.free_requests()
