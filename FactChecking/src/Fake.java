import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Fake {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:/Users/jbuel/Desktop/train.tsv"));
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
		}
		br.close();

		int correctPositive = 0;
		int correctNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		int exception = 0;

		// birth
		for (int i = 0; i < birthList.size(); i++) {
			String fact = birthList.get(i);
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
					correctPositive++;
				} else {
					falsePositive++;
				}
			} else {
				if (isTrue) {
					falseNegative++;
				} else {
					correctNegative++;
				}
			}
		}

		for (int i = 0; i < deathList.size(); i++) {
			String fact = deathList.get(i);
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
				if (isTrue) {
					correctPositive++;
				} else {
					falsePositive++;
				}
			} else {
				if (isTrue) {
					falseNegative++;
				} else {
					correctNegative++;
				}
			}
		}

		// role
		for (int i = 0; i < roleList.size(); i++) {
			try {
				String fact = roleList.get(i);
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
				System.out.print(fact + "\t");
				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				boolean isTrue = fact.contains("1.0");
				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.select("table.infobox").text();
				if (nameWiki.contains(country)) {
					System.out.println("1.0");
					if (isTrue) {
						correctPositive++;
					} else {
						falsePositive++;
					}
				} else {
					System.out.println("0.0");
					if (isTrue) {
						falseNegative++;
					} else {
						correctNegative++;
					}
				}
			} catch (Exception e) {
				exception++;
				System.out.println("EXCEPTION\t");
			}
		}

		// stars
		for (int i = 0; i < starsList.size(); i++) {
			try {
				String fact = starsList.get(i);
				fact = fact.substring(fact.indexOf("\t") + 1);

				fact = fact.replace("(film)", "").replace("(actor)", "").replace("stars", " - ").replace(".\t",
						"\t\t\t");
				while (fact.contains("  ")) {
					fact = fact.replace("  ", " ");
				}
				System.out.print(fact + "\t");

				String film = fact.substring(0, fact.indexOf("-") - 1);
				String actor = fact.substring(fact.indexOf("-") + 2, fact.indexOf("\t"));
				String urlActor = "https://en.wikipedia.org/w/index.php?search=" + actor;

				boolean isTrue = fact.contains("1.0");
				Document doc = Jsoup.connect(urlActor).get();

				String actorWiki = doc.text();
				if (actorWiki.contains(film)) {
					System.out.println("1.0");
					if (isTrue) {
						correctPositive++;
					} else {
						falsePositive++;
					}
				} else {
					System.out.println("0.0");
					if (isTrue) {
						falseNegative++;
					} else {
						correctNegative++;
					}
				}
			} catch (Exception e) {
				exception++;
			}
		}

		for (int i = 0; i < nobelList.size(); i++) {
			try {
				String fact = nobelList.get(i);
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
				System.out.print(fact + "\t");

				String urlName = "https://en.wikipedia.org/w/index.php?search=" + name;

				boolean isTrue = fact.contains("1.0");
				Document doc = Jsoup.connect(urlName).get();

				String nameWiki = doc.text();
				if (nameWiki.contains(award)) {
					System.out.println("1.0");
					if (isTrue) {
						correctPositive++;
					} else {
						falsePositive++;
					}
				} else {
					System.out.println("0.0");
					if (isTrue) {
						falseNegative++;
					} else {
						correctNegative++;
					}
				}
			} catch (Exception e) {
				exception++;
				System.out.println(nobelList.get(i));
				System.out.println(e);
			}
		}

		System.out.println(
				correctPositive + "/" + falsePositive + "/" + falseNegative + "/" + correctNegative + "/" + exception);
	}

}
