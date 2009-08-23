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

public abstract class AbstractUndirectedEdge extends AbstractBidirectedEdge implements UndirectedEdge
{
	private final NodePair<UndirectedNode> undirectedNodePair;

	protected AbstractUndirectedEdge(UndirectedNode leftNode, UndirectedNode rightNode)
	{
		super(leftNode, EndState.None, rightNode, EndState.None);
		this.undirectedNodePair = new NodePair<UndirectedNode>(leftNode, rightNode);
	}

	public final NodePair<UndirectedNode> getUndirectedNodePair()
	{
		return this.undirectedNodePair;
	}

	@Override
	public boolean isIntroverted()
	{
		return false;
	}

	@Override
	public boolean isExtraverted()
	{
		return false;
	}

	@Override
	public boolean isDirected()
	{
		return false;
	}

	@Override
	public boolean isHalfEdge()
	{
		return false;
	}

	@Override
	public boolean isLooseEdge()
	{
		return true;
	}

	@Override
	public boolean isOrdinaryEdge()
	{
		return false;
	}
}
