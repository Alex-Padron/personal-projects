import os
import requests

"""
Abstract class to provide a client interface for homomorphic encryption.
"""
class EncryptedRequest(object):


	def __init__(self):
		#The key object that the class can use to encrypt and decrypt
		self.KeyObj = None
		#size (in bits) of the key
		self.KeyLen = 0

	"""
	Method to send an http request to the server. This method calls the makeURLQuery() 
	method in order to generate the url.

	@param ipAddr: IP address to send to
	@param ctxts: The array of cipher texts to be passed into makeURLQuery
	@return: the result of the http request
	"""
	def sendHTTPRequest(self, ipAddr, ctxts):
		urlQuery = self.makeURLQuery(ctxts)
		url = "http://" + str(ipAddr) + urlQuery
		print("Sending request with url: " + url)
		return requests.get(url).content

	"""
	Method to take a list of cipher texts and generate a query for the server based on them.

	@param ctxts: the list of cipher texts to parse and make a string out of
	@return: the url query created from the cipher texts
	"""
	def makeURLQuery(self, ctxts):
		raise NotImplementedError("MakeURLQuery not implemented")


	"""
	Method to initialize the encryption object with a given key size. This method
	will populate both the KeyObj and KeyLen variables, based on the specific encryption
	scheme that is being implemented

	@param keySize: the size (in bits) of the key for the scheme to use
	"""
	def initialize(self, keySize):
		raise NotImplementedError("Initialize not implemented")


	"""
	Method to encrypt a given method under KeyObj and KeyLen. This method requires
	that the initialize method has been called previously.

	@param message: the message to encrypt
	@return: the cipher text corresponding to the encryption of the message under KeyObj and KeyLen
	"""
	def encrypt(self, message):
		raise NotImplementedError("Encrypt not implemented")


	"""
	Method to decrypt a cipher text under KeyObj and KeyLen. This method requires that
	the initialize method has been called previously.

	@param ctxt: the cipher text to decrypt
	@return the decrypted message under KeyObj and KeyLen
	"""
	def decrypt(self, ctxt):
		raise NotImplementedError("Decrypt not implemented")


	"""
	Method to provide a secure random function for specific implementations to use.

	@return the os urandom secure method
	"""
	def getSecureRand(self):
		return os.urandom





