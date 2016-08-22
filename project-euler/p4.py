

def is_palindrome(x):
    l = [int(i) for i in str(x)]
    start = 0
    for i in range(len(l)):
        if l[i] != l[len(l) - 1 - i]:
            return False
    return True

if __name__ == "__main__":
    l = 0
    for i in range(100,1000):
        for j in range(100, 1000):
            if is_palindrome(i*j):
                print "found palindrome:", i*j
                if i*j > l:
                    l = i*j
    print "Result is:", l
