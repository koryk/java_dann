package com.syncleus.dann;
import com.syncleus.dann.activation.ActivationFunction;
import com.syncleus.dann.activation.HyperbolicTangentActivationFunction;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public abstract class NeuronImpl<SN extends NeuronImpl, SS extends Synapse<? extends SN, ? extends NeuronImpl>, DN extends NeuronImpl, DS extends Synapse<? extends NeuronImpl, ? extends DN>> implements Neuron<SN, SS, DN, DS>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * Represents the current excitation of the neuron from input
     * signals<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double activity;
    /**
     * Represents the current output of the neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double output;
    /**
     * The current weight of the bias input. The bias is an input that is always
     * set to an on position. The bias weight usually adapts in the same manner
     * as the rest of the synapse's weights.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double biasWeight;
    /**
     * An array list of all the synapses that this neuron is currently
     * connection out to.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected HashSet<DS> destinations = new HashSet<DS>();
    /**
     * All the synapses currently connecting into this neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private HashSet<SS> sources = new HashSet<SS>();

    protected ActivationFunction activationFunction;
	protected Random random = new Random();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of Neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public NeuronImpl()
    {
        this.biasWeight = ((this.random.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = new HyperbolicTangentActivationFunction();
    }



    public NeuronImpl(ActivationFunction activationFunction)
    {
        if (activationFunction == null)
            throw new NullPointerException("activationFunction can not be null");


        this.biasWeight = ((this.random.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = activationFunction;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">
    /**
     * This method is called internally, between NetworkNodes, to
     * facilitate the connection process.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.Neuron#connectTo
     */
    protected void connectFrom(SS inSynapse) throws InvalidConnectionTypeDannException
    {
        //make sure you arent already connected fromt his neuron

        //add the synapse to the source list
        this.sources.add(inSynapse);
    }

    /**
     * Causes the NetworkNode to disconnect all connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllSources
     * @see com.syncleus.dann.NetworkNode#disconnectAllDestinations
     */
    public void disconnectAll()
    {
        this.disconnectAllDestinations();
        this.disconnectAllSources();
    }



    /**
     * Causes the NetworkNode to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#disconnectAllSources
     * @see com.syncleus.dann.Neuron#disconnectAll
     */
    public void disconnectAllDestinations()
    {
        for (DS currentDestination : this.destinations)
            try
            {
                this.disconnectDestination(currentDestination);
            }
            catch (SynapseNotConnectedException caughtException)
            {
                caughtException.printStackTrace();
                throw new InternalError("Unexpected Runtime Exception: " + caughtException);
            }
    }



    /**
     * Causes the NetworkNode to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#disconnectAllDestinations
     * @see com.syncleus.dann.Neuron#disconnectAll
     */
    public void disconnectAllSources()
    {
        for (SS currentSource : this.sources)
            try
            {
                this.disconnectSource(currentSource);
            }
            catch (SynapseNotConnectedException caughtException)
            {
                caughtException.printStackTrace();
                throw new InternalError("Unexpected Runtime Exception: " + caughtException);
            }
    }



    /**
     * Disconnects from a perticular outgoing connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The outgoing synapse to disconnect from.<BR>
     * @see com.syncleus.dann.Neuron#removeSource
     */
    public void disconnectDestination(DS outSynapse) throws SynapseNotConnectedException
    {
        if (this instanceof OutputNeuron)
            throw new InvalidParameterException("Can not disconnect a destination for a OutputNeuron");

        if (this.destinations.remove(outSynapse) == false)
            throw new SynapseNotConnectedException("can not disconnect destination, does not exist.");

        try
        {
            if (outSynapse.getDestination() != null)
                outSynapse.getDestination().removeSource(outSynapse);
        }
        catch (SynapseDoesNotExistException caughtException)
        {
            throw new SynapseNotConnectedException("can not disconnect destination, does not exist.", caughtException);
        }
    }



    /**
     * Disconnects from a perticular incomming connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to disconnect from.<BR>
     * @see com.syncleus.dann.Neuron#removeDestination
     */
    public void disconnectSource(SS inSynapse) throws SynapseNotConnectedException
    {
        if (this instanceof InputNeuron)
            throw new InvalidParameterException("Can not disconnect a source for a InputNeuron");

        if (this.sources.remove(inSynapse) == false)
            throw new SynapseNotConnectedException("can not disconnect source, does not exist.");

        try
        {
            if (inSynapse.getSource() != null)
                inSynapse.getSource().removeDestination(inSynapse);
        }
        catch (SynapseDoesNotExistException caughtException)
        {
            throw new SynapseNotConnectedException("can not disconnect source, does not exist.", caughtException);
        }
    }



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.Neuron#disconnectSource
     */
    protected void removeDestination(DS outSynapse) throws SynapseDoesNotExistException
    {
        if (this instanceof OutputNeuron)
            throw new InvalidParameterException("Can not remove a destination for a OutputNeuron");

        if (this.destinations.remove(outSynapse) == false)
            throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.Neuron#disconnectDestination
     */
    protected void removeSource(SS inSynapse) throws SynapseDoesNotExistException
    {
        if (this instanceof InputNeuron)
            throw new InvalidParameterException("Can not disconnect a source for a InputNeuron");

        if (this.sources.remove(inSynapse) == false)
            throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }



    public Set<DS> getDestinations()
    {
        return Collections.unmodifiableSet(this.destinations);
    }



    public Set<Neuron> getNeighbors()
    {
        HashSet<Neuron> neighbors = new HashSet<Neuron>();
        for (Synapse source : this.getSources())
            neighbors.add(source.getSource());
        for (Synapse destination : this.getDestinations())
            neighbors.add(destination.getDestination());

        return Collections.unmodifiableSet(neighbors);
    }



    public Set<SN> getSourceNeighbors()
    {
        HashSet<SN> neighbors = new HashSet<SN>();
        for (SS sourceSynapse : this.getSources())
				neighbors.add(sourceSynapse.getSource());

        return Collections.unmodifiableSet(neighbors);
    }



    public Set<DN> getDestinationNeighbors()
    {
        HashSet<DN> neighbors = new HashSet<DN>();
        for (DS destination : this.getDestinations())
            neighbors.add(destination.getDestination());

        return Collections.unmodifiableSet(neighbors);
    }

    public Set<SS> getSources()
    {
        return Collections.unmodifiableSet(sources);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">
    /**
     * sets the current output on all outgoing synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#propagate
     * @param newOutput The output value.
     */
    protected void setOutput(double newOutput)
    {
        this.output = newOutput;

        for (DS current : this.destinations)
            current.setInput(newOutput);
    }



    /**
     * Gets the current output.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return The current output.
     */
    protected double getOutput()
    {
        return this.output;
    }



    /**
     * obtains the output as a function of the current activity. This is a bound
     * function (usually between -1 and 1) based on the current activity of the
     * neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return a bound value (between -1 and 1 if this function is not
     * 	overwritten). It is a function of the neuron's current activity.
     * @see com.syncleus.dann.Neuron#propagate
     */
    protected double activate()
    {
        return this.activationFunction.activate(this.activity);
    }



    /**
     * This must be the derivity of the ActivityFunction. As such it's output is
     * also based on the current activity of the neuron. If the
     * activationFunction is overwritten then this method must also be
     * overwritten with the proper derivative.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return the derivative output of the activationFunction
     * @see com.syncleus.dann.Neuron#activationFunction
     */
    protected double activateDerivitive()
    {
        return this.activationFunction.activateDerivative(this.activity);
    }
    // </editor-fold>
}
