package com.example.javachess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    private static final String WHITE_QUEEN_IMAGE_PATH = "/com/example/javachess/images/white-queen.png";
    private static final String BLACK_QUEEN_IMAGE_PATH = "/com/example/javachess/images/black-queen.png";

    public Queen(Position position, Team team) {
        super(position, team, Type.QUEEN, team == Team.WHITE ? WHITE_QUEEN_IMAGE_PATH : BLACK_QUEEN_IMAGE_PATH);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> possibleMoves = new ArrayList<>();
        Position currentPosition = this.getPosition();
        int[][] directions = { {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1} };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int x = currentPosition.getX();
            int y = currentPosition.getY();

            while (true) {
                x += dx;
                y += dy;

                if (x < 0 || x >= 8 || y < 0 || y >= 8) {
                    break;
                }

                Position newPos = new Position(x, y);
                if (MainController.hasPieceAt(newPos)) {
                    if (MainController.hasEnemyPieceAt(newPos, this.getTeam())) {
                        possibleMoves.add(newPos);
                    }

                    break;
                }
                else {
                    possibleMoves.add(newPos);
                }
            }
        }

        return possibleMoves;
    }
}
