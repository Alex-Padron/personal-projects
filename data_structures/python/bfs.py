import sys
from sets import Set 

class Node:
    def __init__(self, is_target):
        self.neighbors = Set() #map from node to set of neighbors
        self.parent = None
        self.is_target = is_target
        self.distance = sys.maxint
    
    def add_neighbor(self, neighbor):
        self.neighbors.add(neighbor)

def bfs(start_node):
    start_node.distance = 0
    visited_nodes = Set()
    border_nodes = Set()
    border_nodes.add(start_node)
    while len(border_nodes) > 0:
        new_border = Set()
        for node in border_nodes:
            if node.is_target:
                return node
            visited_nodes.add(node)
            for neighbor in node.neighbors:
                if not neighbor in visited_nodes:
                    neighbor.parent = node
                    neighbor.distance = node.distance + 1
                    new_border.add(neighbor)
        border_nodes = new_border

def print_path(end_node):
    while end_node != None:
        print "end node at mem location", end_node, "with distance from start", end_node.distance
        end_node = end_node.parent

def unit_test():
    n1 = Node(False)
    n2 = Node(False)
    n3 = Node(True)
    n1.add_neighbor(n2)
    n2.add_neighbor(n3)
    n2.add_neighbor(n1)
    target = bfs(n1)
    print_path(target)

if __name__ == "__main__":
    unit_test()


