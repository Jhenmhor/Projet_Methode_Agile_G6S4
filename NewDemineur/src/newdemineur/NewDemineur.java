/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newdemineur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author p1603760
 */
public class NewDemineur extends JFrame implements ActionListener, MouseListener {

    int rows = 10;      //Lignes et colonnes
    int cols = 10;
    int numMines = 10;  //NB MINES
    GridLayout layout = new GridLayout(rows, cols);
    Object[][] data = new Object[0][2];
    /*type[][] name = new type[rows][cols];
     * type[x][y];
     * is 1d
     * type[] name = new type[rows*cols];
     * type[(rows*y)+x];*/
    boolean[] mines = new boolean[rows * cols];
    boolean[] clickable = new boolean[rows * cols];
    String[] state = new String[rows * cols];
    boolean lost = false;
    boolean won = false;
    int[] numbers = new int[rows * cols];
    JButton[] buttons = new JButton[rows * cols];
    boolean[] clickdone = new boolean[rows * cols];
    JMenuItem score = new JMenuItem("Score");
    JMenuItem newGameButton = new JMenuItem("New game");
    JMenuItem cProfil = new JMenuItem("Change profile");
    JMenuItem classicColor = new JMenuItem("Classic");
    JMenuItem awfulColor = new JMenuItem("Rouge");
    JMenuItem regles = new JMenuItem("Regles");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem normal = new JMenuItem("Normal");
    JMenuItem hard = new JMenuItem("Hard");
    JMenuItem custom = new JMenuItem("Custom");
    JLabel mineLabel = new JLabel("mines: " + numMines + " marked: 0");
    JPanel p = new JPanel();

