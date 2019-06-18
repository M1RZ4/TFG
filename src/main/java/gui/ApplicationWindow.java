package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

import dominio.Gestor;
import dominio.Instancia;
import dominio.Planificacion;
import main.java.gui.dialogs.DueDatesDialog;
import main.java.gui.dialogs.DurationsDialog;
import main.java.gui.dialogs.ExperimentalAnalysisDialog;
import main.java.gui.dialogs.InstanceGeneratorDialog;
import main.java.gui.dialogs.TasksDialog;
import main.java.gui.menu.BackgroundMenuBar;
import main.java.logic.InstanceManager;
import main.java.logic.LanguageManager;
import main.java.logic.enums.Rule;
import main.java.logic.instances.ScheduledInstance;

/**
 * Clase ApplicationWindow que representa la ventana principal de la aplicación.
 * Se comunica con {@link main.java.logic.InstanceManager InstanceManager} para
 * dar respuesta a las principales funcionalidades de la aplicación
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class ApplicationWindow {

	private ChartPanel cp;

	private TasksDialog td;
	private boolean displayedTasks = false;
	private DurationsDialog pd;
	private boolean displayedDurations = false;
	private DueDatesDialog dd;
	private boolean displayedDuedates = false;

	private int step;

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
	private JMenuItem mntmEstudioExperimental;
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
	private JButton btnAnterior;

	private InstanceManager manager = new InstanceManager();
	private JLabel lblTardiness;
	private JTextField textFieldTardiness;

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
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
		JComponent.setDefaultLocale(Locale.forLanguageTag("en"));
		frame.setTitle(manager.getText("menu_title"));
		frame.setBounds(100, 100, 1300, 650);
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
			gbl_configPanel.rowHeights = new int[] { 23, 0, 0 };
			gbl_configPanel.columnWeights = new double[] { 1.0, 0, 0.5, Double.MIN_VALUE };
			gbl_configPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			configPanel.setLayout(gbl_configPanel);
			GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
			gbc_buttonPanel.insets = new Insets(0, 0, 5, 5);
			gbc_buttonPanel.fill = GridBagConstraints.BOTH;
			gbc_buttonPanel.gridx = 0;
			gbc_buttonPanel.gridy = 0;
			configPanel.add(getButtonPanel(), gbc_buttonPanel);
			GridBagConstraints gbc_rulesPanel = new GridBagConstraints();
			gbc_rulesPanel.insets = new Insets(0, 0, 5, 5);
			gbc_rulesPanel.fill = GridBagConstraints.BOTH;
			gbc_rulesPanel.gridx = 1;
			gbc_rulesPanel.gridy = 0;
			configPanel.add(getRulesPanel(), gbc_rulesPanel);
			GridBagConstraints gbc_axisPanel = new GridBagConstraints();
			gbc_axisPanel.insets = new Insets(0, 0, 5, 0);
			gbc_axisPanel.fill = GridBagConstraints.BOTH;
			gbc_axisPanel.gridx = 2;
			gbc_axisPanel.gridy = 0;
			configPanel.add(getAxisPanel(), gbc_axisPanel);
		}
		return configPanel;
	}

	private JButton getBtnCargar() {
		if (btnCargar == null) {
			btnCargar = new JButton(manager.getText("button_load"));
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

			try {
				manager.readInstance(file.getAbsolutePath());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage(), manager.getText("error_title"),
						JOptionPane.ERROR_MESSAGE);
				return;
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(frame, manager.getText("error_not_found"), manager.getText("error_title"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

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
		}
		step = 0;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton(manager.getText("button_save"));
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
			mnArchivo = new JMenu(manager.getText("menu_file"));
			mnArchivo.setMnemonic(manager.getMnemonic("mnemonic_file"));
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
			mnVer = new JMenu(manager.getText("menu_view"));
			mnVer.setMnemonic(manager.getMnemonic("mnemonic_view"));
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
			mnHerramientas = new JMenu(manager.getText("menu_tools"));
			mnHerramientas.setMnemonic(manager.getMnemonic("mnemonic_tools"));
			mnHerramientas.setBackground(Color.DARK_GRAY);
			mnHerramientas.setForeground(Color.WHITE);
			mnHerramientas.add(getMntmGenerarInstancias());
			mnHerramientas.add(getMntmEstudioExperimental());
		}
		return mnHerramientas;
	}

	private JMenuItem getMntmGenerarInstancias() {
		if (mntmGenerarInstancias == null) {
			mntmGenerarInstancias = new JMenuItem(manager.getText("menu_instance_generator"));
			mntmGenerarInstancias.setMnemonic(manager.getMnemonic("mnemonic_instance_generator"));
			mntmGenerarInstancias.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
			mntmGenerarInstancias.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createInstanceGeneratorDialog();
				}
			});
		}
		return mntmGenerarInstancias;
	}

	/**
	 * Método auxiliar para desplegar la herramienta de generación de instancias
	 */
	private void createInstanceGeneratorDialog() {
		InstanceGeneratorDialog id = new InstanceGeneratorDialog(this);
		id.setLocationRelativeTo(null);
		id.setVisible(true);
	}

	private JMenuItem getMntmEstudioExperimental() {
		if (mntmEstudioExperimental == null) {
			mntmEstudioExperimental = new JMenuItem(manager.getText("menu_experimental_study"));
			mntmEstudioExperimental.setMnemonic(manager.getMnemonic("mnemonic_experimental_study"));
			mntmEstudioExperimental.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
			mntmEstudioExperimental.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createExperimentalAnalysisDialog();
				}
			});
		}
		return mntmEstudioExperimental;
	}

	/**
	 * Método auxiliar para desplegar la herramienta de estudio experimental
	 */
	private void createExperimentalAnalysisDialog() {
		ExperimentalAnalysisDialog ed = new ExperimentalAnalysisDialog(this);
		ed.setLocationRelativeTo(null);
		ed.setVisible(true);
	}

	private JMenu getMnConfiguracin() {
		if (mnConfiguracin == null) {
			mnConfiguracin = new JMenu(manager.getText("menu_configuration"));
			mnConfiguracin.setMnemonic(manager.getMnemonic("mnemonic_configuration"));
			mnConfiguracin.setBackground(Color.DARK_GRAY);
			mnConfiguracin.setForeground(Color.WHITE);
			mnConfiguracin.add(getMnIdioma());
		}
		return mnConfiguracin;
	}

	private JMenu getMnIdioma() {
		if (mnIdioma == null) {
			mnIdioma = new JMenu(manager.getText("menu_language"));
			mnIdioma.setMnemonic(manager.getMnemonic("mnemonic_language"));
			mnIdioma.add(getMntmEspanol());
			mnIdioma.add(getMntmIngles());
		}
		return mnIdioma;
	}

	private JMenuItem getMntmEspanol() {
		if (mntmEspanol == null) {
			mntmEspanol = new JMenuItem(manager.getText("menu_spanish"));
			mntmEspanol.setMnemonic(manager.getMnemonic("mnemonic_spanish"));
			mntmEspanol.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LanguageManager.getInstance()
							.setTexts(ResourceBundle.getBundle("texts", Locale.forLanguageTag("es")));
					changeLocaleTexts();
					updateMnemonics();
					JComponent.setDefaultLocale(Locale.forLanguageTag("es"));
				}
			});
		}
		return mntmEspanol;
	}

	private JMenuItem getMntmIngles() {
		if (mntmIngles == null) {
			mntmIngles = new JMenuItem(manager.getText("menu_english"));
			mntmIngles.setMnemonic(manager.getMnemonic("mnemonic_english"));
			mntmIngles.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LanguageManager.getInstance()
							.setTexts(ResourceBundle.getBundle("texts", Locale.forLanguageTag("en")));
					changeLocaleTexts();
					updateMnemonics();
					JComponent.setDefaultLocale(Locale.forLanguageTag("en"));
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
		if (td != null && displayedTasks) {
			Point location = td.getLocationOnScreen();
			td.dispose();
			td = null;
			createTasksTable();
			showTaskTable(location);
		}
		if (pd != null && displayedDurations) {
			Point location = pd.getLocation();
			pd.dispose();
			pd = null;
			createDurationsChart();
			showDurationsDialog(location);
		}
		if (dd != null && displayedDuedates) {
			Point location = dd.getLocation();
			dd.dispose();
			dd = null;
			createDueDatesChart();
			showDueDatesDialog(location);
		}
		// Menu
		frame.setTitle(manager.getText("menu_title"));
		mnArchivo.setText(manager.getText("menu_file"));
		mnVer.setText(manager.getText("menu_view"));
		mnHerramientas.setText(manager.getText("menu_tools"));
		mntmGenerarInstancias.setText(manager.getText("menu_instance_generator"));
		mntmEstudioExperimental.setText(manager.getText("menu_experimental_study"));
		mnConfiguracin.setText(manager.getText("menu_configuration"));
		mnIdioma.setText(manager.getText("menu_language"));
		mntmEspanol.setText(manager.getText("menu_spanish"));
		mntmIngles.setText(manager.getText("menu_english"));
		mnAyuda.setText(manager.getText("menu_help"));
		mntmManual.setText(manager.getText("menu_user_manual"));
		mntmInformacion.setText(manager.getText("menu_information"));
		mntmCargar.setText(manager.getText("menu_load"));
		mntmGuardar.setText(manager.getText("menu_save"));
		mntmSalir.setText(manager.getText("menu_exit"));
		chckbxmntmDuracin.setText(manager.getText("menu_duration"));
		chckbxmntmFechaDeVencimiento.setText(manager.getText("menu_due_date"));
		chckbxmntmTareas.setText(manager.getText("menu_tasks"));
		// Botones
		btnCargar.setText(manager.getText("button_load"));
		btnGuardar.setText(manager.getText("button_save"));
		btnSiguiente.setText(manager.getText("button_next"));
		btnFinal.setText(manager.getText("button_final"));
		btnAnterior.setText(manager.getText("button_previous"));
		// Labels
		lblRegla.setText(manager.getText("label_rule"));
		lblG.setText(manager.getText("label_g_parameter"));
		lblSeparacinEntreMarcas.setText(manager.getText("label_separation_between_marks"));
		lblTardiness.setText(manager.getText("label_tardiness"));
		// Gráfico
		if (manager.getChart() != null) {
			if (step > 0)
				setMainChart();
			else {
				frame.remove(cp);
				manager.loadMainChart((int) getAxisSpinner().getValue());
				cp = new ChartPanel(manager.getChart());
				frame.getContentPane().add(cp);
				frame.revalidate();
			}
		}
	}

	/**
	 * Método auxiliar que actualiza los mnemónicos de todas la aplicación tras
	 * producirse un cambio en el idioma
	 */
	private void updateMnemonics() {
		// Archivo
		mnArchivo.setMnemonic(manager.getMnemonic("mnemonic_file"));
		mntmCargar.setMnemonic(manager.getMnemonic("mnemonic_load"));
		mntmGuardar.setMnemonic(manager.getMnemonic("mnemonic_save"));
		mntmSalir.setMnemonic(manager.getMnemonic("mnemonic_exit"));
		// Ver
		chckbxmntmFechaDeVencimiento.setMnemonic(manager.getMnemonic("mnemonic_due_date"));
		chckbxmntmTareas.setMnemonic(manager.getMnemonic("mnemonic_tasks"));
		// Herramientas
		mnHerramientas.setMnemonic(manager.getMnemonic("mnemonic_tools"));
		mntmGenerarInstancias.setMnemonic(manager.getMnemonic("mnemonic_instance_generator"));
		// Configuración
		mnIdioma.setMnemonic(manager.getMnemonic("mnemonic_language"));
		mntmEspanol.setMnemonic(manager.getMnemonic("mnemonic_spanish"));
		mntmIngles.setMnemonic(manager.getMnemonic("mnemonic_english"));
		// Ayuda
		mnAyuda.setMnemonic(manager.getMnemonic("mnemonic_help"));
		mntmManual.setMnemonic(manager.getMnemonic("mnemonic_user_manual"));
	}

	private JMenu getMnAyuda() {
		if (mnAyuda == null) {
			mnAyuda = new JMenu(manager.getText("menu_help"));
			mnAyuda.setMnemonic(manager.getMnemonic("mnemonic_help"));
			mnAyuda.setBackground(Color.DARK_GRAY);
			mnAyuda.setForeground(Color.WHITE);
			mnAyuda.add(getMntmManual());
			mnAyuda.add(getMntmInformacion());
		}
		return mnAyuda;
	}

	private JMenuItem getMntmManual() {
		if (mntmManual == null) {
			mntmManual = new JMenuItem(manager.getText("menu_user_manual"));
			mntmManual.setMnemonic(manager.getMnemonic("mnemonic_user_manual"));
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
			mntmInformacion = new JMenuItem(manager.getText("menu_information"));
			mntmInformacion.setMnemonic('I');
			mntmInformacion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
			mntmInformacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showOptionDialog(frame,
							manager.getText("info_tfg") + "\n\n" + manager.getText("info_author")
									+ " Mirza Ojeda Veira\n" + manager.getText("info_tutors")
									+ " Ramiro José Varela Arias & Francisco Javier Gil Gala\n\n"
									+ manager.getText("info_university"),
							manager.getText("info_title"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
							null, new Object[] {}, null);
				}
			});
		}
		return mntmInformacion;
	}

	private JMenuItem getMntmCargar() {
		if (mntmCargar == null) {
			mntmCargar = new JMenuItem(manager.getText("menu_load"));
			mntmCargar.setMnemonic(manager.getMnemonic("mnemonic_load"));
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
			mntmGuardar = new JMenuItem(manager.getText("menu_save"));
			mntmGuardar.setMnemonic(manager.getMnemonic("mnemonic_save"));
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
			mntmSalir = new JMenuItem(manager.getText("menu_exit"));
			mntmSalir.setMnemonic(manager.getMnemonic("mnemonic_exit"));
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
			chckbxmntmDuracin = new JCheckBoxMenuItem(manager.getText("menu_duration"));
			chckbxmntmDuracin.setMnemonic(manager.getMnemonic("mnemonic_duration"));
			chckbxmntmDuracin.setEnabled(false);
			chckbxmntmDuracin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
			chckbxmntmDuracin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!displayedDurations) {
						createDurationsChart();
						showDurationsDialog(null);
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
		int x = manager.getP().length - step;
		ScheduledInstance i = new ScheduledInstance(step, manager.getInstance(),
				Rule.valueOf((String) getRulesComboBox().getSelectedItem()), (double) rulesSpinner.getValue());
		int[] ids = i.getIds();
		int[] reversed = new int[ids.length];
		for (int j = 0; j < ids.length; j++)
			reversed[j] = ids[ids.length - 1 - j];
		JFreeChart chart = manager.createDurationsChart(Arrays.copyOfRange(manager.getP(), 0, x), reversed);
		ChartPanel cp = new ChartPanel(chart);
		pd = new DurationsDialog();
		pd.getContentPane().add(cp, BorderLayout.CENTER);
	}

	/**
	 * Método auxiliar que crea el diálogo para mostrar la distribución de due dates
	 */
	private void createDueDatesChart() {
		int x = manager.getD().length - step;
		ScheduledInstance i = new ScheduledInstance(step, manager.getInstance(),
				Rule.valueOf((String) getRulesComboBox().getSelectedItem()), (double) rulesSpinner.getValue());
		int[] ids = i.getIds();
		int[] reversed = new int[ids.length];
		for (int j = 0; j < ids.length; j++)
			reversed[j] = ids[ids.length - 1 - j];
		JFreeChart chart = manager.createDueDatesChart(Arrays.copyOfRange(manager.getD(), 0, x), reversed);
		ChartPanel cp = new ChartPanel(chart);
		dd = new DueDatesDialog();
		dd.getContentPane().add(cp, BorderLayout.CENTER);
	}

	private JCheckBoxMenuItem getChckbxmntmFechaDeVencimiento() {
		if (chckbxmntmFechaDeVencimiento == null) {
			chckbxmntmFechaDeVencimiento = new JCheckBoxMenuItem(manager.getText("menu_due_date"));
			chckbxmntmFechaDeVencimiento.setMnemonic(manager.getMnemonic("mnemonic_due_date"));
			chckbxmntmFechaDeVencimiento.setEnabled(false);
			chckbxmntmFechaDeVencimiento.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
			chckbxmntmFechaDeVencimiento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!displayedDuedates) {
						createDueDatesChart();
						showDueDatesDialog(null);
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
			chckbxmntmTareas = new JCheckBoxMenuItem(manager.getText("menu_tasks"));
			chckbxmntmTareas.setMnemonic(manager.getMnemonic("mnemonic_tasks"));
			chckbxmntmTareas.setEnabled(false);
			chckbxmntmTareas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
			chckbxmntmTareas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!displayedTasks) {
						createTasksTable();
						showTaskTable(null);
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
		ScheduledInstance i = new ScheduledInstance(step, manager.getInstance(),
				Rule.valueOf((String) getRulesComboBox().getSelectedItem()), (double) rulesSpinner.getValue());

		int[] startTimes = i.getStartTimes();
		double[] dueDates = manager.getD();
		double[] durations = manager.getP();
		int[] ids = i.getIds();

		td = new TasksDialog(this, (int) getAxisSpinner().getValue());
		DefaultTableModel model = (DefaultTableModel) td.getTasksTable().getModel();
		model.setRowCount(0);
		for (int t = 0; t < dueDates.length; t++) {
			model = (DefaultTableModel) td.getTasksTable().getModel();
			if (t >= startTimes.length)
				model.addRow(new Object[] { ids[t], durations[ids[t]], dueDates[ids[t]], null });
			else
				model.addRow(new Object[] { ids[t], i.getP()[t], i.getD()[t], startTimes[t] });
		}
	}

	/**
	 * Método auxiliar mostrar el diálogo de tareas
	 * 
	 * @param location
	 *            localización del diálogo
	 */
	private void showTaskTable(Point location) {
		td.setVisible(true);
		if (location == null)
			td.setLocationRelativeTo(null);
		else
			td.setLocation(location);
		td.getTasksTable().revalidate();
		td.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				displayedTasks = false;
				chckbxmntmTareas.setSelected(false);
			}
		});
		if (btnAnterior.isEnabled())
			td.getBtnPlanificar().setEnabled(false);
	}

	/**
	 * Método auxiliar mostrar el diálogo de due dates
	 * 
	 * @param location
	 *            localización del diálogo
	 */
	private void showDueDatesDialog(Point location) {
		dd.setVisible(true);
		if (location == null)
			dd.setLocationRelativeTo(null);
		else
			dd.setLocation(location);
		dd.revalidate();
		dd.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				displayedDuedates = false;
				chckbxmntmFechaDeVencimiento.setSelected(false);
			}
		});
	}

	/**
	 * Método auxiliar mostrar el diálogo de duraciones
	 * 
	 * @param location
	 *            localización del diálogo
	 */
	private void showDurationsDialog(Point location) {
		pd.setVisible(true);
		if (location == null)
			pd.setLocationRelativeTo(null);
		else
			pd.setLocation(location);
		pd.revalidate();
		pd.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				displayedDurations = false;
				chckbxmntmDuracin.setSelected(false);
			}
		});
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
			lblRegla = new JLabel(manager.getText("label_rule"));
			lblRegla.setHorizontalAlignment(SwingConstants.CENTER);
			lblRegla.setForeground(Color.WHITE);
		}
		return lblRegla;
	}

	private JLabel getLblG() {
		if (lblG == null) {
			lblG = new JLabel(manager.getText("label_g_parameter"));
			lblG.setHorizontalAlignment(SwingConstants.CENTER);
			lblG.setForeground(Color.WHITE);
		}
		return lblG;
	}

	private JLabel getLblSeparacinEntreMarcas() {
		if (lblSeparacinEntreMarcas == null) {
			lblSeparacinEntreMarcas = new JLabel(manager.getText("label_separation_between_marks"));
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
			rulesPanel.setLayout(new GridLayout(0, 9, 5, 0));
			rulesPanel.add(getLblRegla());
			rulesPanel.add(getRulesComboBox());
			rulesPanel.add(getLblG());
			rulesPanel.add(getRulesSpinner());
			rulesPanel.add(getBtnAnterior());
			rulesPanel.add(getBtnSiguiente());
			rulesPanel.add(getBtnFinal());
			rulesPanel.add(getLblTardiness());
			rulesPanel.add(getTextFieldTardiness());
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
			btnSiguiente = new JButton(manager.getText("button_next"));
			btnSiguiente.setEnabled(false);
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (step < manager.getD().length)
						step++;
					if (step >= manager.getD().length) {
						btnSiguiente.setEnabled(false);
					}
					if (displayedTasks) {
						updateTasks();
					}
					if (displayedDurations) {
						updateDurations();
					}
					if (displayedDuedates) {
						updateDueDates();
					}
					setMainChart();
					if (!btnAnterior.isEnabled())
						btnAnterior.setEnabled(true);
					calculateTardiness();
					if (td != null)
						td.getBtnPlanificar().setEnabled(false);
				}
			});
		}
		return btnSiguiente;
	}

	private JButton getBtnAnterior() {
		if (btnAnterior == null) {
			btnAnterior = new JButton(manager.getText("button_previous"));
			btnAnterior.setEnabled(false);
			btnAnterior.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean flag = true;
					if (step > 0) {
						step--;
						flag = false;
					}
					if (step <= 0) {
						btnAnterior.setEnabled(false);
						frame.remove(cp);
						manager.loadMainChart((int) getAxisSpinner().getValue());
						cp = new ChartPanel(manager.getChart());
						frame.getContentPane().add(cp);
						frame.revalidate();
						flag = true;
					} else {
						setMainChart();
					}
					if (displayedTasks) {
						updateTasks();
						td.getBtnPlanificar().setEnabled(flag);
					}
					if (displayedDurations) {
						updateDurations();
					}
					if (displayedDuedates) {
						updateDueDates();
					}
					if (!btnSiguiente.isEnabled())
						btnSiguiente.setEnabled(true);
					calculateTardiness();
				}
			});
		}
		return btnAnterior;
	}

	private JButton getBtnFinal() {
		if (btnFinal == null) {
			btnFinal = new JButton(manager.getText("button_final"));
			btnFinal.setEnabled(false);
			btnFinal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					step = manager.getD().length;
					if (displayedTasks) {
						updateTasks();
					}
					if (displayedDurations) {
						Point location = pd.getLocationOnScreen();
						pd.dispose();
						createDurationsChart();
						showDurationsDialog(location);
					}
					if (displayedDuedates) {
						Point location = dd.getLocationOnScreen();
						dd.dispose();
						createDueDatesChart();
						showDueDatesDialog(location);
					}
					setMainChart();
					btnSiguiente.setEnabled(false);
					if (!btnAnterior.isEnabled())
						btnAnterior.setEnabled(true);
					calculateTardiness();
					if (td != null)
						td.getBtnPlanificar().setEnabled(false);
				}
			});
		}
		return btnFinal;
	}

	/**
	 * Método auxiliar que actualiza el diálogo con la tabla de tareas al hacer la
	 * planificación paso a paso
	 */
	private void updateTasks() {
		Point location = td.getLocationOnScreen();
		td.dispose();
		createTasksTable();
		showTaskTable(location);
	}

	/**
	 * Método auxiliar que actualiza el diálogo con el gráfico de duraciones al
	 * hacer la planificación paso a paso
	 */
	public void updateDurations() {
		Point location = pd.getLocationOnScreen();
		pd.dispose();
		createDurationsChart();
		showDurationsDialog(location);
	}

	/**
	 * Método auxiliar que actualiza el diálogo con el gráfico de due dates al hacer
	 * la planificación paso a paso
	 */
	public void updateDueDates() {
		Point location = dd.getLocationOnScreen();
		dd.dispose();
		createDueDatesChart();
		showDueDatesDialog(location);
	}

	/**
	 * Método auxiliar que modifica el gráfico principal en función de los botones
	 * de anterior, siguiente y final
	 */
	private void setMainChart() {
		frame.remove(cp);
		Rule rule = Rule.valueOf((String) getRulesComboBox().getSelectedItem());
		manager.setMainChart(step, rule, (double) getRulesSpinner().getValue(), (int) getAxisSpinner().getValue());
		cp = new ChartPanel(manager.getChart());
		frame.getContentPane().add(cp);
		frame.revalidate();
	}

	private JLabel getLblTardiness() {
		if (lblTardiness == null) {
			lblTardiness = new JLabel(manager.getText("label_tardiness"));
			lblTardiness.setForeground(Color.WHITE);
			lblTardiness.setHorizontalAlignment(JLabel.CENTER);
		}
		return lblTardiness;
	}

	public JTextField getTextFieldTardiness() {
		if (textFieldTardiness == null) {
			textFieldTardiness = new JTextField();
			textFieldTardiness.setText("0");
			textFieldTardiness.setEditable(false);
			textFieldTardiness.setColumns(10);
			textFieldTardiness.setHorizontalAlignment(JTextField.CENTER);
		}
		return textFieldTardiness;
	}

	private void calculateTardiness() {
		double tardiness = 0;
		Planificacion p = null;
		Instancia i = null;
		if (step < manager.getD().length)
			i = new ScheduledInstance(step, manager.getInstance(),
					Rule.valueOf((String) getRulesComboBox().getSelectedItem()), (double) rulesSpinner.getValue());
		else
			i = manager.getInstance();
		double g = (double) rulesSpinner.getValue();
		switch (Rule.valueOf((String) getRulesComboBox().getSelectedItem())) {
		case ATC:
			p = Gestor.planificaATC(g, i);
			break;
		case EDD:
			p = Gestor.planificaEDD(i);
			break;
		case SPT:
			p = Gestor.planificaSPT(i);
			break;
		default:
			break;
		}
		int[] startTimes = p.getSti();
		double[] dueDates = manager.getD();
		double[] durations = manager.getP();

		for (int j = 0; j < startTimes.length; j++)
			tardiness += Math.max(0, startTimes[j] + durations[j] - dueDates[j]);
		textFieldTardiness.setText(String.valueOf(tardiness));
	}

	public static ResourceBundle getTexts() {
		return LanguageManager.getInstance().getTexts();
	}

	public InstanceManager getManager() {
		return manager;
	}

	public ChartPanel getCp() {
		return cp;
	}

	public void setCp(ChartPanel cp) {
		this.cp = cp;
	}

	public JFrame getFrame() {
		return frame;
	}

	public boolean isDisplayedDurations() {
		return displayedDurations;
	}

	public boolean isDisplayedDuedates() {
		return displayedDuedates;
	}

}
