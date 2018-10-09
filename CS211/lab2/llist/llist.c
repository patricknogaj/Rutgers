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

	printf("%d :", counter);
	while(temp != NULL) {
		printf(" %d", temp->value);
		temp = temp->next;
	}
	printf("\n");
}

void free_list(node *n) {
	while(n != NULL) {
		free(n);
		n = n->next;
	}
}

int main() {
	node *temp = NULL;
	char input;
	int value;
	int counter = 0;
	
	while(!feof(stdin) && fscanf(stdin, " %c %d ", &input, &value)) {
		if(input == 'i') {
			if(!search(temp, value))
				counter++;
			temp = push(temp, value);
		} else if(input == 'd') {
			if(search(temp, value))
				counter--;
			temp = delete(temp, value);
		}
		print(temp, counter);
	}
	free(temp);
	return EXIT_SUCCESS; 
}
