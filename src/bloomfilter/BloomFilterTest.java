package bloomfilter;

import java.io.*;
import java.util.*;

/*
 * Module to test the BloomFilter Library
 */

public class BloomFilterTest {

	
	public static void main(String[] args){
		
		BloomFilter <String>bf;
		FileReader fr;
		bf = new BloomFilter <String>(0.01, 500);
    	System.out.println("hello");
		
		// testadd()
	
		String filename = "C:/Current Java Projects/src/sampleurls.txt";
	    BufferedReader br=null;
			try {
				br = new BufferedReader(new FileReader(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line=null;
				try {
					line = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		        while (line != null) {
		            //sb.append(line);
		        //	System.out.println(line);
		        	bf.add(line); ;	// add string to bloomfilter
		        	
		            try {
						line = br.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		         sb.toString();
		    } finally {
		        try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		// test getVector()
		System.out.println(bf.getVector());
		
		//testcontains()
		
		String testurl = "http://www.twitter.com/";
		System.out.println("URL : " + bf.contains(testurl));
		
	}
}
