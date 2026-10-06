package nn_Breakout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import breakout.Breakout;
import breakout.BreakoutBoard;
import utils.Commons;

public class NeuralNetworkTraining {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int maxGen = 1000; // condicao de paragem (numero maximo de geracoes)
		int hiddens = 54; // numero de hiddens utilizados

		GeneticAlgorithm GA = new GeneticAlgorithm(0.05, hiddens);

		
		// populacao inicial
		
		ArrayList<FeedForwardNeuralNetwork> initialPopulation = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			initialPopulation.add(GA.randomIndividual());
		}

		GA.setPopulation(initialPopulation);

		
		// treinamento

		BreakoutBoard b;
		
		int generation = 0;
		int seed = 0;
		FeedForwardNeuralNetwork best = null;
		int bestSeed = 0;
		int bestGen = 0;

		while (generation < maxGen) {

			// simulacoes
			
			ArrayList<FeedForwardNeuralNetwork> pop = GA.getPopulation();
			for (FeedForwardNeuralNetwork nn : pop) {

				b = new BreakoutBoard(nn, false, seed);
				
				double fitness = 0.0;
				for(int i = 0; i<2;i++) {
				b.setSeed(seed + i);
				b.runSimulation();
				fitness += b.getFitness();
				}
				
				fitness = fitness/2;
				nn.setFitness(fitness);
				System.out.println("Fitness: " + nn.getFitness() + " generation: " + generation);
			}
			GA.sortPopulation();

			
			// guardar o melhor para mostrar no fim

			FeedForwardNeuralNetwork f = GA.getBest();
			if (best == null || best.getFitness() < f.getFitness()) {
				best = new FeedForwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, hiddens, Commons.BREAKOUT_NUM_ACTIONS,f.getValues());
				best.setFitness(f.getFitness());
				bestSeed = seed;
				bestGen = generation;

			}

			if (generation % 125 == 0 && generation !=0) {
				seed++;
			}
			generation++;
			
			GA.nextGeneration();
		}

		
		Scanner s = new Scanner(System.in);
		System.out.println("Press a key and Enter");
		s.next();
		
		System.out.println(bestGen + " " + best.getFitness() + " " + bestSeed);
		System.out.println(Arrays.toString(best.getValues()));
		b = new BreakoutBoard(best, true, bestSeed);
		b.runSimulation();
		b = new BreakoutBoard(best, true, bestSeed + 1);
		b.runSimulation();
		Breakout bst = new Breakout(best, bestSeed);

	}

}
