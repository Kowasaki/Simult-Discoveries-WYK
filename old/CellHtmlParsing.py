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

def cellParsing(doiList):
# doiList = ['10.1016/j.cell.2015.12.011',
# '10.1016/j.cell.2015.11.017',
# '10.1016/j.cell.2015.11.059',
# '10.1016/j.cell.2015.12.013',
# '10.1016/j.cell.2015.12.003',
# '10.1016/j.cell.2015.10.072',
# '10.1016/j.cell.2015.11.028',
# '10.1016/j.cell.2015.10.069',
# '10.1016/j.cell.2015.11.041',
# '10.1016/j.cell.2015.11.032',
# '10.1016/j.cell.2015.11.009',
# '10.1016/j.cell.2015.11.025',
# '10.1016/j.cell.2015.10.071',
# '10.1016/j.cell.2015.11.033',
# '10.1016/j.cell.2015.10.076',
# '10.1016/j.cell.2015.11.026',
# '10.1016/j.cell.2015.11.015',
# '10.1016/j.cell.2015.10.046',
# '10.1016/j.cell.2015.10.031',
# '10.1016/j.cell.2015.11.001',
# '10.1016/j.cell.2015.10.041',
# '10.1016/j.cell.2015.11.002',
# '10.1016/j.cell.2015.10.075',
# '10.1016/j.cell.2015.10.074',
# '10.1016/j.cell.2015.10.051',
# '10.1016/j.cell.2015.10.013',
# '10.1016/j.cell.2015.10.025',
# '10.1016/j.cell.2015.10.017',
# '10.1016/j.cell.2015.10.012',
# '10.1016/j.cell.2015.10.057',
# '10.1016/j.cell.2015.10.037',
# '10.1016/j.cell.2015.10.016',
# 
#            ]
    
        header_doi = "http://dx.doi.org/"
        count = 0
        for item in doiList:
            try:
                count += 1
                item = header_doi + item
                print "Item: " + item
            
                response = requests.get(item)
                soup = BeautifulSoup(response.text, 'lxml')
                
                total_url = ""
                for link in soup.find_all(id=("pdfLink")):
                    print(link.get('href'))
                    total_url = link.get('href')
                    print "Total: " + total_url
            
                print total_url
                
                if total_url != "" :
                    fr = requests.get(total_url, headers={'CR-Clickthrough-Client-Token': '7fa87717-b6c88c29-5d778799-02f7d49c'})
                
                    pdfFilename = str(count) + "filetest.pdf"
                    directory = "C:\\Users\\yumin\\Desktop\\LiClipse Work\\CrossRef Testing\\src\\def\\" + pdfFilename
                    
                    with open(pdfFilename, "wb") as code:
                        code.write(fr.content)
                    print "PDF Download Complete!" 
                    
                    with open("text.txt", "a") as code:
                        code.write(convert_pdf_to_txt(directory))
                        code.write("\n" + "SEPARATOR: " + str(count))
                    print "PDF to Text conversion successful!"
            except:
                pass