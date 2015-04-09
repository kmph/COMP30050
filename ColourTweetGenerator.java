package Colourama;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ColourTweetGenerator {
	
		private Map<String,String> rgbURLMap = new HashMap<String,String>();
		private Map<String,List<String>> rmadeToURLMap = new HashMap<String,List<String>>();
		private Map<String,String> rgbToRMadeMap = new HashMap<String,String>();
		final String cbtPath = "resource" + File.separator + "colorbot_tweets.txt";
		private ColourMaker cMaker;
		private ColourMap cMap;
		
	public ColourTweetGenerator(ColourMaker cMaker, ColourMap cMap,double threshold) {
		this.cMaker = cMaker;
		this.setcMap(cMap);
		loadRGBURLMap(cbtPath);
		matchRmWithThresholdToRgb(threshold);
		mapRMtoURL();
	}
	//read the file for RGB and URl relationships and store in rgbURLMap
	public void loadRGBURLMap(String file) {
		String line = "";
		try {
			FileInputStream fileInput = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInput,"UTF-8"));
			while ((line = bReader.readLine()) != null) {
				String[] rgbURL = line.split("\t");
				rgbURLMap.put(rgbURL[0],rgbURL[1]);
			}
			bReader.close();
			fileInput.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//getter and setter functions
	public Map<String, String> getRgbURLMap() {
		return rgbURLMap;
	}
	public List<String> getRgbURLMapKeys(){
		List<String> t = new ArrayList<String>();
		for (String s : rgbURLMap.keySet())
			t.add(s);
		return t;
	}
	public void setRgbURLMap(Map<String, String> rgbURLMap) {
		this.rgbURLMap = rgbURLMap;
	}
	public ColourMaker getcMaker() {
		return cMaker;
	}
	public void setcMaker(ColourMaker cMaker) {
		this.cMaker = cMaker;
	}
	public ColourMap getcMap() {
		return cMap;
	}
	public void setcMap(ColourMap cMap) {
		this.cMap = cMap;
	}
	public Map<String, String> getRgbToRMadeWithThreshold() {
		return rgbToRMadeMap;
	}
	public void setRgbToRMadeWithThreshold(
			Map<String, String> rgbToRMadeWithThreshold) {
		rgbToRMadeMap = rgbToRMadeWithThreshold;
	}
	public Map<String, List<String>> getRmadeToURLMap() {
		return rmadeToURLMap;
	}
	public void setRmadeToURLMap(Map<String, List<String>> rmadeToURLMap) {
		this.rmadeToURLMap = rmadeToURLMap;
	}
	
	//tweets a random element of the rMadeToURLMap based on random number selection
	public void tweetRandomReadyMade(NamexTweet3 tweet) {
		
		String url ="";
		Random  number = new Random();
		String readymade = "";
		
		try{
			int temp = number.nextInt(this.rmadeToURLMap.size());
			String[] rMades = this.getRmadeToURLMap().keySet().toArray(new String[this.rmadeToURLMap.size()]);
			List<String> urls = this.getRmadeToURLMap().get(rMades[temp]);
			readymade = rMades[temp];
			
			//if theres more than one url for a readymade choose one of those urls randomly
			if(urls.size() > 1){
			int holder = number.nextInt(urls.size());
			url = urls.get(holder);
			}
			else{
				url = urls.get(0);
			}
			
			String message = "I call this colour.."+" "+"\""+readymade +"\""+" "+ url;
			tweet.authenticate(message);
			
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// method that returns a list of readymades and their distance from a given hexcode within a supplied threshold
	 public List<String> getRMListWithinThreshold(String hexcode, double threshold){
		 
		double distance = 0.0;
		String hex = cMap.formatHexCode(hexcode);//change the format of the hexcode to match the inventory keyset
		List<String> readyMades = new ArrayList<String>();
	
		for(String key : this.cMaker.getInventory().keySet()){
			List<String> holder = new ArrayList<String>();
			holder = cMaker.getInventory().get(key);
			for (String t : holder)
				if((distance = this.cMap.getRGBDistance(hex, t)) <= threshold){
				readyMades.add(new String(distance +"\t"+ key));
				}
			}
		
		return readyMades;
	}
	// sorts a list of array lists- the distance of the list of readymades should be used in this classes case
	private List<ArrayList<String>> sortRMArrayList(List<ArrayList<String>> unordered){
		
		List<ArrayList<String>> sortedArrayList = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> sublist: unordered){
			ArrayList<String> sortedList = new ArrayList<String>();
			Collections.sort(sublist);
			sortedList = sublist;
			sortedArrayList.add(sortedList);
		}
		
		return sortedArrayList;
		
	}
	//sorts a list of strings -again the distance of the string/readymade should be used to sort preferably
	private List<String> sortRMList(List<String> unordered){
	
			List<String> sortedList = new ArrayList<String>();
			Collections.sort(unordered);
			sortedList =  unordered;
			
		return sortedList;
	}
	
	//populates the rgbtoRMadeMap - gets the elements within distance, then sorts them
	//based on euclidean distance before storing them by name
	private void matchRmWithThresholdToRgb(double threshold){
		
		List<String> readyMades = new ArrayList<String>();
		String[] lineSplit;
		try{
		for(String rgb : this.getRgbURLMapKeys()){
		  readyMades = this.getRMListWithinThreshold(rgb, threshold);
		  if(readyMades.isEmpty()){
				continue;
			}
			else{
			this.sortRMList(readyMades);
			lineSplit = readyMades.get(0).split("\t");
			this.rgbToRMadeMap.put(rgb,lineSplit[1]);
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//creates a mapping between readymades and URLs based on the rgb keys of two maps
	private void mapRMtoURL(){
		
		try{
		String readymade="";
		for(String rgb : this.getRgbURLMapKeys()){
			readymade = this.rgbToRMadeMap.get(rgb);
			if(readymade == null){
				continue;
			}
			else if(!rmadeToURLMap.containsKey(readymade)){
				List<String> urls = new ArrayList<String>();
				urls.add(this.rgbURLMap.get(rgb));
				rmadeToURLMap.put(readymade,urls);
			}
			else{
				List<String> urls = new ArrayList<String>();
				urls = this.rmadeToURLMap.get(readymade);
				urls.add(0, this.rgbURLMap.get(rgb));
				this.rmadeToURLMap.remove(readymade);
				this.rmadeToURLMap.put(readymade, urls);
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	
}
