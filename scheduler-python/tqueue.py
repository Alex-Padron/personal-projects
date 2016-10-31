import time

"""
Abstraction for a queue of data with times associated with them.
Popping from queue always returns the element with the lowest timestamp
"""
class TQueue:
    def __init__(self):
        self.queue = None

    def push(self, time, func):
        new_callback = {"function": func, "time": time, "next": None}
        if (not self.queue):
            self.queue = new_callback
            return

        if (self.queue["time"] > time):
            new_callback["next"] = self.queue
            self.queue = new_callback
            return

        target = self.queue
        while (target["next"] and target["next"]["time"] < time):
            target = target["next"]
        child = target["next"]
        target["next"] = new_callback
        new_callback["next"] = child

    def pop(self):
        if (not self.queue):
            return None
        child = self.queue["next"]
        popped = self.queue["function"]
        self.queue = child
        return popped

    def get_closest_time(self):
        if (not self.queue):
            return None
        return self.queue["time"]

if __name__ == "__main__":
    print ("testing TQueue...")
    t = TQueue()
    tm1 = time.time()
    f1 = lambda _: print("1")
    t.push(tm1, f1)
    assert(t.queue["time"] == tm1)
    assert(t.queue["function"] == f1)
    tm2 = tm1 + 1
    f2 = lambda _: print("2")
    t.push(tm2, f2)
    assert(t.queue["next"]["time"] == tm2)
    assert(t.queue["next"]["function"] == f2)
    assert(not t.queue["function"] == f2)
    tm3 = tm1 - 1
    f3 = lambda _: print("3")
    t.push(tm3, f3)
    assert(t.queue["time"] == tm3)
    assert(t.queue["function"] == f3)
    assert(t.queue["next"]["time"] == tm1)
    assert(t.queue["next"]["next"]["time"] == tm2)
    tm4 = tm1 + 0.5
    f4 = lambda _: print("4")
    t.push(tm4, f4)
    assert(t.queue["next"]["next"]["time"] == tm4)
    assert(t.queue["next"]["next"]["function"] == f4)
    assert(t.queue["time"] == tm3)
    assert(t.queue["next"]["time"] == tm1)
    assert(t.queue["next"]["next"]["next"]["time"] == tm2)

    assert(t.get_closest_time() == tm3)

    assert(t.pop() == f3)
    assert(t.pop() == f1)
    assert(t.pop() == f4)
    assert(t.pop() == f2)
    assert(t.pop() == None)
    print ("...passed")
