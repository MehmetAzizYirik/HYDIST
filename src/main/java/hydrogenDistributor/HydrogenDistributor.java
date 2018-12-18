package hydrogenDistributor;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.qsar.descriptors.molecular.AtomCountDescriptor;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;


public class HydrogenDistributor {
	public static IChemObjectBuilder builder =SilentChemObjectBuilder.getInstance();
	public static List<IAtomContainer> distributions = new ArrayList<IAtomContainer>();
	public static IMolecularFormula formula=null;
	public static IAtomContainer acontainer;
	public static Map<Integer, Integer> capacities;
	public static boolean verbose = false;
	public static int size;
	public static int isotopes;
	public static int[] capacity;
	
	static {
		//The atom valences from CDK.
		capacities = new HashMap<Integer, Integer>();
			
		capacities.put(6, 3);
		capacities.put(7, 4);
		capacities.put(8, 1);
		capacities.put(16, 5);
		capacities.put(15, 4);
		capacities.put(9, 0);
		capacities.put(53, 6);
		capacities.put(1, 0);
	}
	
	/**
	 * The basic functions used in the hydrogen distributor.
	 */
	
	/**
	 * Function sets the maximum capacities of the atoms. The maximum capacity is 1 less than the atom's valence.
	 * If the valence was considered as the max, we would have already saturated atoms.
	 */
	
	public static int[] setCapacity(IAtomContainer ac) {
		int[] capacity = new int[ac.getAtomCount()];
		for(int i=0;i<ac.getAtomCount();i++) {
			capacity[i]=capacities.get(ac.getAtom(i).getAtomicNumber());
		}
		HydrogenDistributor.capacity=capacity;
		return capacity;
	}
	
	/**
	 * To order the atoms in the atomcontainer
	 */
	
	public static IAtomContainer orderAtoms(IAtomContainer ac) {
		IAtom temp;
		IAtom[] atoms=AtomContainerManipulator.getAtomArray(ac);
		for (int i = 0; i < atoms.length; i++){
            for (int j = i + 1; j < atoms.length; j++){
                if (capacities.get(atoms[i].getAtomicNumber()) > capacities.get(atoms[j].getAtomicNumber())) {
                    temp = atoms[i];
                    atoms[i] = atoms[j];
                    atoms[j] = temp;
                }
            }
        }
		IAtomContainer ac2= new AtomContainer();
		for(int i=0;i<atoms.length;i++) {
			ac2.addAtom(atoms[i]);
		}
		return ac2;
	}
	
	/**
	 * To initialise the inputs and record the duration time.
	 * @throws CDKException 
	 */
	
	public static List<IAtomContainer> initialise(IMolecularFormula formula) throws FileNotFoundException, UnsupportedEncodingException, CloneNotSupportedException, CDKException {
		long startTime = System.nanoTime(); //Recording the duration time.
		int hydrogen=formula.getIsotopeCount(builder.newInstance(IIsotope.class, "H"));
		HydrogenDistributor.isotopes=formula.getIsotopeCount()-1;
		if(verbose) {
			System.out.println("For molecular formula "+ MolecularFormulaManipulator.getString(formula)+" "+",calculating all the possible distributions of "+hydrogen+" "+"hydrogens ..." );
		}
		formula.removeIsotope(builder.newInstance(IIsotope.class, "H"));
		IAtomContainer ac=MolecularFormulaManipulator.getAtomContainer(formula);
		HydrogenDistributor.size=ac.getAtomCount();
		if(formula.getIsotopeCount()>1) {
			ac=orderAtoms(ac);
		}
		HydrogenDistributor.acontainer=ac;
		setCapacity(ac);
		int[] array = new int[0];
		distribute(hydrogen,array);
		if(verbose) {
			System.out.println("Number of possible distributions is :"+" "+distributions.size());
		}
		long endTime = System.nanoTime()- startTime;
        double seconds = (double) endTime / 1000000000.0;
		DecimalFormat d = new DecimalFormat(".###");
		if(verbose) {
			System.out.println("Duration:"+" "+d.format(seconds));
		}
		return HydrogenDistributor.distributions;
	}
	
	/**
	 * Function sets the implicit hydrogens of the atoms.
	 */
	
	public static IAtomContainer setHydrogens(IAtomContainer ac,int[] distribution) throws CloneNotSupportedException {
		IAtomContainer ac2=ac.clone();
		for(int i=0;i<distribution.length;i++) {
			ac2.getAtom(i).setImplicitHydrogenCount(distribution[i]);
		}
		return ac2;
	}
	
	
	
	public static int zeros(IAtomContainer ac, int[] arr) throws CDKException {
		List<String> types= new ArrayList<String>();
		int zero2=0;
		int zero=0;
		for(int i=0;i<arr.length;i++) {
			if(!types.contains(ac.getAtom(i).getSymbol())) {
				types.add(ac.getAtom(i).getSymbol());
				zero=zero+countAtomType(ac,ac.getAtom(i).getSymbol());
				if(!checkZeros(arr,zero)) {
					zero2+=zero;
					break;
				}
			}
		}
		return zero2;
	}
	
