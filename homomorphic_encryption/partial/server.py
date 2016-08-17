#!/usr/bin/env python

from http.server import BaseHTTPRequestHandler
from http.server import HTTPServer
from urllib.parse import urlparse, parse_qs

# HTTPRequestHandler class
class ElgamalHTTPServer_RequestHandler(BaseHTTPRequestHandler):

    # GET
    def do_GET(self):
        request = urlparse(self.path)
        route = request.path.split('/')[1]
        params = parse_qs(request.query)


        message = ""
        if route == "elgamal":
            message = self.elgamal(params)
        elif route == "paillier":
            message = self.paillier(params)
        else:
            self.send_response(404)

        self.end_headers()

        self.wfile.write(bytes(message, "utf8"))
        return

    def elgamal(self, params):
        self.send_response(200)
        self.send_header('Content-type','text/html')

        ar = int(params["ar"][0])
        am = int(params["am"][0])
        br = int(params["br"][0])
        bm = int(params["bm"][0])
        r = (ar * br)
        m = (am * bm)
        return str(r) + " " + str(m)

    def paillier(self, params):
        self.send_response(200)
        self.send_header('Content-type','text/html')

        x1 = int(params["x1"][0])
        x2 = int(params["x2"][0])
        return str(x1 * x2)

def run():
    print('starting server...')

    # Server settings
    # Choose port 8080, for port 80, which is normally used for a http server, you need root access
    server_address = ('0.0.0.0', 80)
    httpd = HTTPServer(server_address, ElgamalHTTPServer_RequestHandler)
    print('running server...')
    httpd.serve_forever()

if __name__ == "__main__":
    run()
