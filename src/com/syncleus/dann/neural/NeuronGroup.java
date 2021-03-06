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
package com.syncleus.dann.neural;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * A special NetworkNode which can contain other NetworkNodes as children.
 * 
 *
 * @author Syncleus, Inc.
 * @since 1.0
 */
public class NeuronGroup<N extends AbstractNeuron> implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * This contains all the neurons considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     *
     * @since 1.0
     */
    protected HashSet<N> childrenNeurons = new HashSet<N>();

    /**
     * This contains all the neuronGroups considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     *
     * @since 1.0
     */
	protected HashSet<NeuronGroup<? extends N>> childrenNeuronGroups = new HashSet<NeuronGroup<? extends N>>();

	/**
	 * The random number generator used for this class
	 *
	 *
	 * @since 1.0
	 */
	protected static Random random = new Random();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new empty instance of NeuronGroup
	 *
     *
     * @since 1.0
     */
    public NeuronGroup()
    {
    }

	/**
	 * Creates a new NeuronGroup that is a copy of the specified group.
	 *
	 *
	 * @param copyGroup NeuronGroup to copy.
	 * @since 1.0
	 */
	public NeuronGroup(NeuronGroup<? extends N> copyGroup)
	{
		this.childrenNeurons.addAll(copyGroup.childrenNeurons);
		for(NeuronGroup<? extends N> currentGroup : copyGroup.childrenNeuronGroups)
		{
			this.childrenNeuronGroups.add(currentGroup);
		}
	}

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">

    /**
     * Adds another Neuron to this layer.
	 *
     *
     * @since 1.0
     * @param toAdd the Neuron to add.
     */
    public void add(N toAdd)
    {
        this.childrenNeurons.add(toAdd);
    }

    /**
     * Adds another NeuronGroup to this layer.
	 *
     *
     * @since 1.0
     * @param toAdd the NeuronGroup to add.
     */
    public void add(NeuronGroup<? extends N> toAdd)
    {
        this.childrenNeuronGroups.add(toAdd);
    }



    /**
     * Obtains all the Neurons directly owned by this NeuronGroup.
     * @since 1.0
     */
    public Set<N> getChildrenNeurons()
    {
        return Collections.unmodifiableSet(this.childrenNeurons);
    }

    /**
     * Obtains all the NeuronGroups directly owned by this NeuronGroup.
     * @since 1.0
     */
    public Set<NeuronGroup<? extends N>> getChildrenNeuronGroups()
    {
        return Collections.unmodifiableSet(this.childrenNeuronGroups);
    }



    /**
     * Obtains all the NetworkNodes owned recursivly excluding
     * NeuronGroups.<BR>
     * @since 1.0
     */
    public Set<N> getChildrenNeuronsRecursivly()
    {
        HashSet<N> returnList = new HashSet<N>();

		returnList.addAll(this.childrenNeurons);
		for (NeuronGroup<? extends N> currentChild : this.childrenNeuronGroups)
			returnList.addAll(currentChild.getChildrenNeuronsRecursivly());

        return Collections.unmodifiableSet(returnList);
    }
    // </editor-fold>
}
