#include <assert.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SOURCE_ID 0
#define SINK_ID 1
#define NODE_ID(x, y) (2 + (y) * width + (x))
#define NODE_BY_ID(node_id) (graph->node_buf + (node_id))
#define MIN(a, b) (((a) < (b)) ? (a) : (b))

/*****************************************************************************
 * BEGIN Graph
 ****************************************************************************/

struct GraphEdge;

struct GraphNode {
    int id;
    int num_neighbors;
    struct GraphEdge *neighbors;
};

struct GraphEdge {
    struct GraphNode *node;
    int capacity;
    int f;
};

struct Graph {
    int num_nodes;
    int next_neighbor;
    struct GraphNode *node_buf;
    struct GraphEdge *edge_buf;
};

struct Graph *graph_alloc(int num_nodes, int num_edges) {
    struct Graph *graph = malloc(sizeof(struct Graph));
    graph->num_nodes = num_nodes;
    graph->node_buf = malloc(num_nodes * sizeof(struct GraphNode));
    graph->edge_buf = malloc(num_edges * sizeof(struct GraphEdge));
    graph->next_neighbor = 0;
    return graph;
}

void graph_free(struct Graph *graph) {
    free(graph->node_buf);
    free(graph->edge_buf);
    free(graph);
}

struct GraphNode *graph_setup_node(struct Graph *graph, int id, int max_neighbors) {
    struct GraphNode *node = NODE_BY_ID(id);
    node->id = id;
    node->num_neighbors = 0;
    node->neighbors = graph->edge_buf + graph->next_neighbor;
    graph->next_neighbor += max_neighbors;
    return node;
}

struct GraphEdge *graph_get_or_create_edge(struct GraphNode *a, struct GraphNode *b) {
    int i;
    for (i = 0; i < a->num_neighbors; i++) {
        if (a->neighbors[i].node == b) {
            return &a->neighbors[i];
        }
    }

    a->num_neighbors++;
    a->neighbors[i].node = b;
    a->neighbors[i].capacity = 0;
    a->neighbors[i].f = 0;
    return &a->neighbors[i];
}

void graph_add_edge(struct Graph *graph, struct GraphNode *a, int b_id, int capacity) {
    struct GraphNode *b = NODE_BY_ID(b_id);
    graph_get_or_create_edge(a, b)->capacity += capacity;
    graph_get_or_create_edge(b, a)->capacity += capacity;
}

/*****************************************************************************
 * END Graph
 ****************************************************************************/

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

struct Graph *read_graph() {
    int width, height, line_size, x, y;
    struct Graph *graph;
    struct GraphNode *node;
    char *line, *prev_line, *temp_line;

    scanf("%d %d ", &height, &width);
    graph = graph_alloc(
        width * height + 2,
        /* source */ (width + height) * 2 +
        /* sink */ width * height +
        /* inner nodes */ width * height * 5);

    line_size = width + 5;
    line = malloc(line_size);
    prev_line = malloc(line_size);

    graph_setup_node(graph, SOURCE_ID, (width + height) * 2);
    graph_setup_node(graph, SINK_ID, width * height);

    for (y = 0; y < height; y++) {
        fgets(line, line_size, stdin);
        assert(strlen(line) >= width);

        for (x = 0; x < width; x++) {
            if (line[x] == 'X')
                continue;
            node = graph_setup_node(graph, NODE_ID(x, y), 5);
            if (line[x] == 'D')
                graph_add_edge(graph, node, SINK_ID, 4);
            if (y == 0)
                graph_add_edge(graph, node, SOURCE_ID, 1);
            else if (prev_line[x] == '.' || prev_line[x] == 'D')
                graph_add_edge(graph, node, NODE_ID(x, y - 1), 1);
            if (x == 0)
                graph_add_edge(graph, node, SOURCE_ID, 1);
            else if (line[x - 1] == '.' || line[x - 1] == 'D')
                graph_add_edge(graph, node, NODE_ID(x - 1, y), 1);
            if (x == width - 1)
                graph_add_edge(graph, node, SOURCE_ID, 1);
            if (y == height - 1)
                graph_add_edge(graph, node, SOURCE_ID, 1);
        }

        temp_line = prev_line;
        prev_line = line;
        line = temp_line;
    }

    free(line);
    free(prev_line);
    return graph;
}

/* Based on http://en.wikibooks.org/wiki/Algorithm_implementation/Graphs/Maximum_flow/Edmonds-Karp */
int max_flow(struct Graph *graph) {
    struct GraphNode **parents, *u, *v;
    struct Queue *queue;
    struct GraphEdge *u_v;
    int *path_capacity, total, u_v_idx, i;

    parents = malloc(graph->num_nodes * sizeof(struct GraphNode*));
    path_capacity = malloc(graph->num_nodes * sizeof(int));
    queue = queue_alloc();

    while (1) {
        memset(parents, 0, graph->num_nodes * sizeof(struct GraphNode*));
        parents[SOURCE_ID] = NODE_BY_ID(SOURCE_ID);
        memset(path_capacity, 0, graph->num_nodes * sizeof(int));
        path_capacity[SOURCE_ID] = INT_MAX;

        queue_push_back(queue, SOURCE_ID);
        while (queue->front) {
            u = NODE_BY_ID(queue_pop_front(queue));
            for (u_v_idx = 0; u_v_idx < u->num_neighbors; u_v_idx++) {
                u_v = &u->neighbors[u_v_idx];
                v = u_v->node;

                if ((u_v->capacity - u_v->f > 0) && parents[v->id] == NULL) {
                    parents[v->id] = u;
                    path_capacity[v->id] = MIN(path_capacity[u->id], u_v->capacity - u_v->f);
                    if (v->id != SINK_ID) {
                        queue_push_back(queue, v->id);
                    } else {
                        while (parents[v->id] != v) {
                            u = parents[v->id];
                            graph_get_or_create_edge(u, v)->f += path_capacity[SINK_ID];
                            graph_get_or_create_edge(v, u)->f -= path_capacity[SINK_ID];
                            v = u;
                        }
                        break;
                    }
                }
            }
        }
        if (parents[SINK_ID] == NULL) {
            break;
        }
    }

    total = 0;
    u = NODE_BY_ID(SOURCE_ID);
    for (i = 0; i < u->num_neighbors; i++) {
        total += u->neighbors[i].f;
    }
    free(parents);
    free(path_capacity);
    queue_free(queue);
    return total;
}

int main() {
    int num_cases, i;
    struct Graph *graph;

    scanf("%d", &num_cases);
    for (i = 0; i < num_cases; i++) {
        graph = read_graph();
        printf("%d\n", max_flow(graph));
        graph_free(graph);
    }

    return 0;
}