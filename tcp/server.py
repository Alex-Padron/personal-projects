import sys
import socket
import struct
from time import time

sys.path.append("../scheduler-python")
from scheduler import S
from common import header_format
from common import E
from server_state import Server_state

"""
TCP server implementation based on UDP. Uses first 8 bytes of message as
header
"""
class TCP_server:
    def __init__(self, port, on_data, timeout, heartbeat_frequency):
        self.state = Server_state(timeout, heartbeat_frequency, on_data)
        self.state.get_socket().bind(("localhost", port))
        print ("BOUND TO PORT", port)
        self.background_loop()

    def background_loop(self):
        # remove all connections that have timed out
        self.state.check_timeouts(time())
        # send heartbeats to all active connections
        if (self.state.should_send_heartbeats(time())):
            for addr in self.state.get_clients():
                self.send_heartbeat(addr)
            self.state.update_heartbeat_time()
        # handle input
        self.read_socket()
        # continually loop
        S.call(self.background_loop)

    def send_heartbeat(self, addr):
        header = header_format.pack(E.HEARTBEAT, E.UNUSED)
        self.state.get_socket().sendto(header, addr)

    def read_socket(self):
        try:
            data, addr = self.state.get_socket().recv_from(1024)
            status, seq_num = header_format.unpack(data)
            if ((not self.state.contains(addr)) and status == E.NEW_CONNECTION):
                print ("NEW CLIENT, ADDR:", addr)
                self.state.add_client(addr)
            if (status == E.HEARTBEAT):
                self.state.heartbeat(addr)
            if (status == E.DATA):
                if (seq_num < self.state.get_seq_num()): # invalid old request
                    self.send_old_request_response(addr)
                if (seq_num == self.state.get_seq_num()): # current request
                    self.send_data_response(addr, self.state.get_stored_result())
                if (seq_num > self.state.get_seq_num()): # new request
                    self.state.set_seq_num(seq_num)
                    data = data[8:]
                    self.state.set_stored_result(
                        self.state.get_callback(addr, data))
        except:
            pass

    def send_old_request_response(self, client_addr):
        header = header_format.pack(E.OLD_SEQ_NUM, self.state.get_seq_num())
        self.state.get_socket().sendto(header, client_addr)

    def send_data_response(self, client_addr, data):
        header = header_format.pack(E.DATA, self.state.get_seq_num())
        self.state.get_socket().sendto(header + data, client_addr)

if __name__ == "__main__":
    def log_data(addr, data):
                    print("recieved socket data", data, "from addr", addr)

    t = TCP_server(3000, log_data, 3, 0.1)
    S.run()
