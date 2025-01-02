Here is the content formatted in markdown that you can use directly for your GitHub repository as `README.md`:

```markdown
# Simple C HTTP Server

This project is a simple HTTP server implemented in C. It demonstrates basic HTTP server functionality, including handling `GET` requests and serving different types of responses based on the URL.

## Features

- **Serve JSON Data**: Returns a list of friends or a specific friend based on the URL.
- **Serve HTML Content**: Returns a simple HTML page for the `/messages` endpoint.
- **Handle 404 Errors**: Returns an appropriate error response for unsupported endpoints.

## Endpoints

### 1. `/friends`
- **GET /friends**: Returns a JSON array of all friends.
- **GET /friends/{id}**: Returns a JSON object of a specific friend by ID.

**Example Response**:
```json
[
    {"id": 0, "name": "Sir Isaac Newton"},
    {"id": 2, "name": "Sir Albert Einstein"},
    {"id": 3, "name": "Sir John Jacobs"}
]
```

**Specific Friend Example**:
```json
{"id": 0, "name": "Sir Isaac Newton"}
```

### 2. `/messages`
- **GET /messages**: Returns an HTML page with a list of messages.

**Example Response**:
```html
<html>
  <body>
    <li>Hello Isaac</li>
    <li>What are your thoughts on Astronomy</li>
  </body>
</html>
```

### 3. Unsupported Endpoints
- Returns a `404 Not Found` response with a plain text message.

**Example Response**:
```
Resource not found.
```

## How to Run

1. **Compile the Code**:
   Use the following command to compile the code:
   ```bash
   gcc -o server main.c
   ```

2. **Run the Server**:
   Start the server using:
   ```bash
   ./server
   ```

3. **Access the Server**:
   Open your browser or use `curl` to access the server at `http://localhost:3000`.

## Example Usage

### Fetch All Friends
```bash
curl http://localhost:3000/friends
```

### Fetch a Specific Friend
```bash
curl http://localhost:3000/friends/0
```

### Fetch Messages
```bash
curl http://localhost:3000/messages
```

### Unsupported Endpoint
```bash
curl http://localhost:3000/invalid
```


## Known Limitations

1. This server only supports `GET` requests and a basic `POST` is not implemented.
2. It does not use multithreading or asynchronous I/O, so it can handle only one request at a time.
3. No HTTPS support.

## Improvements

- Add multithreading to handle concurrent clients.
- Implement additional HTTP methods such as `POST` and `PUT`.
- Add HTTPS support for secure communication.

