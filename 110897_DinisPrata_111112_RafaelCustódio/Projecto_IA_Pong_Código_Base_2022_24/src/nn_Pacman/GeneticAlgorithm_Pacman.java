package nn_Pacman;

import java.util.ArrayList;

import utils.Commons;

public class GeneticAlgorithm_Pacman {
	

	private ArrayList<FeedForwardNeuralNetwork_Pacman> population;
	private double mutationChance;
	private int numberOfHiddens;
	private int sizeOfArray;
	private double delta = 1.2;

	GeneticAlgorithm_Pacman(double mutationChance,int numberOfHiddens) {
		this.mutationChance = mutationChance;
		this.numberOfHiddens = numberOfHiddens;
		this.sizeOfArray = Commons.PACMAN_STATE_SIZE * numberOfHiddens + numberOfHiddens
				+ numberOfHiddens * Commons.PACMAN_NUM_ACTIONS + Commons.PACMAN_NUM_ACTIONS;
	}

	GeneticAlgorithm_Pacman(double mutationChance, int numberOfHiddens, ArrayList<FeedForwardNeuralNetwork_Pacman> population) {
		this(mutationChance,numberOfHiddens);
		this.population = population;
		this.population.sort(new DescendingFitnessComparator_Pacman());
		
		
	}

	public FeedForwardNeuralNetwork_Pacman randomIndividual() {
		double[] individualValues = new double[sizeOfArray];
		for (int i = 0; i < individualValues.length; i++) {
			individualValues[i] = 2*(Math.random() * 2 - 1);
		}
		return new FeedForwardNeuralNetwork_Pacman(Commons.PACMAN_STATE_SIZE, numberOfHiddens, Commons.PACMAN_NUM_ACTIONS,
				individualValues);
	}

	private FeedForwardNeuralNetwork_Pacman selection(ArrayList<FeedForwardNeuralNetwork_Pacman> population) {
		FeedForwardNeuralNetwork_Pacman one = population.get((int) (Math.random() * population.size()));
		FeedForwardNeuralNetwork_Pacman two = population.get((int) (Math.random() * population.size()));
		if (one.getFitness() > two.getFitness()) {
			return one;
		} else
			return two;
	}

	private FeedForwardNeuralNetwork_Pacman crossover(FeedForwardNeuralNetwork_Pacman parent1, FeedForwardNeuralNetwork_Pacman parent2) {
		double[] parent1Weights;
		double[] parent2Weights;
		
		parent1Weights = parent1.getValues();
		parent2Weights = parent2.getValues();
		

		double[] childWeights = new double[sizeOfArray];
		int divider = (int) (sizeOfArray*0.20+ Math.random()*(sizeOfArray*0.60));

		for (int i = 0; i < sizeOfArray; i++) {
			if (i <= divider) {
				childWeights[i] = parent1Weights[i];
			} else
				childWeights[i] = parent2Weights[i];
		}
		return new FeedForwardNeuralNetwork_Pacman(Commons.PACMAN_STATE_SIZE, numberOfHiddens, Commons.PACMAN_NUM_ACTIONS,
				childWeights);
	}

	private void mutation(FeedForwardNeuralNetwork_Pacman nn) {
		double[] weights = nn.getValues();
		// muda 2% dos valores da rede
		for(int i = 0; i< ((2*weights.length)/100);i++) {
		weights[(int) (Math.random() * sizeOfArray)] += delta*((Math.random()* 2) - 1);
		}
		nn.setValues(weights);
	}

	public void nextGeneration() {
		ArrayList<FeedForwardNeuralNetwork_Pacman> newPop = new ArrayList<>();
		// salva 10% melhores
		
		for (int i = 0; i < 10; i++) {
			if (Math.random() <= 0.2 && i != 0) {
				mutation(population.get(i));
			}
			
			newPop.add(population.get(i));
		}
		population = newPop;
		// filhos apartir dos 10% salvos anteriormente
		for (int i = newPop.size(); i < 100; i++) {
				
			// Com crossover
			FeedForwardNeuralNetwork_Pacman nn = crossover(selection(newPop), selection(newPop));
			if (Math.random() <= mutationChance) {
				mutation(nn);
			}
			
			// So mutacao
//			FeedForwardNeuralNetwork nn = selection(newPop);
//			mutation(nn);
			
			population.add(nn);
		}
	}

	public ArrayList<FeedForwardNeuralNetwork_Pacman> getPopulation() {
		return population;
	}
	
	public void sortPopulation() {
		population.sort(new DescendingFitnessComparator_Pacman());
	}

	public void setPopulation(ArrayList<FeedForwardNeuralNetwork_Pacman> population) {
		this.population = population;
		sortPopulation();
	}

	public FeedForwardNeuralNetwork_Pacman getBest() {
		return population.get(0);
	}
	
	


}
