package nn_Breakout;

import java.util.ArrayList;

import utils.Commons;

public class GeneticAlgorithm {

	private ArrayList<FeedForwardNeuralNetwork> population;
	private double mutationChance;
	private int numberOfHiddens;
	private int sizeOfArray;
	private double delta = 1.7;

	GeneticAlgorithm(double mutationChance,int numberOfHiddens) {
		this.mutationChance = mutationChance;
		this.numberOfHiddens = numberOfHiddens;
		this.sizeOfArray = Commons.BREAKOUT_STATE_SIZE * numberOfHiddens + numberOfHiddens
				+ numberOfHiddens * Commons.BREAKOUT_NUM_ACTIONS + Commons.BREAKOUT_NUM_ACTIONS;
	}

	GeneticAlgorithm(double mutationChance, int numberOfHiddens, ArrayList<FeedForwardNeuralNetwork> population) {
		this(mutationChance,numberOfHiddens);
		this.population = population;
		sortPopulation();
		
		
	}

	// metodo para criar um individuo aleatorio
	public FeedForwardNeuralNetwork randomIndividual() {
		double[] individualValues = new double[sizeOfArray];
		for (int i = 0; i < individualValues.length; i++) {
			individualValues[i] = 2*(Math.random() * 2 - 1);
		}
		return new FeedForwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, numberOfHiddens, Commons.BREAKOUT_NUM_ACTIONS,
				individualValues);
	}

	private FeedForwardNeuralNetwork selection(ArrayList<FeedForwardNeuralNetwork> population) {
		FeedForwardNeuralNetwork one = population.get((int) (Math.random() * population.size()));
		FeedForwardNeuralNetwork two = population.get((int) (Math.random() * population.size()));
		if (one.getFitness() > two.getFitness()) {
			return one;
		} else
			return two;
	}

	private FeedForwardNeuralNetwork crossover(FeedForwardNeuralNetwork parent1, FeedForwardNeuralNetwork parent2) {
		double[] parent1Weights;
		double[] parent2Weights;
		
		parent1Weights = parent1.getValues();
		parent2Weights = parent2.getValues();
		

		double[] childWeights = new double[sizeOfArray];
		// e criado um divisor entre 20 a 80% do tamanho do array
		int divider = (int) (sizeOfArray*(0.20+ Math.random()*0.60));

		for (int i = 0; i < sizeOfArray; i++) {
			if (i <= divider) {
				childWeights[i] = parent1Weights[i];
			} else
				childWeights[i] = parent2Weights[i];
		}
		return new FeedForwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, numberOfHiddens, Commons.BREAKOUT_NUM_ACTIONS,
				childWeights);
	}

	private void mutation(FeedForwardNeuralNetwork nn) {
		double[] weights = nn.getValues();
		// muda 2% dos valores da rede
		for(int i = 0; i< ((2*weights.length)/100);i++) {
		weights[(int) (Math.random() * sizeOfArray)] += delta*((Math.random()* 2) - 1);
		}
		nn.setValues(weights);
	}

	public void nextGeneration() {
		
		if(population == null || population.isEmpty()) {
			throw new IllegalStateException();
		}
		ArrayList<FeedForwardNeuralNetwork> newPop = new ArrayList<>();
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
			FeedForwardNeuralNetwork nn = crossover(selection(newPop), selection(newPop));
			if (Math.random() <= mutationChance) {
				mutation(nn);
			}
			
			// So mutacao
//			FeedForwardNeuralNetwork nn = selection(newPop);
//			mutation(nn);
			
			population.add(nn);
		}
	}

	public ArrayList<FeedForwardNeuralNetwork> getPopulation() {
		return population;
	}
	
	public void sortPopulation() {
		this.population.sort(new DescendingFitnessComparator());
	}

	public void setPopulation(ArrayList<FeedForwardNeuralNetwork> population) {
		this.population = population;
		sortPopulation();
	}

	public FeedForwardNeuralNetwork getBest() {
		return population.get(0);
	}
	
	

}
