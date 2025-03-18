package com.projeto.drones.drones_backend.services;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.borders.Border;
import com.projeto.drones.drones_backend.models.Drone;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PdfExportService {

    // Cores para o documento
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(0, 102, 204);
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(41, 128, 185);
    private static final DeviceRgb TABLE_HEADER_BG = new DeviceRgb(41, 128, 185);
    private static final DeviceRgb TABLE_HEADER_TEXT = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb TABLE_ROW_ODD = new DeviceRgb(240, 240, 240);
    private static final DeviceRgb TABLE_ROW_EVEN = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb TABLE_BORDER = new DeviceRgb(220, 220, 220);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(100, 100, 100);
    private static final DeviceRgb HIGHLIGHT_COLOR = new DeviceRgb(76, 175, 80);
    private static final DeviceRgb WARNING_COLOR = new DeviceRgb(255, 152, 0);

    public byte[] gerarPdfComparacao(List<Drone> drones) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Configuração inicial do documento
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4.rotate()); // Formato paisagem para tabelas grandes
            Document document = new Document(pdfDocument);
            document.setMargins(40, 30, 40, 30);

            // Inicializar fontes
            PdfFont fontRegular;
            PdfFont fontBold;
            PdfFont fontItalic;
            try {
                fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                fontItalic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao carregar fontes", e);
            }

            // Adicionar metadados ao PDF
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();
            info.setTitle("Relatório de Comparação de Drones");
            info.setAuthor("DroneScout");
            info.setSubject("Comparação técnica de drones");
            info.setCreator("DroneScout PDF Export Service");

            // Título do Relatório
            Paragraph title = new Paragraph("Relatório de Comparação de Drones")
                    .setFont(fontBold)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(PRIMARY_COLOR)
                    .setMarginBottom(10);
            document.add(title);

            // Data de geração
            Paragraph date = new Paragraph("Gerado em: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setFont(fontRegular)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(date);

            // Adicionar resumo executivo
            criarResumoExecutivo(document, drones, fontRegular, fontBold, fontItalic);

            // Adicionar tabela de comparação
            criarTabelaComparacao(document, drones, fontRegular, fontBold);

            // Adicionar destaques técnicos
            criarDestaquesTecnicos(document, drones, fontRegular, fontBold);

            // Adicionar detalhes individuais de cada drone
            criarDetalhesIndividuais(document, drones, fontRegular, fontBold);

            // Adicionar comparação por categorias
            criarComparacaoPorCategorias(document, drones, fontRegular, fontBold);

            // Adicionar rodapé
            Paragraph footer = new Paragraph("DroneScout © " + LocalDate.now().getYear())
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(DARK_GRAY)
                    .setMarginTop(20);
            document.add(footer);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void criarResumoExecutivo(Document document, List<Drone> drones, PdfFont fontRegular, PdfFont fontBold, PdfFont fontItalic) {
        Paragraph sectionTitle = new Paragraph("Resumo Executivo")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(10);
        document.add(sectionTitle);

        // Informações gerais sobre os drones comparados
        Paragraph intro = new Paragraph(
                "Este relatório apresenta uma análise comparativa de " + drones.size() + " drones. " +
                        "A comparação inclui especificações técnicas detalhadas e características de desempenho para auxiliar na tomada de decisão.")
                .setFont(fontRegular)
                .setFontSize(10)
                .setMarginBottom(10);
        document.add(intro);

        // Tabela de resumo com informações básicas
        Table resumoTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        // Cabeçalhos
        adicionarCelulaHeader(resumoTable, "Modelo", fontBold);
        adicionarCelulaHeader(resumoTable, "Fabricante", fontBold);
        adicionarCelulaHeader(resumoTable, "Categoria", fontBold);
        adicionarCelulaHeader(resumoTable, "Autonomia", fontBold);
        adicionarCelulaHeader(resumoTable, "Faixa de Preço", fontBold);

        // Dados
        for (int i = 0; i < drones.size(); i++) {
            Drone drone = drones.get(i);
            boolean isEven = i % 2 == 0;

            adicionarCelulaTabela(resumoTable, drone.getNome(), isEven, fontRegular);
            adicionarCelulaTabela(resumoTable, drone.getFabricante(), isEven, fontRegular);
            adicionarCelulaTabela(resumoTable, drone.getCategoria(), isEven, fontRegular);
            adicionarCelulaTabela(resumoTable, drone.getAutonomia() + " min", isEven, fontRegular);
            adicionarCelulaTabela(resumoTable, String.format("%.2f€ - %.2f€", drone.getPrecoMin(), drone.getPrecoMax()), isEven, fontRegular);
        }

        document.add(resumoTable);

        // Destaques principais
        Paragraph highlightsTitle = new Paragraph("Destaques:")
                .setFont(fontBold)
                .setFontSize(10)
                .setMarginBottom(5);
        document.add(highlightsTitle);

        // Encontrar drones com características de destaque
        Drone melhorAutonomia = drones.stream()
                .max(Comparator.comparing(Drone::getAutonomia))
                .orElse(null);

        Drone melhorVelocidade = drones.stream()
                .max(Comparator.comparing(Drone::getVelocidadeMaxima))
                .orElse(null);

        Drone melhorAlcance = drones.stream()
                .max(Comparator.comparing(Drone::getAlcanceMaximo))
                .orElse(null);

        Drone maisCarga = drones.stream()
                .max(Comparator.comparing(Drone::getCargaMaxima))
                .orElse(null);

        // Lista de destaques
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List()
                .setFont(fontRegular)
                .setFontSize(9)
                .setSymbolIndent(12);

        if (melhorAutonomia != null) {
            ListItem item = new ListItem(
                    "Melhor autonomia: " + melhorAutonomia.getNome() + " (" + melhorAutonomia.getAutonomia() + " min)");
            list.add(item);
        }

        if (melhorVelocidade != null) {
            ListItem item = new ListItem(
                    "Maior velocidade: " + melhorVelocidade.getNome() + " (" + melhorVelocidade.getVelocidadeMaxima() + " km/h)");
            list.add(item);
        }

        if (melhorAlcance != null) {
            ListItem item = new ListItem(
                    "Maior alcance: " + melhorAlcance.getNome() + " (" + melhorAlcance.getAlcanceMaximo() + " km)");
            list.add(item);
        }

        if (maisCarga != null) {
            ListItem item = new ListItem(
                    "Maior capacidade de carga: " + maisCarga.getNome() + " (" + maisCarga.getCargaMaxima() + " kg)");
            list.add(item);
        }

        document.add(list);

        // Linha separadora
        SolidBorder border = new SolidBorder(TABLE_BORDER, 1);
        Div separator = new Div()
                .setMarginTop(10)
                .setMarginBottom(10)
                .setBorderBottom(border)
                .setWidth(UnitValue.createPercentValue(100));
        document.add(separator);
    }

    private void criarTabelaComparacao(Document document, List<Drone> drones, PdfFont fontRegular, PdfFont fontBold) {
        Paragraph tableTitle = new Paragraph("Tabela Comparativa")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(10);
        document.add(tableTitle);

        // Criar tabela principal de comparação
        float[] columnWidths = {3, 2.5f, 2, 2, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 2, 2, 2, 2, 2.5f};
        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

        // Cabeçalhos
        String[] headers = {
                "Nome", "Fabricante", "Categoria", "Preço (€)", "Autonomia (min)",
                "Peso (kg)", "Carga Máx. (kg)", "Vel. Máx. (km/h)", "Alcance (km)", "Câmera",
                "GPS", "Anticolisão", "Conectividade", "Modos de Voo"
        };

        for (String header : headers) {
            adicionarCelulaHeader(table, header, fontBold);
        }

        // Adicionar os dados
        for (int i = 0; i < drones.size(); i++) {
            Drone drone = drones.get(i);
            boolean isEven = i % 2 == 0;

            adicionarCelulaTabela(table, drone.getNome(), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getFabricante(), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getCategoria(), isEven, fontRegular);
            adicionarCelulaTabela(table, String.format("%.2f€ - %.2f€", drone.getPrecoMin(), drone.getPrecoMax()), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getAutonomia() + " min", isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getPeso() + " kg", isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getCargaMaxima() + " kg", isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getVelocidadeMaxima() + " km/h", isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getAlcanceMaximo() + " km", isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getResolucaoCamera(), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getGps(), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getSistemaAnticolisao(), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getConectividade(), isEven, fontRegular);
            adicionarCelulaTabela(table, drone.getModosVoo(), isEven, fontRegular);
        }

        document.add(table);

        // Adicionar legenda
        Paragraph legenda = new Paragraph("* Os valores apresentados são baseados nas especificações fornecidas pelos fabricantes.")
                .setFont(fontRegular)
                .setFontSize(9)
                .setFontColor(DARK_GRAY)
                .setMarginTop(5);
        document.add(legenda);
    }

    private void criarDestaquesTecnicos(Document document, List<Drone> drones, PdfFont fontRegular, PdfFont fontBold) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        Paragraph sectionTitle = new Paragraph("Destaques Técnicos")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(10);
        document.add(sectionTitle);

        Paragraph intro = new Paragraph(
                "Esta seção apresenta uma análise comparativa das principais características técnicas dos drones, " +
                        "destacando os modelos com melhor desempenho em cada categoria.")
                .setFont(fontRegular)
                .setFontSize(10)
                .setMarginBottom(15);
        document.add(intro);

        // Tabela de rankings
        Table rankingTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        // Cabeçalhos
        adicionarCelulaHeader(rankingTable, "Categoria", fontBold);
        adicionarCelulaHeader(rankingTable, "1º Lugar", fontBold);
        adicionarCelulaHeader(rankingTable, "2º Lugar", fontBold);
        adicionarCelulaHeader(rankingTable, "3º Lugar", fontBold);
        adicionarCelulaHeader(rankingTable, "Valor de Referência", fontBold);

        // Autonomia
        List<Drone> autonomiaRanking = new ArrayList<>(drones);
        autonomiaRanking.sort(Comparator.comparing(Drone::getAutonomia).reversed());
        adicionarLinhaRanking(rankingTable, "Autonomia (min)", autonomiaRanking,
                drone -> drone.getAutonomia() + " min", fontRegular);

        // Velocidade
        List<Drone> velocidadeRanking = new ArrayList<>(drones);
        velocidadeRanking.sort(Comparator.comparing(Drone::getVelocidadeMaxima).reversed());
        adicionarLinhaRanking(rankingTable, "Velocidade Máxima (km/h)", velocidadeRanking,
                drone -> drone.getVelocidadeMaxima() + " km/h", fontRegular);

        // Alcance
        List<Drone> alcanceRanking = new ArrayList<>(drones);
        alcanceRanking.sort(Comparator.comparing(Drone::getAlcanceMaximo).reversed());
        adicionarLinhaRanking(rankingTable, "Alcance Máximo (km)", alcanceRanking,
                drone -> drone.getAlcanceMaximo() + " km", fontRegular);

        // Carga
        List<Drone> cargaRanking = new ArrayList<>(drones);
        cargaRanking.sort(Comparator.comparing(Drone::getCargaMaxima).reversed());
        adicionarLinhaRanking(rankingTable, "Capacidade de Carga (kg)", cargaRanking,
                drone -> drone.getCargaMaxima() + " kg", fontRegular);

        // Peso (mais leve primeiro)
        List<Drone> pesoRanking = new ArrayList<>(drones);
        pesoRanking.sort(Comparator.comparing(Drone::getPeso));
        adicionarLinhaRanking(rankingTable, "Peso (kg, mais leve primeiro)", pesoRanking,
                drone -> drone.getPeso() + " kg", fontRegular);

        // Preço (mais barato primeiro, usando preço mínimo)
        List<Drone> precoRanking = new ArrayList<>(drones);
        precoRanking.sort(Comparator.comparing(Drone::getPrecoMin));
        adicionarLinhaRanking(rankingTable, "Preço Mínimo (€, mais barato primeiro)", precoRanking,
                drone -> String.format("%.2f€", drone.getPrecoMin()), fontRegular);

        document.add(rankingTable);

        // Adicionar nota explicativa
        Paragraph nota = new Paragraph(
                "Nota: Os rankings acima são baseados exclusivamente nos valores numéricos de cada especificação. " +
                        "A escolha do drone ideal deve considerar o conjunto completo de características e a finalidade específica de uso.")
                .setFont(fontRegular)
                .setFontSize(9)
                .setFontColor(DARK_GRAY)
                .setMarginTop(10);
        document.add(nota);
    }

    private void criarDetalhesIndividuais(Document document, List<Drone> drones, PdfFont fontRegular, PdfFont fontBold) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        Paragraph sectionTitle = new Paragraph("Detalhes Individuais dos Drones")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(15);
        document.add(sectionTitle);

        // Adicionar detalhes de cada drone
        for (int i = 0; i < drones.size(); i++) {
            Drone drone = drones.get(i);

            if (i > 0) {
                document.add(new Paragraph("\n").setMarginBottom(10));
            }

            // Título do drone
            Paragraph droneTitle = new Paragraph((i + 1) + ". " + drone.getNome())
                    .setFont(fontBold)
                    .setFontSize(12)
                    .setFontColor(PRIMARY_COLOR)
                    .setMarginBottom(5);
            document.add(droneTitle);

            // Tabela com informações detalhadas
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(10);

            // Primeira linha
            adicionarCelulaInfo(infoTable, "Fabricante", drone.getFabricante(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Categoria", drone.getCategoria(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Preço", String.format("%.2f€ - %.2f€", drone.getPrecoMin(), drone.getPrecoMax()), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Certificação", drone.getCertificacao(), fontRegular, fontBold);

            // Segunda linha
            adicionarCelulaInfo(infoTable, "Autonomia", drone.getAutonomia() + " min", fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Velocidade Máx.", drone.getVelocidadeMaxima() + " km/h", fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Alcance Máx.", drone.getAlcanceMaximo() + " km", fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Peso", drone.getPeso() + " kg", fontRegular, fontBold);

            // Terceira linha
            adicionarCelulaInfo(infoTable, "Carga Máxima", drone.getCargaMaxima() + " kg", fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Câmera", drone.getResolucaoCamera(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "GPS", drone.getGps(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Anticolisão", drone.getSistemaAnticolisao(), fontRegular, fontBold);

            // Quarta linha
            adicionarCelulaInfo(infoTable, "Conectividade", drone.getConectividade(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Modos de Voo", drone.getModosVoo(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Fail-Safe", drone.getFailSafe(), fontRegular, fontBold);
            adicionarCelulaInfo(infoTable, "Sensores", drone.getSensores(), fontRegular, fontBold);

            document.add(infoTable);

            // Descrição
            if (drone.getDescricao() != null && !drone.getDescricao().isEmpty()) {
                Paragraph descTitle = new Paragraph("Descrição:")
                        .setFont(fontBold)
                        .setFontSize(10)
                        .setMarginTop(5)
                        .setMarginBottom(2);
                document.add(descTitle);

                Paragraph description = new Paragraph(drone.getDescricao())
                        .setFont(fontRegular)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.JUSTIFIED);
                document.add(description);
            }

            // Adicionar linha separadora entre drones
            if (i < drones.size() - 1) {
                SolidBorder border = new SolidBorder(TABLE_BORDER, 1);
                Div separator = new Div()
                        .setMarginTop(10)
                        .setMarginBottom(10)
                        .setBorderBottom(border)
                        .setWidth(UnitValue.createPercentValue(100));
                document.add(separator);
            }
        }
    }

    private void criarComparacaoPorCategorias(Document document, List<Drone> drones, PdfFont fontRegular, PdfFont fontBold) {
        // Só adicionar esta seção se houver pelo menos 2 categorias diferentes
        Map<String, List<Drone>> dronesPorCategoria = drones.stream()
                .collect(Collectors.groupingBy(Drone::getCategoria));

        if (dronesPorCategoria.size() < 2) {
            return;
        }

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        Paragraph sectionTitle = new Paragraph("Comparação por Categorias")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(10);
        document.add(sectionTitle);

        Paragraph intro = new Paragraph(
                "Esta seção apresenta uma análise dos drones agrupados por categoria, " +
                        "permitindo uma comparação mais específica entre modelos com finalidades semelhantes.")
                .setFont(fontRegular)
                .setFontSize(10)
                .setMarginBottom(15);
        document.add(intro);

        // Para cada categoria, criar uma tabela comparativa
        for (Map.Entry<String, List<Drone>> entry : dronesPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            List<Drone> dronesCategoria = entry.getValue();

            // Só criar tabela se houver pelo menos 2 drones na categoria
            if (dronesCategoria.size() < 2) {
                continue;
            }

            Paragraph categoryTitle = new Paragraph("Categoria: " + categoria)
                    .setFont(fontBold)
                    .setFontSize(12)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(15)
                    .setMarginBottom(5);
            document.add(categoryTitle);

            // Tabela comparativa para esta categoria
            Table categoryTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2, 2, 2}))
                    .useAllAvailableWidth()
                    .setMarginBottom(10);

            // Cabeçalhos
            adicionarCelulaHeader(categoryTable, "Modelo", fontBold);
            adicionarCelulaHeader(categoryTable, "Fabricante", fontBold);
            adicionarCelulaHeader(categoryTable, "Preço (€)", fontBold);
            adicionarCelulaHeader(categoryTable, "Autonomia", fontBold);
            adicionarCelulaHeader(categoryTable, "Vel. Máx.", fontBold);
            adicionarCelulaHeader(categoryTable, "Alcance", fontBold);
            adicionarCelulaHeader(categoryTable, "Peso", fontBold);

            // Dados
            for (int i = 0; i < dronesCategoria.size(); i++) {
                Drone drone = dronesCategoria.get(i);
                boolean isEven = i % 2 == 0;

                adicionarCelulaTabela(categoryTable, drone.getNome(), isEven, fontRegular);
                adicionarCelulaTabela(categoryTable, drone.getFabricante(), isEven, fontRegular);
                adicionarCelulaTabela(categoryTable, String.format("%.2f€ - %.2f€", drone.getPrecoMin(), drone.getPrecoMax()), isEven, fontRegular);
                adicionarCelulaTabela(categoryTable, drone.getAutonomia() + " min", isEven, fontRegular);
                adicionarCelulaTabela(categoryTable, drone.getVelocidadeMaxima() + " km/h", isEven, fontRegular);
                adicionarCelulaTabela(categoryTable, drone.getAlcanceMaximo() + " km", isEven, fontRegular);
                adicionarCelulaTabela(categoryTable, drone.getPeso() + " kg", isEven, fontRegular);
            }

            document.add(categoryTable);

            // Encontrar o melhor drone desta categoria (simplificado - baseado em autonomia)
            Drone melhorDroneCategoria = dronesCategoria.stream()
                    .max(Comparator.comparing(Drone::getAutonomia))
                    .orElse(null);

            if (melhorDroneCategoria != null) {
                Paragraph bestDrone = new Paragraph("Destaque da categoria: " + melhorDroneCategoria.getNome() +
                        " - Autonomia de " + melhorDroneCategoria.getAutonomia() + " minutos")
                        .setFont(fontRegular)
                        .setFontSize(9)
                        .setFontColor(HIGHLIGHT_COLOR)
                        .setMarginTop(5)
                        .setMarginBottom(15);
                document.add(bestDrone);
            }
        }
    }

    private void adicionarCelulaHeader(Table table, String text, PdfFont fontBold) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(fontBold).setFontSize(10))
                .setBackgroundColor(TABLE_HEADER_BG)
                .setFontColor(TABLE_HEADER_TEXT)
                .setPadding(5)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cell);
    }

    private void adicionarCelulaTabela(Table table, String text, boolean isEven, PdfFont fontRegular) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(fontRegular).setFontSize(9))
                .setBackgroundColor(isEven ? TABLE_ROW_EVEN : TABLE_ROW_ODD)
                .setBorder(new SolidBorder(TABLE_BORDER, 0.5f))
                .setPadding(5);
        table.addCell(cell);
    }

    private void adicionarCelulaInfo(Table table, String label, String value, PdfFont fontRegular, PdfFont fontBold) {
        Cell cell = new Cell()
                .add(new Paragraph(label + ":").setFont(fontBold).setFontSize(9).setMarginBottom(2))
                .add(new Paragraph(value).setFont(fontRegular).setFontSize(9))
                .setPadding(5)
                .setBorder(new SolidBorder(TABLE_BORDER, 0.5f));
        table.addCell(cell);
    }

    private void adicionarLinhaRanking(Table table, String categoria, List<Drone> ranking,
                                       java.util.function.Function<Drone, String> valorFn, PdfFont fontRegular) {

        adicionarCelulaTabela(table, categoria, true, fontRegular);

        // Primeiro lugar
        if (ranking.size() >= 1) {
            String texto = ranking.get(0).getNome() + " (" + valorFn.apply(ranking.get(0)) + ")";
            adicionarCelulaTabela(table, texto, true, fontRegular);
        } else {
            adicionarCelulaTabela(table, "N/A", true, fontRegular);
        }

        // Segundo lugar
        if (ranking.size() >= 2) {
            String texto = ranking.get(1).getNome() + " (" + valorFn.apply(ranking.get(1)) + ")";
            adicionarCelulaTabela(table, texto, true, fontRegular);
        } else {
            adicionarCelulaTabela(table, "N/A", true, fontRegular);
        }

        // Terceiro lugar
        if (ranking.size() >= 3) {
            String texto = ranking.get(2).getNome() + " (" + valorFn.apply(ranking.get(2)) + ")";
            adicionarCelulaTabela(table, texto, true, fontRegular);
        } else {
            adicionarCelulaTabela(table, "N/A", true, fontRegular);
        }

        // Valor de referência (média de todos os drones)
        if (categoria.contains("Autonomia")) {
            double media = ranking.stream().mapToDouble(Drone::getAutonomia).average().orElse(0);
            adicionarCelulaTabela(table, String.format("Média: %.1f min", media), true, fontRegular);
        } else if (categoria.contains("Velocidade")) {
            double media = ranking.stream().mapToDouble(Drone::getVelocidadeMaxima).average().orElse(0);
            adicionarCelulaTabela(table, String.format("Média: %.1f km/h", media), true, fontRegular);
        } else if (categoria.contains("Alcance")) {
            double media = ranking.stream().mapToDouble(Drone::getAlcanceMaximo).average().orElse(0);
            adicionarCelulaTabela(table, String.format("Média: %.1f km", media), true, fontRegular);
        } else if (categoria.contains("Carga")) {
            double media = ranking.stream().mapToDouble(Drone::getCargaMaxima).average().orElse(0);
            adicionarCelulaTabela(table, String.format("Média: %.2f kg", media), true, fontRegular);
        } else if (categoria.contains("Peso")) {
            double media = ranking.stream().mapToDouble(Drone::getPeso).average().orElse(0);
            adicionarCelulaTabela(table, String.format("Média: %.2f kg", media), true, fontRegular);
        } else if (categoria.contains("Preço")) {
            double media = ranking.stream().mapToDouble(Drone::getPrecoMin).average().orElse(0);
            adicionarCelulaTabela(table, String.format("Média: %.2f€", media), true, fontRegular);
        } else {
            adicionarCelulaTabela(table, "N/A", true, fontRegular);
        }
    }
}

