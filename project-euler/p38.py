
def generate_primes(length):
    l = [True] * length
    l[0] = False
    l[1] = False
    p = 2
    while p < (length/2) + 1:
        while not l[p]:
            p += 1
        j = 2*p
        while j < length:
            l[j] = False
            j += p
        p += 1
    return l


l = generate_primes(10*1000*1000)
print "generated l"

def append_left(num, x):
    length = len(str(num).lstrip("0"))
    return num + (x * (10 ** length))

assert append_left(0,1) == 1
assert append_left(10, 7) == 710

def append_right(num, x):
    return 10*num + x

assert append_right(10,1) == 101
assert append_right(23, 4) == 234

#pair of whether the number can be truncated to the left and right respectively
t = [(False, False)] * 1000000
t[2] = (True, True)
t[3] = (True, True)
t[5] = (True, True)
t[7] = (True, True)

for i in range(len(t)):
    for j in range(1, 10):
        if t[i][0]: #can be generated from left
            new_index = append_left(i, j)
            if l[new_index] and new_index < len(t):
                t[new_index] = (True, t[new_index][1])
        if t[i][1]: #can be generated from left
            new_index = append_right(i, j)
            if l[new_index] and new_index < len(t):
                t[new_index] = (t[new_index][0], True)

"""
for i in range(len(t)):
    print "i =", i, ":", t[i]
"""

s = 0
c = 0
for i in range(len(t)):
    if i > 10:
        if t[i][0] and t[i][1]:
            print "found truncatable value", i
            s += i
            c += 1

print "result is", s, "found", c, "numbers"
    
