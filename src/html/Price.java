package html;

public class Price {
	private Flight[] A;
	private int min;
	public Price(Flight[] a){
		this.A = a;
		int m = 10000;
		for (int i = 0; i<A.length;i++){
			if (A[i].getPrice()<m){
				m = A[i].getPrice();
			}
		}
		this.min = m;
	}
	public int getMin(){
		return min;
	}
	public Flight[] getFlight(){
		return A;
	}
}
