'''
Created on Jun 28, 2016

@author: yumin
'''
import CellHtmlParsing
import LancetDOIHtmlParsing
import ScienceParsing
import CAParsing
import NEJMParsing
import CancerCellHtmlParsing
import CellStemCellHtmlParsing
import NatureHtmlParsing
import noDOINatureHtmlParsing

def testing():
    print "testing"
    
    with open('doiFormatted.txt', 'r') as toRead:
        masterList = []
        for line in toRead.readlines():
            masterList.append(line)
        print masterList
    #     for line in toRead
    
    # cellList = []
    lancetList = []
    scienceList = []
    caList = []
    nejmList = []
    # ccList = []
    stemList = []
    natureList = []
    titleList = []
    # doiFromTitles = []
    
    def sortInput(masterList):
        for item in masterList:
            item = item.lower()
            if '10.' in item:
    #             if 'ccell' in item:
    #                 ccList.append(item)
    #             elif 'cce11' in item:
    #                 ccList.append(item)
    #             elif 'ccr' in item:
    #                 ccList.append(item)
                if 'nature' in item:
                    natureList.append(item)
                elif 's0140-6736' in item:
                    lancetList.append(item)
                elif 'science' in item:
                    scienceList.append(item)
                elif 'caac' in item:
                    caList.append(item)
                elif 'nejm' in item:
                    nejmList.append(item)
    #             elif 'cell' in item:
    #                 cellList.append(item)
                elif 'stem' in item:
                    stemList.append(item)
                elif '/n' in item:
                    natureList.append(item)
                else:
                    print item + 'oops'
            else:
                titleList.append(item)
                print "just title"
        
#         NatureHtmlParsing.natureParsing(natureList) 
        CellStemCellHtmlParsing.stemParsing(stemList)
    #     CancerCellHtmlParsing.cancercellParsing(ccList)
        NEJMParsing.nejmParsing(nejmList)
        CAParsing.caParsing(caList)
        ScienceParsing.scienceParsing(scienceList)
        LancetDOIHtmlParsing.lancetParsing(lancetList)
    #     CellHtmlParsing.cellParsing(cellList)
    
    sortInput(masterList)
    doiFromTitles = noDOINatureHtmlParsing.findDOI(titleList)
    print doiFromTitles
    sortInput(doiFromTitles)
    
testing()