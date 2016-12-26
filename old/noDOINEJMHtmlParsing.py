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

titleList = [
            "Therapeutic Hypothermia in Deceased Organ Donors and Kidney-Graft Function Reply",
            "Takotsubo (Stress) Cardiomyopathy",
            "Therapeutic Hypothermia in Deceased Organ Donors and Kidney-Graft Function"

           ]
# header_doi = "http://dx.doi.org/"
count = 0
header = "http://search.crossref.org/?q="
for item in titleList:
    count += 1
    stri = " "
    item.rstrip()
    item = item.replace(stri, "+") 
    
#     url = header + item
    item = header + item
    print "Item: " + item

    response = requests.get(item)
#     print response.text
    soup = BeautifulSoup(response.text, 'html.parser')
    science_header = "http://science.sciencemag.org"

# print soup.getText()
    total_url = ""
#     for link in soup.find_all(href=re.compile("pdf")):
#     link = soup.find_all(href=re.compile("dx.doi"))
#     doi_url = link[1].get('href')
    def findDOI(soup):
        link = soup.find('a', href=re.compile("dx.doi"))
        print link
        return link
#     doi_url = link.get('href')
    
#     javastring = "javascript:showCiteBox('"
#     doi_url.replace(javastring, "")
#     doi_num = doi_url.find(",")
#     doi_url = doi_url[:-doi_num]
#     print "DOI!! " + doi_url
    
#     link = soup.find('a', href=re.compile("dx.doi"))
#     print(link.get('href'))
#     print link
    
    cross_url = findDOI(soup).get('href')
    print "CrossRef: " + cross_url
    nejm_header = "http://www.nejm.org"
    
    index = cross_url.find("org/")
    number = len(cross_url) - index
    total_url = nejm_header + "/doi/pdf/" + cross_url[index + 4:]
    print "TESTING: " + total_url
    try: 
        if cross_url != "":
            tr = requests.get(cross_url)
        soup2 = BeautifulSoup(tr.text, 'lxml')
        
    #     for link in soup2.find_all(href=re.compile("full.pdf")):
    #         total_url = science_header + link.get('href')
    #         end = total_url.find("pdf")
    #         num = len(total_url) - end
    #         total_url = total_url[:-(num - 3)]
    #         print "Total: " + total_url
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
            print "PDF to Text conversion successful!"
    except:
        print 'ERROR ~LOL~'
        pass
            # parser.feed(response.text)
            # print response.text