#include <assert.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NODES 101
#define START_ID 0
#define SUPPLY_ID (num_nodes - 1)

int num_nodes;
char callsigns[MAX_NODES][7];
int edges[MAX_NODES][MAX_NODES];
int path_costs[MAX_NODES];

/*****************************************************************************
 * BEGIN Queue
 ****************************************************************************/

struct QueueNode {
    int value;
    struct QueueNode *next;
};
static struct QueueNode *queue_freelist = NULL;

struct Queue {
    struct QueueNode *front;
    struct QueueNode *back;
};

struct Queue *queue_alloc() {
    struct Queue *queue = malloc(sizeof(struct Queue));
    queue->front = queue->back = NULL;
    return queue;
}

void queue_push_back(struct Queue *queue, int value) {
    struct QueueNode *node;
    if (queue_freelist) {
        node = queue_freelist;
        queue_freelist = node->next;
    } else {
        node = malloc(sizeof(struct QueueNode));
    }
    node->value = value;
    node->next = NULL;
    if (queue->back) {
        queue->back->next = node;
        queue->back = node;
    } else {
        queue->front = queue->back = node;
    }
}

int queue_pop_front(struct Queue *queue) {
    struct QueueNode *node = queue->front;
    int value = -1;
    if (node) {
        queue->front = node->next;
        if (queue->front == NULL)
            queue->back = NULL;
        value = node->value;
        node->next = queue_freelist;
        queue_freelist = node;
    }
    return value;
}

void queue_free(struct Queue *queue) {
    if (queue->front) {
        queue->back->next = queue_freelist;
        queue_freelist = queue->front;
    }
    free(queue);
}

/*****************************************************************************
 * END Queue
 ****************************************************************************/

int callsign_to_id(char *callsign) {
    assert(strlen(callsign) == 6);
    for (int i = 0; i < num_nodes; i++) {
        if (strcmp(callsigns[i], callsign) == 0) {
            return i;
        }
    }
    assert(0);
    return 0;
}

void shortest_path(int start_node, int start_cost) {
    for (int i = 0; i < num_nodes; i++) {
        path_costs[i] = INT_MAX;
    }
    path_costs[start_node] = start_cost;

    struct Queue *queue = queue_alloc();
    queue_push_back(queue, start_node);
    while (queue->front) {
        int node = queue_pop_front(queue);
        for (int neighbor = 0; neighbor < num_nodes; neighbor++) {
            if (edges[node][neighbor] != INT_MAX) {
                int new_cost = path_costs[node] + edges[node][neighbor];
                if (new_cost < path_costs[neighbor]) {
                    path_costs[neighbor] = new_cost;
                    queue_push_back(queue, neighbor);
                }
            }
        }
    }
    queue_free(queue);
}

void solve_case() {
    int num_outposts, num_roads;
    scanf("%d %d", &num_outposts, &num_roads);
    num_nodes = num_outposts + 1;

    int extra_ammo_id = -1;
    int start_ammo = 0, extra_ammo = 0;
    for (int i = 0; i < num_nodes; i++) {
        for (int j = 0; j < num_nodes; j++) {
            edges[i][j] = INT_MAX;
        }
    }

    for (int i = 0; i < num_outposts; i++) {
        int ammo;
        char has_supplies[4];

        scanf("%s %d %s", callsigns[i], &ammo, has_supplies);
        if (i == 0) {
            start_ammo = ammo;
        }
        if (strcmp(has_supplies, "yes") == 0) {
            edges[i][SUPPLY_ID] = 0;
        } else if (i > 0 && ammo > 0) {
            extra_ammo_id = i;
            extra_ammo = ammo;
        }
    }

    for (int i = 0; i < num_roads; i++) {
        char callsign_a[7], callsign_b[7];
        int z;
        scanf("%s %s %d", callsign_a, callsign_b, &z);
        int a = callsign_to_id(callsign_a);
        int b = callsign_to_id(callsign_b);
        edges[a][b] = z;
        edges[b][a] = z;
    }

    /* Attempt to find a safe path to a supply outpost without picking up extra ammo. */
    shortest_path(START_ID, 0);
    if (path_costs[SUPPLY_ID] <= start_ammo) {
        printf("%d\n", path_costs[SUPPLY_ID]);
        return;
    }

    /* Attempt to find a safe path to an outpost with extra ammo. */
    if (extra_ammo_id >= 0 && path_costs[extra_ammo_id] <= start_ammo) {
        /* Attempt to find a safe path from there to a supply outpost. */
        shortest_path(extra_ammo_id, path_costs[extra_ammo_id]);
        if (path_costs[SUPPLY_ID] <= start_ammo + extra_ammo) {
            printf("%d\n", path_costs[SUPPLY_ID]);
            return;
        }
    }

    puts("No safe path");
}

int main() {
    int num_cases;
    scanf("%d", &num_cases);
    for (int i = 0; i < num_cases; i++) {
        solve_case();
    }
    return 0;
}