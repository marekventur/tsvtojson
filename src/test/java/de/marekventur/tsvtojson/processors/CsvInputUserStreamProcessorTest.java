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

import de.marekventur.tsvtojson.DataCollection;
import de.marekventur.tsvtojson.MockDataCollection;

public class CsvInputUserStreamProcessorTest {

	private enum TestInputColumns implements CsvColumns {
		COL1, COL2;
	}
	
	@Test
	public void test() throws IOException {
		CsvInputUserStreamProcessor<TestInputColumns> processor  = new CsvInputUserStreamProcessor<TestInputColumns>() {
		
			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}

		
			@Override
			protected void processRow(Map<TestInputColumns, String> row,
					OutputStream output) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(output);
				writer.write(row.get(TestInputColumns.COL2));
				writer.write(" - ");
				writer.write(row.get(TestInputColumns.COL1));
				writer.write(" # ");
				writer.close();
				
			}


			@Override
			protected String getOutputFilePathName() {
				return "test.tsv";
			}
			
		};
		
		InputStream input = new ByteArrayInputStream("Row1Col1\tRow1Col2\nRow2Col1\tRow2Col2".getBytes());
		MockDataCollection output = new MockDataCollection();
		
		processor.process(input, output);
		
		output.close();
		
		String expected = "Row1Col2 - Row1Col1 # Row2Col2 - Row2Col1 # ";
		
		assertEquals(expected, output.getData("test.tsv"));
	}
	
	@Test
	public void testNullDetection() throws IOException {
		CsvInputUserStreamProcessor<TestInputColumns> processor  = new CsvInputUserStreamProcessor<TestInputColumns>() {
	
			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}

			@Override
			protected void processRow(Map<TestInputColumns, String> row,
					OutputStream output) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(output);
				writer.write(row.get(TestInputColumns.COL1) == null ? "yes" : "no");
				writer.write(" - ");
				writer.write(row.get(TestInputColumns.COL2) == null ? "yes" : "no");
				writer.write(" # ");
				writer.close();				
			}

			@Override
			protected String getOutputFilePathName() {
				return "test.tsv";
			}
			
		};
		
		InputStream input = new ByteArrayInputStream("Row1Col1\tNULL\nNULL\tRow2Col2".getBytes());
		MockDataCollection output = new MockDataCollection();
		
		processor.process(input, output);
		
		output.close();
		
		String expected = "no - yes # yes - no # ";
		
		assertEquals(expected, output.getData("test.tsv"));
	}

}
