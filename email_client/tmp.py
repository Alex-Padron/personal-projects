import smtplib

fromaddr = 'alexander.f.padron@gmail.com'
toaddrs  = ['alexander.f.padron@gmail.com']
msg = "\r\n".join([
  "From: user_me@gmail.com",
  "To: user_you@gmail.com",
  "Subject: Just a message",
  "",
  "Why, oh why"
  ])
username = 'alexander.f.padron@gmail.com'
password = '1denmw99'
server = smtplib.SMTP('smtp.gmail.com:587')

server_ssl = smtplib.SMTP_SSL("smtp.gmail.com", 465)
server_ssl.ehlo() # optional, called by login()
server_ssl.login(username, password)  
# ssl server doesn't support or need tls, so don't call server_ssl.starttls() 
server_ssl.sendmail(fromaddr, toaddrs, msg)
#server_ssl.quit()
server_ssl.close()

print "sent ssl"
print ()
print ()
server.ehlo()
server.starttls()
server.login(username,password)
server.sendmail(fromaddr, toaddrs, msg)
server.quit()
