from sets import Set

coin_values = [1,2,5,10,20,50,100,200]

def make_sum(x):
    if x <= 1:
        return [[1]]
    s = []
    for value in coin_values:
        if value == x:
            s.append([value])
        if value < x:
            tmp = make_sum(x - value)
            for v in tmp:
                s.append([value] + v)
    return s

def get_sums(x):
    unique = Set()
    s = make_sum(x)
    print "before de-duping there are", len(s), "elements"
    for v in s:
        v.sort()
        h = 0
        for val in v:
            h = hash((h, val))
        unique.add(h)
    return len(unique)
    
print get_sums(1)
print()
print()
print get_sums(2)
print()
print()
print get_sums(5)
print()
print()
#print get_sums(200)


rng = 200
l = [None] * (rng + 1)
for i in range(rng + 1):
    l[i] = Set()

for coin_value in coin_values:
    l[coin_value].add(tuple([coin_value]))

for i in range(rng):
    print "i =", i
    for coin_list in l[i]:
        coin_list = list(coin_list)
        for coin_value in coin_values:
            new_index = i + coin_value
            if new_index < len(l):
                new_set = coin_list + [coin_value]
                new_set.sort()
                l[new_index].add(tuple(new_set))
print "result is", len(l[rng])
        
