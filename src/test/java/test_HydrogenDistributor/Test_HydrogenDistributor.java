package test_HydrogenDistributor;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import hydrogenDistributor.HydrogenDistributor;


public class Test_HydrogenDistributor extends HydrogenDistributor{
	public static IChemObjectBuilder builder =SilentChemObjectBuilder.getInstance();
	
	@Test
	
	/**
	 * Setting the valences of the given element symbols
	 */
	
	public void test_setCapacity() {
		IMolecularFormula formula=MolecularFormulaManipulator.getMolecularFormula("C6H6", builder);
		formula.removeIsotope(builder.newInstance(IIsotope.class, "H"));
		IAtomContainer ac= MolecularFormulaManipulator.getAtomContainer(formula);
		int[] output= new int[] {3, 3, 3, 3, 3, 3};
		assertArrayEquals(setCapacity(ac),output);
	}
	
	@Test
	
	/**
	 *The distribution of the hydrogens atoms is called 'stars & bars' problem in combinatorics.  
	 */
	
	public void test_distribute() throws FileNotFoundException, UnsupportedEncodingException, CloneNotSupportedException {
		IMolecularFormula formula=MolecularFormulaManipulator.getMolecularFormula("C78H94N4O12", builder);
		initialise(formula);
		//With the inputs, there should be 34592 possible hydrogen distributions.
		assertEquals(34592,HydrogenDistributor.distributions.size());
	}
	
	@Test
	
	/**
	 * Function sets the formula based on the hydrogen distribution.
	 */
	
	public void test_setHydrogens() throws CloneNotSupportedException {
		IMolecularFormula formula=MolecularFormulaManipulator.getMolecularFormula("C6H12", builder);
		IAtomContainer ac= MolecularFormulaManipulator.getAtomContainer(formula);
		int[] distribution= new int[] {1, 1, 1, 3, 3, 3};
		IAtomContainer ac2= setHydrogens(ac,distribution);
		//The atom container should have 12 implicit hydrogens.
		assertEquals(12,AtomContainerManipulator.getImplicitHydrogenCount(ac2));
	}

}
