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
 * Represents a Gene which can mutate and expresses constant activity. The
 * activity of a ValueGene remains constant and only changes through mutation.
 * This ValueGene is backed by a MutableByte and its range will be the same.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 *
 */
public class ByteValueGene extends ValueGene<MutableByte>
{
	/**
	 * Initializes a new instance of this class with a value of 0.
	 *
	 * @since 2.0
	 */
	public ByteValueGene()
	{
		super(new MutableByte((byte)0));
	}

	/**
	 * Initializes a new instance of this class with the specified value.
	 *
	 * @param value The value for this gene.
	 * @since 2.0
	 */
	public ByteValueGene(byte value)
	{
		super(new MutableByte(value));
	}

	/**
	 * Initializes a new instance of this class with the specified value.
	 *
	 * @param value The value for this gene.
	 * @since 2.0
	 */
	public ByteValueGene(Byte value)
	{
		super(new MutableByte(value));
	}

	/**
	 * Initializes a new instance of this class with the specified value.
	 *
	 * @param value The value for this gene.
	 * @since 2.0
	 */
	public ByteValueGene(MutableByte value)
	{
		super(value);
	}

	/**
	 * Initializes a new instance of this class that is a copy of the specified
	 * value
	 *
	 * @param copyGene The value to copy.
	 * @since 2.0
	 */
	public ByteValueGene(ByteValueGene copyGene)
	{
		super(copyGene);
	}

	/**
	 * Creates a new instance of this object that is an exact copy.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	public ByteValueGene clone()
	{
		return new ByteValueGene(this);
	}

	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
	public ByteValueGene mutate(double deviation)
	{
		ByteValueGene copy = new ByteValueGene(this);
		copy.internalMutate(deviation);

		return copy;
	}
}
