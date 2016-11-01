import sys
import socket
import struct
from time import time

sys.path.append("../scheduler-python")
from scheduler import S
from common import header_format
from common import E
from client_state import Client_state

"""
TCP client implementation based on UDP.

Implements blocking send and recieve methods
"""
class TCP_client:
    def __init__(self, ip, port, timeout=3):
        self.state = Client_state(timeout)
        self.sock = socket.socket(socket.AF_INET,
                                  socket.SOCK_DGRAM)
        self.sock.setblocking(0)
        self.server_addr = (ip, port)
        self.to_send = None
        self.callback = None
        print("client connecting to ip", ip, "and port", port)
        self.sock.sendto(header_format.pack(E.NEW_CONNECTION, 0),
                         self.server_addr)
        self.background_loop()

    # [callback] will be called once the message is sent
    def send(self, data, callback):
        if (self.to_send):
            raise Exception("trying to send on socket before previous req finished")
        self.seq_num += 1
        self.to_send = data
        self.callback = callback

    def background_loop(self):
        self.check_timeout()

        self.send_heartbeat()

        self.read_socket()

        S.call(self.background_loop)

    def check_timeout(self):
        if (self.state.is_timed_out(time())):
            raise Exception("Socket timed out connection to",
                            self.state.get_server_addr())

    def send_heartbeat(self):
        
    def read_socket(self):
        try:
            data, addr = self.state.get_socket().recv_from(1024)
            status, seq_num = header_format.unpack(data)
            if (status == E.HEARTBEAT):
                self.state.heartbeat()
            if (status == E.OLD_SEQ_NUM):
                self.state.update_seq_num(seq_num + 1)
            if (status == 
        except:
            pass
        print("running background loop")
        if (self.state.is_timed_out(time())):
            raise Exception("Socket timed out, connected to addr",
                            self.server_addr)
        self.send_heartbeat()
        self.send_data()
        self.read_from_server()
        def repeat():
            self.background_loop()
        S.set_timeout(repeat, 0.1)

    def read_from_server(self):
        try:
            data, addr = self.sock.recvfrom(1024)
            print("recieved data from server")
            status, seq_num = header_format.unpack(data)
            if (status == E.HEARTBEAT):
                self.state.recieve_heartbeat()
            if (status == E.DATA):
                data = data[8:]
                self.data_response(seq_num, data)
            print("message has status", status, "and seq num", seq_num)
        except:
            pass

    def data_response(self, seq_num, data):
        if (self.to_send and self.seq_num == seq_num):
            print ("Got response for request with seq num", seq_num)
            self.to_send = None
            callback = self.callback
            self.callback = None
            callback(data)

    def send_heartbeat(self):
        self.sock.sendto(header_format.pack(E.HEARTBEAT, 0), self.server_addr)

    def send_data(self):
        if (self.to_send):
            print("sending data")
            header = header_format.pack(E.DATA, self.state.get_seq_num())
            self.sock.sendto(header + self.to_send,
                             self.server_addr)

if __name__ == "__main__":
    t = TCP_client("localhost", 3000)
    t.send(bytes("Hello World", "utf-8"),
           lambda x: print("got response", x, "from server"))
    S.run()
