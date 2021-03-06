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
package com.syncleus.tests.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.math.Vector;
import com.syncleus.dann.graph.drawing.hyperassociativemap.*;
import org.junit.*;

public class TestHyperassociativeNode
{
	private class TestMap extends AbstractHyperassociativeMap
	{
		public TestMap(int dimensions)
		{
			super(dimensions);
		}

		public void addNode(HyperassociativeNode node)
		{
			this.nodes.add(node);
		}
	}

	@Test
	public void testConstructors()
	{
		TestMap testMap = new TestMap(3);
		Vector testPoint = new Vector(3);
		testMap.addNode(new HyperassociativeNode(testMap, 0.123d, 0.456d));
		testMap.addNode(new HyperassociativeNode(testMap, testPoint, 0.456d, 0.789d));
	}

	@Test
	public void testBinaryNodes()
	{
		TestMap testMap = new TestMap(3);

		HyperassociativeNode testNode1 = new HyperassociativeNode(testMap, new Vector(new double[]{1.0d, 2.0d, 3.0d}), 0.1d);
		HyperassociativeNode testNode2 = new HyperassociativeNode(testMap, new Vector(new double[]{4.0d, 5.0d, 6.0d}), 0.1d);
		testNode1.associate(testNode2, 1.0d);
		testNode2.associate(testNode1, 1.0d);

		testMap.addNode(testNode1);
		testMap.addNode(testNode2);

		for(int count = 0; count < 1000; count++)
		{
			testMap.align();
		}

		double relativeDistance = testNode1.getLocation().calculateRelativeTo(testNode2.getLocation()).getDistance();
		Assert.assertTrue("one time node distance isnt close to equilibrium distance: expecting->1.0 received->" + relativeDistance, Math.abs(relativeDistance - 1.0d)< 0.01d);

		double averageSum = 0.0;
		double averageCount = 0.0;
		for(int count = 0; count < 50; count++)
		{
			testMap.align();
			averageSum += testNode1.getLocation().calculateRelativeTo(testNode2.getLocation()).getDistance();
			averageCount++;
		}
		double average = averageSum / averageCount;

		Assert.assertTrue("average node distance isnt close to equilibrium distance: expecting->1.0 received->" + average, Math.abs(average - 1.0d)< 0.01d);
	}

	@Test
	public void testAsscoiation() throws NeighborNotFoundException
	{
		TestMap testMap = new TestMap(3);

		HyperassociativeNode testNode1 = new HyperassociativeNode(testMap, new Vector(new double[]{1.0d, 2.0d, 3.0d}));
		HyperassociativeNode testNode2 = new HyperassociativeNode(testMap, new Vector(new double[]{4.0d, 5.0d, 6.0d}));

		testNode1.associate(testNode2, 1.0d);
		testNode2.associate(testNode1, 3.0d);

		testMap.addNode(testNode1);
		testMap.addNode(testNode2);

		Assert.assertTrue("testNode1 should have one neighbor", testNode1.getNeighbors().size() == 1);
		Assert.assertTrue("testNode2 should have one neighbor", testNode2.getNeighbors().size() == 1);

		Assert.assertTrue("testNode1 should have a weight of 1 with testNode2", Math.abs(testNode1.getNeighborsWeight(testNode2) - 1.0d) < 0.001d);
		Assert.assertTrue("testNode2 should have a weight of 3 with testNode1", Math.abs(testNode2.getNeighborsWeight(testNode1) - 3.0d) < 0.001d);

		testNode1.dissociate(testNode2);
		testNode2.dissociate(testNode1);

		Assert.assertTrue("testNode1 should have no neighbors", testNode1.getNeighbors().size() == 0);
		Assert.assertTrue("testNode2 should have no neighbors", testNode2.getNeighbors().size() == 0);

		testNode1.associate(testNode2, 1.0d);
		testNode2.associate(testNode1, 1.0d);

		Assert.assertTrue("testNode1 should have testNode2 neighbor", testNode1.getNeighbors().contains(testNode2));
		Assert.assertTrue("testNode2 should have testNode1 neighbor", testNode2.getNeighbors().contains(testNode1));

		testNode1.dissociateAll();
		testNode2.dissociateAll();

		Assert.assertTrue("testNode1 should have no neighbors", testNode1.getNeighbors().size() == 0);
		Assert.assertTrue("testNode2 should have no neighbors", testNode2.getNeighbors().size() == 0);
	}

	@Test
	public void testRandomCoordinates()
	{
		Vector randomPoint = HyperassociativeNode.randomCoordinates(3);

		Assert.assertTrue("randomPoint dim1 is initialized at 0.0", randomPoint.getCoordinate(1) != 0.0d);
		Assert.assertTrue("randomPoint dim2 is initialized at 0.0", randomPoint.getCoordinate(2) != 0.0d);
		Assert.assertTrue("randomPoint dim3 is initialized at 0.0", randomPoint.getCoordinate(3) != 0.0d);
	}

}
