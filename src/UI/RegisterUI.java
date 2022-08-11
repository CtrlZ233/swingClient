package UI;

import javax.swing.*;

import MsgAdapter.MsgBuilder;
import MsgAdapter.MsgDefine.MsgType;
import Service.Service;

import java.awt.*;
import UI.Utils.*;
import java.awt.event.*;
import java.util.Objects;

import Utils.ByteTransform;;

public class RegisterUI extends ToasterFrame {

    private TextFieldUsername usernameField = new TextFieldUsername();
    private TextFieldPassword passwordField = new TextFieldPassword();

    public RegisterUI() {
        super();
        addLogo(mainJPanel);
        addSeparator(mainJPanel);
        addUsernameTextField(mainJPanel);
        addPasswordTextField(mainJPanel);
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

        Dimension size = new Dimension(400, 600);

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

    @Override
    public void addLogo(JPanel panel1) {
        JLabel label1 = new JLabel();
        label1.setFocusable(false);
        label1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("lumo_placeholder.png")).getFile()));
        panel1.add(label1);
        label1.setBounds(100, 50, 200, 110);
    }

    private void addSeparator(JPanel panel1) {
        JSeparator separator1 = new JSeparator();
        separator1.setOrientation(SwingConstants.HORIZONTAL);
        separator1.setForeground(UIUtils.COLOR_OUTLINE);
        panel1.add(separator1);
        separator1.setBounds(50, 200, 280, 1);
    }

    private void addUsernameTextField(JPanel panel1) {
        usernameField.setBounds(70, 300, 250, 44);
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
        passwordField.setBounds(70, 360, 250, 44);
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
                    regEventHandle();
            }
        });

        panel1.add(passwordField);
    }

    private void regEventHandle() {
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
        MsgBuilder builder = new MsgBuilder(MsgType.REGISTER, service.getPid());
        service.send(builder.msgPackage(ByteTransform.fillZero(username.getBytes(), UIUtils.USERNAME_MAX_LEN), 
                                                      ByteTransform.fillZero(passWd.getBytes(), UIUtils.PASSWORD_MAX_LEN)));
    }

    private void addRegisterButton(JPanel panel1) {
        panel1.add(new HyperlinkText(UIUtils.BUTTON_TEXT_REGISTER, 170, 500, () -> {
            regEventHandle();
        }));
    }

}