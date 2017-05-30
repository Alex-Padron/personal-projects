def get_digit_set(x):
    s = {}
    for i in range(10):
        s[i] = 0
    for d in str(x):
        s[int(d)] += 1
    return s

def set_equals(s1, s2):
    for el in s1:
        if s2[el] != s1[el]:
            return False
    return True

for i in range(0, 100000000):
    s = get_digit_set(i)
    valid = True
    for j in range(2,7):
        if not set_equals(get_digit_set(i*j), s):
            valid = False
    if valid:
        print "found solution", i
