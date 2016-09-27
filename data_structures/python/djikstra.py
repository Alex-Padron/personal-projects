import sys
from heapq import *

class Node:
    def __init__(self, weight):
        self.weight = weight
        self.prev = None
        self.dist = sys.maxint

class Pair:
    def __init__(self, x, y):
        self.x = x
        self.y = y

def get_neighbors(pair):
    return filter(lambda p: p.x >= 0 and p.x < side_length and p.y >= 0 and p.y < side_length, 
                  [Pair(pair.x + 1, pair.y), Pair(pair.x - 1, pair.y), Pair(pair.x, pair.y + 1), 
                   Pair(pair.x, pair.y - 1)])

if __name__ == "__main__":
    f = open("matrix.txt")
    l = f.readlines()
    l = map(lambda x: x.split(","), l)
    l = map(lambda x: map(lambda x: Node(int(x)), x), l)
    side_length = len(l[0])

    #set the weight of the start node to be its own weight
    l[0][0].dist = l[0][0].weight
    #originally add all nodes to the set of unvisited nodes
    unvisited_nodes = []
    for i in range(side_length):
        for j in range(side_length):
            heappush(unvisited_nodes, (l[i][j].dist, Pair(i,j)))

    while len(unvisited_nodes) > 0:
        _, u = heappop(unvisited_nodes)
        for neighbor in get_neighbors(u):
            new_dist = l[u.x][u.y].dist + l[neighbor.x][neighbor.y].weight
            if new_dist < l[neighbor.x][neighbor.y].dist:
                l[neighbor.x][neighbor.y].dist = new_dist
                l[neighbor.x][neighbor.y].prev = u
                heappush(unvisited_nodes, (new_dist, neighbor))
            
    n = Pair(side_length - 1, side_length - 1)
    while n != None and l[n.x][n.y] != None:
        print "location", n.x, n.y
        n = l[n.x][n.y].prev
    print "Got path weight", l[side_length - 1][side_length - 1].dist

