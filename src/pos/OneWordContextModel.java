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
 * Right now it only works with 1-word pairings, but it should be able to do more than that.
 * 
 * To see its layout, run main.java.
 * @author Brooke
 *
 */
public class OneWordContextModel extends ContextModel{

	public OneWordContextModel(File file) {
		super(file);
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
		
		while(scanner.hasNext()){
			
			String[] split = scanner.next().split("_");
			//split[0] would be the word itself.
			//split[1] is the key.
			
			String word = split[1];
			String key = context.remove();
			context.add(word);
			
			
			if("".equals(word)){
				continue;
			}
			totalKeyOccurrences++;
			if(model.containsKey(key)){
				ArrayList<WordNode> wnList = model.get(key);
				keyOccurrences.put(key,  keyOccurrences.get(key) + 1); //increment the number of occurrences of the key
				boolean found = false;
				for(WordNode wn: wnList){
					if(wn.word.equals(word)){
						wn.count++;
						model.put(key, wnList);
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
				model.put(key, newList);
				keyOccurrences.put(key,  1);
			}
			
		}
		model.remove("");
	}	
	
	public Double getProbability(String key, String word){
		ArrayList<WordNode> words = model.get(key);
		for(WordNode wn : words){
			if(wn.word.equals(word)){
				return (double)wn.count / (double)keyOccurrences.get(key);
			}
		}
		return .00001;
	}
	
	@Override
	public String getMostProbableNextKey(Object seedWord){
		rand = new Random();
		ArrayList<WordNode> words = null;
		if(seedWord == null){
			String key = "";
			words = model.get(key);
		}else{
			words = model.get(seedWord);
		}
		/*get a word probabilitstically*/
		
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
				randomlyPickedWord = curNode.word.toString();
				break;
			}
		}
		return randomlyPickedWord;
	}
	
	public LinkedList<String> getSeedWords(){
		LinkedList<String> context = new LinkedList<String>();

		context.add("");
		return context;
	}

	public boolean contains(String word) {
		return false;
		//return wordCounts.containsKey(word);
	}

	@Override
	public HashSet<String> getWordTypes() {
		Set<Object> tempSet = model.keySet();
		HashSet<String> wordTypes = new HashSet<String>();
		for(Object obj: tempSet){
			String s = (String) obj;
			wordTypes.add(s);
		}
		return wordTypes;
	}
}
