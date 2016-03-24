package net.eekysam.uhspres.utils.math;

public interface Distribution
{
	/**
	 * Calculates the probability density (PDF) of this distribution at x.
	 * 
	 * @param x The value to get the probability of
	 * @return The probability density function of x
	 */
	public double probability(double x);
	
	/**
	 * Calculates the cumulative distribution function (CDF) of this
	 * distribution using a Taylor series.
	 * 
	 * @param x The top of the interval
	 * @return The cumulative distribution function of x
	 */
	public double cumulative(double x);
}
