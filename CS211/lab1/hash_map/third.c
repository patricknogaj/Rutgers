#include <stdio.h>
#include <stdlib.h>

#define SIZE 10000

typedef struct node {
	int key;
	int data;
} node;

node *hashArray[SIZE];
int success = 0;
int collision = 0;

int hashCode(int key) {
	if(key < 0)
		return (key * -1) % SIZE;
	else
		return key % SIZE;
}

node *search(int key) {
	int index = hashCode(key);

	while(hashArray[index] != NULL) {
		if(hashArray[index]->key == key)
			return hashArray[index];

		++index;
		index %= SIZE;
	}
	return NULL;
}

void insert(int key, int data) {
	node *temp = malloc(sizeof(node));
	temp->key = key;
	temp->data = data;

	int index = hashCode(key);

	if(search(index))
		collision++;

	while(hashArray[index] != NULL && hashArray[index]->key != -1) {
		++index;
		index %= SIZE;
	}

	hashArray[index] = temp;
}

int main(int argc, char *argv[]) {

	FILE *file = fopen(argv[1], "r");

	if(file == NULL) {
		printf("error");
	}

	char cmd;
	int input;

	while (!feof(file) && fscanf(file, " %c %d ", &cmd, &input)) {
		if(cmd == 'i') {
			insert(input, input);
		} else if(cmd == 's') {
			if(search(input) != NULL) 
				success++;
		}
	}

	printf("%d\n%d\n", collision, success);


	return 0;
}