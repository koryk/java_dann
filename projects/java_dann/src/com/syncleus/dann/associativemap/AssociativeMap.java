/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.associativemap;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class AssociativeMap implements Serializable
{
    protected HashSet<AssociativeNode> nodes = new HashSet<AssociativeNode>();
    
    public Set<AssociativeNode> getNodes()
    {
        return Collections.unmodifiableSet(this.nodes);
    }
    
    public void align()
    {
        for(AssociativeNode node : nodes)
            node.align();
    }
}
