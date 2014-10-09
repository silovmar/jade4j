package de.neuland.jade4j.exceptions;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.expression.JexlExpressionHandler;
import de.neuland.jade4j.template.TemplateLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JadeException extends RuntimeException {

	private static final long serialVersionUID = -8189536050437574552L;
	private String filename;
	private int lineNumber;
	private TemplateLoader templateLoader;

	public JadeException(String message, String filename, int lineNumber, TemplateLoader templateLoader, Throwable e) {
		super(message, e);
		this.filename = filename;
		this.lineNumber = lineNumber;
		this.templateLoader = templateLoader;
	}

	public JadeException(String message) {
		super(message);
	}

	public String getFilename() {
		return filename;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public List<String> getTemplateLines() {
		try {
			List<String> result = new ArrayList<String>();
			Reader reader = templateLoader.getReader(filename);
			BufferedReader in = new BufferedReader(reader);
			String line;
			while ((line = in.readLine()) != null) {
				result.add(line);
			}
			return result;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return getClass() + " " + getFilename() + ":" + getLineNumber() + "\n" + getMessage();
	}

	public String toHtmlString() {
		return toHtmlString(null);
	}

    //TODO silovsky: metoda volana ze spring-jade
	public String toHtmlString(String generatedHtml) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("filename", filename);
		model.put("linenumber", lineNumber);
		model.put("message", getMessage());
		model.put("lines", getTemplateLines());
		model.put("exception", getName());
		if (generatedHtml != null) {
			model.put("html", generatedHtml);
		}

		try {
			URL url = JadeException.class.getResource("/error.jade");
            //TODO silovsky: upravit - musi se prevolat existujici expression handler
			return Jade4J.render(url, model, true, new JexlExpressionHandler());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getName() {
		return this.getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim();
	}
}
