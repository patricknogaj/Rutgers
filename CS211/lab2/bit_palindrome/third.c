#include <stdio.h>
#include <stdlib.h>

int get(unsigned short input, int index) {
	int output = 0;	
	output = (input >> index) & 1;
	return output;
}

int main(int argc, char *argv[]) {

	unsigned short x = 0;

	if(argc != 2) {
		printf("ERROR: too many arguments");
	} else {
		x = atoi(argv[1]);
	}

	int i = 0, j = 0;
	int isPalindrome = 1;

	for(i = 0, j = 15; i < 8 && j >= 8; i++, j--) {
		if(get(x, i) != get(x, j)) {
			isPalindrome = 0;
			break;
		}
	}

	if(isPalindrome == 1) 
		printf("Is-Palindrome");
	else
		printf("Not-Palindrome");

	return 0;
}