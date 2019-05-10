package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

import gui.enums.Language;
import gui.enums.Rule;
import gui.menu.BackgroundMenuBar;
import logic.InstanceManager;

public class ApplicationWindow {

	ChartPanel cp;

	TasksDialog td;
	double[] durations;
	double[] dueDates;
	boolean displayedTasks = false;
	DurationsDialog pd;
	boolean displayedDurations = false;
	DueDatesDialog dd;
	boolean displayedDuedates = false;

	private JFrame frame;
	private JPanel configPanel;
	private JButton btnCargar;
	private JButton btnGuardar;
	private BackgroundMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenu mnVer;
	private JMenu mnAyuda;
	private JMenu mnHerramientas;
	private JMenu mnConfiguracin;
	private JMenuItem mntmCargar;
	private JMenuItem mntmGuardar;
	private JMenuItem mntmSalir;
	private JMenuItem mntmManual;
	private JMenuItem mntmInformacion;
	private JMenuItem mntmGenerarInstancias;
	private JMenu mnIdioma;
	private JMenuItem mntmEspanol;
	private JMenuItem mntmIngles;
	private JCheckBoxMenuItem chckbxmntmDuracin;
	private JCheckBoxMenuItem chckbxmntmFechaDeVencimiento;
	private JCheckBoxMenuItem chckbxmntmTareas;
	private JComboBox<String> rulesComboBox;
	private JLabel lblRegla;
	private JLabel lblG;
	private JLabel lblSeparacinEntreMarcas;
	private JPanel buttonPanel;
	private JPanel rulesPanel;
	private JPanel axisPanel;
	private JSpinner rulesSpinner;
	private JSpinner axisSpinner;
	private JButton btnSiguiente;
	private JButton btnFinal;

