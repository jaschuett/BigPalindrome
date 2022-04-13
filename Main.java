/* Problem: Find the highest palindrome that can be created by 
 * multiplying two 7-digit numbers
 * 
 * Procedure:
 * 1. Find the highest palindrome below (9999999)^2
 * 2. Check whether it can be divided by a number between 1000000 and 9999999
 * 3. If not, find the highest palindrome below the previous palindrome and repeat
 */
public class Main {
	static int factor1;
	static int factor2;

	public static void main(String[] args) {
		long start = System.nanoTime();
		System.out.println("The answer is: " + palindrome());
		long end = System.nanoTime();
		System.out.println("Seconds to execute: " + (end-start)/(Math.pow(10,9)));
		System.out.println("First factor: " + factor1);
		System.out.println("Second factor: " + factor2);
	}
	
	
	
	public static long palindrome() {
		//max 7-digit number
		int max = 9999999;
		//max possible value of the product of two 7-digit numbers
		long maxProduct = 9999999L * 9999999L; 
		long minProduct = 1000000L * 1000000L;
		long pal = nextLowestPalindrome(maxProduct);
		
		//Group the potential factors into different 'blocks', and for each
		//palindrome, check if it is divisible by any of the numbers in the block.
		//Then move onto the next block and check every palindrome for that block.
		//The answer is likely to be close to 9999999 so we want to check
		//big values first 
		
		//Note: if the block size is too small, we end up finding the largest 7-digit
		//number that divides a palindrome instead of the largest palindrome which is divided
		//by a 7-digit number
		int blockSize = max/1000;
		for (int block = 0; block < max/blockSize; block++) {
			pal = nextLowestPalindrome(maxProduct);
			
			while (pal > minProduct) {
				//check potential factors
				for (int i = max - (block*blockSize); i > max-((block+1)*blockSize); i--) {
					if (pal % i == 0) {
						if (((pal/i)+"").length() == 7) //7 digit
						{
							factor1 = i;
							factor2 = (int)(pal/i);
							return pal;
						}
					}
				}
				pal = nextLowestPalindrome(pal);
			}
			
		}
		return -1; //not found
	}
	
	//Returns the highest palindrome lower than n. If n is a palindrome,
	//returns the highest palindrome lower than n-1
	private static long nextLowestPalindrome(long n) {
		String nStr = ""+n;
		int len = nStr.length(); //number of digits of n
		
		//divide the string into left, middle, right
		String left = nStr.substring(0, len/2);
		String middle = ""; //the middle digit if it has odd length
		String right = nStr.substring(len/2, len);
		if (len%2 == 1) {
			middle = ""+nStr.charAt(len/2); 
			right = nStr.substring(len/2+1, len);
		}
		
		boolean isPal = true;
		
		//test if n is already a palindrome;
		//we don't want to return it if it is
		for (int i = 0; i < len/2; i++) {
			if (left.charAt(len/2-i-1) != right.charAt(i)) {isPal = false;}
		}
		
		if (isPal) return nextLowestPalindrome(n-1);
		
		//reverse rightHalf so the halves are directly comparable
		StringBuilder revRight = new StringBuilder();
		revRight.append(right);
		revRight.reverse();
		right = revRight.toString();
		
		//find the next lowest palindrome based on different cases
		if(middle.equals("")) {
			if (Long.parseLong(left) < Long.parseLong(right)) {
				//make a copy of the left side (higher powers of 10),
				//reverse it, and stick it on the right side to make
				//a palindrome
				StringBuilder revLeft = new StringBuilder();
				revLeft.append(left);
				revLeft.reverse();
				return Long.parseLong(left+revLeft.toString());
			}
				
			if (Long.parseLong(left) > Long.parseLong(right)) {
				left = ""+(Long.parseLong(left)-1);
				
				StringBuilder revLeft = new StringBuilder();
				revLeft.append(left);
				revLeft.reverse();
				return Long.parseLong(left+revLeft.toString());
			}
		}
		
		if (!middle.equals("")) {
			if (Integer.parseInt(middle) > 0) {
				middle = ""+(Integer.parseInt(middle)-1);
				
				StringBuilder revLeft = new StringBuilder();
				revLeft.append(left);
				revLeft.reverse();
				return Long.parseLong(left+middle+revLeft.toString());
			}
			
			if (Long.parseLong(left) < Long.parseLong(right)) {
				StringBuilder revLeft = new StringBuilder();
				revLeft.append(left);
				revLeft.reverse();
				return Long.parseLong(left+"9"+revLeft.toString());
			}
				
			if (Long.parseLong(left) > Long.parseLong(right)) {
				left = ""+(Long.parseLong(left)-1);
				
				StringBuilder revLeft = new StringBuilder();
				revLeft.append(left);
				revLeft.reverse();
				return Long.parseLong(left+"9"+revLeft.toString());
			}
		}
		
		return Long.parseLong(left+middle+right);
	}

}
