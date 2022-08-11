package UI;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;

import MsgAdapter.MsgBuilder;
import MsgAdapter.MsgDefine.MsgType;
import Service.Service;
import UI.Toaster.Toaster;
import UI.UIManager.UIType;
import UI.Utils.*;
import UI.UIManager;
import Utils.ByteTransform;

public class LoginUI extends ToasterFrame {
    TextFieldUsername usernameField = new TextFieldUsername();
    TextFieldPassword passwordField = new TextFieldPassword();

    public static void main(String[] args) {
        ToasterFrame ui = new LoginUI();
        UIManager.getInstance().registUI(UIType.LOGIN, ui);
        UIManager.getInstance().setCurrentUI(ui);
    }

    private LoginUI() {
        super();
        startService();
        addLogo(mainJPanel);

        addSeparator(mainJPanel);

        addUsernameTextField(mainJPanel);

        addPasswordTextField(mainJPanel);

        addLoginButton(mainJPanel);

        addForgotPasswordButton(mainJPanel);

        addRegisterButton(mainJPanel);

        this.add(mainJPanel);
        this.pack();
        this.setVisible(true);
        this.toFront();
        this.setResizable(false);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
    }

    @Override
    public JPanel getMainJPanel() {
        this.setUndecorated(false);

        Dimension size = new Dimension(800, 400);

        JPanel panel1 = new JPanel();
        panel1.setSize(size);
        panel1.setPreferredSize(size);
        panel1.setBackground(UIUtils.COLOR_BACKGROUND);
        panel1.setLayout(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return panel1;
    }

    private void addSeparator(JPanel panel1) {
        JSeparator separator1 = new JSeparator();
        separator1.setOrientation(SwingConstants.VERTICAL);
        separator1.setForeground(UIUtils.COLOR_OUTLINE);
        panel1.add(separator1);
        separator1.setBounds(310, 80, 1, 240);
    }

    @Override
    public void addLogo(JPanel panel1) {
        JLabel label1 = new JLabel();
        label1.setFocusable(false);
        label1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("lumo_placeholder.png")).getFile()));
        panel1.add(label1);
        label1.setBounds(55, 146, 200, 110);
    }

    private void addUsernameTextField(JPanel panel1) {
        usernameField.setBounds(423, 109, 250, 44);
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(UIUtils.PLACEHOLDER_TEXT_USERNAME)) {
                    usernameField.setText("");
                }
                usernameField.setForeground(Color.white);
                usernameField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText(UIUtils.PLACEHOLDER_TEXT_USERNAME);
                }
                usernameField.setForeground(UIUtils.COLOR_OUTLINE);
                usernameField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        panel1.add(usernameField);
    }

    private void addPasswordTextField(JPanel panel1) {
        passwordField.setBounds(423, 168, 250, 44);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setForeground(Color.white);
                passwordField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setForeground(UIUtils.COLOR_OUTLINE);
                passwordField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    loginEventHandler();
            }
        });

        panel1.add(passwordField);
    }

    private void addLoginButton(JPanel panel1) {
        final Color[] loginButtonColors = {UIUtils.COLOR_INTERACTIVE, Color.white};

        JLabel loginButton = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                super.paintComponent(g2);

                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                g2.setColor(loginButtonColors[0]);
                g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                FontMetrics metrics = g2.getFontMetrics(UIUtils.FONT_GENERAL_UI);
                int x2 = (getWidth() - metrics.stringWidth(UIUtils.BUTTON_TEXT_LOGIN)) / 2;
                int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                g2.setColor(loginButtonColors[1]);
                g2.drawString(UIUtils.BUTTON_TEXT_LOGIN, x2, y2);
            }
        };

        loginButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                loginEventHandler();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButtonColors[0] = UIUtils.COLOR_INTERACTIVE_DARKER;
                loginButtonColors[1] = UIUtils.OFFWHITE;
                loginButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButtonColors[0] = UIUtils.COLOR_INTERACTIVE;
                loginButtonColors[1] = Color.white;
                loginButton.repaint();
            }
        });

        loginButton.setBackground(UIUtils.COLOR_BACKGROUND);
        loginButton.setBounds(423, 247, 250, 44);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel1.add(loginButton);
    }

    private void addForgotPasswordButton(JPanel panel1) {
        panel1.add(new HyperlinkText(UIUtils.BUTTON_TEXT_FORGOT_PASS, 423, 300, () -> {
            toaster.error("Register anohter one.");
        }));
    }

    private void addRegisterButton(JPanel panel1) {
        panel1.add(new HyperlinkText(UIUtils.BUTTON_TEXT_REGISTER, 631, 300, () -> {
            this.setVisible(false);
            ToasterFrame ui = new RegisterUI();
            UIManager.getInstance().registUI(UIType.REGISTER, ui);
            UIManager.getInstance().setCurrentUI(ui);
        }));
    }

    private void loginEventHandler() {
        String username = usernameField.getText();
        String passWd = passwordField.getText();
        if (username.length() < UIUtils.USERNAME_MIN_LEN || username.length() > UIUtils.USERNAME_MAX_LEN) {
            String out = String.format("用户名长度必须大于%d，小于%d！", UIUtils.USERNAME_MIN_LEN, UIUtils.USERNAME_MAX_LEN);
            toaster.error(out);
            return;
        }

        if (passWd.length() < UIUtils.PASSWORD_MIN_LEN || passWd.length() > UIUtils.PASSWORD_MAX_LEN) {
            String out = String.format("密码长度必须大于%d，小于%d！", UIUtils.PASSWORD_MIN_LEN, UIUtils.PASSWORD_MAX_LEN);
            toaster.error(out);
            return;
        }
        Service service = Service.getInstance();
        if (service == null) {
            toaster.error("网络错误");
            return;
        }
        MsgBuilder builder = new MsgBuilder(MsgType.LOGIN, service.getPid());
        service.send(builder.msgPackage(ByteTransform.fillZero(username.getBytes(), UIUtils.USERNAME_MAX_LEN), 
                                                      ByteTransform.fillZero(passWd.getBytes(), UIUtils.PASSWORD_MAX_LEN)));
    }

    private void startService() {
        Thread recv = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Service.getInstance() == null) {
                    try {
                        // 每秒重试
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Service.getInstance().recv();
            }
        });

        Thread msgDispatch = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Service.getInstance() == null) {
                    try {
                        // 每秒重试
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Service.getInstance().messageDispatch();
            } 
        });

        recv.start();
        msgDispatch.start();
    }
}