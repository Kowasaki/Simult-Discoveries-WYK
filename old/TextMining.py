'''
Created on Jun 20, 2016

@author: yuming
'''
import re
import fnmatch
import io

regex = '\(.+?\)'

# l = '(author 2010; author2 2013) testing testing (test) blahblah (fig 1) (ahh) hello'
# with open ('text.txt', 'r') as searchfile:
#     matches = re.findall(regex, searchfile)
#     print matches
counter = 33
begCounter = 1
d={}
for x in range(1,counter):
        d["{0}".format(x)] = []


# code = open('text.txt', "r+")
with open ('text.txt', 'r+') as code: 
    for line in code.readlines():
#         print line
        if not line.startswith('SEPARATOR:'):
#             print "test"
            d[str(begCounter)].append(line)
        else:
            begCounter += 1
#             print str(begCounter) + "SAW SEPARATOR"
        
        
# text = open('text.txt', 'r')
# for num in range(1, counter):
# d.sort
with open ("check.txt", "a") as code: 
    count = 1
    for diction in d:
        stringCurr = str(d[diction]).strip('[]')
        match = re.findall(regex, stringCurr)
        code.write("\n" + str(diction) + stringCurr + "\n")


#     matches = str(match).strip('[]')
# print matches
# regex2 = '\(\d+\)'
# regex2 = '\(;\+*\)'
# regex2 = '.+?\;.+?'
# regex2 = '\(.*?\;.*?\)'

# regex2 = '\(\d{4}\)'
        regex2 = "(.*)(\\d+)(.*)"
    # regex2 = '[^\(\d+\)].+$'
    
    
    
        newList = [x for x in d[diction] if ';' in x]
        listString = str(newList).strip('[]')
        
        
        matcher = re.compile('\({1}.+?\s\d{4};.+?\){1}')
        test2 = []
        for item in newList:
            var = re.findall(matcher, item)
            if len(var) != 0:
                test2.append(var[0])
    #     print test2**
        
        
            
        with open('cocitations.txt', "a") as newCode:
            newCode.write("\n" + "DICTIONARY: " + diction)
            newCode.write('\n')
            for item in test2:            
                newCode.write(item)
                newCode.write('\n')
