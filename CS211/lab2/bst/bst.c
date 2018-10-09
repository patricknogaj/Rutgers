#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct node {
	int value;
	struct node* left;
	struct node* right;
} node;

int compare(int left, int right);
node *find_max(node *root);

node *insert(node *root, int value) {
	if(root == NULL) {
		root = malloc(sizeof(node));
		root->left = root->right = NULL;
		root->value = value;
		printf("inserted");
		return root;
	} else {
		if(compare(value, root->value) == -1)
			root->left = insert(root->left,value);
		else if(compare(value, root->value) == 1) 
			root->right = insert(root->right, value);
		else
			printf("duplicate");
	}
	return root;
}

int search(node *root, int value) {
	while(root) {
		if(root->value == value) {
			return 1;
		}
		if(value < root->value) 
			root = root->left;
		else
			root = root->right;
	}
	return 0;
}

int compare(int left, int right) {
	if(left > right) 
		return 1;
	if(left < right)
		return -1;
	return 0;
}

void print(node *root) {
	if(root == NULL)
		return;
	if(root != NULL) {
		printf("(");
		if(root->left != NULL) {
			print(root->left);
		}
		printf("%d", root->value);
		if(root->right != NULL) {
			print(root->right);
			
		}
	
	}
	printf(")");
}

node *delete(node *root, int value) {

	if(root == NULL)
		return root;

	if(value < root->value) 
		root->left = delete(root->left, value);
	else if (value > root->value)
		root->right = delete(root->right, value);
	else {
		if(root->left == NULL && root->right == NULL) {
			root = NULL;
		} else if(root->left == NULL) {
			root = root->right;
		} else if(root->right == NULL) {
			root = root->left;
		} else {
			node *temp = find_max(root->left);
			root->value = temp->value;
			root->left = delete(root->left, temp->value);
		}	
	} 
	return root;
}	

node *find_max(node *root) {
	if(root->right == NULL)
		return root;
	return find_max(root->right);
}

int main() {
	node* bst = NULL;
	char input;
	int value;

	while(!feof(stdin) && fscanf(stdin, " %c %d ", &input, &value)) {
		if(input == 'i') {
			bst = insert(bst, value);
		} else if(input == 'd') {
			if(search(bst, value) == 0)
				printf("absent");
			else {
				bst = delete(bst, value);
				printf("deleted");
			}
		} else if(input == 's') {
			if(search(bst, value) == 0)
				printf("absent");
			else
				printf("present");
		} else if(input == 'p') {
			print(bst);
		} else {
			break;
		}
		printf("\n");
	}
	
	free(bst);
	return EXIT_SUCCESS;
}
