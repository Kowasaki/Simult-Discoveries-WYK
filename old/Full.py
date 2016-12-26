'''
Created on Jun 22, 2016

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

masterList = [
#               "Comprehensive analysis of cancer-associated somatic mutations in class I HLA genes (vol 33, pg 1152, 2015)",
# #            "Large-scale identification of sequence variants influencing human transcription factor occupancy in vivo (vol 47, pg 1393, 2015)",
# #            "ELECTROCATALYSIS Powered by porphyrin packing",
# #            "Considerations regarding the micromagnetic resonance relaxometry technique for rapid label-free malaria diagnosis reply",
# #            "The receptor NLRP3 is a transcriptional regulator of T(H)2 differentiation (vol 16, pg 859, 2015)",
# #            "Graphic design for scientists (vol 10, 1084, 2015)",
#            "Name: Refugee and migrant health: a priority in the WHO European Region",
# "Ebola vaccination  F. Fitzgerald, S. Yeung, D. M. Gibb, D. E. Baion and A. Pollard",
# "Ebola vaccination S. Tschudin-Sutter, A. F. Widmer, P. Emmerich, J. Schmidt-Chanasit and M. Battegay",
# # "Public investment in biomedical research in Europe (vol 386, pg 1335, 2015)",
# # "Extreme, expedition, and wilderness medicine",
# "Therapeutic Hypothermia in Deceased Organ Donors and Kidney-Graft Function Reply",
#             "Takotsubo (Stress) Cardiomyopathy",
# #             "Therapeutic Hypothermia in Deceased Organ Donors and Kidney-Graft Function"
# #             "Opportunities and challenges in liquid cell electron microscopy",
# 
# #                "10.1038/nnano.2015.289", 
# "10.1038/Nnano.2015.222", 
# # "10.1038/nnano.2015.232", 
# # "10.1038/Nnano.2015.219", 
# #               "10.1038/nm.3998", 
# "Editorial expression of concern",
# # "Preventing tropical mining disasters",
# "Marine Protected Areas miss the boat",
# # "10.1038/nm.3811", 
# # "10.1038/nm1215-1374", 
# # "10.1038/nm.4000", 
# # "10.1038/nm.4004", 
# # "10.1038/nm.3995", 
# #               "10.1038/Nmat4426", 
# # "10.1038/Nmat4408", 
# # "10.1038/Nmat4396", 
# # "10.1038/Nmat4374", 
# # "10.1038/Nmat4387", 
# #               "10.1038/ni.3292", 
"10.1038/ni.3268", 
"10.1038/ni.3293", 
              "10.1038/nature14903", 
"10.1038/nature15515", 
"10.1038/nature15368", 
"10.1038/nature14978", 
              "10.1038/ng.3422", 
"10.1038/ng.3431", 
"10.1038/ng.3440", 
"10.1038/ng.3437", 
"10.1038/ng.3456", 
# # "10.1038/ng.3455", 
# #                "10.1038/nbt.3425", 
# # "10.1038/nbt.3426",
# # "10.1038/nbt1215-1220", 
# # "10.1038/nbt.3386", 
# # "10.1038/nbt.3372", 
# # "10.1016/j.stem.2015.11.008", 
# # "10.1016/j.stem.2015.11.015", 
# # "10.1016/j.stem.2015.11.014", 
# # "10.1016/j.stem.2015.11.010", 
# # "10.1016/j.stem.2015.11.012", 
# # "10.1016/j.stem.2015.11.011", 
# # "10.1016/j.stem.2015.09.005" 
# # "10.1016/j.ccell.2015.12.001", 
# # "10.1016/j.ccell.2015.10.004", 
# # "10.1016/j.ccell.2015.10.008", 
# # "10.1016/j.ccell.2015.11.005", 
# # "10.1016/j.ccell.2015.11.010", 
# # "10.1016/j.ccell.2015.10.013", 
# # "10.1016/j.ccell.2015.11.002", 
# # "10.1016/j.ccell.2015.11.008", 
# # "10.1056/NEJMcibr1511291", 
# # "10.1056/NEJMp1509788", 
# # "10.1056/NEJMp1512241", 
# # "10.1056/NEJMoa1505949", 
# #"10.3322/caac.21321", 
# # "10.3322/caac.21295", 
# # "10.3322/caac.21288",
# # "10.1126/science.aab2713", 
# # "10.1126/science.aac9584",
# # "10.1126/science.aad9467",
            ]

print "testing"

# with open('doiFormatted.txt', 'r') as toRead:
#     masterList = []
#     for line in toRead.readlines():
#         masterList.append(line)
#     print masterList

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
    
    NatureHtmlParsing.natureParsing(natureList) 
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

