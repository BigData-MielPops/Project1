package project1;

public class Util {
	
	public static void insert(String val, String[] floats){
		System.out.println("==insert==");
		System.out.println(val);
		int i;
		for(i=0; i<floats.length; i++){
			try {
				System.out.print(floats[i] + " ");
				System.out.println();
				if(Float.parseFloat(floats[i].split("\t")[0]) < Float.parseFloat(val.split("\t")[0]))
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
}
