package com.example.javachess;

public class Board {
    private final Piece[][] grid = new Piece[8][8];

    public static boolean isInsideBoard(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && x < 8 && y >= 0 && y < 8; 
    }

    public Piece getAt(int row, int col) {
        return grid[row][col];
    }

    public void setAt(int row, int col, Piece piece) {
        grid[row][col] = piece;
    }

    public Piece getPieceAt(Position position) {
        return grid[position.getY()][position.getX()];
    }

    public void removeAt(int row, int col) {
        grid[row][col] = null;
    }

    public boolean hasPieceAt(Position position) {
        if (!isInsideBoard(position)) {
            return false;
        }

        return grid[position.getY()][position.getX()] != null;
    }

    public boolean hasEnemyPieceAt(Position position, Team team) {
        if (!isInsideBoard(position)) {
            return false;
        }

        Piece piece = grid[position.getY()][position.getX()];
        return piece != null && piece.getTeam() != team;
    }

    public boolean hasFriendlyPieceAt(Position position, Team team) {
        if (!isInsideBoard(position)) {
            return false;
        }

        Piece piece = grid[position.getY()][position.getX()];
        return piece != null && piece.getTeam() == team;
    }
}
