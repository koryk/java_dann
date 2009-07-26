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
package com.syncleus.dann.neural.som;

import com.syncleus.dann.neural.*;
import java.util.*;
import java.util.Map.Entry;
import com.syncleus.dann.math.Hyperpoint;

/**
 * A SomBrain acts as the parent class for all brains that use traditional SOM
 * agorithms. It implements a standard SOM leaving only the methods handling the
 * neighborhood and learning rate as abstract. These methods can be implemented
 * in several ways to alow for different types of SOM networks.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public abstract class SomBrain extends Brain
{
	private int iterationsTrained;
	private Hyperpoint upperBounds;
	private Hyperpoint lowerBounds;
	private ArrayList<SomInputNeuron> inputs = new ArrayList<SomInputNeuron>();
	private Hashtable<Hyperpoint, SomNeuron> outputs = new Hashtable<Hyperpoint, SomNeuron>();

	/**
	 * Called by chidren classes to instantiate a basic SomBrain with the given
	 * number of inputs and with an output lattice of the given number of
	 * dimensions.
	 *
	 * @param inputCount The number of inputs
	 * @param dimentionality The number of dimensions of the output lattice
	 * @since 2.0
	 */
	protected SomBrain(int inputCount, int dimentionality)
	{
		if( inputCount <= 0 )
			throw new IllegalArgumentException("input count must be greater than 0");
		if(dimentionality <= 0)
			throw new IllegalArgumentException("dimentionality must be greater than 0");
		
		this.upperBounds = new Hyperpoint(dimentionality);
		this.lowerBounds = new Hyperpoint(dimentionality);
		for(int inputIndex = 0; inputIndex < inputCount; inputIndex++)
			this.inputs.add(new SomInputNeuron());
	}

	private void updateBounds(Hyperpoint position)
	{
		//make sure we have the proper dimentionality
		if(position.getDimensions() != this.getUpperBounds().getDimensions())
			throw new IllegalArgumentException("Dimentionality mismatch");

		for(int dimensionIndex = 1; dimensionIndex <= position.getDimensions(); dimensionIndex++)
		{
			if(this.getUpperBounds().getCoordinate(dimensionIndex) < position.getCoordinate(dimensionIndex))
				this.getUpperBounds().setCoordinate(position.getCoordinate(dimensionIndex), dimensionIndex);
			if(this.getLowerBounds().getCoordinate(dimensionIndex) > position.getCoordinate(dimensionIndex))
				this.getLowerBounds().setCoordinate(position.getCoordinate(dimensionIndex), dimensionIndex);
		}
	}

	/**
	 * Creates a new point in the output lattice at the given position. This
	 * will automatically have all inputs connected to it.
	 *
	 * @param position The position of the new output in the latice.
	 * @since 2.0
	 */
	public void createOutput(Hyperpoint position)
	{
		//make sure we have the proper dimentionality
		if(position.getDimensions() != this.getUpperBounds().getDimensions())
			throw new IllegalArgumentException("Dimentionality mismatch");

		Hyperpoint positionCopy = new Hyperpoint(position);

		//increase the upper bounds if needed
		this.updateBounds(positionCopy);

		//create and add the new output neuron
		SomNeuron outputNeuron = new SomNeuron();
		this.outputs.put(positionCopy, outputNeuron);

		//connect all inputs to the new neuron
		try
		{
			for(SomInputNeuron input : inputs)
				input.connectTo(outputNeuron);
		}
		catch(InvalidConnectionTypeDannException caughtException)
		{
			throw new AssertionError("unexpected InvalidConnectionTypeDannException");
		}
	}

	/**
	 * Gets the positions of all the outputs in the output lattice.
	 *
	 * @return the positions of all the outputs in the output lattice.
	 * @since 2.0
	 */
	public Set<Hyperpoint> getPositions()
	{
		HashSet<Hyperpoint> positions = new HashSet<Hyperpoint>();
		for(Hyperpoint position : this.outputs.keySet())
			positions.add(new Hyperpoint(position));
		return Collections.unmodifiableSet(positions);
	}

	/**
	 * Gets the current output at the specified position in the output lattice
	 * if the position doesnt have a SomNeuron associated with it then it
	 * returns null.
	 *
	 * @param position position in the output latice of the output you wish to
	 * retreive.
	 * @return The value of the specified SomNeuron, or null if there is no
	 * SomNeuron associated with the given position.
	 * @since 2.0
	 */
	public double getOutput(Hyperpoint position)
	{
		SomNeuron outputNeuron = this.outputs.get(position);
		outputNeuron.propagate();
		return outputNeuron.getOutput();
	}

	/**
	 * Obtains the BMU (Best Matching Unit) for the current input set. This will
	 * also train against the current input.
	 *
	 * @return the BMU for the current input set.
	 */
	public Hyperpoint getBestMatchingUnit()
	{
		return this.getBestMatchingUnit(true);
	}

	/**
	 * Obtains the BMU (Best Matching Unit) for the current input set. This will
	 * also train against the current input when specified.
	 *
	 * @param train true to train against the input set, false if no training
	 * occurs.
	 * @return the BMU for the current input set.
	 * @since 2.0
	 */
	public Hyperpoint getBestMatchingUnit(boolean train)
	{
		//make sure we have atleast one output
		if( this.outputs.size() <= 0)
			throw new IllegalStateException("Must have atleast one output");

		//find the best matching unit
		Hyperpoint bestMatchingUnit = null;
		double bestError = Double.POSITIVE_INFINITY;
		for(Entry<Hyperpoint, SomNeuron> entry : this.outputs.entrySet())
		{
			entry.getValue().propagate();
			
			if(bestMatchingUnit == null)
				bestMatchingUnit = entry.getKey();
			else if(entry.getValue().getOutput() < bestError)
			{
				bestMatchingUnit = entry.getKey();
				bestError = entry.getValue().getOutput();
			}
		}

		if(train)
			this.train(bestMatchingUnit);

		return bestMatchingUnit;
	}

	private void train(Hyperpoint bestMatchingUnit)
	{
		double neighborhoodRadius = this.neighborhoodRadiusFunction();
		double learningRate = this.learningRateFunction();

		for(Entry<Hyperpoint, SomNeuron> entry : this.outputs.entrySet())
		{
			double currentDistance = entry.getKey().calculateRelativeTo(bestMatchingUnit).getDistance();
			if( currentDistance < neighborhoodRadius)
			{
				double neighborhoodAdjustment = this.neighborhoodFunction(currentDistance);
				entry.getValue().train(learningRate, neighborhoodAdjustment);
			}
		}

		this.iterationsTrained++;
	}


	/**
	 * The number of iterations trained so far.
	 *
	 * @return the iterationsTrained so far.
	 * @since 2.0
	 */
	public int getIterationsTrained()
	{
		return iterationsTrained;
	}



	/**
	 * The upper bounds of the positions of the output neurons.
	 *
	 * @since 2.0
	 * @return the upperBounds
	 */
	protected Hyperpoint getUpperBounds()
	{
		return upperBounds;
	}



	/**
	 * The lower bounds of the positions of the output neurons.
	 *
	 * @since 2.0
	 * @return the lowerBounds
	 */
	protected Hyperpoint getLowerBounds()
	{
		return lowerBounds;
	}

	/**
	 * Gets the number of inputs.
	 *
	 * @return The number of inputs.
	 * @since 2.0
	 */
	public int getInputCount()
	{
		return this.inputs.size();
	}

	/**
	 * Sets the current input.
	 *
	 * @since 2.0
	 * @param inputIndex
	 * @param inputValue
	 */
	public void setInput(int inputIndex, double inputValue)
	{
		if(inputIndex >= this.getInputCount())
			throw new IllegalArgumentException("inputIndex is out of bounds");
		
		SomInputNeuron currentInput = this.inputs.get(inputIndex);
		currentInput.setInput(inputValue);
		currentInput.propagate();
	}

	/**
	 * Gets the current input value at the specied index.
	 *
	 * @param index Index of the input to get.
	 * @return The current value for the specified input.
	 * @since 2.0
	 */
	public double getInput(int index)
	{
		return this.inputs.get(index).getInput();
	}

	/**
	 * Determines the neighboorhood function based on the neurons distance from
	 * the Best Matching Unit (BMU).
	 *
	 * @param distanceFromBest The neuron's distance from the BMU.
	 * @return the decay effecting the learning of the specified neuron due to
	 * its distance from the BMU.
	 * @since 2.0
	 */
	protected abstract double neighborhoodFunction(double distanceFromBest);

	/**
	 * Determine the current radius of the neighborhood which will be centered
	 * around the Best Matching Unit (BMU).
	 *
	 * @return the current radius of the neighborhood.
	 * @since 2.0
	 */
	protected abstract double neighborhoodRadiusFunction();

	/**
	 * Determines the current learning rate for the network.
	 *
	 * @return the current learning rate for the network.
	 * @since 2.0
	 */
	protected abstract double learningRateFunction();
}
