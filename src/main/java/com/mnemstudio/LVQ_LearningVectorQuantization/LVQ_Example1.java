package com.mnemstudio.LVQ_LearningVectorQuantization;

/**
 * Learning Vector Quantization.
 * 
 * 
 * src http://mnemstudio.org/ai/nn/lvq_java_ex1.txt
 * http://mnemstudio.org/neural-networks-learning-vector-quantization.htm
 * 
 * @author b.bien
 * 
 * 
 *         Introduction based on articles by Laurene Fausett, T. Kohonen, and Pandya & Macy.
 * 
 *         "While VQ and the basic SOM are unsupervised clustering and learning methods, LVQ describes supervised
 *         learning. On the other hand, unlike in SOM, no neighborhoods around the "
 *         winner" are defined during learning in the basic LVQ...(Kohonen(2001), p. 245)".
 * 
 *         You might have noticed that learning vector quantization is also covered in the Clustering area of this site.
 *         Here, the neural network version of learning vector quantization is a little different than the traditional
 *         version. The traditional version is based more on statistical pattern recognition, creating prototypes from
 *         the commonalities between various patterns (i.e.; attempting to find the clusters). The neural network
 *         version of LVQ, on the other hand, operates under the assumption that the centroids or prototypes are already
 *         known and that all input vectors are expected to fit into these clusters. Neural network LVQ is heavily
 *         dependent on supervised learning.
 * 
 * 
 * 
 * 
 *
 */
public class LVQ_Example1
{
	private static final int NUMBER_OF_CLUSTERS = 7;
	private static final int VEC_LEN = 63;
	private static final int TRAINING_PATTERNS = 21;

	private static final double DECAY_RATE = 0.96; // About 100 iterations.
	private static final double MIN_ALPHA = 0.01;

	private static double alpha = 0.6;
	private static double d[] = null; // Network nodes. The "clusters"

	// Weight matrix.
	private static double w[][] = null;

	// Training patterns.
	private static int mPattern[][] = null;
	private static int mTarget[] = null;

	public static void initialize()
	{
		// Insert pattern arrays into mPattern() to make an array of arrays.
		mPattern = new int[TRAINING_PATTERNS][];
		mPattern[0] = A1;
		mPattern[1] = B1;
		mPattern[2] = C1;
		mPattern[3] = D1;
		mPattern[4] = E1;
		mPattern[5] = J1;
		mPattern[6] = K1;
		mPattern[7] = A2;
		mPattern[8] = B2;
		mPattern[9] = C2;
		mPattern[10] = D2;
		mPattern[11] = E2;
		mPattern[12] = J2;
		mPattern[13] = K2;
		mPattern[14] = A3;
		mPattern[15] = B3;
		mPattern[16] = C3;
		mPattern[17] = D3;
		mPattern[18] = E3;
		mPattern[19] = J3;
		mPattern[20] = K3;

		d = new double[NUMBER_OF_CLUSTERS]; // Network nodes.

		mTarget = new int[]
		{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 };

		// Weight matrix to be filled with values between 0.0 and 1.0
		w = new double[NUMBER_OF_CLUSTERS][VEC_LEN];
		return;
	}

	public static void initializeWeights(int clusterNumber, int trainingPattern[])
	{
		// clusterNumber = the output node (cluster) to assign the pattern to.
		// trainingPattern = the pattern which the output node will respond to.

		// Initialize weights.
		for (int i = 0; i < VEC_LEN; i++)
		{
			w[clusterNumber][i] = trainingPattern[i];
		}

	}

	private static void training()
	{
		int dMin = 0;

		while (alpha > MIN_ALPHA)
		{
			for (int VecNum = 0; VecNum < TRAINING_PATTERNS; VecNum++)
			{
				// Compute input for all nodes.
				computeInput(mPattern, VecNum);

				// See which is smaller?
				dMin = minimum(d);

				// Update the weights on the winning unit.
				updateWeights(VecNum, dMin);

			} // VecNum

			// Reduce the learning rate.
			alpha = DECAY_RATE * alpha;
		}
	}

	public static int getCluster(int[] inputPattern)
	{
		// Compute input for all nodes.
		computeInput(inputPattern);

		// See which is smaller?
		return minimum(d);
	}

