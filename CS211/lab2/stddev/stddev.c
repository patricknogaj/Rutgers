#include <stdio.h>
#include <math.h>
#include <stdlib.h>

double standard_deviation(double input[], int size) {
	double sum, average, answer;
	int i;
	
	for(i = 0; i < size; i++) {
		sum += input[i];
	}
	average = sum / size;
	printf("mean: %.0f", average);	

	for(i = 0; i < size; i++) {
		answer += pow(input[i] - average, 2);
	}
	return sqrt(answer / size);
}


int main() {

	double *input = malloc(sizeof(double) * 10);
	double i;
	int counter = 0;

	while(scanf("%lf", &i) == 1) {
		
		if(counter >= (sizeof(input) / sizeof(double)))
			input = realloc(input, sizeof(double) * (counter + 1));	
		input[counter] = i;
		counter++;
	}

	if(counter == 0) { 
		printf("no data");
		return EXIT_FAILURE;
	}

	printf("\nstddev: %.0f", standard_deviation(input, counter));
	return 0;
}
