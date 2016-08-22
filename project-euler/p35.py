
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

def l_to_num(l):
    s = 0
    m = 1
    i = len(l) - 1
    while i >= 0:
        s += m*l[i]
        m *= 10
        i -= 1
    return s

assert(l_to_num([1,2,3]) == 123)

def rotate(l):
    tmp = l[0] 
    i = 1
    while i < len(l):
        l[i - 1] = l[i] 
        i += 1
    l[-1] = tmp
    return l

assert rotate([1,2,3,4]) == [2,3,4,1]

count = 0
for i in range(1*1000*1000):
    if l[i]:
        num_list = [int(x) for x in str(i)]
        all_valid = True
        for j in range(10):
            num_list = rotate(num_list)
            if not l[l_to_num(num_list)]:
                all_valid = False
        if all_valid:
            print "found solution", i
            count += 1
print "result is", count

