# now.everyone-google-FactChecking
# Team name:
The Green Corpus

# Group Members:
1. Jonas Bülling
2. Rabia Athar
3. Muhammad Faizan Batra


# Approach Description
1. We order the given facts into serveral lists depending on the topic of the fact using keywords. For example we put facts containing the words "nascence" or "birth" in one list and all facts containing the keywords "death" and "last place" into another.
2. Now we try to tackle each problem on its own and implemented therefore different procedures for each subproblem. The basic structure of all of these procedures can be described as follows:
    - Some preprocessing is done that removes periods, apostrophes and the predicate which is the same for each element in a list.
    - A subject and an object are extracted from the sentence. For example for a fact about a birth place, the name of the person is the subject, the place is the object.
    - We try to verify the facts using wikipedia as a knowledge base. 
        - Therefore we try to get the content of the webpage about the subject in wikipedia using wikipedias search engine and the subject name.
        - Depending on the list we either look if the object can be found in certain parts of the webpage about the subject (e.g. in the info box) or somewhere in the whole site.
        - If we find the object we assume the fact is true, if not we assume it's wrong. (... of course this assumption doesn't hold for complex sentences and relations, but it usually meets the given benchmark)
    - If any problem occured during this procedure (e.g. when the webpage can't be found) we assume the fact is wrong.
    
# Facts for which our program failes:
False Positive:
1. 'Leonardo DiCaprio's birth place is L.A.' -> The system can't handle abbreviations.
2. 'Hamburg, Germany is Angela Merkel's nascence place.' -> Wikipedia has West Germany, instead of Germany
3. 'George Bush's death place is Houston, Texas.' -> The right George Bush isn't found.
4. 'Germany is in Europe.' -> The system can't handle facts that don't belong to the given topics.
5. 'Thomas Müller's team is Bayern Munich.' -> The system has problems with special letters like 'ü'.

False Negative:
6.	'Google is Google's subordinate.' -> Google can be found in the entry about Google. 
7. 	'Shakespeare's author is Shakespeare.' -> Likewise the author algorithm doesn't check if Shakespeare is a book at all.
8.	'Toni Kroos' team is UEFA.' -> The word UEFA can be found in the entry about Toni Kroos.
9.	'Toni Kroos' foundation place is Madrid.' -> The routine doesn't really check if the used predicate makes sense.
10.	'Real Madrid's author is Toni Kroos.' -> Similar problem.
    
# Description how project can be built and executed
1. Tool: Preferrably Eclipse(IDE) for JAVA is required. Also Java-jdk-version 1.8.0.
2. Result file can be found in main folder "FactChecking" with name result.ttl.
Note: the result file contains existing results. Before running delete the existing results from file.
3. Open eclipse.
4. Click File->Import.
5. From the Wizard select "General".
6. From Sub menu of General select "Existing Projects into Workspace".
7. Click Next
8. On the left of "Select root directory" click browse and select the unziped folder and navigate to "FactChecking" folder.
9. Click on "Finish".
10. If the import is successfully done, it can be executed by clicking the green play button.
11. If project to be tested using new data set then refer line #  35 and replace the file name along with extension.
12. The Project will start running.
13. IMPORATNT! Wait till a message displays as below:
  "Result Computed" 
 
  "Result are stored in result.ttl file, kindly refer to the project folder."
  
14. The result file wil have new results.

