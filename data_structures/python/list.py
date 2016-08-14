

class Node:
    def __init__ (self, data):
        self.data = data
        self.child = None

class List:
    def __init__ (self):
        self.child = None
        self.length = 0
        
    def append(self, data):
        self.length += 1
        if (self.child == None):
            self.child = Node(data)
            return
        node = self.child
        while (node.child != None):
            node = node.child
        node.child = Node(data)
        
    def push(self, data):
        self.length += 1
        previous_nodes = self.child
        self.child = Node(data)
        self.child.child = previous_nodes
        
    #remove and return the child of the list
    def pop(self):
        if self.child == None:
            return None
        self.length -= 1
        old_child = self.child
        self.child = old_child.child
        return old_child.data

    def filter(self, f):
        node = self
        while node != None and node.child != None:
            if f(node.child.data):
                self.length -= 1
                node.child = node.child.child
            node = node.child
    
    def get(self, index):
        if self.length < index - 1:
            return None
        node = self.child
        while index > 0:
            node = node.child
            index -= 1
        return node.data

def unit_test():
    print("testing append...")
    l = List()
    l.append(1)
    l.append(2)
    assert l.child.data == 1
    assert l.child.child.data == 2
    print("...passed")
    
    print("testing push...")
    l = List()
    l.push(1)
    l.push(2)
    assert l.child.data == 2
    assert l.child.child.data == 1
    print("...passed")
    
    print("testing pop...")
    l = List()
    assert l.pop() == None
    l.push(2)
    assert l.pop() == 2
    l.push(1)
    l.push(2)
    l.push(3)
    assert l.pop() == 3
    assert l.pop() == 2
    assert l.pop() == 1
    print("...passed")
    
    print("testing get...")
    l = List()
    for i in range(10):
        l.push(i)
    for i in range(10):
        assert(l.get(i) == 9 - i)
    print("...passed")
    
    print("testing filter...")
    l = List()
    l.push(1)
    l.push(2)
    l.push(3)
    l.push(4)
    l.push(5)
    l.push(6)
    l.filter(lambda x: x % 2 == 0)
    assert(l.length == 3)
    assert(l.get(0) == 5)
    assert(l.get(1) == 3)
    assert(l.get(2) == 1)
    l = List()
    l.push(1)
    l.push(2)
    l.push(3)
    l.push(4)
    l.push(5)
    l.push(6)
    l.filter(lambda x: x % 2 == 1)
    assert(l.length == 3)
    assert(l.get(0) == 6)
    assert(l.get(1) == 4)
    assert(l.get(2) == 2)
    print("...passed")
if __name__ == "__main__":
    unit_test()
