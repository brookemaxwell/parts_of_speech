package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This is a class to sort words into parts of speech with their occurrences. It maps a part of speech to words.
 * To see its layout, run main.java.
 * @author Brooke
 *
 */
public class DocumentDictionary{

	/*An entry would be like NN->{{dog, 4}, {cat,5}}*/
	HashMap<Object, ArrayList<WordNode>> partsOfSpeech;
	Random rand; 
	HashMap<Object, Integer> posCounts;
	
	
	public DocumentDictionary(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured with: "+ file.getAbsolutePath());
			e.printStackTrace();
		}
		
		partsOfSpeech = new HashMap<Object, ArrayList<WordNode>>();
		posCounts = new HashMap<Object, Integer>();
		
		while(scanner.hasNext()){
			
			String[] split = scanner.next().split("_");
			String key = split[1];
			String word = split[0];
			
			if("".equals(key)){
				continue;
			}
			
			if(partsOfSpeech.containsKey(key)){
				ArrayList<WordNode> count = partsOfSpeech.get(key);
				posCounts.put(key,  posCounts.get(key)+1);
				boolean found = false;
				for(WordNode wn: count){
					if(wn.word.equals(word)){
						wn.count++;
						partsOfSpeech.put(key, count);
						found = true;
						break;
					}
				}
				if(!found)
					count.add(new WordNode(word, 1));
				
			}
			else{
				ArrayList<WordNode> newList = new ArrayList<WordNode>();
				newList.add(new WordNode(word, 1));
				posCounts.put(key,  1);
				partsOfSpeech.put(key, newList);
			}			
		}
		
	}	
	
	public String getRandomWord(String seedWord){
		return "randomword";
	}
	public String getNextWord(String seedWord){
		rand = new Random();
		ArrayList<WordNode> words = null;
		if(seedWord == null){
			return "";
		}else{
			words = partsOfSpeech.get(seedWord);
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
		sb.append("DocumentDictionary:");
		for(Object key: partsOfSpeech.keySet()){
			sb.append("\n\t"+ key + " (" + posCounts.get(key) + ")");
			List<WordNode> wnList = partsOfSpeech.get(key);
			for(WordNode wn : wnList){
				sb.append("\n\t\t"+ wn.word +"  "+ wn.count);
			}
			
		}
		return sb.toString();
	}


	public Double getProbability(Object key, Object value){
		double count = 0;
		for(WordNode wn : partsOfSpeech.get(key)){
			if(value.equals(wn.word))
				count = wn.count;
		}
		return count / (double)posCounts.get(key);
	}


	public boolean contains(String word) {
		return false;
		//return wordCounts.containsKey(word);
	}
}
