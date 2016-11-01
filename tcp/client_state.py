from time import time

"""
Rep of the state needed for a tcp client. This state is used
by both the client and server
"""

class Client_state:
    def __init__(self, timeout):
        self.seq_num = 0
        self.last_sent_heartbeat = 0
        self.timeout = timeout
        self.callback = None
        self.last_recived_heartbeat = time()

    def get_seq_num(self):
        return self.seq_num

    def recieve_data(self, seq_num):
        if (seq_num == self.data_seq_num + 1):
            self.data_seq_num += 1
        self.recv_time = time.time()

    def recieve_heartbeat(self):
        self.recv_time = time()

    def is_timed_out(self, current_time):
        return current_time > self.recv_time + self.timeout
