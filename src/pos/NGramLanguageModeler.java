package pos;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class uses a contextModel and DocumentDictionary to create a paragraph.
 * main.java runs it with a paragraph length of 20.
 * @author Brooke
 *
 */
public class NGramLanguageModeler {
	
	ContextModel cm;
	DocumentDictionary dd;
	Queue<String> context;
	
	NGramLanguageModeler(ContextModel cm, DocumentDictionary dd){
		this.dd = dd;
		this.cm = cm;
		context = new LinkedList<String>();
	}
	
	public void run(int numOfWords){
		//the dictionary is our model.
		context = cm.getSeedWords();
		
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < numOfWords; i++){
			String nextKey = cm.getNextKey(context.remove());
			s.append(dd.getNextWord(nextKey));
			s.append(" ");
			context.add(nextKey);
		}
		
		System.out.println(s.toString());
	}
/*
 * import random
 
infilename = "vm-shared/textprep/2009-Obama.txt"
trainingdata = open(infilename).read()
 
contextconst = [""]
 
context = contextconst
model = {}
 
for word in trainingdata.split():
    #print (word)
    model[str(context)] = model.setdefault(str(context),[])+ [word]
    context = (context+[word])[1:]
 
#print(model)
 
context = contextconst
for i in range(100):
    word = random.choice(model[str(context)])
    print(word,end=" ")
    context = (context+[word])[1:]
 
print()
 */

}
