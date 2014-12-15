package com.mnemstudio.ART_AdaptiveResonanceTheory.example_4;

/**
 * Adaptive Resonance Theory
 * 
 * 
 * http://mnemstudio.org/neural-networks-art1-example-4.htm
 * 
 * ART1 Example Adapted From M. T. Jones I originally found this code in "AI Application Programming", by M. T. Jones.
 * It was written in C, which I ported to Java, Python and VB.Net. The algorithm design might look a little different
 * than the previous examples, but the overall function achieves similar ends.
 * 
 * This code was designed as an example of personalization, where customer purchases are analyzed to group the customers
 * together for future product recommendations. In the Database array below, each row represents individual customer
 * purchases (1's), in contrast to other items in the store which weren't purchased (0's). For example, the first column
 * might be candy bars, the second might be dish soap, the third might be lunch meat, etc.
 * 
 * Note the use of the "Count" variable; it's being used as an arbitrary limit for the algorithm's iterations. The
 * original author may have recognized certain constants that lead to convergence with 50 iterations. However, the
 * typical ART1 algorithm includes a check to see if it has converged or not (as in the previous examples).
 * 
 * You'll probably notice after some experimentation that the resulting groups can vary depending on which order the
 * data rows are entered.
 * 
 * @author b.bien
 *
 */
public class ART1_mtjones1
{
	private static final int MAX_ITEMS = 11;
	private static final int MAX_CUSTOMERS = 14;
	private static final int TOTAL_PROTOTYPE_VECTORS = 10;

	private static final double BETA = 1.0; // Small positive number.
	private static final double VIGILANCE = 0.6; // 0 <= VIGILANCE < 1

	private static int prototypeVectors = 0; // Total populated prototype vectors.

	private static int prototypeVector[][] = new int[TOTAL_PROTOTYPE_VECTORS][MAX_ITEMS];

	// Number of occupants of cluster.
	private static int members[] = new int[TOTAL_PROTOTYPE_VECTORS];

	// Identifies which cluster a member belongs to.
	private static int membership[] = new int[MAX_CUSTOMERS];

