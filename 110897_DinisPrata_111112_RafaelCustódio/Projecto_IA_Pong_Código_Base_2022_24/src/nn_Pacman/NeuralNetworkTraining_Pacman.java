package nn_Pacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import pacman.Pacman;
import pacman.PacmanBoard;
import utils.Commons;

public class NeuralNetworkTraining_Pacman {

	public static void main(String[] args) {

		int maxGen = 1000; // condicao de paragem
		int hiddens = 10;

		GeneticAlgorithm_Pacman GA = new GeneticAlgorithm_Pacman(0.05, hiddens);

		// populacao inicial
		ArrayList<FeedForwardNeuralNetwork_Pacman> initialPopulation = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			initialPopulation.add(GA.randomIndividual());
		}

		GA.setPopulation(initialPopulation);

		// treinamento

		PacmanBoard b;
		
		int generation = 0;
		int seed = 0;
		FeedForwardNeuralNetwork_Pacman best = null;
		int bestSeed = 0;
		int bestGen = 0;

		while (generation < maxGen) {

			// simulacoes
			ArrayList<FeedForwardNeuralNetwork_Pacman> pop = GA.getPopulation();
			for (FeedForwardNeuralNetwork_Pacman nn : pop) {

				b = new PacmanBoard(nn, false, seed);
				b.setSeed(seed);
				b.runSimulation();
				nn.setFitness(b.getFitness());
				System.out.println("Fitness: " + nn.getFitness() + " generation: " + generation);
			}
			GA.sortPopulation();

			
			// guarda o melhor para mostrar no fim
			
			FeedForwardNeuralNetwork_Pacman f = GA.getBest();
			if (best == null || best.getFitness() < f.getFitness()) {
				best = new FeedForwardNeuralNetwork_Pacman(Commons.PACMAN_STATE_SIZE, hiddens, Commons.PACMAN_NUM_ACTIONS,f.getValues());
				best.setFitness(f.getFitness());
				bestSeed = seed;
				bestGen = generation;

			}

			if (generation % 50 == 0 && generation !=0) {
				seed++;
			}
			generation++;
			
			GA.nextGeneration();
		}

		
		Scanner s = new Scanner(System.in);
		System.out.println("Press a key and Enter");
		s.next();
		
		Pacman pac = new Pacman(best, true, bestSeed);
		System.out.println(Arrays.toString(best.getValues()));
		System.out.println(bestGen + " " + best.getFitness() + " " + bestSeed);

	}

	

}
