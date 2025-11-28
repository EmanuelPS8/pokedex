package view;

import java.awt.EventQueue;
import java.sql.Connection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import database.connection.ConnectionFactory;
import database.dao.PokemonDAO;
import database.model.Pokemon;


public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablePokemons;
    private DefaultTableModel tableModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 772, 540);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblOptions = new JLabel("Opções");
        lblOptions.setBounds(360, 11, 60, 13);
        contentPane.add(lblOptions);

        JButton btnAddPokemon = new JButton("Adicionar Pokemon");
        btnAddPokemon.setBounds(71, 42, 150, 22);
        contentPane.add(btnAddPokemon);

        JButton btnReadPokemon = new JButton("Mostrar Pokemon");
        btnReadPokemon.setBounds(231, 42, 150, 22);
        contentPane.add(btnReadPokemon);

        JButton btnUpdatePokemon = new JButton("Editar Pokemon");
        btnUpdatePokemon.setBounds(391, 42, 150, 22);
        contentPane.add(btnUpdatePokemon);

        JButton btnDeletePokemon = new JButton("Deletar Pokemon");
        btnDeletePokemon.setBounds(551, 42, 150, 22);
        contentPane.add(btnDeletePokemon);

        String[] colunas = { "ID", "Pokemon", "Tipo" };
        tableModel = new DefaultTableModel(colunas, 0); 

        tablePokemons = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePokemons);
        scrollPane.setBounds(50, 100, 670, 380);
        contentPane.add(scrollPane);

        btnReadPokemon.addActionListener(e -> carregarPokemonsNaTabela());
    }

    private void carregarPokemonsNaTabela() {
        try {
        	Connection connection = ConnectionFactory.getConnection("localhost", "3306", "root", "1234", "pokedex");

            PokemonDAO dao = new PokemonDAO(connection);
            List<Pokemon> pokemons = dao.selectAll();

            tableModel.setRowCount(0);

            for (Pokemon p : pokemons) {
                tableModel.addRow(new Object[] {
                    p.getId(),
                    p.getPokemonName(),   
                    p.getPokemonType()    
                });
            }

            if (pokemons.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum Pokémon cadastrado.",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar pokémons: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
