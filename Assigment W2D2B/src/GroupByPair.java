import java.util.List;

public class GroupByPair<K,V> {
	private K key;
	private List<V> list;
	
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public List<V> getList() {
		return list;
	}
	public void setList(List<V> list) {
		this.list = list;
	}
	public GroupByPair(K key, List<V> list) {
		super();
		this.key = key;
		this.list = list;
	}
	@Override
	public String toString() {
		return "< " + key + " , " + list.toString() + " >";
	}
}
