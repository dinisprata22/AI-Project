package nn_Pacman;

import java.util.Comparator;


public class DescendingFitnessComparator_Pacman implements Comparator<FeedForwardNeuralNetwork_Pacman>  {

	@Override
	public int compare(FeedForwardNeuralNetwork_Pacman o1, FeedForwardNeuralNetwork_Pacman o2) {
		// TODO Auto-generated method stub
		double result = o2.getFitness()-o1.getFitness();
		if(result<0.0) {
			return -1;
		}else if(result>0.0) {
			return 1;
			}else return 0;
	}

}
