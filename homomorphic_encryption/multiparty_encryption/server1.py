from sympy import * #symbolic execution library
import program

#class for storing and computing on encrypted values
class Computer:
	#list of values. Each value is a [value, offset] pair
	values = []
	output = []

	"""
	@param input_values: list of values with no offset
	"""
	def __init__(self, input_values):
		#list of variable names which will be str(variable_name)
		variable_name = 0 
		for value in input_values:
			variable_name += 1
			var = symbols(str(variable_name))
			self.values.append([value, var])

	def getValues(self):
		return self.values

	def getOutput(self):
		return self.output

	def compute(self):
		self.output = program.compute(self.values)

	def encrypt(self, b):
		assert (len(b) == program.output_size)
		for index in range(len(self.output)):
			self.output[index][0] = (self.output[index][0] + b[index]) % program.m
	