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

import com.syncleus.dann.NetworkNode;
import java.util.HashSet;
import java.util.Random;


public class NetworkNodeAssociativeNode extends AssociativeNode
{
    private static Random random = new Random();
    
    private NetworkNode networkNode;
    
    
    public NetworkNodeAssociativeNode(BrainAssociativeMap network, int dimensions, NetworkNode networkNode)
    {
        super(network, randomCoordinates(dimensions));
        
        this.networkNode = networkNode;
    }



    public NetworkNodeAssociativeNode(BrainAssociativeMap network, Hyperpoint location, NetworkNode networkNode)
    {
        super(network, location);

        this.networkNode = networkNode;
    }



    @Override
    protected BrainAssociativeMap getNetwork()
    {
        return (BrainAssociativeMap) super.getNetwork();
    }



    void refresh()
    {
        this.dissociateAll();

        HashSet<NetworkNode> neighborNeurons = new HashSet<NetworkNode>();
        neighborNeurons.addAll(this.networkNode.getNeighbors());
        for (NetworkNode neighborNeuron : neighborNeurons)
            this.associate(this.getNetwork().getNodeFromNeuron(neighborNeuron), 1.0);
    }



    public NetworkNode getNetworkNode()
    {
        return networkNode;
    }
}