    public NewDemineur() {

        changeProfil();

        p.setLayout(layout);
        setupI();

        for (int i = 0; i < (rows * cols); i++) {

            p.add(buttons[i]);

        }

        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Menu");
        JMenu c = new JMenu("Préférence");
        JMenu d = new JMenu("Difficulty");
        classicColor.addActionListener(this);
        c.add(classicColor);
        awfulColor.addActionListener(this);
        c.add(awfulColor);
        newGameButton.addActionListener(this);
        m.add(newGameButton);
        cProfil.addActionListener(this);
        m.add(cProfil);
        score.addActionListener(this);
        m.add(score);
        regles.addActionListener(this);
        m.add(regles);
        easy.addActionListener(this);
        d.add(easy);
        normal.addActionListener(this);
        d.add(normal);
        hard.addActionListener(this);
        d.add(hard);
        custom.addActionListener(this);
        d.add(custom);
        mb.add(m);
        mb.add(c);
        mb.add(d);
        this.setJMenuBar(mb);
        this.add(p);
        this.add(mineLabel, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        showRegles();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void showRegles() {
        final Container c = getContentPane();
        JOptionPane.showMessageDialog(c,
                "Le but du jeu est de découvrir toutes les cases libres sans faire exploser les mines." + "\n"
                + "Pour libérer une case, faire un clic gauche (clic normal)" + "\n"
                + "Pour marquer une mine, faire un clic droit, qui fera apparaître un X." + "\n"
                + "Le compteur en bas à droite indique le nombre de mines qu'il reste à trouver." + "\n"
                + "Le chiffre qui s'affiche sur les cases cliquées indique le nombre de mines se trouvant à proximité : " + "\n"
                + "à gauche ou à droite, en haut ou en bas, ou en diagonale.", "",
                JOptionPane.INFORMATION_MESSAGE);

    }

    public void changeProfil() {
        String input = JOptionPane.showInputDialog("Your Name : ");
        this.setTitle(input);
    }

    public void fillMines() {
        int needed = this.numMines;
        while (needed > 0) {
            int x = (int) Math.floor(Math.random() * rows);
            int y = (int) Math.floor(Math.random() * cols);
            if (!mines[(rows * y) + x]) {
                mines[(rows * y) + x] = true;
                needed--;
            }
        }
    }

    public void fillNumbers() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                int cur = (rows * y) + x;
                if (mines[cur]) {
                    numbers[cur] = 0;
                    continue;
                }
                int temp = 0;
                boolean l = (x - 1) >= 0;
                boolean r = (x + 1) < rows;
                boolean u = (y - 1) >= 0;
                boolean d = (y + 1) < cols;
                int left = (rows * (y)) + (x - 1);
                int right = (rows * (y)) + (x + 1);
                int up = (rows * (y - 1)) + (x);
                int upleft = (rows * (y - 1)) + (x - 1);
                int upright = (rows * (y - 1)) + (x + 1);
                int down = (rows * (y + 1)) + (x);
                int downleft = (rows * (y + 1)) + (x - 1);
                int downright = (rows * (y + 1)) + (x + 1);
                if (u) {
                    if (mines[up]) {
                        temp++;
                    }
                    if (l) {
                        if (mines[upleft]) {
                            temp++;
                        }
                    }
                    if (r) {
                        if (mines[upright]) {
                            temp++;
                        }
                    }
                }
                if (d) {
                    if (mines[down]) {
                        temp++;
                    }
                    if (l) {
                        if (mines[downleft]) {
                            temp++;
                        }
                    }
                    if (r) {
                        if (mines[downright]) {
                            temp++;
                        }
                    }
                }
                if (l) {
                    if (mines[left]) {
                        temp++;
                    }
                }
                if (r) {
                    if (mines[right]) {
                        temp++;
                    }
                }
                numbers[cur] = temp;
            }
        }
    }

    public void setupI() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                mines[(rows * y) + x] = false;
                clickdone[(rows * y) + x] = false;
                clickable[(rows * y) + x] = true;
                state[(rows * y) + x] = "";
                buttons[(rows * y) + x] = new JButton( /*"" + ( x * y )*/);
                buttons[(rows * y) + x].setPreferredSize(new Dimension(45, 45));
                buttons[(rows * y) + x].addActionListener(this);
                buttons[(rows * y) + x].addMouseListener(this);
            }
        }
        fillMines();
        fillNumbers();
        go_Chrono();
    }

    public void setupI2() {
        this.remove(p);
        p = new JPanel();
        layout = new GridLayout(rows, cols);
        p.setLayout(layout);
        buttons = new JButton[rows * cols];
        mines = new boolean[rows * cols];
        clickdone = new boolean[rows * cols];
        clickable = new boolean[rows * cols];
        String[] state = new String[rows * cols];
        this.state = state;
        numbers = new int[rows * cols];
        setupI();
        for (int i = 0; i < (rows * cols); i++) {
            p.add(buttons[i]);
        }
        this.add(p);
        this.pack();
        fillNumbers();
        go_Chrono();
    }

    public void setup() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                mines[(rows * y) + x] = false;
                clickdone[(rows * y) + x] = false;
                clickable[(rows * y) + x] = true;
                state[(rows * y) + x] = "";
                buttons[(rows * y) + x].setEnabled(true);
                buttons[(rows * y) + x].setText("");
            }
        }
        fillMines();
        fillNumbers();
        go_Chrono();
        lost = false;
        mineLabel.setText("mines: " + numMines + " marked: 0");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == custom) {
            this.cols = this.rows = Integer.parseInt((String) JOptionPane.showInputDialog(
                    this, "Size", "Size", JOptionPane.PLAIN_MESSAGE,
                    null, null, 10));
            this.numMines = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Mines", "Mines",
                    JOptionPane.PLAIN_MESSAGE, null, null, 10));
            setupI2();
        }

        if (e.getSource() == easy) {
            this.rows = 10;
            this.cols = 10;
            this.numMines = 10;

            setupI2();
        }
        if (e.getSource() == normal) {
            this.rows = 15;
            this.cols = 15;
            this.numMines = 25;

            setupI2();
        }
        if (e.getSource() == hard) {
            this.rows = 20;
            this.cols = 20;
            this.numMines = 50;

            setupI2();
        }

        if (e.getSource() == score) {
            showScore();
        }

        if (!won) {
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    if (e.getSource() == buttons[(rows * y) + x]
                            && !won && clickable[(rows * y) + x]) {
                        doCheck(x, y);
                        break;
                    }
                }
            }
        }
        if (e.getSource() == newGameButton) {
            setup();
            won = false;
            return;

        }

        if (e.getSource() == regles) {
            showRegles();
        }

        if (e.getSource() == cProfil) {
            changeProfil();
        }

        if (e.getSource() == classicColor) {
            for (int i = 0; i < (rows * cols); i++) {
                buttons[i].setBackground(new JButton().getBackground());
            }
        }

        if (e.getSource() == awfulColor) {
            for (int i = 0; i < (rows * cols); i++) {
                if (!clickdone[i]) {
                    buttons[i].setBackground(Color.RED);
                }
                if (clickdone[i]) {
                    buttons[i].setBackground(new JButton().getBackground());
                }
            }
        }

        checkWin();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {
            int n = 0;
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {

                    int ind = (rows * y) + x;

                    if (e.getSource() == buttons[ind]) {
                        clickable[ind] = !clickable[ind];
                        if (state[ind].equals("")) {
                            state[ind] = "X";
                            buttons[ind].setText("X");
                        } else if (state[ind].equals("X")) {
                            state[ind] = "?";
                            buttons[ind].setText("?");
                        } else {
                            state[ind] = "";
                            buttons[ind].setText("");
                        }
                    }

                    if (!clickdone[ind]) {

                        if (!clickable[ind]) {
                            n++;
                        }

                        mineLabel.setText("mines: " + numMines + " marked: " + n);

                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void doCheck(int x, int y) {
        int cur = (rows * y) + x;
        boolean l = (x - 1) >= 0;
        boolean r = (x + 1) < rows;
        boolean u = (y - 1) >= 0;
        boolean d = (y + 1) < cols;
        int left = (rows * (y)) + (x - 1);
        int right = (rows * (y)) + (x + 1);
        int up = (rows * (y - 1)) + (x);
        int upleft = (rows * (y - 1)) + (x - 1);
        int upright = (rows * (y - 1)) + (x + 1);
        int down = (rows * (y + 1)) + (x);
        int downleft = (rows * (y + 1)) + (x - 1);
        int downright = (rows * (y + 1)) + (x + 1);

        clickdone[cur] = true;
        buttons[cur].setEnabled(false);
        if (numbers[cur] == 0 && !mines[cur] && !lost && !won) {
            if (u && !won) {
                if (!clickdone[up] && !mines[up]) {
                    clickdone[up] = true;
                    buttons[up].doClick();
                    buttons[up].setBackground(new JButton().getBackground());
                }
                if (l && !won) {
                    if (!clickdone[upleft] && numbers[upleft] != 0
                            && !mines[upleft]) {
                        clickdone[upleft] = true;
                        buttons[upleft].doClick();
                        buttons[upleft].setBackground(new JButton().getBackground());
                    }
                }
                if (r && !won) {
                    if (!clickdone[upright] && numbers[upright] != 0
                            && !mines[upright]) {
                        clickdone[upright] = true;
                        buttons[upright].doClick();
                        buttons[upright].setBackground(new JButton().getBackground());
                    }
                }
            }
            if (d && !won) {
                if (!clickdone[down] && !mines[down]) {
                    clickdone[down] = true;
                    buttons[down].doClick();
                    buttons[down].setBackground(new JButton().getBackground());
                }
                if (l && !won) {
                    if (!clickdone[downleft] && numbers[downleft] != 0
                            && !mines[downleft]) {
                        clickdone[downleft] = true;
                        buttons[downleft].doClick();
                        buttons[downleft].setBackground(new JButton().getBackground());
                    }
                }
                if (r && !won) {
                    if (!clickdone[downright]
                            && numbers[downright] != 0
                            && !mines[downright]) {
                        clickdone[downright] = true;
                        buttons[downright].doClick();
                        buttons[downright].setBackground(new JButton().getBackground());
                    }
                }
            }
            if (l && !won) {
                if (!clickdone[left] && !mines[left]) {
                    clickdone[left] = true;
                    buttons[left].doClick();
                    buttons[left].setBackground(new JButton().getBackground());
                }
            }
            if (r && !won) {
                if (!clickdone[right] && !mines[right]) {
                    clickdone[right] = true;
                    buttons[right].doClick();
                    buttons[right].setBackground(new JButton().getBackground());
                }
            }
        } else {
            buttons[cur].setText("" + numbers[cur]);
            if (!mines[cur] && numbers[cur] == 0) {
                buttons[cur].setText("");
            }
        }
        if (mines[cur] && !won) {
            buttons[cur].setText("0");
            doLose();
        }
    }

    public void checkWin() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                int cur = (rows * y) + x;
                if (!clickdone[cur]) {
                    if (mines[cur]) {
                        continue;
                    } else {
                        return;
                    }
                }
            }
        }

        doWin();
    }

    public void doWin() {
        if (!lost && !won) {
            won = true;
            long temps = stop_Chrono();
            JOptionPane.showMessageDialog(null,
                    "Tu as gagné en " + temps / 1000 + " secondes", "Bien jouÃ©!",
                    JOptionPane.INFORMATION_MESSAGE);

            Object[] newPlayer = {this.getTitle(), temps / 1000};

            this.data = add(this.data, newPlayer);

            newGameButton.doClick();
        }
    }

    public void doLose() {
        if (!lost && !won) {
            lost = true;
            for (int i = 0; i < rows * cols; i++) {
                if (!clickdone[i]) {
                    buttons[i].doClick(0);
                }
            }
            long temps = stop_Chrono();
            JOptionPane.showMessageDialog(null,
                    "Tu as perdu en " + temps / 1000 + " secondes", "Dommage!",
                    JOptionPane.ERROR_MESSAGE);
            setup();
        }
    }

    static long chrono = 0;

    static void go_Chrono() {
        chrono = java.lang.System.currentTimeMillis();
    }

    static long stop_Chrono() {
        long chrono2 = java.lang.System.currentTimeMillis();
        long temps = chrono2 - chrono;
        return temps;
    }

    public void showScore() {
        String column[] = {"Nom", "Score"};
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTable jt = new JTable(data,column);
        JScrollPane sp=new JScrollPane(jt);    
        
        f.add(sp);
        f.setSize(300, 400);
        f.setVisible(true);

    }

    public static Object[][] add(Object[][] arr, Object... elements) {
        Object[][] tempArr = new Object[arr.length + elements.length][];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for (int i = 0; i < elements.length; i++) {
            tempArr[arr.length + i][0] = elements[i];
        }
        return tempArr;

    }

    public static void main(String[] args) {
        new NewDemineur();
        go_Chrono();
    }
}
