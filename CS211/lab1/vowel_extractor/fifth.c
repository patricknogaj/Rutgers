#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int check_vowel(char c) {
	if(c == 'a' || c == 'A' || c == 'e' || c == 'E' || c == 'i' || c == 'I' || c == 'o' || c == 'O' || c == 'u' || c == 'U')
		return 1;
	else
		return 0;
}

int main(int argc, char *argv[]) {
	int i, j, k = 0;
	char *input;
	char result[1000];
	
	for(i = 1; i < argc; i++) {
		input = argv[i];

		for(j = 0; input[j] != '\0'; j++) {
			if(check_vowel(input[j]) == 1) {
				result[k] = input[j];
				k++;
			}
		}	
	}
	result[k] = '\0';
	printf("%s", result);

	return 0;
}