	private ResourceBundle texts;
	private InstanceManager manager = new InstanceManager();
	private Language language;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.put("Menu.opaque", true);
					UIManager.put("CheckBoxMenuItem.opaque", true);
					UIManager.put("MenuItem.opaque", true);
					ApplicationWindow window = new ApplicationWindow();
					window.frame.setVisible(true);
					window.frame.setBackground(Color.WHITE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		BorderLayout borderLayout = (BorderLayout) frame.getContentPane().getLayout();
		borderLayout.setVgap(10);
		borderLayout.setHgap(10);
		texts = ResourceBundle.getBundle("rcs/texts", new Locale("en"));
		language = Language.ENGLISH;
		frame.setTitle(texts.getString("menu_title"));
		frame.setBounds(100, 100, 1100, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(getConfigPanel(), BorderLayout.SOUTH);
		frame.getContentPane().add(getMenuBar(), BorderLayout.NORTH);
	}

	private JPanel getConfigPanel() {
		if (configPanel == null) {
			configPanel = new JPanel();
			configPanel.setBorder(new LineBorder(new Color(64, 64, 64), 5));
			configPanel.setBackground(Color.DARK_GRAY);
			GridBagLayout gbl_configPanel = new GridBagLayout();
			gbl_configPanel.columnWidths = new int[] { 254, 254, 254, 0 };
			gbl_configPanel.rowHeights = new int[] { 23, 0 };
			gbl_configPanel.columnWeights = new double[] { 0.75, 0, 0.5, Double.MIN_VALUE };
			gbl_configPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			configPanel.setLayout(gbl_configPanel);
			GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
			gbc_buttonPanel.fill = GridBagConstraints.BOTH;
			gbc_buttonPanel.gridx = 0;
			gbc_buttonPanel.gridy = 0;
			configPanel.add(getButtonPanel(), gbc_buttonPanel);
			GridBagConstraints gbc_rulesPanel = new GridBagConstraints();
			gbc_rulesPanel.fill = GridBagConstraints.BOTH;
			gbc_rulesPanel.gridx = 1;
			gbc_rulesPanel.gridy = 0;
			configPanel.add(getRulesPanel(), gbc_rulesPanel);
			GridBagConstraints gbc_axisPanel = new GridBagConstraints();
			gbc_axisPanel.fill = GridBagConstraints.BOTH;
			gbc_axisPanel.gridx = 2;
			gbc_axisPanel.gridy = 0;
			configPanel.add(getAxisPanel(), gbc_axisPanel);
		}
		return configPanel;
	}

	private JButton getBtnCargar() {
		if (btnCargar == null) {
			btnCargar = new JButton(texts.getString("button_load"));
			btnCargar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					load();
				}
			});
		}
		return btnCargar;
	}

	/**
	 * Método auxiliar que carga un gráfico a partir de un fichero de texto
	 * seleccionado por el usuario con un diálogo. También muestra los diálogos
	 * adicionales en función de las opciones seleccionadas por el usuario en el
	 * menu
	 */
	private void load() {
		if (cp != null)
			frame.remove(cp);

		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();

			manager.readInstance(file.getAbsolutePath());
			manager.initializeChartManager(texts);
			manager.loadMainChart((int) getAxisSpinner().getValue());

			cp = new ChartPanel(manager.getChart());
			frame.getContentPane().add(cp, BorderLayout.CENTER);
			frame.revalidate();

			getRulesComboBox().setEnabled(true);

			getBtnSiguiente().setEnabled(true);
			getBtnFinal().setEnabled(true);

			getMntmGuardar().setEnabled(true);

			getChckbxmntmDuracin().setEnabled(true);
			getChckbxmntmFechaDeVencimiento().setEnabled(true);
			getChckbxmntmTareas().setEnabled(true);

			getBtnGuardar().setEnabled(true);

			if (getChckbxmntmTareas().isSelected()) {
				if (td != null)
					td.dispose();
				createTasksTable();
				displayedTasks = true;
			} else if (td != null) {
				td.dispose();
				displayedTasks = false;
			}
			if (getChckbxmntmDuracin().isSelected()) {
				if (pd != null)
					pd.dispose();
				createDurationsChart();
				displayedDurations = true;
			} else if (pd != null) {
				pd.dispose();
				displayedDurations = false;
			}
			if (getChckbxmntmFechaDeVencimiento().isSelected()) {
				if (dd != null)
					dd.dispose();
				createDueDatesChart();
				displayedDuedates = true;
			} else if (dd != null) {
				dd.dispose();
				displayedDuedates = false;
			}
		}
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton(texts.getString("button_save"));
			btnGuardar.setEnabled(false);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					int returnVal = jfc.showOpenDialog(frame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = jfc.getSelectedFile();
						manager.writeChart(file.getAbsolutePath() + ".pdf");
					}
				}
			});
		}
		return btnGuardar;
	}

	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new BackgroundMenuBar();
			menuBar.setColor(Color.DARK_GRAY);
			menuBar.add(getMnArchivo());
			menuBar.add(getMnVer());
			menuBar.add(getMnHerramientas());
			menuBar.add(getMnConfiguracin());
			menuBar.add(getMnAyuda());
		}
		return menuBar;
	}

	private JMenu getMnArchivo() {
		if (mnArchivo == null) {
			mnArchivo = new JMenu(texts.getString("menu_file"));
			if (language.equals(Language.ENGLISH))
				mnArchivo.setMnemonic('F');
			else if (language.equals(Language.SPANISH))
				mnArchivo.setMnemonic('A');
			mnArchivo.setBackground(Color.DARK_GRAY);
			mnArchivo.setForeground(Color.WHITE);
			mnArchivo.add(getMntmCargar());
			mnArchivo.add(getMntmGuardar());
			mnArchivo.add(new JSeparator());
			mnArchivo.add(getMntmSalir());
		}
		return mnArchivo;
	}

	private JMenu getMnVer() {
		if (mnVer == null) {
			mnVer = new JMenu(texts.getString("menu_view"));
			mnVer.setMnemonic('V');
			mnVer.setBackground(Color.DARK_GRAY);
			mnVer.setForeground(Color.WHITE);
			mnVer.add(getChckbxmntmDuracin());
			mnVer.add(getChckbxmntmFechaDeVencimiento());
			mnVer.add(getChckbxmntmTareas());
		}
		return mnVer;
	}

	private JMenu getMnHerramientas() {
		if (mnHerramientas == null) {
			mnHerramientas = new JMenu(texts.getString("menu_tools"));
			if (language == Language.ENGLISH)
				mnHerramientas.setMnemonic('T');
			else if (language.equals(Language.SPANISH))
				mnHerramientas.setMnemonic('H');
			mnHerramientas.setBackground(Color.DARK_GRAY);
			mnHerramientas.setForeground(Color.WHITE);
			mnHerramientas.add(getMntmGenerarInstancias());
		}
		return mnHerramientas;
	}

	private JMenuItem getMntmGenerarInstancias() {
		if (mntmGenerarInstancias == null) {
			mntmGenerarInstancias = new JMenuItem(texts.getString("menu_instance_generator"));
			mntmGenerarInstancias.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
			mntmGenerarInstancias.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					InstanceGeneratorDialog id = new InstanceGeneratorDialog();
					id.setLocationRelativeTo(null);
					id.setVisible(true);
				}
			});
		}
		return mntmGenerarInstancias;
	}

	private JMenu getMnConfiguracin() {
		if (mnConfiguracin == null) {
			mnConfiguracin = new JMenu(texts.getString("menu_configuration"));
			mnConfiguracin.setMnemonic('C');
			mnConfiguracin.setBackground(Color.DARK_GRAY);
			mnConfiguracin.setForeground(Color.WHITE);
			mnConfiguracin.add(getMnIdioma());
		}
		return mnConfiguracin;
	}

	private JMenu getMnIdioma() {
		if (mnIdioma == null) {
			mnIdioma = new JMenu(texts.getString("menu_language"));
			mnIdioma.add(getMntmEspanol());
			mnIdioma.add(getMntmIngles());
		}
		return mnIdioma;
	}

	private JMenuItem getMntmEspanol() {
		if (mntmEspanol == null) {
			mntmEspanol = new JMenuItem(texts.getString("menu_spanish"));
			mntmEspanol.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					texts = ResourceBundle.getBundle("rcs/texts", Locale.forLanguageTag("es"));
					language = Language.SPANISH;
					changeLocaleTexts();
					updateMnemonics();
				}
			});
		}
		return mntmEspanol;
	}

	private JMenuItem getMntmIngles() {
		if (mntmIngles == null) {
			mntmIngles = new JMenuItem(texts.getString("menu_english"));
			mntmIngles.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					texts = ResourceBundle.getBundle("rcs/texts", Locale.forLanguageTag("en"));
					language = Language.ENGLISH;
					changeLocaleTexts();
					updateMnemonics();
				}
			});
		}
		return mntmIngles;
	}

	/**
	 * Método auxiliar que recarga los textos de todas la aplicación tras producirse
	 * un cambio en el idioma
	 */
	private void changeLocaleTexts() {
		if (td != null) {
			// TODO actualizar textos diálogo de tareas
		}
		if (pd != null) {
			// TODO actualizar textos diálogo de duraciones
		}
		if (dd != null) {
			// TODO actualizar textos diálogo de duedates
		}
		// Menu
		frame.setTitle(texts.getString("menu_title"));
		mnArchivo.setText(texts.getString("menu_file"));
		mnVer.setText(texts.getString("menu_view"));
		mnHerramientas.setText(texts.getString("menu_tools"));
		mntmGenerarInstancias.setText(texts.getString("menu_instance_generator"));
		mnConfiguracin.setText(texts.getString("menu_configuration"));
		mnIdioma.setText(texts.getString("menu_language"));
		mntmEspanol.setText(texts.getString("menu_spanish"));
		mntmIngles.setText(texts.getString("menu_english"));
		mnAyuda.setText(texts.getString("menu_help"));
		mntmManual.setText(texts.getString("menu_user_manual"));
		mntmInformacion.setText(texts.getString("menu_information"));
		mntmCargar.setText(texts.getString("menu_load"));
		mntmGuardar.setText(texts.getString("menu_save"));
		mntmSalir.setText(texts.getString("menu_exit"));
		chckbxmntmDuracin.setText(texts.getString("menu_duration"));
		chckbxmntmFechaDeVencimiento.setText(texts.getString("menu_due_date"));
		chckbxmntmTareas.setText(texts.getString("menu_tasks"));
		// Botones
		btnCargar.setText(texts.getString("button_load"));
		btnGuardar.setText(texts.getString("button_save"));
		btnSiguiente.setText(texts.getString("button_next"));
		btnFinal.setText(texts.getString("button_final"));
		// Labels
		lblRegla.setText(texts.getString("label_rule"));
		lblG.setText(texts.getString("label_g_parameter"));
		lblSeparacinEntreMarcas.setText(texts.getString("label_separation_between_marks"));
		// TODO charts?
	}

	/**
	 * Método auxiliar que actualiza los mnemónicos de todas la aplicación tras
	 * producirse un cambio en el idioma
	 */
	private void updateMnemonics() {
		if (language == Language.ENGLISH) {
			mnArchivo.setMnemonic('F');
			mnHerramientas.setMnemonic('T');
			mnAyuda.setMnemonic('H');
		} else if (language.equals(Language.SPANISH)) {
			mnArchivo.setMnemonic('A');
			mnHerramientas.setMnemonic('H');
			mnAyuda.setMnemonic('Y');
		}
	}

	private JMenu getMnAyuda() {
		if (mnAyuda == null) {
			mnAyuda = new JMenu(texts.getString("menu_help"));
			if (language == Language.ENGLISH)
				mnAyuda.setMnemonic('H');
			else if (language.equals(Language.SPANISH))
				mnAyuda.setMnemonic('Y');
			mnAyuda.setBackground(Color.DARK_GRAY);
			mnAyuda.setForeground(Color.WHITE);
			mnAyuda.add(getMntmManual());
			mnAyuda.add(getMntmInformacion());
		}
		return mnAyuda;
	}

	private JMenuItem getMntmManual() {
		if (mntmManual == null) {
			mntmManual = new JMenuItem(texts.getString("menu_user_manual"));
			mntmManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
			mntmManual.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO ayuda html
				}
			});
		}
		return mntmManual;
	}

	private JMenuItem getMntmInformacion() {
		if (mntmInformacion == null) {
			mntmInformacion = new JMenuItem(texts.getString("menu_information"));
			mntmInformacion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
			mntmInformacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO diálogo con info
				}
			});
		}
		return mntmInformacion;
	}

	private JMenuItem getMntmCargar() {
		if (mntmCargar == null) {
			mntmCargar = new JMenuItem(texts.getString("menu_load"));
			mntmCargar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
			mntmCargar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					load();
				}
			});
		}
		return mntmCargar;
	}

	private JMenuItem getMntmGuardar() {
		if (mntmGuardar == null) {
			mntmGuardar = new JMenuItem(texts.getString("menu_save"));
			mntmGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
			mntmGuardar.setEnabled(false);
			mntmGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					int returnVal = jfc.showOpenDialog(frame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = jfc.getSelectedFile();
						manager.writeChart(file.getAbsolutePath() + ".pdf");
					}
				}
			});
		}
		return mntmGuardar;
	}

	private JMenuItem getMntmSalir() {
		if (mntmSalir == null) {
			mntmSalir = new JMenuItem(texts.getString("menu_exit"));
			mntmSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
			mntmSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
			});

		}
		return mntmSalir;
	}

	private JCheckBoxMenuItem getChckbxmntmDuracin() {
		if (chckbxmntmDuracin == null) {
			chckbxmntmDuracin = new JCheckBoxMenuItem(texts.getString("menu_duration"));
			chckbxmntmDuracin.setEnabled(false);
			chckbxmntmDuracin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
			chckbxmntmDuracin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!displayedDurations) {
						createDurationsChart();
						displayedDurations = true;
					} else {
						pd.dispose();
						displayedDurations = false;
					}
				}
			});
		}
		return chckbxmntmDuracin;

	}

	/**
	 * Método auxiliar que crea el diálogo para mostrar la distibución de duraciones
	 */
	private void createDurationsChart() {
		ChartPanel cp = new ChartPanel(manager.createDurationsChart(manager.getP()));
		pd = new DurationsDialog(texts);
		pd.getContentPane().add(cp, BorderLayout.CENTER);
		pd.setVisible(true);
		pd.setLocationRelativeTo(null);
		pd.revalidate();
	}

	/**
	 * Método auxiliar que crea el diálogo para mostrar la distribución de due dates
	 */
	private void createDueDatesChart() {
		ChartPanel cp = new ChartPanel(manager.createDueDatesChart(manager.getD()));
		dd = new DueDatesDialog(texts);
		dd.getContentPane().add(cp, BorderLayout.CENTER);
		dd.setVisible(true);
		dd.setLocationRelativeTo(null);
		dd.revalidate();
	}

	private JCheckBoxMenuItem getChckbxmntmFechaDeVencimiento() {
		if (chckbxmntmFechaDeVencimiento == null) {
			chckbxmntmFechaDeVencimiento = new JCheckBoxMenuItem(texts.getString("menu_due_date"));
			chckbxmntmFechaDeVencimiento.setEnabled(false);
			chckbxmntmFechaDeVencimiento.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
			chckbxmntmFechaDeVencimiento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!displayedDuedates) {
						createDueDatesChart();
						displayedDuedates = true;
					} else {
						dd.dispose();
						displayedDuedates = false;
					}
				}
			});
		}
		return chckbxmntmFechaDeVencimiento;
	}

	private JCheckBoxMenuItem getChckbxmntmTareas() {
		if (chckbxmntmTareas == null) {
			chckbxmntmTareas = new JCheckBoxMenuItem(texts.getString("menu_tasks"));
			chckbxmntmTareas.setEnabled(false);
			chckbxmntmTareas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
			chckbxmntmTareas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!displayedTasks) {
						createTasksTable();
						displayedTasks = true;
					} else {
						td.dispose();
						displayedTasks = false;
					}
				}
			});
		}
		return chckbxmntmTareas;
	}

	/**
	 * Método auxiliar que crea el diálogo para mostrar la información de la stareas
	 */
	private void createTasksTable() {
		dueDates = manager.getD();
		durations = manager.getP();
		td = new TasksDialog(texts);
		DefaultTableModel model = (DefaultTableModel) td.getTasksTable().getModel();
		model.setRowCount(0);
		for (int t = 0; t < dueDates.length; t++) {
			model = (DefaultTableModel) td.getTasksTable().getModel();
			model.addRow(new Object[] { t, durations[t], dueDates[t], null });
		}

		td.setVisible(true);
		td.setLocationRelativeTo(null);
		td.getTasksTable().revalidate();
	}

	private JComboBox<String> getRulesComboBox() {
		if (rulesComboBox == null) {
			rulesComboBox = new JComboBox<String>();
			rulesComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String rule = (String) getRulesComboBox().getSelectedItem();
					switch (rule) {
					case "ATC":
						getRulesSpinner().setEnabled(true);
						break;
					case "EDD":
						getRulesSpinner().setEnabled(false);
						break;
					case "SPT":
						getRulesSpinner().setEnabled(false);
						break;
					default:
						break;
					}
				}
			});
			rulesComboBox.setEnabled(false);
			rulesComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "EDD", "SPT", "ATC" }));
		}
		return rulesComboBox;
	}

	private JLabel getLblRegla() {
		if (lblRegla == null) {
			lblRegla = new JLabel(texts.getString("label_rule"));
			lblRegla.setHorizontalAlignment(SwingConstants.CENTER);
			lblRegla.setForeground(Color.WHITE);
		}
		return lblRegla;
	}

	private JLabel getLblG() {
		if (lblG == null) {
			lblG = new JLabel(texts.getString("label_g_parameter"));
			lblG.setHorizontalAlignment(SwingConstants.CENTER);
			lblG.setForeground(Color.WHITE);
		}
		return lblG;
	}

	private JLabel getLblSeparacinEntreMarcas() {
		if (lblSeparacinEntreMarcas == null) {
			lblSeparacinEntreMarcas = new JLabel(texts.getString("label_separation_between_marks"));
			lblSeparacinEntreMarcas.setHorizontalAlignment(SwingConstants.CENTER);
			lblSeparacinEntreMarcas.setForeground(Color.WHITE);
		}
		return lblSeparacinEntreMarcas;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.DARK_GRAY);
			buttonPanel.setLayout(new GridLayout(0, 2, 5, 0));
			buttonPanel.add(getBtnCargar());
			buttonPanel.add(getBtnGuardar());
		}
		return buttonPanel;
	}

	private JPanel getRulesPanel() {
		if (rulesPanel == null) {
			rulesPanel = new JPanel();
			rulesPanel.setBackground(Color.DARK_GRAY);
			rulesPanel.setLayout(new GridLayout(0, 7, 5, 0));
			rulesPanel.add(getLblRegla());
			rulesPanel.add(getRulesComboBox());
			rulesPanel.add(getLblG());
			rulesPanel.add(getRulesSpinner());
			rulesPanel.add(getBtnSiguiente());
			rulesPanel.add(getBtnFinal());
		}
		return rulesPanel;
	}

	private JPanel getAxisPanel() {
		if (axisPanel == null) {
			axisPanel = new JPanel();
			axisPanel.setBackground(Color.DARK_GRAY);
			axisPanel.setLayout(new GridLayout(0, 2, 5, 0));
			axisPanel.add(getLblSeparacinEntreMarcas());
			axisPanel.add(getAxisSpinner());
		}
		return axisPanel;
	}

	private JSpinner getRulesSpinner() {
		if (rulesSpinner == null) {
			rulesSpinner = new JSpinner();
			rulesSpinner.setEnabled(false);
			rulesSpinner.setModel(new SpinnerNumberModel(0.25, 0.01, 1.0, 0.01));
		}
		return rulesSpinner;
	}

	private JSpinner getAxisSpinner() {
		if (axisSpinner == null) {
			axisSpinner = new JSpinner();
			axisSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (cp != null) {
						XYPlot plot = manager.getChart().getXYPlot();
						NumberAxis domain = (NumberAxis) plot.getDomainAxis();
						domain.setTickUnit(new NumberTickUnit((int) getAxisSpinner().getValue()));
						frame.revalidate();
					}

				}
			});
			axisSpinner.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		}
		return axisSpinner;
	}

	private JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new JButton(texts.getString("button_next"));
			btnSiguiente.setEnabled(false);
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO
				}
			});
		}
		return btnSiguiente;
	}

	private JButton getBtnFinal() {
		if (btnFinal == null) {
			btnFinal = new JButton(texts.getString("button_final"));
			btnFinal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goToEnd();
					btnFinal.setEnabled(false); // TODO así no puedes recargar con otra regla
					btnSiguiente.setEnabled(false);
				}
			});
			btnFinal.setEnabled(false);
		}
		return btnFinal;
	}

	private void goToEnd() {
		frame.remove(cp);
		Rule rule = Rule.valueOf((String) getRulesComboBox().getSelectedItem());
		manager.createMainChart(rule, (double) getRulesSpinner().getValue(), (int) getAxisSpinner().getValue());
		cp = new ChartPanel(manager.getChart());
		frame.getContentPane().add(cp);
		frame.revalidate();
	}
}
