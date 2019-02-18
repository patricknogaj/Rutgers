#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <ctype.h>

typedef struct node {
	int value;
	struct node *next;	
} node;

bool search(node *n, int compare) {
	node *curr = n;

	while(curr != NULL) {
		if(curr->value == compare)
			return true;
		curr = curr->next;
	}	
	return false;
}

node *push(node *n, int new) {

	node *temp = (node*)malloc(sizeof(node));
	temp->value = new;
	temp->next = NULL;

	if(n == NULL) {
		n = temp;
		return n;
	}

	if(search(n, new)) {
		return n;
	} else {
		if(n->value > new) {
			temp->next = n;
			return temp;
		} else {
			node *curr = NULL;
			node *prev = NULL;
			curr = n;

			while(curr != NULL && curr->value < new) {
				prev = curr;
				curr = curr->next;
			}
			if(curr != NULL) {
				temp->next = curr;
				prev->next = temp;
				return n;
			} else {
				prev->next = temp;
				return n;
			}
		}   
	}  
}

node *push2(node *n, int data) {
	node *temp = malloc(sizeof(node));
	temp->value = data;
	temp->next = NULL;

	if(n == NULL) {
		n = temp;
		return n;
	}

	if(n->value > data || n->value == data) {
		temp->next = n;
		return temp;
	} else {
		node *curr = NULL;
			node *prev = NULL;
			curr = n;

			while(curr != NULL && curr->value < data) {
				prev = curr;
				curr = curr->next;
			}
			if(curr != NULL) {
				temp->next = curr;
				prev->next = temp;
				return n;
			} else {
				prev->next = temp;
				return n;
		}
	}   
}

node *delete(node *n, int value) {
	node *temp = n;
	node *temp2 = NULL;
	
	if(n == NULL) {
		return n;
	}
	while(temp != NULL) {
		if(temp-> value == value) {
			break;
		}
		temp2 = temp;
		temp = temp->next;
	}

	if(temp == NULL) {
		return n;
	}
	if(temp2 == NULL) {
		n = temp->next;
	} else {
		temp2->next = temp->next;
	}
	free(temp);
	return n;
}

void print(node *ref, int counter) {
	node *temp = ref;

	int *array = malloc(sizeof(int) * counter);
	int i = 0, j = 0;

	printf("%d\n", counter);
	while(temp != NULL) {
		for(i = 0; i < counter; i++) {
			array[i] = temp->value;
			temp = temp->next;
		}
	}

	for(i = 0; i < counter; i++) {
		for(j = i + 1; j < counter; j++) {
			if(array[i] == array[j])
				i++;
			}
		printf("%d\t", array[i]);
	}
	printf(" ");
	
}

void free_list(node *n) {
	while(n != NULL) {
		free(n);
		n = n->next;
	}
}

int main(int argc, char *argv[]) {
	node *temp = NULL;
	char input;
	int value;
	int counter = 0;
	FILE *file = fopen(argv[1], "r");

	if(file == NULL) {
		printf("error");
	}
	
	while(!feof(file) && fscanf(file, " %c %d ", &input, &value)) {
		//printf("%c %d\n", input, value);
		
		if(input == 'i') {
			temp = push2(temp, value);
			counter++;
		} else if(input == 'd') {
			if(search(temp, value))
				counter--;
			temp = delete(temp, value);
		}
	}
	print(temp, counter);
	//free_list(temp);
	return 0; 
}
