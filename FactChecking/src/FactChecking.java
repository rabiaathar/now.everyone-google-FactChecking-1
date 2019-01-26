import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FactChecking {
	public static String FACTURI(String id)
	{
		//<Fact-URI>
		StringBuilder factid = new StringBuilder();
		factid.append("<http://swc2017.aksw.org/task2/dataset/").append(id).append("> ");
		return factid.toString();
	}
	public static String PROPURI(double value)
	{
		//<prop-URI>
		StringBuilder factvalue = new StringBuilder();
		factvalue.append("<http://swc2017.aksw.org/hasTruthValue> \"").append(value);
		return factvalue.toString();
	}
	public static String writeToFile(String id, double value) throws IOException {
		//<Fact-URI> <prop-URI> "value"^^type .
		StringBuilder resultLine = new StringBuilder();
		String valueType="\"^^<http://www.w3.org/2001/XMLSchema#double> .\n";
		resultLine.append(FACTURI(id)).append(PROPURI(value)).append(valueType);
		return resultLine.toString();
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("test.tsv"));
		File file = new File("result.ttl");
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br1 = new BufferedWriter(fr);
		ArrayList<String> deathList = new ArrayList<>();
		ArrayList<String> birthList = new ArrayList<>();
		ArrayList<String> starsList = new ArrayList<>();
		ArrayList<String> nobelList = new ArrayList<>();
		ArrayList<String> teamList = new ArrayList<>();
		ArrayList<String> authorList = new ArrayList<>();
		ArrayList<String> foundationList = new ArrayList<>();
		ArrayList<String> spouseList = new ArrayList<>();
		ArrayList<String> subordinateList = new ArrayList<>();
		ArrayList<String> roleList = new ArrayList<>();
		ArrayList<String> remainingList = new ArrayList<>();
		
		String FID;
		double factValue = 0;
		String line;
		System.out.println("Processing Start... \nWait till all the results are computed and stored in file successfully!");
		while ((line = br.readLine()) != null) {
			if (line.contains("death") ||line.contains("last place")) {
				deathList.add(line);
			} else if (line.contains("birth") || line.contains("nascence place")) {
				birthList.add(line);
			} else if (line.contains("stars")) {
				starsList.add(line);
			} else if (line.contains("role") || line.contains("office")) {
				roleList.add(line);
			} else if (line.contains("Nobel") || line.contains("award") || line.contains("honour")) {
				nobelList.add(line);
			} else if (line.contains("team") || line.contains("squad")) {
				teamList.add(line);
			} else if (line.contains("author") || line.contains("generator")) {
				authorList.add(line);
			} else if (line.contains("foundation") || line.contains("innovation")) {
				foundationList.add(line);
			} else if (line.contains("spouse") || line.contains("better half")) {
				spouseList.add(line);
			} else if (line.contains("subordinate") || line.contains("subsidiary")) {
				subordinateList.add(line);
			}
			else {
				remainingList.add(line);
			}
		}
		
		br.close();
		int exception = 0;

		////Team Sublist
		for (int i = 0; i < teamList.size(); i++) {
			String fact = teamList.get(i);
			FID = fact.substring(0, fact.indexOf("\t"));				
			factValue=0.5;
			try {
				fact = fact.substring(fact.indexOf("\t") + 1);
				String name="";
				String team="";
				if (fact.contains("team")) {
					fact = fact.replace("team", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ")
							.replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					team = fact.substring(fact.indexOf("-") + 2,fact.length());
				} 
				if (fact.contains("squad")) {
					fact = fact.replace("squad", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ")
							.replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					team = fact.substring(fact.indexOf("-") + 2,fact.length());
				} 
				System.out.println(fact + "\t");

				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.text();
				if (nameWiki.contains(team)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println(e);
			}
				br1.write(writeToFile(FID, factValue));

		}
		
		
		// Birth-Born Sublist
		for (int i = 0; i < birthList.size(); i++) {
			String fact = birthList.get(i);
			FID = fact.substring(0, fact.indexOf("\t"));
			factValue = 0.5;
			try {
				fact = fact.substring(fact.indexOf("\t") + 1);

			String name;
			String place;

			if (fact.contains("birth place")) {
				fact = fact.replace("birth place", "").replace("'s", " ").replace("' ", " ").replace(" is ", " - ")
						.replace(".", "");
				name = fact.substring(0, fact.indexOf("-") - 1);
				place = fact.substring(fact.indexOf("-") + 2, fact.length());
			} else {
				fact = fact.replace("nascence place", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ")
						.replace(".", "");

				place = fact.substring(0, fact.indexOf("-") - 1);
				name = fact.substring(fact.indexOf("-") + 2, fact.length());
			}
			System.out.println(fact);
			String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

			//boolean isTrue = fact.contains("1.0");
			Document doc = Jsoup.connect(urlName).get();

			String diedWiki = doc.select("table.infobox").select("tr:contains(Born)").text();
			if (diedWiki.contains(place)) {
					factValue = 1.0;
			} else {
				factValue = 0.0;
			}}
			catch (Exception e) {}
			br1.write(writeToFile(FID, factValue));
			
		}
		//Death Sublist
		for (int i = 0; i < deathList.size(); i++) {
			String fact = deathList.get(i);
			FID = fact.substring(0, fact.indexOf("\t"));
			factValue = 0.5;
			try {
			fact = fact.substring(fact.indexOf("\t") + 1);

			fact = fact.replace("death place", "").replace("last place", "").replace("'s", "").replace("' ", " ")
					.replace("  ", " ").replace(" is ", " - ").replace(".", "");

			System.out.println(fact + "\t");

			String name = fact.substring(0, fact.indexOf("-") - 1);
			String place = fact.substring(fact.indexOf("-") + 2, fact.length());
			String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

			Document doc = Jsoup.connect(urlName).get();

			String diedWiki = doc.select("table.infobox").select("tr:contains(Died)").text();
			if (diedWiki.contains(place)) {
				factValue = 1.0;
			} else {
				factValue = 0.0;
			}
			}catch (Exception e){}
			br1.write(writeToFile(FID, factValue));
		}

		// Role Sublist
		for (int i = 0; i < roleList.size(); i++) {
				String fact = roleList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue = 0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				String country;
				String name;
				if (fact.contains("role")) {
					fact = fact.replace("'s", "").replace("'", "").replace("role", "").replace(" is ", " - ")
							.replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					country = fact.substring(0, fact.indexOf("-") - 1);
					name = fact.substring(fact.indexOf("-") + 2, fact.length());
				} else {
					fact = fact.replace("'s", "").replace("'", "").replace("office", "").replace(" is ", " - ")
							.replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					country = fact.substring(fact.indexOf("-") + 2, fact.length());
				}
				System.out.println(fact + "\t");
				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.select("table.infobox").text();
				if (nameWiki.contains(country)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println("EXCEPTION\t");
			}
				br1.write(writeToFile(FID, factValue));
		}

		// Stars-Actors Sublist
		for (int i = 0; i < starsList.size(); i++) {
				String fact = starsList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue = 0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				fact = fact.replace("(film)", "").replace("(actor)", "").replace("stars", " - ").replace(".",
						"");
				while (fact.contains("  ")) {
					fact = fact.replace("  ", " ");
				}
				System.out.println(fact + "\t");

				String film = fact.substring(0, fact.indexOf("-") - 1);
				String actor = fact.substring(fact.indexOf("-") + 2,fact.length());
				String urlActor = "https://en.wikipedia.org/w/index.php?search=" + actor;

				Document doc = Jsoup.connect(urlActor).get();

				String actorWiki = doc.text();
				if (actorWiki.contains(film)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
				}catch (Exception e) {}
				br1.write(writeToFile(FID, factValue));
		}
		//NoblePrize-Award-Honers Sublist
		for (int i = 0; i < nobelList.size(); i++) {
				String fact = nobelList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue=0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				String name;
				String award;

				if (fact.contains("award")) {
					fact = fact.replace("award", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ")
							.replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					award = fact.substring(fact.indexOf("-") + 2,fact.length());
				} else {
					fact = fact.replace("honour", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ")
							.replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					award = fact.substring(0, fact.indexOf("-") - 1);
					name = fact.substring(fact.indexOf("-") + 2, fact.length());
				}
				System.out.println(fact + "\t");

				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.text();
				if (nameWiki.contains(award)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println(e);
			}
				br1.write(writeToFile(FID, factValue));
		}

		// Author Sublist
		for (int i = 0; i < authorList.size(); i++) {
				String fact = authorList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue = 0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				String name;
				String book;

				if (fact.contains("author")) {
					fact = fact.replace("author", "").replace("'s", "").replace("' ", " ").replaceAll("\\(.*?\\)", "")
							.replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					book = fact.substring(0, fact.indexOf("-") - 1);
					name = fact.substring(fact.indexOf("-") + 2, fact.length());
				} else {
					fact = fact.replace("generator", "").replace("'s", "").replace("' ", " ").replace("(.*)", "")
							.replaceAll("\\(.*?\\)", "").replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					book = fact.substring(fact.indexOf("-") + 2, fact.length());
				}
				System.out.println(fact);
				
				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.text();
				if (nameWiki.contains(book)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println(nobelList.get(i));
				System.out.println(e);
			}
				br1.write(writeToFile(FID, factValue));
		}
		// Spouse-BetterHalf Sublist
		for (int i = 0; i < spouseList.size(); i++) {
				String fact = spouseList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue = 0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				String name;
				String spouse;

				if (fact.contains("spouse")) {
					fact = fact.replace("spouse", "").replace("'s", "").replace("' ", " ").replaceAll("\\(.*?\\)", "")
							.replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					spouse = fact.substring(fact.indexOf("-") + 2, fact.length());
				} else {
					fact = fact.replace("better half", "").replace("'s", "").replace("' ", " ").replace("(.*)", "")
							.replaceAll("\\(.*?\\)", "").replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					spouse = fact.substring(0, fact.indexOf("-") - 1);
					name = fact.substring(fact.indexOf("-") + 2,fact.length());
				}
				System.out.println(fact + "\t");

				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.text();
				if (nameWiki.contains(spouse)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println(nobelList.get(i));
				System.out.println(e);
			}
				br1.write(writeToFile(FID, factValue));
		}
		// Subordinate
		for (int i = 0; i < subordinateList.size(); i++) {
				String fact = subordinateList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue=0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				String company1;
				String company2;

				if (fact.contains("subordinate")) {
					fact = fact.replace("subordinate", "").replace("'s", "").replace("' ", " ")
							.replaceAll("\\(.*?\\)", "").replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					company2 = fact.substring(0, fact.indexOf("-") - 1);
					company1 = fact.substring(fact.indexOf("-") + 2, fact.length());
				} else {
					fact = fact.replace("subsidiary", "").replace("'s", "").replace("' ", " ").replace("(.*)", "")
							.replaceAll("\\(.*?\\)", "").replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					company1 = fact.substring(0, fact.indexOf("-") - 1);
					company2 = fact.substring(fact.indexOf("-") + 2, fact.length());
				}
				System.out.println(fact + "\t");

				String urlCompany2 = "https://en.wikipedia.org/w/index.php?search=" + company2;

				Document doc = Jsoup.connect(urlCompany2).get();

				String company2Wiki = doc.text();
				if (company2Wiki.contains(company1)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println(nobelList.get(i));
				System.out.println(e);
			}
				br1.write(writeToFile(FID, factValue));
		}
		// Foundation
		for (int i = 0; i < foundationList.size(); i++) {
				String fact = foundationList.get(i);
				FID = fact.substring(0, fact.indexOf("\t"));
				factValue=0.5;
				try {
				fact = fact.substring(fact.indexOf("\t") + 1);

				String company;
				String place;

				if (fact.contains("foundation")) {
					fact = fact.replace("foundation place", "").replace("'s", "").replace("' ", " ")
							.replaceAll("\\(.*?\\)", "").replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					 company = fact.substring(0, fact.indexOf("-") - 1);
					place = fact.substring(fact.indexOf("-") + 2,fact.length());
				} else {
					fact = fact.replace("innovation place", "").replace("'s", "").replace("' ", " ").replace("(.*)", "")
							.replaceAll("\\(.*?\\)", "").replace(" is ", " - ").replace(".", "");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					place = fact.substring(0, fact.indexOf("-") - 1);
					company = fact.substring(fact.indexOf("-") + 2, fact.length());
				}
				System.out.println(fact + "\t");

				String urlCompany = "https://en.wikipedia.org/w/index.php?search=" + company;

				Document doc = Jsoup.connect(urlCompany).get();

				String company2Wiki = doc.text();
				if (company2Wiki.contains(place)) {
					factValue = 1.0;
				} else {
					factValue = 0.0;
				}
			} catch (Exception e) {
				exception++;
				System.out.println(nobelList.get(i));
				System.out.println(e);
			}
				br1.write(writeToFile(FID, factValue));
		}
		
		//Remaining Facts
		for (int i = 0; i < remainingList.size(); i++) {
			String fact = remainingList.get(i);
			FID = fact.substring(0, fact.indexOf("\t"));
			System.out.println("left: "+fact);
			br1.write(writeToFile(FID, 0.5));
			
		}
		System.out.println("Result Computed");
		System.out.println("Result are stored in \"result.ttl\" file, kindly refer to the project folder");
		br1.flush();
		br1.close();
	}

}
