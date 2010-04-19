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
package com.syncleus.dann.genetics.wavelets;

import java.util.*;
import java.util.concurrent.*;
import org.apache.log4j.Logger;
import com.syncleus.dann.DannRuntimeException;
import com.syncleus.dann.InterruptedDannRuntimeException;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.genetics.AbstractGeneticAlgorithmFitnessFunction;
import com.syncleus.dann.genetics.wavelets.AbstractOrganism;
import com.syncleus.dann.genetics.wavelets.AbstractWaveletGene;
import com.syncleus.dann.genetics.wavelets.Chromosome;
import com.syncleus.dann.genetics.wavelets.Mutations;

/**
 * Rerpesents a population governed by Genetic Algorithm parameters. This class
 * is abstract and should be extended to assign fitness functions to each
 * member of the population. This class handles each generation of the
 * population.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public abstract class AbstractWaveletPopulation
{
	private final static Random RANDOM = new Random();
	private final TreeSet<AbstractWaveletFitnessFunction> population;
	private final double mutationDeviation;
	private final double crossoverPercentage;
	private final double dieOffPercentage;
	private int generations;
	private final ThreadPoolExecutor threadExecutor;
	//private final static Logger LOGGER = Logger.getLogger(AbstractGeneticAlgorithmPopulation.class);

	private static class Process implements Runnable
	{
		private final AbstractWaveletFitnessFunction fitnessFunction;
		private final static Logger LOGGER = Logger.getLogger(Process.class);

		public Process(AbstractWaveletFitnessFunction fitnessFunction)
		{
			this.fitnessFunction = fitnessFunction;
		}

		public void run()
		{
			try
			{
				this.fitnessFunction.process();
			}
			catch(Exception caught)
			{
				LOGGER.error("A throwable was caught!", caught);
				throw new DannRuntimeException("Throwable caught: " + caught, caught);
			}
			catch(Error caught)
			{
				LOGGER.error("A throwable was caught!", caught);
				throw new Error("Throwable caught: " + caught, caught);
			}
		}
	}

		/**
	 * Creates a new population with an initial population consisting of the
	 * specified chromosomes and with the given Genetic Algorithm parameters.
	 *
	 * @param initialChromosomes The initial chromosomes for the first
	 * generation of the population.
	 * @param mutationDeviation The deviation used when mutating each chromosome
	 * in the population.
	 * @param crossoverPercentage The percentage change crossover will take
	 * place.
	 * @param dieOffPercentage The percentage of the population to die off in
	 * each generation.
	 * @since 2.0
	 */
	public AbstractWaveletPopulation(double mutationDeviation, double crossoverPercentage, double dieOffPercentage)
	{
		this(mutationDeviation, crossoverPercentage, dieOffPercentage, new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors()*3, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()));
	}
	public int getPopulationSize(){
		return this.population.size();
	}
	/**
	 * Creates a new population with an initial population consisting of the
	 * specified chromosomes and with the given Genetic Algorithm parameters.
	 *
	 * @param initialChromosomes The initial chromosomes for the first
	 * generation of the population.
	 * @param mutationDeviation The deviation used when mutating each chromosome
	 * in the population.
	 * @param crossoverPercentage The percentage change crossover will take
	 * place.
	 * @param dieOffPercentage The percentage of the population to die off in
	 * each generation.
	 * @since 2.0
	 */
	public AbstractWaveletPopulation(double mutationDeviation, double crossoverPercentage, double dieOffPercentage, ThreadPoolExecutor threadExecutor)
	{
		this.population  = new TreeSet<AbstractWaveletFitnessFunction>();
		this.mutationDeviation = mutationDeviation;
		this.crossoverPercentage = crossoverPercentage;
		this.dieOffPercentage = dieOffPercentage;
		this.threadExecutor = threadExecutor;
	}

	public final void add(final AbstractOrganism chromosome)
	{
		this.population.add(this.packageChromosome(chromosome));
	}

	public final void addAll(final Collection<AbstractOrganism> chromosomes)
	{
		//create all the fitness functions and then process them in parallel
		final ArrayList<AbstractWaveletFitnessFunction> initialPopulation = new ArrayList<AbstractWaveletFitnessFunction>();
		final ArrayList<Future> futures = new ArrayList<Future>();
		for(AbstractOrganism chromosome : chromosomes)
		{
			final AbstractWaveletFitnessFunction fitnessFunction = this.packageChromosome(chromosome);
			initialPopulation.add(fitnessFunction);
			futures.add(this.threadExecutor.submit(new Process(fitnessFunction)));
		}
		//wait for processing to finish
		try
		{
			for(Future future : futures)
				future.get();
		}
		catch(InterruptedException caught)
		{
			//LOGGER.error("Unexpected interuption of Process(fitnessFunction)", caught);
			throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
		}
		catch(ExecutionException caught)
		{
			//LOGGER.error("Unexpected execution exception thrown from within Process(fitnessFunction)", caught);
			caught.printStackTrace();
			throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
		}

		//add to thetree set and sort
		this.population.addAll(initialPopulation);
	}

	/**
	 * Gets all the chromosomes consisting of the current generation of the
	 * population.
	 *
	 * @return An unmodifiable set of AbstractOrganisms consiting of
	 * the current population.
	 * @since 2.0
	 */
	public final Set<AbstractOrganism> getChromosomes()
	{
		final HashSet<AbstractOrganism> chromosomes = new HashSet<AbstractOrganism>();
		for(AbstractWaveletFitnessFunction member : population)
		{
			chromosomes.add(member.getChromosome());
		}

		return Collections.unmodifiableSet(chromosomes);
	}

	/**
	 * Gets the most successful member of the current population according to
	 * its fitness function.
	 *
	 * @return the most successful member of the current population according to
	 * its fitness function.
	 * @since 2.0
	 */
	public AbstractOrganism getWinner()
	{
		return this.population.last().getChromosome();
	}

	/**
	 * Gets the number of generations this population has went through.
	 *
	 * @return The number of generations this population has went through.
	 * @since 2.0
	 */
	public final int getGenerations()
	{
		return this.generations;
	}

	private final AbstractOrganism getRandomMember()
	{
		final int randomIndex = RANDOM.nextInt(this.population.size());
		int currentIndex = 0;		
		for(AbstractWaveletFitnessFunction member : this.population)
		{
			if(currentIndex == randomIndex)
				return member.getChromosome();

			currentIndex++;
		}

		throw new UnexpectedDannError("randomIndex was out of bounds!");
	}

	/**
	 * Proceeds to the next generation of the population. This includes killing
	 * off the worst preforming of the population and randomly selecting parents
	 * to replace them. Parents produce children through mutation and crossover.
	 *
	 * @since 2.0
	 */
	public void nextGeneration()
	{
		if(this.population.size() < 4)
			throw new IllegalStateException("Must have a population of atleast 4. Currently: " + this.population.size());

		this.generations++;

		//calculate population sizes
		final int populationSize = this.population.size();
		int lostPopulation = (int)((double)populationSize * this.dieOffPercentage);
		//ensure the population to kill off is even.
		if( lostPopulation%2 != 0)
			lostPopulation--;
		final int remainingPopulation = populationSize - lostPopulation;

		//remove least performing members of the population
		while(this.population.size() > remainingPopulation)
			this.population.remove(this.population.first());
		for (AbstractWaveletFitnessFunction<AbstractWaveletFitnessFunction> f : population)
			if (Mutations.mutationEvent(mutationDeviation))
				f.getChromosome().mutate();
		//breed children through mutation and crossover
		final ArrayList<AbstractOrganism> children = new ArrayList<AbstractOrganism>();

		while(this.population.size() < populationSize)
		{
			while(this.population.size() + children.size() < populationSize)
			{
				//obtain parents and mutate into children
				AbstractOrganism child1 = this.getRandomMember();
				AbstractOrganism child2 = this.getRandomMember();
				//store the new children
				if (child1 == child2)
					continue;
				children.add(child1.mate(child2));
				if(this.population.size() + children.size() < populationSize)
					children.add(child2.mate(child1));

			}
			
			//add children to the population
			this.addAll(children);
			children.clear();
			System.out.print((population.size() == populationSize)? "" : populationSize - population.size());
		}
	}

	/**
	 * An abstract method that must be implemented to package a supplied
	 * chromosome into an appropriate fitness function wrapper.
	 *
	 * @param chromosome Chromosome to wrap into a fitness function class.
	 * @return A fitness function wrapping the chromosome.
	 * @since 2.0
	 */
	protected abstract AbstractWaveletFitnessFunction packageChromosome(final AbstractOrganism chromosome);
}
