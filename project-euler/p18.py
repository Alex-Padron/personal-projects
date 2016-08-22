


class Node:
    def __init__(self, weight):
        self.weight = weight
        self.path_value = None

f = open("p067_triangle.txt")
l = f.readlines()
l = map(lambda x: map(lambda x: Node(int(x)), x.split(" ")), l)
#print(l)

def get_parent_indices(x, y):
    parents = []
    #has a node directly above
    if len(l[y-1]) > x:
        parents.append(x)
    if x > 0:
        parents.append(x-1)
    return parents

if __name__ == "__main__":
    #initialize bottom layer
    for i in range(len(l[-1])):
        l[-1][i].path_value = l[-1][i].weight
        
    #propogate upwards
    index = len(l) - 1
    while index >= 1:
        print "iterating through index", index, "weights for this level are", map(lambda x: x.path_value, l[index])
        for i in range(1, len(l[index])):
            parents = get_parent_indices(i, index)
            for parent in parents:
                l[index-1][parent].path_value = max(l[index-1][parent].path_value, \
                                                    l[index][i].path_value \
                                                    + l[index - 1][parent].weight)
        index -= 1
    print "result is", l[0][0].path_value
