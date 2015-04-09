package Colourama;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter4j.TwitterException;
import  Colourama.ColourMap;
import Colourama.ColourMaker;

public class Test {

	public static void main(String[] args) throws TwitterException, IOException {
		
		//ColourMaker
		System.out.println("ColourMaker Results:");
		ColourMaker r = new ColourMaker();
		r.printcMakerMapSizes();
		List<String> gggggg = new ArrayList<String>();
	
		List<String> ab = Arrays.asList("#754E7F","#5F7172","#587B8C","#58678C");
		System.out.println(r.getFrequency(r.getUnigram(),"acidbaby"));
		System.out.println(r.getreadyMadeColours("amberlily"));
		System.out.println(r.getRmNameFromColourList(ab));
		
		System.out.println(r.getPluralFrequency(r.getPluralBigrams(), "pine nuts"));
	
		System.out.println(r.getCm().getHexCode("acid_green"));
		System.out.println(r.linguisticRules("plural"));
		System.out.println("\nColourMap Results");
		r.viewReadymadeList();
		
		//COLOURMAP

		ColourMap cmap = new ColourMap();
		System.out.println(cmap.formatHexCode("0x56de17"));
		System.out.println(cmap.getHexCode("acid_green"));
		System.out.println(cmap.getRGBNumber("acid_green"));
		System.out.println(cmap.getHexCode("apple_green"));
		System.out.println(cmap.getHexCode("apple_red"));
		System.out.println(cmap.getColourName("#8DB600"));

		List <String> ntt = new ArrayList<>();
		ntt = cmap.getColoursForObjects("sunset");
		System.out.println(ntt);
		System.out.println(cmap.getHexCodeList("apple"));
		
		ArrayList<String> str1 = new ArrayList<String>();
		ArrayList<String> str2 = new ArrayList<String>();
		List<String> str3 = new ArrayList<String>();
		str1.add("#FFFFFF");
		str1.add("#020202");
		str1.add("#000000");
		str2.add("#44AABB");
		str2.add("#23AB23");
		//str3 = cmap.colourBlender(str1, str2, 0.3);
		str3 = cmap.getRGBListFromHexList(ab);
		System.out.println(str3);
		System.out.println(cmap.getColoursForObjects("sage"));
		String tt = "#CC8E69";
		String bb = "#9C8F60";
		//String zz = "#7CFC00";
		
		String cc = cmap.colourBlender(tt, bb,0.11);
		System.out.println(cc);


		System.out.println(cmap.colourBlender("#000000","#FFFFFF",.99));
		System.out.println(cmap.colourBlender("#000000", "#FFFFFF")); 
		
		String testColour1 = "#EFFFF0"; 
		String testColour2 = "#FFFFFF";
		double testEuclideanDistance = cmap.getRGBDistance(testColour1, testColour2);
		System.out.println(testEuclideanDistance);
		
		//ColourTweetGenerator
		
		NamexTweet3 tweeter = new NamexTweet3();
		ColourMap cMap = new ColourMap();
		ColourMaker cMaker = new ColourMaker();
		ColourTweetGenerator gg = new ColourTweetGenerator(cMaker, cMap,0.03);
		gg.tweetRandomReadyMade(tweeter);
		
	}
}
