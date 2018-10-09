#include <stdio.h>
#include <stdlib.h>

void rot13(char *text) {
	int i = 0;
	for(i = 0; text[i] != '\0'; i++) {
		if((*(text + i) >= 'a' && *(text + i) < 'n') ||  (*(text + i) >= 'A' && *(text + i) < 'N'))
				*(text + i) += 13;

		else if((*(text + i) >= 'n' && *(text + i) <= 'z') || (*(text +i) >= 'N' && *(text + i) <= 'Z'))
			*(text + i) -= 13;
	}
}

int main(int argc, char *argv[]) {

	if(argc != 2) {
		printf("Error: too many arguments");
		return EXIT_FAILURE;
	}

	rot13(argv[1]);
	printf("%s", argv[1]);

	return 0;
}
