package bloomfilter;

import java.io.Serializable;
import java.util.*;
import java.security.*;
import java.nio.charset.Charset;

/*
 Implementation of Bloom Filter in Java
 */

public class BloomFilter <E> implements Serializable {
	
	private int N;  // no of elements 
	private int k; // no of hash functions
	private int setSize;
	private BitSet vector;
	
	static final Charset charset = Charset.forName("UTF-8"); // to store hash values as strings
	private int m;  // size of Vector
	private static MessageDigest md;
	
	static {
		try{
			md  = MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException e){
			md = null;
		}
	}
	
	/*
	 * Initialize members
	 * bits is the no of of bits per element
	 * n is the no of elements to be added to BloomFilter
	 * k is the no of hash functions to be used 
	 */
	
	public BloomFilter(double bits,int n, int k) {
		this.k = k;
		this.N = n;
		this.setSize = (int)Math.ceil(bits * n);
		this.vector = new BitSet(setSize);
	}
	
	/*
	 * errorProbability is the probability of False Positives
	 * n is the no of elements for which bloom filter is to be created
	 */
	
	public BloomFilter(double errorProbability, int n) {
		
		this(Math.ceil(-(Math.log(errorProbability) / Math.log(2))) / Math.log(2), // bits = k / ln(2)
	             n,
	             (int)Math.ceil(-(Math.log(errorProbability) / Math.log(2)))); // k = ceil(-log_2(false prob))	
	}
	
	public BloomFilter(int bitSetSize, int NumberOElements) {
        	this(bitSetSize / (double)NumberOElements,
        	     NumberOElements,
        	     (int) Math.round((bitSetSize / (double)NumberOElements) * Math.log(2.0)));
    }
	
	/*
	 * Generate message digests based on bytearray & split the array into 4 parts
	 * data stores string to be hased in byte form
	 * totalhashes based on number of hash functions
	 */
	
	public static int [] createHashValues (byte data[],int totalhashes) {
		int hashindexes[] = new int[totalhashes];
		int k = 0;
		byte salt = 0;
		
		while(k < totalhashes) {
			byte [] digest;
			md.update(salt);
			salt++;
			digest = md.digest(data);
			
			for (int i = 0; i < digest.length/4 && k < totalhashes; i++) {
				int index = 0;
		                for (int j = (i*4); j < (i*4)+4; j++) {
		                    index <<= 8;
		                    index |= ((int) digest[j]) & 0xFF;
		                }
		        	hashindexes[k] = index;
		             	k++;
	         }			
		}
		return hashindexes;
	}
	
	public int getNoOfHashFunctions(){
		return k;
	}

	public void clear(){
		vector.clear();
	}
	
	public void add(E element){
		 add(element.toString().getBytes());		
	}
	
	public void add(byte bytes[]){
		int indexes[] = createHashValues(bytes, k);	// hash the element through k hash functions
		for(int index: indexes){
			vector.set(Math.abs(index % setSize),true);	// set the bit at each index returned by hash function
		}
	}
	
	public boolean contains(E element){
		return contains(element.toString().getBytes());
	}
	
	public boolean contains(byte bytes[]){
		int indexes[] = createHashValues(bytes, k);
		for (int index : indexes){
			if(!vector.get(Math.abs(index % setSize))){
				return false;		// Element definitely not present in the Set
			}
		}
		return true;			  	// Element might be present in the set
	}
	
	public BitSet getVector(){
		return vector;
	}
	
	public void setBit(int index){
		vector.set(index);		
	}
}
