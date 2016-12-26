'''
Created on Jun 13, 2016

@author: yuming
'''
import urllib
import urllib2
import requests
from lxml import html
import HTMLParser

url2 = "http://www.nature.com/nnano/journal/v5/n3/pdf/nnano.2010.40.pdf"
DOI = "http://dx.doi.org/10.1038/nnano.2010.40"
url = "http://dx.doi.org/10.1038/nnano.2010.40"
urltotal = url + url2
# url = "https://dphhs.mt.gov/Portals/85/dsd/documents/DDP/MedicalDirector/CommonCold101713.pdf"
# url = "http://ac.els-cdn.com/S0092867415011149/1-s2.0-S0092867415011149-main.pdf?_tid=e247a55e-31d1-11e6-a57c-00000aab0f26&acdnat=1465868999_1aa390351c3243738dc5f2c0e1424ecd"
 
opener = urllib2.build_opener()
opener.addheaders = [('Accept', 'application/vnd.crossref.unixsd+xml')]
r = opener.open(DOI)
print r.info() 
# ['Link']

# op = urllib2.Request(DOI) 
# op.add_header('Accept', 'application/vnd.crossref.unixsd+xml')
# print op
 
# response = urllib2.urlopen(urllib2.Request(url, headers={'CR-Clickthrough-Client-Token': '7fa87717-b6c88c29-5d778799-02f7d49c'}))

fr = requests.get(url2, headers={'CR-Clickthrough-Client-Token': '7fa87717-b6c88c29-5d778799-02f7d49c'});
# page = fr.content
# print page

 
# fileReq = urllib2.Request(url)
# fileReq.add_header("CR-Clickthrough-Client-Token: 7fa87717-b6c88c29-5d778799-02f7d49c", url)
# file = urllib2.urlopen(fileReq)
# with open('test.pdf', 'wb') as output:
#     output.write(file.read())

# fr = requests.get(url)
 
with open("filetest.pdf", "wb") as code:
    code.write(fr.content)
print "Download Complete!"    
    
# fr = requests.get(url)
# tree = html.fromstring(fr.content)
# buyers = tree.xpath('//text()')
# 
# print 'Buyers: ', buyers

# # print r
# print r.status_code
# # print r.headers
# print r.content
# 
# testfile = urllib.URLopener()
# testfile.retrieve(url, "test7.pdf")

# urllib2.urlopen("http://api.elsevier.com/content/article/PII:S0092867415015755?httpAccept=text/xml").read()
 
# def main():
#     download_file(url)
# 
# def download_file(download_url):
#     response = urllib2.urlopen(download_url)
#     file = open("test4.pdf", 'wb')
#     file.write(response.read())
#     file.close()
#     print("Completed")
# 
# if __name__ == "__main__":
#     main()