package com.pdf.merge;

import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class Merge {
	
	public static void main(String[] args) throws Exception {
		
		File cartoes = new File("pdf/personal_etapa_3.pdf");
		
		File frente  = new File("pdf/modelos/psc2020_a3_frente.pdf");
		File verso   = new File("pdf/modelos/psc2020_a3_verso.pdf");
		
		File saida = new File("pdf/saida/saida_etapa3.pdf");
		
		long runtime = merge(cartoes, frente, verso, saida,0,0,0,0);
		
		// File template = new File("pdf/modelos/psc2020_a4.pdf");
		// long runtime = merge(cartoes, template, saida,5f,0);
		
		
		
		
		
		System.out.printf(":: Runtime: %sms\n", runtime);
		
	}
	
	/** Adiciona um background do arquivo 'frente' e um do 'verso' em todas as páginas ímpares e pares de 'entrada', respectivamente,
	 *  e escreve o documento resultante em 'saida'. É possível também fazer ajustes de deslocamento vertical e horizontal em cada template
	 *  via parâmetros 'offset'. Este método é compatível com qualquer configuração de página, portanto, para um melhor resultado, é
	 *  recomendável que todos os documentos de entrada possuam as mesmas dimensões e orientação.
	 *  @param entrada - PDF de entrada, todas as suas páginas serão adicionadas ao PDF de saída
	 *  @param frente - PDF contendo o template das páginas frontais, será adicionado como background a todas as páginas ímpares (frente) do PDF de entrada
	 *  @param verso - PDF contendo o template das páginas traseiras, será adicionado como background a todas as páginas pares (verso) do PDF de entrada
	 *  @param saida - PDF resultante
	 *  @param offset_x_frente - deslocamento horizontal do template 'frente'
	 *  @param offset_y_frente - deslocamento vertical do template 'frente'
	 *  @param offset_x_verso - deslocamento horizontal do template 'verso'
	 *  @param offset_y_verso - deslocamento vertical do template 'verso'
	 *  @throws IOException quando qualquer um dos arquivos não pode ser acessado
	 *  @throws DocumentException quando há algum erro no processamento do PDF
	 *  @return tempo de execução deste método (em ms) */
	public static long merge(File entrada, File frente, File verso, File saida,
			float offset_x_frente, float offset_y_frente, float offset_x_verso, float offset_y_verso) throws IOException, DocumentException {
		
		// Marca o tempo de início de execução
		long start = System.currentTimeMillis();
		
		// Abrindo os arquivos de leitura
		PdfReader reader_cartoes = new PdfReader(entrada.getAbsolutePath());
		PdfReader reader_frente  = new PdfReader(frente .getAbsolutePath());
		PdfReader reader_verso   = new PdfReader(verso  .getAbsolutePath());
		
		// Abrindo o PDF de saída
		PdfStamper pdf_saida = new PdfStamper(reader_cartoes, new FileOutputStream(saida));
		
		// Recuperando a primeira página de cada template
		PdfImportedPage impar = pdf_saida.getImportedPage(reader_frente,1);
		PdfImportedPage par   = pdf_saida.getImportedPage(reader_verso ,1);
		
		// Copia todas as páginas de 'cartoes' para 'saida'...
		for (int i=1; i<= reader_cartoes.getNumberOfPages(); i++) {
			
			// ...adicionando o background do 'verso', para páginas pares ou 
			if (i % 2 == 0)
				pdf_saida.getUnderContent(i).addTemplate(par,offset_x_verso,offset_y_verso);
			
			// ...'frente' para as páginas ímpares
			else
				pdf_saida.getUnderContent(i).addTemplate(impar,offset_x_frente,offset_y_frente);
		    
		}
		
		// Este método fecha TODOS os arquivos
		pdf_saida.close();
		
		// Retorna o tempo de execução
		return (System.currentTimeMillis() - start);
	}
	
	/** Adiciona um background do arquivo 'template' em todas as páginas de 'entrada' e escreve o documento resultante em 'saida'.
	 *  É possível também fazer ajustes de deslocamento vertical e horizontal do template via parâmetros 'offset'.
	 *  Este método é compatível com qualquer configuração de página, portanto, para um melhor resultado, é
	 *  recomendável que todos os documentos de entrada possuam as mesmas dimensões e orientação.
	 *  @param entrada - PDF de entrada, todas as suas páginas serão adicionadas ao PDF de saída
	 *  @param template - PDF contendo o template, será adicionado como background a todas as páginas do PDF de entrada
	 *  @param saida - PDF resultante
	 *  @param offset_x - deslocamento horizontal do template
	 *  @param offset_y - deslocamento vertical do template
	 *  @throws IOException quando qualquer um dos arquivos não pode ser acessado
	 *  @throws DocumentException quando há algum erro no processamento do PDF
	 *  @return tempo de execução deste método (em ms) */
	public static long merge(File entrada, File template, File saida, float offset_x, float offset_y) throws IOException, DocumentException {
		
		// Marca o tempo de início de execução
		long start = System.currentTimeMillis();
		
		// Abrindo os arquivos de leitura
		PdfReader reader_cartoes  = new PdfReader(entrada .getAbsolutePath());
		PdfReader reader_template = new PdfReader(template.getAbsolutePath());
		
		// Abrindo o PDF de saída
		PdfStamper pdf_saida = new PdfStamper(reader_cartoes, new FileOutputStream(saida));
		
		// Recuperando a primeira página do template
		PdfImportedPage template_page = pdf_saida.getImportedPage(reader_template,1);
		
		// Copia todas as páginas de 'cartoes' para 'saida', adicionando o 'template' ao fundo
		for (int i=1; i<= reader_cartoes.getNumberOfPages(); i++)
			pdf_saida.getUnderContent(i).addTemplate(template_page,offset_x,offset_y);
		
		// Este método fecha TODOS os arquivos
		pdf_saida.close();
		
		// Retorna o tempo de execução
		return (System.currentTimeMillis() - start);
	}
	
	
	
	
	
	public static long split(File pdf, File dir_saida) {
		
		// Marca o tempo de início de execução
		long start = System.currentTimeMillis();
		
		
		
		// Retorna o tempo de execução
		return (System.currentTimeMillis() - start);
	}
	
	private static void split() throws Exception {
		
		PdfStamper stamper;
		PdfReader cartoes = new PdfReader("pdf/saida/saida.pdf");
		
		int paginas  = cartoes.getNumberOfPages();
		int max_pag  = 2000;
		int impressas;
		
		for (impressas = 0; impressas < paginas; ) {
			
			String selecao = String.format("%d-%d",impressas+1,impressas += max_pag);
			
			cartoes.selectPages(selecao);
			stamper = new PdfStamper(cartoes, new FileOutputStream("pdf/saida/" + selecao + ".pdf"));
			stamper.close();
			cartoes = new PdfReader("pdf/saida/saida.pdf");
			
		}
		
	}
	
}
