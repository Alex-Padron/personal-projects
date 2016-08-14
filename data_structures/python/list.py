

class Node:
    def __init__ (self, data):
        self.data = data
        self.child = None

class List:
    def __init__ (self):
        self.head = None
        self.length = 0
        
    def append(self, data):
        self.length += 1
        if (self.head == None):
            self.head = Node(data)
            return
        node = self.head
        while (node.child != None):
            node = node.child
        node.child = Node(data)
        
    def push(self, data):
        self.length += 1
        previous_nodes = self.head
        self.head = Node(data)
        self.head.child = previous_nodes
        
    #remove and return the head of the list
    def pop(self):
        if self.head == None:
            return None
        self.length -= 1
        old_head = self.head
        self.head = old_head.child
        return old_head.data

    def filter(self, f):
        if self.head == None:
            return
        if self.head.child == None:
            if f(self.head.data):
                self.head = None
                return
        node = self.head
        while node.child != None:
            if f(node.child.data):
                self.length -= 1
                node.child = node.child.child
            node = node.child
                
def unit_test():
    print("testing append...")
    l = List()
    l.append(1)
    l.append(2)
    assert l.head.data == 1
    assert l.head.child.data == 2
    print("...passed")
    
    print("testing push...")
    l = List()
    l.push(1)
    l.push(2)
    assert l.head.data == 2
    assert l.head.child.data == 1
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
    
    print("testing filter...")
    l = List()
    l.push(1)
    l.push(2)
    l.push(3)
    l.push(4)
    l.push(5)
    l.push(6)
    def is_even(x):
        x % 2 == 0
    l.filter(is_even)
    print(l.length)
    assert(l.length == 3)
    print("...passed")
if __name__ == "__main__":
    unit_test()
