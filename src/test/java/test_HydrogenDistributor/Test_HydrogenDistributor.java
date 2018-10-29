package test_HydrogenDistributor;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import hydrogenDistributor.HydrogenDistributor;

public class Test_HydrogenDistributor {
	public  String Desktop=(System.getProperty("user.home") + "\\Desktop").replace("\\", "/");
	
	@Test
	
	/**
	 * Setting the valences of the given element symbols
	 */
	public void test_valences() {
		String atoms= "CHONS";
		List<Integer> output= new ArrayList<Integer>(Arrays.asList(4,1,2,5,6));
		assertEquals(HydrogenDistributor.setValences(atoms),output);
	}
	
	@Test
	
	/**
	 *The distribution of the hydrogens atoms is called 'stars & bars' problem in combinatorics.  
	 */
	public void test_starnbar() throws FileNotFoundException, UnsupportedEncodingException {
		HydrogenDistributor.writer = new PrintWriter(Desktop+"HydrogenDistributorOutput.txt", "UTF-8"); //The output file.
		String atoms="CCCCCC";
		HydrogenDistributor.atominfo=atoms; //The atoms are set in the class.
		HydrogenDistributor.hydrogens=12; //The number of hydrogens.
		List<Integer> zeros=new ArrayList<Integer>(Collections.nCopies(atoms.length(), 0));
		HydrogenDistributor.starNbar(atoms.length(),12,zeros,HydrogenDistributor.setValences(atoms));
		//With the inputs, there should be 7 possible hydrogen distributions.
		HydrogenDistributor.writer.close();
		assertEquals(7,HydrogenDistributor.distributions.size());
	}
	
	@Test
	
	/**
	 * Function sets the formula based on the hydrogen distribution.
	 */
	public void test_setformula() {
		String atoms="CCCCCC";
		List<Integer> dist= new ArrayList<Integer>(Arrays.asList(3,3,3,3,0,0));
		assertEquals("C3C3C3C3CC",HydrogenDistributor.setFormula(atoms,dist));
	}

}
