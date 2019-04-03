package hydrogenDistributor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class trial {
	public static IChemObjectBuilder builder =SilentChemObjectBuilder.getInstance();
	public static List<IAtomContainer> distributions = new ArrayList<IAtomContainer>();
	public static IMolecularFormula formula=null;
	public static IAtomContainer acontainer;
	public static Map<Integer, Integer> capacities;
	public static boolean verbose = false;
	public static int size;
	public static int isotopes;
	public static int[] capacity;
	public static int[] valences;
	public static int totalHydrogen;
	public static int[] totalAtom;
	public static int distHyd;
	
	static {
		//The atom valences from CDK.
		capacities = new HashMap<Integer, Integer>();
			
		capacities.put(6, 3);
		capacities.put(8, 1);
		capacities.put(16, 1);
		capacities.put(15, 2);
		capacities.put(1, 0);
		capacities.put(7, 2);
		capacities.put(9, 0);
		capacities.put(53, 0);
	}
	
	public static int[] addElement(int[] a, int e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
	
	public static void run(IMolecularFormula formula) throws CloneNotSupportedException {
		long startTime = System.nanoTime(); //Recording the duration time.
		int hydrogen=formula.getIsotopeCount(builder.newInstance(IIsotope.class, "H"));
		trial.isotopes=formula.getIsotopeCount()-1;
		formula.removeIsotope(builder.newInstance(IIsotope.class, "H"));
		IAtomContainer ac=MolecularFormulaManipulator.getAtomContainer(formula);
		trial.size=ac.getAtomCount();
		trial.acontainer=ac;
		setCapacity(formula);
		trial.totalHydrogen=hydrogen;
		int count=0;
		for(int[] dene:partition(totalHydrogen,isotopes,0)){
			LinkedList<List <int[]>> lists = new LinkedList<List <int[]>>();
			for(int i=0;i<dene.length;i++) {
					trial.distHyd=dene[i];
					List<int[]> iarrays= new ArrayList<int[]>();
					int[] array = new int[0];
					distribute(iarrays,dene[i],array,valences[i],totalAtom[i]);
					lists.add(iarrays);
			}			
			for(int[] combine:combineArrays(lists)) {
				//System.out.println(Arrays.toString(combine));
				count++;
			}
		}
		System.out.println(count);
		long endTime = System.nanoTime()- startTime;
        double seconds = (double) endTime / 1000000000.0;
		DecimalFormat d = new DecimalFormat(".###");
		System.out.println("Duration:"+" "+d.format(seconds));
	}
	
	public static int[] setCapacity(IMolecularFormula formula) {
		int[] capacity = new int[formula.getIsotopeCount()];
		int[] valences = new int[formula.getIsotopeCount()];
		int[] totalAtom = new int[formula.getIsotopeCount()];
		int i=0;
		for(IIsotope top:formula.isotopes()) {
			totalAtom[i]=formula.getIsotopeCount(top);
			valences[i]=capacities.get(top.getAtomicNumber());
			capacity[i]=capacities.get(top.getAtomicNumber())*formula.getIsotopeCount(top);
			i++;
		}
		trial.capacity=capacity;
		trial.valences=valences;
		trial.totalAtom=totalAtom;
		return capacity;
	}
	
	public static List<int[]> partition(int n, int d,int depth) {
		if(d==depth) {
			List<int[]> array= new ArrayList<int[]>();
			int[] take=new int[0];
			array.add(take);
			return array;
		}
		return buildArray(n,d,depth);
		
	}
	
	public static List<int[]> buildArray(int n,int d, int depth){
		List<int[]> array= new ArrayList<int[]>();
		IntStream range = IntStream.rangeClosed(0,n);
		for(int i:range.toArray()) {
			for(int[] item: partition(n-i,d,depth+1)) {
				if(i<=capacity[item.length]) {
					item=addElement(item,i);
			        if(item.length==d) {
			        	if(sum(item)==totalHydrogen) {
			        		array.add(item);
			        	}
			        }else {
			        	array.add(item);
			        }
				}
			}
		}
		return array;
	}
	public static int[] addZeros(int[] array, int zeros) {
		for(int i=0;i<zeros;i++) {
			array=addElement(array,0);
		}
		return array;
	}
	
	public static void distribute(List<int[]> arrays,int hydrogen,int[]arr,int valence, int numAtom) throws CloneNotSupportedException {
		if(hydrogen==0 && sum(arr)==distHyd){
			if(arr.length!=numAtom) {
				arr=addZeros(arr,(numAtom-arr.length));
			}
			arrays.add(arr);
			//System.out.println(Arrays.toString(arr));
		}else if((numAtom-arr.length)==1) {
			int add=Math.min(hydrogen,valence);
			hydrogen=hydrogen-add;
			if(arr.length==0) {
				distribute(arrays,0,addElement(arr,add),valence,numAtom); 
			}
			if((arr.length)>0) {
				if(arr[arr.length-1]<=add){ 
					distribute(arrays,0,addElement(arr,add),valence,numAtom);
				}
			}
		}else { 
			for(int i = Math.min(valence,hydrogen); i > 0; i--) {	
				if(arr.length==0) {
					distribute(arrays,(hydrogen-i),addElement(arr,i),valence,numAtom); 
				}
				if((arr.length)>0) {
					if(arr[arr.length-1]<=i){ 
						distribute(arrays,(hydrogen-i),addElement(arr,i),valence,numAtom);
					}
				}
			}
		}
	}
	public static int sum(int[] array) {
		int sum=0;
		for(int i=0;i<array.length;i++) {
			sum=sum+array[i];
		}
		return sum;
	}
	
	public static int[] mergeArrays(List<int[]> arrays) {
		int size = 0;
		for (int[] array : arrays) {
			size += array.length;
		}
		int[] mergedArray = new int[size];
		int index = 0;
		for (int[] array : arrays) {
			for (int i : array) {
				mergedArray[index++] = i;
		    }
		}
		return mergedArray;
	}
	
	
	public static int[] arraySum(int[] a, int[] b) {
		List<int[]> arrays= new ArrayList<int[]>();
		arrays.add(a);
		arrays.add(b);
		return mergeArrays(arrays);
	}
	
	public static List<List<int[]>> buildLists(int n){
		List<List<int[]>> lists= new ArrayList<List<int[]>>();
		for (int i=0; i<n; ++i) {
			List<int[]> ilist= new ArrayList<int[]>();
			lists.add(ilist);
		}
		return lists;
	}
	
	
	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	
	public static List<int[]> combineArrays(LinkedList<List <int[]>> lists) {
		List<int[]> comb = new ArrayList<int[]>();
	    for (int[] s: lists.removeFirst()) {
	    	comb.add(s);
	    }
	    while (!lists.isEmpty()) {
	        List<int[]> list = lists.removeFirst();
	        List<int[]> newComb =  new ArrayList<int[]>();
	        for (int[] arr1: comb) { 
	            for (int[] arr2 : list) { 
	            	newComb.add(arraySum(arr1,arr2));
	            }
	        }

	        comb = newComb;
	    }
	    return comb;
	}
	
	public static void main(String[] args) throws CloneNotSupportedException {
		IMolecularFormula formula=MolecularFormulaManipulator.getMolecularFormula("C4H7NO2S", builder);
		run(formula);
	}

}
