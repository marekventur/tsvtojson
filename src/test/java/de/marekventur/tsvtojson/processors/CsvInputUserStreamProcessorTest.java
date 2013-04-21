package de.marekventur.tsvtojson.processors;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Map;

import org.junit.Test;

public class CsvInputUserStreamProcessorTest {

	private enum TestInputColumns implements CsvColumns {
		COL1, COL2;
	}
	
	@Test
	public void test() throws IOException {
		CsvInputUserStreamProcessor<TestInputColumns> processor  = new CsvInputUserStreamProcessor<TestInputColumns>() {
		
			@Override
			protected void processRow(Map<TestInputColumns, String> row, OutputStream output)
					throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(output);
				writer.write(row.get(TestInputColumns.COL2));
				writer.write(" - ");
				writer.write(row.get(TestInputColumns.COL1));
				writer.write(" # ");
				writer.close();
			}

			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}
			
		};
		
		InputStream input = new ByteArrayInputStream("Row1Col1\tRow1Col2\nRow2Col1\tRow2Col2".getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		processor.process(input, output);
		
		String expected = "Row1Col2 - Row1Col1 # Row2Col2 - Row2Col1 # ";
		
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testNullDetection() throws IOException {
		CsvInputUserStreamProcessor<TestInputColumns> processor  = new CsvInputUserStreamProcessor<TestInputColumns>() {
		
			@Override
			protected void processRow(Map<TestInputColumns, String> row, OutputStream output)
					throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(output);
				writer.write(row.get(TestInputColumns.COL1) == null ? "yes" : "no");
				writer.write(" - ");
				writer.write(row.get(TestInputColumns.COL2) == null ? "yes" : "no");
				writer.write(" # ");
				writer.close();
			}

			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}
			
		};
		
		InputStream input = new ByteArrayInputStream("Row1Col1\tNULL\nNULL\tRow2Col2".getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		processor.process(input, output);
		
		String expected = "no - yes # yes - no # ";
		
		assertEquals(expected, output.toString());
	}
	
	@Test(expected=IOException.class)
	public void testExceptionPassThrough() throws IOException {
		CsvInputUserStreamProcessor<TestInputColumns> processor  = new CsvInputUserStreamProcessor<TestInputColumns>() {
		
			@Override
			protected void processRow(Map<TestInputColumns, String> row, OutputStream output)
					throws IOException {
				throw new IOException();
			}

			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}
			
		};
		
		InputStream input = new ByteArrayInputStream("Row1Col1\tRow1Col2\nRow2Col1\tRow2Col2".getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		processor.process(input, output);
	}

}
