

class Node:
    def __init__ (self, key, value, height):
        self.key = key
        self.value = value
        self.left = None
        self.right = None
        self.height = height 

    def insert(self, key, value):
        target = None
        if key > self.key:
            if self.right == None:
                self.right = Node(key, value, 0)
            else:
                self.right.insert(key, value)
            self.height = max(self.height, self.right.height + 1)
        elif key < self.key:
            if self.left == None:
                self.left = Node(key, value, 0)
            else:
                self.left.insert(key, value)
            self.height = max(self.height, self.left.height + 1)
        else:
            self.value = value

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
            node = node.left
        return node
    
    
    def remove_min(self):
        if self.left == None:
            return self.right
        else:
            self.left = self.left.remove_min()
            return self

    def remove(self, key):
        if key > self.key:
            if self.right != None:
                self.right = self.right.remove(key)
        if key < self.key:
            if self.left != None:
                self.left = self.left.remove(key)
        else:
            if self.left == None:
                return self.right
            if self.right == None:
                return self.left
            n = find_min(self.right)
            self.key = n.key
            self.value = n.value
            self.right = remove_min(n.right)
        return self

class AVL_Tree:
    def __init__(self):
        self.child = None
        self.size = 0
        
    def insert(self, key, value):
        if self.child == None:
            self.child = Node(key, value, 0)
        else:
            self.child.insert(key, value)

    def get(self, key):
        if self.child == None:
            return None
        else:
            return self.child.get(key)

    def remove(self, key):
        if self.child == None:
            return
        self.child = self.child.remove(key)
        
    def find_min(self):
        if self.child == None:
            return None
        else:
            return self.child.find_min()
    
    def height(self):
        if self.child == None:
            return -1
        else:
            return self.child.height
    
    def remove_min(self):
        if self.child == None:
            return
        else:
            self.child = self.child.remove_min()

def unit_test():
    print("testing basic insert and get...")
    t = AVL_Tree()
    t.insert(1, "a")
    t.insert(2, "b")
    t.insert(0.5, "c")
    n = t.child
    assert(n.key == 1)
    assert(n.value == "a")
    assert(n.right.key == 2)
    assert(n.right.value == "b")
    assert(n.left.key == 0.5)
    assert(n.left.value == "c")
    print("...passed")
    
    print("testing basic get..")
    t = AVL_Tree()
    t.insert(1, "a")
    t.insert(2, "b")
    t.insert(1.5, "c")
    t.insert(4, "d")
    t.insert(0.5, "e")
    assert(t.get(1) == "a")
    assert(t.get(2) == "b")
    assert(t.get(1.5) == "c")
    assert(t.get(4) == "d")
    assert(t.get(0.5) == "e")
    assert(t.height() == 2)
    print("...passed")

    print("testing remove min...")
    n = Node(1, "a", 0)
    n.insert(0.5, "b")
    n = n.remove_min()
    assert(n.left == None)
    n.insert(0.5, "b")
    n.insert(0.25, "c")
    assert(n.left.key == 0.5)
    n = Node(100, "a", 0)
    n.insert(50, "b")
    n.insert(75, "c")
    n = n.remove_min()
    assert(n.left.key == 75)
    assert(n.left.value == "c")
    print("...passed")

    print("test remove...")
    t = AVL_Tree()
    t.insert(1,"a")
    t.insert(0.5, "b")
    t.insert(0.75, "c")
    t.insert(0.6, "d")
    t.remove(0.5)
    assert(t.child.left.key == 0.75)
    assert(t.child.left.left.key == 0.6)
    t.insert(0.25, "e")
    assert(t.child.left.left.left.key == 0.25)
    t.remove(0.25)
    assert(t.child.left.left.left == None)
    assert(t.get(0.25) == None)
    n = Node(1, "a", 0)
    n.insert(2, "b")
    n.remove(2)
    assert(n.right == None)
    n.insert(2, "b")
    n.insert(3, "c")
    n.remove(2)
    assert(n.right.key == 3)
    assert(n.right.value == "c")
    print("...passed")

    print("testing heights...")
    t = AVL_Tree()
    assert(t.height() == -1)
    t.insert(1, "a")
    assert(t.height() == 0)
    t.insert(2, "b")
    assert(t.height() == 1)
    t.insert(0.5, "c")
    assert(t.height() == 1)
    t.insert(4, "d")
    assert(t.height() == 2)
    print("...passed")
if __name__ == "__main__":
    unit_test()
    
