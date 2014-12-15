package com.mnemstudio.ART_AdaptiveResonanceTheory.example_1;

/**
 * Adaptive Resonance Theory
 * 
 * 
 * http://mnemstudio.org/neural-networks-art1-example-1.htm
 * 
 * 
 * ART1 Example 1 based on articles by Laurene Fausett, and Carpenter and Grossberg
 * 
 * As you experiment with different constants, you'll see some interesting things happen; especially when you raise the
 * VIGILANCE value. When you raise the vigilance, this means that the algorithm will be more "picky" about assigning
 * input vectors to clusters (i.e., the definition of similarity is MORE strict). When you lower the vigilance, the
 * algorithm will be more willing to accept input vectors into clusters (i.e., definition of similarity is LESS strict).
 * 
 * NOTE: One problem you'll run into with this code is when the loop in the ART1() function starts running in an
 * infinite loop. The reason for this is because this code isn't set up to create new cluster nodes to satisfy "picky"
 * vigilance values. When constrained with too few clusters, the algorithm can easily decide it doesn't want to put a
 * particular input vector in any of the clusters, but not know what to do with it. You can get around this (at least in
 * this example) by manually increasing the number of clusters (m) so a "picky" vigilance value will be able to
 * intensely discriminate among clusters for placing each input vector and not run out of cluster nodes to do it.
 * 
 * @author b.bien
 *
 */
public class ART1_Example1
{
	private static final int n = 4; // Number of components in an input vector.
	private static final int m = 3; // Max number of clusters to be formed.
	private static final double VIGILANCE = 0.4;
	private static final int PATTERNS = 7;
	private static final int TRAINING_PATTERNS = 4; // Use this many for training, the rest are for tests.
	private static int pattern[][] = null; // Training patterns.

	private static double bw[][] = null; // Bottom-up weights.
	private static double tw[][] = null; // Top-down weights.

	private static int f1a[] = null; // Input layer.
	private static int f1b[] = null; // Interface layer.
	private static double f2[] = null;

	private static void initialize()
	{
		pattern = new int[][]
		{
		{ 1, 1, 0, 0 },
		{ 0, 0, 0, 1 },
		{ 1, 0, 0, 0 },
		{ 0, 0, 1, 1 },
		{ 0, 1, 0, 0 },
		{ 0, 0, 1, 0 },
		{ 1, 0, 1, 0 } };

		// Initialize bottom-up weight matrix.
		System.out.println("Weights initialized to:");
		bw = new double[m][n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				bw[i][j] = 1.0 / (1.0 + n);
				System.out.print(bw[i][j] + ", ");
			} // j
			System.out.print("\n");
		} // i

		System.out.println();

		// Initialize top-down weight matrix.
		tw = new double[m][n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				tw[i][j] = 1.0;
				System.out.print(tw[i][j] + ", ");
			} // j
			System.out.print("\n");
		} // i
		System.out.println();

		f1a = new int[n];
		f1b = new int[n];
		f2 = new double[m];

		return;
	}

	private static void ART1()
	{
		int inputSum = 0;
		int activationSum = 0;
		int f2Max = 0;
		boolean reset = true;

		System.out.println("Begin ART1:\n");
		for (int vecNum = 0; vecNum < PATTERNS; vecNum++)
		{
			System.out.println("Vector: " + vecNum + "\n");

			// Initialize f2 layer activations to 0.0
			for (int i = 0; i < m; i++)
			{
				f2[i] = 0.0;
			}

			// Input pattern() to f1 layer.
			for (int i = 0; i < n; i++)
			{
				f1a[i] = pattern[vecNum][i];
			}

			// Compute sum of input pattern.
			inputSum = vectorSum(f1a);
			System.out.println("InputSum (si) = " + inputSum + "\n");

			// Compute activations for each node in the f1 layer.
			// Send input signal from f1a to the fF1b layer.
			for (int i = 0; i < n; i++)
			{
				f1b[i] = f1a[i];
			}

			// Compute net input for each node in the f2 layer.
			for (int i = 0; i < m; i++)
			{
				for (int j = 0; j < n; j++)
				{
					f2[i] += bw[i][j] * (double) f1a[j];
					System.out.print(String.format("%.1f", f2[i]) + ", ");
				} // j
				System.out.println();
			} // i
			System.out.println();

			reset = true;
			while (reset == true)
			{
				// Determine the largest value of the f2 nodes.
				f2Max = maximum(f2);

				// Recompute the f1a to f1b activations (perform AND function).
				for (int i = 0; i < n; i++)
				{
					System.out.println(f1b[i] + " * " + String.format("%.1f", tw[f2Max][i]) + " = " + String.format("%.1f", f1b[i] * tw[f2Max][i]));
					f1b[i] = f1a[i] * (int) Math.floor(tw[f2Max][i]);
				}

				// Compute sum of input pattern.
				activationSum = vectorSum(f1b);
				System.out.println("ActivationSum (x(i)) = " + activationSum + "\n");

				reset = testForReset(activationSum, inputSum, f2Max);

			}

			// Only use number of TRAINING_PATTERNS for training, the rest are tests.
			if (vecNum < TRAINING_PATTERNS)
			{
				updateWeights(activationSum, f2Max);
			}

			System.out.println("Vector #" + vecNum + " belongs to cluster #" + f2Max + "\n");

		} // vecNum
		return;
	}

	private static int vectorSum(int[] nodeArray)
	{
		int sum = 0;

		// Compute sum of input pattern.
		for (int i = 0; i < n; i++)
		{
			sum += nodeArray[i];
		}

		return sum;
	}

	private static void updateWeights(int activationSum, int f2Max)
	{
		// Update bw(f2Max)
		for (int i = 0; i < n; i++)
		{
			bw[f2Max][i] = (2.0 * (double) f1b[i]) / (1.0 + (double) activationSum);
		}

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				System.out.print(String.format("%.1f", bw[i][j]) + ", ");
			} // j
			System.out.println();
		} // i
		System.out.println();

		// Update tw(f2Max)
		for (int i = 0; i < n; i++)
		{
			tw[f2Max][i] = f1b[i];
		}

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				System.out.print(String.format("%.1f", tw[i][j]) + ", ");
			} // j
			System.out.println();
		} // i
		System.out.println();

		return;
	}

	private static boolean testForReset(int activationSum, int inputSum, int f2Max)
	{
		if ((double) activationSum / (double) inputSum >= VIGILANCE)
		{
			return false; // Candidate is accepted.
		}
		else
		{
			f2[f2Max] = -1.0; // Inhibit.
			return true; // Candidate is rejected.
		}
	}

	private static int maximum(double[] nodeArray)
	{
		int winner = 0;
		boolean foundNewWinner = false;
		boolean done = false;

		while (!done)
		{
			foundNewWinner = false;
			for (int i = 0; i < m; i++)
			{
				if (i != winner)
				{ // Avoid self-comparison.
					if (nodeArray[i] > nodeArray[winner])
					{
						winner = i;
						foundNewWinner = true;
					}
				}
			}

			if (foundNewWinner == false)
			{
				done = true;
			}
		}
		return winner;
	}

	private static void printResults()
	{
		System.out.println("Final weight values:");
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				System.out.print(String.format("%.1f", bw[i][j]) + ", ");
			} // j
			System.out.print("\n");
		} // i
		System.out.println();

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				System.out.print(String.format("%.1f", tw[i][j]) + ", ");
			} // j
			System.out.print("\n");
		} // i

		return;
	}

	public static void main(String[] args)
	{
		initialize();
		ART1();
		printResults();
		return;
	}

}