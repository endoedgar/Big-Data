
public class KeyValuePair<T, I> {
	private T t;
	private I i;
	
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	public I getI() {
		return i;
	}
	public void setI(I i) {
		this.i = i;
	}
	public KeyValuePair(T t, I i) {
		super();
		this.t = t;
		this.i = i;
	}
	@Override
	public String toString() {
		return "(" + t + ", " + i + ")";
	}
	
}
