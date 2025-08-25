package com.example.javachess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rook extends Piece {
    private static final String WHITE_ROOK_IMAGE_PATH = "/com/example/javachess/images/white-rook.png";
    private static final String BLACK_ROOK_IMAGE_PATH = "/com/example/javachess/images/black-rook.png";
    private boolean hasMoved;

    public Rook(Position position, Team team) {
        super(position, team, Type.ROOK, team == Team.WHITE ? WHITE_ROOK_IMAGE_PATH : BLACK_ROOK_IMAGE_PATH);
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public void move(Position position) {
        this.setPosition(position);
        setHasMoved(true);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> possibleMoves = new ArrayList<>();
        Position currentPosition = this.getPosition();
        int[][] directions = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

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

    @Override
    public String toString() {
        return "Rook{" +
                "hasMoved=" + this.hasMoved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rook rook)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return this.hasMoved == rook.hasMoved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.hasMoved);
    }
}
