package com.example.javachess;

import java.util.List;
import java.util.Objects;

public abstract class Piece {
    private Position position;
    private Team team;
    private Type type;
    private String imagePath;

    public Piece(Position position, Team team, Type type, String imagePath) {
        this.position = position;
        this.team = team;
        this.type = type;
        this.imagePath = imagePath;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void move(Position position) {
        this.setPosition(position);
    }

    public abstract List<Position> getPossibleMoves();

    @Override
    public String toString() {
        return "Piece{" +
                "position=" + this.position +
                ", team=" + this.team +
                ", type=" + this.type +
                ", imagePath='" + this.imagePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piece piece)) {
            return false;
        }

        return Objects.equals(this.position, piece.position) && this.team == piece.team && this.type == piece.type && Objects.equals(this.imagePath, piece.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.position, this.team, this.type, this.imagePath);
    }
}
