#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 3000
#define BUFFER_SIZE 4096

typedef struct {
    int id;
    char name[50];
} Friend;

Friend friends[] = {
    {0, "Sir Isaac Newton"},
    {2, "Sir Albert Einstein"},
    {3, "Sir John Jacobs"}
};

int friends_count = sizeof(friends) / sizeof(friends[0]);

void handle_client(int client_socket) {
    char buffer[BUFFER_SIZE];
    int read_size = read(client_socket, buffer, sizeof(buffer) - 1);
    if (read_size <= 0) {
        close(client_socket);
        return;
    }

    buffer[read_size] = '\0';

    // Parse HTTP method and URL
    char method[8];
    char url[256];
    sscanf(buffer, "%s %s", method, url);

    if (strcmp(method, "GET") == 0 && strstr(url, "/friends") == url) {
        if (strlen(url) > 8) {
            int id = atoi(url + 9); // Get ID from URL
            char response[BUFFER_SIZE];
            if (id >= 0 && id < friends_count) {
                snprintf(response, sizeof(response),
                         "HTTP/1.1 200 OK\r\n"
                         "Content-Type: application/json\r\n\r\n"
                         "{\"id\": %d, \"name\": \"%s\"}",
                         friends[id].id, friends[id].name);
            } else {
                snprintf(response, sizeof(response),
                         "HTTP/1.1 404 Not Found\r\n"
                         "Content-Type: text/plain\r\n\r\n"
                         "Friend not found.");
            }
            write(client_socket, response, strlen(response));
        } else {
            char response[BUFFER_SIZE];
            snprintf(response, sizeof(response), "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n[");
            for (int i = 0; i < friends_count; i++) {
                char friend_entry[128];
                snprintf(friend_entry, sizeof(friend_entry), "{\"id\": %d, \"name\": \"%s\"}%s",
                         friends[i].id, friends[i].name, (i < friends_count - 1) ? ", " : "");
                strncat(response, friend_entry, sizeof(response) - strlen(response) - 1);
            }
            strncat(response, "]", sizeof(response) - strlen(response) - 1);
            write(client_socket, response, strlen(response));
        }
    } else if (strcmp(method, "GET") == 0 && strstr(url, "/messages") == url) {
        const char *response =
            "HTTP/1.1 200 OK\r\n"
            "Content-Type: text/html\r\n\r\n"
            "<html><body>"
            "<li>Hello Isaac</li>"
            "<li>What are your thoughts on Astronomy</li>"
            "</body></html>";
        write(client_socket, response, strlen(response));
    } else {
        const char *response =
            "HTTP/1.1 404 Not Found\r\n"
            "Content-Type: text/plain\r\n\r\n"
            "Resource not found.";
        write(client_socket, response, strlen(response));
    }

    close(client_socket);
}

int main() {
    int server_socket, client_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_addr_size;

    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Failed to create socket");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(server_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1) {
        perror("Bind failed");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    if (listen(server_socket, 10) == -1) {
        perror("Listen failed");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    printf("Server listening on port %d\n", PORT);

    while (1) {
        client_addr_size = sizeof(client_addr);
        client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_addr_size);
        if (client_socket == -1) {
            perror("Failed to accept connection");
            continue;
        }
        handle_client(client_socket);
    }

    close(server_socket);
    return 0;
}
