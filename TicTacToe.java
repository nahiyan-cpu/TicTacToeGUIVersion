import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TicTacToe extends JFrame {


    private static final Color BG_DARK       = new Color(15, 15, 25);
    private static final Color BG_CARD       = new Color(25, 25, 40);
    private static final Color COLOR_X       = new Color(99, 221, 183);
    private static final Color COLOR_O       = new Color(255, 107, 107);
    private static final Color COLOR_ACCENT  = new Color(123, 97, 255);
    private static final Color COLOR_TEXT    = new Color(230, 230, 245);
    private static final Color COLOR_MUTED   = new Color(100, 100, 130);

    
    private Player player1, player2, currentPlayer;
    private Board  gameBoard;
    private boolean gameActive = true;
    private int boardSize = 3;


    private GameButton[] boardButtons;
    private JLabel statusLabel   = new JLabel("", SwingConstants.CENTER);
    private JLabel player1Label  = new JLabel("", SwingConstants.CENTER);
    private JLabel player2Label  = new JLabel("", SwingConstants.CENTER);
    private JPanel boardPanel;

    public TicTacToe() {
        showMainMenu();
    }

    private void showMainMenu() {
        setVisible(false);
        dispose();

        JFrame menu = new JFrame("Tic-Tac-Toe  ✦  Main Menu");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setSize(420, 460);
        menu.setLocationRelativeTo(null);
        menu.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(COLOR_ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel sub = new JLabel("by Syed Nahiyan", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.ITALIC, 13));
        sub.setForeground(COLOR_MUTED);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 28, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BG_DARK);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(sub,   BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 0, 14));
        btnPanel.setBackground(BG_DARK);

        JButton playBtn    = makeMenuButton("▶️   Play Game",     COLOR_ACCENT);
        JButton historyBtn = makeMenuButton("📜  View History",  new Color(35, 35, 55));
        JButton exitBtn    = makeMenuButton("✕   Exit",          new Color(120, 40, 40));

        playBtn.addActionListener(e -> {
            menu.dispose();
            if (!showLoginDialog(null)) { showMainMenu(); return; }
            if (!setupAndLaunch())      { showMainMenu(); }
        });
        historyBtn.addActionListener(e -> showHistoryDialog(menu));
        exitBtn.addActionListener(e -> System.exit(0));

        btnPanel.add(playBtn);
        btnPanel.add(historyBtn);
        btnPanel.add(exitBtn);

        root.add(titlePanel, BorderLayout.NORTH);
        root.add(btnPanel,   BorderLayout.CENTER);

        menu.setContentPane(root);
        menu.setVisible(true);
    }

    
    private boolean showLoginDialog(Component parent) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_CARD);
        tabs.setForeground(COLOR_TEXT);


        JPanel passPanel = new JPanel(new GridLayout(2, 2, 8, 10));
        passPanel.setBackground(BG_CARD);
        passPanel.setBorder(BorderFactory.createEmptyBorder(16, 10, 10, 10));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        styleField(userField);
        styleField(passField);
        passPanel.add(mkLabel("Username:")); passPanel.add(userField);
        passPanel.add(mkLabel("Password:")); passPanel.add(passField);
        tabs.addTab("🔐 Password", passPanel);

    
        JPanel secPanel = new JPanel(new GridLayout(2, 2, 8, 10));
        secPanel.setBackground(BG_CARD);
        secPanel.setBorder(BorderFactory.createEmptyBorder(16, 10, 10, 10));
        JTextField secAnswer = new JTextField();
        styleField(secAnswer);
        JLabel hint = mkLabel("(Hint: 'fluffy')");
        hint.setForeground(COLOR_MUTED);
        secPanel.add(mkLabel("Pet's name?"));  secPanel.add(hint);
        secPanel.add(mkLabel("Answer:"));      secPanel.add(secAnswer);
        tabs.addTab("🔑 Secret Q", secPanel);

        int res = JOptionPane.showConfirmDialog(parent, tabs, "🔐  Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return false;

        if (tabs.getSelectedIndex() == 0) {
            if (userField.getText().equals("admin") &&
                new String(passField.getPassword()).equals("1234")) return true;
            JOptionPane.showMessageDialog(parent, "❌  Wrong credentials!", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (secAnswer.getText().trim().equalsIgnoreCase("fluffy")) return true;
            JOptionPane.showMessageDialog(parent, "❌  Wrong answer!", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    private boolean setupAndLaunch() {
        
        String[] sizes = {"3×3  (Classic)", "5×5  (Medium)", "7×7  (Large)"};
        int sc = JOptionPane.showOptionDialog(null, "Choose Board Size", "🎲  Board Size",
          JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, sizes, sizes[0]);
        if (sc < 0) return false;
        boardSize = (sc == 0) ? 3 : (sc == 1) ? 5 : 7;

        Map<String, Integer> saved = FileHandler.loadScores();

        
        String p1 = JOptionPane.showInputDialog(null, "Enter Your Name (Player 1):",
                "👤  Player 1", JOptionPane.PLAIN_MESSAGE);
        if (p1 == null || p1.trim().isEmpty()) return false;
        player1 = new HumanPlayer(p1.trim(), 'X');
        player1.setScore(saved.getOrDefault(player1.getName(), 0));

        
        String[] modes = {"🤖  vs Computer", "👥  vs Player"};
        int mode = JOptionPane.showOptionDialog(null, "Choose Mode", "⚙️  Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modes, modes[0]);
        if (mode < 0) return false;

        if (mode == 0) {
            String[] levels = {"😊  Easy", "🤔  Medium", "😈  Hard"};
        int lvl = JOptionPane.showOptionDialog(null, "Choose Difficulty", "🎯  Difficulty",
         JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);
            if (lvl < 0) return false;
            player2 = new ComputerPlayer('O', lvl + 1, boardSize);
        } else {
            String p2 = JOptionPane.showInputDialog(null, "Enter Player 2's Name:",
                    "👤  Player 2", JOptionPane.PLAIN_MESSAGE);
            if (p2 == null || p2.trim().isEmpty()) return false;
            player2 = new HumanPlayer(p2.trim(), 'O');
        }
        player2.setScore(saved.getOrDefault(player2.getName(), 0));

        launchGameWindow();
        return true;
    }

    
    private void launchGameWindow() {
        setTitle("Tic-Tac-Toe  ✦  " + boardSize + "×" + boardSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        
        JMenuBar bar = new JMenuBar();
        bar.setBackground(BG_CARD);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_ACCENT));
        JMenu gm = new JMenu("☰  Game");
        gm.setForeground(COLOR_TEXT);
        gm.setFont(new Font("SansSerif", Font.BOLD, 14));
        gm.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        gm.add(mkMenuItem("⟳  New Round",  e -> startNewGame()));
        gm.add(mkMenuItem("🏠  Main Menu", e -> showMainMenu()));
        gm.add(mkMenuItem("📜  History",   e -> showHistoryDialog(this)));
        gm.add(new JSeparator());
        gm.add(mkMenuItem("✕  Exit",       e -> System.exit(0)));
        bar.add(gm);
        setJMenuBar(bar);

        
        int cells   = boardSize * boardSize;
        int btnSize = (boardSize == 3) ? 120 : (boardSize == 5) ? 80 : 60;
        int gap     = (boardSize == 3) ? 8   : (boardSize == 5) ? 5  : 4;

        boardButtons = new GameButton[cells];
        boardPanel   = new JPanel(new GridLayout(boardSize, boardSize, gap, gap));
        boardPanel.setBackground(BG_DARK);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        for (int i = 0; i < cells; i++) {
            boardButtons[i] = new GameButton(btnSize);
            final int idx = i;
            boardButtons[i].addActionListener(e -> onCellClick(idx));
            boardPanel.add(boardButtons[i]);
        }

        
        JPanel scorePanel = new JPanel(new GridLayout(1, 3, 10, 0));
        scorePanel.setBackground(BG_DARK);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        scorePanel.add(buildPlayerCard(player1Label, COLOR_X));
        scorePanel.add(buildVS());
        scorePanel.add(buildPlayerCard(player2Label, COLOR_O));

        
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(COLOR_TEXT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        
        JLabel titleLbl = new JLabel("TIC-TAC-TOE  " + boardSize + "×" + boardSize,
                SwingConstants.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLbl.setForeground(COLOR_ACCENT);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(titleLbl,   BorderLayout.NORTH);
        top.add(scorePanel, BorderLayout.CENTER);
        top.add(statusLabel, BorderLayout.SOUTH);

        
        JButton newRoundBtn = accentBtn("⟳  New Round");
        newRoundBtn.addActionListener(e -> startNewGame());

        JButton menuBtn = accentBtn("🏠  Main Menu");
        menuBtn.setBackground(new Color(60, 40, 90));
        menuBtn.addMouseListener(new MouseAdapter() {
public void mouseEntered(MouseEvent e) { menuBtn.setBackground(new Color(80, 55, 120)); }
 public void mouseExited(MouseEvent e)  { menuBtn.setBackground(new Color(60, 40, 90)); }
        });
        menuBtn.addActionListener(e -> showMainMenu());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        bottom.setBackground(BG_DARK);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bottom.add(newRoundBtn);
        bottom.add(menuBtn);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        main.add(top,        BorderLayout.NORTH);
        main.add(boardPanel, BorderLayout.CENTER);
        main.add(bottom,     BorderLayout.SOUTH);

        setContentPane(main);

        gameBoard     = new Board(boardSize);
        gameActive    = true;
        currentPlayer = player1;
        updateScoreLabels();
        updateStatus();

        int w = (boardSize == 3) ? 500 : (boardSize == 5) ? 570 : 630;
        int h = (boardSize == 3) ? 640 : (boardSize == 5) ? 700 : 760;
        setSize(w, h);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    private void onCellClick(int idx) {
        if (!gameActive || !gameBoard.isMoveValid(idx)) return;
        placeMove(idx, currentPlayer.getSymbol());
        if (checkState()) return;
        switchPlayer();
        if (currentPlayer instanceof ComputerPlayer && gameActive) {
            disableBoard();
            new SwingWorker<Integer, Void>() {
                protected Integer doInBackground() throws Exception {
                    Thread.sleep(500);
                    return currentPlayer.makeMove(gameBoard);
                }
                protected void done() {
                    try {
                        int mv = get();
if (mv >= 0) { placeMove(mv, currentPlayer.getSymbol()); if (checkState()) return; switchPlayer(); }
                        enableBoard();
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            }.execute();
        }
    }

    private void placeMove(int idx, char sym) {
        gameBoard.placeMove(idx, sym);
        boardButtons[idx].setSymbol(sym);
        boardButtons[idx].setEnabled(false);
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        updateStatus();
    }

    private boolean checkState() {
        int[] combo = gameBoard.getWinningCombo(currentPlayer.getSymbol());
        if (combo != null) {
            for (int i : combo) boardButtons[i].setHighlight(true);
            statusLabel.setText("🎉  " + currentPlayer.getName() + " Wins!");
            statusLabel.setForeground(currentPlayer.getSymbol() == 'X' ? COLOR_X : COLOR_O);
            currentPlayer.incrementScore();
            endGame(currentPlayer.getName());
            return true;
        }
        if (gameBoard.isFull()) {
            statusLabel.setText("🤝  It's a Draw!");
            statusLabel.setForeground(COLOR_MUTED);
            endGame("Draw");
            return true;
        }
        return false;
    }

    private void endGame(String winner) {
        gameActive = false;
        disableBoard();
        FileHandler.logGameHistory(player1.getName(), player2.getName(), winner);
        saveScores();
        updateScoreLabels();

        Timer t = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();
            String[] opts = {"🔄  Play Again", "🏠  Main Menu", "✕  Exit"};
            int ch = JOptionPane.showOptionDialog(TicTacToe.this,
                    statusLabel.getText() + "\n\nWhat would you like to do?",
                    "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, opts, opts[0]);
            if (ch == 0)      startNewGame();
            else if (ch == 1) showMainMenu();
            else              System.exit(0);
        });
        t.setRepeats(false);
        t.start();
    }

    private void startNewGame() {
        gameBoard     = new Board(boardSize);
        gameActive    = true;
        currentPlayer = player1;
        for (GameButton b : boardButtons) b.reset();
        statusLabel.setForeground(COLOR_TEXT);
        updateStatus();
    }

    private void disableBoard() { for (GameButton b : boardButtons) b.setEnabled(false); }
    private void enableBoard()  { for (int i = 0; i < boardButtons.length; i++) if (gameBoard.isMoveValid(i)) boardButtons[i].setEnabled(true); }

    private void updateStatus() {
        statusLabel.setText(currentPlayer.getName() + "'s Turn  (" + currentPlayer.getSymbol() + ")");
        statusLabel.setForeground(currentPlayer.getSymbol() == 'X' ? COLOR_X : COLOR_O);
        updateScoreLabels();
    }

    private void updateScoreLabels() {
        player1Label.setText("<html><center>" + player1.getName() + "<br><b style='font-size:18px'>" + player1.getScore() + "</b></center></html>");
        player2Label.setText("<html><center>" + player2.getName() + "<br><b style='font-size:18px'>" + player2.getScore() + "</b></center></html>");
    }

    private void saveScores() {
        Map<String, Integer> all = FileHandler.loadScores();
        all.put(player1.getName(), player1.getScore());
        all.put(player2.getName(), player2.getScore());
        FileHandler.saveScores(all);
    }

    
    private JPanel buildPlayerCard(JLabel lbl, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(50, 50, 70), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(color);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildVS() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("SansSerif", Font.BOLD, 18));
        vs.setForeground(COLOR_MUTED);
        p.add(vs, BorderLayout.CENTER);
        return p;
    }

    private JMenuItem mkMenuItem(String text, ActionListener al) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(BG_CARD);
        item.setForeground(COLOR_TEXT);
        item.setFont(new Font("SansSerif", Font.PLAIN, 13));
        item.addActionListener(al);
        return item;
    }

    private JButton makeMenuButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setForeground(COLOR_TEXT);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        Color hov = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hov); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg);  }
        });
        return btn;
    }

    private JButton accentBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(COLOR_TEXT);
        btn.setBackground(COLOR_ACCENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(9, 24, 9, 24));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(100, 75, 210)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_ACCENT); }
        });
        return btn;
    }

    private void styleField(JTextField f) {
        f.setBackground(BG_DARK);
        f.setForeground(COLOR_TEXT);
        f.setCaretColor(COLOR_TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_ACCENT, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private JLabel mkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(COLOR_TEXT);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }

    private void showHistoryDialog(Component parent) {
        JTextArea area = new JTextArea(15, 50);
        area.setText(FileHandler.getHistory());
        area.setEditable(false);
        area.setBackground(BG_CARD);
        area.setForeground(COLOR_TEXT);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(area);
        sp.getViewport().setBackground(BG_CARD);
        JOptionPane.showMessageDialog(parent, sp, "📜  Game History", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
class GameButton extends JButton {
    private static final Color COLOR_X       = new Color(99, 221, 183);
    private static final Color COLOR_O       = new Color(255, 107, 107);
    private static final Color COLOR_ACCENT  = new Color(123, 97, 255);
    private static final Color COLOR_WIN_BG  = new Color(50, 35, 80);
    private static final Color COLOR_BTN_BG  = new Color(35, 35, 55);
    private static final Color COLOR_BTN_HOV = new Color(50, 50, 75);

    private char    symbol    = '_';
    private boolean highlight = false;
    private boolean hovered   = false;
    private final int size;

    GameButton(int size) {
        this.size = size;
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(size, size));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    void setSymbol(char s)       { symbol    = s; repaint(); }
    void setHighlight(boolean h) { highlight = h; repaint(); }
    void reset() { symbol = '_'; highlight = false; hovered = false; setEnabled(true); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight(), arc = 14;
        Color bg = highlight ? COLOR_WIN_BG : (hovered && symbol == '_') ? COLOR_BTN_HOV : COLOR_BTN_BG;
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        Color border = (highlight || (hovered && symbol == '_')) ? COLOR_ACCENT : new Color(50, 50, 75);
        g2.setColor(border);
        g2.setStroke(new BasicStroke(highlight ? 2.5f : 1.5f));
        g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
        int pad = Math.max(10, size / 5);
        float stroke = Math.max(3f, size / 22f);
        if (symbol == 'X') {
            g2.setColor(COLOR_X);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(pad, pad, w - pad, h - pad);
            g2.drawLine(w - pad, pad, pad, h - pad);
        } else if (symbol == 'O') {
            g2.setColor(COLOR_O);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(pad, pad, w - pad * 2, h - pad * 2);
        }
        g2.dispose();
    }
}


class Board {
    private final char[] board;
    private final int    size;
    private final int    cells;
    private final int    winLen;

    Board(int size) {
        this.size   = size;
        this.cells  = size * size;
        this.winLen = (size == 3) ? 3 : 4;
        board = new char[cells];
        Arrays.fill(board, '_');
    }

    int     getSize()             { return size; }
    int     getWinLen()           { return winLen; }
    boolean isMoveValid(int i)    { return i >= 0 && i < cells && board[i] == '_'; }
    void    placeMove(int i, char s) { if (isMoveValid(i)) board[i] = s; }
    void    undoMove(int i)       { if (i >= 0 && i < cells) board[i] = '_'; }
    boolean isFull()              { for (char c : board) if (c == '_') return false; return true; }
    boolean checkWinner(char s)   { return getWinningCombo(s) != null; }
    char[]  getBoardState()       { return board.clone(); }

    int scoreBoard(char me, char opp) {
        if (checkWinner(me))  return  1000;
        if (checkWinner(opp)) return -1000;
        return 0;
    }

    int[] getWinningCombo(char s) {
        
        for (int r = 0; r < size; r++)
            for (int c = 0; c <= size - winLen; c++)
                if (lineMatch(s, r, c, 0, 1)) return buildCombo(r, c, 0, 1);
        
        for (int c = 0; c < size; c++)
            for (int r = 0; r <= size - winLen; r++)
                if (lineMatch(s, r, c, 1, 0)) return buildCombo(r, c, 1, 0);
    
        for (int r = 0; r <= size - winLen; r++)
            for (int c = 0; c <= size - winLen; c++)
                if (lineMatch(s, r, c, 1, 1)) return buildCombo(r, c, 1, 1);
        for (int r = 0; r <= size - winLen; r++)
            for (int c = winLen - 1; c < size; c++)
                if (lineMatch(s, r, c, 1, -1)) return buildCombo(r, c, 1, -1);
        return null;
    }

    private boolean lineMatch(char s, int r, int c, int dr, int dc) {
        for (int k = 0; k < winLen; k++)
            if (board[(r + k * dr) * size + (c + k * dc)] != s) return false;
        return true;
    }

    private int[] buildCombo(int r, int c, int dr, int dc) {
        int[] combo = new int[winLen];
        for (int k = 0; k < winLen; k++) combo[k] = (r + k * dr) * size + (c + k * dc);
        return combo;
    }
}

abstract class Player {
    protected String name;
    protected char   symbol;
    protected int    score;
    Player(String n, char s) { name = n; symbol = s; }
    String getName()          { return name;   }
    char   getSymbol()        { return symbol; }
    int    getScore()         { return score;  }
    void   setScore(int s)    { score = s;     }
    void   incrementScore()   { score++;       }
    abstract int makeMove(Board board);
}

class HumanPlayer extends Player {
    HumanPlayer(String n, char s) { super(n, s); }
    public int makeMove(Board b)  { return -1;  }
}

class ComputerPlayer extends Player {
    private final Random rng = new Random();
    private final int    level;
    private final int    bSize;

    ComputerPlayer(char s, int level, int boardSize) {
        super("Computer", s);
        this.level = level;
        this.bSize = boardSize;
    }

    public int makeMove(Board board) {
        if (level == 1) return easyMove(board);
        if (level == 2) return mediumMove(board);
        return hardMove(board);
    }

    private int easyMove(Board b) { return randomMove(b); }

    
    private int mediumMove(Board b) {
        char opp = (symbol == 'X') ? 'O' : 'X';
        int win   = findWinOrBlock(b, symbol);
        if (win != -1 && rng.nextInt(10) < 7) return win;
        int block = findWinOrBlock(b, opp);
        if (block != -1) return block;
        if (win != -1) return win;
        int center = (bSize / 2) * bSize + bSize / 2;
        if (b.isMoveValid(center)) return center;
        return randomMove(b);
    }

    private int hardMove(Board b) {
        char opp = (symbol == 'X') ? 'O' : 'X';
        int win   = findWinOrBlock(b, symbol); if (win  != -1) return win;
        int block = findWinOrBlock(b, opp);    if (block != -1) return block;
        return (bSize == 3) ? minimaxBest(b) : heuristicBest(b);
    }

    private int minimaxBest(Board b) {
        int best = Integer.MIN_VALUE, bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (!b.isMoveValid(i)) continue;
            b.placeMove(i, symbol);
            int sc = minimax(b, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            b.undoMove(i);
            if (sc > best) { best = sc; bestMove = i; }
        }
        return bestMove == -1 ? randomMove(b) : bestMove;
    }

    private int minimax(Board b, boolean isMax, int alpha, int beta, int depth) {
        char opp = (symbol == 'X') ? 'O' : 'X';
        int sc = b.scoreBoard(symbol, opp);
        if (sc != 0) return sc - depth * (sc > 0 ? 1 : -1);
        if (b.isFull()) return 0;
        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (!b.isMoveValid(i)) continue;
                b.placeMove(i, symbol);
                best = Math.max(best, minimax(b, false, alpha, beta, depth + 1));
                b.undoMove(i);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (!b.isMoveValid(i)) continue;
                b.placeMove(i, opp);
                best = Math.min(best, minimax(b, true, alpha, beta, depth + 1));
                b.undoMove(i);
                beta = Math.min(beta, best);
                if (beta <= alpha) break;
            }
            return best;
        }
    }

    private int heuristicBest(Board b) {
        int best = Integer.MIN_VALUE, bestMove = -1;
        int maxD = (bSize == 5) ? 3 : 2;
        char opp = (symbol == 'X') ? 'O' : 'X';
        for (int i = 0; i < bSize * bSize; i++) {
            if (!b.isMoveValid(i)) continue;
            b.placeMove(i, symbol);
            int sc = hMinimax(b, false, maxD, Integer.MIN_VALUE, Integer.MAX_VALUE);
            b.undoMove(i);
            int r = i / bSize, c = i % bSize, mid = bSize / 2;
            sc += (bSize - Math.abs(r - mid) - Math.abs(c - mid));
            if (sc > best) { best = sc; bestMove = i; }
        }
        return bestMove == -1 ? randomMove(b) : bestMove;
    }

    private int hMinimax(Board b, boolean isMax, int depth, int alpha, int beta) {
        char opp = (symbol == 'X') ? 'O' : 'X';
        int sc = b.scoreBoard(symbol, opp);
        if (sc != 0 || depth == 0 || b.isFull()) return sc + countThreats(b, symbol) - countThreats(b, opp) * 2;
        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < bSize * bSize; i++) {
                if (!b.isMoveValid(i)) continue;
                b.placeMove(i, symbol);
                best = Math.max(best, hMinimax(b, false, depth - 1, alpha, beta));
                b.undoMove(i);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < bSize * bSize; i++) {
                if (!b.isMoveValid(i)) continue;
                b.placeMove(i, opp);
                best = Math.min(best, hMinimax(b, true, depth - 1, alpha, beta));
                b.undoMove(i);
                beta = Math.min(beta, best);
                if (beta <= alpha) break;
            }
            return best;
        }
    }

    private int countThreats(Board b, char s) {
        int count = 0, wl = b.getWinLen(), n = bSize;
        char[] st = b.getBoardState();
        for (int r = 0; r < n; r++)
            for (int c = 0; c <= n - wl; c++) {
                int mine = 0, empty = 0;
                for (int k = 0; k < wl; k++) { char ch = st[r*n+(c+k)]; if (ch==s) mine++; else if (ch=='_') empty++; }
                if (mine + empty == wl) count += mine;
            }
        for (int c = 0; c < n; c++)
            for (int r = 0; r <= n - wl; r++) {
                int mine = 0, empty = 0;
                for (int k = 0; k < wl; k++) { char ch = st[(r+k)*n+c]; if (ch==s) mine++; else if (ch=='_') empty++; }
                if (mine + empty == wl) count += mine;
            }
        return count;
    }

    private int findWinOrBlock(Board b, char s) {
        for (int i = 0; i < bSize * bSize; i++) {
            if (!b.isMoveValid(i)) continue;
            b.placeMove(i, s);
            boolean win = b.checkWinner(s);
            b.undoMove(i);
            if (win) return i;
        }
        return -1;
    }

    private int randomMove(Board b) {
        List<Integer> empty = new ArrayList<>();
        for (int i = 0; i < bSize * bSize; i++) if (b.isMoveValid(i)) empty.add(i);
        return empty.isEmpty() ? -1 : empty.get(rng.nextInt(empty.size()));
    }
}


class FileHandler {
    private static final String SCORE_FILE   = "scores.txt";
    private static final String HISTORY_FILE = "game_history.log";

    static Map<String, Integer> loadScores() {
        Map<String, Integer> m = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(":");
                if (p.length == 2) m.put(p[0], Integer.parseInt(p[1].trim()));
            }
        } catch (IOException ignored) {}
        return m;
    }

    static void saveScores(Map<String, Integer> scores) {
        try (PrintWriter w = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (Map.Entry<String, Integer> e : scores.entrySet())
                w.println(e.getKey() + ":" + e.getValue());
        } catch (IOException e) { e.printStackTrace(); }
    }

    static void logGameHistory(String p1, String p2, String winner) {
        try (PrintWriter w = new PrintWriter(new FileWriter(HISTORY_FILE, true))) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            w.println("[" + time + "] " + p1 + " vs " + p2 + "  →  Winner: " + winner);
        } catch (IOException e) { e.printStackTrace(); }
    }

    static String getHistory() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = r.readLine()) != null) sb.append(line).append("\n");
        } catch (IOException ignored) {}
        String s = sb.toString();
        return s.isEmpty() ? "No history found." : s;
    }
}