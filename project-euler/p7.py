import sys

def is_prime(x):
    for i in range(2, x - 1):
        if x % i == 0:
            return False
    return True

if __name__ == "__main__":
    n = int(sys.argv[1])
    c = 0
    i = 2
    while True:
        if is_prime(i):
            c += 1
            if c % 1000 == 0:
                print "calculated", c, "prime numbers"
            if c == n:
                break
        i += 1
    print "result is", i
