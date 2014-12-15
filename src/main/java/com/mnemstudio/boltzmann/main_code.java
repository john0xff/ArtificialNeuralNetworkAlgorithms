package com.mnemstudio.boltzmann;

// Filename: Boltzmann1.java

/**
 * Boltzmann Machine
 * 
 * 
 * http://mnemstudio.org/neural-networks-boltzmann.htm
 * 
 * <pre>
 * 
 * 
 * 
 * This is a rendition of the classic Traveling Salesman Problem, where the shortest tour needs to be found among all
 * cites without visiting the same one twice. The algorithm works out the minimum Cartesian distance through eight
 * cities. The cities are arranged in a circle, where the shortest distance is to go around in order, but the algorithm
 * doesn't know this. The correct solution can be in forward or reverse order, and it doesn't matter which city is the
 * start (ie: 01234567, 76543210, or 45670123). There are 10^8 (or, 100,000,000) possible combinations, but this
 * algorithm can find them in less than 10^3. Note that the number of cities can be modified for experimentation.
 * 
 * At the start, the Boltzmann algorithm tries random variations of combinations searching for the final solution. As it
 * gets closer to the solution, it limits the variation of combinations to those that come closest to succeeding. This
 * focuses the search, but we don't want it to focus too narrowly, too fast, otherwise it will wander off track and lose
 * the final sulotion. The rate at which the algorithm should focus is called the temperature (e.g.; like the rate for
 * reducing temperature to get the right consistency).
 * 
 * In this example, the temperature is being reduced by a factor of 0.99, which is pretty close to the slowest possible
 * reduction speed (i.e.; more epochs at each temperature). Setting this factor lower, like 0.95 or 0.90, will entail
 * fewer epochs at each temperature, which might make the algorithm run faster, but increases the likelihood of getting
 * stuck in local minima.
 * 
 * The gamma value performs a similar function to temperature, keeping the algorithm from getting stuck in local minima.
 * </pre>
 * 
 * 
 * 
 * @author Bart88
 *
 */
public class main_code
{

	private static final double PI = 3.14159;
	private static final double aFactor = 0.99;

	public static Boltzmann1 Net;

	public static void main(String[] args)
	{
		Net = new Boltzmann1(8, 100, 7);

		Initialize();
		Net.CalcWeights();
		Net.SetRandom(); // Randomly set output nodes to either On or Off.

		while (ValidTour() == false)
		{
			Net.ReduceHeat();
			showResults();
			Net.Temperature(Net.Temperature() * aFactor);
		}
		return;
	}

	public static void Initialize()
	{
		int n1, n2;
		double x1, x2, y1, y2;
		double Alpha1, Alpha2;
		int nCities = Net.Cities();

		for (n1 = 0; n1 < nCities; n1++)
		{
			for (n2 = 0; n2 < nCities; n2++)
			{
				Alpha1 = ((double) n1 / nCities) * 2.0 * PI;
				Alpha2 = ((double) n2 / nCities) * 2.0 * PI;
				x1 = Math.cos(Alpha1);
				y1 = Math.sin(Alpha1);
				x2 = Math.cos(Alpha2);
				y2 = Math.sin(Alpha2);
				Net.Distance(n1, n2, (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))));
			} // n2
		} // n1

		System.out.println("Temperature     Valid   Length      Tour\n");
		return;
	}

	public static boolean ValidTour()
	{
		int Cities, Stops;
		int i, j;
		int nCities = Net.Cities();

		for (i = 0; i < nCities; i++)
		{
			Cities = 0;
			Stops = 0;
			for (j = 0; j < nCities; j++)
			{
				if (Net.Output(i * nCities + j) == true)
				{
					Cities += 1;
					if (Cities > 1)
					{
						return false;
					}
				}
				if (Net.Output(j * nCities + i) == true)
				{
					Stops += 1;
					if (Stops > 1)
					{
						return false;
					}
				}
			} // j
			if ((Cities != 1) || (Stops != 1))
			{
				return false;
			}
		} // i

		return true;
	}

	public static void showResults()
	{
		if (ValidTour())
		{
			System.out.println("\nTemperature     Valid   Length      Tour");
			System.out.print(Net.Temperature() + "          yes     " + Net.LengthOfTour() + "     ");
			getTour();
		}
		else
		{
			System.out.print(Net.Temperature() + "          no     ");
			getTour();
		}

		System.out.println();
		return;
	}

	public static void getTour()
	{
		int nCities = Net.Cities();
		int i, j;

		for (i = 0; i < nCities; i++)
		{
			for (j = 0; j < nCities; j++)
			{
				if (Net.Output(i * nCities + j) == true)
				{
					System.out.print(j);
				}
			} // j
		} // i

		return;

	}

}