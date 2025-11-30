package view;

import database.dao.PokemonDAO;
import database.model.PokemonStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PokemonStatusFrame extends JDialog {

    private final PokemonDAO pokemonDAO;
    private JTable table;

    public PokemonStatusFrame(Frame owner, PokemonDAO pokemonDAO) {
        super(owner, "Status dos Pokémons (vw_pokemon_status)", true);
        this.pokemonDAO = pokemonDAO;

        setSize(700, 400);
        setLocationRelativeTo(owner);

        initLayout();
        loadData();
    }

    private void initLayout() {
        table = new JTable();
        JScrollPane scroll = new JScrollPane(table);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    private void loadData() {
        List<PokemonStatus> lista = pokemonDAO.viewListPokemonStatus();

        String[] colunas = { "ID", "Pokémon", "Tipo", "Status", "Motivo Remoção" };
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        for (PokemonStatus ps : lista) {
            Object[] row = {
                    ps.getId(),
                    ps.getPokemon(),
                    ps.getTipo(),
                    ps.getStatus(),
                    ps.getMotivoRemocao()
            };
            model.addRow(row);
        }

        table.setModel(model);
    }
}
