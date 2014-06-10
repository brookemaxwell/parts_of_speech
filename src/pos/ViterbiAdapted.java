package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ViterbiAdapted {

	private Object[] states; //= {"Healthy", "Fever"};//parts of speech, 
	//private Object[] observations;// = {"normal", "cold", "fever"};//words we encounter (read in from file)
			 
	//private HashMap<Object, Double> start_probability; //this is the number of times the pos occur at all.
	
	private ContextModel transition_model; //this is our contextModel (transistions)
	private DocumentDictionary  emission_model; //this is our documentDictionary for the contextModel

	public ViterbiAdapted(ContextModel owcm, DocumentDictionary dd){
		
		transition_model = owcm;
		emission_model = dd;
		states = owcm.getStates();
		
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
			// V[0][y] = start_p[y] * emit_p[y][obs[0]]
			Object key = states[y];
			double value = transition_model.getStateProbability(key)/*start_probability.get(key)*/ * emission_model.getProbability(key, obs[0]);//get(key).get(obs[0]);
			V.get(0).put(key, value);
			//path[y] = [y]
			ArrayList<Object> temp =  new ArrayList<>();
			temp.add(key);
			path.put(key, temp);
			
	    }
	 
	    // Run Viterbi for t > 0
	    //for t in range(1, len(obs)):
		for(int t = 1; t< obs.length; t++ ){
	        //V.append({})
			HashMap<Object, Double> tempV = new HashMap<Object, Double>();
			HashMap<Object, ArrayList<Object>> newpath = new HashMap<>();

			//for y in states:
			for(int y =0; y<states.length; y++ ){
				Object key = states[y];
				Object bestState= "";
				double bestProb = Double.NEGATIVE_INFINITY;
				//(prob, state) = max((V[t-1][y0] * trans_p[y0][y] * emit_p[y][obs[t]], y0) for y0 in states)
				//this loop handles the max
				for(int y0 =0; y0<states.length; y0++ ){
					Object primeKey = states[y0];
					double prob = V.get(t-1).get(primeKey) * transition_model.getTransProbability(primeKey, key) * emission_model.getProbability(key, obs[t]);//get(key).get(obs[t]);	
					if(bestProb < prob){
						bestProb = prob;
						bestState = primeKey;
					}
				}
				// V[t][y] = prob
				V.get(t).put(key, bestProb);
			    //newpath[y] = path[state] + [y]		//I am guessing on what this is supposed to be doing
				ArrayList<Object> tempArrayList = listAdd(path.get(bestState), key);
				newpath.put(key, tempArrayList);
			}
	            			 
	        //Don't need to remember the old paths
	        path = newpath;
		}
		
	    int n = 0;//           # if only one element is observed max is sought in the initialization values
	    //if len(obs)!=1:
	    if(obs.length !=1){
	    	// n = t
	        n = obs.length -1;
	    }
	    //print_dptable(V)
	    //(prob, state) = max((V[n][y], y) for y in states)
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
	    //return (prob, path[state])
	
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
