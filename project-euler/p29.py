from sets import Set
l = Set()

maxrange = 100
for a in range(2, maxrange + 1):
    for b in range(2, maxrange + 1):
        l.add(a ** b)

l = list(l)
print "result is", len(l)
