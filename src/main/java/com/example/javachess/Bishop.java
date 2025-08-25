package com.example.javachess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public static final String WHITE_BISHOP_IMAGE_PATH = "/com/example/javachess/images/white-bishop.png";
    public static final String BLACK_BISHOP_IMAGE_PATH = "/com/example/javachess/images/black-bishop.png";

    public Bishop(Position position, Team team) {
        super(position, team, Type.BISHOP, team == Team.WHITE ? WHITE_BISHOP_IMAGE_PATH : BLACK_BISHOP_IMAGE_PATH);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> possibleMoves = new ArrayList<>();
        Position currentPosition = this.getPosition();

        int[][] directions = { {1, 1}, {-1, 1}, {1, -1}, {-1, -1} };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            for (int i = 1; i < 8; i++) {
                Position nextPos = new Position(currentPosition.getX() + i * dx, currentPosition.getY() + i * dy);

                if (nextPos.getX() < 0 || nextPos.getX() >= 8 || nextPos.getY() < 0 || nextPos.getY() >= 8) {
                    break;
                }

                if (MainController.hasPieceAt(nextPos)) {
                    if (MainController.hasEnemyPieceAt(nextPos, this.getTeam())) {
                        possibleMoves.add(nextPos);
                    }

                    break;
                }
                else {
                    possibleMoves.add(nextPos);
                }
            }
        }

        return possibleMoves;
    }
}
