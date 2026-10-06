package nn_Pacman;

import pacman.PacmanBoard;
import utils.GameController;

public class FeedForwardNeuralNetwork_Pacman implements GameController {

	private int inputDim;
	private int hiddenDim;
	private int outputDim;
	private double[][] hiddenWeights;
	private double[] hiddenBiases;
	private double[][] outputWeights;
	private double[] outputBiases;
	private double fitness;
	private double[] values;

	
	public FeedForwardNeuralNetwork_Pacman(int inputDim, int hiddenDim, int outputDim) {
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;
		this.fitness = 0.0;
		
	}
	

	public FeedForwardNeuralNetwork_Pacman(int inputDim, int hiddenDim, int outputDim, double[] values) {
		
		this(inputDim,hiddenDim,outputDim);
		this.values = values;
		initializeValues();
	}
	
	private void initializeValues() {
		
		this.hiddenWeights = new double[inputDim][hiddenDim];
		this.hiddenBiases = new double[hiddenDim];
		this.outputBiases = new double[outputDim];
		this.outputWeights = new double[hiddenDim][outputDim];
		
		int aux = 0;
		
		//hiddenweights
		for(int i = 0; i< inputDim; i++)
			for(int j = 0;j<hiddenDim;j++) {
				hiddenWeights[i][j] = values[aux];
		aux++;
			}
		
		//hiddenBiases
		for(int i = 0; i< hiddenDim; i++) {
			hiddenBiases[i] = values[aux];
			aux++;
		}
		
		//outputWeights
		for(int i = 0; i< hiddenDim; i++)
			for(int j = 0;j<outputDim;j++) {
				outputWeights[i][j] = values[aux];
				aux++;
			}
		
		//outputBiases
		for(int i = 0; i<outputDim;i++) {
			outputBiases[i] = values[aux];
			aux++;
		}
	}
	
	private double sigmoid( double value) {
		return 1/(1+Math.exp(-value));
	}

	public double[] forward(int[] inputValues) {
		double output[] = new double[outputDim];
		double valsHidden[]= new double[hiddenDim];
		
		
		//Hiddens
		for(int i =0;i<hiddenDim;i++) {
			for(int j =0;j<inputValues.length;j++) {
				valsHidden[i] += inputValues[j] * hiddenWeights[j][i];
			}
			valsHidden[i] += hiddenBiases[i];
			valsHidden[i] = sigmoid(valsHidden[i]);
		}

		// outputs
		for (int i = 0; i < outputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				output[i] += valsHidden[j] * outputWeights[j][i];
			}
			output[i] += outputBiases[i];
			output[i] =sigmoid(output[i]);
		}

		return output;
		
		
	}
	
	@Override
	public int nextMove(int[] currentState) {
		double[] outputValues = forward(currentState);
		int choose = biggest(outputValues);
		if (choose == 0) {
			return PacmanBoard.NONE;
		} else if (choose == 1) {
			return PacmanBoard.LEFT;
		} else if (choose == 2) {
			return PacmanBoard.RIGHT;
		} else if (choose == 3) {
			return PacmanBoard.UP;
		} else
			return PacmanBoard.DOWN;
	}

	// metodo que dado um vetor de double devolve o indice do maior double
	private int biggest(double[] output) {
		double biggest = 0;
		int biggestIndex = 0;
		for(int i = 0;i<output.length;i++) {
			if(output[i] > biggest) {
				biggest = output[i];
				biggestIndex = i;
			}
		}
		return biggestIndex;
	}

	public double getFitness() {
		return fitness;
	}


	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
		initializeValues();
	}

}
