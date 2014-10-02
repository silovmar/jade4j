package de.neuland.jade4j.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.TestFileHelper;
import de.neuland.jade4j.expression.JexlExpressionHandler;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class JadeExceptionTest {

	@Test
	public void test() throws Exception {
		String errorJade = TestFileHelper.getCompilerResourcePath("exceptions/error.jade");
		String exceptionHtml = TestFileHelper.getCompilerResourcePath("exceptions/error.html");
        JexlExpressionHandler expressionHandler = new JexlExpressionHandler();
        try {
            Jade4J.render(errorJade, new HashMap<String, Object>(), expressionHandler);
			fail();
		} catch (JadeException e) {
			assertTrue(e.getMessage().startsWith("unable to evaluate [non.existing.query()]"));
			assertEquals(9, e.getLineNumber());
			assertEquals(errorJade, e.getFilename());
			String expectedHtml = readFile(exceptionHtml);
			String html = e.toHtmlString("<html><head><title>broken", expressionHandler);
			assertEquals(removeAbsolutePath(expectedHtml), removeAbsolutePath(html));
		}
	}

	private String removeAbsolutePath(String html) {
		html = html.replaceAll("(<h2>In ).*(compiler/exceptions/error\\.jade at line 9\\.</h2>)", "$1\\.\\./$2");
		return html;
	}

	private String readFile(String fileName) {
		try {
			return FileUtils.readFileToString(new File(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
