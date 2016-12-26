'''
Created on Jun 14, 2016

@author: yuming
'''

from HTMLParser import HTMLParser
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
# 
# doiList = [
#            "10.1038/Nnano.2010.6", 
#            "10.1038/Nnano.2010.7",
#            "10.1038/Nnano.2010.8",
#            "10.1038/Nnano.2010.9",
#            "10.1038/Nnano.2010.10",
#            "10.1038/Nnano.2010.11",
#            "10.1038/Nnano.2010.12"           
#            ]

doiList = [
           "10.1038/nature15764", 
           "10.1038/nature16070", 
           "10.1038/nature15741", 
           "10.1038/nature16189", 
           "10.1038/nature15749", 

           ]
header_doi = "http://dx.doi.org/"
count = 0
for item in doiList:
    count += 1
    item = header_doi + item
    print "Item: " + item

    response = requests.get(item)
#     print response.text
    soup = BeautifulSoup(response.text, 'lxml')
    nature_header_url = "http://www.nature.com"

# print soup.getText()
    total_url = ""
#     for link in soup.find_all(href=re.compile("pdf")):
    for link in soup.find_all(id=("pdfLink")):
        print(link.get('href'))
        total_url = nature_header_url + link.get('href')
        print "Total: " + total_url

    print total_url
    
    if total_url != "" :
        fr = requests.get(total_url, headers={'CR-Clickthrough-Client-Token': '7fa87717-b6c88c29-5d778799-02f7d49c'})
    
        pdfFilename = str(count) + "filetest.pdf"
        txtFilename = str(count) + "text.txt"
        directory = "C:\\Users\\yumin\\Desktop\\LiClipse Work\\CrossRef Testing\\src\\def\\" + pdfFilename
        
        with open(pdfFilename, "wb") as code:
            code.write(fr.content)
        print "PDF Download Complete!" 
    # print convert_pdf_to_txt('C:\\Users\\yumin\\Desktop\\filetest.pdf')
        
        with open(txtFilename, "wb") as code:
            code.write(convert_pdf_to_txt(directory))
        print "PDF to Text conversion successful!"
    # parser.feed(response.text)
    # print response.text