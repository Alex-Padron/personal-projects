import os
import program
import server1
import server2
import time

class client:
	#inputs to the client
	inputs = []
	#inputs encrypted under a
	encrypted_inputs = []
	#outputs 
	outputs = []

	a = []
	b = []

	def __init__(self, inputs):
		assert (program.input_size > 0)
		assert (len(inputs) == program.input_size)
		self.inputs = inputs

	def generateA(self):
		for i in range(program.input_size):
			#pick random secure pad in range(0,m-1)
			rand = getSecurePad()

			#encrypt under rand
			self.encrypted_inputs.append((rand + self.inputs[i]) % program.m)

			#store the random value in the a array
			self.a.append(rand)

	def generateB(self):
		for i in range(program.output_size):
			rand = getSecurePad()
			self.b.append(rand)

	def decrypt(self, values):
		assert (len(values) == program.output_size)
		for i in range(len(values)):
			values[i] = (values[i] - self.b[i]) % program.m
		self.outputs = values

	def pprint(self):
		print("Printing MPHE Client")
		print("Unencrypted input is: ", self.inputs)
		print("a is: ", self.a)
		print("b is: ", self.b)
		print("encrypted input is: ", self.encrypted_inputs)

	def getEncryptedValues(self):
		return self.encrypted_inputs

	def getA(self):
		return self.a

	def getB(self):
		return self.b

	def getInputs(self):
		return self.inputs

	def getOutputs(self):
		return self.outputs



"""
Helper method to get secure random numbers
"""
def getSecurePad():
	rand = int.from_bytes(os.urandom(8), byteorder="big") / ((1 << 64) - 1)
	return int(rand * program.m)


if __name__ == "__main__":
	print("Testing time with homomorphic encryption...\n")
	start_time = time.time()
	#start client
	inputs = [getSecurePad(),getSecurePad(),getSecurePad(),getSecurePad()]
	cl = client(inputs)
	cl.generateA()
	cl.generateB()
	cl.pprint()

	end_time = time.time()
	print("Starting client took: ", end_time - start_time, " seconds")

	start_time = time.time()

	#start server1
	sv1 = server1.Computer(cl.getEncryptedValues())
	sv1.compute()
	#encrypt server1's values under b
	sv1.encrypt(cl.getB())

	end_time = time.time()

	print("Server 1 computation took: ", end_time - start_time, " seconds")

	start_time = time.time()

	#simplify the result with server 2
	sv2 = server2.Simplifier(sv1.getOutput())
	sv2.simplify(cl.getA())

	end_time = time.time()

	print("Server 2 computation took: ", end_time - start_time, " seconds")

	start_time = time.time()

	#client decrypt under b
	cl.decrypt(sv2.getValues())

	end_time = time.time()

	print("Client decryption took: ", end_time - start_time, " seconds")

	print("Final decrypted result is: ", cl.getOutputs())
	encrypted_results = cl.getOutputs()
	print("\nTesting time running on client...\n")

	start_time = time.time()
	#call the given program with these new functions
	result = program.compute(inputs)

	#check that our results are correct
	assert (result == encrypted_results)

	end_time = time.time()

	print("Time running locally was: ", end_time - start_time)









