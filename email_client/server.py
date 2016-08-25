
from http.server import BaseHTTPRequestHandler
from http.server import HTTPServer
from urllib.parse import urlparse, parse_qs
import smtplib

fromaddr = "padron.robin@newfairfieldschools.org"
password = "1denmw99"

server_ssl = smtplib.SMTP_SSL("smtp.gmail.com", 465)
server_ssl.ehlo() 
server_ssl.login(fromaddr, password)  
print("logged in to email service")

def rreplace(s, old, new, occurrence):
    li = s.rsplit(old, occurrence)
    return new.join(li)

class EmailHTTPServer_RequestHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        self.data_string = self.rfile.read(int(self.headers['Content-Length']))
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        
        split = str(self.data_string).split(":")
        to_addrs = [split[0][2:]]
        error = rreplace("\r\n".join(split[1:]), "'", "", 1)
        print("sending email to ", to_addrs[0])
        msg = "\r\n".join([
            "From: " + fromaddr,
            "To: " + to_addrs[0],
            "Subject: TEST",
            "",
            error
        ])
        return 
        server_ssl.sendmail(fromaddr, to_addrs, msg)
        print("sent mail successfully")
        return

def run():
    server_address = ('', 8080)
    httpd = HTTPServer(server_address, EmailHTTPServer_RequestHandler)
    print('running server...')
    httpd.serve_forever()

if __name__ == "__main__":
    run()
