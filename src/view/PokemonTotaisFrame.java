package view;

import database.dao.PokemonDAO;
import database.model.PokemonTotais;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PokemonTotaisFrame extends JDialog {

    private final PokemonDAO pokemonDAO;
    private JTable table;

    public PokemonTotaisFrame(Frame owner, PokemonDAO pokemonDAO) {
        super(owner, "Totais por Tipo (vw_pokemon_totais)", true);
        this.pokemonDAO = pokemonDAO;

        setSize(600, 300);
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
        List<PokemonTotais> lista = pokemonDAO.viewListPokemonTotais();

        String[] colunas = { "Tipo", "Total Antes", "Removidos", "Total Depois" };
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        for (PokemonTotais pt : lista) {
            Object[] row = {
                    pt.getTipo(),
                    pt.getTotalAntes(),
                    pt.getRemovidos(),
                    pt.getTotalDepois()
            };
            model.addRow(row);
        }

        table.setModel(model);
    }
}
