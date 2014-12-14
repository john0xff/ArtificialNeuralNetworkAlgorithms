package com.mnemstudio.kohonen.SOM;

/**
 * http://mnemstudio.org/neural-networks-som1.htm
 * 
 * <pre>
 * 
 * A Kohonen Self-Organizing Network with 4 Inputs and 2-Node Linear Array of Cluster Units.
 * 
 * The results will vary slightly with different combinations of learning rate, decay rate, and alpha value.
 * 
 * 
 * Example Results
 * 
 * After 101 iterations, this code would produce the following results:
 * 
 * Iterations: 101
 * Clusters for training input:
 * Vector (1, 1, 0, 0, ) fits into category 1
 * Vector (0, 0, 0, 1, ) fits into category 0
 * Vector (1, 0, 0, 0, ) fits into category 1
 * Vector (0, 0, 1, 1, ) fits into category 0
 * Weights for Node 0 connections:
 * .000, .000, .508, 1.000,
 * Weights for Node 1 connections:
 * 1.000, .492, .000, .000,
 * Categorized test input:
 * Vector (1, 0, 0, 1, ) fits into category 1
 * Vector (0, 1, 1, 0, ) fits into category 0
 * Vector (1, 0, 1, 0, ) fits into category 1
 * Vector (0, 1, 0, 1, ) fits into category 0
 * 
 * </pre>
 * 
 * 
 * 
 * 
 * @author Bart88
 *
 */
public class main_code
{

	public static final int MAX_CLUSTERS = 2;
	public static final int VECTORS = 4;
	public static final int VEC_LEN = 4;
	public static final double DECAY_RATE = 0.96;
	public static final double MIN_ALPHA = 0.01;

	public static final int Pattern[][] = new int[][]
	{
	{ 1, 1, 0, 0 },
	{ 0, 0, 0, 1 },
	{ 1, 0, 0, 0 },
	{ 0, 0, 1, 1 } };

	public static final int Tests[][] = new int[][]
	{
	{ 1, 0, 0, 1 },
	{ 0, 1, 1, 0 },
	{ 1, 0, 1, 0 },
	{ 0, 1, 0, 1 } };

	public static void main(String[] args)
	{
		double Alpha = 0.6;

		SOM_Class1 som = new SOM_Class1(VECTORS, MAX_CLUSTERS, Alpha, MIN_ALPHA, DECAY_RATE, VEC_LEN);
		som.Train(Pattern, Tests);
		som.Test(Pattern, Tests);

		System.out.println("Iterations: " + som.Iterations() + "\n");

		return;
	}

}