package view;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import database.connection.ConnectionFactory;
import database.dao.PokemonDAO;
import database.model.Pokemon;


public class MainFrame extends JFrame {

    private final Connection connection;
    private final PokemonDAO pokemonDAO;

    private JButton btnAddPokemon;
    private JButton btnDistribuirTipo;
    private JButton btnGerarTotalizador;
    private JButton btnRemoverDuplicados;
    private JButton btnMostrarStatus;
    private JButton btnMostrarTotais;

    public MainFrame(Connection connection) throws SQLException {
        this.connection = connection;
        this.pokemonDAO = new PokemonDAO(connection);

        setTitle("Gerenciador de Pokémons");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();
    }

    private void initComponents() {
        btnAddPokemon = new JButton("Adicionar Pokémon (Q1)");
        btnDistribuirTipo = new JButton("Distribuir por Tipo (Q2 + Q3)");
        btnGerarTotalizador = new JButton("Gerar Totalizador (Q4)");
        btnRemoverDuplicados = new JButton("Remover Duplicados (Q5)");
        btnMostrarStatus = new JButton("Mostrar Status (Q6)");
        btnMostrarTotais = new JButton("Mostrar Totais (Q6)");
    }

    private void initLayout() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(btnAddPokemon);
        panel.add(btnDistribuirTipo);
        panel.add(btnGerarTotalizador);
        panel.add(btnRemoverDuplicados);
        panel.add(btnMostrarStatus);
        panel.add(btnMostrarTotais);

        setContentPane(panel);
    }

    private void initActions() {

        // Q1 – abrir tela de cadastro simples
        btnAddPokemon.addActionListener(e -> {
            try {
                AddPokemonFrame tela = new AddPokemonFrame(connection, pokemonDAO);
                tela.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao abrir tela de cadastro: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Q2 + Q3 – distribuir por tipo e evitar duplicados
        btnDistribuirTipo.addActionListener(e -> {
            try {
                pokemonDAO.sortPokemonByType();
                JOptionPane.showMessageDialog(this,
                        "Pokémons distribuídos por tipo com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao distribuir por tipo: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Q4 – gerar totalizador
        btnGerarTotalizador.addActionListener(e -> {
            try {
                pokemonDAO.generateTotalByType();
                JOptionPane.showMessageDialog(this,
                        "Totalizador gerado/atualizado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao gerar totalizador: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Q5 – remover duplicados da tabela principal tb_pokemon
        btnRemoverDuplicados.addActionListener(e -> {
            int opc = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja remover os duplicados da tabela principal?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (opc == JOptionPane.YES_OPTION) {
                try {
                    pokemonDAO.deleteDuplicatesFrom("tb_pokemon");
                    JOptionPane.showMessageDialog(this,
                            "Duplicados removidos e registrados em tb_pokemon_deletados!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Erro ao remover duplicados: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Q6 – mostrar view de status
        btnMostrarStatus.addActionListener(e -> {
            try {
                PokemonStatusFrame statusFrame = new PokemonStatusFrame(this, pokemonDAO);
                statusFrame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao mostrar status: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Q6 – mostrar view de totais
        btnMostrarTotais.addActionListener(e -> {
            try {
                PokemonTotaisFrame totaisFrame = new PokemonTotaisFrame(this, pokemonDAO);
                totaisFrame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao mostrar totais: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
