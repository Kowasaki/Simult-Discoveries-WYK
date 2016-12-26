import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;
/*
 * FilterXML goes through the XML files and puts in relevant information for
 * these 15 journals into ArrayLists (which is added to an ArrayList of ArrayLists
 * in main, but in filter all articles are added to a single ArrayList of Articles). 
 * Relevant information for a parent article includes author, year, journal, title,
 * number of references, and an ID we create called "parNum", as well as DOI if 
 * applicable. More importantly we gather the references from each parent article,
 * which has relevant information such as author, year, journal, title. Note that
 * references only have one author due to the data we have, even if they should
 * have more. 
 */
public class FilterXML {
//	static int totalArticles = 0;
	static ArrayList<Article> nature = new ArrayList<Article>();
	static ArrayList<Article> science = new ArrayList<Article>();
	static ArrayList<Article> cell = new ArrayList<Article>();
	static ArrayList<Article> newEnglandJournalOfMedicine = new ArrayList<Article>();
	static ArrayList<Article> jama = new ArrayList<Article>();
	static ArrayList<Article> lancet = new ArrayList<Article>();
	static ArrayList<Article> ca = new ArrayList<Article>();
	static ArrayList<Article> natureGenetics = new ArrayList<Article>();
	static ArrayList<Article> natureMaterials = new ArrayList<Article>();
	static ArrayList<Article> natureMedicine = new ArrayList<Article>();
	static ArrayList<Article> natureImmunology = new ArrayList<Article>();
	static ArrayList<Article> natureNanotech = new ArrayList<Article>();
	static ArrayList<Article> natureBiotech = new ArrayList<Article>();
	static ArrayList<Article> cancerCell = new ArrayList<Article>();
	static ArrayList<Article> cellStemCell = new ArrayList<Article>();
	static ArrayList<ArrayList<Article>> allJournals = new ArrayList<ArrayList<Article>>();


	public static ArrayList<Article> filter(String fileIn, String fileOut) {
		ArrayList<Article> parArticles = new ArrayList<Article>();
		ArrayList<Article> allRefs = new ArrayList<Article>();
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			
			
			//for parent articles
			int count = 0;
			int year = 0;
			String title = "";
			ArrayList<String> authors = new ArrayList<String>();
			String DOI = "";
			int parNum = 1;
			int numCited = 0;
			String journal = "";
			int refCount = -1;
			int refCountChecker = 0;
			ArrayList<Article> references = new ArrayList<Article>();
			
			//for references
			int refYear = 0;
			String refTitle = "";
			ArrayList<String> refAuthors = new ArrayList<String>();
			String refID = "";
			ArrayList<Integer> parentArticles = new ArrayList<Integer>();
			
			while(br.ready()) {
				String line = br.readLine();
				if(line.trim().isEmpty()) {
					//start a new article
					count++;
				} else if(line.contains("pubyear=")) {
					year = Integer.parseInt(line.substring(line.indexOf("pubyear") + 9, 
							line.indexOf("pubyear") + 13));
				} else if (line.contains("<title type=\"source\">")) {
					journal = line.substring(line.indexOf("source\">") + 8, line.indexOf("</title>"));
				} else if (line.contains("<title type=\"item\">")) {
					title = line.substring(line.indexOf("item\">") + 6, line.indexOf("</title>"));
				} else if (line.contains("<full_name>") && line.contains(",")) {
					String currAuthor = line.substring(line.indexOf("<full_name>") + 11, 
							line.indexOf("</full_name>"));
					authors.add(currAuthor);
				} else if (line.contains("<UID>WOS:")) {
					DOI = line.substring(line.indexOf("WOS:") + 4, line.indexOf("</UID>"));
				} else if (line.contains("<references count=")) {
					System.out.println(line);
					if (line.contains("/>")) {
						refCount = Integer.parseInt(line.substring(line.indexOf("count=") + 7, 
								line.indexOf("\"/>")));
					} else {
						refCount = Integer.parseInt(line.substring(line.indexOf("count=") + 7, 
								line.indexOf("\">")));
					}
					
				}
				
				if (line.contains("<reference>")) {
					refYear = 0;
					refTitle = "empty";
					refAuthors = new ArrayList<String>();
					refID = "";
					parentArticles = new ArrayList<Integer>();
					
					while(br.ready() && !line.contains("</reference>")) {
						line = br.readLine();
						
						if(line.contains("<uid>")) {
							refID = line.substring(line.indexOf("<uid>") + 9, line.indexOf("</uid>"));
						} else if (line.contains("citedAuthor")) {
							String auth = line.substring(line.indexOf("<citedAuthor>") + 13, line.indexOf("</cited"));
							refAuthors.add(auth);
						} else if (line.contains("<year>")) {
							refYear = Integer.parseInt(line.substring(line.indexOf("<year>") + 6, line.indexOf("</year>")));
//							System.out.println(refYear);	
						} else if (line.contains("citedTitle")) {
							if(!line.contains("</citedTitle")) {
								refTitle = line.substring(line.indexOf("<citedTitle>") + 12).trim();
								while(!line.contains("</citedTitle")) {
									refTitle += " ";
									line = br.readLine().trim();
									refTitle += line;
									
								}
								refTitle += line.substring(0, line.indexOf("</citedTitle"));
								
//								System.out.println(refTitle);
							} else {
								refTitle = line.substring(line.indexOf("<citedTitle>") + 12, line.indexOf("</citedTitle"));
//								System.out.println(refTitle);
							}
							
						}
					}
					parentArticles.add(parNum);
					Article currentRef = new Article(refTitle, refYear, refAuthors, refID, parentArticles);
					boolean alreadyExists = false;
					
					for(Article curr: allRefs) {
						if (curr.equals(currentRef)) {
							curr.parentArticle.add(parNum);
							references.add(curr);
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						allRefs.add(currentRef);
						references.add(currentRef);
					}
					
				}
				
				if(line.contains("</REC>") && refCount != 0) {
					Article parArticle = new Article(title, year, authors, DOI, parNum, journal, references);
					journal = journal.trim();
					Article currArt = parArticle;
					if (journal.equalsIgnoreCase("Nature")) {
						nature.add(currArt);
					} else if(journal.equalsIgnoreCase("Science")) {
						science.add(currArt);
					} else if(journal.equalsIgnoreCase("Cell")) {
						cell.add(currArt);
					} else if(journal.equalsIgnoreCase("New England Journal of Medicine")) {
						newEnglandJournalOfMedicine.add(currArt);
					} else if(journal.contains("JAMA")) {
						jama.add(currArt);
					} else if(journal.equalsIgnoreCase("Lancet")) {
						lancet.add(currArt);
					} else if(journal.startsWith("CA-")) {
						ca.add(currArt);
					} else if(journal.equalsIgnoreCase("Nature Genetics")) {
						natureGenetics.add(currArt);
					} else if(journal.equalsIgnoreCase("Nature Materials")) {
						natureMaterials.add(currArt);
					} else if(journal.equalsIgnoreCase("Nature Immunology")) {
						natureImmunology.add(currArt);
					} else if(journal.equalsIgnoreCase("Nature Nanotechnology")) {
						natureNanotech.add(currArt);
					} else if(journal.equalsIgnoreCase("Nature BioTechnology")) {
						natureBiotech.add(currArt);
					} else if(journal.equalsIgnoreCase("Cancer Cell")) {
						cancerCell.add(currArt);
					} else if(journal.equalsIgnoreCase("Cell Stem Cell")) {
						cellStemCell.add(currArt);
					}
					
					
					parArticles.add(parArticle);
					
					
					parNum++;
					year = 0;
					title = "";
					authors = new ArrayList<String>();
					DOI = "";
					numCited = 0;
					journal = "";
					references = new ArrayList<Article>();
				}
				
				//this means that this is a parent article with no references
				if (refCount == 0) {
					year = 0;
					title = "";
					authors = new ArrayList<String>();
					DOI = "";
					numCited = 0;
					journal = "";
					refCount = -1;
					references = new ArrayList<Article>();
				} 
			
			}
				
			
			
		} catch(Exception e) {
				System.out.println("error" + e.getMessage());
				e.printStackTrace();
		}
		
		return parArticles;
	}
	
