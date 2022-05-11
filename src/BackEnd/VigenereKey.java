package BackEnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.print.CancelablePrintJob;

public class VigenereKey {

	
	public static byte[] VigenereDecrypt(byte[] ct, Integer key)
	{	
		byte pt[] = new byte[ct.length];	
		for (int i = 0; i < ct.length; i++) {
			pt[i] = (byte)(((char)(ct[i] + 'A') - key) % 26 + 'A');
		}
		return pt; 
	}
	//^ DOESN'T REALLY WORK DON'T USE//
/////////////////////////////////////////////////////////////////////////////////////
	
	
	
public static  byte[] Sample(byte[] ar, int skip, int start) {
    //byte[] result = new byte[1 + ar.length/skip];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //int pos = 0;
            for( int i = start; i < ar.length; i = i + skip) { //ADD ANOTHER VALUE TO i = 0 FOR EACH KEY SEGMENT #-1. FOR 9 SEGMENTS GO TO 8. 
        //result[pos] = ar[i];
        //pos++;
                baos.write(ar[i]);
        }
    //return result;
    return baos.toByteArray();
}
	

public static double CosineSimilarity(byte[] ar) {
	double dotProduct = 0.0;
	double normA = 0.0;
	double normB = 0.0;
	
	int[] freq = CryptoTools.getFrequencies(ar);
	for (int i = 0; i < freq.length; i++) {
		dotProduct += freq[i] * CryptoTools.ENGLISH[i]; //ENGLISH is constant value
		normA += Math.pow(freq[i], 2);
        normB += Math.pow(CryptoTools.ENGLISH[i], 2);
	}
	return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
}


public static int keyLength(byte[] ct) {
	//has to be an IC value that is closest to 0.064 for English
	HashMap<Integer, Double> icValue = new HashMap<>();
	 for (int k = 1; k < 50; k++) { //use to find IC for key length from 1 to 50
		 byte[] sample = Sample(ct, k,0); // use to find IC
		double ic =CryptoTools.getIC(sample, 100000);
		String shortDouble = String.format("%.2f", ic);
		icValue.put(k, Math.abs(0.064 - Double.parseDouble(shortDouble))); 
		//System.out.printf("Key length %2d --> IC = %.3f\n", k, Math.abs(0.064 - ic));
		}
	//Finds IC value that is closest to 0.064 for English language
	  return minIC(icValue);
}

public static int minIC(HashMap<Integer, Double> map) {
	int length = 0;
	 Double min = map.get(1);
	 for (int j = 2; j < map.size(); j++) {
		 if (map.get(j) < min) {
			 min = map.get(j);
			 length = j;
		 }
	 }
	return length;
}

public static int maxCSValue(ArrayList<Double> arr) {
	double give = 0;
	int result = 0;
	for (int i = 0; i< arr.size(); i++){

			if (arr.get(i) > give){
			give = arr.get(i);
			result = i;
			}
		}
	return result;
}

public static int segmentShiftValue(ArrayList<byte[]> segments_list, int i) {
	ArrayList<Double> arr_CS = new ArrayList<Double>();

	for (int j = 0; j < 26; j++) { 
		byte[] eh = C_Exhaustive.CaeserDencrypt(segments_list.get(i), j);
		arr_CS.add(j, CosineSimilarity(eh));
		maxCSValue(arr_CS);
		}	
	return maxCSValue(arr_CS);
}

public static String keyString(byte[] ct) {
	String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	String resultString = "";
	ArrayList<String> reStrings = new ArrayList<>();
	ArrayList<byte[]> segments_list = new ArrayList<byte[]>();
	for (int i = 0; i < keyLength(ct); i++) {
		segments_list.add(Sample(ct, keyLength(ct), i));
		//System.out.print(alphabet[segmentShiftValue(segments_list, i)] + "  - Shift is: "+ segmentShiftValue(segments_list, i) + "\n"); //gives key and shift in parts i.e. lines
		reStrings.add(alphabet[segmentShiftValue(segments_list, i)]); //gives the complete key
	}
	return reStrings.toString();
}


	public static void main(String[] args) throws Exception {

		//Step 1 - place ciphertext in Vciphertext.txt
			byte[] ct = CryptoTools.fileToBytes("data/Vciphertext.txt"); //ciphertext goes in here
			
		//Step 2 - find the key length
		//USE THIS TO CHECK FOR IC (CLOSEST TO 0.064 FOR ENGLISH) TO FIND KEY LENGTH ////////////
		//IC value that is CLOSEST to 0.064 for English gives the KEY LENGTH
			//keyLength(ct); 
			//System.out.print(keyLength(ct));
		
		//Step 3  - Check cosine similarity for the letter from each segment, obtain the key string
		//replace key_length with discovered key_length value from Step 2
			//int key_length = 9; 
		
			System.out.println(keyString(ct));	
	
	}
	

}
	
	
	
	
	
	
	
	
	
	
	

