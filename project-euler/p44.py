from sets import Set

def generate_pentagonal(size):
    s = Set()
    l = [False] * size
    for i in range(1, len(l)):
        index = i * (3*i - 1) / 2 
        if index < len(l):
            l[index] = True
            s.add(index)
    return l, s


l, s = generate_pentagonal(10*1000*1000)
print "generated l"
assert l[22] and l[70] and l[92]

mind = 1000000000000
for i in s:
    for j in s:
        if i+j in s and i-j in s:
            print "found solution, i =", i, "j =", j
            if (i-j) < mind:
                mind = i - j
print "result is", mind
  
assert(False)            
for i in range(1, 100000):
    for j in range(1, 100000):
        if l[i] and l[j] and l[i+j] and l[abs(i-j)] and i != j:
            x = abs(i - j)
            if x < mind:
                print "found optimal solution, i =", i, "j =", j
                mind = abs(i - j)