	public static boolean checkZeros(int[] arr, int limit) throws CDKException {
		boolean zeros=true;
		for(int i=0;i<limit;i++) {
			if(arr[i]!=0) {
				zeros=false;
				break;
			}
		}
		return zeros;
	}
	
	public static int countAtomType(IAtomContainer ac, String symbol) throws CDKException {
		Object[] param = { symbol };
		AtomCountDescriptor atomCounter= new AtomCountDescriptor();
		atomCounter.setParameters(param);
		DescriptorValue value = atomCounter.calculate(ac);
		int result=((IntegerResult) value.getValue()).intValue();
		return result;
	};
	
	/** 
	 * Distributes the hydrogens to the atoms in all the possible ways.
	 * @throws CDKException 
	 */
	
	public static void distribute(int hydrogen,int[]arr) throws CloneNotSupportedException, CDKException {
		if(hydrogen == 0){
			if(isotopes==1 && arr[0]!=0) {
				/**
				 * If the array starts with 0, the size should be longer than number of carbons.
				 * Otherwise, it would be the symmetric distribution of hydrogens into carbons
				 * like {3,3,0,0,0,0} and {0,0,0,0,3,3}.
				 */
				distributions.add(setHydrogens(acontainer,arr));
			}else if(isotopes>1 && arr.length>=zeros(acontainer,arr)) {
				distributions.add(setHydrogens(acontainer,arr));
			}
		}else if((size-arr.length)==1) {	
			int add=Math.min(hydrogen,capacity[arr.length]); //Just to add maximum the capacity not all the remaining carbons.
			if(acontainer.getAtom(size-2).getSymbol()!=acontainer.getAtom(size-1).getSymbol()) {	
				distribute(0,addElement(arr,add));
			}else {
				if(arr[arr.length-1]<=add){
					distribute(0,addElement(arr,add));
				}
			}
		}else { 
			for(int i = Math.min(capacity[arr.length],hydrogen); i >= findMin(hydrogen,arr,arr.length); i--) {				
				if(arr.length==0) {
					distribute((hydrogen-i),addElement(arr,i)); //First step to add a number to the empty array.
				}
				if((arr.length)>0) {
					if(arr[arr.length-1]<=i){ // It extends the array in ascending order. So no need to check duplicates
						distribute((hydrogen-i),addElement(arr,i));
					}
				}
			}
		}
	}
	
	/**
	 * Just adding element to an array
	 */
	
	public static int[] addElement(int[] a, int e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
	
	/**
	 * To decide at least how many hydrogen distributed to an atom, the capacities of the next ones are calculated.
	 */
	
	public static int otherCapacities(int index) {
		int count=0;
		for(int i=index+1;i<capacity.length;i++) {
			count=count+capacity[i];
		}
		return count;
	}
	
	/**
	 * Based on the capacities of the following atoms, the minimum number of hydrogens to add to an atom is calculated.
	 */
	
	public static int findMin(int hydrogens,int[] arr, int index) {
		int min=0;
		if(hydrogens>otherCapacities(index)) {
			min=min+(hydrogens-otherCapacities(index));
		}
		return min;
	}
	
	void parseArguments(String[] arguments) throws ParseException
	{
		Options options = setupOptions(arguments);	
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, arguments);
			String formula = cmd.getOptionValue("formula");
			HydrogenDistributor.formula=MolecularFormulaManipulator.getMolecularFormula(formula, builder);
			if (cmd.hasOption("verbose")) HydrogenDistributor.verbose = true;
		
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setOptionComparator(null);
			String header = "\nFor a molecular formula, it calculates all the possible hydrogen distributions to the atoms.";
			String footer = "\nPlease report issues at https://github.com/MehmetAzizYirik/HydrogenDistributor";
			formatter.printHelp( "java -jar HydrogenDistributor.jar", header, options, footer, true );
			throw new ParseException("Problem parsing command line");
		}
	}
	
	private Options setupOptions(String[] arguments)
	{
		Options options = new Options();
		Option formula = Option.builder("f")
			     .required(true)
			     .hasArg()
			     .longOpt("formula")
			     .desc("Molecular Formula (required)")
			     .build();
		options.addOption(formula);	
		Option verbose = Option.builder("v")
			     .required(false)
			     .longOpt("verbose")
			     .desc("Print messages about the distributor")
			     .build();
		options.addOption(verbose);	
		return options;
	}
	public static void main(String[] arguments) throws FileNotFoundException, UnsupportedEncodingException {
		HydrogenDistributor distribution= new HydrogenDistributor();//C78H94N4O12
		//String[] arguments1= {"-f","C78H94N4O12","-v"};
		try {
			distribution.parseArguments(arguments);
			HydrogenDistributor.initialise(HydrogenDistributor.formula);
		} catch (Exception e) {
			if (HydrogenDistributor.verbose) e.getCause(); 
		}
	}
}