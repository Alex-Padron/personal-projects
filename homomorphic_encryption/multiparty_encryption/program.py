from sympy import * #symbolic execution library
import math
"""
This code is the program to be run homomorphically
"""

#global parameters for computation
m = int(math.pow(10,300))
input_size = 4
output_size = 2


def compute(input_values):
	for i in range(100):
		x = add(input_values[0],input_values[0])
		y = mul(input_values[1], input_values[0])
		a = mul(x,y)
		b = add(a,y)

	result = [a,b]
	print("finished computing, result is: ", result)
	return result


"""
Helper methods to add and multiply symbolically

Additionally, provides functionality for testing the 
runtime unencrypted
"""
def add(r1, r2):
	if (isinstance(r1,list)):
		val = r1[0] + r2[0]
		offset = simplify(r1[1] + r2[1])
		return [val % m, offset]
	else:
		return (r1 + r2) % m

def mul(r1, r2):
	if (isinstance(r1,list)):
		val = r1[0] * r2[0]
		offset = simplify((r1[0]*r2[1]) + (r1[1]*r2[0]) - (r1[1]*r2[1]))
		return [val % m, offset]
	else:
		return (r1 * r2) % m