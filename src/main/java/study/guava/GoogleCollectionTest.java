package study.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;

//静态导入，导入Maps这个类中所有的静态方法，也可以指定导入静态方法的名称
//import static com.google.common.collect.Maps.*;
import static com.google.common.collect.Maps.newHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set; 

/**
 * 
 * @author zhouyelin
 * @Date 2018年9月5日
 * @desc https://www.cnblogs.com/SummerinShire/p/6054983.html
 *
 */
public class GoogleCollectionTest {

	/**
	 * 传统读取文件方式
	 */
	public void readFile() {
		File file = new File(getClass().getResource("/test.txt").getFile());
		BufferedReader reader;
		String text = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				text += line.trim() + "\n";
			}
			reader.close();
			reader = null;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("readFile : " + text);
	}
	
	/**
	 * 使用Guava中工具类读取文件
	 */
	public void readFileWithGuava(){
		File file = new File(getClass().getResource("/test.txt").getFile());
		List<String> lines = null;
		try {
			lines = Files.readLines(file, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("readFileWithGuava : " + lines);
	}
	
	/**********************************************************************************/
	private class LengthLessThanPredicate implements Predicate<String> {
		private final int length;

		private LengthLessThanPredicate(final int length) {
			this.length = length;
		}

		public boolean apply(final String s) {
			return s.length() < length;
		}
	}
	
	private class EqualToPredicate implements Predicate<String>{
		private final String target;
		
		private EqualToPredicate(final String target){
			this.target = target;
		}
		@Override
		public boolean apply(String input) {
			return target.equals(input);
		}
		
	}
	
	private class OrPredicate implements Predicate<String>{
		private final Predicate<String> str1, str2;
		
		private OrPredicate(final Predicate<String> str1, final Predicate<String> str2){
			this.str1 = str1;
			this.str2 = str2;
		}
		@Override
		public boolean apply(String input) {
			return str1.apply(input) || str2.apply(input);
		}
		
	}
	
	public Predicate<String> lengthLessThan(final int length) {
		return new LengthLessThanPredicate(length);
	}
	
	public Predicate<String> equalTo(final String target) {
		return new EqualToPredicate(target);
	}
	
	public Predicate<String> or(final Predicate<String> str1, final Predicate<String> str2) {
		return new OrPredicate(str1, str2);
	}
	
	public Iterable<String> filter(List<String> names, Predicate<String> filter) {
		//names.removeIf((java.util.function.Predicate<? super String>) filter);
		return names;
	}
	
	/**********************************************************************************/
	private class Person implements Iterable{
		private String firstName;
		private String lastName;
		private int age;
		private int sex;
		/**
		 * @return the firstName
		 */
		public String getFirstName() {
			return firstName;
		}
		/**
		 * @param firstName the firstName to set
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		/**
		 * @return the lastName
		 */
		public String getLastName() {
			return lastName;
		}
		/**
		 * @param lastName the lastName to set
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		/**
		 * @return the age
		 */
		public int getAge() {
			return age;
		}
		/**
		 * @param age the age to set
		 */
		public void setAge(int age) {
			this.age = age;
		}
		/**
		 * @return the sex
		 */
		public int getSex() {
			return sex;
		}
		/**
		 * @param sex the sex to set
		 */
		public void setSex(int sex) {
			this.sex = sex;
		}
		@Override
		public Iterator iterator() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**********************************************************************************/
	public static void main(String[] args) {
		GoogleCollectionTest test = new GoogleCollectionTest();
		/****************************IO*****************************/
		test.readFile();
		test.readFileWithGuava();
		
		/****************************Google Collections(集合)*****************************/
		//Map<String, Map<Long, List<String>>> map = Maps.newHashMap();
		Map<String, Map<Long, List<String>>> map = newHashMap();
		List<String> list = Lists.newArrayList();
		Set<String> set = Sets.newHashSet();
		
		ImmutableList<String> imlist = ImmutableList.of("a", "b", "c", "d");
		ImmutableMap<String,String> immap = ImmutableMap.of("key1", "value1", "key2", "value2");
		List<Integer> list1 = Lists.newArrayList(1, 2, 3, 4);
		
		/****************************primitives(基础类型)*****************************/
		int[] array = Ints.toArray(list1);
		int compare = Ints.compare(1, 1); // 相等返回0,不相等返回-1
		System.out.println(compare);
		
		//判断array数组中是否有特定的整型数字1
		boolean contains = Ints.contains(array, 1);
		//获取array数组中整型数字1的下标
		int indexOf = Ints.indexOf(array, 1);
		//获取array数组中的最大值
		int max = Ints.max(array);
		//获取array数组中的最小值
		int min = Ints.min(array);
		int[] array2 = {1,7,8,9};
		// 合并数组
		int[] concat = Ints.concat(array, array2);
		String s = String.format("contains : %b, indexOf : %d, max : %d, min : %d, concat : %s", contains, indexOf, max, min, Ints.join("-", concat));
		System.out.println(s); // contains : true, indexOf : 0, max : 4, min : 1, concat : 1-2-3-4-1-7-8-9
		
		
		/****************************CharMatcher(字符)*****************************/
		// 从字符串中得到所有的数字
		String string = CharMatcher.digit().retainFrom("some text 89983 and more");
		System.out.println(string); //89983
		// 把字符串中的数据都去掉
		String string2 = CharMatcher.digit().removeFrom("some text 89983 and more");
		System.out.println(string2); //some text  and more
		
		/****************************Joiner and Splitter(连接和分隔器)*****************************/
		String[] subdirs = { "usr", "local", "lib" };
		String directory = Joiner.on("/").join(subdirs);
		System.out.println(directory); // usr/local/lib
		
		int[] numbers = { 1, 2, 3, 4, 5 };
		String numbersAsString = Joiner.on(";").join(Ints.asList(numbers));
		System.out.println(numbersAsString); // 1;2;3;4;5
		
		String numbersAsStringDirectly = Ints.join(";", numbers);
		System.out.println(numbersAsStringDirectly); // 1;2;3;4;5
		
		String testString = "1, 2, 3, ,, 4, 5, ";
		Iterable<String> split = Splitter.on(",").split(testString);
		
		//输出结果：1 2 3  4 5 
		split.forEach((item)->System.out.print(item));
		
		String[] splitRegular = testString.split(",");
		System.out.println(splitRegular);

		//输出结果：12345
		Iterable<String> split2 = Splitter.on(",").omitEmptyStrings().trimResults().split(testString);
		split2.forEach((item)->System.out.print(item));
		
		/****************************GOOGLE COLLECTIONS(函数化)*****************************/
		/**Functions 函数化**/
		Map<String, Double> eurPriceMap = ImmutableMap.of("a", 1.2d, "b", 1.0d);
		Map<String, Double> usdPriceMap = Maps.transformValues(eurPriceMap, new Function() {
			double eurToUsd = 1.4888;

			@Override
			public Object apply(Object value) {
				return Double.valueOf(value.toString()) * eurToUsd;
			}
		});
		System.out.println(usdPriceMap); //{a=1.78656, b=1.4888}
		
		/**Filtering 使用条件过滤集合**/
		List<String> names = ImmutableList.of("Aleksander", "Jaran", "Integrasco", "Guava", "Java");
		//返回名字长度小于5的列表
		Iterable<String> filtered = test.filter(names, test.or(test.or(test.equalTo("Aleksander"),test.equalTo("Jaran")), test.lengthLessThan(5)));
		
		
		
		/**Ordering 对集合排序**/
		//根据lastName排序
		Comparator<Person> byLastName = new Comparator<Person>() {
			public int compare(final Person p1, final Person p2) {
				return p1.getLastName().compareTo(p2.getLastName());
			}
		};
		//根据firstName排序
		Comparator<Person> byFirstName = new Comparator<Person>() {
			public int compare(final Person p1, final Person p2) {
				return p1.getFirstName().compareTo(p2.getFirstName());
			}
		};
		//Person[] persons = {new Person(), new Person()};
		//先根据lastName排序，再根据firstName排序，然后对排序结果反序
		//List<Person> sortedCopy = Ordering.from(byLastName).compound(byFirstName).reverse().sortedCopy(persons);
		//List<Person> sortedCopy2 = orderByLastName.compound(orderByFirstName).reverse().sortedCopy(persons);
		
		/****************************Set的并集、差集、交集*****************************/
		HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
		HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);
		//两个Set的并集
		SetView<Integer> union = Sets.union(setA, setB);
		System.out.println("union:");
		for (Integer integer : union) //12345867
		    System.out.println(integer);        

		//两个Set的差集
		SetView<Integer> difference = Sets.difference(setA, setB);
		System.out.println("difference:");
		for (Integer integer : difference) //123
		    System.out.println(integer);       

		//两个Set的交集
		SetView<Integer> intersection = Sets.intersection(setA, setB);
		System.out.println("intersection:");
		for (Integer integer : intersection) //45
		    System.out.println(integer);
		
		/****************************Map的相关操作*****************************/
		Map<String, Integer> mapA = ImmutableMap.of("a", 1, "b", 2, "c", 3, "m", 4, "n", 6);
		Map<String, Integer> mapB = ImmutableMap.of("x", 7, "y", 8, "z", 9, "m", 5, "n", 6);
		MapDifference<String, Integer> differenceMap = Maps.difference(mapA, mapB);
		boolean areEqual = differenceMap.areEqual();
		System.out.println("differenceMap.areEqual:" + areEqual); //differenceMap.areEqual:false
		
		//获取两个map中共同key的值
		Map<String, ValueDifference<Integer>> entriesDiffering = differenceMap.entriesDiffering();
		System.out.println("entriesDiffering:");
		for(Entry<String, ValueDifference<Integer>> entry : entriesDiffering.entrySet())
			System.out.println(entry); //m=(4, 5)
		
		//以左侧Map为基准，获取左侧Map中独有的key及其value
		Map<String, Integer> entriesOnlyOnLeft = differenceMap.entriesOnlyOnLeft();
		System.out.println("entriesOnlyOnLeft:");
		for(Entry<String, Integer> entry : entriesOnlyOnLeft.entrySet())
			System.out.println(entry); // a=1   b=2   c=3
		
		//以右侧Map为基准，获取右侧Map中独有的key及其value
		Map<String, Integer> entriesOnlyOnRight = differenceMap.entriesOnlyOnRight();
		System.out.println("entriesOnlyOnRight:");
		for(Entry<String, Integer> entry : entriesOnlyOnRight.entrySet())
			System.out.println(entry); // x=7   y=8   z=9
		
		//获取两个map中key和value都相同的子集
		Map<String, Integer> entriesInCommon = differenceMap.entriesInCommon();
		System.out.println("entriesInCommon:");
		for(Entry<String, Integer> entry : entriesInCommon.entrySet())
			System.out.println(entry); // n=6
		
		/****************************Preconditions（前置条件）*****************************/
		int count = -1;
		try {
			//Preconditions.checkArgument(count > 0, "must be positive: %s", count); // Exception in thread "main" java.lang.IllegalArgumentException: must be positive: -1 ...
		} catch (IllegalArgumentException e) {
			
		}
		String entry = "test";
		String notNull = Preconditions.checkNotNull(entry, "must be not null");
		System.out.println(notNull); // test
		try {
			//Preconditions.checkNotNull(null, "must be not null"); // Exception in thread "main" java.lang.NullPointerException: must be not null ...
		} catch (IllegalArgumentException e) {
			
		}
		
		/****************************Multimap（可实现一个key对应多个value）*****************************/
		Multimap<String, Integer> multimap = ArrayListMultimap.create();
		multimap.put("test", 1);
		multimap.put("test", 2);
		multimap.put("test01", 3);
		for(Entry<String, Integer> mentry : multimap.entries()) //test=1   test=2   test01=3
			System.out.println(mentry);
		
		Multimap<String, Integer> multimap2 = ImmutableMultimap.of("test", 1, "test", 2, "test01", 3);
		for(Entry<String, Integer> mentry2 : multimap2.entries()) //test=1   test=2   test01=3
			System.out.println(mentry2);
		
		/****************************分片集合*****************************/
		/*
		 * 使用场景：我们假设我们已经拥有了包含了一组map的list。list里的每一个Map代表拥有指定属性的一个文档，这个Map看起来可能会是下面这个样子：
mapOf("type", "blog", "id", "292", "author", "john");
即每个Map中我们拥有3个属性，他们分别是“type”、 “id”和“author”。
如上所示，所有我们的List看起来应该是下面这个样子：
List<Map<String, String>> listOfMaps
　　现在，我们想把这个list根据所装载对象的类型不同分成多个list，比如一个叫“blog”，一个叫“news”等等...
　　如果没有Google Collections这将是一场恶梦！我们必须得先循环这个list，然后再分别检查每一个map中的key，然后再把根据类型的不同放入不同的list中。但如果我们不知道map里都有哪些类型，这个过程会更痛苦！
想不想和我一起来看看有没有轻松的办法解决？
用一点点Function的魔法加上Multimaps，我样可以以一种上相当优雅的方式来解决这个问题（代码如下），现在我们拥有了每一个key代表不同类型的Multimaps了！
　　如果现在我们想要指定类型的所有map，唯一需要做的就是找Multimaps要！
		 */
		Map<String, String> map1 = ImmutableMap.of("type", "blog", "id", "292", "author", "john");
		Map<String, String> map2 = ImmutableMap.of("type", "news", "id", "293", "author", "andy");
		Map<String, String> map3 = ImmutableMap.of("type", "blog", "id", "294", "author", "jim");
		Map<String, String> map4 = ImmutableMap.of("type", "news", "id", "295", "author", "jack");
		Map<String, String> map5 = ImmutableMap.of("type", "blog", "id", "296", "author", "tom");
		List<Map<String, String>> listOfMaps = ImmutableList.of(map1, map2, map3, map4, map5);
		Multimap<String, Map<String, String>> partitionedMap = Multimaps.index(listOfMaps, new Function<Map<String, String>, String>() {
		    public String apply(final Map<String, String> from) {
		        return from.get("type");
		    }
		});
		/*
		 * 输出结果：
		 * blog={type=blog, id=292, author=john}
		 * blog={type=blog, id=294, author=jim}
		 * blog={type=blog, id=296, author=tom}
		 * news={type=news, id=293, author=andy}
		 * news={type=news, id=295, author=jack}
		 */
		for(Entry<String, Map<String, String>> entry3 : partitionedMap.entries())
			System.out.println(entry3);
		
		partitionedMap.keySet().forEach((key) -> {
			//输出结果：blog  blog  blog  news  news
			System.out.println(key);
			//输出结果：[{type=blog, id=292, author=john}, {type=blog, id=294, author=jim}, {type=blog, id=296, author=tom}]
			//	[{type=news, id=293, author=andy}, {type=news, id=295, author=jack}]
			System.out.println(partitionedMap.get(key));
		});
		
		partitionedMap.keySet().forEach((key) -> {
			//输出结果：blog  news
			System.out.println(key);
			//输出结果：[{type=blog, id=292, author=john}, {type=blog, id=294, author=jim}, {type=blog, id=296, author=tom}]  
			// 	[{type=news, id=293, author=andy}, {type=news, id=295, author=jack}]
			System.out.println(partitionedMap.get(key));
		});
	}

	
}
