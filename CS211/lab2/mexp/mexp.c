#include <stdio.h>
#include <stdlib.h>

int **createMatrix(int **matrix, int rows, int columns) {
	int i = 0;
	matrix = malloc(sizeof(int*) * rows);
	
	for(i = 0; i < rows; i++) {
		matrix[i] = malloc(sizeof(int) * columns);
	} 
	return matrix;
}

int **mult_matrix(int **a, int **b, int size) {
	int i = 0, j = 0, k = 0;
	int **temp = NULL;
	temp = createMatrix(temp, size, size);
	int sum = 0;
	
	for(i = 0; i < size; i++) {
		for(j = 0; j < size; j++) {
			for(k = 0; k < size; k++) {
				sum += a[i][k] * b[k][j];
			}	
			temp[i][j] = sum;
			sum = 0;
		}
	} return temp;
} 

int **mult_matrix_exp(int **matrix, int size, int exp) {
	int i = 0;
	int** temp = matrix;

	for(i = 0; i < exp - 1; i++) {
		matrix = mult_matrix(matrix, temp, size);
	}
	return matrix;
}


void printMatrix(int **matrix, int rows, int columns) {
	int i = 0, j = 0;

	for(i = 0; i < rows; i++) {
		for( j = 0; j < columns; j++) {
			//printf("%d ", matrix[i][j]);
			if(j == (columns - 1))
				printf("%d", matrix[i][j]);
			else
				printf("%d ", matrix[i][j]);
		}
		printf("\n");
	}
}

void freeMatrix(int **matrix, int rows, int columns) {
	int i = 0;

	for(i = 0; i < rows; i++) {
		free(matrix[i]);
	}
	free(matrix);
}

int main(int argc, char *argv[]) {

	int **matrix = NULL;
	FILE *file = fopen(argv[1], "r");
	int size;
	int i = 0, j = 0, input;

	if(file != NULL) {
		while(!feof(file)) {
			fscanf(file, " %d", &size);
			break;
		} 
	} else {
			printf("File not found.");	
	}

	int rows = size, columns = size;
	matrix = createMatrix(matrix, rows, columns);

	for( i = 0; i < rows; i++) {
		for(j = 0; j < columns; j++) {
			fscanf(file, "%d", &input);
			matrix[i][j] = input;
		}
	}
	fscanf(file, "%d", &input);

	if(input == 0) {
		for(i = 0; i < rows; i++) {
			for(j = 0; j < columns; j++) {
				if(i == j) 
					matrix[i][j] = 1;
				else
					matrix[i][j] = 0;
			}
		}
	}

	matrix = mult_matrix_exp(matrix, size, input);
	printMatrix(matrix, rows, columns);
	freeMatrix(matrix, rows, columns);
	return 0;
}
