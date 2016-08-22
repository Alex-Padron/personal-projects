
class Num:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def value(self):
        return 10.*self.x + 1.*self.y

    def prune_x(self):
        self.x = 0
        
    def prune_y(self):
        self.y = self.x
        self.x = 0
    
    def to_string(self):
        return str(self.x) + str(self.y)

solutions = []
for a in range(10):
    for b in range(10):
        for c in range(10):
            for d in range(10):
                n1 = Num(a, b)
                n2 = Num(c, d)
                div = 0
                if n2.value() > 0:
                    div = n1.value() / n2.value()
                n1.prune_y()
                n2.prune_x()
                if n2.value() > 0 and div == n1.value() / n2.value() and b == c:
                    #check for non-trivial solution
                    n1 = Num(a, b)
                    n2 = Num(c, d)
                    n1.prune_y()
                    n2.prune_y()
                    if n2.value() > 0 and (not n1.value() / n2.value() == div):
                        print "found solution:", (10*a + b), "/", (10*c + d), "with b =", b, "and d =", d
