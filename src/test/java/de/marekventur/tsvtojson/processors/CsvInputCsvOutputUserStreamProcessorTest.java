package de.marekventur.tsvtojson.processors;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

import de.marekventur.tsvtojson.MockDataCollection;

import au.com.bytecode.opencsv.CSVWriter;

public class CsvInputCsvOutputUserStreamProcessorTest {

	private enum TestInputColumns implements CsvColumns {
		COL1, COL2;
	}
	
	private enum TestOutputColumns implements CsvColumnsWithHeaderNames {
		COL2 ("col 2"), COL1("col 1");
		
		private final String headerName;

		private TestOutputColumns(String headerName) {
			this.headerName = headerName;
		}

		public String getHeaderName() {
			return headerName;
		}
	}
	
	@Test
	public void test() throws IOException {
		CsvInputCsvOutputUserStreamProcessor<TestInputColumns, TestOutputColumns> processor  = new CsvInputCsvOutputUserStreamProcessor<TestInputColumns, TestOutputColumns>() {
			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}

			@Override
			protected TestOutputColumns[] getOutputColumns() {
				return TestOutputColumns.values();
			}

			@Override
			protected void processRow(Map<TestInputColumns, String> row,
					CSVWriter writer) throws IOException {
				writer.writeNext(new String[] {row.get(TestInputColumns.COL2), row.get(TestInputColumns.COL1)});
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
		
		String expected = "\"col 2\"\t\"col 1\"\n\"Row1Col2\"\t\"Row1Col1\"\n\"Row2Col2\"\t\"Row2Col1\"\n";
		
		assertEquals(expected, output.getData("test.tsv"));
	}

}
