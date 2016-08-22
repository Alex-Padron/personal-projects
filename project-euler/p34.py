
factorials = {}
def factorial(x):
    if x in factorials:
        return factorials[x]
    m = 1
    while x > 1:
        m = m*x
        x -= 1
    factorials[x] = m
    return m

assert factorial(2) == 2
assert factorial(4) == 24

def factorial_of_digits(x):
    l = [int(i) for i in str(x)]
    s = 0
    for val in l:
        s += factorial(val)
    return s

assert factorial_of_digits(4) == 24
assert factorial_of_digits(24) == 26

s = 0
for i in range(10000000):
    if factorial_of_digits(i) == i:
        print "found solution i =", i
        s += i

print "result is", s
