/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph;

import java.util.List;
import java.util.Set;

public interface HyperGraph<N, E extends HyperEdge<? extends N>, W extends HyperWalk<? extends N, ? extends E>> extends Graph<N,E,W>
{
	int getNeighborCount(N node);
	boolean isSymmetric(N firstNode, N secondNode);
	boolean isSymmetric(E firstEdge, E secondEdge);

	boolean isPartial(HyperGraph partialGraph);
	boolean isDual(HyperGraph dualGraph);
	boolean isHost(HyperGraph hostGraph);
	boolean isPrimal(Graph primalGraph);
	boolean isUniform();
	boolean isSymmetric();
	boolean isVertexSymmetric();
	boolean isEdgeSymmetric();

	//Parent methods
	List<E> getEdges(N node);
	List<E> getTraversableEdges(N node);
	int getDegree(N node);
	boolean isConnected(N leftNode, N rightNode);
	List<N> getNeighbors(N node);
	List<N> getTraversableNeighbors(N node);

	Set<N> getNodes();
	List<E> getEdges();
	boolean isConnected();
	Set<Graph<N,E,W>> getConnectedComponents();
	boolean isMaximalConnected();
	boolean isCut(Graph<? extends N, ? extends E, ? extends W> subGraph);
	boolean isCut(Graph<? extends N, ? extends E, ? extends W> subGraph, N begin, N end);
	int getNodeConnectivity();
	int getEdgeConnectivity();
	int getNodeConnectivity(N begin, N end);
	int getEdgeConnectivity(N begin, N end);
	boolean isCompleteGraph();
	int getOrder();
	int getCycleCount();
	boolean isPancyclic();
	int getGirth();
	int getCircumference();
	boolean isTraceable();
	boolean isSpanning(W walk);
	boolean isSpanning(TreeGraph graph);
	boolean isTraversable();
	boolean isEularian(W walk);
	boolean isTree();
	boolean isSubGraph(Graph<? extends N, ? extends E, ? extends W> graph);
	boolean isKnot(Graph<? extends N, ? extends E, ? extends W> subGraph);
	int getTotalDegree();
	boolean isMultigraph();
	boolean isIsomorphic(Graph<? extends N, ? extends E, ? extends W> isomorphicGraph);
	boolean isHomomorphic(Graph<? extends N, ? extends E, ? extends W> homomorphicGraph);
	boolean isRegular();
}
