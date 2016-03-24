package net.eekysam.uhspres.utils.math;

public class NormalDistribution implements Distribution
{
	public double mean;
	public double deviation;
	
	public NormalDistribution(double mean, double deviation)
	{
		this.mean = mean;
		this.deviation = deviation;
	}
	
	@Override
	public double probability(double x)
	{
		return BlueMath.standardNormalProbability(this.getStandardX(x)) / x;
	}
	
	@Override
	public double cumulative(double x)
	{
		return BlueMath.standardNormalCumulative(this.getStandardX(x));
	}
	
	/**
	 * Gets the mean of this distribution excluding all values lower than left.
	 * 
	 * @param left The lowest value to include in the mean
	 * @return The mean
	 */
	public double sliceMean(double left)
	{
		return this.sliceMean(left, 6);
	}
	
	/**
	 * Gets the mean of this distribution excluding all values lower than left.
	 * 
	 * @param left The lowest value to include in the mean
	 * @param n How many iterations to make the calculation with
	 * @return The mean
	 */
	public double sliceMean(double left, int n)
	{
		//Don't even try to figure this out
		//Solved using Wolfram Mathematica
		/**
		 * <code>SliceMean[dist_, edge_] := Integrate[PDF[dist, x]*x, {x, edge, \[Infinity]}]/(1 - CDF[dist, edge])</code>
		 * <b>
		 * <code>NormalSliceMean[\[Mu]_, \[Sigma]_, edge_] := Evaluate[FullSimplify[SliceMean[NormalDistribution[\[Mu], \[Sigma]], edge]]]</code>
		 * <b> <code>NormalSliceMean[\[Mu], \[Sigma], a]</code><b>
		 * out =
		 * <code>(E^(-((a - \[Mu])^2/(2 \[Sigma]^2))) Sqrt[2/\[Pi]] \[Sigma] + \[Mu] Sqrt[1/\[Sigma]^2] \[Sigma] - \[Mu] Erf[(a - \[Mu])/(Sqrt[2] \[Sigma])])/(1 + Erf[(-a + \[Mu])/(Sqrt[2] \[Sigma])])</code>
		 */
		double diff = this.mean - left;
		double ds = this.deviation * this.deviation;
		double error = BlueMath.errorFunction((diff) / (BlueMath.sqrtTwo * this.deviation), n);
		double out = Math.exp((diff * diff) / (-2.0D * ds));
		out *= BlueMath.sqrtTwoOverPi;
		out += this.mean * Math.sqrt(1.0D / ds);
		out *= this.deviation;
		out += this.mean * error;
		out /= 1.0D + error;
		return out;
	}
	
	public double cumulative(double x, int n)
	{
		return BlueMath.standardNormalCumulative(this.getStandardX(x), n);
	}
	
	/**
	 * Scales and translates a value on this distribution to match the standard
	 * distribution.
	 * 
	 * @param x The value to scale
	 * @return The z-score of the value
	 */
	private double getStandardX(double x)
	{
		return (x - this.mean) / this.deviation;
	}
	
	/**
	 * Gets the fraction of the standard distribution that is in the given
	 * range.
	 * 
	 * @param min The bottom of the range
	 * @param max The top of the range
	 * @return The cumulative probability of the range
	 */
	public double cumulativeRange(double min, double max)
	{
		return BlueMath.standardNormalCumulativeRange(this.getStandardX(min), this.getStandardX(max));
	}
	
	/**
	 * Gets the fraction of the given range that is less than the given value.
	 * 
	 * @param min The bottom of the range
	 * @param max The top of the range
	 * @param x The value to compare
	 * @return The fraction of the cumulative probability of the range lower
	 *         than x
	 */
	public double cumulativeInRange(double min, double max, double x)
	{
		return BlueMath.standardNormalCumulativeInRange(this.getStandardX(min), this.getStandardX(max), this.getStandardX(x));
	}
	
	/**
	 * Gets the fraction of the standard distribution that is in the given
	 * range.
	 * 
	 * @param min The bottom of the range
	 * @param max The top of the range
	 * @param n How many iterations to make the calculation with
	 * @return The cumulative probability of the range
	 */
	public double cumulativeRange(double min, double max, int n)
	{
		return BlueMath.standardNormalCumulativeRange(this.getStandardX(min), this.getStandardX(max), n);
	}
	
	/**
	 * Gets the fraction of the given range that is less than the given value.
	 * 
	 * @param min The bottom of the range
	 * @param max The top of the range
	 * @param x The value to compare
	 * @param n How many iterations to make the calculation with
	 * @return The fraction of the cumulative probability of the range lower
	 *         than x
	 */
	public double cumulativeInRange(double min, double max, double x, int n)
	{
		return BlueMath.standardNormalCumulativeInRange(this.getStandardX(min), this.getStandardX(max), this.getStandardX(x), n);
	}
}
