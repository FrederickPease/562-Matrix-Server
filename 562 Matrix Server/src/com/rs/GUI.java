package com.rs;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.World;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;
import javax.swing.JButton;
import java.awt.GridLayout;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField commandsTextField;
	private JTextPane textPane = new JTextPane();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel commandsPanel = new JPanel();
		tabbedPane.addTab("Commands", null, commandsPanel, null);
		commandsPanel.setLayout(new BorderLayout(0, 0));
		
		commandsTextField = new JTextField();
		commandsPanel.add(commandsTextField, BorderLayout.SOUTH);
		commandsTextField.setColumns(10);
		
		final JScrollPane commandsScrollPane = new JScrollPane();
		commandsPanel.add(commandsScrollPane, BorderLayout.CENTER);
		
		commandsScrollPane.setViewportView(textPane);
		
		commandsTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					addText(commandsTextField.getText());
					processCommand(commandsTextField.getText());
					commandsTextField.setText(null);
					commandsScrollPane.setAutoscrolls(true);
				}
			}
		});
		tabbedPane.setEnabledAt(0, true);
		
		JPanel playersPanel = new JPanel();
		tabbedPane.addTab("Players", null, playersPanel, null);
		playersPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		playersPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
	}

	public void addText(String text) {
		textPane.setText(textPane.getText() + "\n" + text);
	}
	
	private void processCommand(String command) {
		String[] cmd = command.toLowerCase().split(" ");
		String name = "";
		switch (cmd[0]) {
		case "?":
			String[] commands = {	"? - shows a list of commands",
									"equipment [player] - shows a list of what the player is wearing",
									"yell [text] - sends a message to the server"};
			for (String i : commands)
				addText(i);
			break;
		case "equipment":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player target = World.getPlayerByDisplayName(name);
			name = Utils.formatPlayerNameForProtocol(name);
			if (!SerializableFilesManager.containsPlayer(name)) {
				addText("Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
				return;
			}
			target = SerializableFilesManager.loadPlayer(name);
			addText("Equipment for: " + Utils.formatPlayerNameForDisplay(name));
			for (int i = 0; i < Equipment.EQUIPMENT_SLOTS.length; i++) {
				int[] eIds = { target.getEquipment().getHatId(),	target.getEquipment().getCapeId(),
							target.getEquipment().getAmuletId(),target.getEquipment().getWeaponId(),
							target.getEquipment().getChestId(),	target.getEquipment().getShieldId(),
							target.getEquipment().getLegsId(),	target.getEquipment().getGlovesId(),
							target.getEquipment().getBootsId(),	target.getEquipment().getRingId(),
							target.getEquipment().getAmmoId() };
				name = (eIds[i] < 0 ? "Nothing" : ItemDefinitions.getItemDefinitions(eIds[i]).getName());
				addText(Equipment.EQUIPMENT_SLOTS[i] + " - " + eIds[i] + " - " + name);
			}
			break;
		case "yell":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			World.sendWorldWideMessage("[Server] - " + name);
			addText("[Server] - " + name);
			break;
		default:
			addText("Error: no such command, do '?' for a list of commands");
			break;
		}
	}
}