
def is_palindromic(l):
    s = 0
    e = len(l) - 1
    while e > s:
        if l[s] != l[e]:
            return False
        s += 1
        e -= 1
    return True

assert is_palindromic([1,2,3,2,1])
assert not is_palindromic([1,2,3,3,4,1])
x = 252
assert is_palindromic(str(x))

def check_int(x):
    return is_palindromic(str(x)) and is_palindromic('{0:08b}'.format(x).lstrip("0"))

assert check_int(585)

s = 0
for i in range(1*1000*1000):
    if check_int(i):
        print "found binary and base 10 palindrome", i, '{0:08b}'.format(i)
        s += i
print "result is", s
        
