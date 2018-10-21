package hydist;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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



public class hydist {
	public static  HashSet<List<Integer>> lists = new HashSet<List<Integer>>(); 
	public static int hydrogens;
	public static boolean verbose = false;
	static String hyd=null;
	static String atoms= null;
	static String filedir="";
	public static PrintWriter writer;
	public static Map<Character, Integer> cdkval; 
	static {
		//The atom valences from CDK.
		cdkval = new HashMap<Character, Integer>();
			
		cdkval.put('C', 4);
		cdkval.put('N', 5);
		cdkval.put('O', 2);
		cdkval.put('S', 6);
		cdkval.put('P', 5);
		cdkval.put('F', 1);
		cdkval.put('I', 7);
		cdkval.put('H', 1);
	}
	
	/**
	 * The function initializes the output file and calls starnbar function
	 * for the hydrogen distributions.
	 */
	
	public static void intnrun(String atoms, String hyd, String filedir) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter(filedir+"distoutput.txt", "UTF-8");
		writer.println("The list of distributions: ");
		hydrogens=Integer.parseInt(hyd);
		List<Integer> array=new ArrayList<Integer>(Collections.nCopies(atoms.length(), 0));
		if(verbose) {
			System.out.println("Distributing the "+hydrogens+" "+"to the atoms..." );
		}
		starnbar(atoms.length(),Integer.parseInt(hyd),array,hydist.valences(atoms));
		if(verbose) {
			writer.println("Number of possible distributions is :"+" "+lists.size());
			System.out.println("Number of possible distributions is :"+" "+lists.size());
		}
		writer.close();
	}
	
	/**
	 * Function sets the formula based on the hydrogen distribution.
	 */
	
	public static String setformula(String atoms, List<Integer> dist) {
		String formula="";
		for(int i=0;i<atoms.length();i++) {
			if(dist.get(i)!=0) {
				formula=formula+atoms.charAt(i)+dist.get(i);
			}else {
				formula=formula+atoms.charAt(i);
			}
		}
		return formula;
	}
	/**
	 * Setting the valences of the given element symbols
	 */
	
	public static List<Integer> valences(String atoms){
		List<Integer> vlncs=new ArrayList<Integer>();
		for(int i=0;i<atoms.length();i++) {
			//System.out.println(formula.charAt(i)+" "+cdkval.get(formula.charAt(i)));
			vlncs.add(cdkval.get(atoms.charAt(i)));
		}
		return vlncs;
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
	 *In mathematics, the distribution of the hydrogens atoms is a 'stars & bars'
	 * problem (especially in combinatorics). For instance, a+b+c=10.Here, 'stars and bars'
	 * approach calculates all the possible (a,b,c) triplets. For hydrogen case,
	 * the atom valences are the restrictions or upper limits for these three integers.
	 */
	
	public static void starnbar(int n, int k,List<Integer> list, List<Integer> valences) {
	//n atoms, k hydrogens, list holds how many hydrogens in current assignment
		if(n == 0){
			if(sum(list)==hydrogens) {
				if(!lists.contains(list)) {
					setformula(atoms,list);
					writer.println(setformula(atoms,list));
					lists.add(list);
				}
			}
		}else if(n==1) {
			list.remove(0);
			int add2=k%valences.get(0);
			list.add(0, add2);
			starnbar(0,0,list,valences);
		}else { 
			for(int i=0;i<k+1;i++) {
				//the last atom has i stars, set them and recurse
				list.remove(n-1);
				int add=i%valences.get(n-1);
				list.add(n-1, add);
                starnbar(n-1,k-add,list,valences);
			}
		}
	}
	private void parseArgs(String[] args) throws ParseException
	{
		Options options = setupOptions(args);	
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			hydist.atoms = cmd.getOptionValue("atoms");
			hydist.hyd=cmd.getOptionValue("hydrogens");
			hydist.filedir=hydist.filedir+cmd.getOptionValue("filedir");
			if (cmd.hasOption("verbose")) hydist.verbose = true;
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			HelpFormatter formatter = new HelpFormatter();
			formatter.setOptionComparator(null);
			String header = "\nFor a list of atoms, it calculates all the possible distribution of the given number of hydrogens.";
			String footer = "\nPlease report issues at https://github.com/MehmetAzizYirik";
			formatter.printHelp( "java -jar hydist.jar", header, options, footer, true );
			throw new ParseException("Problem parsing command line");
		}
	}
	
	private Options setupOptions(String[] args)
	{
		Options options = new Options();
		Option atoms = Option.builder("a")
			     .required(true)
			     .hasArg()
			     .longOpt("atoms")
			     .desc("String of element symbols (required)")
			     .build();
		options.addOption(atoms);
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
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		hydist inp= null;
		String[] args1= {"-a","CCCCCC","-v","-h","12", "-d", "C:\\Users\\mehme\\Desktop\\"};
		try {
			inp = new hydist();
			inp.parseArgs(args1);
			hydist.intnrun(hydist.atoms, hydist.hyd,hydist.filedir);
		} catch (Exception e) {
			// We don't do anything here. Apache CLI will print a usage text.
			if (hydist.verbose) e.getCause(); 
		}
		
	}

}