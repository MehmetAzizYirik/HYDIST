package hydist;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class test_hydist extends hydist {
	public  String Desktop=(System.getProperty("user.home") + "\\Desktop").replace("\\", "/");
	
	@Test
	
	/**
	 * Setting the valences of the given element symbols
	 */
	public void test_valences() {
		String atoms= "CHONS";
		List<Integer> output= new ArrayList<Integer>(Arrays.asList(4,1,2,5,6));
		assertEquals(valences(atoms),output);
	}
	
	@Test
	
	/**
	 *The distribution of the hydrogens atoms is called 'stars & bars' problem in combinatorics.  
	 */
	public void test_starnbar() throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter(Desktop+"distoutput.txt", "UTF-8"); //The output file.
		String atoms="CCCCCC";
		hydist.atoms=atoms; //The atoms are set in the class.
		hydist.hydrogens=12; //The number of hydrogens.
		List<Integer> array=new ArrayList<Integer>(Collections.nCopies(atoms.length(), 0));
		starnbar(atoms.length(),12,array,hydist.valences(atoms));
		//With the inputs, there should be 336 possible hydrogen distributions.
		writer.close();
		assertEquals(336,hydist.lists.size());
	}
	
	@Test
	
	public void test_setformula() {
		String atoms="CCCCCC";
		List<Integer> dist= new ArrayList<Integer>(Arrays.asList(3,3,3,3,0,0));
		assertEquals("C3C3C3C3CC",setformula(atoms,dist));
	}

}
