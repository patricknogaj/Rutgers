#include <stdio.h>
#include <stdlib.h>

void sort(int array[], int size) {
	int temp, i, j, left = 0, right = 0, count_evens = 0;
	int tempArray[size];

	for(i = 0; i < size; i++) {
		if(array[i] % 2 == 0)
			count_evens++;
	}

	right = count_evens;

	for(i = 0; i < size; i++) {
		for(j = 0; j < (size - i - 1); j++) {
			if(array[j] > array[j+1]) {
				temp = array[j];
				array[j] = array[j + 1];
				array[j + 1] = temp;
			}
		}
	}
	
	for(i = 0; i < size; i++) {
		if(array[i] % 2 == 0) {
			tempArray[left] = array[i];
			left++;
		}
	}

	for(j = size - 1; j >=0; j--) {
		if(array[j] % 2 != 0) {
			tempArray[right] = array[j];
			right++;
		}
	}

	for(i = 0; i < size; i++) {
		array[i] = tempArray[i];
	}
}

int main(int argc, char* argv[]) {

	FILE *file = fopen(argv[1], "r");
	int counter = 0, size = 0, i;
	int *input;

	if(file != NULL) {
		if(fscanf(file, "%d", &size) == 1) {
			input = malloc(sizeof(int) * size);

			while(fscanf(file, "%d", &i) == 1) {
				input[counter] = i;
				counter++;
			}
		}
	} else {
		printf("error");
	}

	sort(input, size);

	for(i = 0; i < counter; i++) {
		printf("%d\t", input[i]);
	}

	free(input);

	return 0;
}
