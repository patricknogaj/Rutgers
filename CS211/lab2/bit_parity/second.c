#include <stdlib.h>
#include <stdio.h>

int get(unsigned short input, int index) {
	int output = 0;	
	output = (input >> index) & 1;
	return output;
}

int main(int argc, char *argv[]) {

	unsigned short x = atoi(argv[1]);

	int i = 0;

	int evens = 0;
	int evenAmt = 0;
	
	for(i = 0; i < 16; ++i) {
		if(get(x, i) == 1)
			evenAmt++;
	}

	for(i = 0; i < 15; i+=2) {
		if((get(x, i) && get(x, i + 1)) == 1) {
			evens++;
		} else {
			i--;
		}
	}

	if(evenAmt % 2 == 0)
		printf("Even-Parity\t%d", evens);
	else
		printf("Odd-Parity\t%d", evens);

	return 0;
}