from sympy import *
import program

class Simplifier():
	values = []
	offsets = []

	def __init__(self, vals):
		for i in range(len(vals)):
			self.values.append(vals[i][0])
			self.offsets.append(vals[i][1])

	def simplify(self, a):
		assert (len(a) == program.input_size)
		for i in range(len(self.values)):
			#make the list of sympy symbols
			replacements = []
			for j in range(program.input_size):
				sym = symbols(str(j+1))
				replacements.append((sym, a[j]))

			#perform simplification
			result = self.offsets[i].subs(replacements)
			self.values[i] = (self.values[i] - result) % program.m 


	def getValues(self):
		return self.values
