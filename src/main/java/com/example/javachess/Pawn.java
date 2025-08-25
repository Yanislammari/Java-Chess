package com.example.javachess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pawn extends Piece {
    private static final String WHITE_PAWN_IMAGE_PATH = "/com/example/javachess/images/white-pawn.png";
    private static final String BLACK_PAWN_IMAGE_PATH = "/com/example/javachess/images/black-pawn.png";
    private boolean hasMoved;

    public Pawn(Position position, Team team) {
        super(position, team, Type.PAWN, team == Team.WHITE ? WHITE_PAWN_IMAGE_PATH : BLACK_PAWN_IMAGE_PATH);
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
        int direction = this.getTeam() == Team.WHITE ? -1 : 1;
        Position currentPosition = this.getPosition();

        Position oneStep = new Position(currentPosition.getX(), currentPosition.getY() + direction);
        if (!MainController.hasPieceAt(oneStep)) {
            possibleMoves.add(oneStep);

            Position twoSteps = new Position(currentPosition.getX(), currentPosition.getY() + 2 * direction);
            if (!this.hasMoved && !MainController.hasPieceAt(twoSteps)) {
                possibleMoves.add(twoSteps);
            }
        }

        Position diagLeft = new Position(currentPosition.getX() - 1, currentPosition.getY() + direction);
        if (diagLeft.getX() >= 0 && diagLeft.getX() < 8 && diagLeft.getY() >= 0 && diagLeft.getY() < 8) {
            if (MainController.hasEnemyPieceAt(diagLeft, this.getTeam())) {
                possibleMoves.add(diagLeft);
            } else {
                Position ep = MainController.getEpTarget();
                if (ep != null && ep.getX() == diagLeft.getX() && ep.getY() == diagLeft.getY()) {
                    possibleMoves.add(diagLeft);
                }
            }
        }

        Position diagRight = new Position(currentPosition.getX() + 1, currentPosition.getY() + direction);
        if (diagRight.getX() >= 0 && diagRight.getX() < 8 && diagRight.getY() >= 0 && diagRight.getY() < 8) {
            if (MainController.hasEnemyPieceAt(diagRight, this.getTeam())) {
                possibleMoves.add(diagRight);
            } else {
                Position ep = MainController.getEpTarget();
                if (ep != null && ep.getX() == diagRight.getX() && ep.getY() == diagRight.getY()) {
                    possibleMoves.add(diagRight);
                }
            }
        }

        return possibleMoves;
    }

    public List<Position> getControlledPositions() {
        List<Position> controlled = new ArrayList<>();
        int dir = this.getTeam() == Team.WHITE ? -1 : 1;
        Position p = this.getPosition();

        int nx = p.getX() - 1;
        int ny = p.getY() + dir;
        if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
            controlled.add(new Position(nx, ny));
        }

        nx = p.getX() + 1;
        if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
            controlled.add(new Position(nx, ny));
        }

        return controlled;
    }

    @Override
    public String toString() {
        return super.toString() + ", Pawn{" +
                "hasMoved=" + this.hasMoved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pawn pawn)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return this.hasMoved == pawn.hasMoved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.hasMoved);
    }
}
