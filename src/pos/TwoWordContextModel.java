package pos;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
/**
 * This is a class to map parts of speech to their followers. 
 *
 */
public class TwoWordContextModel extends ContextModel{
	
	public TwoWordContextModel(File file) {
		super(file);
		HashSet<String> wordTypes = new HashSet<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured with: "+ file.getAbsolutePath());
			System.out.println("Program will exit");
			e.printStackTrace();
			System.exit(0);
		}
		
		LinkedList<String> context = new LinkedList<String>();
		context.add("");
		context.add("ThisIsProbablyBadCodingPraticeButThisIsn'tExactlyGoogle");	
		
		while(scanner.hasNext()){
			
			String[] split = scanner.next().split("_");
			//split[0] would be the word itself.
			//split[1] is the key.
			
			String word = split[1];
			String key1 = context.remove();
			String key2 = context.getFirst();
			context.add(word);
			
			if("".equals(word)){
				continue;
			}
			//this makes sure we read at least two words before processing
			if( key1.endsWith("ThisIsProbablyBadCodingPraticeButThisIsn'tExactlyGoogle") || 
				key2.equals("ThisIsProbablyBadCodingPraticeButThisIsn'tExactlyGoogle")){
				continue;
			}
			TwoWordKey twKey = new TwoWordKey(key1, key2);
			wordTypes.add(key1);
			wordTypes.add(key2);
			if(model.containsKey(twKey)){
				ArrayList<WordNode> wnList = model.get(twKey);
				keyOccurrences.put(twKey,  keyOccurrences.get(twKey) + 1); //increment the number of occurrences of the key
				boolean found = false;
				for(WordNode wn: wnList){
					if(wn.word.equals(word)){
						wn.count++;
						model.put(twKey, wnList);
						found = true;
						break;
					}
				}
				if(!found)
					wnList.add(new WordNode(word, 1));
				
			}
			else{
				ArrayList<WordNode> newList = new ArrayList<WordNode>();
				newList.add(new WordNode(word, 1));
				model.put(twKey, newList);
				keyOccurrences.put(twKey, 1); //increment the number of occurrences of the key
			}
			
		}
		
		
		
		/*Because we going to predict using two keys, we need to worry about situations where the randomness
		 * generates a word alignment not seen in the document. First, we add every possible key to the 
		 * two key set. Second, then we iterate over every key and, for every part of speech not seen after the 
		 * TwoWordKey, we add in a WordNode containing the part of speech with count 1*/
		//add the start conditions
		wordTypes.add("");
		
		for(String key1: wordTypes){
			for(String key2: wordTypes){
				TwoWordKey possibleWordCombo = new TwoWordKey(key1, key2);
				if(!model.containsKey(possibleWordCombo)){
					model.put(possibleWordCombo, new ArrayList<WordNode>());
				}
			}
		}
		
		//"" is  is not a possible word type 
		wordTypes.remove("");
		
		for(Object curCombo: model.keySet()){
			ArrayList<WordNode> nodes = model.get(curCombo);
			for(String wordType: wordTypes){
				//check to see if this combo doesn't have the word type
				if(!nodes.contains(wordType)){
					nodes.add(new WordNode(wordType,1));
				}
			}
		}
		
		
		
	}	
	
	@Override
	public String getMostProbableNextKey(Object object){
		rand = new Random();
		ArrayList<WordNode> words = null;
		
		TwoWordKey twKey = (TwoWordKey) object;
		//TwoWordKey twKey = new TwoWordKey(w1,w2);
		if(object == null){
			twKey = new TwoWordKey("","");
			words = model.get(twKey);
		}else{
			words = model.get(twKey);
		}
		/*get a word probabilitstically. We will image that the words array is an array where each word node has been expanded..
		 * For example, if we have WordNodes {"cat",4} {"dog",7}, then the first 4 items of the array would be cat and the next
		 * 7 items would be dog.*/
		
		//get a count of the total number of words associated with that part of speech
		int total =0;
		for(WordNode curNode: words){
			total+= curNode.count;
		}
		
		//generate a random index from 0 to the total number of words
		//the nextInt method is perfect for working with the array as 0 is inclusive and total is excluded
		int randomIndex = rand.nextInt((total));

		//get the word at that index
		String randomlyPickedWord ="";	
		for(WordNode curNode: words){
			randomIndex-= curNode.count;
			if(randomIndex <0){
				randomlyPickedWord = curNode.word;
				break;
			}
		}
		return randomlyPickedWord;
	}
	
	public LinkedList<String> getSeedWords(){
		LinkedList<String> context = new LinkedList<String>();
		context.add("");
		context.add("");
		return context;
	}
	
	/**This method does two things:
	 * 1)make the key lower case to avoid "RanDomWOrds" not being the same as "rANDOMWord"
	   2) trims off non-letter characters at the begining and ending of words (so that "healing." is the same as "healing")
	 * @param key
	 * @return
	 */
	public static String trim(String key) {
		String temp = key;
		
		//makes the key lower case
		key = key.toLowerCase();
		
		//trim off the none letter characters
		while(!key.equals("") && !Character.isLetter(key.charAt(0))){
			key = key.substring(1);
		}
		while(!key.equals("") && !Character.isLetter(key.charAt(key.length()-1))){
			key = key.substring(0, key.length()-1);
		}
		
		return key;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ContextModel:");
		for(Object key: model.keySet()){
			sb.append("\n\t"+ key + " ("+keyOccurrences.get(key) + ")");
			List<WordNode> wnList = model.get(key);
			for(WordNode wn : wnList){
				sb.append("\n\t\t"+ wn.word +"  "+ wn.count);
			}
			
		}
		return sb.toString();
	}




	public boolean contains(String word) {
		return false;
		//return wordCounts.containsKey(word);
	}
}

