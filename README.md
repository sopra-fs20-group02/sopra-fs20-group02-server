<p>
  <a href="https://github.com/sopra-fs20-group02/sopra-fs20-group02-server/actions">
      <img src="https://github.com/sopra-fs20-group02/sopra-fs20-group02-server/workflows/Deploy%20Project/badge.svg">
  </a>
  <a href="https://heroku-badge.herokuapp.com/?app=sopra-fs20-group-02-server">
      <img src="https://heroku-badge.herokuapp.com/?app=sopra-fs20-group-02-server">
  </a>
  <a href="https://sonarcloud.io/dashboard?id=sopra-fs20-group02_sopra-fs20-group02-server">
      <img src="https://sonarcloud.io/api/project_badges/measure?project=sopra-fs20-group02_sopra-fs20-group02-server&metric=alert_status">
  </a>
  <a href="https://sonarcloud.io/dashboard?id=sopra-fs20-group02_sopra-fs20-group02-server">
      <img src="https://sonarcloud.io/api/project_badges/measure?project=sopra-fs20-group02_sopra-fs20-group02-server&metric=coverage">
  </a>
</p>

# Jess

## Introduction
We aimed to create a state of the art online multiplayer chess game, called Jess, where users can easily create
and join games with different rules. Besides, there is an extensive user statistics page and a chat 
functionality. The main focus was implementing our own chess engine where all possible moves can 
be fetched from the backend and displayed to the user. This enables users with less knowledge about
the game to perform better. 

## Technologies
* Spring
* WebSocket
* Lombok
* Github
* Heroku
* SonarQube
* Java

## High-Level components
### [Board](https://github.com/sopra-fs20-group02/sopra-fs20-group02-server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/logic/Board.java)

The game board is the heart piece of the entire game logic. It can be setup with: 

```{Java}
board.setGame(game)
```
Where ``game`` is an instance of the game database class. ``setGame`` will copy all the values from
the database game class into the current instance of the board class. The board class can then perform
most important computations for the current game, since it knows about all the ``Piece`` instances. 

For example one can call ``board.getPossibleMoves(pieceId)`` to get all possible moves for the piece
with the specified id. 

### [Piece](https://github.com/sopra-fs20-group02/sopra-fs20-group02-server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/logic/Piece.java)

From the abstract class ``Piece`` all the specific piece classes are derived, namely King, Queen, Rook, Pawn, Bishop and Knight. They contain information about
the piece's specific freedoms in movement and cover special cases which need to be considered. For example
the pawn class needs to set its ``movementSteps`` field to 1 after completing its first move. Since the pawn
can walk 2 tiles but only in the very first move, which makes it along with the Kind and the Rook the only 
pieces which have different freedoms in movement in different stages of the game. 

### [Vector](https://github.com/sopra-fs20-group02/sopra-fs20-group02-server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/logic/Vector.java)

We designed a vector class specificially for this game in order to simplify the game logic. The vectors are constricted
between (1,1) and (8,8), since the game board only consists of 8x8 tiles. It has different basic methods such as:
``add`` which will add another vector, ``mulS`` which multiplies the its current x and y components by a integer 
scalar. The vector class is used in the ``Piece`` and ``Board`` class.

### [GameService](https://github.com/sopra-fs20-group02/sopra-fs20-group02-server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/GameService.java)

The GameService is the connecting piece between the JPA Database, the game logic and the GameController, which handles
all the REST requests related to the game. If a REST request is made, the GameService fetches the corresponding game
from the database, gives it to the board where it is processed according to the request and then saves the data back to
the database. It has different functionalities, like ``createNewGame``, ``joinGame`` or ``makeMove``.

## Launch & Deployment
#### Get started
1. Download an IDE (e.g., Eclipse, IntelliJ)
2. Make sure you have Java 13 installed on your system
3. Clone the github repository
    ```
    $ git clone git@github.com:sopra-fs20-group02/sopra-fs20-group02-server.git
    ```
4. Open the project with your IDE
5. Build: right click the ``build.gradle`` file and choose ``Run Build``

#### Run backend
To run the project locally you can start the backend in the development mode:
1. Open two terminal windows
2. Navigate in both windows to the project folder
3. Run in on window
    #### `./gradlew build –continuous`
    or  if you want to avoid running all tests with every change
    #### `./gradlew build --continuous -xtest`
4. Run in the other terminal
    #### `./gradlew bootRun`
   
With every push to the master branch the project gets automatically deployed to Heroku. <br/>
Please find it under the following URL:

http://sopra-fs20-group-02-server.herokuapp.com/


## Roadmap
* Chess Engine: Add a chess bot
* More game modes: Extend the game with some more game modes
* Level of support: Offer the possibility to set the level of support that the game provides to the player

## Authors
[Andrin Rehmann](https://github.com/andrinr) <br/>
[Dominic Schmidli](https://github.com/dschmidli) <br/>
[Michael Hodel](https://github.com/michaelhodel) <br/>
[Philippe Schmidli](https://github.com/pschmidli)

## Acknowledgement
Thanks to group 2 for creating such a great and nice game. Special thanks to our tutor [Moritz Eck](https://github.com/meck93) who provided support
and valuable feedback in all phases of our project. We all learned a lot and are looking forward to challenging each other in Jess.

## License
This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details.

> © by SoPra-FS20-Group02
