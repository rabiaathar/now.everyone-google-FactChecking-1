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
  2a. Some preprocessing is done that removes periods, apostrophes and the predicate which is the same for each element in a list.
  2b. A subject and an object are extracted from the sentence. For example for a fact about a birth place, the name of the person is the subject, the place is the object.
  2c. We try to verify the facts using wikipedia as a knowledge base. 
    2c1. Therefore we try to get the content of the webpage about the subject in wikipedia using wikipedias search engine and the subject name.
    2c2. Depending on the list we either look if the object can be found in certain parts of the webpage about the subject (e.g. in the info box) or somewhere in the whole site.
    2c3. If we find the object we assume the fact is true, if not we assume it's wrong. (... of course this assumption doesn't hold for complex sentences and relations, but it met the given benchmark)
    2c4. If any problem occured during this procedure (e.g. when the webpage can't be found) we assume the fact is wrong.
    
# Facts for which our program failes:
Positive:
1.
2.
3.
4.
5. 

Negative:
1.
2.
3.
4.
5.
    
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

