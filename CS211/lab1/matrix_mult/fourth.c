#include <stdio.h>
#include <stdlib.h>

int **create_matrix(int **matrix, int rows, int columns) {
	int i = 0;
	matrix = malloc(sizeof(int*) * rows);
	
	for(i = 0; i < rows; i++) {
		matrix[i] = malloc(sizeof(int) * columns);
	} 
	return matrix;
}

int **mult_matrix(int **a, int **b, int rowsA, int columnsA, int rowsB, int columnsB) {
	int i = 0, j = 0, k = 0;
	int **temp = NULL;
	temp = create_matrix(temp, rowsA, columnsB);
	int sum = 0;
	
	for(i = 0; i < rowsA; i++) {
		for(j = 0; j < columnsB; j++) {
			for(k = 0; k < columnsA; k++) {
				sum += a[i][k] * b[k][j];
			}	
			temp[i][j] = sum;
			sum = 0;
		}
	} return temp;
} 

void print_matrix(int **matrix, int rows, int columns) {
	int i = 0, j = 0;

	for(i = 0; i < rows; i++) {
		for( j = 0; j < columns; j++) {
			if(j == (columns - 1))
				printf("%d", matrix[i][j]);
			else
				printf("%d\t", matrix[i][j]);
		}
		printf("\n");
	}
}

void free_matrix(int **matrix, int rows, int columns) {
	int i = 0;

	for(i = 0; i < rows; i++) {
		free(matrix[i]);
	}
	free(matrix);
}

int main(int argc, char *argv[]) {

	int **matrixA = NULL;
	int **matrixB = NULL;
	int **matrixProduct = NULL;
	FILE *file = fopen(argv[1], "r");
	int rowsA, columnsA, rowsB, columnsB;
	int i = 0, j = 0, input;

	if(file != NULL) {
		while(!feof(file)) {
			fscanf(file, " %d %d", &rowsA, &columnsA);
			break;
		} 
	} else {
			printf("File not found.");	
	}

	//printf("%d %d", rowsA, columnsA);

	matrixA = create_matrix(matrixA, rowsA, columnsA);

	for(i = 0; i < rowsA; i++) {
		for(j = 0; j < columnsA; j++) {
			fscanf(file, " %d ", &input);
			matrixA[i][j] = input;
		}
	}
	
	while(!feof(file)) {
		fscanf(file, " %d %d", &rowsB, &columnsB);
		break;
	}

	//printf("%d %d", rowsB, columnsB);
	matrixB = create_matrix(matrixB, rowsB, columnsB);
	
	for(i = 0; i < rowsB; i++) {
		for(j = 0; j < columnsB; j++) {
			fscanf(file, "%d", &input);
			matrixB[i][j] = input;
		}
	}

	if(columnsA != rowsB) {
		printf("bad-matrices");	
	} else {
		matrixProduct = create_matrix(matrixProduct, rowsA, columnsB);
		matrixProduct = mult_matrix(matrixA, matrixB, rowsA, columnsA, rowsB, columnsB);
		print_matrix(matrixProduct, rowsA, columnsB);
		free_matrix(matrixProduct, rowsA, columnsB);
	}

	free_matrix(matrixA, rowsA, columnsA);
	free_matrix(matrixB, rowsB, columnsB);
	return 0;
}
