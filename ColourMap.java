package Colourama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ColourMap {

	private Map<String, String> colourMapper = new HashMap<String,String>(); // public
	private Map<String,ArrayList<String>> colourObjects = new HashMap<String,ArrayList<String>>();
	final String cLPath = "resource" + File.separator + "ColourList.txt";

	public ColourMap() {
		try {
			addBasicColours();
			loadColoursFromFile(cLPath);
			makeColourObjects();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// read the names and colour with each hexValue from text
	// joining the colour type onto the associated word as a stereotyped string
	// and storing that as a key to the HexValue associated with it
	public void loadColoursFromFile(String file) {
		try {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = null;
			while ((line = bReader.readLine()) != null) {
				String[] stereotype = line.split("\t");
				String colourName = stereotype[0] + "_" + stereotype[1];
				String colourCode = stereotype[2];
				if (!colourMapper.containsKey(colourName)) {
					colourMapper.put(colourName, colourCode);
				}
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//getter and setter functions
	public Map<String, ArrayList<String>> getColourObjects() {
		return colourObjects;
	}

	public void setColourObjects(HashMap<String,ArrayList<String>> colourObjects) {
		this.colourObjects = colourObjects;
	}

	public void setColourMapper(Map<String, String> colourMapper) {
		this.colourMapper = colourMapper;
	}

	public Map<String, String> getColourMapper() {
		return colourMapper;
	}
	public String getHexCode(String colourAlias) {
		return colourMapper.get(colourAlias);
	}
	public ArrayList<String> getHexCodeList(String colourAlias){
		return colourObjects.get(colourAlias);
	}
 
	//add basic colours to the colourMap
	private void addBasicColours(){
		this.colourMapper.put("red", "#FF0000");
		this.colourMapper.put("green", "#00FF00");
		this.colourMapper.put("blue", "#0000FF");
		this.colourMapper.put("black", "#000000");
		this.colourMapper.put("white", "#FFFFFF");
		this.colourMapper.put("pink", "#FFC0CB");
		this.colourMapper.put("purple", "#800080");
		this.colourMapper.put("orange", "#FFA500");
		this.colourMapper.put("yellow", "#FFFF00");
		this.colourMapper.put("brown", "#A52A2A");
		this.colourMapper.put("gray", "#808080");
		this.colourMapper.put("grey", "#808080");	
	}
	//returns the colour name based on hexcode
	public String getColourName(String name){

		Iterator<Map.Entry<String,String>> iter = colourMapper.entrySet().iterator();
		String key ="";
		while (iter.hasNext()) {
			Map.Entry<String,String> entry = iter.next();
			if (entry.getValue().equals(name)) {
				key = entry.getKey();
			}
		}
		return key;
	}
	
	//return a string of RGB values from a supplied colourName
	@SuppressWarnings("static-access")
	public String getRGBNumber(String colourAlias) {

		String convertHex = colourMapper.get(colourAlias);
		int[] rgb = new int[3];
		rgb = getRGBValues(convertHex);
		String rGB = "";
		return rGB.format("%03d", rgb[0]) + " " + rGB.format("%03d", rgb[1])
				+ " " + rGB.format("%03d", rgb[2]);
		}
	
	//add entries to the colourMap
	public void putColourCode(String Name, String Code) {

		if (!(match(Code)) || !(Name instanceof String)) {
			System.out.println("Not a valid colour code");
		} else {
			colourMapper.put(Name, Code);
		}
	}
	
	// test for valid hex string
	private boolean match(String code) {

		final String hex_Pattern = "^#([0-9A-Fa-f]{6})$";
		if (code.matches(hex_Pattern)) {
			return true;
		}
		return false;
	}

	// Two versions of a method to produce a new colour blended from two
	// known colours - One with an adjustable offset - the other a 50-50 blend
	public String colourBlender(String colour1, String colour2, double offset) {

		int[] RGB1 = getRGBValues(colour1);
		int[] RGB2 = getRGBValues(colour2);
		int[] newRGB = new int[3];
		
		if (offset < 0.0 || offset > 1.0){ 
			System.out.println("Invalid offset submitted");
			System.exit(0);
		}
		newRGB[0] = (int) Math.abs((offset * RGB1[0] - (1 - offset) * RGB2[0]));
		newRGB[1] = (int) Math.abs((offset * RGB1[1] - (1 - offset) * RGB2[1]));
		newRGB[2] = (int) Math.abs((offset * RGB1[2] - (1 - offset) * RGB2[2]));
		return  getNewHexCode(newRGB);
	}
	public String colourBlender(String colour1, String colour2) {

		return colourBlender(colour1, colour2, 0.5);			
	}
	
	// this version of colourBlender returns a list of blended colours from the supplied
	// lists of colours and an offset to bias the weighting of both colours being combined
	public List<String> colourBlender(ArrayList<String> list1, ArrayList<String> list2,double offset){
		
		if (offset < 0.0 || offset > 1.0){ 
			System.out.println("Invalid offset submitted");
			System.exit(0);
		}

		List<String> newNumbers = new ArrayList<String>();	
		for(int i = 0;i < list1.size();i++){
			for(int j = 0;j<list2.size();j++){
				newNumbers.add(colourBlender(list1.get(i),list2.get(j),offset));
			}
		}
		return newNumbers;
	}

	// helper function that converts an integer array to a hex string
	private String getNewHexCode(int[] RGB) {
		String hexCode;
		String red, blue, green;

		red = Integer.toHexString(0x100 | RGB[0]).substring(1).toUpperCase();
		green = Integer.toHexString(0x100 | RGB[1]).substring(1).toUpperCase();
		blue = Integer.toHexString(0x100 | RGB[2]).substring(1).toUpperCase();
		hexCode = '#' + red + green + blue;
		return hexCode;
	}
	
	//function goes through the colourMap key entries and splits them
	//and for each noun found in the split, puts that with the relevant colour into 
	//the colourObject Map, having checked for possible repetition of colours within
	//the values
	@SuppressWarnings("serial")
	private void makeColourObjects(){

		ArrayList<String> list = new ArrayList<String>();

		for(final Entry<String,String> k : this.getColourMapper().entrySet()){
			final String[] colourObject = k.getKey().split("_");

		 if(colourObjects.containsKey(colourObject[0])){
			  list = colourObjects.get(colourObject[0]);
			  list.add(0,k.getValue());
			  colourObjects.remove(colourObject[0]);

			if(list.get(0).equals(list.get(1))){
				list.remove(list.get(1));
				}
			if(list.size()>2){
				if(list.get(0).equals(list.get(2))){
					list.remove(2);
					}
				}
			colourObjects.put(colourObject[0],list);
			}
	else{
			colourObjects.put(colourObject[0],new ArrayList<String>(){{add((k.getValue()));}});
			}
		}
	}
	
	@SuppressWarnings("static-access")
	//returns a list of rgbCodes from a supplied list of hex codes
	public List<String> getRGBListFromHexList(List<String> hexList){
		
		List<String> rgbList = new ArrayList<String>();
		String temp ="";
		for(String s : hexList){
			int[] rgb = getRGBValues(s);
			rgbList.add(temp.format("%03d", rgb[0]) + " " + temp.format("%03d", rgb[1])
					+ " " + temp.format("%03d", rgb[2]));
			}
		return rgbList;
		
	}
	
	//returns a list of colours for a given noun from the colourObject Map
	public List<String> getColoursForObjects(String noun){
		List<String> hexList = new ArrayList<String>();
		for(Entry<String, ArrayList<String>> s : this.getColourObjects().entrySet()){
			if(s.getKey().equals(noun)){
				hexList.addAll(0,s.getValue());
			}
		}
		return hexList;	
	}
	
	// helper function that converts a hexString to numeric values
	// by splitting the string into red green and blue
	private int[] getRGBValues(String hexCode) {
		String hexConvert = hexCode;
		int[] rgbNumbers = new int[3];

		rgbNumbers[0] = Integer.parseInt(hexConvert.substring(1, 3), 16);
		rgbNumbers[1] = Integer.parseInt(hexConvert.substring(3, 5), 16);
		rgbNumbers[2] = Integer.parseInt(hexConvert.substring(5, 7), 16);

		return rgbNumbers;
	}
	//changes the format of hexCodes to match the colourMap entries
	public String formatHexCode(String hexcode){
		String formattedHex = "";
	try{
		if(hexcode.startsWith("0x")){
		formattedHex = hexcode.replace("0x", "#");
		}
		if(match(formattedHex)){
		return formattedHex.toUpperCase();
		}
		else{
			return "Invlalid Hexcode format entered";
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return formattedHex;
		
	}
	// for calculating the similarity of colours
	public double getRGBDistance(String RGB1, String RGB2) {

		double euclideanDistance = 0;
		final double FULLCOLOUR = 255;
		int[] rgb1 = getRGBValues(RGB1);
		int[] rgb2 = getRGBValues(RGB2);

		double normRed1 = rgb1[0] / FULLCOLOUR;
		double normGreen1 = rgb1[1] / FULLCOLOUR;
		double normBlue1 = rgb1[2] / FULLCOLOUR;
		double normRed2 = rgb2[0] / FULLCOLOUR;
		double normGreen2 = rgb2[1] / FULLCOLOUR;
		double normBlue2 = rgb2[2] / FULLCOLOUR;

		double distanceRed = normRed2 - normRed1;
		double distanceGreen = normGreen2 - normGreen1;
		double distanceBlue = normBlue2 - normBlue1;

		euclideanDistance = Math.sqrt((distanceRed) * (distanceRed)
				+ (distanceGreen) * (distanceGreen) + (distanceBlue)
				* (distanceBlue));
		return euclideanDistance;
	}
}