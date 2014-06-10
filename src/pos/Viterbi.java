package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Viterbi {

	private Object[] states; //parts of speech,
	private ContextModel transition_model; //this is our contextModel (transitions)
	private DocumentDictionary  emission_model; //this is our documentDictionary for the contextModel

	public Viterbi(File f){
		transition_model = new OneWordContextModel(f);
		emission_model = new DocumentDictionary(f);
		states = transition_model.getStates();
	}
	public Viterbi(ContextModel owcm, DocumentDictionary dd){
		transition_model = owcm;
		emission_model = dd;
		states = owcm.getStates();
		
	}
		
	public void printData(Object[] obs){
		StringBuilder sb = new StringBuilder();
		sb.append(transition_model.toString() + "\n");
		sb.append(emission_model.toString()+"\n");
		
		sb.append("DATA\n");
		for(Object ob : obs)
			sb.append(ob.toString() + "\t");
		
		System.out.println(sb.toString());
	}
	public void run( Object[] obs){
		ArrayList<HashMap<Object, Double>> V = new ArrayList<>();//[{}];
		HashMap<Object, ArrayList<Object>> path = new HashMap<>();
		
		for(int t = 0; t< obs.length; t++){
			V.add(new HashMap<Object, Double>());
		}
	 
	    // Initialize base cases (t == 0)
	    //for y in states:
		for(int y =0; y<states.length; y++ ){
			
			Object key = states[y];
			double value = transition_model.getStateProbability(key) * emission_model.getProbability(key, obs[0]);//get(key).get(obs[0]);
			V.get(0).put(key, value);
			
			ArrayList<Object> temp =  new ArrayList<>();
			temp.add(key);
			path.put(key, temp);
			
	    }
	 
	    // Run Viterbi for t > 0
		for(int t = 1; t< obs.length; t++ ){
			
			HashMap<Object, Double> tempV = new HashMap<Object, Double>();
			HashMap<Object, ArrayList<Object>> newpath = new HashMap<>();

			//for y in states:
			for(int y =0; y<states.length; y++ ){
				Object key = states[y];
				Object bestState= "";
				double bestProb = Double.NEGATIVE_INFINITY;

				//this loop handles the max
				for(int y0 =0; y0<states.length; y0++ ){
					Object primeKey = states[y0];
					double prob = V.get(t-1).get(primeKey) * transition_model.getTransProbability(primeKey, key) * emission_model.getProbability(key, obs[t]);//get(key).get(obs[t]);	
					if(bestProb < prob){
						bestProb = prob;
						bestState = primeKey;
					}
				}
				
				V.get(t).put(key, bestProb);

				ArrayList<Object> tempArrayList = listAdd(path.get(bestState), key);
				newpath.put(key, tempArrayList);
			}
	            			 
	        //Don't need to remember the old paths
	        path = newpath;
		}
		
	    int n = 0;//           # if only one element is observed max is sought in the initialization values
	    
	    if(obs.length !=1){
	        n = obs.length -1;
	    }

	    double bestProb = Double.NEGATIVE_INFINITY;
	    Object bestState = "";
	    for(int y =0; y< states.length; y++){
	    	double curProb = V.get(n).get(states[y]);
	    	if(bestProb < curProb){
	    		bestProb = curProb;
	    		bestState =  states[y];
	    	}
	    	
	    }
	    System.out.println("Best guess:  "+ path.get(bestState));
	}


	private ArrayList<Object> listAdd(ArrayList<Object> arrayList, Object key) {
		ArrayList<Object> list = new ArrayList<Object>();
		for(Object cur: arrayList){
			list.add(cur);
		}
		list.add(key);
		return list;
	}
	
}
