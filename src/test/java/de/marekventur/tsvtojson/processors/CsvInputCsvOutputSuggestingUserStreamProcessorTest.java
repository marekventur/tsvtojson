package de.marekventur.tsvtojson.processors;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.junit.Test;

import de.marekventur.tsvtojson.MockDataCollection;

import au.com.bytecode.opencsv.CSVWriter;

public class CsvInputCsvOutputSuggestingUserStreamProcessorTest {

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
	
	private enum TestOutputColumnsThatDontMatchUp implements CsvColumnsWithHeaderNames {
		COL3 ("col 3"), COL1("col 1");
		
		private final String headerName;

		private TestOutputColumnsThatDontMatchUp(String headerName) {
			this.headerName = headerName;
		}

		public String getHeaderName() {
			return headerName;
		}
	}
	
	@Test
	public void test() throws IOException {		
		InputStream input = new ByteArrayInputStream("Row1Col1\tRow1Col2\nRow2Col1\tRow2Col2".getBytes());
		MockDataCollection output = new MockDataCollection();
		
		getTestProcessor().process(input, output);
		
		output.close();
		
		String expected = "\"col 2\"\t\"col 1\"\n\"Row1Col2\"\t\"Row1Col1\"\n\"Row2Col2\"\t\"Row2Col1\"\n";
		
		assertEquals(expected, output.getData("test.tsv"));
	}
	
	@Test
	public void testNullFields() throws IOException {
		InputStream input = new ByteArrayInputStream("Row1Col1\tNULL\nRow2Col1\tRow2Col2".getBytes());
		MockDataCollection output = new MockDataCollection();
		
		getTestProcessor().process(input, output);
		
		output.close();
		
		String expected = "\"col 2\"\t\"col 1\"\n\"\"\t\"Row1Col1\"\n\"Row2Col2\"\t\"Row2Col1\"\n";
		
		assertEquals(expected, output.getData("test.tsv"));
	}
	
	@Test
	public void testColumnsThatDontMatchUp() throws IOException {
		CsvInputCsvOutputSuggestingUserStreamProcessor<TestInputColumns, TestOutputColumnsThatDontMatchUp> processor = new CsvInputCsvOutputSuggestingUserStreamProcessor<TestInputColumns, TestOutputColumnsThatDontMatchUp>() {
			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}

			@Override
			protected TestOutputColumnsThatDontMatchUp[] getOutputColumns() {
				return TestOutputColumnsThatDontMatchUp.values();
			}

			@Override
			protected void processRowWithSuggestion(
					Map<TestInputColumns, String> row, String[] suggestion,
					CSVWriter writer) throws IOException {
				writer.writeNext(suggestion);
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
		
		String expected = "\"col 3\"\t\"col 1\"\n\"\"\t\"Row1Col1\"\n\"\"\t\"Row2Col1\"\n";
		
		assertEquals(expected, output.getData("test.tsv"));
	}
	
	private CsvInputCsvOutputSuggestingUserStreamProcessor<TestInputColumns, TestOutputColumns> getTestProcessor() {
		return new CsvInputCsvOutputSuggestingUserStreamProcessor<TestInputColumns, TestOutputColumns>() {
			@Override
			protected TestInputColumns[] getInputColumns() {
				return TestInputColumns.values();
			}

			@Override
			protected TestOutputColumns[] getOutputColumns() {
				return TestOutputColumns.values();
			}

			@Override
			protected void processRowWithSuggestion(
					Map<TestInputColumns, String> row, String[] suggestion,
					CSVWriter writer) throws IOException {
				writer.writeNext(suggestion);
			}

			@Override
			protected String getOutputFilePathName() {
				return "test.tsv";
			}

		};
	}
}
