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
# titleList = [
#             "Comprehensive analysis of cancer-associated somatic mutations in class I HLA genes (vol 33, pg 1152, 2015)",
#            "Large-scale identification of sequence variants influencing human transcription factor occupancy in vivo (vol 47, pg 1393, 2015)",
#            "ELECTROCATALYSIS Powered by porphyrin packing",
#            "Considerations regarding the micromagnetic resonance relaxometry technique for rapid label-free malaria diagnosis reply",
#            "The receptor NLRP3 is a transcriptional regulator of T(H)2 differentiation (vol 16, pg 859, 2015)",
#            "Graphic design for scientists (vol 10, 1084, 2015)",
#            ]

def findDOI(titleList):
        newList = []
        count = 0
        header = "http://search.crossref.org/?q="
        for item in titleList:
            count += 1
            stri = " "
            item.rstrip()
            item = item.replace(stri, "+") 
            
            item = header + item
            print "Item: " + item
        
            response = requests.get(item)
            soup = BeautifulSoup(response.text, 'html.parser')
        
            total_url = ""
            try:
                def DOI(soup):
                    link = soup.find('a', href=re.compile("dx.doi"))
        #             print link
                    doiFull = link.get('href')
                    correctDOI = doiFull[18:]
                    newList.append(correctDOI)
                    return link
            except:
                pass    
            DOI(soup)
        return newList
    
        
        ''' cross_url = DOI(soup).get('href')
        print "CrossRef: " + cross_url
    
    
        if cross_url != "":
            tr = requests.get(cross_url)
        soup2 = BeautifulSoup(tr.text, 'lxml')
        
        for link in soup2.find_all(href=re.compile("pdf/n")):
            total_url = nature_header_url + link.get('href')
            print "Total: " + total_url
        #     print total_url
            
        if total_url != "" :
            fr = requests.get(total_url, headers={'CR-Clickthrough-Client-Token': '7fa87717-b6c88c29-5d778799-02f7d49c'})
            
            pdfFilename = str(count) + "filetest.pdf"
        #         txtFilename = str(count) + "text.txt"
            directory = "C:\\Users\\yumin\\Desktop\\LiClipse Work\\CrossRef Testing\\src\\def\\" + pdfFilename
                
            with open(pdfFilename, "wb") as code:
                code.write(fr.content)
            print "PDF Download Complete!" 
            # print convert_pdf_to_txt('C:\\Users\\yumin\\Desktop\\filetest.pdf')
                
            with open(str(count) + "text.txt", "wb") as code:
               code.write(convert_pdf_to_txt(directory))
               code.write("\n" + "SEPARATOR: " + str(count) + "\n")
            print "PDF to Text conversion successful!"'''
