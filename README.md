# Jess

## Introduction

We aimed to create a state of the art online mutliplayer chess game where users can easily create
and join games with different rules. Besides there is a extensive user statistics page and a chat 
functionality. The main focus was implementing our own chess engine where all possible moves can 
be fetched from the backend and displayed to the user. This enables users with less knowledge about
the game to perform better. 

## Technologies

Spring, WebSocket, Lombock

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

//TODO

## Launch & Deployment

// TODO

