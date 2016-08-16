from hashtbl import Hashtbl

class HashSet:
    def __init__(self, size):
        self.hashtbl = Hashtbl(size)
    
    def insert(self, value):
        self.hashtbl.set(value, True)

    def contains(self, value):
        return (self.hashtbl.get(value) == True)
        
    def remove(self, value):
        self.hashtbl.remove(value)
        
    def size(self):
        return self.hashtbl.elements

def unit_test():
    print("testing hashset...")
    hs = HashSet(4)
    hs.insert("a")
    hs.insert("b")
    assert(hs.size() == 2)
    assert(hs.contains("a"))
    assert(hs.contains("b"))
    assert(not hs.contains("c"))
    hs.remove("a")
    assert(not hs.contains("a"))
    assert(hs.size() == 1)
    hs.remove("b")
    assert(hs.size() == 0)
    assert(not hs.contains("b"))
    print("...passed")

if __name__ == "__main__":
    unit_test()
