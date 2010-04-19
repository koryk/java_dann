/*******************************************************************************
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
package com.syncleus.dann.genetics.wavelets;

import java.util.HashSet;
import java.util.Set;

public class Cell
{
	protected HashSet<SignalKeyConcentration> localConcentrations;
	protected Nucleus nucleus;

	public Cell(Cell copy)
	{
		this.nucleus = copy.nucleus;
		this.localConcentrations  = new HashSet<SignalKeyConcentration>(copy.localConcentrations);
	}

	public Cell(Nucleus nucleus)
	{
		this.nucleus = nucleus;
		this.localConcentrations = new HashSet<SignalKeyConcentration>();

		Set<SignalKey> localSignals = this.nucleus.getExpressedSignals(false);
		for(SignalKey localSignal : localSignals)
		{
			SignalKeyConcentration newConcentration = new SignalKeyConcentration(localSignal);
			this.localConcentrations.add(newConcentration);
			this.nucleus.bind(newConcentration, false);
		}
	}

	public boolean bind(SignalKeyConcentration concentration, boolean isExternal)
	{
		return false;
	}

	public Set<SignalKey> getExpressedSignals()
	{
		return this.nucleus.getExpressedSignals(true);
	}

	public void preTick()
	{
		this.nucleus.preTick();
	}

	public void tick()
	{
		this.nucleus.tick();
	}
}