	private static void updateWeights(int vectorNumber, int dMin)
	{
		for (int i = 0; i < VEC_LEN; i++)
		{
			// Update the winner.
			if (dMin == mTarget[vectorNumber])
			{
				w[dMin][i] += (alpha * (mPattern[vectorNumber][i] - w[dMin][i]));
			}
			else
			{
				w[dMin][i] -= (alpha * (mPattern[vectorNumber][i] - w[dMin][i]));
			}
		}
		return;
	}

	private static void computeInput(int[][] vectorArray, int vectorNumber)
	{
		// Overloaded function. See computeInput below.
		clearArray(d);
		for (int i = 0; i < NUMBER_OF_CLUSTERS; i++)
		{
			for (int j = 0; j < VEC_LEN; j++)
			{
				d[i] += Math.pow((w[i][j] - vectorArray[vectorNumber][j]), 2);
			} // j
		} // i
		return;
	}

	private static void computeInput(int[] vectorArray)
	{
		// Overloaded function. See computeInput above.
		clearArray(d);
		for (int i = 0; i < NUMBER_OF_CLUSTERS; i++)
		{
			for (int j = 0; j < VEC_LEN; j++)
			{
				d[i] += Math.pow((w[i][j] - vectorArray[j]), 2);
			} // j
		} // i
		return;
	}

	private static void clearArray(double[] anArray)
	{
		for (int i = 0; i < NUMBER_OF_CLUSTERS; i++)
		{
			anArray[i] = 0.0;
		}
		return;
	}

	private static int minimum(double[] nodeArray)
	{
		int winner = 0;
		boolean foundNewWinner = false;
		boolean done = false;

		while (!done)
		{
			foundNewWinner = false;
			for (int i = 0; i < NUMBER_OF_CLUSTERS; i++)
			{
				if (i != winner)
				{ // Avoid self-comparison.
					if (nodeArray[i] < nodeArray[winner])
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

	// If you look closely, you can see the letters made out of 1's
	private static int[] A1 = new int[]
	{ 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1,
			0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1 };

	private static int[] B1 = new int[]
	{ 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 };

	private static int[] C1 = new int[]
	{ 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0 };

	private static int[] D1 = new int[]
	{ 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0 };

	private static int[] E1 = new int[]
	{ 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };

	private static int[] J1 = new int[]
	{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1,
			0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int[] K1 = new int[]
	{ 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1,
			0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1 };

	private static int[] A2 = new int[]
	{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1,
			0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0 };

	private static int[] B2 = new int[]
	{ 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0,
			0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 };

	private static int[] C2 = new int[]
	{ 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int[] D2 = new int[]
	{ 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0,
			0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0 };

	private static int[] E2 = new int[]
	{ 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0,
			0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 };

	private static int[] J2 = new int[]
	{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1,
			0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int[] K2 = new int[]
	{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0,
			0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0 };

	private static int[] A3 = new int[]
	{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0,
			0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1 };

	private static int[] B3 = new int[]
	{ 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 };

	private static int[] C3 = new int[]
	{ 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int[] D3 = new int[]
	{ 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1,
			0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0 };

	private static int[] E3 = new int[]
	{ 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };

	private static int[] J3 = new int[]
	{ 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
			0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int[] K3 = new int[]
	{ 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1,
			0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1 };

	private static String[] mFontNames = new String[]
	{ "A1", "B1", "C1", "D1", "E1", "J1", "K1", "A2", "B2", "C2", "D2", "E2", "J2", "K2", "A3", "B3", "C3", "D3", "E3", "J3", "K3" };

	public static void main(String[] args)
	{
		initialize();

		for (int i = 0; i < NUMBER_OF_CLUSTERS; i++)
		{
			initializeWeights(i, mPattern[i]);
			System.out.println("Weights for cluster " + i + " initialized to pattern " + mFontNames[i]);
		}

		training();

		// Display results
		for (int i = 0; i < TRAINING_PATTERNS; i++)
		{
			System.out.println("Pattern " + mFontNames[i] + " belongs to cluster " + getCluster(mPattern[i]));
		}
		return;
	}

}