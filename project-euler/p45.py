from sets import Set 

def generate_pentagonal(size):
    s = Set()
    for i in range(1, size):
        index = i * (3*i - 1) / 2 
        s.add(index)
    return s

def generate_triangular(size):
    ind = 1
    s = Set()
    for i in range(1, size):
        index = i * (i + 1) / 2 
        s.add(index)
    return s

def generate_hexagonal(size):
    s = Set()
    for i in range(1, size):
        index = i * (2*i - 1) 
        s.add(index)
    return s

size = 10*1000*1000
sp = generate_pentagonal(size)
print "generated pentagonal"
st = generate_triangular(size)
print "generated triangular"
sh = generate_hexagonal(size)
print "generated hexagonal"
assert 40755 in sp and 40755 in st and 40755 in sh

s = 0
for x in sp:
    if x in st and x in sh:
        print "found solution", x
        if x > 40755 and s == 0:
            print "assigning s =", x
            s = x
print "solution is", s
            
