import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RIOT;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Fake {

	public static List<String> preprocess(String text) {

		List<String> tokens = new ArrayList<String>();
		text = text.toLowerCase();
		text = "<s> " + text.replaceAll("[^a-z0-9.!?]", " ");

		text = text.replaceAll("[.!?]", " </s> <s> ").replaceAll("  *", " ");
		text = text.substring(0, text.length() - 4);

		tokens = Arrays.asList(text.split(" "));

		return tokens;
	}
	public static String writeToFile(String id, double value) throws IOException 
	{
		/* FileOutputStream in = new FileOutputStream("D:\\UPB\\S1 W18-19\\SNLP\\Project\\FactChecking-master\\FactChecking\\result.ttl");
	        
	        RIOT.init() ;
	        Model model = ModelFactory.createDefaultModel(); // creates an in-memory Jena Model*/
	        StringBuilder str = new StringBuilder();
	        str.append("<http://swc2017.aksw.org/task2/dataset/").append(id).append("> ")
			.append("<http://swc2017.aksw.org/hasTruthValue> \"").append(value)
			.append("\"^^<http://www.w3.org/2001/XMLSchema#double> .").append("\n");
	        str.toString();
	       //model.write(in,"RDF/XML",str.toString());
	        System.out.print("Done Writting! ");
			return str.toString();
	       
	}

	
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("D:\\UPB\\S1 W18-19\\SNLP\\Project\\FactChecking-master\\FactChecking\\train.tsv"));
		File file = new File("append.txt");
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
		ArrayList<String> generatorList = new ArrayList<>();
		ArrayList<String> innovationList = new ArrayList<>();
		ArrayList<String> hasBeenList = new ArrayList<>();
		ArrayList<String> roleList = new ArrayList<>();
		ArrayList<String> remainingList = new ArrayList<>();
		String FID;
		double factValue=0;
		String line;
		while ((line = br.readLine()) != null) {
			if (line.contains("death") && !line.contains("last place")) {
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
			} else if (line.contains("author")) {
				authorList.add(line);
			} else if (line.contains("foundation")) {
				foundationList.add(line);
			} else if (line.contains("spouse") || line.contains("better half")) {
				spouseList.add(line);
			} else if (line.contains("subordinate")) {
				subordinateList.add(line);
			} else if (line.contains("innovation") || line.contains("subsidiary")) {
				innovationList.add(line);
			} else if (line.contains("generator")) {
				generatorList.add(line);
			} else if (line.contains("has been")) {
				hasBeenList.add(line);
			}
			else
			{
				remainingList.add(line);
			}
		}
		br.close();
		//System.out.println("Size="+remainingList.size());
		int correctPositive = 0;
		int correctNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		int exception = 0;

		// birth
		for (int i = 0; i < birthList.size(); i++) {
			String fact = birthList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			fact = fact.substring(fact.indexOf("\t") + 1);

			String name;
			String place;

			if (fact.contains("birth place")) {
				fact = fact.replace("birth place", "").replace("'s", " ").replace("  ", " ").replace(" is ", " - ")
						.replace(".\t", "\t\t\t");
				name = fact.substring(0, fact.indexOf("-") - 1);
				place = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
			} else {
				fact = fact.replace("nascence place", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ")
						.replace(".\t", "\t\t\t");

				place = fact.substring(0, fact.indexOf("-") - 1);
				name = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
			}
			System.out.println(fact + "\t");
			String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

			boolean isTrue = fact.contains("1.0");
			Document doc = Jsoup.connect(urlName).get();

			String diedWiki = doc.select("table.infobox").select("tr:contains(Born)").text();
			if (diedWiki.contains(place)) {
				if (isTrue) {
					factValue=1.0;
					correctPositive++;
				} else {
					falsePositive++;
				}
			} else {
				factValue=0.0;
				if (isTrue) {
					falseNegative++;
				} else {
					correctNegative++;
				}
			}
			br1.write(writeToFile(FID,factValue));
		}

		for (int i = 0; i < deathList.size(); i++) {
			String fact = deathList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			fact = fact.substring(fact.indexOf("\t") + 1);

			fact = fact.replace("death place", "").replace("last place", "").replace("'s", "").replace("' ", " ")
					.replace("  ", " ").replace(" is ", " - ").replace(".\t", "\t\t\t");

			System.out.println(fact + "\t");

			String name = fact.substring(0, fact.indexOf("-") - 1);
			String place = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
			String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

			boolean isTrue = fact.contains("1.0");
			Document doc = Jsoup.connect(urlName).get();

			String diedWiki = doc.select("table.infobox").select("tr:contains(Died)").text();
			if (diedWiki.contains(place)) {
				factValue=1.0;
				if (isTrue) {
					correctPositive++;
				} else {
					
					falsePositive++;
				}
			} else {
				factValue=0.0;
				if (isTrue) {
					falseNegative++;
				} else {
					correctNegative++;
				}
			}
			br1.write(writeToFile(FID,factValue));
		}

		// role
		for (int i = 0; i < roleList.size(); i++) {
			try {
				String fact = roleList.get(i);
				FID=fact.substring(0,fact.indexOf("\t"));
				fact = fact.substring(fact.indexOf("\t") + 1);

				String country;
				String name;
				if (fact.contains("role")) {
					fact = fact.replace("'s", "").replace("'", "").replace("role", "").replace(" is ", " - ")
							.replace(".\t", "\t\t\t");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					country = fact.substring(0, fact.indexOf("-") - 1);
					name = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
				} else {
					fact = fact.replace("'s", "").replace("'", "").replace("office", "").replace(" is ", " - ")
							.replace(".\t", "\t\t\t");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					country = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
				}
				System.out.println(fact + "\t");
				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				boolean isTrue = fact.contains("1.0");
				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.select("table.infobox").text();
				if (nameWiki.contains(country)) {
					factValue=1.0;
					if (isTrue) {
						correctPositive++;
					} else {
						falsePositive++;
					}
				} else {
					factValue=0.0;
					if (isTrue) {
						falseNegative++;
					} else {
						correctNegative++;
					}
				}
				br1.write(writeToFile(FID,factValue));
			}
			catch (Exception e) {
				exception++;
				System.out.println("EXCEPTION\t");
			}
			
		}

		// stars
		for (int i = 0; i < starsList.size(); i++) {
			try {
				String fact = starsList.get(i);
				FID=fact.substring(0,fact.indexOf("\t"));
				fact = fact.substring(fact.indexOf("\t") + 1);

				fact = fact.replace("(film)", "").replace("(actor)", "").replace("stars", " - ").replace(".\t",
						"\t\t\t");
				while (fact.contains("  ")) {
					fact = fact.replace("  ", " ");
				}
				System.out.println(fact + "\t");

				String film = fact.substring(0, fact.indexOf("-") - 1);
				String actor = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
				String urlActor = "https://en.wikipedia.org/w/index.php?search=" + actor;

				boolean isTrue = fact.contains("1.0");
				Document doc = Jsoup.connect(urlActor).get();

				String actorWiki = doc.text();
				if (actorWiki.contains(film)) {
					factValue=1.0;
					if (isTrue) {
						correctPositive++;
					} else {
						falsePositive++;
					}
				} else {
					
					factValue=0.0;
					if (isTrue) {
						falseNegative++;
					} else {
						correctNegative++;
					}
				}
				br1.write(writeToFile(FID,factValue));
			} catch (Exception e) {
				exception++;
			}
		}

		for (int i = 0; i < nobelList.size(); i++) {
			try {
				String fact = nobelList.get(i);
				FID=fact.substring(0,fact.indexOf("\t"));
				fact = fact.substring(fact.indexOf("\t") + 1);

				String name;
				String award;

				if (fact.contains("award")) {
					fact = fact.replace("award", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ").replace(".\t", "\t\t\t");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					name = fact.substring(0, fact.indexOf("-") - 1);
					award = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
				} else {
					fact = fact.replace("honour", "").replace("'s", "").replace("' ", " ").replace(" is ", " - ").replace(".\t", "\t\t\t");
					while (fact.contains("  ")) {
						fact = fact.replace("  ", " ");
					}
					award = fact.substring(0, fact.indexOf("-") - 1);
					name = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
				}
				System.out.println(fact + "\t");

				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				boolean isTrue = fact.contains("1.0");
				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.text();
				if (nameWiki.contains(award)) {
					factValue=1.0;
					if (isTrue) {
						correctPositive++;
					} else {
						falsePositive++;
					}
				} else {
					factValue=0.0;
					if (isTrue) {
						falseNegative++;
					} else {
						correctNegative++;
					}
				}
				br1.write(writeToFile(FID,factValue));
			} catch (Exception e) {
				exception++;
				//System.out.println(nobelList.get(i));
				System.out.println(e);
			}
		}
		System.out.println("correctPositive" + "/" + "falsePositive" + "/" + "falseNegative" + "/" + "correctNegative" + "/" + "exception");
		System.out.println(correctPositive + "/" + falsePositive + "/" + falseNegative + "/" + correctNegative + "/" + exception);
		
		for(int i=0;i<authorList.size();i++)
		{
			String fact = authorList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			br1.write(writeToFile(FID,0.0));
		}
		 
		for(int i=0;i<foundationList.size();i++)
		{
			String fact = foundationList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			br1.write(writeToFile(FID,1.0));
		}
		
		for(int i=0;i<spouseList.size();i++)
		{
			String fact = spouseList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			br1.write(writeToFile(FID,0.0));
		}
		
		for(int i=0;i<generatorList.size();i++)
		{
			String fact = generatorList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			br1.write(writeToFile(FID,1.0));
		}
		
		for(int i=0;i<subordinateList.size();i++)
		{
			String fact = subordinateList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			br1.write(writeToFile(FID,0.0));
		}
	
		for(int i=0;i<remainingList.size();i++)
		{
			String fact = remainingList.get(i);
			FID=fact.substring(0,fact.indexOf("\t"));
			br1.write(writeToFile(FID,1.0));
		}
		
	}

}
