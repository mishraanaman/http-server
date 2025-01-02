package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
)

// Friend struct to hold friend details
type Friend struct {
	ID   int    `json:"id"`
	Name string `json:"name"`
}

// Initialize a slice of friends
var friends = []Friend{
	{ID: 0, Name: "Sir Isaac Newton"},
	{ID: 2, Name: "Sir Albert Einstein"},
	{ID: 3, Name: "Sir John Jacobs"},
}

func main() {
	http.HandleFunc("/friends", handleFriends)
	http.HandleFunc("/messages", handleMessages)

	fmt.Println("Server started on port 3000")
	http.ListenAndServe(":3000", nil)
}

// Handler for /friends
func handleFriends(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		handleGetFriends(w, r)
	case http.MethodPost:
		handlePostFriend(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// GET /friends handler
func handleGetFriends(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	idStr := r.URL.Query().Get("id")
	if idStr != "" {
		id, err := strconv.Atoi(idStr)
		if err != nil || id < 0 || id >= len(friends) {
			http.Error(w, "Friend not found", http.StatusNotFound)
			return
		}
		json.NewEncoder(w).Encode(friends[id])
	} else {
		json.NewEncoder(w).Encode(friends)
	}
}

// POST /friends handler
func handlePostFriend(w http.ResponseWriter, r *http.Request) {
	var friend Friend
	err := json.NewDecoder(r.Body).Decode(&friend)
	if err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}
	friend.ID = len(friends) // Assign an ID automatically
	friends = append(friends, friend)
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(friend)
}

// Handler for /messages
func handleMessages(w http.ResponseWriter, r *http.Request) {
	if r.Method == http.MethodGet {
		w.Header().Set("Content-Type", "text/html")
		fmt.Fprintf(w, `
			<html>
				<body>
					<ul>
						<li>Hello Isaac</li>
						<li>What are your thoughts on Astronomy?</li>
					</ul>
				</body>
			</html>`)
	} else {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}
