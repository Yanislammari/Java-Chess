package com.example.javachess;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController implements Initializable {
    @FXML
    private GridPane chessBoard;
    @FXML
    private Label checkmateLabel;
    @FXML
    private StackPane overlayPane;
    private static final int TILE_SIZE = 80;
    private static final Board boardModel = new Board();
    private static Position epTarget = null;
    private final StackPane[][] tiles = new StackPane[8][8];
    private final List<Position> highlightedTargets = new ArrayList<>();
    private Piece selectedPiece = null;
    private Position selectedPosition = null;
    private Team currentTurn = Team.WHITE;
    private boolean checkmate = false;
    private boolean stalemate = false;

    public static Piece getPieceAt(Position position) {
        if (Board.isInsideBoard(position)) {
            return null;
        }
        return boardModel.getPieceAt(position);
    }

    public static Position getEpTarget() {
        return epTarget;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.drawBoard();
        this.placeInitialPieces();
        this.overlayPane.setVisible(false);
        this.overlayPane.setManaged(false);
    }

    private void drawBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane tile = this.createTile(row, col);
                this.tiles[row][col] = tile;
                this.chessBoard.add(tile, col, row);
            }
        }
    }

    private StackPane createTile(int row, int col) {
        StackPane tile = new StackPane();
        tile.setPrefSize(TILE_SIZE, TILE_SIZE);

        String colorClass = (row + col) % 2 == 0 ? "light-tile" : "dark-tile";
        tile.getStyleClass().add(colorClass);

        tile.setOnMouseEntered(event -> {
            if (!tile.getStyleClass().contains("tile-hover")) {
                tile.getStyleClass().add("tile-hover");
            }
        });

        tile.setOnMouseExited(event -> {
            tile.getStyleClass().remove("tile-hover");
        });

        tile.setOnMouseClicked(event -> this.handleTileClick(row, col));
        tile.setAlignment(Pos.CENTER);

        return tile;
    }

    private void placeInitialPieces() {
        List<Piece> pieces = this.createStartingPosition();
        for (Piece piece : pieces) {
            this.addPieceToBoard(piece);
        }
    }

    private void addPieceToBoard(Piece piece) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource(piece.getImagePath())).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setFitWidth(TILE_SIZE * 0.8);
        imageView.setFitHeight(TILE_SIZE * 0.8);
        imageView.setMouseTransparent(true);

        int col = piece.getPosition().getX();
        int row = piece.getPosition().getY();

        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            StackPane tile = this.tiles[row][col];
            tile.getChildren().add(imageView);
            boardModel.setAt(row, col, piece);
        }
    }

    private List<Piece> createStartingPosition() {
        List<Piece> pieces = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            pieces.add(new Pawn(new Position(x, 1), Team.BLACK));
        }

        pieces.add(new Rook(new Position(0, 0), Team.BLACK));
        pieces.add(new Knight(new Position(1, 0), Team.BLACK));
        pieces.add(new Bishop(new Position(2, 0), Team.BLACK));
        pieces.add(new Queen(new Position(3, 0), Team.BLACK));
        pieces.add(new King(new Position(4, 0), Team.BLACK));
        pieces.add(new Bishop(new Position(5, 0), Team.BLACK));
        pieces.add(new Knight(new Position(6, 0), Team.BLACK));
        pieces.add(new Rook(new Position(7, 0), Team.BLACK));

        for (int x = 0; x < 8; x++) {
            pieces.add(new Pawn(new Position(x, 6), Team.WHITE));
        }

        pieces.add(new Rook(new Position(0, 7), Team.WHITE));
        pieces.add(new Knight(new Position(1, 7), Team.WHITE));
        pieces.add(new Bishop(new Position(2, 7), Team.WHITE));
        pieces.add(new Queen(new Position(3, 7), Team.WHITE));
        pieces.add(new King(new Position(4, 7), Team.WHITE));
        pieces.add(new Bishop(new Position(5, 7), Team.WHITE));
        pieces.add(new Knight(new Position(6, 7), Team.WHITE));
        pieces.add(new Rook(new Position(7, 7), Team.WHITE));

        return pieces;
    }

    private void handleTileClick(int row, int col) {
        if (this.checkmate || this.stalemate) {
            return;
        }
        if (this.selectedPiece != null) {
            boolean clickedIsTarget = this.highlightedTargets.stream().anyMatch(p -> p.getX() == col && p.getY() == row);
            if (clickedIsTarget) {
                this.moveSelectedPieceTo(row, col);
                return;
            }

            if (this.selectedPosition != null) {
                StackPane prevSelectedTile = this.tiles[this.selectedPosition.getY()][this.selectedPosition.getX()];
                prevSelectedTile.getStyleClass().remove("selected-tile");
            }

            this.clearHighlightedTargets();
            this.selectedPiece = null;
            this.selectedPosition = null;
        }

        Piece piece = boardModel.getAt(row, col);
        if (piece == null || piece.getTeam() != this.currentTurn) {
            return;
        }

        this.selectedPiece = piece;
        this.selectedPosition = new Position(col, row);

        StackPane currentSelectedTile = this.tiles[row][col];
        if (!currentSelectedTile.getStyleClass().contains("selected-tile")) {
            currentSelectedTile.getStyleClass().add("selected-tile");
        }

        this.highlightMoves(piece);
    }

    public static boolean hasPieceAt(Position position) {
        return new MainController().boardModel.hasPieceAt(position);
    }

    public static boolean hasEnemyPieceAt(Position position, Team team) {
        return new MainController().boardModel.hasEnemyPieceAt(position, team);
    }

    private void clearHighlightedTargets() {
        for (Position pos : this.highlightedTargets) {
            if (Board.isInsideBoard(pos)) {
                StackPane tile = this.tiles[pos.getY()][pos.getX()];
                tile.getStyleClass().remove("move-target");
                tile.getStyleClass().remove("capture-target");
            }
        }

        this.highlightedTargets.clear();
    }

    private void highlightMoves(Piece piece) {
        this.clearHighlightedTargets();
        List<Position> possibleMoves = MoveGenerator.getLegalMoves(boardModel, piece);
        for (Position pos : possibleMoves) {
            if (!Board.isInsideBoard(pos)) {
                continue;
            }
            if (boardModel.hasFriendlyPieceAt(pos, piece.getTeam())) {
                continue;
            }

            StackPane targetTile = this.tiles[pos.getY()][pos.getX()];
            if (boardModel.hasEnemyPieceAt(pos, piece.getTeam())) {
                if (!targetTile.getStyleClass().contains("capture-target")) {
                    targetTile.getStyleClass().add("capture-target");
                }
            }
            else {
                if (!targetTile.getStyleClass().contains("move-target")) {
                    targetTile.getStyleClass().add("move-target");
                }
            }

            this.highlightedTargets.add(pos);
        }
    }

    private void moveSelectedPieceTo(int destRow, int destCol) {
        if (this.selectedPiece == null || this.selectedPosition == null) {
            return;
        }

        List<Position> legalMoves = MoveGenerator.getLegalMoves(boardModel, this.selectedPiece);
        boolean isLegalDestination = false;
        
        for (Position p : legalMoves) {
            if (p.getX() == destCol && p.getY() == destRow) {
                isLegalDestination = true;
                break;
            }
        }

        if (!isLegalDestination) {
            return;
        }

        int srcRow = this.selectedPosition.getY();
        int srcCol = this.selectedPosition.getX();

        StackPane srcTile = this.tiles[srcRow][srcCol];
        srcTile.getStyleClass().remove("selected-tile");

        ImageView srcImage = this.getPieceImageViewAt(srcRow, srcCol);
        if (srcImage == null) {
            return;
        }

        Piece captured = boardModel.getAt(destRow, destCol);
        boolean isEpCapture = (this.selectedPiece.getType() == Type.PAWN) && captured == null && destCol != srcCol;
        if (isEpCapture) {
            int victimRow = destRow + (this.selectedPiece.getTeam() == Team.WHITE ? 1 : -1);
            Piece victim = boardModel.getAt(victimRow, destCol);
            if (victim.getType() == Type.PAWN) {
                ImageView victimImage = this.getPieceImageViewAt(victimRow, destCol);
                if (victimImage != null) {
                    this.tiles[victimRow][destCol].getChildren().remove(victimImage);
                }
                boardModel.setAt(victimRow, destCol, null);
            }
        }

        ImageView destImage = this.getPieceImageViewAt(destRow, destCol);
        if (destImage != null) {
            this.tiles[destRow][destCol].getChildren().remove(destImage);
        }

        this.tiles[srcRow][srcCol].getChildren().remove(srcImage);
        this.tiles[destRow][destCol].getChildren().add(srcImage);

        boardModel.setAt(destRow, destCol, this.selectedPiece);
        boardModel.setAt(srcRow, srcCol, null);

        if (this.selectedPiece.getType() == Type.KING && Math.abs(destCol - srcCol) == 2 && destRow == srcRow) {
            if (destCol == 6) {
                int rookSrcCol = 7;
                int rookDestCol = 5;
                Piece rook = boardModel.getAt(srcRow, rookSrcCol);

                if (rook.getType() == Type.ROOK) {
                    ImageView rookImg = this.getPieceImageViewAt(srcRow, rookSrcCol);
                    if (rookImg != null) {
                        this.tiles[srcRow][rookSrcCol].getChildren().remove(rookImg);
                        this.tiles[srcRow][rookDestCol].getChildren().add(rookImg);
                    }

                    boardModel.setAt(srcRow, rookDestCol, rook);
                    boardModel.setAt(srcRow, rookSrcCol, null);
                    rook.move(new Position(rookDestCol, srcRow));
                }
            }
            else if (destCol == 2) {
                int rookSrcCol = 0;
                int rookDestCol = 3;
                Piece rook = boardModel.getAt(srcRow, rookSrcCol);

                if (rook.getType() == Type.ROOK) {
                    ImageView rookImg = this.getPieceImageViewAt(srcRow, rookSrcCol);
                    if (rookImg != null) {
                        this.tiles[srcRow][rookSrcCol].getChildren().remove(rookImg);
                        this.tiles[srcRow][rookDestCol].getChildren().add(rookImg);
                    }

                    boardModel.setAt(srcRow, rookDestCol, rook);
                    boardModel.setAt(srcRow, rookSrcCol, null);
                    rook.move(new Position(rookDestCol, srcRow));
                }
            }
        }

        this.selectedPiece.move(new Position(destCol, destRow));

        if (this.selectedPiece.getType() == Type.PAWN) {
            if ((this.selectedPiece.getTeam() == Team.WHITE && destRow == 0) || (this.selectedPiece.getTeam() == Team.BLACK && destRow == 7)) {
                Team team = this.selectedPiece.getTeam();
                Queen promoted = new Queen(new Position(destCol, destRow), team);
                boardModel.setAt(destRow, destCol, promoted);
                Image queenImage = new Image(Objects.requireNonNull(getClass().getResource(promoted.getImagePath())).toExternalForm());
                srcImage.setImage(queenImage);
                this.selectedPiece = promoted;
            }
        }

        this.clearHighlightedTargets();

        Team mover = this.selectedPiece.getTeam();
        Team defender = mover == Team.WHITE ? Team.BLACK : Team.WHITE;

        Position newEp = null;
        if (this.selectedPiece.getType() == Type.PAWN) {
            if (Math.abs(destRow - srcRow) == 2) {
                newEp = new Position(destCol, (destRow + srcRow) / 2);
            }
        }

        epTarget = newEp;
        this.selectedPiece = null;
        this.selectedPosition = null;

        if (Rules.isCheckmate(boardModel, defender)) {
            this.showCheckmate(mover);
            return;
        }
        if (Rules.isStalemate(boardModel, defender)) {
            this.showDraw();
            return;
        }

        this.currentTurn = (this.currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    private void showCheckmate(Team winner) {
        this.checkmate = true;
        this.overlayPane.setVisible(true);
        this.overlayPane.setManaged(true);
        this.checkmateLabel.setText((winner == Team.WHITE ? "Whites" : "Blacks") + " wins - Checkmate");
    }

    private void showDraw() {
        this.stalemate = true;
        this.overlayPane.setVisible(true);
        this.overlayPane.setManaged(true);
        this.checkmateLabel.setText("Draw - Stalemate");
    }

    private ImageView getPieceImageViewAt(int row, int col) {
        StackPane tile = this.tiles[row][col];
        for (Node node : tile.getChildren()) {
            if (node instanceof ImageView) {
                return (ImageView) node;
            }
        }

        return null;
    }

    @FXML
    private void onRestartClicked() {
        this.restartGame();
    }

    private void restartGame() {
        this.highlightedTargets.clear();
        this.selectedPiece = null;
        this.selectedPosition = null;
        this.currentTurn = Team.WHITE;
        this.checkmate = false;
        this.stalemate = false;
        this.overlayPane.setVisible(false);
        this.overlayPane.setManaged(false);
        this.checkmateLabel.setText("");

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardModel.setAt(row, col, null);
                StackPane tile = this.tiles[row][col];

                if (tile != null) {
                    tile.getStyleClass().remove("move-target");
                    tile.getStyleClass().remove("capture-target");
                    tile.getStyleClass().remove("selected-tile");

                    List<Node> toRemove = new ArrayList<>();
                    for (Node node : tile.getChildren()) {
                        if (node instanceof ImageView) {
                            toRemove.add(node);
                        }
                    }

                    tile.getChildren().removeAll(toRemove);
                }
            }
        }

        this.placeInitialPieces();
    }
}
