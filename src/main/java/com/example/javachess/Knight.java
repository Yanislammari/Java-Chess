package com.example.javachess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    private static final String WHITE_KNIGHT_IMAGE_PATH = "/com/example/javachess/images/white-knight.png";
    private static final String BLACK_KNIGHT_IMAGE_PATH = "/com/example/javachess/images/black-knight.png";

    public Knight(Position position, Team team) {
        super(position, team, Type.KNIGHT, team == Team.WHITE ? WHITE_KNIGHT_IMAGE_PATH : BLACK_KNIGHT_IMAGE_PATH);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> possibleMoves = new ArrayList<>();
        int[][] knightMoves = { {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2} };
        Position currentPosition = this.getPosition();

        for (int[] move : knightMoves) {
            int newX = currentPosition.getX() + move[0];
            int newY = currentPosition.getY() + move[1];

            if (newX < 0 || newX >= 8 || newY < 0 || newY >= 8) {
                continue;
            }

            Position newPos = new Position(newX, newY);
            if (MainController.hasPieceAt(newPos)) {
                if (MainController.hasEnemyPieceAt(newPos, this.getTeam())) {
                    possibleMoves.add(newPos);
                }
            }
            else {
                possibleMoves.add(newPos);
            }
        }

        return possibleMoves;
    }
}
