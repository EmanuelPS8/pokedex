package view;

import database.connection.ConnectionFactory;
import database.dao.PokemonDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class AddPokemonFrame extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtTipo;
    private JButton btnAdicionar;

    private PokemonDAO pokemonDAO;

    public AddPokemonFrame(Connection connection, PokemonDAO pokemonDAO) throws SQLException {
        this.pokemonDAO = new PokemonDAO(connection);

        setTitle("Adicionar Pokémon");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        panel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        panel.add(txtNome);

        panel.add(new JLabel("Tipo:"));
        txtTipo = new JTextField();
        panel.add(txtTipo);

        btnAdicionar = new JButton("Adicionar");
        panel.add(btnAdicionar);

        // espaço vazio para alinhar
        panel.add(new JLabel(""));

        add(panel);

        btnAdicionar.addActionListener(e -> addPokemon());
    }

    private void addPokemon() {
        try {
            String nome = txtNome.getText().trim();
            String tipo = txtTipo.getText().trim();

            if (nome.isEmpty() || tipo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            long inserted = pokemonDAO.insertPokemon(nome, tipo);

            if (inserted > 0) {
                JOptionPane.showMessageDialog(this, "Pokémon adicionado!");
                txtNome.setText("");
                txtTipo.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao inserir Pokémon.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
