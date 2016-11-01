import sys
import socket
import struct
from time import time
import traceback

sys.path.append("../scheduler-python")
from scheduler import S
from common import header_format
from common import E
from client_state import Client_state

"""
TCP client implementation based on UDP.

Implements blocking send and recieve methods

@param ip {String} to connect to
@param port {int} to connect to
@param live_timeout {float} seconds to wait before dying if no heartbeat
@param window_size {int} size for sliding window
@param heartbeat_timeout {float} send heartbeats every this many seconds
@param data_timeout {float} resend data without response every this many seconds
"""
class TCP_client:
    def __init__(self, ip, port, live_timeout, window_size, heartbeat_timeout,
                 data_timeout):
        self.state = Client_state(live_timeout, window_size, (ip, port),
                                  heartbeat_timeout, data_timeout)
        self.background_loop()

    def send(self, data, callback):
        self.state.add_message(data, callback)

    def background_loop(self):
        self.check_timeout()
        self.send_message()
        self.read_socket()
        self.send_heartbeat()
        S.call(self.background_loop)

    def send_message(self):
        # send new requests
        while (self.state.can_send()):
            data, seq_num = self.state.inc_window()
            print("NEW REQUEST SEQ NUM", seq_num)
            header = header_format.pack(E.DATA, seq_num)
            self.state.get_sock().sendto(header + data,
                                         self.state.get_server_addr())

        # send old messages that didn't get a response
        tm = time()
        for seq_num in self.state.waiting():
            if (self.state.msg_timeout(seq_num, tm)):
                print("OLD REQUEST SEQ NUM", seq_num)
                header = header_format.pack(E.DATA, seq_num)
                data = self.state.update_send_request(seq_num)
                self.state.get_sock().sendto(header + data,
                                             self.state.get_server_addr())

    def read_socket(self):
        try:
            data, addr = self.state.get_sock().recvfrom(1024)
            status, seq_num = header_format.unpack(data[:8])
            if (status == E.DATA):
                self.state.recv_seq_num(seq_num, data[8:])
            if (status == E.HEARTBEAT):
                self.state.recv_heartbeat()
        except socket.error:
            pass
        except:
            traceback.print_exc()

    def check_timeout(self):
        if (self.state.is_timed_out()):
            raise socket.error("Socket Timed Out, Connection:",
                               self.state.get_server_addr())

    def send_heartbeat(self):
        if (self.state.should_send_heartbeat()):
            header = header_format.pack(E.HEARTBEAT, 0)
            self.state.get_sock().sendto(header, self.state.get_server_addr())
            self.state.sent_heartbeat()

if __name__ == "__main__":
    ip = "localhost"
    port = 3000
    data_timeout = 0.5
    live_timeout = 1
    window_size = 3
    heartbeat_timeout = 0.1
    t = TCP_client(ip, port, live_timeout, window_size, heartbeat_timeout,
                   data_timeout)

    def log_response(data):
        print("Logging Response", data)

    for i in range(20):
        t.send(bytes("Hello World", "utf-8"), log_response)

    S.run()
