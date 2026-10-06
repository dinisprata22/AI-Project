package nn_Breakout;

import java.util.Comparator;

public class DescendingFitnessComparator implements Comparator<FeedForwardNeuralNetwork> {

	@Override
	public int compare(FeedForwardNeuralNetwork o1, FeedForwardNeuralNetwork o2) {
		double result = o2.getFitness() - o1.getFitness();
		if (result < 0.0) {
			return -1;
		} else if (result > 0.0) {
			return 1;
		} else
			return 0;
	}

}
