from hashtbl import Hashtbl

def divide_by_power_of_two(x, exp):
    return x >> exp
    
def is_even(x):
    return x == (x >> 1) << 1
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

def exp(a, b):
    dp_store = Hashtbl(4)
    def f(a, b):
        #if we have already computed the result for this exponent, return it
        if dp_store.get(str(b)) != None:
            return dp_store.get(str(b))
        if b == 1:
            return a
        if b == 0:
            return 1
        if is_even(b):
            b =  divide_by_power_of_two(b, 1)
            result = f(a*a, b)
            dp_store.set(str(b), result)
            return result
        else:
            b = divide_by_power_of_two(b-1, 1)
            result = a*f(a*a, b)
            dp_store.set(str(b), result)
            return result
    return f(a, b)
    
#simple implementation of divide that doesn't use floating point
R = 30
def div(a, b):
    #initial guess
    x = divide_by_power_of_two(exp(R,2), find_closest_power_of_two(b))
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

    print("testing is_even...")
    assert(is_even(2))
    assert(not is_even(3))
    assert(is_even(0))
    assert(not is_even(27))
    print("...passed")

    print("testing exp...")
    assert(exp(3,2) == 9)
    assert(exp(1,1) == 1)
    assert(exp(3,0) == 1)
    assert(exp(0,1) == 0)
    assert(exp(2,10) == 1024)
    print("...passed")

    print("testing div...")
    assert(div(4,2) == 2)
    assert(div(423, 76) == 5)
    assert(div(400*760, 760) == 400)
    print("...passed")
if __name__ == "__main__":
    unit_test()
