from sets import Set

def is_pandigital(l):
    seen = Set()
    for digit in l:
        if digit in seen:
            return False
        seen.add(digit)
    return len(seen) == 9 and (not "0" in seen)

assert not is_pandigital([1,2,3])
assert is_pandigital([1,9,2,3,8,4,7,5,6])

def l_to_num(l):
    s = 0
    m = 1
    i = len(l) - 1
    while i >= 0:
        s += m*int(l[i])
        m *= 10
        i -= 1
    return s

assert(l_to_num("123") == 123)

def generate_list(i, n):
    s = 1
    result = ""
    while s <= n:
        r = str(i * s).lstrip("0")
        result = result + r
        s += 1
    return result

assert generate_list(10,2) == "1020"
assert generate_list(192, 3) == "192384576"
print "generated", generate_list(9352, 2)
assert not is_pandigital(generate_list(9352, 2))
maxv = 0
for i in range(100*1000):
    for n in range(10):
        l = generate_list(i, n)
        if is_pandigital(l):
            if l_to_num(l) > maxv:
                print "found solution", l_to_num(l), "with i =", i, "and n =", n
                maxv = l_to_num(l)
