

def divide_by_power_of_two(x, exp):
    return x >> exp
    
def find_closest_power_of_two(x):
    closest_value = 1
    p = 0
    while(2*closest_value < x):
        closest_value *= 2
        p += 1
    if abs(x - closest_value) < abs(x - 2*closest_value):
        return p
    else:
        return p + 1

#simple implementation of divide that doesn't use floating point
R = 30
def div(a, b):
    #initial guess
    x = divide_by_power_of_two(2**R, find_closest_power_of_two(b))
    #update with newton's method
    y = -1
    while y != x:
        y = x
        x = 2*x - divide_by_power_of_two((b*x*x), R)
    #multiply by a and remove the factor of R
    return divide_by_power_of_two(a*x, R)

def unit_test():
    print("testing divide_by_power_of_two...")
    assert(divide_by_power_of_two(16, 3) == 2)
    assert(divide_by_power_of_two(17, 3) == 2)
    assert(divide_by_power_of_two(8,0) == 8)
    print("...passed")
    
    print("testing find_closest_power_of_two...")
    assert(find_closest_power_of_two(2) == 1)
    assert(find_closest_power_of_two(15) == 4)
    assert(find_closest_power_of_two(65) == 6)
    print("...passed")
    
    print("testing div...")
    assert(div(4,2) == 2)
    assert(div(423, 76) == 5)
    assert(div(423423452454353, 42342424) == 10252939)
    print("...passed")
if __name__ == "__main__":
    unit_test()
