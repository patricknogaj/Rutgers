#include <stdio.h>
#include <stdlib.h>

signed int gcd(signed int i, signed int j) {
	if(j == 0) 
		return i;
	return gcd(j, i % j);
}

int main(int argc, char *argv[]) {
	
	if(argc != 3) {
		printf("Error: invalid argument size.");
		return EXIT_FAILURE;
	}

	signed int i = atoi(argv[1]);
	signed int j = atoi(argv[2]);

	printf("%d\n", gcd(i, j));

	return 0;
}

