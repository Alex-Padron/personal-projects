import socket
import sys
import time

from tqueue import TQueue

class Scheduler:
    def __init__(self):
        self.n_queue = {} # map from port to the callback waiting on that port
        self.t_queue = TQueue() # waiting for a given time
        self.i_queue = [] # run as soon as possible
        self.alive = False

    def run(self):
        self.alive = True
        while (self.alive):
            # run all functions that can be run instantly
            if (len(self.i_queue) > 0):
                self.i_queue.pop(0)()
            # run all functions whose timeout has passed
            while (self.t_queue.get_closest_time() and
                   self.t_queue.get_closest_time() < time.time()):
                self.t_queue.pop()()

    def call(self, func):
        self.i_queue.append(func)

    def set_timeout(self, func, timeout):
        self.t_queue.push(time.time() + timeout, func)

    def kill(self):
        self.alive = False

if __name__ == "__main__":
    print ("testing scheduler...")
    S = Scheduler()
    start_time = time.time()
    a = []
    def print_hello():
        a.append("a")
        if (start_time + 1 < time.time()):
            S.kill()
        S.set_timeout(print_hello, 0.1)
    S.set_timeout(print_hello, 0.1)
    S.run()
    assert(len(a) == 10)
    a = []
    def appendTo(a):
        a.append("a")
        return len(a)

    def rep_append():
        if (appendTo(a) > 5):
            S.kill()
        S.call(rep_append)
    S.call(rep_append)
    S.run()
    assert(len(a) == 6)
    print ("...passed")
