package html;

public class PriceAR {
	private Flight [] A_price;
	private Flight [] R_price;
	private int days;
//	private int min;
	public static void main(String[] fasfaf){
		Flight[] a = Database.search("roma", "oslo", "2016-04");
		Flight[] r = Database.search("oslo", "roma", "2016-04");
		Utils.sort(a);
		
		Flight[] r_next = Database.search("oslo", "roma", "2016-05");
		PriceAR p = new PriceAR(a,r,r_next,10);
		
	}
	public PriceAR(Flight [] A, Flight[] R, Flight[] R_next, int days){
		
		int k, i = 0;
		Flight[] F;
		if (R_next.length >= days){
			System.out.println("c'è next month");
			F = new Flight[A.length];
			for ( k = days-1;k<R.length;k++){
				F[i] = R[k];
				i++;
			}
			for (k = 0; k<days-1;k++){
				F[i] = R_next[k];
				i++;
			}
			this.A_price = A;
		}
		else{
			System.out.println("non c'è next month");
			Flight[] A_new = new Flight[A.length-days+1];
			System.out.println(A_new.length);
			for (i = 0; i<A_new.length;i++){
				A_new[i] = A[i];
			}
			
			i = 0;
			F = new Flight[A_new.length];
			for ( k = days-1;k<F.length+days-1;k++){
				F[i] = R[k];
				i++;
			}
			this.A_price = A_new;
			
		}
		this.R_price = F;
		
		this.days = days;
	}
	public Flight[] getA(){
		return A_price;
	}
	public Flight[] getR(){
		return R_price;
	}
	public int getDays(){
		return days;
	}
	
	
}