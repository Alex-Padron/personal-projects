import sys
import math

def is_prime(x):
    for i in range(2, x - 1):
        if x % i == 0:
            return False
    return True

if __name__ == "__main__":
    n = int(sys.argv[1])
    l = -1
    for i in range(1, int(math.sqrt(n)) + 2):
        if n % i == 0:
            if is_prime(i):
                if l < i:
                    l = i
    print "Result is:", l
