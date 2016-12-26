import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeSet;

public class Match {

	
	/* determines if there are duplicates of articles and returns the unique ones
	 * input: ArrayList of Articles
	 * output: ArrayList of Articles
	*/
	public static ArrayList<Article> findDuplicates(ArrayList<Article> articles) {
		//sort articles by year and then by title
		Collections.sort(articles);
		//contains duplicate ids
		TreeSet<String> duplicates = new TreeSet<String>();
		ArrayList<Article> currYears = articles;

		//copies of the article lists
		ArrayList<Article> copy = new ArrayList<Article>();
		ArrayList<Article> copy2 = new ArrayList<Article>();
		for (Article n: currYears) {
			copy.add(n);
			copy2.add(n);
		}

		System.out.println("Size of list of references: " + currYears.size());
		Collections.sort(currYears);
		Collections.sort(copy);
		//nested for loop to find duplicates and add them to the list based on same titles
		for(int i = currYears.size() - 1; i >= 0; i--) {
			String title = currYears.get(i).title;
			title = (title.trim()).toLowerCase();
			copy.remove(currYears.get(i)); //the inside loop slowly decreases in size 
			for(int j = 0; j < copy.size(); j++) {
				String comparison = copy.get(j).title;
				comparison = (comparison.trim()).toLowerCase();
				if (title.equals(comparison)) {
					duplicates.add(copy.get(j).id);
					currYears.get(i).numCited++;
//					currYears.get(i).parentArticle.add(String.valueOf(copy.get(j).parentNumber));
					currYears.get(i).parentArticle.add(copy.get(j).parentNumber);
				}
			}
		}
		
		articles = currYears;
		//remove the duplicates based off of what we found
		for(Article curr: copy2) {
			for(String id: duplicates) {
				if (curr.id.equals(id)) {
					currYears.remove(curr);
				}
			}
		}

		//write the unique ones out to a file- mostly for testing
		try {
			FileWriter fw = new FileWriter("unique.txt");
			BufferedWriter bw = new BufferedWriter(fw);

			Collections.sort(currYears);
			for(Article unique: currYears) {
				bw.write("Unique title: " + unique.title);
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return currYears;
	}
	
	
	/* The pair method goes through the now unique ArrayList of Articles, figures out pairs
	 * based on being +1 / -1 year away from each other as well as not having the same
	 * authors overlapping, and then creates an ArrayList of Pairs based on this information,
	 * which is returned.
	 * 
	 */
	public static ArrayList<Pair> pair(ArrayList<Article> uniques) {
		ArrayList<Article> multCited = new ArrayList<Article>();
		int minYr = Integer.MAX_VALUE;
		int maxYr = Integer.MIN_VALUE;
		int one_cite_counter = 0;
		//add the unique articles that were cited more than once to a list
		for (Article n : uniques) {
			if (n.numCited > 0) {
				multCited.add(n);
				if (n.year < minYr) {
					minYr = n.year;
				} else if (n.year > maxYr) {
					maxYr = n.year;
				}
			} else {
				one_cite_counter++;
			}
		}
		System.out.println("Num of articles only cited once: " + one_cite_counter);
		Collections.sort(multCited);

		ArrayList<Pair> cocited = new ArrayList<Pair>();
		ArrayList<Pair> cocitedFinal = new ArrayList<Pair>();
		TreeSet<Pair> overlap = new TreeSet<Pair>();
		int counter = 0;
		int pair_count = 0;
		int pair_count2 = 0;

		//going through all the years systematically
		while(minYr <= maxYr) {
			ArrayList<Article> currYears = new ArrayList<Article>(); 
			for(Article curr: multCited) {
				//if the articles are within one year of the current year we're focused on
				if (curr.year == minYr || curr.year == minYr + 1 || curr.year == minYr - 1) {
					currYears.add(curr);
				}
			}

			ArrayList<Article> baseYear = new ArrayList<Article>();
			ArrayList<Article> copy = new ArrayList<Article>();
			for (Article a: currYears) {
				if (a.year == minYr) {
					baseYear.add(a);
					copy.add(a);
				}
			}

			//find cocited articles, create pairs and add to list of cocited
			for (int i = baseYear.size() - 1; i >= 0; i--) {
				copy.remove(baseYear.get(i));
				for (int j = 0; j < copy.size(); j++) {
//					ArrayList<String> parent1arts = baseYear.get(i).parentArticle;
//					ArrayList<String> parent2arts = copy.get(j).parentArticle;
					ArrayList<Integer> parent1arts = baseYear.get(i).parentArticle;
					ArrayList<Integer> parent2arts = copy.get(j).parentArticle;
					Collections.sort(parent1arts); 
					Collections.sort(parent2arts);
					int artNum1 = parent1arts.size() - 1;
					int artNum2 = parent2arts.size() - 1;
					while (artNum1 >= 0 && artNum2 >= 0) {
//						String art1 = parent1arts.get(artNum1).toUpperCase().trim();
//						String art2 = parent2arts.get(artNum2).toUpperCase().trim();
						int art1 = parent1arts.get(artNum1);
						int art2 = parent2arts.get(artNum2);
//						if (art1.compareTo(art2) == 0) {
						if (art1 - art2 == 0) {
							Pair pair = new Pair(baseYear.get(i), copy.get(j));
							cocited.add(pair);
							cocitedFinal.add(pair);
							pair_count++;
							break;
//						} else if(art1.compareTo(art2) > 0) {
						} else if(art1 - art2> 0) {
							artNum1--;
						} else {
							artNum2--;
						}
					}
				}
			}


			//remove the pairs with overlapping authors
			for (Pair cocitation : cocited) {
				ArrayList<String> parent1auths = cocitation.one.authors;
				ArrayList<String> parent2auths = cocitation.two.authors;
				Collections.sort(parent1auths); 
				Collections.sort(parent2auths);
				int authNum1 = parent1auths.size() - 1;
				int authNum2 = parent2auths.size() - 1;
				while (authNum1 >= 0 && authNum2 >= 0) {
					String auths1 = parent1auths.get(authNum1).toLowerCase();
					String auths2 = parent2auths.get(authNum2).toLowerCase();
					if (auths1.compareTo(auths2) == 0) {
						cocitedFinal.remove(cocitation);
						overlap.add(cocitation);
						counter++;
						authNum1--;
						authNum2--;
						break;
					} else if((auths1).compareTo(auths2) > 0) {
						authNum1--;
					} else {
						authNum2--;
					}
				}
			}
			minYr++; //move on to the next year to check through
		}


		//printing is for testing and metrics recording
		System.out.println("Size of unique list: " + uniques.size());
		System.out.println("Total number of pairs pre-author removal: " + pair_count);
		System.out.println("Number of overlapping author pairs: " + overlap.size());
		System.out.println("Number of non-author-overlapping cocitations: " + cocitedFinal.size());
		return cocitedFinal;
	}

	/*
	 * Determines Jaccard Index of pairs, returns the same pairs but with the
	 * indices updated.
	 */
	public static ArrayList<Pair> jaccard(ArrayList<Pair> pairs) {
		int count = 0;
		for (Pair pr : pairs) {
			count++;
//			ArrayList<String> par1 = pr.one.parentArticle;
//			ArrayList<String> par2 = pr.two.parentArticle;
			ArrayList<Integer> par1 = pr.one.parentArticle;
			ArrayList<Integer> par2 = pr.two.parentArticle;
			double total1 = par1.size() + 1;
			double total2 = par2.size() + 1;
			Collections.sort(par1); 
			Collections.sort(par2);
			int artNum1 = par1.size() - 1;
			int artNum2 = par2.size() - 1;
			while (artNum1 >= 0 && artNum2 >= 0) {
//				String art1 = par1.get(artNum1);
//				String art2 = par2.get(artNum2);
				int art1 = par1.get(artNum1);
				int art2 = par2.get(artNum2);
//				if (art1.compareTo(art2) == 0) {
				if (art1 - art2 == 0) {
					pr.cocite++;
					pr.comParents.add(art1);
					artNum1--;
					artNum2--;
//				} else if(art1.compareTo(art2) > 0) {
				} else if(art1 - art2 > 0) {
					artNum1--;
				} else {
					artNum2--;
				}
			}
			pr.jacc = (pr.cocite) / (total1 + total2 - pr.cocite);
			if (pr.jacc > 0) {
				//System.out.println("Pair #: " + count + " " + pr.jacc + " cocite: " + pr.cocite + " total 1: " + total1
				//	+ " total 2: " + total2);
			} else {
				//System.out.println("Pair #: " + count + " jacc is 0");
			}
		}
		return pairs;
	}

	/*
	 * Returns the pairs with a jaccard index above a certain number, with the input
	 * of the ArrayList of all Pairs, and the output of an ArrayList of the pairs with
	 * above that jaccard index.
	 */
	public static ArrayList<Pair> filterPairs(ArrayList<Pair> allPairs) {
		ArrayList<Pair> importantPairs = new ArrayList<Pair>();
		int count = 0;
		int allCount = 0;
		double jaccIndex = 0.25;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("ImportantPairs.txt"));

			for (Pair pr: allPairs) {
				if (pr.jacc > jaccIndex) {
					importantPairs.add(pr);
					count++;
					bw.write("Pair #: " + count + "\t");
					bw.write("Jaccard: " + pr.jacc + "\t");
					System.out.println("Pair #: " + count);
					bw.write("Title 1: " + pr.one.title + "\t");
					System.out.println("Title 1: " + pr.one.title);
					bw.write("Title 2: " + pr.two.title + "\t");
					System.out.println("Title 2: " + pr.two.title);
					bw.write("Authors: " + pr.one.authors + "\t");
					bw.write("Authors: " + pr.two.authors + "\t");
					bw.newLine();
					System.out.println("Authors: " + pr.one.authors);
					System.out.println("Authors: " + pr.two.authors);

//					for (String common : pr.comParents) {
//						System.out.println("Parent in common: " + common);
//					}
					for (int common : pr.comParents) {
						System.out.println("Parent in common: " + common);
					}
				}
				allCount++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Important Pairs: " + count + " All Pairs: " + allCount);
		return importantPairs;
	}

	
	/*
	 * This method no longer works due to the change in the way we get our data
	 * Must be edited
	 * 
	 * The original intent was to go through the important pairs, and using their parent
	 * article numbers get the information from the original parent articles and add that
	 * to the references to use later to determine which parent Articles to get PDF's for.
	 * 
	 * This is a little bit confusing because we now use the Article class for both
	 * parent Articles and reference Articles but we used to use ParentArticles as a 
	 * different class. The file reading portion does not work anymore because it
	 * was a different way of gathering data. We will have to find a different way
	 * to get the parent articles from the pairs.
	 */
	public static ArrayList<Pair> findParents(ArrayList<Pair> jaccPairs) {
		String DOI;
		String url;
		String title;
		int num;

//		TreeSet<String>  parentNumbers = new TreeSet<String>();
		TreeSet<Integer>  parentNumbers = new TreeSet<Integer>();
		ArrayList<ParentArticles> parents = new ArrayList<ParentArticles>();
		for (Pair pr : jaccPairs) {
//			for (String parent : pr.comParents) {
//				parentNumbers.add(parent);
//			}
			for (int parent : pr.comParents) {
				parentNumbers.add(parent);
			}
		}

		FileReader fr;
		try {
			fr = new FileReader("citations_output.txt");

			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			String line2;

			while (br.ready()){
				if (line.startsWith("?PT")){
					line2 = br.readLine();
//					for (String number: parentNumbers) {
					for (int number: parentNumbers) {
						if (line2.startsWith("Article Number: " + number)) {
							DOI = line.substring(line.indexOf("DI:") + 4, line.indexOf("Vol:"));
							title = line.substring(line.indexOf("Name:") + 6, line.indexOf("Yr:"));
							url = line.substring(line.indexOf("http"), line.indexOf("Affiliation"));
//							num = Integer.parseInt(number);
							num = number;
							ParentArticles p = new ParentArticles(num, DOI, title, url);

							parents.add(p);
						}
					}
				}
				line = br.readLine();
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		Collections.sort(parents);

		int counter = 1;
		for (Pair pr: jaccPairs) {
//			for (String number: pr.comParents) {
			for (int number: pr.comParents) {
//				num = Integer.parseInt(number);
				num = number;
				int index = 0;
				while (index < parents.size() && index >= 0) {
					if (parents.get(index).parNum == num) {
						pr.finalParents.add(parents.get(index));
						//add jaccard references to the parents
						parents.get(index).references.add(pr.one);
						parents.get(index).references.add(pr.two);
						counter++;
						break;
					} else {
						index++;
					}
				}
			}

		}

		return jaccPairs;
	}

	/* Checks which cocitations are in fact true and meet all the requirements by checking author and crosschecking with year*/
	public static ArrayList<Pair> checkAuthors(ArrayList<CC> ccList, ArrayList<Pair> finalPairs){		
		int count = 0;
		for (CC cc : ccList){
			//example: {Hopwood=1972, Roozemond=1969}

			boolean oneContains = false;
			boolean twoContains = false;
			for (Pair p: finalPairs) {
				for (Map.Entry<String, Integer> entry : cc.names.entrySet()) {
					// example entries
					//Hopwood=1972
					//Roozemond=1969

					String realName = "";
					if (entry.getKey().contains(" ")) {
						realName = entry.getKey().substring(0, entry.getKey().indexOf(" ")).toLowerCase();
					} else {
						realName = entry.getKey().toLowerCase();
					}


					int realYear = entry.getValue();
					String store1 = "";
					String store2 = "";

					if (p.one.authors.contains(realName) && p.one.year == realYear) {
						oneContains = true;
						store1 = realName;
						System.out.println("TRUE ONE: " + realName + " " + p.one.title);
					} else if (p.two.authors.contains(realName) && p.two.year == realYear){
						store2 = realName;
						twoContains = true;
						System.out.println("TRUE TWO: " + realName + " " + p.two.title);
					}
					if (oneContains && twoContains) {
						p.inline = true;
						System.out.println("ONE AND TWO ARE TRUE: " + store1 + " " + store2 + " " + p.one.authors + p.two.authors);

					}

				}
				oneContains = false;
				twoContains = false;
			}

		}

		ArrayList<Pair> inline = new ArrayList<Pair>();
		for (Pair p: finalPairs) {
			if (!p.inline){
				//				System.out.println(p.one.title);
				//				System.out.println(p.two.title);
				//				System.out.println();
			} else {
				count++;
				inline.add(p);
				System.out.println("TRUE! " + count);
				System.out.println(p.one.title + " TRUE!");
				System.out.println(p.two.title + " TRUE!");
			}
		}
		return inline;
	}

	/*ESSENTIALLY RUNS ENTIRE PROGRAM*/
	public static void main(String[] args) {
		
		String fileName = args[0];
		String fileOutName = args[1];
		ArrayList<Pair> test = pair(findDuplicates(FilterXML.findRefs(FilterXML.filter(fileName, fileOutName))));
		
		//findParents needs to be edited first
		ArrayList<Pair> finalPairs = findParents(filterPairs(jaccard(test)));
		
		TreeSet<ParentArticles> parents = new TreeSet<ParentArticles>();
		
		//This part is for PDF text mining, might not be implementable yet
		ArrayList<CC> ccList = TextMining.isolate(TextMining.createDictionaries());
		ArrayList<Pair> inline = checkAuthors(ccList, finalPairs);

		try {
			FileWriter fw = new FileWriter("doi.txt");
			BufferedWriter bw = new BufferedWriter(fw);

			int counter = 1;
			for (Pair p : finalPairs) {
				for (ParentArticles pars: p.finalParents) {
					parents.add(pars);
					counter++;
				}
			}

			int count = 0;
			for (ParentArticles pa: parents) {
				count++;
				bw.write("N:" + pa.parNum + "\t" + "D:" + pa.DOI + "\t" + 
						"URL:" + pa.url + "\t" + "T:" + pa.title);
				bw.newLine();
			}
			System.out.println("# PARENTS: " + count);
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] empty = new String[0]; 
		FormatDOI.main(empty);
		System.out.println("# of inline pairs: " + inline.size());
	}
}

