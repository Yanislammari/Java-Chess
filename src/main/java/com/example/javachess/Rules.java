package com.example.javachess;

import java.util.List;

public final class Rules {
    public static boolean isInCheck(Board board, Team team) {
        Position kingPos = findKingPosition(board, team);
        if (kingPos == null) {
            return false;
        }

        Team opponent = team == Team.WHITE ? Team.BLACK : Team.WHITE;
        return isSquareAttacked(board, kingPos, opponent);
    }

    public static Position findKingPosition(Board board, Team team) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = board.getAt(y, x);
                if (p != null && p.getTeam() == team && p.getType() == Type.KING) {
                    return new Position(x, y);
                }
            }
        }

        return null;
    }

    public static boolean isSquareAttacked(Board board, Position target, Team byTeam) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece attacker = board.getAt(y, x);
                if (attacker == null || attacker.getTeam() != byTeam) {
                    continue;
                }

                List<Position> attacks;
                if (attacker.getType() == Type.PAWN) {
                    attacks = ((Pawn) attacker).getControlledPositions();
                }
                else if (attacker.getType() == Type.KING) {
                    attacks = ((King) attacker).getControlledPositions();
                }
                else {
                    attacks = attacker.getPossibleMoves();
                }

                for (Position p : attacks) {
                    if (p != null && p.getX() == target.getX() && p.getY() == target.getY()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isCheckmate(Board board, Team defender) {
        if (!isInCheck(board, defender)) {
            return false;
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getAt(y, x);
                if (piece == null || piece.getTeam() != defender) {
                    continue;
                }

                List<Position> legalMoves = MoveGenerator.getLegalMoves(board, piece);
                if (!legalMoves.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isStalemate(Board board, Team defender) {
        if (isInCheck(board, defender)) {
            return false;
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getAt(y, x);
                if (piece == null || piece.getTeam() != defender) {
                    continue;
                }

                List<Position> legalMoves = MoveGenerator.getLegalMoves(board, piece);
                if (!legalMoves.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }
}
