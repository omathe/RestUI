package restui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<>();
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		list.add(i1);
		list.add(i2);
		list.add(i3);

		Map<Integer, Integer> map = new HashMap<>();
		map.put(i1, 10);
		map.put(i2, 10);
		map.put(i3, 10);

		System.out.println(map.size());
		list.remove(0);
		System.err.println(map.size());



	}

}
