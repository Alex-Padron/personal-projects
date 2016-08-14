

class Node:
    def __init__ (self, key, value, height):
        self.key = key
        self.value = value
        self.left = None
        self.right = None
        self.height = height 
        
    def insert(self, key, value):
        target = None
        if key >= self.key:
            if self.right == None:
                self.right = Node(key, value, 0)
            else:
                self.right.insert(key, value)
            self.height = max(self.height, self.right.height + 1)
        else:
            if self.left == None:
                self.left = Node(key, value, 0)
            else:
                self.right.insert(key, value)
            self.height = max(self.height, self.left.height + 1)
    
    def get(self, key):
        if self.key == key:
            return self.value
        if key >= self.key:
            if self.right == None:
                return None
            else:
                return self.right.get(key)
        else:
            if self.left == None:
                return None
            else:
                return self.left.get(key)
                
    def find_min(self):
        node = self
        while node.left != None:
            node = node->left
        return node
    
    def remove(self, key):
        
    def remove(self, key):
        if self.right != None:
            if self.right.key == key:
                if self.right.left == None:
                    self.right = self.right.right
                    return
                if self.right.right == None:
                    self.right = self.right.left
                n = findmin(self.right)
                n.
            else:
                remove(self.right, key)
        if self.left != None:
            if self.left.key == key:
                #remove key
            else:
                remove(self.right, key)

def unit_test():
    print("testing basic insert...")
    n = Node(1, "a", 0)
    n.insert(2, "b")
    assert(n.key == 1)
    assert(n.value == "a")
    assert(n.right.key == 2)
    assert(n.right.value == "b")
    n.insert(0.5, "c")
    assert(n.left.key == 0.5)
    assert(n.left.value == "c")
    print("...passed")
    
    print("testing basic get..")
    n = Node(1, "a", 0)
    n.insert(2, "b")
    n.insert(1.5, "c")
    n.insert(4, "d")
    n.insert(0.5, "e")
    assert(n.get(1) == "a")
    assert(n.get(2) == "b")
    assert(n.get(1.5) == "c")
    assert(n.get(4) == "d")
    assert(n.get(0.5) == "e")
    assert(n.height == 2)
    print("...passed")
    
    print("test remove...")
    n = Node(1, "a", 0)
    n.insert(2, "b")
    n.remove(2)
    assert(n.right == None)
    n.insert(2, "b")
    n.insert(3, "c")
    n.remove(2)
    assert(n.right.key == 3)
    assert(n.right.value == "c")
if __name__ == "__main__":
    unit_test()
    
