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
    def __init__(self, port, on_data, window_size, live_timeout,
                 heartbeat_timeout):
        self.state = Server_state(on_data, window_size, port, live_timeout,
                                  heartbeat_timeout)
        self.state.get_sock().bind(self.state.get_addr())
        print ("BOUND TO PORT", port)
        self.background_loop()

    def background_loop(self):
        self.check_timeouts()
        self.send_heartbeats()
        self.read_socket()
        S.call(self.background_loop)

    def read_socket(self):
        try:
            data, addr = self.state.get_sock().recvfrom(1024)
            status, seq_num = header_format.unpack(data[:8])
            if (not self.state.contains(addr)):
                print ("NEW CLIENT, ADDR:", addr)
                self.state.add_client(addr)
            if (status == E.DATA):
                print ("DATA REQUEST, ADDR:", addr, "SEQ_NUM", seq_num)
                self.state.register_message(addr, seq_num, data[8:])
                to_send_num = self.state.get_window_start(addr)
                highest_to_send = self.state.get_next_to_recieve(addr) - 1
                while (to_send_num <= highest_to_send):
                    header = header_format.pack(E.DATA, to_send_num)
                    to_send_data = self.state.get_response(addr, to_send_num)
                    self.state.get_sock().sendto(header + to_send_data,
                                                   addr)
                    to_send_num += 1
            elif (status == E.HEARTBEAT):
                self.state.register_heartbeat(addr)
        except socket.error:
            pass
        except Exception as e:
            print(e)

    def check_timeouts(self):
        original_length = len(self.state.get_client_addrs())
        self.state.remove_timeouts()
        new_length = len(self.state.get_client_addrs())
        if (new_length < original_length):
            print (original_length - new_length, "CLIENT(S) TIMED OUT")

    def send_heartbeats(self):
        if (self.state.should_send_heartbeats()):
            for addr in self.state.get_client_addrs():
                header = header_format.pack(E.HEARTBEAT, 0)
                self.state.get_sock().sendto(header, addr)
            self.state.sent_heartbeats()

if __name__ == "__main__":
    def log_data(data, addr):
                    print("Logging Data", data, "From Addr", addr)
                    return bytes("Hello Back", "utf-8")

    port = 3000
    window_size = 10
    live_timeout = 1
    heartbeat_timeout = 0.1
    t = TCP_server(3000, log_data, window_size, live_timeout, heartbeat_timeout)
    S.run()
