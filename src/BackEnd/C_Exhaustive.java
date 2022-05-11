package BackEnd;

import java.util.Arrays;

public class C_Exhaustive {
	//key is 22 for M2G2.txt - Key must be maximum 26
	
	public static byte[] CaeserDencrypt(byte[] ct, Integer keyShift )
	{	
		byte pt[] = new byte[ct.length];	
		for (int i = 0; i != ct.length; i++) {
			pt[i] = (byte) (((char)(ct[i] + 'A') - keyShift) % 26 + 'A');	
		}
		return pt;    
	}
	
		//method to return the dot product of the byte array which then calculates frequencies and the Alphabet English array
		public static double CosineSimilarity(byte[] ar) {
			double dotProduct = 0.0;
			double normA = 0.0;
			double normB = 0.0;
			
			for (int i = 0; i < CryptoTools.getFrequencies(ar).length; i++) {
				
				dotProduct += CryptoTools.getFrequencies(ar)[i] * CryptoTools.ENGLISH[i]; //ENGLISH is constant value
				normA += Math.pow(CryptoTools.getFrequencies(ar)[i], 2);
		        normB += Math.pow(CryptoTools.ENGLISH[i], 2);
			}
			return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
		}
	
		
		public static void main(String[] args) throws Exception {	
	
		byte[] raw = CryptoTools.fileToBytes("data/QQ2V.txt"); //read the ciphertext from this file and clean it
		byte[] ct = CryptoTools.clean(raw);
		
		//OPTION 1 - Exhaustive Attack against Caesar
		//When you obtain the cosine similarity value closest to 1, use it as a shift for the decryption
		for (int j = 0; j < 26; j++) { //alphabet letters 0 to 25, less than 26, can only pick between 0-25 keys (total 26)
			
			CryptoTools.bytesToFile(CaeserDencrypt(ct, j), "data/Trial.txt");
			byte[] eh = CryptoTools.fileToBytes("data/Trial.txt");
			System.out.println("Shift = " + j + " Cosine Similarity = " + CosineSimilarity(eh));
									
		}
		
		//**************************************************************************************************************************
		//OPTION 2 - Cryptanalytic Attack against Caesar
		//Find the most frequently occuring letter in the ciphertext
		//Subtract the position of it from the position of e (5 in alaphabet 1-25)
		//Use that as the shift value i.e. the key
		System.out.println(Arrays.toString(CryptoTools.getFrequencies(ct)));
		
		//**************************************************************************************************************************
		//OUTPUT
		//CryptoTools.bytesToFile(CaeserDencrypt(ct, 5), "data/Trial.txt"); //outputs the decrypted Ciphertext to a text file
		//System.out.print(new String(CaeserDencrypt(ct, 5))); //outputs the ciphertext to the console 

		
		}
	
}
