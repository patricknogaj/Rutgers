#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int has_numbers(char text[]) {
	int value = 0, i = 0;	
	for(i = 0; i < sizeof(text); i++) {
		if(isdigit(text[i])) {
			value = 1;
			break;
		}
	}
	return value;
}

char *rle(char *text) {
	int i = 0, count = 1, len = strlen(text);
	char curr = text[0], prev;
	char *compressed = malloc(sizeof(char) * (len * 2 + 1));
	char *temp = malloc(sizeof(char) * (len * 2 + 1));

	for(i = 0; text[i] != '\0'; i++) {
		
		if(curr == text[i+1]) {
			count++;
		} else {
			prev = curr;
			sprintf(temp, "%c%i", prev, count);
			strcat(compressed, temp);	
			curr = text[i + 1];
			count = 1;
		}	
	}	
	return compressed;
}


int main(int argc, char *argv[]) {

	if(argc != 2) {
		printf("error");
		return EXIT_FAILURE;
	} else {
		if(has_numbers(argv[1])== 1) {
			printf("error");
			return EXIT_FAILURE;
		}
	}
	char *output = rle(argv[1]);
	
	if(strlen(output) <= strlen(argv[1]))
		printf("%s", output);
	else
		printf("%s", argv[1]);

	return 0;
}
