
if __name__ == "__main__":
    for a in range(1000):
        for b in range(1000 - a):
            c = 1000 - a - b
            if a**2 + b**2 == c**2:
                print "found values", a, b, c, "product is", a*b*c
    print "done"
                
