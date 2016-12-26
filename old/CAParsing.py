'''
Created on Jun 14, 2016

@author: yuming
'''

from HTMLParser import HTMLParser
import urllib
import urllib2
import requests
from lxml import html
from bs4 import BeautifulSoup
import re
from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfpage import PDFPage
from cStringIO import StringIO


def convert_pdf_to_txt(path):
    rsrcmgr = PDFResourceManager()
    retstr = StringIO()
    codec = 'utf-8'
    laparams = LAParams()
    device = TextConverter(rsrcmgr, retstr, codec=codec, laparams=laparams)
    fp = file(path, 'rb')
    interpreter = PDFPageInterpreter(rsrcmgr, device)
    password = ""
    maxpages = 0
    caching = True
    pagenos=set()

    for page in PDFPage.get_pages(fp, pagenos, maxpages=maxpages, password=password,caching=caching, check_extractable=True):
        interpreter.process_page(page)

    text = retstr.getvalue()

    fp.close()
    device.close()
    retstr.close()
    return text


# doiList = [
#            "10.3322/caac.21321", 
# "10.3322/caac.21295", 
# "10.3322/caac.21286", 
# "10.3322/caac.21314", 
# "10.3322/caac.21288", 
# 
#            ]

def caParsing(doiList):
        header_doi = "http://dx.doi.org/"
        count = 0
        for item in doiList:
            try:
                count += 1
                print "Item: " + item
                url = "http://onlinelibrary.wiley.com/doi/" + item + "/pdf"
                print url
            
                response = requests.get(url)
                
                soup = BeautifulSoup(response.text, 'lxml')
                for link in soup.find_all(id="pdfDocument"):
                    total_url = link.get('src')
                    print "Total: " + total_url
            
                if total_url != "" :
                    fr = requests.get(total_url, headers={'CR-Clickthrough-Client-Token': '7fa87717-b6c88c29-5d778799-02f7d49c'})
                
                    pdfFilename = str(count) + "filetest.pdf"
                    txtFilename = str(count) + "text.txt"
                    directory = "C:\\Users\\yumin\\Desktop\\LiClipse Work\\CrossRef Testing\\src\\def\\" + pdfFilename
                    
                    with open(pdfFilename, "wb") as code:
                        code.write(fr.content)
                    print "PDF Download Complete!" 
                
                    with open('text.txt', "a") as code:
                        code.write(convert_pdf_to_txt(directory))
                    print "PDF to Text conversion successful!"
            except:
                pass