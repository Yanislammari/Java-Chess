package com.example.javachess;

import java.util.ArrayList;
import java.util.List;

public final class MoveGenerator {
    public static List<Position> getLegalMoves(Board board, Piece piece) {
        List<Position> legal = new ArrayList<>();
        Position original = piece.getPosition();
        int ox = original.getX();
        int oy = original.getY();

        for (Position move : piece.getPossibleMoves()) {
            if (move == null || !Board.isInsideBoard(move)) {
                continue;
            }

            Piece targetPiece = board.getAt(move.getY(), move.getX());
            if (targetPiece != null && targetPiece.getType() == Type.KING) {
                continue;
            }

            boolean isCastling = piece.getType() == Type.KING && Math.abs(move.getX() - ox) == 2 && move.getY() == oy;
            if (isCastling) {
                Team team = piece.getTeam();
                Team opponent = (team == Team.WHITE) ? Team.BLACK : Team.WHITE;
                Position through = new Position((ox + move.getX()) / 2, oy);

                if (Rules.isInCheck(board, team) || Rules.isSquareAttacked(board, through, opponent)) {
                    continue;
                }
            }

            Piece captured = board.getAt(move.getY(), move.getX());
            board.setAt(oy, ox, null);
            board.setAt(move.getY(), move.getX(), piece);
            piece.setPosition(move);

            boolean leavesOwnKingInCheck = Rules.isInCheck(board, piece.getTeam());

            board.setAt(move.getY(), move.getX(), captured);
            board.setAt(oy, ox, piece);
            piece.setPosition(original);

            if (!leavesOwnKingInCheck) {
                legal.add(move);
            }
        }

        return legal;
    }
}
