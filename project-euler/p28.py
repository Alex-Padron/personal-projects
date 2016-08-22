
s = 1001
l = [None] * s
for i in range(len(l)):
    l[i] = [0]*s

def check_bounds(x, y):
    return x >= 0 and x < len(l) and y >= 0 and y < len(l)

def move(x, y, d):
    if d == 0:
        return (x+1), y
    elif d == 1:
        return x, (y+1)
    elif d == 2:
        return (x-1), y
    elif d == 3:
        return x, (y-1)
    else:
        print "invalid direction"
        assert False

side_length = 1
side_counter = 1
inc = 1
d = 0
x = (len(l) - 1)/2
y = (len(l) - 1)/2

while check_bounds(x,y):
    l[y][x] = inc
    x,y = move(x,y,d)
    inc += 1
    side_length -= 1
    if side_length == 0:
        d  = (d+1) % 4
        side_counter += 1
        side_length = int((side_counter + 1)/2)

"""
for line in l:
    print line
"""

r = 0
for i in range(s):
    r += l[i][i]

for i in range(s):
    if i != (len(l) - 1)/2:
        r += l[i][s-1-i]
print "result is", r


