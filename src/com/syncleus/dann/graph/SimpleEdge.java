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
package com.syncleus.dann.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleEdge<N> implements Edge<N>
{
	private final List<N> nodes;

	public SimpleEdge(List<N> nodes)
	{
		this.nodes = Collections.unmodifiableList(new ArrayList<N>(nodes));
	}

	public SimpleEdge(N... nodes)
	{
		List<N> newNodes = new ArrayList<N>();
		for(N node : nodes)
			newNodes.add(node);
		this.nodes = Collections.unmodifiableList(newNodes);
	}

	public final List<N> getNodes()
	{
		return this.nodes;
	}

	@Override
	public boolean equals(Object compareToObj)
	{
		if(!(compareToObj instanceof Edge))
			return false;
		Edge compareTo = (Edge) compareToObj;
		return (compareTo.getNodes().equals(this.nodes))&&
			(this.nodes.equals(compareTo.getNodes()));
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		for(N node : this.nodes)
			hash += node.hashCode();
		return hash;
	}

	@Override
	public String toString()
	{
		StringBuffer outString = null;
		for(N node : this.nodes)
		{
			if(outString == null)
				outString = new StringBuffer(node.toString());
			else
				outString.append(":" + node);
		}
		return outString.toString();
	}
}
