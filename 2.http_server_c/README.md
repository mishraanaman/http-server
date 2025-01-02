# Simple C HTTP Server

This project demonstrates a simple HTTP server implemented in C. The server provides two main functionalities: managing a list of "friends" and displaying a basic HTML message.

## Features

1. **Friends API**:
   - **GET /friends**: Retrieves the list of all friends.
   - **GET /friends/:id**: Retrieves a specific friend by ID.
   - **POST /friends**: Adds a new friend to the list.

2. **Messages API**:
   - **GET /messages**: Returns a simple HTML page with example messages.

## Getting Started

### Prerequisites
- [Node.js](https://nodejs.org/) installed on your system.

### Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Compile the Code**:
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

## API Endpoints

### 1. Friends API

#### **GET /friends**
Retrieve the list of all friends.
- **Response**: JSON array of friends.
  ```json
  [
    { "id": 0, "name": "Sir Isaac Newton" },
    { "id": 2, "name": "Sir Albert Einstein" },
    { "id": 3, "name": "Sir John Jacobs" }
  ]
  ```

#### **GET /friends/:id**
Retrieve a specific friend by ID.
- **Response**: JSON object of the friend.
  ```json
  { "id": 0, "name": "Sir Isaac Newton" }
  ```

#### **POST /friends**
Add a new friend.
- **Request**: JSON object containing the friend's details.
  ```json
  { "id": 4, "name": "New Friend" }
  ```
- **Response**: The same JSON object as the request.

### 2. Messages API

#### **GET /messages**
Display a simple HTML page with example messages.
- **Response**: HTML content.
  ```html
  <html>
    <body>
      <li>Hello Isaac</li>
      <li>What are your thoughts on Astronomy</li>
    </ul>
    </body>
  </html>
  ```

## Code Overview

### Key Modules
- **http**: Used to create the server and handle HTTP requests.

### Main Logic
- **Routing**: Handled by parsing `req.url` and `req.method`.
- **Data Handling**: Processes incoming data streams for POST requests.
- **Response Management**: Sets appropriate HTTP headers and formats responses as JSON or HTML.

### Example POST Request
To add a new friend, you can use tools like `curl` or Postman:
```bash
curl -X POST -H "Content-Type: application/json" -d '{"id": 4, "name": "New Friend"}' http://localhost:3000/friends
```

## Notes
- The server does not persist data; all changes are lost upon restart.
- Error handling for invalid requests is minimal and can be extended.


---

