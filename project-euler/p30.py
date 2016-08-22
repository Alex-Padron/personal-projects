

def power_digits(digits):
    s = 0
    for digit in digits:
        s += digit ** 5
    return s

s = 0
for i in range(2, 10000000):
    if i == power_digits([int(j) for j in str(i)]):
        print "found valid value", i
        s += i
print "result is", s
                         
