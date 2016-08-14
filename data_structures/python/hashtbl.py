from list import List
import hashlib

class kv_node:
    def __init__(self, key, value):
        self.key = key
        self.value = value

class Hashtbl:
    def __init__(self, size):
        self.tbl = [None]*(2**size)
        self.elements = 0
        self.size = size
        
    def hash(self, key):
        return int(hashlib.sha256(key).hexdigest(), base=16) % self.size
        
    def set(self, key, value):
        new_node = kv_node(key, value)
        bin = self.hash(key)  
        self.insert_to_table(bin, new_node)
        self.resize()

    def get(self, key):
        bin = self.hash(key) 
        if self.tbl[bin] == None:
            return None
        kvnode = self.tbl[bin].find(lambda x: x.key == key)
        if kvnode:
            return kvnode.value

    def remove(self, key):
        bin = self.tbl[self.hash(key)]
        old_length = bin.length
        bin.filter(lambda x: x.key == key)
        new_length = bin.length 
        self.elements += (new_length - old_length)
        self.resize()

    def bin_sizes(self):
        bin_sizes = [None]*self.size
        for i in range(self.size):
            if self.tbl[i] == None:
                bin_sizes[i] = 0
            else:
                bin_sizes[i] = self.tbl[i].length
        return bin_sizes

    def insert_to_table(self, bin, node):
        if self.tbl[bin] == None:
            self.tbl[bin] = List()
        else:
            old_length = self.tbl[bin].length
            self.tbl[bin].filter(lambda x: x.key == node.key)
            new_length = self.tbl[bin].length
            self.elements = self.elements + new_length - old_length
        self.elements += 1
        self.tbl[bin].push(node)
        
    def copy_table(self, new_size):
        old_tbl = self.tbl
        old_size = self.size
        self.tbl = [None]*(new_size)
        self.size = new_size
        self.elements = 0
        count = 0
        for i in range(old_size):
            bin = old_tbl[i]
            if bin != None:
                for j in range(bin.length):
                    node = bin.pop()
                    self.insert_to_table(self.hash(node.key), node)
                    count += 1
                        
    def resize(self):
        if self.elements > self.size/2:
            self.copy_table(2*self.size)
        elif self.elements < self.size/4:
            if self.size <= 4:
                return
            self.copy_table(self.size/2)

def unit_test():
    print("testing set...")
    ht = Hashtbl(2)
    ht.set("a", "b")
    bin = ht.hash("a")
    l = ht.tbl[bin]
    assert l.child.data.value == "b"
    assert l.child.data.key == "a"
    ht.set("a", "c")
    assert l.child.data.value == "c"
    print("...passed")

    print("testing get...")
    assert(ht.get("a") == "c")
    ht.set("b","d")
    assert(ht.get("b") == "d")
    assert(ht.get("f") == None)
    print("...passed")
    
    print("testing many inserts...")
    ht = Hashtbl(2)
    for i in range(1000):
        ht.set("a"*i, 2*i)
    for i in range(1000):
        assert(ht.get("a"*i) == 2*i)
    assert(ht.elements == 1000)
    print("...passed")

    print("testing bucket sizes...")
    def balanced(arr, size):
        largest = -1
        for i in range(size):
            val = arr[i]
            if val > largest:
                largest = val
        return largest < 10
    assert(balanced(ht.bin_sizes(), ht.size))
    print("...passed")

    print("testing repeat inserts...")
    ht = Hashtbl(2)
    for i in range(1000):
        ht.set("a"*(i%5), "b"*(i%5))
    assert(balanced(ht.bin_sizes(), ht.size))
    assert(ht.elements == 5)
    for i in range(1000):
        assert(ht.get("a"*(i%5)) ==  "b"*(i%5))
    print("...passed")

    print("testing remove...")
    ht = Hashtbl(2)
    for i in range(1000):
        ht.set("a"*i, 2*i)
    for i in range(1000):
        ht.remove("a"*i)
    for i in range(1000):
        assert(ht.get("a"*i) == None)
    assert(ht.size == 4)
    assert(balanced(ht.bin_sizes(), ht.size))
    print("...passed")

if __name__ == "__main__":
    unit_test()
