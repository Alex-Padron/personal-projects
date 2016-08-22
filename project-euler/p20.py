

def factorial(x):
    r = 1
    while x > 0:
        r = r*x
        x -= 1
    return r

assert factorial(4) == 24
assert factorial(10) == 3628800

y = factorial(100)

l = [int(i) for i in str(y)]
c = 0 
for i in l:
    c += i
print "result is:", c
