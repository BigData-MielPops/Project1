package project1;

public class Util {

	private static String separator = "\t";

	public static void insertInOrderByScore(String val, String[] floats){
		System.out.println("==insert==");
		System.out.println(val);
		int i;
		for(i=0; i<floats.length; i++){
			try {
				System.out.print(floats[i] + " ");
				System.out.println();
				if(Float.parseFloat(floats[i].split(separator)[0]) < Float.parseFloat(val.split(separator)[0]))
					break;
			}
			catch (Exception e) {
				break;
			}
		}
		System.out.println(i);
		if(i<floats.length){
			for(int k=floats.length-2; k>=i; k--){
				System.out.print(floats[k] + " ");
				floats[k+1]=floats[k];            
			}
			System.out.println();

			floats[i]=val;
		}
	}

	public static String orderCouple(String s1, String s2) {
		return orderCouple(s1, s2, separator);
	}

	public static String orderCouple(String s1, String s2, String separator) {
		if(s1.compareTo(s2) == -1) return s1 + separator + s2;
		//else
		return s2 + separator + s1;
	}
}
