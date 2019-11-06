package com.pdf.merge;

import java.io.*;
import com.itextpdf.text.pdf.*;

public class Merge {
	
	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
		
		PdfReader cartoes = new PdfReader("pdf/personaliz_psi2019_1dia.pdf");
		PdfReader frente  = new PdfReader("pdf/modelos/frente.pdf");
		PdfReader verso   = new PdfReader("pdf/modelos/verso.pdf");
		
		PdfStamper saida = new PdfStamper(cartoes, new FileOutputStream("pdf/saida/saida.pdf"));
		
		PdfImportedPage impar = saida.getImportedPage(frente,1);
		PdfImportedPage par   = saida.getImportedPage(verso ,1);
		
		for (int i=1; i<= cartoes.getNumberOfPages(); i++) {
			
			if (i % 2 == 0)
				saida.getUnderContent(i).addTemplate(par,0,-1);
			else
				saida.getUnderContent(i).addTemplate(impar,0,0);
		    
		}
		
		saida.close();
		
		System.out.println(":: Runtime: " + (System.currentTimeMillis() - start) + " ms");
		
		split();
		
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