	/*
	 * Once you run filter, the return is an ArrayList of parent articles but
	 * Match uses an ArrayList of references. This just gets all the references
	 * from the parent articles and returns those in an ArrayList
	 */
	public static ArrayList<Article> findRefs(ArrayList<Article> parents) {
		ArrayList<Article> refs = new ArrayList<Article>();
		for(Article par: parents) {
			refs.addAll(par.references);
		}
		return refs;
	}
	
	public static void main(String[] args) {
		int parArtCounter = 1;
		String fileName = "XML.txt";
		String fileOutName = "XMLformatted.txt";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			//input file and output file should be command line arguments
			fileName = args[0];
			fileOutName = args[1];
			ArrayList<Article> allParents = filter(fileName, fileOutName);
			allJournals.add(nature);
			allJournals.add(science);
			allJournals.add(cell);
			allJournals.add(newEnglandJournalOfMedicine);
			allJournals.add(jama);
			allJournals.add(lancet);
			allJournals.add(ca);
			allJournals.add(natureGenetics);
			allJournals.add(natureMaterials);
			allJournals.add(natureMedicine);
			allJournals.add(natureImmunology);
			allJournals.add(natureNanotech);
			allJournals.add(natureBiotech);
			allJournals.add(cancerCell);
			allJournals.add(cellStemCell);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOutName));
			
			for(ArrayList<Article> journalArts : allJournals) {
				for(Article currArticle : journalArts) {
					bw.write(currArticle.toStringParent());
				}
			}
			
			
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
			e.printStackTrace();
		}
		//this section was for testing
		
//		for(Article currArt: filter(fileName, fileOutName)) {
//			System.out.println("Parent Article Count: " + parArtCounter);
//			String journ = currArt.journal.trim();
//			System.out.println(journ);
//			
////			System.out.println(currArt.year);
////			System.out.println(currArt.title);
//			System.out.println("Num refs: " + currArt.references.size());
//			int tick = 0;
//			for(Article currRef: currArt.references) {
//				tick++;
////				System.out.println("Reference title " + currRef.title);
////				System.out.println("Ref ID  " + currRef.id);
//			}
//			System.out.println("REF COUNT ACTUAL: " + tick);
//			parArtCounter++;
//			
//		}
//		System.out.println("TOTAL including 0 refs: " + totalArticles);
	}
}