	private static int database[][] = new int[][]
	{
	{ 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0 },
	{ 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1 },
	{ 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0 },
	{ 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1 },
	{ 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 },
	{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
	{ 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
	{ 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0 },
	{ 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 },
	{ 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0 },
	{ 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
	{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
	{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
	{ 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 } };

	private static void initialize()
	{
		for (int i = 0; i < TOTAL_PROTOTYPE_VECTORS; i++)
		{
			for (int j = 0; j < MAX_ITEMS; j++)
			{
				prototypeVector[i][j] = 0;
			} // j
		} // i

		for (int i = 0; i < MAX_CUSTOMERS; i++)
		{
			membership[i] = -1;
		}
		return;
	}

	private static void performART1()
	{
		int magPE = 0;
		int magP = 0;
		int magE = 0;
		double result = 0.0;
		double test = 0.0;
		boolean done = false;
		int Count = 50;

		while (!done)
		{
			done = true;
			for (int i = 0; i < MAX_CUSTOMERS; i++)
			{
				for (int PVec = 0; PVec < TOTAL_PROTOTYPE_VECTORS; PVec++)
				{
					// Check to see if this vector has any members.
					if (members[PVec] > 0)
					{

						int ANDResult[] = vectorBitwiseAND(database, i, prototypeVector, PVec);

						magPE = vectorMagnitude(ANDResult);
						magP = vectorMagnitude(prototypeVector, PVec);
						magE = vectorMagnitude(database, i);

						result = (double) magPE / (BETA + (double) magP);

						test = (double) magE / (BETA + (double) MAX_ITEMS);

						if (result > test)
						{
							// Test for vigilance acceptability.
							if (((double) magPE / (double) magE) < VIGILANCE)
							{
								int old = 0;
								// Ensure this is a different cluster.
								if (membership[i] != PVec)
								{
									// Move customer to the new cluster
									old = membership[i];
									membership[i] = PVec;

									if (old >= 0)
									{
										members[old] -= 1;
										if (members[old] == 0)
										{
											prototypeVectors -= 1;
										}
										members[PVec] += 1;
										// Recalculate the prototype vectors for the old and new clusters.
										if ((old >= 0) && (old < TOTAL_PROTOTYPE_VECTORS))
										{
											updatePrototypeVectors(old);
										}
										updatePrototypeVectors(PVec);
										done = false;
										break;
									}
								}
								else
								{
									// Already in this cluster.
								}
							}
						}
					}
				} // PVec.

				// Check to see if the current vector was processed.
				if (membership[i] == -1)
				{
					// No prototype vector was found to be close to the example
					// vector. Create a new prototype vector for this example.
					membership[i] = createNewPrototypeVector(database, i);
					done = false;
				}

			} // for Customers loop.

			if (Count <= 0)
			{
				break;
			}
			else
			{
				// Not Done yet.
				Count -= 1;
			}
		}
		return;
	}

	private static void displayCustomerClusters()
	{
		for (int cluster = 0; cluster < TOTAL_PROTOTYPE_VECTORS; cluster++)
		{
			System.out.print("\n");
			System.out.print("ProtoType Vector: " + cluster + ":   ");
			for (int item = 0; item < MAX_ITEMS; item++)
			{
				System.out.print(prototypeVector[cluster][item]);
			} // item
			System.out.print("\n");

			for (int customer = 0; customer < MAX_CUSTOMERS; customer++)
			{
				if (membership[customer] == cluster)
				{
					System.out.print("Customer: " + customer + ":           ");
					for (int item = 0; item < MAX_ITEMS; item++)
					{
						System.out.print(database[customer][item]);
					} // item
					System.out.print("\n");
				}
			} // customer

			System.out.print("\n");
		} // cluster
		System.out.print("\n");
		return;
	}

	private static void displayMemberships()
	{
		for (int customer = 0; customer < MAX_CUSTOMERS; customer++)
		{
			System.out.println("Membership: " + membership[customer]);
		}
		System.out.println();
		return;
	}

	private static void displayCustomerDatabase()
	{
		for (int customer = 0; customer < MAX_CUSTOMERS; customer++)
		{
			System.out.println();
			System.out.print("Customer: " + customer + ":           ");
			for (int item = 0; item < MAX_ITEMS; item++)
			{
				System.out.print(database[customer][item]);
			} // item
		} // customer
		return;
	}

	private static void updatePrototypeVectors(final int Cluster)
	{
		boolean first = true;
		if (Cluster >= 0)
		{
			for (int Item = 0; Item < MAX_ITEMS; Item++)
			{
				prototypeVector[Cluster][Item] = 0;
			} // Item
			for (int Customer = 0; Customer < MAX_CUSTOMERS; Customer++)
			{
				if (membership[Customer] == Cluster)
				{
					if (first)
					{
						for (int Item = 0; Item < MAX_ITEMS; Item++)
						{
							prototypeVector[Cluster][Item] = database[Customer][Item];
						} // Item
						first = false;
					}
					else
					{
						for (int Item = 0; Item < MAX_ITEMS; Item++)
						{
							prototypeVector[Cluster][Item] = prototypeVector[Cluster][Item] * database[Customer][Item];
						} // Item
					}
				}
			} // Customer
		}
		return;
	}

	private static int createNewPrototypeVector(final int[][] vector, final int vRow)
	{
		int cluster;
		for (cluster = 0; cluster < TOTAL_PROTOTYPE_VECTORS; cluster++)
		{
			if (members[cluster] == 0)
			{
				break;
			}
		}
		if (cluster == TOTAL_PROTOTYPE_VECTORS - 1)
		{
			prototypeVectors += 1;
		}
		for (int i = 0; i < MAX_ITEMS; i++)
		{
			prototypeVector[cluster][i] = vector[vRow][i];
		}
		members[cluster] = 1;
		return cluster;
	}

	private static int[] vectorBitwiseAND(final int[][] v, final int vRow, final int[][] w, final int wRow)
	{
		int ANDResult[] = new int[MAX_ITEMS];
		for (int i = 0; i < MAX_ITEMS; i++)
		{
			ANDResult[i] = (v[vRow][i] * w[wRow][i]);
		}
		return ANDResult;
	}

	private static int vectorMagnitude(final int[] vector)
	{
		// This function counts up all the 1's in a given vector.
		int totalOnes = 0;
		for (int i = 0; i < MAX_ITEMS; i++)
		{
			if (vector[i] == 1)
			{
				totalOnes += 1;
			}
		}

		return totalOnes;
	}

	private static int vectorMagnitude(final int[][] vector, final int vRow)
	{
		// This function counts up all the 1's in a given vector.
		int totalOnes = 0;
		for (int i = 0; i < MAX_ITEMS; i++)
		{
			if (vector[vRow][i] == 1)
			{
				totalOnes += 1;
			}
		}

		return totalOnes;
	}

	public static void main(String[] args)
	{
		initialize();
		performART1();
		displayCustomerClusters();
		displayMemberships();
		displayCustomerDatabase();
		return;
	}

}