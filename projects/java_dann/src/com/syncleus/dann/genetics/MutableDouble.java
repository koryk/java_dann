/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.genetics;

/**
 * This is a MutableNumber backed by a Double. It essentially just allows the
 * number to mutate
 *
 * @author Syncleus, Inc.
 * @since 2.0
 *
 */
public class MutableDouble extends MutableNumber<Double> implements Comparable<MutableDouble>
{
	/**
	 * Initializes a new instance of this class with the specified value.
	 *
	 * @param value The value of this number.
	 * @since 2.0
	 */
	public MutableDouble(double value)
	{
		super(Double.valueOf(value));
	}

	/**
	 * Initializes a new instance of this class from the value represented
	 * by the specified string.
	 *
	 * @param s A string representing the value of this number.
	 * @since 2.0
	 */
	public MutableDouble(String s)
	{
		super(Double.valueOf(s));
	}

	/**
	 * Initializes a new instance of this class as a copy of the specefied
	 * number.
	 *
	 * @param value The value to copy
	 * @since 2.0
	 */
	public MutableDouble(Double value)
	{
		super(value);
	}

	/**
	 * An new exact copy of this object with the same value.
	 *
	 * @return a new exact copy of this object with the same value.
	 * @since 2.0
	 */
	public MutableDouble clone()
	{
		return new MutableDouble(this.getNumber());
	}

	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation. If the Number is
	 * mutated past its largest or smallest representable number it will
	 * simply return the max or min respectivly.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
	public MutableDouble mutate(double deviation)
	{
		double distributedRand = MutableNumber.getDistributedRandom(deviation);

		double result = this.getNumber().doubleValue() + distributedRand;

		if(Double.isInfinite(result))
		{
			if(result > 0)
				result = Double.MAX_VALUE;
			else
				result = Double.MAX_VALUE * 1.0;
		}

		return new MutableDouble(result);
	}

	/**
	 * Compares the value of this number against another object of the same
	 * type. The backing number handles the comparison.
	 *
	 * @param compareWith Number to compare against.
	 * @return the natural ordering of the backed number.
	 * @since 2.0
	 */
	public int compareTo(MutableDouble compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
