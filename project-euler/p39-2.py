from sets import Set

def is_right_triangle(a, b, c):
    return a ** 2 + b ** 2 == c ** 2

def find_right_triangles(p):
    seen = Set()
    for a in range(p):
        for b in range(p - a):
            c = p - a - b
            if is_right_triangle(a, b, c) and a > 0 and b > 0 and c > 0:
                l = [a,b,c]
                l.sort()
                seen.add(tuple(l))
                #print "found right triangle. a =", a, "b =", b, "c =", c
    return len(seen)

print find_right_triangles(120)
assert find_right_triangles(120) == 3
maxc = 0
for p in range(1000):
    c = find_right_triangles(p) 
    if c > maxc:
        print "found optial solution, p =", p, "c =", c
        maxc = c
