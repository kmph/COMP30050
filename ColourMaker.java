package Colourama;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ColourMaker {
	private Map<String,List<String>>  readymadeInventory;
	private ColourMap cm;
	private HashMap<String,Double> unigram;
	private HashMap<String,Double> bracketedBigram;
	private HashMap<String,Double> unBracketedBigram;
	private HashMap<String, HashMap<String,Double>> pluralBigram;

	final String BBPath = "resource" + File.separator + "bracketed_bigrams.txt";
	final String UBBPath = "resource"+ File.separator + "unbracketed_bigrams.txt";
	final String PBPath = "resource" + File.separator + "plural_bigrams.txt";
	final String unigramPath = "resource" + File.separator + "unigrams.txt";

	public ColourMaker() {
		readymadeInventory = new HashMap<String,List<String>>();
		unigram = new HashMap<>();
		bracketedBigram = new HashMap<String,Double>();
		unBracketedBigram = new HashMap<String,Double>();
		pluralBigram = new HashMap<>();
		cm = new ColourMap();
		loadBracketedBigramList(BBPath);
		loadUnbracketedBigramList(UBBPath);
		loadPluralBigramList(PBPath);
		loadUnigramList(unigramPath);
		getBracketedBigramColours();
		getUnBracketedBigramColours();
		getPluralBigramColours();
	}
	
	public ColourMap getCm() {
		return cm;
	}
	public HashMap<String, Double> getUnigram() {
		return unigram;
	}

	public void setUnigram(HashMap<String, Double> unigram) {
		this.unigram = unigram;
	}

	public HashMap<String, Double> getBracketedBigram() {
		return bracketedBigram;
	}

	public void setBracketedBigram(HashMap<String, Double> bracketedBigram) {
		this.bracketedBigram = bracketedBigram;
	}

	public HashMap<String, Double> getUnbracketedBigram() {
		return unBracketedBigram;
	}

	public void setUnbracketedBigram(HashMap<String, Double> unbracketedBigram) {
		this.unBracketedBigram = unbracketedBigram;
	}

	public HashMap<String, HashMap<String, Double>> getPluralBigrams() {
		return pluralBigram;
	}

	public void setPluralBigrams(
			HashMap<String, HashMap<String, Double>> pluralBigrams) {
		this.pluralBigram = pluralBigrams;
	}
	public Map<String, List<String>> getInventory() {
		return readymadeInventory;
	}

	public void setInventory(Map<String, List<String>> inventory) {
		readymadeInventory = inventory;
	}
	//returns the frequency of specific terms in the bracketed,unbracketed or unigram hashMaps
	public double getFrequency (HashMap<String,Double> hmap, String key){
		double frequency = 0.0;

		for(Entry<String,Double> entry : hmap.entrySet()){
			if(entry.getKey().equalsIgnoreCase(key)){
				frequency = entry.getValue();
			}
		}
		return frequency;
	}
	//returns the frequency occurence of a pluralBigram from the PluralBigram HashMap
	public Double getPluralFrequency (HashMap<String,HashMap<String,Double>> hmap, String key){
		HashMap<String,Double> keyEntries = new HashMap<>();
		double frequency = 0.0;

		for(Entry <String,HashMap<String, Double>>  entry : hmap.entrySet()){
			if(hmap.keySet().contains(key)){
				keyEntries = hmap.get(key);
			}
		}
		for(double d : keyEntries.values()){
			if(keyEntries.values() != null){
				frequency = d;
			}
		}
		return frequency;
	}
	//returns a list of the colours associated with a given readymade name
	public List<String> getreadyMadeColours(String s){
		List<String> colourList = null;

		for(Entry<String,List<String>> entry : this.getInventory().entrySet()){
			if(entry.getKey().equalsIgnoreCase(s)){
				colourList = entry.getValue();
			}
		}
		return colourList;	
	}
	//return a set of readymade names that map to a given list of hexCodes
	public Set<String> getRmNameFromColourList(List<String> colours){
		Set<String> readymadeName = new LinkedHashSet<String>();

		for(Entry <String,List<String>> entry : this.getInventory().entrySet()){
			for(String s : entry.getValue()){
				if(colours.contains(s)){
					readymadeName.add(entry.getKey());
				}
			}	
		}
		return readymadeName;	
	}
	//an iterator of all names in the readymade inventory
	public void viewReadymadeNameList() {
		Set keys = readymadeInventory.keySet();
		Iterator listIter = keys.iterator();
		try {
			while (listIter.hasNext()) {
			System.out.println(listIter.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//reads the file and store the components of each line in a HashMap
	private void loadBracketedBigramList(String file) {
		String line = "";
		try {
			FileInputStream fileInput = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInput,"UTF-8"));
			while ((line = bReader.readLine()) != null) {
				String[] stereotype = line.split("\t");
				bracketedBigram.put(stereotype[1]+" "+stereotype[2],Double.parseDouble(stereotype[4]));
			}
			bReader.close();
			fileInput.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// gets the colours for each part of the bigram by comparison with the
	//ColourObjects set, blends the colours of the two parts and stores in Inventory
	private void getBracketedBigramColours(){
		String linguisticRule = "bracketed";
		List<String> bracketedBigramColours = new ArrayList<String>();
		try{
			for(Entry<String,Double> s: this.getBracketedBigram().entrySet()){
				String[] keySplit = s.getKey().split(" ");
			  if(!(cm.getColourObjects().keySet().contains(keySplit[0])) ||
					!(cm.getColourObjects().keySet().contains(keySplit[1]))){
					continue;
					}
			else{
				while(!(this.readymadeInventory.containsKey(s.getKey()))){
					bracketedBigramColours = cm.colourBlender(cm.getHexCodeList(keySplit[0]),
							cm.getHexCodeList(keySplit[1]),linguisticRules(linguisticRule));
					this.readymadeInventory.put(s.getKey(), bracketedBigramColours);
					}
				}		
			}
		}catch(Exception e){
				e.printStackTrace();
			}
	}
	//reads the file and store the components of each line in a HashMap
	private void loadUnbracketedBigramList(String file) {
		String line = "";
		try {
			FileInputStream fileInput = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInput,"UTF-8"));
			while ((line = bReader.readLine()) != null) {
				String[] stereotype = line.split("\t");
				unBracketedBigram.put(stereotype[0]+" "+stereotype[1],Double.parseDouble(stereotype[2]));
				}
			bReader.close();
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
			}
	}
	// gets the colours for each part of the bigram by comparison with the
	//ColourObjects set, blends the colours of the two parts and stores in Inventory
	private void getUnBracketedBigramColours(){
		String linguisticRule = "unbracketed";
		String lowerCase1 ="";
		String lowerCase2 ="";
		List<String> unBracketedBigramColours = new ArrayList<String>();

		try{
		for(Entry<String,Double> s: this.getUnbracketedBigram().entrySet()){
			String[] keySplit = s.getKey().split(" ");
			lowerCase1 = keySplit[0].toLowerCase();
			lowerCase2 = keySplit[1].toLowerCase();
			if(!(cm.getColourObjects().keySet().contains(lowerCase1)) ||
					!(cm.getColourObjects().keySet().contains(lowerCase2))){
				continue;
					}
			else{
				while(!(this.readymadeInventory.containsKey(s.getKey()))){
					unBracketedBigramColours = cm.colourBlender(cm.getHexCodeList(lowerCase1), 
							cm.getHexCodeList(lowerCase2),linguisticRules(linguisticRule));
					this.readymadeInventory.put(s.getKey(), unBracketedBigramColours);
					}
				}		
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//reads the file and store the components of each line in a HashMap
	private void loadPluralBigramList(String file) {
		String line = "";
		try {
			FileInputStream fileInput = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInput,"UTF-8"));
			while ((line = bReader.readLine()) != null) {
				 final String[] stereotype = line.split("\t");
				 pluralBigram.put(stereotype[0]+" "+stereotype[1], new HashMap<String,Double>(){{put(stereotype[3], Double.parseDouble(stereotype[2]));}});
				}
			bReader.close();
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
			}
	}
	//gets the pluralList file from the Colour Objects set and
	// blends the colours to store in the inventory
	private void getPluralBigramColours(){
		String linguisticRule = "plural";
		String singular ="" ;
		String head ="";
		Map<String,Double> temp = new HashMap<String,Double>();
		List<String> pluralColours = new ArrayList<String>();
		
		try{
		for(Entry<String, HashMap<String, Double>> s: this.getPluralBigrams().entrySet()){
			String[] keySplit = s.getKey().split(" ");
			head = keySplit[0].toLowerCase();
			temp = s.getValue();
			for(Entry<String,Double> holder : temp.entrySet())
				singular = holder.getKey().toLowerCase();
			if(!(cm.getColourObjects().keySet().contains(head)) ||
					!(cm.getColourObjects().keySet().contains(singular))){
				continue;
			}
			else{
				while(!(this.readymadeInventory.containsKey(s.getKey()))){
					pluralColours = cm.colourBlender(cm.getHexCodeList(head),
							cm.getHexCodeList(singular),linguisticRules(linguisticRule));
					this.readymadeInventory.put(s.getKey(), pluralColours);
					}
				}		
			}
		}catch(Exception e){
			e.printStackTrace();
			}
	}
	//loads the unigrams into the unigram Hashmap but also generates their set of colours
	//and stores them in the inventory of readymades
	private void loadUnigramList(String file) {	
		String line = "";
		String linguisticRule = "unigram";
		ArrayList<String> nouns = new ArrayList<String>();
		
		try{
			FileInputStream fileInput = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInput,"UTF-8"));
			while ((line = bReader.readLine()) != null) {
				String[] stereotype = line.split("\t");
				unigram.put(stereotype[0],Double.parseDouble(stereotype[1]));
				nouns = getColoursForUnigrams(cm.getColourObjects(),stereotype[0]);
				String str1 = nouns.get(0);
				String str2 = nouns.get(1);
			if(!(this.readymadeInventory.containsKey(stereotype[0]))){
				List<String> newBlend =  cm.colourBlender(cm.getHexCodeList(str1),
							cm.getHexCodeList(str2),linguisticRules(linguisticRule));
				readymadeInventory.put(stereotype[0],newBlend);
				}
			}
			
			bReader.close();
			fileInput.close();
		}
		  catch(Exception e) {                                                                                                                                                                                                                                                                                                                                                          
			  e.printStackTrace();
			}
	}
	//get set of words at start and end of unigram and return the colours of the longest substring split
	//based on the key values of the passed in Map - used as a helper for Unigrams
	private ArrayList<String> getColoursForUnigrams(Map<String, ArrayList<String>> map, String s){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> lastWords = new ArrayList<String>();
		ArrayList<String> firstWords = new ArrayList<String>();
		
		for(Entry<String, ArrayList<String>> t : map.entrySet()){
			if(s.startsWith(t.getKey()) ){
			 firstWords.add(t.getKey());
			}
			else if (s.endsWith(t.getKey())){
			 lastWords.add(t.getKey());
			}
		}
		
		String longestPre = firstWords.get(0);
		String longestSuf = lastWords.get(0);
		
		for(String str : firstWords){
			if(str.length() >longestPre.length()){
			 longestPre = str;
			}
		}
		for(String str : lastWords){
			if(str.length() > longestSuf.length()){
			 longestSuf = str;
			}
		}
		words.add(longestPre);
		words.add(longestSuf);
		
		return words;		
	}
	//simple linguistic rules method returning a double value
	// weighting for a supplied string defining the rule required
	protected double linguisticRules(String type){
		double offset = 0.0;
		try{
		switch (type){
			case "unigram":
				 offset = 0.3;
				break;
			case "plural":
			    offset = 0.25;
				break;
			case "bracketed":
				offset = 0.35;
				break;
			case "unbracketed":
			    offset = 0.4;
				break;
			default: 
			    offset = 0.3;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return offset;	
	}
	//readyMades Iterator
	public void viewReadymadeList() {
		Set keys = readymadeInventory.entrySet();
		Iterator listIter = keys.iterator();
		try {
			while (listIter.hasNext()) {
			System.out.println(listIter.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void printcMakerMapSizes(){
		System.out.println("ReadyMadeInventory contains "+this.readymadeInventory.size()+" entries");
		System.out.println("UnigramMap contains "+this.unigram.size()+" entries");
		System.out.println("BracketedBigramMap contains "+this.bracketedBigram.size()+" entries");
		System.out.println("UnbracketedBigramMap contains "+this.unBracketedBigram.size()+" entries");
		System.out.println("PluralBigramMap contains "+this.pluralBigram.size()+" entries");
	}

	
}
