package com.example.javachess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class King extends Piece {
    private static final String WHITE_KING_IMAGE_PATH = "/com/example/javachess/images/white-king.png";
    private static final String BLACK_KING_IMAGE_PATH = "/com/example/javachess/images/black-king.png";
    private boolean hasMoved;

    public King(Position position, Team team) {
        super(position, team, Type.KING, team == Team.WHITE ? WHITE_KING_IMAGE_PATH : BLACK_KING_IMAGE_PATH);
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

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = currentPosition.getX() + dx;
                int newY = currentPosition.getY() + dy;

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
        }

        if (!this.hasMoved) {
            int y = currentPosition.getY();

            if (!MainController.hasPieceAt(new Position(5, y)) && !MainController.hasPieceAt(new Position(6, y))) {
                Piece rook = MainController.getPieceAt(new Position(7, y));
                assert rook != null;
                if (rook.getType() == Type.ROOK && !((Rook) rook).hasMoved() && rook.getTeam() == this.getTeam()) {
                    possibleMoves.add(new Position(6, y));
                }
            }

            if (!MainController.hasPieceAt(new Position(1, y)) && !MainController.hasPieceAt(new Position(2, y)) && !MainController.hasPieceAt(new Position(3, y))) {
                Piece rook = MainController.getPieceAt(new Position(0, y));
                assert rook != null;
                if (rook.getType() == Type.ROOK && !((Rook) rook).hasMoved() && rook.getTeam() == this.getTeam()) {
                    possibleMoves.add(new Position(2, y));
                }
            }
        }

        return possibleMoves;
    }

    public List<Position> getControlledPositions() {
        List<Position> controlled = new ArrayList<>();
        Position p = this.getPosition();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int nx = p.getX() + dx;
                int ny = p.getY() + dy;
                if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                    controlled.add(new Position(nx, ny));
                }
            }
        }

        return controlled;
    }

    @Override
    public String toString() {
        return "King{" +
                "hasMoved=" + this.hasMoved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof King king)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return this.hasMoved == king.hasMoved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.hasMoved);
    }
}
