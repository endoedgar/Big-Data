import java.util.List;

public class GroupByPair<K,V extends List<?>> {
	private K key;
	private V list;
	
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getList() {
		return list;
	}
	public void setList(V list) {
		this.list = list;
	}
	public GroupByPair(K key, V list) {
		super();
		this.key = key;
		this.list = list;
	}
	@Override
	public String toString() {
		return "< " + key + " , " + list.toString() + " >";
	}
}
