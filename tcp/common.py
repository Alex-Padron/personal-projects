import struct

"""
Data packing for the header for tcp packets
The first int is the state, see the enums below.
The second int is the sequence number. This is used for data messages.
"""
header_format = struct.Struct("I I")

"""
Enums for the messages between client and server
"""
class Enums:
    def __init__(self):
        self.NEW_CONNECTION = 0
        self.HEARTBEAT = 1
        self.DATA = 2
        self.UNUSED = -1
E = Enums()
