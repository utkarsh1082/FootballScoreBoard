Football Scoreboard Keeper- An application to create, update and find scores of football matches.

I have divided the P.S. into 2 parts-
1. Create football match
2. Get and update score of the football matches

To test the app, clone into your system and run the application on localhost:8080, download the Postman collection of the requests to test the applications. 
The collection is attached in the main REPO with the name Football Scoreboard.postman_collection. 

The APIs are as follows-
1. Create Match- POST API to create a new Match, required arguments-> team1 name, team2 name and duration of match. Start time is not mandatory, if not provided current time is taken as start time.
2. Get Match Details- GET API call to get match details by providing match id
3. Get Scoreboard for a match- GET API call to get current scoreboard of match by matchID.
4. Update Scoreboard- PUT API call to update the scoreboard by providing the match id and name of the team which scored the goal.

Current Test Coverage- 89.6%

The DB storage used is Firebase since we need a NOSQL DB as the data was not required to be relational and was vertically increasing.


