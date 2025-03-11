package com.projeto.drones.drones_backend.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.projeto.drones.drones_backend.models.Drone;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfExportService {

    public byte[] gerarPdfComparacao(List<Drone> drones) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Título
            document.add(new Paragraph("Comparação de Drones")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Tabela com 10 colunas para incluir todos os novos campos
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 3}))
                    .useAllAvailableWidth();

            // Cabeçalhos da tabela
            table.addHeaderCell(new Cell().add(new Paragraph("Nome").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Fabricante").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Categoria").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Preço (€)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Autonomia (min)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Peso (kg)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Carga Máxima (kg)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Velocidade Máxima (km/h)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Alcance Máximo (km)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Sensores").setBold()));

            // Adicionando os dados dos drones
            for (Drone drone : drones) {
                table.addCell(new Cell().add(new Paragraph(drone.getNome())));
                table.addCell(new Cell().add(new Paragraph(drone.getFabricante())));
                table.addCell(new Cell().add(new Paragraph(drone.getCategoria())));

                // Exibindo o intervalo de preço
                String precoIntervalo = String.format("%s - %s", drone.getPrecoMin(), drone.getPrecoMax());
                table.addCell(new Cell().add(new Paragraph(precoIntervalo)));

                table.addCell(new Cell().add(new Paragraph(String.valueOf(drone.getAutonomia()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(drone.getPeso()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(drone.getCargaMaxima()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(drone.getVelocidadeMaxima()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(drone.getAlcanceMaximo()))));
                table.addCell(new Cell().add(new Paragraph(drone.getSensores())));
            }

            // Adicionando a tabela ao documento
            document.add(table);
            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}

