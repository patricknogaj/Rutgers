#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int get(unsigned short input, int index) {
	int output = 0;	
	output = (input >> index) & 1;
	return output;
}

int set(unsigned short input, int index, int number) {
	int output = 0;
	int mask = 1 << index;
	output = (input & ~mask) | ((number << index) & mask);
	return output;
}


int comp(unsigned short input, int index) {
	int mask = 1 << index;
	unsigned short output = (input & ~mask) | ((~get(input, index) << index) & mask);
	return output;
}
		
int main(int argc, char *argv[]) {

	FILE *file = fopen(argv[1], "r");

	if(argc != 2) {
		printf("Invalid argument amount.");
	}

	if(file == NULL) {
		printf("error");
	}

	unsigned short x = 0;
	char input[20];
	int num1 = 0, num2 = 0;

	if(!feof(file) && fscanf(file, " %hu ", &x)) {
		//printf("Orig value: %hu\n", x);
	}

	while(!feof(file) && fscanf(file, " %s\t%d\t%d ", input, &num1, &num2)) {
		if(strcmp(input, "get") == 0) {
			printf("%hu\n", get(x, num1));
		} else if(strcmp(input, "set") == 0) {
			x = set(x, num1, num2);
			printf("%hu\n", x);
		} else if(strcmp(input, "comp") == 0) {
			x = comp(x, num1);
			printf("%hu\n", x);
		} else {
			break;
		}
	}

	return 0;
}