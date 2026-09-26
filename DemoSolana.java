package org.example;

import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.Cluster;
import org.p2p.solanaj.rpc.RpcClient;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URI;

public class DemoSolana {

    // RpcClient reaproveitado entre chamadas
    private static final RpcClient client = new RpcClient(Cluster.DEVNET);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {


            // CORES DO PROJETO

            Color BRANCO = Color.WHITE;
            Color PRETO = new Color(20, 20, 20);
            Color ROXO = new Color(110, 70, 255);
            Color CINZA = new Color(245, 245, 245);

           
            // JANELA PRINCIPAL

            JFrame frame = new JFrame("Auditor de Contas Solana Devnet");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(620, 420);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout(10, 10));

            // Fundo branco
            frame.getContentPane().setBackground(BRANCO);

            // ==============================
            // PAINEL SUPERIOR
            // ==============================

            JPanel painelTopo = new JPanel(new BorderLayout(8, 8));

            painelTopo.setBackground(BRANCO);

            painelTopo.setBorder(
                    BorderFactory.createEmptyBorder(
                            10, 10, 5, 10
                    )
            );

            // Texto de instrução
            JLabel lblInstrucao = new JLabel(
                    "Digite ou cole o endereço da conta (Devnet):"
            );

            lblInstrucao.setForeground(PRETO);
            lblInstrucao.setFont(
                    new Font("Arial", Font.PLAIN, 13)
            );

            // Campo do endereço
            JTextField txtEndereco = new JTextField(
                    ""
            );

            txtEndereco.setBackground(BRANCO);
            txtEndereco.setForeground(PRETO);
            txtEndereco.setCaretColor(PRETO);

            // Borda roxa
            txtEndereco.setBorder(
                    new LineBorder(ROXO, 2)
            );

            // Botão Auditar
            JButton btnAuditar = new JButton("Auditar Conta");

            btnAuditar.setBackground(PRETO);
            btnAuditar.setForeground(BRANCO);
            btnAuditar.setFocusPainted(false);
            btnAuditar.setBorderPainted(false);
            btnAuditar.setFont(
                    new Font("Arial", Font.BOLD, 12)
            );

            painelTopo.add(
                    lblInstrucao,
                    BorderLayout.NORTH
            );

            painelTopo.add(
                    txtEndereco,
                    BorderLayout.CENTER
            );

            painelTopo.add(
                    btnAuditar,
                    BorderLayout.EAST
            );

            frame.add(
                    painelTopo,
                    BorderLayout.NORTH
            );

            // ÁREA DE RESULTADO

            JTextArea areaResultado = new JTextArea();

            areaResultado.setEditable(false);

            // Fundo branco
            areaResultado.setBackground(BRANCO);

            // Texto preto
            areaResultado.setForeground(PRETO);

            // Fonte
            areaResultado.setFont(
                    new Font("Consolas", Font.PLAIN, 13)
            );

            areaResultado.setLineWrap(true);
            areaResultado.setWrapStyleWord(true);

            areaResultado.setMargin(
                    new Insets(10, 10, 10, 10)
            );

            areaResultado.setText(
                    "================ RELATÓRIO ================\n\n" +
                    ">>> Digite a chave pública acima e clique em 'Auditar Conta'.\n"
            );

            // Borda roxa
            areaResultado.setBorder(
                    new LineBorder(ROXO, 2)
            );

            JScrollPane scrollPane =
                    new JScrollPane(areaResultado);

            // Remove a borda padrão do ScrollPane
            scrollPane.setBorder(
                    BorderFactory.createEmptyBorder(
                            0, 20, 10, 20
                    )
            );

            scrollPane.getViewport().setBackground(BRANCO);

            frame.add(
                    scrollPane,
                    BorderLayout.CENTER
            );

            // BOTÃO SOLANA EXPLORER

            JButton btnAbrirExplorer =
                    new JButton("Abrir no Solana Explorer");

            btnAbrirExplorer.setEnabled(false);

            btnAbrirExplorer.setBackground(PRETO);
            btnAbrirExplorer.setForeground(BRANCO);

            btnAbrirExplorer.setFocusPainted(false);

            // Borda Roxo
            btnAbrirExplorer.setBorder(
                    new LineBorder(
                            ROXO,
                            2
                    )
            );

            btnAbrirExplorer.setFont(
                    new Font("Arial", Font.BOLD, 12)
            );

            frame.add(
                    btnAbrirExplorer,
                    BorderLayout.SOUTH
            );

            // AÇÃO - AUDITAR CONTA

            btnAuditar.addActionListener(e -> {

                String endereco =
                        txtEndereco.getText().trim();

                if (endereco.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Por favor, digite um endereço."
                    );

                    return;
                }

                // Desabilita enquanto consulta
                btnAuditar.setEnabled(false);

                areaResultado.setText(
                        "Consultando nós validadores da Solana Devnet...\n\n"
                );

                new Thread(() -> {

                    try {

                        // CONSULTA SOLANA

                        PublicKey conta =
                                new PublicKey(endereco);

                        long slot =
                                client.getApi().getSlot();

                        long saldoLamports =
                                client.getApi().getBalance(conta);

                        double saldoSol =
                                saldoLamports / 1_000_000_000.0;

                        // MOSTRAR RESULTADO

                        SwingUtilities.invokeLater(() -> {

                            areaResultado.setText("");

                            areaResultado.append(
                                    "================ RELATÓRIO ================\n\n"
                            );

                            areaResultado.append(
                                    "Endereço:  " +
                                    endereco +
                                    "\n"
                            );

                            areaResultado.append(
                                    "Bloco/Slot: " +
                                    slot +
                                    "\n"
                            );

                            areaResultado.append(
                                    "Saldo Real: " +
                                    saldoSol +
                                    " SOL\n\n"
                            );

                            areaResultado.append(
                                    "============================================\n\n"
                            );

                            areaResultado.append(
                                    "Status: Conta auditada diretamente no livro-razão!"
                            );

                            // Libera botão Explorer
                            btnAbrirExplorer.setEnabled(true);

                            // Libera botão Auditar
                            btnAuditar.setEnabled(true);
                        });

                    } catch (IllegalArgumentException iae) {

                        SwingUtilities.invokeLater(() -> {

                            areaResultado.setText(
                                    "================ ERRO ================\n\n" +
                                    "O endereço digitado não é uma chave " +
                                    "Base58 válida da Solana."
                            );

                            btnAuditar.setEnabled(true);
                        });

                    } catch (Exception ex) {

                        SwingUtilities.invokeLater(() -> {

                            areaResultado.setText(
                                    "================ ERRO ================\n\n" +
                                    "Não foi possível auditar.\n\n" +
                                    "Verifique sua conexão ou o endereço.\n\n" +
                                    "Detalhe: " +
                                    ex.getMessage()
                            );

                            btnAuditar.setEnabled(true);
                        });
                    }

                }).start();
            });


            // ABRIR SOLANA EXPLORER

            btnAbrirExplorer.addActionListener(e -> {

                try {

                    String endereco =
                            txtEndereco.getText().trim();

                    Desktop.getDesktop().browse(
                            new URI(
                                    "https://explorer.solana.com/address/"
                                    + endereco
                                    + "?cluster=devnet"
                            )
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Erro ao abrir link: "
                            + ex.getMessage()
                    );
                }
            });

            // MOSTRAR JANELA

            frame.setVisible(true);
        });
    }
}