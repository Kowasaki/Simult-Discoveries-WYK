# Simult-Discoveries-WYK
My version of scripts for Simultaneous Discoveries project

*All “To use” sections are meant to be run on the command line.

A1

•	XMLOperations

  	o	Extracts all the reference-parent information from the XMLs and convert into csv format. Only the 15 parent journals are included but no additional filtering done.
  
  	o	Input: XML files
  
  	o	Output: allyears.csv (all csvs concatenated
  
 		o	To use: java XMLOperations [name of xml file]
  
•	pArg.sh

  	o	Shell script for running XMLOperations on HPCC
  
  	o	To use: qsub pArg.sh [name of xml file]
  
A2 – A3

•	CSVParser

  	o	Parses the csv created by XMLOperations and filters out entries with missing information (except DOI).
		
  	o	CSV Format: ref. title, “author”, pub. year, ref. DOI, ref. journal, parent title, “author1,author2,…”, pub. year, parent DOI, parent journal, pair ID, child ID, parent ID, child ID count
		
  	o	Input: allyears.csv
		
  	o	Output: parsed-##.csv
		
  	o	To use: java CSVParser [name of csv file]
		
•	parser.sh

		o	Runs the CSVParser on HPCC

		o	To use: qsub parser.sh
		
•	PairFinder

		o	Identifies the child IDs which were often cited in publications (determined by jaccard index of >= 0.5)

		o	CSV Format: ChildID1:ChildID2:…, “ParentID1,ParentID2…”, jaccard index

		o	Input: parsed-##.csv

		o	Output: pairs-##.csv

		o	To use: java PairFinder [name of csv file]
	
•	findPairs.sh

  	o	Runs PairFinder on HPCC
		
  	o	To use: qsub findPairs.sh

