# Tarrie API API documentation version v1
http://api.tarrie.com/{version}

---

## /users

### /users

* **post**: Add a new user to Tarrie - I think this is were JWT token will be set up

### /users/{userID}

* **get**: Get a specific user by userID
* **put**: Edit an existing user - Edit profile
* **delete**: Delete an existing user - Deactivate account basically

## /events

### /events

* **post**: Add a new event to Tarrie

### /events/{eventID}

* **get**: Get a specific event by userID
* **put**: Edit an existing event - Edit event
* **delete**: Delete an existing event - Delete Event

## /groups

### /groups

* **get**: Get a list of all groups
* **post**: Creates a new group.

### /groups/{groupID}

* **get**: Get a specific group by userID
* **put**: Edit an existing group - Edit group
* **delete**: Delete an existing group - Delete group

