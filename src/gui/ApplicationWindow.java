package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

import dominio.Gestor;
import dominio.Instancia;
import dominio.Intervalo;
import dominio.Planificacion;
import gui.enums.Language;
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

	private JFrame frmVisuzalizadorYGenerador;
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
					window.frmVisuzalizadorYGenerador.setVisible(true);
					window.frmVisuzalizadorYGenerador.setBackground(Color.WHITE);
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
		frmVisuzalizadorYGenerador = new JFrame();
		frmVisuzalizadorYGenerador.getContentPane().setBackground(Color.WHITE);
		BorderLayout borderLayout = (BorderLayout) frmVisuzalizadorYGenerador.getContentPane().getLayout();
		borderLayout.setVgap(10);
		borderLayout.setHgap(10);
		texts = ResourceBundle.getBundle("rcs/texts", new Locale("en"));
		language = Language.ENGLISH;
		frmVisuzalizadorYGenerador.setTitle(texts.getString("menu_title"));
		frmVisuzalizadorYGenerador.setBounds(100, 100, 1100, 600);
		frmVisuzalizadorYGenerador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVisuzalizadorYGenerador.setLocationRelativeTo(null);
		frmVisuzalizadorYGenerador.getContentPane().add(getConfigPanel(), BorderLayout.SOUTH);
		frmVisuzalizadorYGenerador.getContentPane().add(getMenuBar(), BorderLayout.NORTH);
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
			frmVisuzalizadorYGenerador.remove(cp);

		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(frmVisuzalizadorYGenerador);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();

			manager.readInstance(file.getAbsolutePath());
			manager.initializeChartManager(texts);
			manager.loadMainChart((int) getAxisSpinner().getValue());

			cp = new ChartPanel(manager.getChart());
			frmVisuzalizadorYGenerador.getContentPane().add(cp, BorderLayout.CENTER);
			frmVisuzalizadorYGenerador.revalidate();

			getRulesComboBox().setEnabled(true);

			getBtnSiguiente().setEnabled(true);
			getBtnFinal().setEnabled(true);

			getMntmGuardar().setEnabled(true);

			getChckbxmntmDuracin().setEnabled(true);
			getChckbxmntmFechaDeVencimiento().setEnabled(true);
			getChckbxmntmTareas().setEnabled(true);

			getBtnGuardar().setEnabled(true);

			if (getChckbxmntmTareas().isSelected()) {
				createTasksTable();
				displayedTasks = true;
			} else if (td != null) {
				td.dispose();
				displayedTasks = false;
			}
			if (getChckbxmntmDuracin().isSelected()) {
				createDurationsChart();
				displayedDurations = true;
			} else if (pd != null) {
				pd.dispose();
				displayedDurations = false;
			}
			if (getChckbxmntmFechaDeVencimiento().isSelected()) {
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
					int returnVal = jfc.showOpenDialog(frmVisuzalizadorYGenerador);
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
		frmVisuzalizadorYGenerador.setTitle(texts.getString("menu_title"));
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
					int returnVal = jfc.showOpenDialog(frmVisuzalizadorYGenerador);
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
					frmVisuzalizadorYGenerador
							.dispatchEvent(new WindowEvent(frmVisuzalizadorYGenerador, WindowEvent.WINDOW_CLOSING));
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

	private void createDurationsChart() {
		ChartPanel cp = new ChartPanel(manager.createDurationsChart(manager.getInstance().getP()));
		pd = new DurationsDialog(texts);
		pd.getContentPane().add(cp, BorderLayout.CENTER);
		pd.setVisible(true);
		pd.setLocationRelativeTo(null);
		pd.revalidate();
	}

	private void createDueDatesChart() {
		dueDates = i.getD();
		XYDataset ds = dueDatesDataset();
		JFreeChart durationsChart = ChartFactory.createXYLineChart("", "Tareas", "t", ds, PlotOrientation.VERTICAL,
				true, true, false);
		XYPlot plot = durationsChart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		int maxDomain = dueDates.length; // Nº Tareas

		domain.setRange(0.00, maxDomain);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);

		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		double maxRange = 0;
		for (Double d : dueDates)
			if (d > maxRange)
				maxRange = d; // Max due-date

		range.setRange(0.0, maxRange + 10);
		range.setTickUnit(new NumberTickUnit(10)); // TODO personalizable mejor?
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, Color.RED);

		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);
		ChartPanel cp = new ChartPanel(durationsChart);

		dd = new DueDatesDialog();
		dd.getContentPane().add(cp, BorderLayout.CENTER);
		dd.setVisible(true);
		dd.setLocationRelativeTo(null);
		dd.revalidate();
	}

	private XYDataset dueDatesDataset() {
		XYSeries dueDate = new XYSeries("Fecha de vencimiento");
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i = 0; i < dueDates.length; i++)
			dueDate.add(i, dueDates[i]);
		dataset.addSeries(dueDate);
		return dataset;
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

	private void createTasksTable() {
		dueDates = i.getD();
		durations = i.getP();
		td = new TasksDialog();
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
						frmVisuzalizadorYGenerador.revalidate();
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
					goToEnd(); // TODO disable btnFinal?
				}
			});
			btnFinal.setEnabled(false);
		}
		return btnFinal;
	}

	private void goToEnd() {
		frmVisuzalizadorYGenerador.remove(cp);

		String rule = (String) getRulesComboBox().getSelectedItem();
		switch (rule) {
		case "ATC":
			p = Gestor.planificaATC((double) getRulesSpinner().getValue(), i);
			break;
		case "EDD":
			p = Gestor.planificaEDD(i);
			break;
		case "SPT":
			p = Gestor.planificaSPT(i);
			break;
		default:
			break;
		}
		intervals = i.getPerfilMaquina();

		// Modify renderer (jfreechart)
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesPaint(1, Color.BLACK);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		// Create tasks (jfreechart)
		Map<Integer, Integer> capacity = createTasks(renderer, i, p);

		// Create dataset (jfreechart)
		// XYDataset ds = createDataset();
		XYDataset ds = createDataset(i, p, capacity);

		// Create chart (jfreechart)
		xylineChart = ChartFactory.createXYLineChart("", "t", "", ds, PlotOrientation.VERTICAL, true, true, false);

		// Modify plot (jfreechart)
		XYPlot plot = xylineChart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		// Modify axis (jfreechart)
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		Intervalo interval = intervals.get(intervals.size() - 1);
		double maxDomain = interval.getInicio(); // TODO menor en instancias grandes
		if (interval.getFin() < 300000)
			maxDomain = interval.getFin();

		domain.setRange(0.00, maxDomain + 1);
		domain.setTickUnit(new NumberTickUnit((int) getAxisSpinner().getValue()));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);

		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		int maxRange = 0;
		for (Intervalo aux : intervals)
			if (aux.getCap() > maxRange)
				maxRange = aux.getCap();

		range.setRange(0.0, maxRange + 1);
		range.setTickUnit(new NumberTickUnit(1));
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);

		// Set renderer (jfreechart)
		plot.setRenderer(renderer);

		// Create chart panel (jfreechart)
		cp = new ChartPanel(xylineChart);

		// Add chart to panel (swing)
		frmVisuzalizadorYGenerador.getContentPane().add(cp);
		getBtnSiguiente().setEnabled(false);

		frmVisuzalizadorYGenerador.revalidate();
	}

	private Map<Integer, Integer> createTasks(XYLineAndShapeRenderer renderer, Instancia i, Planificacion p) {

		BasicStroke stroke = new BasicStroke(1.0f);
		Color border = Color.BLACK;
		Color fill = Color.LIGHT_GRAY;
		double height = 1;
		double y = 0;
		double[] durations = i.getP();
		double[] dueDates = i.getD();
		int[] startTimes = p.getSti();
		double[] endTimes = durations.clone(); // startTime[t-1]
		HashMap<Double, Double> tasks = new HashMap<Double, Double>(); // [y][endTime]
		HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>(); // [startTime][taskID] startTime !único falla
		HashMap<Integer, Integer> capacity = new HashMap<Integer, Integer>(); // [time][occupied]

		for (int j = 0; j < startTimes.length; j++)
			ids.put(startTimes[j], j);

		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

		for (int s = 0; s <= startTimes.length - 1; s++) {
			for (int k = 0; k <= startTimes.length - 2; k++) {
				if (startTimes[k] > startTimes[k + 1]) {
					double temp = 0d;

					temp = startTimes[k];
					startTimes[k] = startTimes[k + 1];
					startTimes[k + 1] = (int) temp;

					temp = endTimes[k];
					endTimes[k] = endTimes[k + 1];
					endTimes[k + 1] = temp;

					temp = durations[k];
					durations[k] = durations[k + 1];
					durations[k + 1] = temp;

					temp = dueDates[k];
					dueDates[k] = dueDates[k + 1];
					dueDates[k + 1] = temp;

				}
			}
		}

		for (int t = 0; t < startTimes.length; t++) {
			if (t == 0) {
				tasks.put(0d, endTimes[t]);
				// capacity.put(0, 1);
			} else {
				Object[] sortedKeys = tasks.keySet().toArray();
				Arrays.sort(sortedKeys);
				boolean found = false;
				for (int j = 0; j < sortedKeys.length; j++)
					if (tasks.get(sortedKeys[j]) <= startTimes[t]) {
						y = (double) sortedKeys[j];
						found = true;
						break;
					}
				if (!found) {
					y = (double) sortedKeys[sortedKeys.length - 1] + 1;
				}
				tasks.put(y, endTimes[t]);
			}

			Shape rectangle = new Rectangle2D.Double(startTimes[t], y, durations[t], height);
			XYShapeAnnotation note = new XYShapeAnnotation(rectangle, stroke, border, fill);
			XYTextAnnotation text = new XYTextAnnotation("" + ids.get(startTimes[t]),
					rectangle.getBounds().getCenterX(), rectangle.getBounds().getCenterY());
			renderer.addAnnotation(note, Layer.BACKGROUND);
			renderer.addAnnotation(text); // TODO omitir en instancias grandes
		}

		for (int j = 0; j < endTimes[endTimes.length - 1]; j++) {
			for (int k = 0; k < startTimes.length; k++) { // recorrer tareas

				if (j >= startTimes[k] && j < endTimes[k]) {
					if (capacity.get(j) == null) {
						capacity.put(j, 1);
					} else {
						capacity.put(j, capacity.get(j) + 1);
					}
				}
			}

		}
		capacity.put((int) endTimes[endTimes.length - 1], 1); // endTime !unico falla
		return capacity;
	}

	private XYDataset createDataset(Instancia i, Planificacion p, Map<Integer, Integer> occupied) {
		double[] durations = i.getP();
		int[] startTimes = p.getSti();
		double[] endTimes = durations.clone();

		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

		for (int s = 0; s <= startTimes.length - 1; s++) {
			for (int k = 0; k <= startTimes.length - 2; k++) {
				if (startTimes[k] > startTimes[k + 1]) {
					double temp = 0d;

					temp = startTimes[k];
					startTimes[k] = startTimes[k + 1];
					startTimes[k + 1] = (int) temp;

					temp = endTimes[k];
					endTimes[k] = endTimes[k + 1];
					endTimes[k + 1] = temp;

					temp = durations[k];
					durations[k] = durations[k + 1];
					durations[k + 1] = temp;

				}
			}
		}

		final XYSeries tasks = new XYSeries("Capacidad Ocupada");
		Object[] sortedKeys = occupied.keySet().toArray();
		Arrays.sort(sortedKeys);
		for (int j = 0; j < sortedKeys.length; j++) {
			for (int t = 0; t < startTimes.length; t++) {
				int key = (int) sortedKeys[j];
				if (key == startTimes[t] || key == endTimes[t]) {
					if (key > 0 && occupied.get(key) > occupied.get(sortedKeys[j - 1]))
						tasks.add(key, occupied.get(key) - 1);
					else if (key > 0 && occupied.get(key) < occupied.get(sortedKeys[j - 1]))
						tasks.add(key, occupied.get(key - 1));
					tasks.add(key, occupied.get(key));
				}
			}
		}
		tasks.add(endTimes[endTimes.length - 1], 0);

		final XYSeries capacity = new XYSeries("Capacidad disponible");
		for (int j = 0; j < i.getPerfilMaquina().size(); j++) {
			if (j == i.getPerfilMaquina().size() - 1)
				break;
			capacity.add(i.getPerfilMaquina().get(j).getInicio(), i.getPerfilMaquina().get(j).getCap());
			capacity.add(i.getPerfilMaquina().get(j).getFin(), i.getPerfilMaquina().get(j).getCap());
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(tasks);
		dataset.addSeries(capacity);
		return dataset;
	}
}
