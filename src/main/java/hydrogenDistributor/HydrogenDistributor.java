package hydrogenDistributor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;



public class HydrogenDistributor {
	public static  HashSet<String> duplicateCheck = new HashSet<String>(); 
	public static  HashSet<String> distributions = new HashSet<String>(); 
	public static int hydrogens;
	public static boolean verbose = false;
	public static String hydrogeninfo=null;
	public static String atominfo= null;
	public static String filedir="";
	public static PrintWriter writer;
	public static Map<Character, Integer> valenceinfo; 
	static {
		//The atom valences from CDK.
		valenceinfo = new HashMap<Character, Integer>();
			
		valenceinfo.put('C', 4);
		valenceinfo.put('N', 5);
		valenceinfo.put('O', 2);
		valenceinfo.put('S', 6);
		valenceinfo.put('P', 5);
		valenceinfo.put('F', 1);
		valenceinfo.put('I', 7);
		valenceinfo.put('H', 1);
	}
	
	/**
	 * The function initialises the output file and calls starNbar function
	 * for the hydrogen distribution.
	 */
	
	public static HashSet<String> initialise(String atominfo, String hydrogeninfo, String filedir) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter(filedir+"HydrogenDistributorOutput.txt", "UTF-8");
		writer.println("The list of hydrogen distributions: ");
		hydrogens=Integer.parseInt(hydrogeninfo);
		List<Integer> array=new ArrayList<Integer>(Collections.nCopies(atominfo.length(), 0));
		if(verbose) {
			System.out.println("Distributing the "+hydrogens+" "+"hydrogens to the atoms..." );
		}
		starNbar(atominfo.length(),Integer.parseInt(hydrogeninfo),array,HydrogenDistributor.setValences(atominfo));
		for(String distribution:distributions) {
			System.out.println(distribution);
		}
		if(verbose) {
			writer.println("Number of possible distributions is :"+" "+distributions.size());
			System.out.println("Number of possible distributions is :"+" "+distributions.size());
		}
		writer.close();
		return HydrogenDistributor.distributions;
	}
	
	/**
	 * Function sets the formula based on the hydrogen distribution.
	 */
	
	public static String setFormula(String atominfo, List<Integer> distributions) {
		String formula="";
		for(int i=0;i<atominfo.length();i++) {
			if(distributions.get(i)!=0) {
				formula=formula+atominfo.charAt(i)+distributions.get(i);
			}else {
				formula=formula+atominfo.charAt(i);
			}
		}
		return formula;
	}
	
	/**
	 * Setting the valences of the given element symbols
	 */
	
	public static List<Integer> setValences(String atominfo){
		List<Integer> valences=new ArrayList<Integer>();
		for(int i=0;i<atominfo.length();i++) {
			valences.add(valenceinfo.get(atominfo.charAt(i)));
		}
		return valences;
	}
	
	/**
	 * Summation of the list of integers.
	 */
	
	public static int sum(List<Integer> list) {
		int sum =0;
		for(Integer i:list) {
			sum=sum+i;
		}
		return sum;
	}
	
	/**
	 * Ordering a string in an alphabetical order.
	 */
	
	public static String orderFormula(String formula) {
		String[] atominfo = formula.split("(?=[A-Z])");
        Arrays.sort(atominfo, new Comparator<String>() {
            public int compare(String string1, String string2) {
                String substring1 = string1.substring(0);
                String substring2 = string2.substring(0);
                return substring1.compareTo(substring2);
            }
        });
        String str = String.join(",", atominfo); //Cleaning "," from string
        str = str.substring(0, str.length()).replaceAll(",", "");
        return str;
	}
	/**
	 * In mathematics, the distribution of the hydrogen atoms is a chemical example of 'stars & bars'
	 * problem (especially in combinatorics). For instance, a+b+c=10.Here, 'stars and bars'
	 * approach calculates all the possible (a,b,c) triplets. For hydrogen distribution, the atom
	 * valences are the restrictions (or upper limits) for these three integers.
	 */
	
	public static void starNbar(int atoms, int hydrogen,List<Integer> list, List<Integer> valences) {
		if(atoms == 0){
			if(sum(list)==hydrogens) {
				String formula=setFormula(atominfo,list);
				if(!duplicateCheck.contains(orderFormula(formula))) {
					//writer.println(setFormula(atoms,list));
					distributions.add(orderFormula(formula));
					duplicateCheck.add(orderFormula(formula));
				}
			}
		}else if(atoms==1) {
			list.remove(0);
			int add2=hydrogen%valences.get(0);
			list.add(0, add2);
			starNbar(0,0,list,valences);
		}else { 
			for(int i=0;i<hydrogen+1;i++) {
				//the last atom has i stars, set them and recurse
				list.remove(atoms-1);
				int add=i%valences.get(atoms-1);
				list.add(atoms-1, add);
                starNbar(atoms-1,hydrogen-add,list,valences);
			}
		}
	}
	
	private void parseArguments(String[] arguments) throws ParseException
	{
		Options options = setupOptions(arguments);	
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, arguments);
			HydrogenDistributor.atominfo = cmd.getOptionValue("atominfo");
			HydrogenDistributor.hydrogeninfo=cmd.getOptionValue("hydrogens");
			HydrogenDistributor.filedir=HydrogenDistributor.filedir+cmd.getOptionValue("filedir");
			if (cmd.hasOption("verbose")) HydrogenDistributor.verbose = true;
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			HelpFormatter formatter = new HelpFormatter();
			formatter.setOptionComparator(null);
			String header = "\nFor a list of atoms, it calculates all the possible distribution of the given number of hydrogens.";
			String footer = "\nPlease report issues at https://github.com/MehmetAzizYirik/HydrogenDistributor";
			formatter.printHelp( "java -jar HydrogenDistributor.jar", header, options, footer, true );
			throw new ParseException("Problem parsing command line");
		}
	}
	
	private Options setupOptions(String[] arguments)
	{
		Options options = new Options();
		Option atominfo = Option.builder("a")
			     .required(true)
			     .hasArg()
			     .longOpt("atominfo")
			     .desc("String of element symbols (required)")
			     .build();
		options.addOption(atominfo);
		Option verbose = Option.builder("v")
			     .required(false)
			     .longOpt("verbose")
			     .desc("Print messages about the distributor")
			     .build();
		options.addOption(verbose);	
		Option hydrogens = Option.builder("h")
			     .required(true)
			     .hasArg()
			     .longOpt("hydrogens")
			     .desc("The number of hydrogens to distribute (required)")
			     .build();
		options.addOption(hydrogens);
		Option filedir = Option.builder("d")
			     .required(true)
			     .hasArg()
			     .longOpt("filedir")
			     .desc("The file directory to store the output (required)")
			     .build();
		options.addOption(filedir);
		return options;
	}
	public static void main(String[] arguments) throws FileNotFoundException, UnsupportedEncodingException {
		HydrogenDistributor distribution= null;
		String[] arguments1= {"-a","CCCCCC","-v","-h","15", "-d", "C:\\Users\\mehme\\Desktop\\"};
		try {
			distribution = new HydrogenDistributor();
			distribution.parseArguments(arguments1);
			HydrogenDistributor.initialise(HydrogenDistributor.atominfo, HydrogenDistributor.hydrogeninfo,HydrogenDistributor.filedir);
		} catch (Exception e) {
			// We don't do anything here. Apache CLI will print a usage text.
			if (HydrogenDistributor.verbose) e.getCause(); 
		}
	}

}